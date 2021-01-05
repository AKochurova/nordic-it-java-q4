package com.example.demo.config;

import com.example.demo.Bot;
import com.example.demo.TelegramFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String botPath;
    private String botToken;
    private String botUsername;

    @Bean
    public Bot myJSeekerBot(TelegramFacade telegramFacade){
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
        Bot bot = new Bot(options, telegramFacade);
        bot.setBotUsername(botUsername);
        bot.setBotPath(botPath);
        bot.setBotToken(botToken);
        return bot;

    }
}
