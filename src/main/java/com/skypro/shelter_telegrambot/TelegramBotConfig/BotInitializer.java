package com.skypro.shelter_telegrambot.TelegramBotConfig;

import com.skypro.shelter_telegrambot.service.TelegramBot;
import com.skypro.shelter_telegrambot.service.VolunteerBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {
    @Autowired
    TelegramBot telegramBot;
    @Autowired
    VolunteerBot volunteerBot;

    @EventListener ({ContextRefreshedEvent.class})
    public void init () throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        TelegramBotsApi telegramBotsApi2 = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(telegramBot);
            telegramBotsApi2.registerBot(volunteerBot);
        }
           catch (TelegramApiException e) {
        }
    }
}
