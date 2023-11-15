package com.shopee.clone.service.auth.impl;

import com.shopee.clone.DTO.auth.login.LoginDTO;
import com.shopee.clone.DTO.auth.refresh_token.RefreshTokenResponse;
import com.shopee.clone.DTO.auth.register.AddChatIdRequest;
import com.shopee.clone.DTO.auth.register.RegisterDTO;
import com.shopee.clone.DTO.auth.user.User;
import com.shopee.clone.DTO.seller.SellerDTO;
import com.shopee.clone.entity.*;
import com.shopee.clone.repository.RefreshTokenRepository;
import com.shopee.clone.repository.RoleRepository;
import com.shopee.clone.repository.SellerRepository;
import com.shopee.clone.repository.UserRepository;
import com.shopee.clone.response.auth.login.ResponseLogin;
import com.shopee.clone.response.auth.login.ResponseLoginHasRoleSeller;
import com.shopee.clone.security.impl.UserDetailImpl;
import com.shopee.clone.service.auth.IAuthService;
import com.shopee.clone.service.user.UserService;
import com.shopee.clone.util.JWTProvider;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;
    private final ModelMapper mapper;
    private final UserService userService;
    private final SellerRepository sellerRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JWTProvider jwtProvider,
                       ModelMapper mapper,
                       UserService userService,
                       SellerRepository sellerRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.mapper = mapper;
        this.userService = userService;
        this.sellerRepository = sellerRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<?> login(LoginDTO loginDTO) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getEmail(), loginDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = jwtProvider.generateJwtToken(authentication);
            UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
            UserEntity userEntity = userRepository.findById(userDetail.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            User userDTO = mapper.map(userEntity, User.class);
            boolean hasRoleSeller = userEntity.getRoles().stream().anyMatch(item -> item.getName().equals(ERole.ROLE_SELLER));
            if(hasRoleSeller) {
                SellerEntity sellerEntity = sellerRepository.findByUserId(userEntity.getId())
                        .orElseThrow(() -> new RuntimeException("Seller not found"));
                if (userEntity.getChatId() != null){
                    sellerEntity.setChatId(userEntity.getChatId());
                }
                sellerRepository.save(sellerEntity);
                SellerDTO sellerDTO = mapper.map(sellerEntity, SellerDTO.class);
                ResponseLoginHasRoleSeller<User, SellerDTO> responseLoginHasRoleSeller = new ResponseLoginHasRoleSeller<>();
                responseLoginHasRoleSeller.setData(userDTO);
                responseLoginHasRoleSeller.setShopData(sellerDTO);
                responseLoginHasRoleSeller.setAccessToken(accessToken);
                responseLoginHasRoleSeller.setRefreshToken(jwtProvider.createRefreshToken(userDetail.getId()).getRefreshToken());
                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("Login Success !")
                        .results(responseLoginHasRoleSeller)
                        .build()
                );
            }else {
                ResponseLogin<User> responseLogin = new ResponseLogin<>();
                responseLogin.setData(userDTO);
                responseLogin.setAccessToken(accessToken);
                responseLogin.setRefreshToken(jwtProvider.createRefreshToken(userDetail.getId()).getRefreshToken());
                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("Login Success !")
                        .results(responseLogin)
                        .build()
                );
            }
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject
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
    public ResponseEntity<?> register(RegisterDTO registerDTO) {
        try {
            if(!registerDTO.getPassword().equals(registerDTO.getRePassword())){
                return ResponseEntity
                        .badRequest()
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("FAIL")
                                        .message("Re-password not match")
                                        .results("")
                                        .build()
                        );
            }

            registerDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
            RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role not found !"));
            Set<RoleEntity> roles = new HashSet<>();
            roles.add(userRole);
            UserEntity newUser = mapper.map(registerDTO, UserEntity.class);
            newUser.setRoles(roles);
            newUser.setStatus(true);

            UserEntity userCreated = userService.save(newUser);
            return ResponseEntity
                    .ok()
                    .body(ResponseObject
                            .builder()
                            .status("SUCCESS")
                            .message("Register Success")
                            .results(userCreated)
                            .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject
                            .builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
                    );
        }
    }

    @Override
    public ResponseEntity<?> addChatId(Long id, AddChatIdRequest addChatIdRequest) {
        try{
            UserEntity userEntity = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            userEntity.setChatId(addChatIdRequest.getChatId());
            userRepository.save(userEntity);
            User user = mapper.map(userEntity, User.class);
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Add chat id success")
                                    .results(user)
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
    public ResponseEntity<?> refreshToken(String refreshToken) {
        try{
            RefreshTokenResponse response =  refreshTokenRepository.findByRefreshToken(refreshToken)
                    .map(jwtProvider::verifyRefreshTokenExpiration)
                    .map(RefreshTokenEntity::getUser)
                    .map(userEntity -> {
                        jwtProvider.deleteRefreshToken(refreshToken);
                        String accessToken = jwtProvider.generateJwtTokenFromUserName(userEntity.getUserName());
                        String newRefreshToken = jwtProvider.createRefreshToken(userEntity.getId()).getRefreshToken();
                        return RefreshTokenResponse
                                .builder()
                                .accessToken(accessToken)
                                .refreshToken(newRefreshToken)
                                .build();
                    })
                    .orElseThrow(() -> new RuntimeException("Refresh Token not found !"));
            return ResponseEntity.ok().body(ResponseObject
                                                .builder()
                                                .status("SUCCESS")
                                                .message("Get refresh token success !")
                                                .results(response)
                                                .build());
        }catch (Exception e){
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
}
