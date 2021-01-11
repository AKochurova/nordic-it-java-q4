package com.example.demo.service;


import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@Service
public class ReplyMessageService {
    public SendMessage getReplyMessage(long chatId, String replyMessage){

        return new SendMessage(chatId, replyMessage);

    }


}
