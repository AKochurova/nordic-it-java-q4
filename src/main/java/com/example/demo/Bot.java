package com.example.demo;
import com.example.demo.cache.UserDataCache;
import com.example.demo.handlers.UserProfileData;

import com.example.demo.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
/*import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;*/
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;


@Component
public class Bot extends TelegramWebhookBot {

    private String botPath;
    private String botToken;
    private String botUsername;



    private UserDataCache userDataCache = new UserDataCache();
    private ReplyMessageService messageService = new ReplyMessageService();
    private String town = "Кемерово";

    /*private TelegramFacade telegramFacade;

    public Bot(DefaultBotOptions botOptions, TelegramFacade telegramFacade){
        super(botOptions);
        this.telegramFacade=telegramFacade;

    }*/

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

   /* public void sendMsg(Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try{
            setButton(sendMessage);
            execute(sendMessage);

        }catch (TelegramApiException e){
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
        keyboardFirstRow.add(new KeyboardButton("/помощь"));
        //keyboardFirstRow.add(new KeyboardButton("/изменить город"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }*/
   public void sendMsg(SendMessage sendMessage){


       try{

           execute(sendMessage);

       }catch (TelegramApiException e){
           e.printStackTrace();
       }
   }



    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
       /* Message message = update.getMessage();


        ArrayList<Model> arr = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Model m = new Model();
            arr.add(m);
        }

        if (message != null && message.hasText()) {

            switch (message.getText()) {

                case "/найти работу":
                    sendMsg(message, "Введите город");

                    break;
                default:

                    try {

                        for (int i = 0; i < arr.size(); i++) {
                            sendMsg(message, Jobs.getJobs(message.getText(), arr, i, town));
                        }
                    } catch (Exception e) {
                        sendMsg(message, " Not found");
                    }
            }


        }
        return null;

    }*/
        SendMessage replyMessage = null;

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            //log.info("New message from User:{}, chatId: {}, with text: {}",
            //message.getFrom().getUserName(), message.getChatId(), message.getText());
            /*replyMessage =*/ handleInputMessage(message);


        }
        return /*replyMessage*/null;
    }


        /*SendMessage replyMessageToUser = telegramFacade.handleUpdate(update);

        return replyMessageToUser;*/



    public /*SendMessage*/void handleInputMessage(Message message){
            String inputMsg = message.getText();
            int userId = message.getFrom().getId();

            BotState botState;

            SendMessage replyMessage;

            switch (inputMsg) {
                case "/help":
                    botState = BotState.SEND_HELP;
                    break;
                case "/start":
                    botState = BotState.FILLING_PROFILE;
                    break;
                default:
                    botState = userDataCache.getUsersCurrentBotState(userId);
                    break;
            }

            userDataCache.setUsersCurrentBotState(userId, botState);
            //InputMessageHandler currentMessageHandler = new FillingProfileHandle(userDataCache, messageService);
           /* replyMessage = currentMessageHandler.*/handle(message);

            //return replyMessage;
        }



    public /*SendMessage*/void handle(Message message) {
        if(userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)){
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.CHOOSE_CITY);
        }
        /*return*/ processUsersInput(message);
    }

    private /*SendMessage*/void   processUsersInput(Message inputMsg){
        Message usersAnswer = inputMsg;
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.CHOOSE_CITY)){
            /*replyToUser =*/ sendMsg(messageService.getReplyMessage(chatId, "choose city"));

            userDataCache.setUsersCurrentBotState(userId, BotState.FIND_JOB);
        }
        if (botState.equals(BotState.FIND_JOB)){
            profileData.setTown(usersAnswer.getText());
            sendMsg(messageService.getReplyMessage(chatId, "choose job"+profileData.getTown()));
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
                    sendMsg(messageService.getReplyMessage(chatId, Jobs.getJobs(usersAnswer.getText(), i, arr,  profileData.getTown())));
                }
            }catch (Exception e){
                sendMsg(messageService.getReplyMessage(userId, " Not found"+usersAnswer.getText()));
            }
            profileData.setJob(usersAnswer.getText());
            userDataCache.setUsersCurrentBotState(userId, BotState.FILLING_PROFILE);


        }
        userDataCache.saveUserProfileData(userId, profileData);

        //return replyToUser;
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