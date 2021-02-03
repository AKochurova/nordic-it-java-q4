package com.example.demo.controller;


import com.example.demo.botapi.Bot;
import com.example.demo.cache.Aouth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RestController
public class WebHookController {
    private final Bot telegramBot;
    private final Aouth aouth = new Aouth();

    public WebHookController(Bot telegramBot){
        this.telegramBot = telegramBot;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){
        return telegramBot.onWebhookUpdateReceived(update);
    }

    @RequestMapping(value = "/getcode/{id}", method = RequestMethod.GET)
    public void getCode(@PathVariable("id") String id, @RequestParam("code") String code){
        log.info("Получен код:{}, userId: {}", id, code);
       aouth.setUsersCodes(id, code);
    }
}