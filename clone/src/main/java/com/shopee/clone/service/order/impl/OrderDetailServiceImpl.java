package com.shopee.clone.service.order.impl;

import com.shopee.clone.repository.order.OrderDetailRepository;
import com.shopee.clone.service.order.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
}
