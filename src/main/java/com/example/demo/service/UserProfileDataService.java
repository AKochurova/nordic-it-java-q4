package com.example.demo.service;

import com.example.demo.model.UserProfileDataMongo;
import com.example.demo.repository.UsersProfileMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileDataService {
    @Autowired
    private UsersProfileMongoRepository profileMongoRepository;


    public void saveUserProfileData(UserProfileDataMongo profileDataId){
        profileMongoRepository.save(profileDataId);
    }

    public UserProfileDataMongo getUserProfileData(long userId){
        return profileMongoRepository.findByUserId(userId);
    }
}
