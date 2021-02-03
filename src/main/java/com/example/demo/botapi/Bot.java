package com.example.demo.botapi;

import com.example.demo.cache.Aouth;
import com.example.demo.cache.UserDataCache;
import com.example.demo.cache.UserProfileData;
import com.example.demo.service.ReplyMessageService;
import com.example.demo.superjobapi.Jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Slf4j
@Component
@PropertySource("classpath:application.properties")
public class Bot extends TelegramWebhookBot {

    @Autowired
    private UserDataCache userDataCache;
    @Autowired
    private ReplyMessageService messageService;

    private Aouth ao;
    
    @Value("${telegram.username}")
    private String botUsername;
    @Value("${telegram.webhook}")
    private String botPath;
    @Value("${telegram.token}")
    private String botToken;

    public void sendMsg(SendMessage sendMessage) {

        Buttons buttons = new Buttons();

        try {
            buttons.setButton(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения пользователю");
        }
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("Новое сообщение от пользователя:{}, chatId: {}, with text: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            handleInputMessage(message);

        }
        return null;
    }

    public void handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();

        BotState botState;

        switch (inputMsg) {
            case "/дальше":
                botState = BotState.GET_CODE;
                break;
            case "/Добавить в избранное":
                botState = BotState.ADD_FAVORITE;
                break;
            case "/start":
                botState = BotState.FILLING_PROFILE;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);
        handle(message);

    }


    public void handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.CHOOSE_CITY);
        }
        processUsersInput(message);
    }

    private void processUsersInput(Message inputMsg) {
        Message usersAnswer = inputMsg;
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);


        if (botState.equals(BotState.CHOOSE_CITY)) {
            sendMsg(messageService.getReplyMessage(chatId, "Введите город"));

            userDataCache.setUsersCurrentBotState(userId, BotState.FIND_JOB);
        }
        if (botState.equals(BotState.FIND_JOB)) {
            profileData.setTown(usersAnswer.getText());
            sendMsg(messageService.getReplyMessage(chatId, "Введите работу"));
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
        }
        if(botState.equals(BotState.ADD_FAVORITE)){
            String str = "https://www.superjob.ru/authorize/?client_id=1599&redirect_uri=https://jobseeker-bot.herokuapp.com/getcode/"+userId;
            sendMsg(messageService.getReplyMessage(chatId, "Авторизируйтесь на SJ:\n "+str));
            userDataCache.setUsersCurrentBotState(userId, BotState.FILLING_PROFILE);
        }
        if(botState.equals(BotState.GET_CODE)){
            sendMsg(messageService.getReplyMessage(chatId, ao.getUsersCodes((""+userId))));
            userDataCache.setUsersCurrentBotState(userId, BotState.FILLING_PROFILE);
        }
        if (botState.equals(BotState.PROFILE_FILLED)) {


            try {

                for (int i = 0; i < 5; i++) {
                    sendMsg(messageService.getReplyMessage(chatId, Jobs.getJobs(usersAnswer.getText(), i, profileData.getTown())));
                }
            } catch (Exception e) {
                sendMsg(messageService.getReplyMessage(userId, " Не найдено: " + usersAnswer.getText()));
            }
            profileData.setJob(usersAnswer.getText());
            userDataCache.setUsersCurrentBotState(userId, BotState.FILLING_PROFILE);


        }
        userDataCache.saveUserProfileData(userId, profileData);

    }


    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    @Override
    public String getBotPath() {
        return this.botPath;
    }


}