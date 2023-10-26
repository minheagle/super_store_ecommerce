package com.shopee.clone.service.order.impl;

import com.shopee.clone.DTO.ResponseData;
import com.shopee.clone.DTO.cart.ProductItemMatchToCart;
import com.shopee.clone.DTO.order.request.*;
import com.shopee.clone.DTO.order.response.OrderDetailResponse;
import com.shopee.clone.DTO.order.response.OrderResponse;
import com.shopee.clone.DTO.product.response.*;
import com.shopee.clone.DTO.promotion.response.TypeDiscountResponse;
import com.shopee.clone.DTO.seller.response.Seller;
import com.shopee.clone.entity.AddressEntity;
import com.shopee.clone.entity.ProductEntity;
import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.entity.cart.CartEntity;
import com.shopee.clone.entity.order.*;
import com.shopee.clone.entity.payment.EDiscountType;
import com.shopee.clone.entity.promotion.PromotionEntity;
import com.shopee.clone.repository.AddressRepository;
import com.shopee.clone.repository.SellerRepository;
import com.shopee.clone.repository.cart.CartRepository;
import com.shopee.clone.repository.order.OrderDetailRepository;
import com.shopee.clone.repository.order.OrderRepository;
import com.shopee.clone.repository.promotion.PromotionRepository;
import com.shopee.clone.service.order.OrderService;
import com.shopee.clone.service.productItem.impl.ProductItemService;
import com.shopee.clone.service.promotion.IPromotionService;
import com.shopee.clone.service.user.UserService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private  CartRepository cartRepository;
    @Autowired
    private IPromotionService promotionService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private  OrderDetailRepository orderDetailRepository;
    @Autowired
    private  SellerRepository sellerRepository;
    @Autowired
    private  AddressRepository addressRepository;
    @Autowired
    private  ModelMapper mapper;
    @Autowired
    private  ProductItemService productItemService;
    @Autowired
    private  UserService userService;
    @Autowired
    private PromotionRepository promotionRepository;

    @Transactional
    @Override
    public ResponseEntity<?> save(OrderRequest orderRequest) {
        try {
            List<OrderResponse> list = new ArrayList<>();
            Optional<UserEntity> userOptional = userService.findUserByID(orderRequest.getUserId());
            Optional<AddressEntity> addressOptional = addressRepository.findById(orderRequest.getAddressId());

            if(userOptional.isPresent() && addressOptional.isPresent()){
//              Chạy vòng lặp để lưu các đơn hàng theo từng shop

                orderRequest.getListOrderBelongToSeller().forEach(o ->{
                    int finalOrderNumber = getFinalOrderNumber();
                    UserEntity user = userOptional.get();
                    AddressEntity address = addressOptional.get();

                    OrderEntity orderEntity = new OrderEntity();
                    if(o.getPromotionId() != null) {
                        Boolean check = promotionService.checkValidUsage(user.getId(), o.getPromotionId(), o.getAmount(), o.getSellerId());
//                    PromotionEntity promotion = promotionRepository.findById(o.getPromotionId())
//                            .orElseThrow(NoSuchElementException::new);
                        if (check) {
                            orderEntity.setPromotionId(o.getPromotionId());
                            TypeDiscountResponse discountResponse = promotionService.getTypeDiscount(o.getPromotionId());
                            if (EDiscountType.DISCOUNT_PERCENT.equals(discountResponse.getDiscountType())) {
                                orderEntity.setDiscount((discountResponse.getDiscountValue() * o.getAmount()) / 100);
                            } else if (EDiscountType.FIXED_AMOUNT.equals(discountResponse.getDiscountType())) {
                                if (discountResponse.getDiscountValue() < o.getAmount()) {
                                    orderEntity.setDiscount(discountResponse.getDiscountValue().doubleValue());
                                } else {
                                    orderEntity.setDiscount(o.getAmount());
                                }
                            } else if (EDiscountType.FREE_SHIP.equals(discountResponse.getDiscountType())) {
                                if (discountResponse.getDiscountValue() == null) {
                                    orderEntity.setDiscount(o.getShipMoney());
                                } else {
                                    if (discountResponse.getDiscountValue() < o.getShipMoney()) {
                                        orderEntity.setDiscount(discountResponse.getDiscountValue().doubleValue());
                                    } else {
                                        orderEntity.setDiscount(o.getShipMoney());
                                    }
                                }
                            }
                            promotionService.minusUsage(user.getId(), o.getPromotionId(), o.getAmount(), o.getSellerId());
                        }
                    }
                    orderEntity.setUser(user);
                    orderEntity.setAddress(address);
                    orderEntity.setShipMoney(o.getShipMoney());
                    orderEntity.setOrderNumber(finalOrderNumber);
                    orderEntity.setDate(Date.from(Instant.now()));
                    orderEntity.setPaymentStatus(orderRequest.getPaymentStatus());
//                  True là thanh toán rồi
                    if(orderEntity.getPaymentStatus()){
                        orderEntity.setStatus(EOrder.Awaiting_Payment);
                    }else orderEntity.setStatus(EOrder.Pending);

                    Optional<SellerEntity> sellerOptional = sellerRepository.findById(o.getSellerId());
                    if(sellerOptional.isPresent()){
                        SellerEntity seller = sellerOptional.get();
                        orderEntity.setSeller(seller);

                    OrderEntity order =  orderRepository.save(orderEntity);
                    List<CartEntity> cartList = cartRepository.findAllById(o.getCartId());
//                  Chuyển giỏ hàng thành chi tiết đơn hàng
                    List<OrderDetailEntity> orderDetailEntityList =
                            cartList.stream().map(c-> {
                                OrderDetailEntity orderDetail = new OrderDetailEntity();
                                orderDetail.setProductItems(c.getProductItems());
//                              giảm số lượng sản phẩm tồn kho
                                productItemService.minusQuantityInStock(
                                        orderDetail.getProductItems().getPItemId(),
                                        c.getQuantity());
                                orderDetail.setQuantity(c.getQuantity());
                                orderDetail.setUnitPrice(c.getProductItems().getPrice());
                                orderDetail.setOrder(order);
                                return orderDetail;
                            }).toList();

//                  Xóa tất cả giỏ hàng đã mua
                    cartRepository.deleteAll(cartList);
                    List<OrderDetailEntity> orderDetails =
                            orderDetailRepository.saveAll(orderDetailEntityList);
                    order.setOrderDetails(orderDetails);
                    OrderResponse response = convertOrderEntityToOrderResponse(order);
                    list.add(response);
                    }
                });

                ResponseData<Object> data = ResponseData.builder()
                        .data(list)
                        .build();
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Save order Success")
                                        .results(data)
                                        .build()
                        );

            }
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Tham so khong ton tai!")
                            .results("")
                            .results("")
                            .build()
                    );

            }
        catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
                    );
        }

    }
    @Override
    @Transactional
    public void changeStatusWhenCallPayment(Integer orderNumber, Boolean bl){
        List<OrderEntity> orderList = orderRepository.findAllByOrderNumber(orderNumber);
        int newOrderNumber = getFinalOrderNumber();
        orderList.forEach(o->{
            if(bl){
                o.setStatus(EOrder.Transferred);
            }else{
                o.setStatus(EOrder.Pending);
                o.setOrderNumber(newOrderNumber);
            }
        })
        ;
        orderRepository.saveAll(orderList);
    }


    private int getFinalOrderNumber() {
        int orderNumber = randomOrderNumber();
        while(checkOrderNumber(orderNumber)){
            orderNumber = randomOrderNumber();
        }
        return orderNumber;
    }

    @Override
    public ResponseEntity<?> getHistoryOrder(Long userId) {
        try {
            Optional<UserEntity> userOptional = userService.findUserByID(userId);
            if(userOptional.isPresent()){
                UserEntity user = userOptional.get();
                List<OrderEntity> orderList = orderRepository.findAllByUser(user);
//              Trả về Json
                List<OrderResponse> responses = new ArrayList<>();

                orderList.forEach(order->{
                    OrderResponse orderResponse = convertOrderEntityToOrderResponse(order);
                    responses.add(orderResponse);
                });

                ResponseData<Object> data = ResponseData.builder().data(responses).build();

                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("Show history Order success!")
                        .results(data)
                        .build());
            }

        }
        catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
                    );
        }
        return null;
    }

    private OrderResponse convertOrderEntityToOrderResponse(OrderEntity order) {
//              Tạo từng đơn hàng theo Json
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());

        orderResponse.setSeller(mapper.map(order.getSeller(),Seller.class));
        orderResponse.setPayment(order.getPaymentStatus());
        orderResponse.setOrderNumber(order.getOrderNumber());
        orderResponse.setDiscount(order.getDiscount());
        Double amount = amount(order.getOrderDetails());
        orderResponse.setAmount(amount);
        orderResponse.setTotal(amount + order.getShipMoney() - order.getDiscount());
        orderResponse.setDate(order.getDate());
        orderResponse.setPayment(order.getPaymentStatus());
        orderResponse.setShipMoney(order.getShipMoney());
        orderResponse.setStatus(order.getStatus().name());
        Optional<AddressEntity> address = addressRepository.findById(order.getAddress().getId());
        address.ifPresent(addressEntity -> orderResponse.setDeliveryAddress(addressEntity.getAddressName()));
        List<OrderDetailResponse> orderDetailResponseList =
        order.getOrderDetails().stream().map(this::convertOrderDetailToODResponse).toList();
        orderResponse.setOrderDetailList(orderDetailResponseList);
        return orderResponse;
    }

    private Double amount(List<OrderDetailEntity> orderDetails) {
        AtomicReference<Double> amount = new AtomicReference<>(0D);
        orderDetails.forEach(oD ->{
            amount.updateAndGet(v -> v + oD.getUnitPrice() * oD.getQuantity());
        });
        return amount.get();
    }

    private OrderDetailResponse convertOrderDetailToODResponse(OrderDetailEntity x) {
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        orderDetailResponse.setId(x.getId());
        orderDetailResponse.setQuantity(x.getQuantity());
        orderDetailResponse.setUnitPrice(x.getUnitPrice());
        orderDetailResponse.setProduct(mapper.map(x.getProductItems().getProduct(), ProductMatchToCartResponse.class));

        ProductItemMatchToCart productItemDTO = mapper.map(x.getProductItems(), ProductItemMatchToCart.class);

        List<OptionTypeDTO> typeList = new ArrayList<>();
        x.getProductItems()
                .getOptionValues()
                .forEach(v ->
                {
                    OptionTypeDTO type = mapper.map(v.getOptionType(), OptionTypeDTO.class);
                    type.setOptionValue(mapper.map(v, OptionValueDTO.class));
                    typeList.add(type);
                });

        orderDetailResponse.getProduct().setProductItemResponse(productItemDTO);
        orderDetailResponse.getProduct().getProductItemResponse().setOptionTypes(typeList);
        return orderDetailResponse;
    }

    @Override
    public ResponseEntity<?> getOrder(Long orderId) {
        try {
            Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
            if(orderEntity.isPresent()){
                OrderEntity order = orderEntity.get();
//              Trả về Json
                OrderResponse orderResponse = convertOrderEntityToOrderResponse(order);

                ResponseData<Object> data = ResponseData.builder().data(orderResponse).build();

                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("Get Order success!")
                        .results(data)
                        .build());
            }
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Order not exist!")
                            .results("")
                            .build()
                    );

        }
        catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
                    );
        }
    }

    @Override
    public ResponseEntity<?> cancelOrder(Long orderId) {
        try {
            Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
            if(orderEntity.isPresent()){
                OrderEntity order = orderEntity.get();
                if(order.getStatus().equals(EOrder.Pending) || order.getStatus().equals(EOrder.Awaiting_Payment)|| order.getStatus().equals(EOrder.Transferred)){
                    order.setStatus(EOrder.Cancelled);
                    if(order.getPromotionId()!=null){
                        promotionService.plusUsage(order.getUser().getId(),order.getPromotionId(),amount(order.getOrderDetails()),order.getSeller().getId());
                    }
                   //                Trả lại số lượng cho order
                    order.getOrderDetails().forEach(oD ->{
                        productItemService.plusQuantityInStock(oD.getProductItems().getPItemId(),oD.getQuantity());
                    });
                    orderRepository.save(order);
//              Trả về Json
                    OrderResponse orderResponse = convertOrderEntityToOrderResponse(order);

                    ResponseData<Object> data = ResponseData.builder().data(orderResponse).build();

                    return ResponseEntity.ok().body(ResponseObject
                            .builder()
                            .status("SUCCESS")
                            .message("Cancel Order success!")
                            .results(data)
                            .build());
                }

                return ResponseEntity.badRequest().body(ResponseObject
                        .builder()
                        .status("Fail")
                        .message("You can't cancel order because order do ship!")
                        .results("")
                        .build());
            }
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Order not exist!")
                            .results("")
                            .build()
                    );

        }
        catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
                    );
        }
    }

    @Override
    public ResponseEntity<?> confirmOrder(Long sellerId, Long orderId) {
        try {
            Optional<OrderEntity> orderOptional = orderRepository.findById(orderId);
            if(orderOptional.isPresent()){
                OrderEntity order = orderOptional.get();

                if(order.getStatus().equals(EOrder.Pending) || order.getStatus().equals(EOrder.Awaiting_Payment) || order.getStatus().equals(EOrder.Transferred)) {
                    order.setConfirmDate(Date.from(Instant.now()));
                    order.setStatus(EOrder.Processing);
                    orderRepository.save(order);
//              Trả về Json
//                    ResponseData<Object> data = ResponseData.builder().data().build();
//
                    return ResponseEntity.ok().body(getOrderBySeller(sellerId));
                }
                return ResponseEntity
                        .badRequest()
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("The status of the order is incorrect!")
                                .results("")
                                .build()
                        );
            }
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Order not exist!")
                            .results("")
                            .build()
                    );

        }
        catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
                    );
        }
    }
    @Override
    public Double getTotalByOrderNumber(Integer orderNumber){
        List<OrderEntity> orderList = orderRepository.findAllByOrderNumber(orderNumber);
        AtomicReference<Double> total = new AtomicReference<>(0D);
        orderList.forEach(o->{
           total.updateAndGet(v -> v + (amount(o.getOrderDetails()) + o.getShipMoney() - o.getDiscount()));
        });
        return total.get();
    }
    private int randomOrderNumber() {
        Random random = new Random();
        int min = 100; // Số nhỏ nhất có 5 chữ số
        int max = 99999; // Số lớn nhất có 5 chữ số
        return random.nextInt(max - min + 1) + min;
    }

    private Boolean checkOrderNumber(int orderNumber){
        Optional<OrderEntity> order =  orderRepository.findByOrderNumber(orderNumber);
        return order.isPresent();
    }
    @Override
    public ResponseEntity<?> getOrderBySeller(Long sellerId) {
        try {
            Optional<SellerEntity> sellerOptional = sellerRepository.findById(sellerId);
            if(sellerOptional.isPresent()){
                SellerEntity seller = sellerOptional.get();

                List<OrderEntity> orderList =  orderRepository.findAllBySeller(seller);

                List<OrderResponse> list = new ArrayList<>();
                orderList.forEach(o->{
//              Trả về Json
                OrderResponse orderResponse = convertOrderEntityToOrderResponse(o);
                list.add(orderResponse);
                });

                ResponseData<Object> data = ResponseData.builder().data(list).build();

                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("Get Order by Seller success!")
                        .results(data)
                        .build());
            }

            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Seller not exist!")
                            .results("")
                            .build()
                    );

        }
        catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
                    );
        }
    }

    @Override
    public ResponseEntity<?> rejectionOrder(Long sellerId, Long orderId) {
        try {
            Optional<OrderEntity> orderOptional = orderRepository.findById(orderId);
            if(orderOptional.isPresent()){
                OrderEntity order = orderOptional.get();

                if(order.getStatus().equals(EOrder.Pending)) {
                    order.setConfirmDate(Date.from(Instant.now()));
                    order.setStatus(EOrder.Rejection);
                    if(order.getPromotionId()!=null){
                        promotionService.plusUsage(order.getUser().getId(),order.getPromotionId(),amount(order.getOrderDetails()), sellerId);
                    }
                    order.getOrderDetails().forEach(oD ->{
                        productItemService.plusQuantityInStock(oD.getProductItems().getPItemId(),oD.getQuantity());
                    });
                    orderRepository.save(order);
                    return getOrderBySeller(sellerId);
//
                }
                return ResponseEntity
                        .badRequest()
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("The status of the order is incorrect!")
                                .results("")
                                .build()
                        );
            }
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Order not exist!")
                            .results("")
                            .build()
                    );

        }
        catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
                    );
        }
    }

    @Override
    public ResponseEntity<?> getAllOrderConfirm() {
            try {
                List<OrderEntity> ordersToday = summarizeOrdersYesterday();
                List<OrderResponse> list = new ArrayList<>();
                ordersToday.forEach(o->{
//              Trả về Json
                    OrderResponse orderResponse = convertOrderEntityToOrderResponse(o);
                    list.add(orderResponse);
                });

                ResponseData<Object> data = ResponseData.builder().data(list).build();

                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("Get all order yesterday success!")
                        .results(data)
                        .build());
            }
            catch (Exception e){
                return ResponseEntity
                        .badRequest()
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message(e.getMessage())
                                .results("")
                                .build()
                        );
            }
    }

    @Override
    public int randomOrder() {
        return getFinalOrderNumber();
    }

    public List<OrderEntity> summarizeOrdersYesterday() {
        // Lấy ngày hôm qua
        Date yesterday = getYesterday();

        // Lấy ngày bắt đầu của hôm qua (00:00:00)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(yesterday);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfYesterday = calendar.getTime();

        // Lấy ngày kết thúc của hôm qua (23:59:59.999)
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endOfYesterday = calendar.getTime();

        // Tìm các đơn hàng có ConfirmDate trong khoảng từ startOfYesterday đến endOfYesterday và trạng thái "Processing"
        return orderRepository.findByConfirmDateBetweenAndStatus(startOfYesterday, endOfYesterday, EOrder.Processing);
    }


    private Date getYesterday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 0); // Lấy ngày hôm qua
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
}
    @Override
    public ResponseEntity<?> callApi() {
//      Trả về Json
        ResponseData<Object> data = ResponseData.builder().data(callApiDeliveryEveryday()).build();
        return ResponseEntity.ok().body(ResponseObject
                    .builder()
                    .status("SUCCESS")
                    .message("call api success!")
                    .results(data)
                    .build());

    }

    @Override
    public List<Long> getTopSellingProduct() {
        List<ProductEntity> list = orderDetailRepository.findMostSoldProduct();
        return list.stream().map(ProductEntity::getProductId).toList();
    }

    @Override
    public ResponseEntity<?> getAllOrderWithShopOnDay(Long sellerId) {

        List<OrderEntity> orderEntityList = orderRepository.findOrdersBySellerAndDate(sellerId);
        List<OrderResponse> responses = orderEntityList.stream().map(this::convertOrderEntityToOrderResponse).toList();
        ResponseData<Object> data = ResponseData.builder().data(responses).build();
        return ResponseEntity.ok().body(ResponseObject
                .builder()
                .status("SUCCESS")
                .message("call api success!")
                .results(data)
                .build());
    }

    @Override
    public Double getTotalOnDay() {
        LocalDate today = LocalDate.now();
        Date startDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant());

        List<EOrder> statuses = Arrays.asList(EOrder.Shipped, EOrder.Processing, EOrder.Pending, EOrder.Awaiting_Payment, EOrder.Transferred);
        List<OrderEntity> orders = orderRepository.findByStatusInAndDateBetween(statuses, startDate, endDate);

        double totalAmount = 0.0;

        for (OrderEntity order : orders) {
            // Tính tổng tiền từ các đơn hàng
            totalAmount += amount(order.getOrderDetails()) + order.getShipMoney() - order.getDiscount();
        }

        return totalAmount;
    }

    @Override
    public Double getTotalOnMonth(int year, int month) {
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());

        Date startDate = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant());

        List<EOrder> statuses = Arrays.asList(EOrder.Shipped, EOrder.Processing, EOrder.Pending, EOrder.Awaiting_Payment, EOrder.Transferred);
        List<OrderEntity> orders = orderRepository.findByStatusInAndDateBetween(statuses, startDate, endDate);

        double totalAmount = 0.0;

        for (OrderEntity order : orders) {
            // Tính tổng tiền từ các đơn hàng
            totalAmount += amount(order.getOrderDetails()) + order.getShipMoney() - order.getDiscount();
        }

        return totalAmount;
    }

    @Override
    public Double getTotalWithSellerOnDay(Long sellerId) {
        LocalDate today = LocalDate.now();
        Date startDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant());

        List<EOrder> statuses = Arrays.asList(EOrder.Shipped, EOrder.Processing, EOrder.Pending, EOrder.Awaiting_Payment, EOrder.Transferred);
        List<OrderEntity> orders = orderRepository.findBySeller_IdAndStatusInAndDateBetween(sellerId, statuses, startDate, endDate);

        double totalAmount = 0.0;

        for (OrderEntity order : orders) {
            // Tính tổng tiền từ các đơn hàng
            totalAmount += amount(order.getOrderDetails()) + order.getShipMoney() - order.getDiscount();
        }

        return totalAmount;
    }

    @Override
    public Double getTotalWithSellerOnMonth(Long sellerId, int year, int month) {
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());

        Date startDate = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant());

        List<EOrder> statuses = Arrays.asList(EOrder.Shipped, EOrder.Processing, EOrder.Pending, EOrder.Awaiting_Payment, EOrder.Transferred);
        List<OrderEntity> orders = orderRepository.findBySeller_IdAndStatusInAndDateBetween(sellerId, statuses, startDate, endDate);

        double totalAmount = 0.0;

        for (OrderEntity order : orders) {
            // Tính tổng tiền từ các đơn hàng
            totalAmount += amount(order.getOrderDetails()) + order.getShipMoney() - order.getDiscount();
        }

        return totalAmount;
    }

