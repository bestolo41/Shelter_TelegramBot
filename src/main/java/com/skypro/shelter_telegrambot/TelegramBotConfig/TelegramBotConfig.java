package com.skypro.shelter_telegrambot.TelegramBotConfig;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Класс TelegramBotConfig используется для конфигурации ботов.
 * Он читает значения из application.properties и предоставляет их в виде полей класса.
 */
@Configuration
@PropertySource("application.properties")
@Data
public class TelegramBotConfig {
    /**
     * Имя основного бота.
     */
    @Value("${bot.name}")
    String name;
    /**
     * Токен основного бота.
     */
    @Value("${bot.token}")
    String token;
    /**
     * Имя волонтерского бота.
     */
    @Value("${volunteerBot.name}")
    String VolunteerBotName;
    /**
     * Токен волонтерского бота.
     */
    @Value("${volunteerBot.token}")
    String VolunteerBotToken;
}
