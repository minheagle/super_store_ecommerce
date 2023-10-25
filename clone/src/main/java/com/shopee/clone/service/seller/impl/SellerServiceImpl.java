package com.shopee.clone.service.seller.impl;

import com.shopee.clone.DTO.seller.SellerDTO;
import com.shopee.clone.DTO.seller.request.SellerRequestUpdate;
import com.shopee.clone.DTO.seller.response.Seller;
import com.shopee.clone.DTO.upload_file.ImageUploadResult;
import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.repository.SellerRepository;
import com.shopee.clone.response.seller.DetailSellerResponse;
import com.shopee.clone.service.seller.SellerService;
import com.shopee.clone.service.upload_cloud.IUploadImageService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final ModelMapper mapper;
    private final IUploadImageService uploadImageService;

    @Value("${cloudinary.store.folder}")
    private String storeImageFolder;

    public SellerServiceImpl(SellerRepository sellerRepository, ModelMapper mapper, IUploadImageService uploadImageService) {
        this.sellerRepository = sellerRepository;
        this.mapper = mapper;
        this.uploadImageService = uploadImageService;
    }

    @Override
    public SellerEntity getBySellerId(Long sellerId) {
        try{
            SellerEntity sellerEntity = sellerRepository.findById(sellerId)
                    .orElseThrow(() -> new RuntimeException("Seller not found"));
            return sellerEntity;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> responseGetSellerById(Long sellerId) {
        try{
            SellerEntity sellerEntity = getBySellerId(sellerId);
            SellerDTO seller = mapper.map(sellerEntity, SellerDTO.class);
            DetailSellerResponse<SellerDTO> detailSellerResponse = new DetailSellerResponse<>(seller);
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get seller detail success")
                                    .results(detailSellerResponse)
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
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
    @Transactional
    public ResponseEntity<?> updatePrivateInformation(SellerRequestUpdate sellerRequestUpdate) {
        try {
            if(!sellerRepository.existsById(sellerRequestUpdate.getId())){
                return ResponseEntity
                        .badRequest()
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("FAIL")
                                        .message("Store Not Exist")
                                        .results("")
                                        .build()
                        );
            }
            SellerEntity sellerEntity = getBySellerId(sellerRequestUpdate.getId());

            sellerEntity.setStoreName(sellerRequestUpdate.getStoreName());
            sellerEntity.setStoreAddress(sellerRequestUpdate.getStoreAddress());
            sellerEntity.setStorePhoneNumber(sellerRequestUpdate.getStorePhoneNumber());
            sellerEntity.setStoreBankName(sellerRequestUpdate.getStoreBankName());
            sellerEntity.setStoreBankAccountNumber(sellerRequestUpdate.getStoreBankAccountNumber());

            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Updated Store Information")
                                    .results("")
                                    .build()
                    );

        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
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
    @Transactional
    public ResponseEntity<?> updateStoreAvatar(Long storeId, MultipartFile storeAvatar) {
        try{
            if(!sellerRepository.existsById(storeId)){
                return ResponseEntity
                        .badRequest()
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("FAIL")
                                        .message("Store Not Exist")
                                        .results("")
                                        .build()
                        );
            }
            //Upload Avatar
            ImageUploadResult resultUploadAvatar =
                    uploadImageService.uploadSingle(storeAvatar, storeImageFolder);
            SellerEntity seller = getBySellerId(storeId);
            seller.setImgAvatarPublicId(resultUploadAvatar.getPublic_id());
            seller.setStoreAvatarUrl(resultUploadAvatar.getSecure_url());

            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Updated Avatar")
                                    .results("")
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
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
    @Transactional
    public ResponseEntity<?> updateStoreBackground(Long storeId, MultipartFile storeBackground) {
        try{
            if(!sellerRepository.existsById(storeId)){
                return ResponseEntity
                        .badRequest()
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("FAIL")
                                        .message("Store Not Exist")
                                        .results("")
                                        .build()
                        );
            }
            //Upload Background
            ImageUploadResult resultUploadBackground =
                    uploadImageService.uploadSingle(storeBackground, storeImageFolder);
            SellerEntity seller = getBySellerId(storeId);
            seller.setImgBackgroundPublicId(resultUploadBackground.getPublic_id());
            seller.setStoreBackgroundUrl(resultUploadBackground.getSecure_url());

            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Updated Background")
                                    .results("")
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
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
}
