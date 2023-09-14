package com.shopee.clone.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "categories")
@Data
@Builder
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content")
    private String content;
    @Column(name = "image_public_id")
    private String imagePublicId;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "left_position")
    private Integer left;
    @Column(name = "right_position")
    private Integer right;
}
