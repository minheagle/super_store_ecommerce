package com.shopee.clone.service.auth.impl;

import com.shopee.clone.DTO.auth.login.LoginDTO;
import com.shopee.clone.DTO.auth.login.LoginResponse;
import com.shopee.clone.DTO.auth.refresh_token.RefreshTokenResponse;
import com.shopee.clone.DTO.auth.register.RegisterDTO;
import com.shopee.clone.entity.*;
import com.shopee.clone.repository.RefreshTokenRepository;
import com.shopee.clone.repository.RoleRepository;
import com.shopee.clone.repository.UserRepository;
import com.shopee.clone.security.impl.UserDetailImpl;
import com.shopee.clone.service.address.AddressService;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final AddressService addressService;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JWTProvider jwtProvider,
                       ModelMapper mapper, UserService userService, AddressService addressService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.mapper = mapper;
        this.userService = userService;
        this.addressService = addressService;
    }

    @Override
    public ResponseEntity<?> login(LoginDTO loginDTO) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getEmail(), loginDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtProvider.generateJwtToken(authentication);
            UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
            LoginResponse loginResponse = UserDetailImpl.convertAuthPrincipalToLoginResponse(userDetail);
            loginResponse.setAccessToken(accessToken);
            loginResponse.setRefreshToken(jwtProvider.createRefreshToken(userDetail.getId()).getRefreshToken());
            return ResponseEntity.ok().body(ResponseObject
                    .builder()
                    .status("SUCCESS")
                    .message("Login Success !")
                    .results(loginResponse)
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
    @Transactional
    public ResponseEntity<?> register(RegisterDTO registerDTO) {
        try {
            registerDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
            Set<String> stringRoles = registerDTO.getRole();
            Set<RoleEntity> roles = new HashSet<>();
            stringRoles.forEach(role -> {
                switch (role){
                    case "ROLE_ADMIN":
                        RoleEntity adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                        .orElseThrow(() -> new RuntimeException("Role not found !"));
                        roles.add(adminRole);
                        break;
                    case "ROLE_SELLER" :
                        RoleEntity sellerRole = roleRepository.findByName(ERole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Role not found !"));
                        roles.add(sellerRole);
                        break;
                    default:
                        RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Role not found !"));
                        roles.add(userRole);
                }
            });
            UserEntity newUser = mapper.map(registerDTO, UserEntity.class);
            newUser.setRoles(roles);
            newUser.setStatus(true);
//            newUser.setAddress();
            UserEntity userCreated = userService.save(newUser);
//                    userRepository.save(newUser);
            Set<String> stringAddress = registerDTO.getAddress();
            List<AddressEntity> address = stringAddress.stream()
                    .map(s -> new AddressEntity(s,userCreated)).toList();
            addressService.saveAll(address);
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

    @Override
    public ResponseEntity<?> logout(long user_id) {
        try{
            UserEntity userEntity = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found !"));
            refreshTokenRepository.deleteByUser(userEntity);
            return ResponseEntity.ok().body(ResponseObject.builder().status("SUCCESS").message("Logout success").results(""));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(ResponseObject.builder().status("FAIL").message(e.getMessage()).results(""));
        }
    }
}
