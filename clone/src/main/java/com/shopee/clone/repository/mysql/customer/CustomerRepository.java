package com.shopee.clone.repository.mysql.customer;

import com.shopee.clone.entity.mysql.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
