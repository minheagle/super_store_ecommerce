package com.shopee.clone.service.product.impl;

import com.shopee.clone.DTO.product.*;
import com.shopee.clone.DTO.product.request.ProductRequestCreate;
import com.shopee.clone.DTO.product.specification.ProductSpecification;
import com.shopee.clone.DTO.product.update.ProductRequestEdit;
import com.shopee.clone.DTO.product.response.*;
import com.shopee.clone.entity.*;
import com.shopee.clone.repository.CategoryRepository;
import com.shopee.clone.repository.SellerRepository;
import com.shopee.clone.repository.product.ProductItemRepository;
import com.shopee.clone.repository.product.ProductRepository;
import com.shopee.clone.service.product.IProductService;
import com.shopee.clone.service.productItem.IProductItemService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductItemRepository itemRepository;
    private final IProductItemService productItemService;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    public ProductService(ProductRepository productRepository,
                          ProductItemRepository itemRepository,
                          ModelMapper modelMapper, CategoryRepository categoryRepository, SellerRepository sellerRepository, IProductItemService productItemService) {
        this.productRepository = productRepository;
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
        this.sellerRepository = sellerRepository;
        this.productItemService = productItemService;
    }

    @Override
    public ResponseEntity<?> getAllProductBelongWithShop(Long shopId) {
        try{
            if(sellerRepository.existsById(shopId)){
                List<ProductEntity> productEntities = productRepository.findProductsByShopId(shopId);
                List<ProductResponseDTO> productResponseDTOs = mappingProductEntityListToProductDTOs(productEntities);

                ProductResponseObject<List<ProductResponseDTO>> productsResponse = new ProductResponseObject<>();
                productsResponse.setData(productResponseDTOs);

                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Get Products Success")
                                        .results(productsResponse)
                                        .build()
                        );
            }

        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(404))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
        return null;
    }

    @Override
    public ResponseEntity<?> getAllProductByCategoryId(Long categoryId) {
        try{
            if(categoryRepository.existsById(categoryId)){
                List<ProductEntity> productEntities = productRepository.findProductsByCategoryId(categoryId);
                List<ProductResponseDTO> productResponseDTOs = mappingProductEntityListToProductDTOs(productEntities);

                ProductResponseObject<List<ProductResponseDTO>> productsResponse = new ProductResponseObject<>();
                productsResponse.setData(productResponseDTOs);

                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Get Products By Category Success")
                                        .results(productsResponse)
                                        .build()
                        );
            }
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(404))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
        return null;
    }

    @Override
    public ResponseEntity<?> getAllProductPaging(Pageable pageable) {
        try{
            Page<ProductEntity> productEntities = productRepository.findProducts(pageable);
            PaginationResponse paginationResponse = new PaginationResponse(
                    productEntities.getTotalPages(),
                    productEntities.getTotalElements(),
                    productEntities.getNumber()+1,
                    productEntities.getSize());

            List<ProductResponseDTO> productResponseDTOs = mappingProductEntityListToProductDTOs(productEntities);

            ProductResponseObject<List<ProductResponseDTO>> productsResponse = new ProductResponseObject<>();
            productsResponse.setData(productResponseDTOs);

            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get Products Success")
                                    .results(productsResponse)
                                    .pagination(paginationResponse)
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(404))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
    }

    /*
     * Nhận vào List ProductItemResponseDTO Rỗng và itemEntity -> convert Img-OptionType-OptionValue
     * sang ProductItemResponseDTO - sau đó add vào List ProductItemResponseDTO
     * */
    private void mappingSpecialImg_OptionWithProductItem(List<ProductItemResponseDTO> productItemResponseDTOList,
                                                         ProductItemEntity productItemEntity) {
        List<OptionValue> optionValues;
        List<ImageProduct> imageProducts = productItemEntity.getImageProductList()
                .stream()
                .map(imageProductEntity -> ImageProduct.builder()
                        .imgProductId(imageProductEntity.getImgProductId())
                        .imgPublicId(imageProductEntity.getImgPublicId())
                        .imgProductUrl(imageProductEntity.getImgProductUrl())
                        .build())
                .collect(Collectors.toList());

        optionValues = productItemEntity.getOptionValues()
                .stream()
                .map(optionValueEntity -> OptionValue.builder()
                        .opValueId(optionValueEntity.getOpValueId())
                        .valueName(optionValueEntity.getValueName())
                        .optionType(OptionType

                                .builder()
                                .opTypeId(optionValueEntity.getOptionType().getOpTypeId())
                                .optionName(optionValueEntity.getOptionType().getOptionName())
                                .build())
                        .build())
                .collect(Collectors.toList());

        List<OptionTypeDTO> optionTypeDTOS = optionValues
                .stream()
                .map(optionValue -> OptionTypeDTO
                        .builder()
                        .opTypeId(optionValue.getOptionType().getOpTypeId())
                        .optionName(optionValue.getOptionType().getOptionName())
                        .optionValue(OptionValueDTO
                                .builder()
                                .opValueId(optionValue.getOpValueId())
                                .valueName(optionValue.getValueName())
                                .build())
                        .build()).collect(Collectors.toList());

        ProductItemResponseDTO productItemResponseDTO = ProductItemResponseDTO
                .builder()
                .pItemId(productItemEntity.getPItemId())
                .price(productItemEntity.getPrice())
                .qtyInStock(productItemEntity.getQtyInStock())
                .status(productItemEntity.getStatus())
                .imageProductList(imageProducts)
                .optionTypes(optionTypeDTOS)
                .build();
        productItemResponseDTOList.add(productItemResponseDTO);
    }

    @Override
    public ResponseEntity<?> getProductById(Long productId) {
        try {
            ProductResponseDTO productResponseDTO = getProductByIdForService(productId);
            ProductResponseObject<ProductResponseDTO> productResponse = new ProductResponseObject<>();
                productResponse.setData(productResponseDTO);
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Get Product Success")
                                        .results(productResponse)
                                        .build()
                        );
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(404))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<?> confirmFinishCreateProduct(Long productId) {
        try{
            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(NoSuchElementException::new);
            Double minPrice = productItemService.findMinPriceInProductItem(product.getProductItemList());
            Double maxPrice = productItemService.findMaxPriceInProductItem(product.getProductItemList());
            product.setMinPrice(minPrice);
            product.setMaxPrice(maxPrice);
            productRepository.save(product);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(404))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(
                        ResponseObject
                                .builder()
                                .status("SUCCESS")
                                .message("Finished!")
                                .build()
                );
    }

    @Override
    public ProductResponseDTO getProductByIdForService(Long productId) {
        try {
            ProductEntity productEntity = productRepository.findById(productId)
                    .orElseThrow(NoSuchElementException::new);
            if (productEntity.getStatus()) {
                List<ProductItemEntity> productItemEntities = productEntity.getProductItemList();
                ProductResponseDTO productResponseDTO = new ProductResponseDTO();
                List<ProductItemResponseDTO> productItemResponseDTOList = new ArrayList<>();

                for (ProductItemEntity productItemEntity : productItemEntities) {

                    mappingSpecialImg_OptionWithProductItem(productItemResponseDTOList, productItemEntity);

                    productResponseDTO = ProductResponseDTO
                            .builder()
                            .productId(productEntity.getProductId())
                            .sellerId(productEntity.getSeller().getId())
                            .minPrice(productItemService.findMinPriceInProductItem(productEntity.getProductItemList()))
                            .maxPrice(productItemService.findMaxPriceInProductItem(productEntity.getProductItemList()))
                            .productName(productEntity.getProductName())
                            .minPrice(productEntity.getMinPrice())
                            .maxPrice(productEntity.getMaxPrice())
                            .description(productEntity.getDescription())
                            .status(productEntity.getStatus())
                            .categoryId(productEntity.getCategory().getId())
                            .productItemResponseList(productItemResponseDTOList)
                            .voteStar(productEntity.getVoteStar())
                            .build();
                }
                return productResponseDTO;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<?> addNewProduct(ProductRequestCreate productRequest) {
        try {
            if(sellerRepository.existsById(productRequest.getSellerId()) &&
                    categoryRepository.existsById(productRequest.getCategoryId())){
                SellerEntity sellerEntity = sellerRepository.findById(productRequest.getSellerId())
                        .orElseThrow(NoSuchElementException::new);
                CategoryEntity categoryEntity = categoryRepository.findById(productRequest.getCategoryId())
                        .orElseThrow(NoSuchElementException::new);
                SellerEntity seller = SellerEntity
                        .builder()
                        .id(sellerEntity.getId())
                        .storeName(sellerEntity.getStoreName())
                        .storeAddress(sellerEntity.getStoreAddress())
                        .storeAvatarUrl(sellerEntity.getStoreAvatarUrl())
                        .storeBackgroundUrl(sellerEntity.getStoreBackgroundUrl())
                        .createdAt(sellerEntity.getCreatedAt())
                        .numberFollower(sellerEntity.getNumberFollower())
                        .build();
                CategoryEntity category = CategoryEntity
                        .builder()
                        .id(categoryEntity.getId())
                        .content(categoryEntity.getContent())
                        .imagePublicId(categoryEntity.getImagePublicId())
                        .imageUrl(categoryEntity.getImageUrl())
                        .left(categoryEntity.getLeft())
                        .right(categoryEntity.getRight())
                        .build();
                Product product = Product
                        .builder()
                        .productName(productRequest.getProductName())
                        .description(productRequest.getDescription())
                        .status(true)
                        .category(category)
                        .seller(seller)
                        .build();
                Product productAfterSaved = modelMapper.map(
                        productRepository.save(modelMapper.map(product,ProductEntity.class)),Product.class);
                ProductResponseObject<Product> productResponse = new ProductResponseObject<>();
                productResponse.setData(productAfterSaved);
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(201))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Add Product Success Pls Do Next-Step: Add Item and list image")
                                        .results(productResponse)
                                        .build()
                        );
            }

        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(404))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
        return null;
    }
    @Override
    public ResponseEntity<?> searchAndFilter(String name,
                                             Double minPrice,
                                             Double maxPrice,
                                             Long categoryId,
                                             Pageable pageable) {

        ProductResponseObject<List<ProductResponseDTO>> productsResponse = new ProductResponseObject<>();
        try{
            //Khởi tạo specification với thiết lập ban đầu là điều kiện rỗng.
            //Sau đó sẽ lấy điều kiện dựa trên tham số người dùng cung cấp.
            Specification<ProductEntity> productSpc = Specification.where(null);

            productSpc = productSpc.and(ProductSpecification.searchByName(name));
            productSpc = productSpc.and(ProductSpecification.filterByPrice(minPrice,maxPrice));
            productSpc = productSpc.and(ProductSpecification.filterByCategory(categoryId));

            // Sử dụng phân trang danh sách product trong truy vấn
            Page<ProductEntity> productPage = productRepository.findAll(productSpc, pageable);


            List<ProductEntity> productEntities = productPage.getContent();

            List<ProductResponseDTO> productResponseDTOs = mappingProductEntityListToProductDTOs(productEntities);
            productsResponse.setData(productResponseDTOs);

            // Lấy thông tin phân trang từ đối tượng Page
            PaginationResponse paginationResponse = new PaginationResponse(
                    productPage.getTotalPages(),
                    productPage.getTotalElements(),
                    productPage.getNumber()+1,
                    productPage.getSize());
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .results(productsResponse)
                                    .pagination(paginationResponse)
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(404))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }

    }

    @Override
    public ResponseEntity<?> editProductById(Long productId, ProductRequestEdit pRequestEdit) {
        try {
            if(productRepository.existsById(productId)){
                ProductEntity product = productRepository.findById(productId)
                        .orElseThrow(NoSuchElementException::new);
                product.setProductName(pRequestEdit.getProductName());
                product.setDescription(pRequestEdit.getDescription());
                productRepository.save(product);
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Product was Updated")
                                        .build()
                        );
            }

        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(404))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message("Product Not Exist!")
                                    .build()
                    );

        }
        return null;
    }
    @Transactional
    @Override
    public ResponseEntity<?> removeProductById(Long productId) {
        if(productRepository.existsById(productId)){
            ProductEntity productEntity = productRepository.findById(productId)
                    .orElseThrow(NoSuchElementException::new);
            productEntity.setStatus(false);
            productEntity.getProductItemList()
                            .forEach(productItemEntity -> {
                                productItemEntity.setStatus(false);
                                itemRepository.save(productItemEntity);
                            });
            productRepository.save(productEntity);
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Product is Removed")
                                    .build()
                    );
        }
        return ResponseEntity
                .status(HttpStatusCode.valueOf(404))
                .body(
                        ResponseObject
                                .builder()
                                .status("FAIL")
                                .message("Product Not Exist!")
                                .build()
                );
    }

    private List<ProductResponseDTO> mappingProductEntityListToProductDTOs(Page<ProductEntity> productEntities){
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();

        for(ProductEntity productEntity: productEntities){
            List<ProductItemEntity> productItemEntities = productEntity.getProductItemList();

            List<ProductItemResponseDTO> productItemResponseDTOList = new ArrayList<>();
            for(ProductItemEntity productItemEntity : productItemEntities){

                mappingSpecialImg_OptionWithProductItem(productItemResponseDTOList, productItemEntity);
            }
            ProductResponseDTO productResponseDTO = ProductResponseDTO
                    .builder()
                    .productId(productEntity.getProductId())
                    .productName(productEntity.getProductName())
                    .minPrice(productItemService.findMinPriceInProductItem(productItemEntities))
                    .maxPrice(productItemService.findMaxPriceInProductItem(productItemEntities))
                    .description(productEntity.getDescription())
                    .status(productEntity.getStatus())
                    .sellerId(productEntity.getSeller().getId())
                    .categoryId(productEntity.getCategory().getId())
                    .productItemResponseList(productItemResponseDTOList)
                    .voteStar(productEntity.getVoteStar())
                    .build();
            productResponseDTOList.add(productResponseDTO);
        }
        return productResponseDTOList;
    }
    private List<ProductResponseDTO> mappingProductEntityListToProductDTOs(List<ProductEntity> productEntities){
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();

        for(ProductEntity productEntity: productEntities){
            List<ProductItemEntity> productItemEntities = productEntity.getProductItemList();

            List<ProductItemResponseDTO> productItemResponseDTOList = new ArrayList<>();
            for(ProductItemEntity productItemEntity : productItemEntities){

                mappingSpecialImg_OptionWithProductItem(productItemResponseDTOList, productItemEntity);
            }
            ProductResponseDTO productResponseDTO = ProductResponseDTO
                    .builder()
                    .productId(productEntity.getProductId())
                    .productName(productEntity.getProductName())
                    .minPrice(productItemService.findMinPriceInProductItem(productItemEntities))
                    .maxPrice(productItemService.findMaxPriceInProductItem(productItemEntities))
                    .description(productEntity.getDescription())
                    .status(productEntity.getStatus())
                    .sellerId(productEntity.getSeller().getId())
                    .categoryId(productEntity.getCategory().getId())
                    .productItemResponseList(productItemResponseDTOList)
                    .voteStar(productEntity.getVoteStar())
                    .build();
            productResponseDTOList.add(productResponseDTO);
        }
        return productResponseDTOList;
    }
}
