package com.example.demo.service;

import com.example.demo.model.UserProfileDataMongo;
import com.example.demo.repository.UsersProfileMongoRepository;
import org.springframework.stereotype.Service;

@Service
public class UserProfileDataService {
    private UsersProfileMongoRepository profileMongoRepository;

    public UserProfileDataService(UsersProfileMongoRepository profileMongoRepository){
        this.profileMongoRepository = profileMongoRepository;
    }

    public void saveUserProfileData(UserProfileDataMongo profileDataId){
        profileMongoRepository.save(profileDataId);
    }

    public UserProfileDataMongo getUserProfileData(long userId){
        return profileMongoRepository.findByUserId(userId);
    }
}
