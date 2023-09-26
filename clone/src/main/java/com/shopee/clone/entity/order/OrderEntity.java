package com.shopee.clone.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shopee.clone.entity.AddressEntity;
import com.shopee.clone.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;
    private Date date;
    @OneToMany(mappedBy = "order")
    private List<OrderDetailEntity> orderDetails;
    @Column(name = "payment")
    @Enumerated(EnumType.STRING)
    private EPayment payment;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EOrder status;
}
