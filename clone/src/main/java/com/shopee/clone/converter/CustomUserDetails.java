package com.shopee.clone.converter;

import com.shopee.clone.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class CustomUserDetails implements UserDetails {
    private final Optional<UserEntity> userEntity;
    @Getter
    private Long id;

    public CustomUserDetails(Optional<UserEntity> userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Lấy danh sách roles từ UserEntity
        userEntity.ifPresent(entity -> {
            List<String> roles = Collections.singletonList(entity.getRole());
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return userEntity.get().getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.get().getEmail(); // Hoặc tên đăng nhập tương ứng
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
