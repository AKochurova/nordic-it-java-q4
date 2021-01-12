package com.example.demo;

import com.example.demo.cache.UserDataCache;
import com.example.demo.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Bot extends TelegramWebhookBot {


    private UserDataCache userDataCache = new UserDataCache();
    private ReplyMessageService messageService = new ReplyMessageService();


    public void sendMsg(SendMessage sendMessage) {

        try {
            setButton(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButton(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("/start"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }


    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {


        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatId: {}, with text: {}",
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
        if (botState.equals(BotState.PROFILE_FILLED)) {

            ArrayList<Model> arr = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Model m = new Model();
                arr.add(m);
            }
            try {

                for (int i = 0; i < arr.size(); i++) {
                    sendMsg(messageService.getReplyMessage(chatId, Jobs.getJobs(usersAnswer.getText(), i, arr, profileData.getTown())));
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
        return "MyTestBot";
    }

    @Override
    public String getBotToken() {
        return "1494861198:AAH8K7yIpRcohFyiLB_Ale_UAi_9U3l7RBE";
    }

    @Override
    public String getBotPath() {
        return "https://jobseeker-bot.herokuapp.com/";
    }


}