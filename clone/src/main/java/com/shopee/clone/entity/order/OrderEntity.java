package com.shopee.clone.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shopee.clone.entity.AddressEntity;
import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private SellerEntity seller;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;
    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER)
    private List<OrderDetailEntity> orderDetails;
    private Long promotionId;
    private Double discount = 0.0;
    private Date date;
    private Boolean paymentStatus;
    private Double shipMoney = 0.0;
    private LocalDateTime noteTimeRecipient;
    private int orderNumber;
    private Date confirmDate;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EOrder status;
}
