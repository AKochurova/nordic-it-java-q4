package com.example.demo.model;


import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Data
@Document(collection = "userProfileData")
public class UserProfileDataMongo implements Serializable {

    @Id
    private String id;
    private String password;
    private String login;
    private long userId;

}


