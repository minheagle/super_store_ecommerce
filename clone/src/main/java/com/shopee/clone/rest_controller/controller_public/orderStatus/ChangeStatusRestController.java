package com.shopee.clone.rest_controller.controller_public.orderStatus;

import com.shopee.clone.DTO.order.request.DeliveryStatusRequest;
import com.shopee.clone.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public/order")
public class ChangeStatusRestController {
    @Autowired
    private OrderService orderService;
    @PostMapping("delivery-request")
    public void changeStatusWhenDelivery(@RequestBody DeliveryStatusRequest deliveryStatusRequest){
        orderService.changeStatusWhenDelivery(deliveryStatusRequest);
    }
}
