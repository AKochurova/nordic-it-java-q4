package com.example.demo.config;

import com.example.demo.Bot;
import com.example.demo.TelegramFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String botPath;
    private String botToken;
    private String botUsername;

    @Bean
    public Bot bot(TelegramFacade telegramFacade){
        Bot bot  = new Bot(telegramFacade);
        bot.setBotPath(botPath);
        bot.setBotToken(botToken);
        bot.setBotUsername(botUsername);
        return bot;
    }
}
