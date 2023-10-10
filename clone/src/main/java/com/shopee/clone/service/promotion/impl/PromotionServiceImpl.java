package com.shopee.clone.service.promotion.impl;

import com.shopee.clone.DTO.ResponseData;
import com.shopee.clone.DTO.promotion.request.PromotionRequestCreate;
import com.shopee.clone.DTO.promotion.response.PromotionResponse;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> editStatusPromotion(Long promotionId, Boolean status) {
        try {
            if(promotionRepository.existsById(promotionId)){

                PromotionEntity promotionEntity = promotionRepository.findById(promotionId)
                        .orElseThrow(NoSuchElementException::new);
                promotionEntity.setIsActive(status);

                ResponseData<PromotionEntity> promotionResponseData = new ResponseData<>();
                promotionResponseData.setData(promotionEntity);
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Changed Status")
                                        .results(promotionResponseData)
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
    public Boolean isValidPromotion(String name) {
        PromotionEntity promotion = promotionRepository.findByName(name);
        if(promotion != null){
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

//                List<PromotionEntity> promotionList = listPromotionId.stream()
//                        .map(promotionId -> {
//                            PromotionEntity promotion = promotionRepository.findById(promotionId)
//                                    .orElseThrow(NoSuchElementException::new);
//                            promotion.setUserEntities(Set.of(user));
//                            return promotion;
//                        }).toList();
//                promotionRepository.saveAll(promotionList);
//                user.setPromotionEntities(Set.copyOf(promotionList));
//                userRepository.save(user);

                List<PromotionResponse> promotionResponses = promotionList.stream()
                        .map(promotionEntity -> {
                            PromotionResponse promotionResponse = PromotionResponse
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
                                    .build();
                            return promotionResponse;
                        }).toList();

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

    @Override
    public Boolean checkValidUsage(Long userId, String promotionName) {
        if(userRepository.existsById(userId) && this.isValidPromotion(promotionName)){

//            PromotionEntity promotion = promotionRepository.findByName(promotionName);
            UserEntity user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

            //User get setPromotion
            Set<PromotionBeLongUserEntity> setPromotionBeLongUser = user.getPromotionEntities();
            for(PromotionBeLongUserEntity promotionWithUser: setPromotionBeLongUser){
                if(setPromotionBeLongUser.contains(promotionWithUser)){
                    if(promotionWithUser.getUsageAvailable() > 0){
                        return Boolean.TRUE;
                    }
                }
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean minusUsage(Long userId,String promotionName) {
        if(userRepository.existsById(userId) && this.isValidPromotion(promotionName)){

//            PromotionEntity promotion = promotionRepository.findByName(promotionName);
            UserEntity user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

            //User get setPromotion
            Set<PromotionBeLongUserEntity> setPromotionBeLongUser = user.getPromotionEntities();
            for(PromotionBeLongUserEntity promotionWithUser: setPromotionBeLongUser){
                if(setPromotionBeLongUser.contains(promotionWithUser)){
                    promotionWithUser.setUsageAvailable(promotionWithUser.getUsageAvailable()-1);
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean plusUsage(Long userId,String promotionName) {
        if(userRepository.existsById(userId) && this.isValidPromotion(promotionName)){

//            PromotionEntity promotion = promotionRepository.findByName(promotionName);
            UserEntity user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

            //User get setPromotion
            Set<PromotionBeLongUserEntity> setPromotionBeLongUser = user.getPromotionEntities();
            for(PromotionBeLongUserEntity promotionWithUser: setPromotionBeLongUser){
                if(setPromotionBeLongUser.contains(promotionWithUser)){
                    promotionWithUser.setUsageAvailable(promotionWithUser.getUsageAvailable()+1);
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
}
