package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.TelegramBotConfig.TelegramBotConfig;
import com.skypro.shelter_telegrambot.model.Volunteer;
import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
/**
 * Класс VolunteerBot представляет собой бота для работы с волонтерами.
 * Отмечен аннотацией @Component для включения его в контекст приложения Spring.
 * Наследует от TelegramLongPollingBot для работы с API Telegram.
 */
@Data
@Component
public class VolunteerBot extends TelegramLongPollingBot {
    private final TelegramBotConfig telegramBotConfig;
    private final VolunteerDAO volunteerDAO;
    private String userChatId;
    private boolean dialogueMode = false;
    private final TelegramBot telegramBot;
    /**
     * Конструктор класса VolunteerBot.
     *
     * @param telegramBotConfig конфигурация бота Telegram
     * @param volunteerDAO объект DAO для работы с волонтерами
     * @param telegramBot объект бота Telegram
     */
    public VolunteerBot(TelegramBotConfig telegramBotConfig, VolunteerDAO volunteerDAO, TelegramBot telegramBot) {
        this.telegramBotConfig = telegramBotConfig;
        this.volunteerDAO = volunteerDAO;
        this.telegramBot = telegramBot;
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfig.getVolunteerBotName();
    }

    @Override
    public String getBotToken() {
        return telegramBotConfig.getVolunteerBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String volunteerName = update.getMessage().getFrom().getFirstName();
            long volunteerId = update.getMessage().getFrom().getId();
            long chatId = update.getMessage().getChatId();
            long messageId = update.getMessage().getMessageId();

            switch (messageText) {
                case "/start":
                    Volunteer volunteer = new Volunteer(volunteerId, volunteerName, volunteerId);
                    volunteerDAO.addVolunteer(volunteer);

                    SendMessage message = new SendMessage();
                    message.setChatId(chatId);
                    message.setText("Добро пожаловать! Вы теперь волонтёр");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "/stop":
                    dialogueMode = false;
                    telegramBot.setDialogueMode(false);
                    telegramBot.sendMessage(Long.parseLong(userChatId), "Волонтер завершил чат");
                    sendMessage(chatId, "Чат завершен");
                    break;
                default:
                    if (dialogueMode) {
                        telegramBot.sendMessage(Long.parseLong(userChatId), update.getMessage().getText());
                    }
                    break;

            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals(userChatId)) {
                sendMessage(chatId, "Запрос принят");
                dialogueMode = true;
                telegramBot.sendMessage(Long.parseLong(userChatId), "Какой у вас вопрос?");
            }
            switch (callbackData) {
                case "NO":
                    sendMessage(chatId, "Запрос отклонен");
            }
        }
    }


    public void sendMessage(long userChatId, long chatId, String text) {
        this.userChatId = String.valueOf(userChatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var buttonYes = new InlineKeyboardButton();
        var buttonNo = new InlineKeyboardButton();
        buttonYes.setText("ДА");
        buttonYes.setCallbackData(String.valueOf(userChatId));
        buttonNo.setText("НЕТ");
        buttonNo.setCallbackData("NO");
        rowInLine.add(buttonYes);
        rowInLine.add(buttonNo);
        rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        sendMessage.setReplyMarkup(markupInLine);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(long chatId, String text) {
        this.userChatId = String.valueOf(userChatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
