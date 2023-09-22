package com.shopee.clone.service.user.impl;

import com.shopee.clone.DTO.auth.user.*;
import com.shopee.clone.entity.AddressEntity;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.repository.UserRepository;
import com.shopee.clone.response.user.ResponseDetailUser;
import com.shopee.clone.service.address.AddressService;
import com.shopee.clone.service.user.UserService;
import com.shopee.clone.util.JWTProvider;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressService addressService;

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper mapper;

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
                user.setAvatar(userUpdateDTO.getAvatar());
                user.setEmail(userUpdateDTO.getEmail());
                user.setDataOfBirth(userUpdateDTO.getDateOfBirth());

                // Lưu thông tin người dùng đã cập nhật vào cơ sở dữ liệu
                UserEntity userCreated= userRepository.save(user);

                // Trả về ResponseEntity chứa thông tin cập nhật thành công
                return ResponseEntity.ok().body(new ResponseObject("SUCCESS",
                        "User updated successfully",user));
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
                return ResponseEntity.ok().body(new ResponseObject("SUCCESS",
                        "Ban User successfully",user));
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
                return ResponseEntity.ok().body(new ResponseObject("SUCCESS",
                        "unBan User successfully",user));
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
    public ResponseEntity<?> changePassword( Long id,ChangePasswordDTO changePasswordDTO) {
        try {

            // Tìm kiếm người dùng trong cơ sở dữ liệu bằng userId
            Optional<UserEntity> optionalUser = userRepository.findById(id);

            if (optionalUser.isPresent()) {
                // Lấy đối tượng người dùng từ Optional
                UserEntity user = optionalUser.get();

                if(checkPassword(changePasswordDTO.getOutPassword(),user.getPassword())){
                    if(changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())){
                        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
                        // Lưu thông tin người dùng đã cập nhật vào cơ sở dữ liệu
                        userRepository.save(user);

                        // Trả về ResponseEntity chứa thông tin cập nhật thành công
                        return ResponseEntity.ok().body(new ResponseObject("SUCCESS",
                                "Change password successfully",changePasswordDTO.getNewPassword()));
                    }
                    // Trả về ResponseEntity chứa thông tin lỗi
                    return ResponseEntity.ok().body(new ResponseObject("Fail",
                            "Confirm password Error","newPass: "+changePasswordDTO.getNewPassword())+ "" +
                            " ConfirmPassword: "+changePasswordDTO.getConfirmPassword());
                };

                // Trả về ResponseEntity chứa thông tin lỗi nêú sai mật khẩu
                return ResponseEntity.ok().body(new ResponseObject("Fail",
                        "OutPassword error",changePasswordDTO.getOutPassword()));
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
            // Tìm kiếm người dùng trong cơ sở dữ liệu bằng userId
            Optional<AddressEntity> optionalAddress = addressService.findById(id);

            if (optionalAddress.isPresent()) {
                // Lấy đối tượng người dùng từ Optional
                AddressEntity address = optionalAddress.get();

                // Cập nhật thông tin người dùng từ DTO (Data Transfer Object)
                address.setAddressName(updateAddressDTO.getAddress());

                // Lưu thông tin địa chỉ người dùng đã cập nhật vào cơ sở dữ liệu
                addressService.save(address);

                // Trả về ResponseEntity chứa thông tin cập nhật thành công
                return ResponseEntity.ok().body(new ResponseObject("SUCCESS",
                        "Address updated successfully",address.getAddressName()));
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
    public ResponseEntity<?> findUserByUserName(String userName) {
        try {
            // Tìm kiếm người dùng trong cơ sở dữ liệu bằng userId
            Optional<UserEntity> optionalUser = userRepository.findByUserName(userName);

            if (optionalUser.isPresent()) {
                // Lấy đối tượng người dùng từ Optional
                UserEntity user = optionalUser.get();
                User userDTO = mapper.map(user, User.class);
                ResponseDetailUser<User> responseDetailUser = new ResponseDetailUser<>();
                responseDetailUser.setData(userDTO);

                // Trả về ResponseEntity chứa thông tin cập nhật thành công
                return ResponseEntity
                        .ok()
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Get user success")
                                        .results(responseDetailUser)
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
    public ResponseEntity<?> getUserById(Long id) {
        try {
            // Tìm kiếm người dùng trong cơ sở dữ liệu bằng userId
            Optional<UserEntity> optionalUser = userRepository.findById(id);

            if (optionalUser.isPresent()) {
                // Lấy đối tượng người dùng từ Optional
                UserEntity user = optionalUser.get();

                // Lưu thông tin người dùng đã cập nhật vào cơ sở dữ liệu
                userRepository.save(user);

                // Trả về ResponseEntity chứa thông tin cập nhật thành công
                return ResponseEntity.ok().body(new ResponseObject("SUCCESS",
                        "Get user successfully",user));
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
    public ResponseEntity<?> becomeSellerService(BecomeSellerRequest becomeSellerRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> findUserByUserName(String userName) {
        try {
            // Tìm kiếm người dùng trong cơ sở dữ liệu bằng userId
            Optional<UserEntity> optionalUser = userRepository.findByUserName(userName);

            if (optionalUser.isPresent()) {
                // Lấy đối tượng người dùng từ Optional
                UserEntity user = optionalUser.get();

                // Lưu thông tin người dùng đã cập nhật vào cơ sở dữ liệu
                userRepository.save(user);

                // Trả về ResponseEntity chứa thông tin cập nhật thành công
                return ResponseEntity.ok().body(new ResponseObject("SUCCESS",
                        "Get user successfully",user));
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

                // Lưu thông tin người dùng đã cập nhật vào cơ sở dữ liệu
                userRepository.save(user);

                // Trả về ResponseEntity chứa thông tin cập nhật thành công
                return ResponseEntity.ok().body(new ResponseObject("SUCCESS",
                        "Get user successfully",user));
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

    private boolean checkPassword(String outPassword, String password) {
        return passwordEncoder.matches(outPassword,password);
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
    public List<User> getListUser() {
        return userRepository.findAll().stream().map(userEntity ->
                mapper.map(userEntity,User.class)).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
