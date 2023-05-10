package com.skypro.shelter_telegrambot.TelegramBotConfig;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("application.properties")
@Data
public class TelegramBotConfig {

    @Value("${bot.name}")
    String name;

    @Value("${bot.token}")
    String token;

    @Value("${volunteerBot.name}")
    String VolunteerBotName;
    @Value("${volunteerBot.token}")
    String VolunteerBotToken;
}
