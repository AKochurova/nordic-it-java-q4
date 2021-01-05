package com.example.demo.handlers;

import com.example.demo.BotState;
import com.example.demo.Jobs;
import com.example.demo.Model;
import com.example.demo.service.ReplyMessageService;
import com.example.demo.cache.UserDataCache;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;

@Component
public class FillingProfileHandle implements InputMessageHandler{
    private UserDataCache userDataCache;
    private ReplyMessageService messageService;

    public FillingProfileHandle(UserDataCache userDataCache, ReplyMessageService messageService){
        this.userDataCache = userDataCache;
        this.messageService = messageService;

    }


    @Override
    public SendMessage handle(Message message) {
        if(userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)){
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.CHOOSE_CITY);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMsg){
        Message usersAnswer = inputMsg;
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.CHOOSE_CITY)){
            replyToUser = messageService.getReplyMessage(chatId, "choose city");
            userDataCache.setUsersCurrentBotState(userId, BotState.FIND_JOB);
        }
        if (botState.equals(BotState.FIND_JOB)){
            profileData.setTown(usersAnswer.getText());
            replyToUser = messageService.getReplyMessage(chatId, "choose job");
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
        }
        if (botState.equals(BotState.PROFILE_FILLED)){

            ArrayList<Model> arr = new ArrayList<>();
            for(int i=0; i<5; i++){
                Model m = new Model();
                arr.add(m);
            }
            try {

                for (int i=0; i<arr.size(); i++) {
                   replyToUser = messageService.getReplyMessage(chatId, Jobs.getJobs(usersAnswer.getText(), arr, i, profileData.getTown()));
                }
            }catch (Exception e){
                replyToUser = messageService.getReplyMessage(userId, " Not found");
            }
            profileData.setJob(usersAnswer.getText());
            userDataCache.setUsersCurrentBotState(userId, BotState.FILLING_PROFILE);


        }
        userDataCache.saveUserProfileData(userId, profileData);

        return replyToUser;
    }

}
