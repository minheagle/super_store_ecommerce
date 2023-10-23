package com.shopee.clone.service.promotion.impl;

import com.shopee.clone.DTO.ResponseData;
import com.shopee.clone.DTO.promotion.request.PromotionRequestCreate;
import com.shopee.clone.DTO.promotion.response.PromotionOfUserResponse;
import com.shopee.clone.DTO.promotion.response.PromotionResponse;
import com.shopee.clone.DTO.promotion.response.TypeDiscountResponse;
import com.shopee.clone.DTO.seller.response.Seller;
import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.entity.promotion.PromotionBeLongUserEntity;
import com.shopee.clone.entity.promotion.PromotionEntity;
import com.shopee.clone.repository.SellerRepository;
import com.shopee.clone.repository.UserRepository;
import com.shopee.clone.repository.promotion.PromotionBeLongUserRepository;
import com.shopee.clone.repository.promotion.PromotionRepository;
import com.shopee.clone.service.promotion.IPromotionService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class PromotionServiceImpl implements IPromotionService {
    private final PromotionRepository promotionRepository;
    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final PromotionBeLongUserRepository promotionBeLongUserRepository;
    private final ModelMapper modelMapper;

    public PromotionServiceImpl(PromotionRepository promotionRepository, SellerRepository sellerRepository, ModelMapper modelMapper, UserRepository userRepository, PromotionBeLongUserRepository promotionBeLongUserRepository) {
        this.promotionRepository = promotionRepository;
        this.sellerRepository = sellerRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.promotionBeLongUserRepository = promotionBeLongUserRepository;
    }

    @Override
    public ResponseEntity<?> createPromotion(PromotionRequestCreate promotionRequestCreate) {
        try{
            if(sellerRepository.existsById(promotionRequestCreate.getSellerId())){

                SellerEntity sellerEntity = sellerRepository.findById(promotionRequestCreate.getSellerId())
                        .orElseThrow(NoSuchElementException::new);

                PromotionEntity promotion = PromotionEntity
                        .builder()
                        .name(promotionRequestCreate.getName())
                        .description(promotionRequestCreate.getDescription())
                        .startDate(promotionRequestCreate.getStartDate())
                        .endDate(promotionRequestCreate.getEndDate())
                        .seller_created(sellerEntity)
                        .createAt(LocalDate.now())
                        .discountType(promotionRequestCreate.getDiscountType())
                        .discountValue(promotionRequestCreate.getDiscountValue())
                        .minPurchaseAmount(promotionRequestCreate.getMinPurchaseAmount())
                        .isActive(Boolean.TRUE)
                        .usageLimitPerUser(promotionRequestCreate.getUsageLimitPerUser())
                        .build();

                PromotionEntity promotionAdded = promotionRepository.save(promotion);

                PromotionResponse promotionResponse = PromotionResponse
                        .builder()
                        .promotionId(promotionAdded.getPromotionId())
                        .name(promotionAdded.getName())
                        .description(promotionAdded.getDescription())
                        .startDate(promotionAdded.getStartDate())
                        .endDate(promotionRequestCreate.getEndDate())
                        .seller(modelMapper.map(sellerEntity, Seller.class))
                        .discountType(promotionAdded.getDiscountType())
                        .discountValue(promotionAdded.getDiscountValue())
                        .minPurchaseAmount(promotionAdded.getMinPurchaseAmount())
                        .isActive(promotionAdded.getIsActive())
                        .usageLimitPerUser(promotionAdded.getUsageLimitPerUser())
                        .build();

                ResponseData<PromotionResponse> responseData = new ResponseData<>();
                responseData.setData(promotionResponse);
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Promotion's Created!")
                                        .results(responseData)
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
                                    .build()
                    );
        }
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<?> editStatusPromotion(Long promotionId, Boolean status) {
        try {
            if(promotionRepository.existsById(promotionId)){

                PromotionEntity promotionEntity = promotionRepository.findById(promotionId)
                        .orElseThrow(NoSuchElementException::new);
                promotionEntity.setIsActive(status);

                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Changed Status")
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
                                    .build()
                    );
        }
        return null;
    }

    //Function check existPromotionByName And Check Available Date.
    @Override
    public Boolean isValidPromotion(Long promotionId, Double purchasedAmount, Long seller_id) {
        PromotionEntity promotion = promotionRepository.findById(promotionId)
                .orElseThrow(NoSuchElementException::new);
        LocalDate currentDate = LocalDate.now();
        if(promotion != null && Objects.equals(promotion.getSeller_created().getId(), seller_id)
                    && purchasedAmount >= promotion.getMinPurchaseAmount()
                && (currentDate.isBefore(promotion.getEndDate()) || currentDate.equals(promotion.getEndDate()))
            && (currentDate.equals(promotion.getStartDate()) || currentDate.isAfter(promotion.getStartDate()))){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public ResponseEntity<?> addPromotionBeLongUser(Long userId, List<Long> listPromotionId) {
        try {
            if(userRepository.existsById(userId)){

                UserEntity user = userRepository.findById(userId)
                        .orElseThrow(NoSuchElementException::new);
                List<PromotionEntity> promotionList = new ArrayList<>();
                for(Long promotionId : listPromotionId){
                    PromotionEntity promotion = promotionRepository.findById(promotionId)
                            .orElseThrow(NoSuchElementException::new);
                    PromotionBeLongUserEntity promotionBeLongUser = PromotionBeLongUserEntity
                            .builder()
                            .user(user)
                            .promotion(promotion)
                            .usageAvailable(promotion.getUsageLimitPerUser())
                            .build();
                    PromotionBeLongUserEntity addPromotionWithUser = promotionBeLongUserRepository.save(promotionBeLongUser);
                    promotionList.add(promotion);
                }

                List<PromotionResponse> promotionResponses = promotionList.stream()
                        .map(promotionEntity -> PromotionResponse
                                .builder()
                                .promotionId(promotionEntity.getPromotionId())
                                .name(promotionEntity.getName())
                                .description(promotionEntity.getDescription())
                                .startDate(promotionEntity.getStartDate())
                                .endDate(promotionEntity.getEndDate())
                                .seller(modelMapper.map(promotionEntity.getSeller_created(), Seller.class))
                                .discountType(promotionEntity.getDiscountType())
                                .discountValue(promotionEntity.getDiscountValue())
                                .minPurchaseAmount(promotionEntity.getMinPurchaseAmount())
                                .isActive(promotionEntity.getIsActive())
                                .usageLimitPerUser(promotionEntity.getUsageLimitPerUser())
                                .build()).toList();

                ResponseData<List<PromotionResponse>> listPromotionAvailable = new ResponseData<>();
                listPromotionAvailable.setData(promotionResponses);
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("User add promotion success")
                                        .results(listPromotionAvailable)
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
                                    .build()
                    );
        }
        return null;
    }

    //Function Check Available Usage
    @Override
    public Boolean checkValidUsage(Long userId, Long promotionId, Double purchasedAmount, Long seller_id) {
        if(userRepository.existsById(userId) && this.isValidPromotion(promotionId,purchasedAmount, seller_id)){

            PromotionEntity promotionFromInput = promotionRepository.findById(promotionId)
                    .orElseThrow(NoSuchElementException::new);

            UserEntity user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

            //User get setPromotion
            Set<PromotionBeLongUserEntity> setPromotionBeLongUser = user.getPromotionEntities();
            for(PromotionBeLongUserEntity promotionWithUser: setPromotionBeLongUser){
                if(Objects.equals(promotionWithUser.getPromotion(),promotionFromInput)){
                    if(promotionWithUser.getUsageAvailable() > 0){
                        return Boolean.TRUE;
                    }
                }
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean minusUsage(Long userId,Long promotionId, Double purchasedAmount, Long seller_id) {
        if(userRepository.existsById(userId) && this.isValidPromotion(promotionId, purchasedAmount, seller_id)){

            PromotionEntity promotionFromInput = promotionRepository.findById(promotionId)
                    .orElseThrow(NoSuchElementException::new);

            UserEntity user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

            //User get setPromotion
            Set<PromotionBeLongUserEntity> setPromotionBeLongUser = user.getPromotionEntities();
            for(PromotionBeLongUserEntity promotionWithUser: setPromotionBeLongUser){
                if(Objects.equals(promotionWithUser.getPromotion(), promotionFromInput)){
                    promotionWithUser.setUsageAvailable(promotionWithUser.getUsageAvailable()-1);
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean plusUsage(Long userId, Long promotionId, Double purchasedAmount, Long seller_id) {
        if(userRepository.existsById(userId) && this.isValidPromotion(promotionId, purchasedAmount, seller_id)){

            PromotionEntity promotionFromInput = promotionRepository.findById(promotionId)
                    .orElseThrow(NoSuchElementException::new);

            UserEntity user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

            //User get setPromotion
            Set<PromotionBeLongUserEntity> setPromotionBeLongUser = user.getPromotionEntities();
            for(PromotionBeLongUserEntity promotionWithUser: setPromotionBeLongUser){
                if(Objects.equals(promotionWithUser.getPromotion(), promotionFromInput)){
                    promotionWithUser.setUsageAvailable(promotionWithUser.getUsageAvailable()+1);
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
    @Override
    public ResponseEntity<?> getAllPromotionAvailable(Long userId) {
        try{
            LocalDate currentDate = LocalDate.now();
            List<PromotionEntity> promotionEntities = promotionRepository.findAllByIsActiveAvailable(currentDate);

            UserEntity user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
            List<PromotionEntity> listPromotionUserHas = promotionBeLongUserRepository.getAllPromotionUserHas(user);

            List<PromotionResponse> promotionResponses = new ArrayList<>();
            for(PromotionEntity promotion : promotionEntities){
                if(!(listPromotionUserHas.contains(promotion))){

                    PromotionResponse promotionResult = PromotionResponse
                            .builder()
                            .promotionId(promotion.getPromotionId())
                            .name(promotion.getName())
                            .description(promotion.getDescription())
                            .startDate(promotion.getStartDate())
                            .endDate(promotion.getEndDate())
                            .seller(modelMapper.map(promotion.getSeller_created(), Seller.class))
                            .discountType(promotion.getDiscountType())
                            .discountValue(promotion.getDiscountValue())
                            .minPurchaseAmount(promotion.getMinPurchaseAmount())
                            .isActive(promotion.getIsActive())
                            .usageLimitPerUser(promotion.getUsageLimitPerUser())
                            .build();
                    promotionResponses.add(promotionResult);
                }
            }
            System.out.println(promotionResponses.size());

            ResponseData<List<PromotionResponse>> promotionDataResponses= new ResponseData<>();
            promotionDataResponses.setData(promotionResponses);
            if(promotionDataResponses.getData().size() > 0){
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Get All Promotion success")
                                        .results(promotionDataResponses)
                                        .build()
                        );
            }
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Not Have Promotion Available")
                                    .results(promotionDataResponses)
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
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<?> getAllPromotionBySellerId(Long sellerId) {
        try {
            if(!sellerRepository.existsById(sellerId)){
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(403))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("Fail")
                                        .message("Seller Not Exist")
                                        .results("")
                                        .build()
                        );
            }
            SellerEntity seller = sellerRepository.findById(sellerId).orElseThrow(NoSuchElementException::new);
            List<PromotionEntity> promotionEntities = promotionRepository.findAllBySeller_created(seller);
            List<PromotionResponse> promotionResponses = promotionEntities.stream()
                    .map(promotionEntity -> modelMapper.map(promotionEntity,PromotionResponse.class)).toList();

            ResponseData<List<PromotionResponse>> responseData = new ResponseData<>();
            responseData.setData(promotionResponses);
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get Promotions By Seller success")
                                    .results(responseData)
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
                                    .build()
                    );
        }
    }
    @Override
    public ResponseEntity<?> getPromotionOfUser(Long userId) {
        try{
            if(!userRepository.existsById(userId)){
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(403))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("Fail")
                                        .message("User Not Exist")
                                        .results("")
                                        .build()
                        );
            }

            UserEntity user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

            List<PromotionBeLongUserEntity> listPromotionOfUser = promotionBeLongUserRepository.getPromotionOfUser(user);
            //Convert fromEntity To Data response
            List<PromotionOfUserResponse> promotionOfUserResponseList = listPromotionOfUser.stream()
                    .map(promotionBeLongUserEntity -> PromotionOfUserResponse
                            .builder()
                            .promotionId(promotionBeLongUserEntity.getPromotion().getPromotionId())
                            .name(promotionBeLongUserEntity.getPromotion().getName())
                            .description(promotionBeLongUserEntity.getPromotion().getDescription())
                            .startDate(promotionBeLongUserEntity.getPromotion().getStartDate())
                            .endDate(promotionBeLongUserEntity.getPromotion().getEndDate())
                            .seller(modelMapper.map(promotionBeLongUserEntity.getPromotion().getSeller_created(),Seller.class))
                            .discountType(promotionBeLongUserEntity.getPromotion().getDiscountType())
                            .discountValue(promotionBeLongUserEntity.getPromotion().getDiscountValue())
                            .minPurchaseAmount(promotionBeLongUserEntity.getPromotion().getMinPurchaseAmount())
                            .usageAvailable(promotionBeLongUserEntity.getUsageAvailable())
                            .build()).toList();
            ResponseData<List<PromotionOfUserResponse>> responseData = new ResponseData<>();
            responseData.setData(promotionOfUserResponseList);
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get Promotions By User success")
                                    .results(responseData)
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
                                    .build()
                    );
        }
    }

    @Override
    public TypeDiscountResponse getTypeDiscount(Long promotionId) {
        PromotionEntity promotion = promotionRepository.findById(promotionId).orElseThrow(() -> new NoSuchElementException());
        if(promotion != null){
            return TypeDiscountResponse
                    .builder()
                    .discountType(promotion.getDiscountType())
                    .discountValue(promotion.getDiscountValue())
                    .build();
        }
        return null;
    }

}
