package com.example.demo.botapi;

import com.example.demo.cache.Aouth;
import com.example.demo.cache.UserAouthData;
import com.example.demo.cache.UserDataCache;
import com.example.demo.cache.UserProfileData;
import com.example.demo.service.ReplyMessageService;
import com.example.demo.superjobapi.Jobs;

import com.example.demo.superjobapi.Tokens;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;


@Slf4j
@Component
@PropertySource("classpath:application.properties")
public class Bot extends TelegramWebhookBot {

    @Autowired
    private UserDataCache userDataCache;
    @Autowired
    private ReplyMessageService messageService;
    @Autowired
    private Buttons buttons;


    @Value("${telegram.username}")
    private String botUsername;
    @Value("${telegram.webhook}")
    private String botPath;
    @Value("${telegram.token}")
    private String botToken;

    public void sendMsg(SendMessage sendMessage) {

        try {
            buttons.setReplyButton(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения пользователю");
        }
    }

    public void sendInlineButtons(long chatId, String messageText, String buttonText, String callbackData) {
        InlineKeyboardMarkup inlineKeyboardMarkup = buttons.setInlineButton(buttonText, callbackData);
        try {
            execute(new SendMessage().setChatId(chatId).setText(messageText).setReplyMarkup(inlineKeyboardMarkup));
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки callback пользователю");
        }
    }

    public void answerCallbackQuery(String callbackId, String message) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackId);
        answer.setText(message);
        answer.setShowAlert(true);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки ответа на callback пользователю");
        }
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        if (update.hasCallbackQuery()) {
            log.info("Callback от пользователя: {}, сообщение: {} ", update.getCallbackQuery().getFrom().getUserName(), update.getCallbackQuery().getData());
            handleCallbackQuery(update.getCallbackQuery());
        }
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("Новое сообщение от пользователя:{}, chatId: {}, сообщение: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            handleInputMessage(message);
        }
        return null;
    }

    public void handleCallbackQuery(CallbackQuery callbackQuery) {
        int userId = callbackQuery.getFrom().getId();
        Long chatId = callbackQuery.getMessage().getChatId();
        //String str = "https://www.superjob.ru/authorize/?client_id=1599&redirect_uri=https://jobseeker-bot.herokuapp.com/getcode/" + userId;
        switch (callbackQuery.getData()) {
            /*case "favlist":

                try {
                    sendMsg(messageService.getReplyMessage(chatId, "Избранные вакансии: \n"+Tokens.getFavsList(userId)));
                }catch (IOException e){
                    log.error("Не удалось отправить список вакансий");
                }
                break;*/
            case "next":
                try {
                    if (Tokens.getFavs(userDataCache.getUserAouth(userId).getLogin(),
                            userDataCache.getUserAouth(userId).getPassword(), userDataCache.getUsersFavId(userId)))
                        answerCallbackQuery(callbackQuery.getId(), "Вакансия добавлена в избранное");
                    else answerCallbackQuery(callbackQuery.getId(), "Произошла ошибка");
                } catch (IOException e) {
                    log.error("error");
                }
                break;
            default:
                if (userDataCache.getUserAouth(userId).getLogin()==null){
                    userDataCache.setUsersCurrentBotState(userId, BotState.GET_LOGIN);
                    userDataCache.setUsersFavId(userId, callbackQuery.getData());
                    sendMsg(messageService.getReplyMessage(callbackQuery.getMessage().getChatId(), "Авторизируйтесь на SJ, введите логин\n"));
                    break;
                }
               // sendMsg(messageService.getReplyMessage(callbackQuery.getMessage().getChatId(), "Авторизируйтесь на SJ:\n" + str));
                userDataCache.setUsersFavId(userId, callbackQuery.getData());
                sendInlineButtons(chatId, "Нажмите чтобы продолжить", "Далее", "next");
                break;
        }

    }

    public void handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();

        BotState botState;

        switch (inputMsg) {

            case "/список избранных вакансий":
                botState = BotState.GET_FAVSLIST;
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
        UserAouthData userAouthData = userDataCache.getUserAouth(userId);
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

        if (botState.equals(BotState.GET_FAVSLIST)) {
            /*String str = "https://www.superjob.ru/authorize/?client_id=1599&redirect_uri=https://jobseeker-bot.herokuapp.com/getcode/" + userId;
            sendMsg(messageService.getReplyMessage(chatId,"Авторизируйтесь на SJ:\n" + str));
            sendInlineButtons(chatId, "Нажмите чтобы продолжить", "Далее", "favlist");*/
            userDataCache.setUsersCurrentBotState(userId, BotState.FILLING_PROFILE);
            try {
                sendMsg(messageService.getReplyMessage(chatId, "Избранные вакансии: \n"+Tokens.getFavsList(userAouthData.getLogin(),
                        userAouthData.getPassword())));
            }catch (IOException e){
                log.error("Не удалось отправить список вакансий");
            }
        }
        if(botState.equals(BotState.GET_LOGIN)){
            userAouthData.setLogin(usersAnswer.getText());
            sendMsg(messageService.getReplyMessage(chatId, "Введите пароль:"));
            userDataCache.setUsersCurrentBotState(userId, BotState.GET_PASSWORD);
        }
        if(botState.equals(BotState.GET_PASSWORD)){
            try {
                Tokens.getTokens(userAouthData.getLogin(), usersAnswer.getText());
                sendMsg(messageService.getReplyMessage(chatId, "Вы авторизированы"));
                userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
                sendInlineButtons(chatId, "Нажмите чтобы продолжить", "Далее", "next");
                userAouthData.setPassword(usersAnswer.getText());
            } catch (IOException e) {
                log.error("Авторизация не прошла, пароль "+usersAnswer.getText()+" login "+userAouthData.getLogin());
                sendMsg(messageService.getReplyMessage(chatId, "Введите логин и пароль заново"));
                userDataCache.setUsersCurrentBotState(userId, BotState.GET_LOGIN);
            }
        }
        if (botState.equals(BotState.PROFILE_FILLED)) {
            try {
                for (int i = 0; i < 5; i++) {
                    sendInlineButtons(chatId, Jobs.getJobs(usersAnswer.getText(), i, profileData.getTown()), "Добавить в избранное", Jobs.getFavorites(usersAnswer.getText(), i, profileData.getTown()));
                }
            } catch (Exception e) {
                sendMsg(messageService.getReplyMessage(userId, " Не найдено: " + usersAnswer.getText()));
            }
            profileData.setJob(usersAnswer.getText());
            userDataCache.setUsersCurrentBotState(userId, BotState.FILLING_PROFILE);
        }
        userDataCache.saveUserProfileData(userId, profileData);
        userDataCache.saveUserAouth(userId, userAouthData);

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