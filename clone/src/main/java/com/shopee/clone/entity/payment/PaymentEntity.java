package com.shopee.clone.entity.payment;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long paymentId;
    private Long userId;
    private Integer orderNumber;
    private Integer paymentAmount;
    @Column(name = "description")
    private String desc_payment;
    private LocalDateTime paymentDate;
    private Boolean paymentStatus;
}
