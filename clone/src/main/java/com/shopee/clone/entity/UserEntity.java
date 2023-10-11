package com.shopee.clone.entity;

import com.shopee.clone.entity.promotion.PromotionBeLongUserEntity;
import com.shopee.clone.entity.promotion.PromotionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "users")
//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "img_public_id")
    private String imgPublicId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AddressEntity> address = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_has_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();
    private boolean status;
    @Column(name = "chat_id")
    private String chatId;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private Set<PromotionBeLongUserEntity> promotionEntities = new HashSet<>();
}
