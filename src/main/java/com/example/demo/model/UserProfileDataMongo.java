package com.example.demo.model;


import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;



@Entity
@Table(name = "users")
@Getter
@Setter
//@Document(collection = "userProfileData")
public class UserProfileDataMongo extends AbstractBaseEntity{

    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "login", nullable = false)
    private String login;
    @Column(name = "chat_id", unique = true, nullable = false)
    private long userId;

}


