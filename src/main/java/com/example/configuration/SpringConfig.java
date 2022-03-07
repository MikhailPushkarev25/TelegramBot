package com.example.configuration;

import com.example.telegram.CallbackQueryHandler;
import com.example.telegram.MessageHandler;
import com.example.telegram.WriteReadBot;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
@AllArgsConstructor
public class SpringConfig {

    private final TelegramConfig telegramConfig;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramConfig.getWebhookPath()).build();
    }

    @Bean
    public WriteReadBot springWebhookBot(SetWebhook setWebhook,
                                         MessageHandler messageHandler,
                                         CallbackQueryHandler callbackQueryHandler
    ) {
        WriteReadBot bot = new WriteReadBot(setWebhook, messageHandler, callbackQueryHandler);
        bot.setPath(telegramConfig.getWebhookPath());
        bot.setUsername(telegramConfig.getUser());
        bot.setToken(telegramConfig.getToken());

        return bot;
    }
}
