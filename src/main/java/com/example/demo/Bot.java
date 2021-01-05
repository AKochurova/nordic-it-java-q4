package com.example.demo;

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


@Component
public class Bot extends TelegramWebhookBot {

    private String botPath;
    private String botToken;
    private String botUsername;


    private String town = "Кемерово";



    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void sendMsg(Message message, String text){
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
        keyboardFirstRow.add(new KeyboardButton("/найти работу"));
        keyboardFirstRow.add(new KeyboardButton("/изменить город"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }




    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        Message message = update.getMessage();


        ArrayList<Model> arr = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Model m = new Model();
            arr.add(m);
        }

        if (message != null && message.hasText()) {

            /*if(message.getText().equals("/найти работу")){
                sendMsg(message, "Введите город");
            }
            else setTown(message.getText());
            if (town != null){
                try {

                    for (int i = 0; i < arr.size(); i++) {
                        sendMsg(message, Jobs.getJobs(message.getText(), arr, i, town));
                    }
                } catch (Exception e) {
                    sendMsg(message, " Not found");
                }
            }*/
            switch (message.getText()) {

                case "/найти работу":
                    sendMsg(message, "Введите город");
                    break;

                /*case "/выбрать город":
                    setTown(message.getText());
                    break;*/

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