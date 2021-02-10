package com.example.demo.repository;

import com.example.demo.model.UserProfileDataMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersProfileMongoRepository extends MongoRepository<UserProfileDataMongo, String> {
    UserProfileDataMongo findByUserId(long userId);
}
