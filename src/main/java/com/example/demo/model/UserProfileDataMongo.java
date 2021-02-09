package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@Document(collection = "userProfileData")
public class UserProfileDataMongo implements Serializable {
    @Id
    private String id;
    private String password;
    private String login;
    private long userId;

}


