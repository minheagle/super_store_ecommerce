package com.shopee.clone.repository.mongodb.user;

import com.shopee.clone.entity.mongodb.user.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByEmail(String email);
}
