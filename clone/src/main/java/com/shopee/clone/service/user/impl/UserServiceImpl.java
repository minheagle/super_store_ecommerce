package com.shopee.clone.service.user.impl;

import com.shopee.clone.DTO.ResponseData;
import com.shopee.clone.DTO.auth.user.*;
import com.shopee.clone.DTO.product.response.PaginationResponse;
import com.shopee.clone.DTO.seller.SellerDTO;
import com.shopee.clone.DTO.seller.response.Seller;
import com.shopee.clone.DTO.upload_file.ImageUploadResult;
import com.shopee.clone.entity.*;
import com.shopee.clone.repository.RoleRepository;
import com.shopee.clone.repository.SellerRepository;
import com.shopee.clone.repository.UserRepository;
import com.shopee.clone.response.user.ResponseBecomeSeller;
import com.shopee.clone.response.user.ResponseDetailUser;
import com.shopee.clone.response.user.ResponseDetailUserHasRoleSeller;
import com.shopee.clone.service.address.AddressService;
import com.shopee.clone.service.upload_cloud.IUploadImageService;
import com.shopee.clone.service.user.UserService;
import com.shopee.clone.util.JWTProvider;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service

public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AddressService addressService;
    @Autowired
    private IUploadImageService uploadImageService;

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper mapper;

    @Value("cloudinary.avatar.folder")
    private String avatarFolder;

    // Hàm cập nhật thông tin người dùng
    public ResponseEntity<?> updateUser(long userId, UserUpdateDTO userUpdateDTO) {
        try {
            // Tìm kiếm người dùng trong cơ sở dữ liệu bằng userId
            Optional<UserEntity> optionalUser = userRepository.findById(userId);

            if (optionalUser.isPresent()) {
                // Lấy đối tượng người dùng từ Optional
                UserEntity user = optionalUser.get();

                // Cập nhật thông tin người dùng từ DTO (Data Transfer Object)
                user.setUserName(userUpdateDTO.getUserName());
                user.setFullName(userUpdateDTO.getFullName());
                user.setPhone(userUpdateDTO.getPhone());
//                user.setAvatar(userUpdateDTO.getAvatar());
                user.setEmail(userUpdateDTO.getEmail());
                user.setDateOfBirth(userUpdateDTO.getDateOfBirth());

                // Lưu thông tin người dùng đã cập nhật vào cơ sở dữ liệu
                userRepository.save(user);

                User userMapper = mapper.map(user, User.class);

                // Trả về ResponseEntity chứa thông tin cập nhật thành công
                return ResponseEntity.ok().body(
                        ResponseObject.builder()
                                .status("SUCCESS")
                                .message("User updated successfully")
                                .results(userMapper)
                                .build()
                       );
            } else {
                // Trả về ResponseEntity chứa thông tin lỗi nếu không tìm thấy người dùng
                return ResponseEntity
                        .badRequest()
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("User not found")
                                .results("")
                                .build());
            }
        } catch (Exception e) {
            // Trả về ResponseEntity chứa thông tin lỗi nếu có lỗi xảy ra
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build());

        }
    }

    @Override
    public ResponseEntity<?> changeAvatar(long userId, ChangeAvatarRequest changeAvatarRequest) {
        try{
            UserEntity userEntity = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            ImageUploadResult imageUploadResult;
            if(changeAvatarRequest.getPublicId() == null) {
                imageUploadResult = uploadImageService
                        .uploadSingle(changeAvatarRequest.getAvatar(), avatarFolder);
            }else{
                imageUploadResult = uploadImageService
                        .replaceSingle(changeAvatarRequest.getAvatar(), changeAvatarRequest.getPublicId(), avatarFolder);
            }
            userEntity.setImageUrl(imageUploadResult.getSecure_url());
            userEntity.setImgPublicId(imageUploadResult.getPublic_id());
            userRepository.save(userEntity);
            ResponseDetailUser<UserEntity> responseDetailUser = new ResponseDetailUser<>();
            responseDetailUser.setData(userEntity);
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("Change avatar success")
                                    .results(responseDetailUser)
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
    public ResponseEntity<?> blockUser(Long id) {
        try {
            // Tìm kiếm người dùng trong cơ sở dữ liệu bằng userId
            Optional<UserEntity> optionalUser = userRepository.findById(id);

            if (optionalUser.isPresent()) {
                // Lấy đối tượng người dùng từ Optional
                UserEntity user = optionalUser.get();

                user.setStatus(false);

                // Lưu thông tin người dùng đã cập nhật vào cơ sở dữ liệu
                userRepository.save(user);

                // Trả về ResponseEntity chứa thông tin cập nhật thành công
                return ResponseEntity.ok().body(
                        ResponseObject.builder()
                                .status("SUCCESS")
                                .message("Ban User successfully")
                                .results(user)
                                .build()
                        );
            } else {
                // Trả về ResponseEntity chứa thông tin lỗi nếu không tìm thấy người dùng
                return ResponseEntity
                        .badRequest()
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("User not found")
                                .results("")
                                .build());
            }
        } catch (Exception e) {
            // Trả về ResponseEntity chứa thông tin lỗi nếu có lỗi xảy ra
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build());
        }
    }

    @Override
    public ResponseEntity<?> unBlockUser(Long id) {
        try {
            // Tìm kiếm người dùng trong cơ sở dữ liệu bằng userId
            Optional<UserEntity> optionalUser = userRepository.findById(id);

            if (optionalUser.isPresent()) {
                // Lấy đối tượng người dùng từ Optional
                UserEntity user = optionalUser.get();

                user.setStatus(true);

                // Lưu thông tin người dùng đã cập nhật vào cơ sở dữ liệu
                userRepository.save(user);

                // Trả về ResponseEntity chứa thông tin cập nhật thành công
                return ResponseEntity.ok().body(
                        ResponseObject.builder()
                                .status("SUCCESS")
                                .message("unBan User successfully")
                                .results(user)
                                .build()
                );
            } else {
                // Trả về ResponseEntity chứa thông tin lỗi nếu không tìm thấy người dùng
                return ResponseEntity
                        .badRequest()
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("User not found")
                                .results("")
                                .build());
            }
        } catch (Exception e) {
            // Trả về ResponseEntity chứa thông tin lỗi nếu có lỗi xảy ra
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build());
        }
    }

    @Override
    public ResponseEntity<?> changePassword( Long id, ChangePasswordDTO changePasswordDTO) {
        try {

            // Tìm kiếm người dùng trong cơ sở dữ liệu bằng userId
            Optional<UserEntity> optionalUser = userRepository.findById(id);

            if (optionalUser.isPresent()) {
                // Lấy đối tượng người dùng từ Optional
                UserEntity user = optionalUser.get();

                if(checkPassword(changePasswordDTO.getOldPassword(),user.getPassword())){
                    if(changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())){
                        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
                        // Lưu thông tin người dùng đã cập nhật vào cơ sở dữ liệu
                        userRepository.save(user);

                        // Trả về ResponseEntity chứa thông tin cập nhật thành công
                        return ResponseEntity.ok().body(
                                ResponseObject.builder()
                                        .status("SUCCESS")
                                        .message("Change password successfully")
                                        .results(changePasswordDTO.getNewPassword())
                                        .build()
                                );
                    }
                    // Trả về ResponseEntity chứa thông tin lỗi
                    return ResponseEntity.badRequest().body(
                            ResponseObject.builder()
                                    .status("Fail")
                                    .message("Confirm password Error")
                                    .build()
                            );
                };

                // Trả về ResponseEntity chứa thông tin lỗi nêú sai mật khẩu
                return ResponseEntity.ok().body(
                        ResponseObject.builder()
                                .status("Fail")
                                .message("OutPassword error")
                                .results(changePasswordDTO.getOldPassword())
                                .build()
                        );
            } else {
                // Trả về ResponseEntity chứa thông tin lỗi nếu không tìm thấy người dùng
                return ResponseEntity
                        .badRequest()
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("User not found")
                                .results("")
                                .build());
            }
        } catch (Exception e) {
            // Trả về ResponseEntity chứa thông tin lỗi nếu có lỗi xảy ra
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build());
        }
    }

    @Override
    public ResponseEntity<?> updateAddress(Long id, UpdateAddressDTO updateAddressDTO) {
        try {
            // Tìm kiếm địa chỉ người dùng trong cơ sở dữ liệu bằng userId
            Optional<AddressEntity> optionalAddress = addressService.findById(id);

            if (optionalAddress.isPresent()) {
                // Lấy địa chỉ người dùng từ Optional
                AddressEntity address = optionalAddress.get();

                // Cập nhật thông tin địa chỉ người dùng từ DTO (Data Transfer Object)
                address.setAddressName(updateAddressDTO.getAddress());

                // Lưu thông tin địa chỉ người dùng đã cập nhật vào cơ sở dữ liệu
                addressService.save(address);

                // Trả về ResponseEntity chứa thông tin cập nhật thành công
                return ResponseEntity.ok().body(
                        ResponseObject.builder()
                                .status("SUCCESS")
                                .message("Address updated successfully")
                                .results(address.getAddressName())
                                .build()
                        );
            } else {
                // Trả về ResponseEntity chứa thông tin lỗi nếu không tìm thấy địa chỉ id
                return ResponseEntity
                        .badRequest()
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("Address not found")
                                .results("")
                                .build());
            }
        } catch (Exception e) {
            // Trả về ResponseEntity chứa thông tin lỗi nếu có lỗi xảy ra
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build());

        }
    }

    @Override
    public ResponseEntity<?> becomeSellerService(Long userId, BecomeSellerRequest becomeSellerRequest) {
        try{
            UserEntity userEntity = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if(sellerRepository.existsByStoreName(becomeSellerRequest.getStoreName())){
                return ResponseEntity
                        .badRequest()
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("FAIL")
                                        .message("Store name already exists")
                                        .results("")
                                        .build()
                        );
            }
            RoleEntity roleEntity = roleRepository
                    .findByName(ERole.ROLE_SELLER)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            Set<RoleEntity> newListRole = userEntity.getRoles();
            newListRole.add(roleEntity);
            userEntity.setRoles(newListRole);
            userRepository.save(userEntity);
            SellerEntity sellerEntity = new SellerEntity();
            sellerEntity.setUserId(userId);
            sellerEntity.setStoreName(becomeSellerRequest.getStoreName());
            sellerEntity.setStoreAddress(becomeSellerRequest.getStoreAddress());
            sellerRepository.save(sellerEntity);
            User userDTO = mapper.map(userEntity, User.class);
            SellerDTO sellerDTO = mapper.map(sellerEntity, SellerDTO.class);
            ResponseBecomeSeller<User, SellerDTO> responseBecomeSeller = new ResponseBecomeSeller<>();
            responseBecomeSeller.setData(userDTO);
            responseBecomeSeller.setShopData(sellerDTO);
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Register seller success")
                                    .results(responseBecomeSeller)
                                    .build()
                    );
        }catch(Exception e){
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
    public ResponseEntity<?> addAddress(Long id, UpdateAddressDTO updateAddressDTO) {
        try {
            // Tìm kiếm địa chỉ người dùng trong cơ sở dữ liệu bằng userId
            UserEntity userEntity = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            AddressEntity addressEntity = new AddressEntity();
            addressEntity.setAddressName(updateAddressDTO.getAddress());
            addressEntity.setUser(userEntity);
            addressService.save(addressEntity);

            List<AddressEntity> addressEntityList = userEntity.getAddress();
            addressEntityList.add(addressEntity);
            userEntity.setAddress(addressEntityList);
            userRepository.save(userEntity);

            User userDTO = mapper.map(userEntity, User.class);

            // Trả về ResponseEntity chứa thông tin cập nhật thành công
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .status("SUCCESS")
                            .message("Address add successfully")
                            .results(userDTO)
                            .build()
                    );

        } catch (Exception e) {
            // Trả về ResponseEntity chứa thông tin lỗi nếu có lỗi xảy ra
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build());

        }
    }

    @Override
    public ResponseEntity<?> getListSeller(Pageable pageable) {
        try {
            Page<SellerEntity> seller = sellerRepository.findAll(pageable);
            List<Seller> sellerList = seller.getContent().stream().map(s->{
                return mapper.map(s,Seller.class);
            }).toList();

            ResponseData<Object> data = ResponseData.builder().data(sellerList).build();
            PaginationResponse paginationResponse = new PaginationResponse(
                    seller.getTotalPages(),
                    seller.getTotalElements(),
                    seller.getNumber() + 1,
                    seller.getSize()
            );

            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .status("SUCCESS")
                            .message("Get seller successfully")
                            .results(data)
                            .pagination(paginationResponse)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
            );
        }
    }

    @Override
    public ResponseEntity<?> search(String key, Pageable pageable) {
        try{

            // Sử dụng phân trang danh sách users trong truy vấn
            Page<UserEntity> userPage = userRepository.searchUsers(key,pageable);

            List<User> users = userPage.getContent().stream().map(u->{
                return mapper.map(u,User.class);
            }).toList();;

            // Lấy thông tin phân trang từ đối tượng Page
            PaginationResponse paginationResponse = new PaginationResponse(
                    userPage.getTotalPages(),
                    userPage.getTotalElements(),
                    userPage.getNumber()+1,
                    userPage.getSize());
            ResponseData<Object> data = ResponseData.builder().data(users).build();
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .results(data)
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
    public ResponseEntity<?> findUserByUserName(String userName) {
        try {
            // Tìm kiếm người dùng trong cơ sở dữ liệu bằng userId
            Optional<UserEntity> optionalUser = userRepository.findByUserName(userName);

            if (optionalUser.isPresent()) {
                // Lấy đối tượng người dùng từ Optional
                UserEntity userEntity = optionalUser.get();
                User userDTO = mapper.map(userEntity, User.class);
                boolean hasRoleSeller = userEntity.getRoles().stream().anyMatch(item -> item.getName().equals(ERole.ROLE_SELLER));
                if(hasRoleSeller) {
                    SellerEntity sellerEntity = sellerRepository.findByUserId(userEntity.getId())
                            .orElseThrow(() -> new RuntimeException("Seller not found"));
                    SellerDTO sellerDTO = mapper.map(sellerEntity, SellerDTO.class);
                    ResponseDetailUserHasRoleSeller<User, SellerDTO> responseDetailUserHasRoleSeller = new ResponseDetailUserHasRoleSeller<>();
                    responseDetailUserHasRoleSeller.setData(userDTO);
                    responseDetailUserHasRoleSeller.setShopData(sellerDTO);
                    return ResponseEntity.ok().body(ResponseObject
                            .builder()
                            .status("SUCCESS")
                            .message("Get user detail success")
                            .results(responseDetailUserHasRoleSeller)
                            .build()
                    );
                }else {
                    ResponseDetailUser<User> responseDetailUser = new ResponseDetailUser<>();
                    responseDetailUser.setData(userDTO);
                    return ResponseEntity.ok().body(ResponseObject
                            .builder()
                            .status("SUCCESS")
                            .message("Get user detail success !")
                            .results(responseDetailUser)
                            .build()
                    );
                }
            } else {
                // Trả về ResponseEntity chứa thông tin lỗi nếu không tìm thấy người dùng
                return ResponseEntity
                        .badRequest()
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("User not found")
                                .results("")
                                .build());
            }
        } catch (Exception e) {
            // Trả về ResponseEntity chứa thông tin lỗi nếu có lỗi xảy ra
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build());
        }
    }

    @Override
    public ResponseEntity<?> getUserById(Long id) {
        try {
            // Tìm kiếm người dùng trong cơ sở dữ liệu bằng userId
            Optional<UserEntity> optionalUser = userRepository.findById(id);

            if (optionalUser.isPresent()) {
                // Lấy đối tượng người dùng từ Optional
                UserEntity user = optionalUser.get();

                User userDTO = mapper.map(user, User.class);

                ResponseData<Object> data = ResponseData.builder().data(userDTO).build();
                return ResponseEntity.ok().body(
                        ResponseObject.builder()
                                .status("SUCCESS")
                                .message("Get user successfully")
                                .results(data)
                                .build()
                );
            } else {
                // Trả về ResponseEntity chứa thông tin lỗi nếu không tìm thấy người dùng
                return ResponseEntity
                        .badRequest()
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("User not found")
                                .results("")
                                .build());
            }
        } catch (Exception e) {
            // Trả về ResponseEntity chứa thông tin lỗi nếu có lỗi xảy ra
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build());
        }
    }

    private boolean checkPassword(String oldPassword, String password) {
        return passwordEncoder.matches(oldPassword,password);
    }

    @Override
    public Optional<UserEntity> findUserByID(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public ResponseEntity<?> getListUser(Pageable pageable) {
        try {
            Page<UserEntity> users = userRepository.findAll(pageable);
            List<User> userList = users.getContent().stream().map(u->{
               return mapper.map(u,User.class);
            }).toList();

            ResponseData<Object> data = ResponseData.builder().data(userList).build();
            PaginationResponse paginationResponse = new PaginationResponse(
                    users.getTotalPages(),
                    users.getTotalElements(),
                    users.getNumber() + 1,
                    users.getSize()
            );

            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .status("SUCCESS")
                            .message("Get user successfully")
                            .results(data)
                            .pagination(paginationResponse)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
            );
        }
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
