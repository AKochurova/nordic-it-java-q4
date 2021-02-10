package com.example.demo.repository;

import com.example.demo.model.UserProfileDataMongo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersProfileMongoRepository extends JpaRepository<UserProfileDataMongo, String> {
    UserProfileDataMongo findByChatId(long chatId);
}
