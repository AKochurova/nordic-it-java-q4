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


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class Bot extends TelegramWebhookBot {

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
        keyboardFirstRow.add(new KeyboardButton("/help"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }


    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        Message message = update.getMessage();
        Model model = new Model();
        ArrayList<Model> arr = new ArrayList<>();
        for(int i=0; i<5; i++){
            Model m = new Model();
            arr.add(m);
        }

        if (message != null && message.hasText()){
            switch (message.getText()){
                case "/помощь":
                    sendMsg(message, "Введите название нужной вам специализации");
                    break;
                default:
                    try {
                        String str="https://api.superjob.ru/2.0/v3.r.133386385.0a3795f1baaab6cb9057bbd1af19f5b2ba967a72.c20cacaa5da55fe4c622288ac5fbbde1ed74759f/vacancies/?keyword="+message;
                        URL url = new URL(str);


                        Scanner scanner = new Scanner((InputStream)url.getContent());
                        String result = "";


                        result += scanner.nextLine();

                        for (int i=0; i<arr.size(); i++) {
                            sendMsg(message, Jobs.getJobs(message.getText(), arr, i, result));
                        }
                    }catch (Exception e){
                        sendMsg(message, " Not found");
                    }
            }

        }
        return null;
    }

    public String getBotUsername() {
        return "MyTestBot";
    }

    public String getBotToken() {
        return "1494861198:AAH8K7yIpRcohFyiLB_Ale_UAi_9U3l7RBE";
    }

    @Override
    public String getBotPath() {
        return "https://jobseeker-bot.herokuapp.com/";
    }


}