//    @Override
//    public List<OrderEntity> findAllByOrderNumber(Integer orderNumber) {
//        return orderRepository.findAllByOrderNumber(orderNumber);
//    }
//
//    @Override
//    public List<OrderEntity> findAllBySellerOnMonth(Long sellerId) {
//        return null;
//    }

//        @Scheduled(fixedRate = 1810000) // Lên lịch chạy mỗi 2 phút (120,000 milliseconds)
//    public void sayHello() {
//
//            System.out.println("====================");
//            System.out.println(getOrderBySellerAndStatusProcessing(2L));
//    }
    @Scheduled(cron = "0 0 0 * * ?") // Chạy sau 12h đêm hàng ngày
    public RawEcommerceOrderCreate callApiDeliveryEveryday(){
        List<OrderEntity> orderEntityList = summarizeOrdersYesterday();
        if(!orderEntityList.isEmpty()){
//            Tạo một request để gọi qua api delivery
            RawEcommerceOrderCreate rawEcommerceOrderCreate = new RawEcommerceOrderCreate();

            List<SellerEntity> sellerList = new ArrayList<>();
            List<RawEcommerceRequest> rawEcommerceRequestList = new ArrayList<>();
            orderEntityList.forEach(o->{
                AtomicReference<Double> total = new AtomicReference<>(0D);
                SellerEntity seller = o.getSeller();
                if(!sellerList.contains(seller)){
                    sellerList.add(seller);
                    RawEcommerceRequest rawEcommerceRequest = new RawEcommerceRequest();
                    PickupInformationRequest pickupInformationRequest = new PickupInformationRequest();
                    pickupInformationRequest.setPickupAddress(seller.getStoreAddress());
                    pickupInformationRequest.setShopId(seller.getId());
                    pickupInformationRequest.setShopName(seller.getStoreName());
//              Truyền sdt của shop
                    pickupInformationRequest.setPhoneContact(seller.getStorePhoneNumber());
//              Truyền thông tin của shop
                    rawEcommerceRequest.setPickupInformationRequest(pickupInformationRequest);
                    List<DeliveryInformationRequest> deliveryInformationRequests = new ArrayList<>();
                    DeliveryInformationRequest deliveryInformationRequest = new DeliveryInformationRequest();
                    deliveryInformationRequest.setDeliveryAddress(o.getAddress().getAddressName());
                    deliveryInformationRequest.setOrderDate(o.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                    deliveryInformationRequest.setOrderNumber(o.getId().toString());
                    deliveryInformationRequest.setRecipientName(o.getUser().getFullName());
                    deliveryInformationRequest.setPhoneNumber(o.getUser().getPhone());
                    deliveryInformationRequest.setEmail(o.getUser().getEmail());
                    deliveryInformationRequest.setPaymentSt(o.getPaymentStatus());
                    deliveryInformationRequest.setNoteTimeRecipient(o.getNoteTimeRecipient());
                    List<ItemTransportRequest> itemTransportRequestList = new ArrayList<>();
                    o.getOrderDetails().forEach(oD->{
                        ItemTransportRequest item = new ItemTransportRequest();
                        item.setQuantity(oD.getQuantity());
                        item.setProductName(oD.getProductItems().getProduct().getProductName());
                        item.setUnitPrice(oD.getUnitPrice());
                        total.updateAndGet(v -> v + item.getUnitPrice() * item.getQuantity());
                        itemTransportRequestList.add(item);
                    });
                    deliveryInformationRequest.setItemTransportRequestList(itemTransportRequestList);
                    deliveryInformationRequests.add(deliveryInformationRequest);
                    rawEcommerceRequest.setDeliveryInformationRequestList(deliveryInformationRequests);

                    if(rawEcommerceRequest.getTotalAmount()==null){
                        Double amount=0D;
                        rawEcommerceRequest.setTotalAmount(total.get()+ amount);
                    }else{
                        rawEcommerceRequest.setTotalAmount(total.get()+ rawEcommerceRequest.getTotalAmount());
                    }
                    rawEcommerceRequestList.add(rawEcommerceRequest);
                }else {
                    rawEcommerceRequestList.forEach(r ->{
                        if(r.getPickupInformationRequest().getShopId().equals(seller.getId())){
                            DeliveryInformationRequest deliveryInformationRequest = new DeliveryInformationRequest();
                            deliveryInformationRequest.setDeliveryAddress(o.getAddress().getAddressName());
                            deliveryInformationRequest.setOrderDate(o.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                            deliveryInformationRequest.setOrderNumber(o.getId().toString());
                            deliveryInformationRequest.setRecipientName(o.getUser().getFullName());
                            deliveryInformationRequest.setPhoneNumber(o.getUser().getPhone());
                            deliveryInformationRequest.setEmail(o.getUser().getEmail());
                            deliveryInformationRequest.setPaymentSt(o.getPaymentStatus());
                            deliveryInformationRequest.setNoteTimeRecipient(o.getNoteTimeRecipient());
                            List<ItemTransportRequest> itemTransportRequestList = new ArrayList<>();
                            o.getOrderDetails().forEach(oD->{
                                ItemTransportRequest item = new ItemTransportRequest();
                                item.setQuantity(oD.getQuantity());
                                item.setProductName(oD.getProductItems().getProduct().getProductName());
                                item.setUnitPrice(oD.getUnitPrice());
                                total.updateAndGet(v -> v + item.getUnitPrice() * item.getQuantity());
                                itemTransportRequestList.add(item);
                            });
                            deliveryInformationRequest.setItemTransportRequestList(itemTransportRequestList);
                            r.setTotalAmount(total.get()+ r.getTotalAmount());

                            r.getDeliveryInformationRequestList().add(deliveryInformationRequest);

                        }
                    });
                }
//          Thay đổi trạng thái của order
                o.setStatus(EOrder.Shipped);
                orderRepository.save(o);
            });

            rawEcommerceOrderCreate.setRawEcommerceRequestList(rawEcommerceRequestList);

            restTemplate.postForObject("https://deliverysystembe-production.up.railway.app/api/v1/delivery/receive-order", rawEcommerceOrderCreate, Object.class);
            return rawEcommerceOrderCreate;
        }
        return null;
//
    }

    public List<Long> findTopUsersByOrderCountInCurrentMonth() {
        // Lấy tháng hiện tại
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();

        // Truyền giá trị tháng hiện tại vào phương thức repository
        return orderRepository.findTopUserIdsByOrderCountInMonth(currentMonth);
    }

    @Override
    public ResponseEntity<?> getOrderBySellerAndStatusPending(Long sellerId) {
        try {
            Optional<SellerEntity> sellerOptional = sellerRepository.findById(sellerId);
            if(sellerOptional.isPresent()){
                SellerEntity seller = sellerOptional.get();

                List<OrderEntity> orderList =  orderRepository.findAllBySellerAndStatusIn(seller, Arrays.asList(EOrder.Pending, EOrder.Awaiting_Payment,EOrder.Transferred));

                List<OrderResponse> list = new ArrayList<>();
                orderList.forEach(o->{
//              Trả về Json
                    OrderResponse orderResponse = convertOrderEntityToOrderResponse(o);
                    list.add(orderResponse);
                });

                ResponseData<Object> data = ResponseData.builder().data(list).build();

                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("Get Order by Seller success!")
                        .results(data)
                        .build());
            }

            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Seller not exist!")
                            .results("")
                            .build()
                    );

        }
        catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
                    );
        }
    }

    @Override
    public ResponseEntity<?> getOrderBySellerAndStatusProcessing(Long sellerId) {
        try {
            Optional<SellerEntity> sellerOptional = sellerRepository.findById(sellerId);
            if(sellerOptional.isPresent()){
                SellerEntity seller = sellerOptional.get();

                List<OrderEntity> orderList =  orderRepository.findAllBySellerAndStatus(seller, EOrder.Processing);

                List<OrderResponse> list = new ArrayList<>();
                orderList.forEach(o->{
//              Trả về Json
                    OrderResponse orderResponse = convertOrderEntityToOrderResponse(o);
                    list.add(orderResponse);
                });

                ResponseData<Object> data = ResponseData.builder().data(list).build();

                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("Get Order by Seller success!")
                        .results(data)
                        .build());
            }

            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Seller not exist!")
                            .results("")
                            .build()
                    );

        }
        catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
                    );
        }
    }

    @Override
    public ResponseEntity<?> getOrderBySellerAndStatusCancel(Long sellerId) {
        try {
            Optional<SellerEntity> sellerOptional = sellerRepository.findById(sellerId);
            if(sellerOptional.isPresent()){
                SellerEntity seller = sellerOptional.get();

                List<OrderEntity> orderList =  orderRepository.findAllBySellerAndStatusIn(seller, Arrays.asList(EOrder.Cancelled,EOrder.Rejection));

                List<OrderResponse> list = new ArrayList<>();
                orderList.forEach(o->{
//              Trả về Json
                    OrderResponse orderResponse = convertOrderEntityToOrderResponse(o);
                    list.add(orderResponse);
                });

                ResponseData<Object> data = ResponseData.builder().data(list).build();

                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("Get Order by Seller success!")
                        .results(data)
                        .build());
            }

            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Seller not exist!")
                            .results("")
                            .build()
                    );

        }
        catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
                    );
        }
    }

    @Override
    public void changeStatusWhenDelivery(DeliveryStatusRequest deliveryStatusRequest) {
            Optional<OrderEntity> orderEntity = orderRepository.findBySeller_IdAndOrderNumber(deliveryStatusRequest.getSellerId(),deliveryStatusRequest.getOrderNumber());
            if(orderEntity.isPresent()){
                OrderEntity order = orderEntity.get();
                if(deliveryStatusRequest.getStatus()){
                    order.setStatus(EOrder.Completed);
                }else{
                    order.setStatus(EOrder.Fail);
                }
                orderRepository.save(order);
            }
        }

    @Override
    public boolean checkExistProductInListOrderDetail(List<OrderDetailEntity> orderDetails, ProductEntity product) {
        for(OrderDetailEntity oD : orderDetails){
            if(oD.getProductItems().getProduct().equals(product)){
                return true;
            }
        }
        return false;
    }

}
