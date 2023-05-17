package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.model.Button;
import com.skypro.shelter_telegrambot.model.Volunteer;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class VolunteerService {
    private String groupChatId = "-1001972925833";
    private final TelegramBot bot;
    private final MessageService messageService;
    private final VolunteerDAO volunteerDAO;

    public VolunteerService(TelegramBot telegramBot, MessageService messageService, VolunteerDAO volunteerDAO) {
        this.bot = telegramBot;
        this.messageService = messageService;
        this.volunteerDAO = volunteerDAO;
    }

    /**
     * Отправляет запрос на помощь волонтеру.
     *
     * @param userChatId идентификатор чата.
     */
    public void requestVolunteer(long userChatId) {
        // Замените GROUP_CHAT_ID на идентификатор чата вашей группы
//        String groupChatId = "GROUP_CHAT_ID";


        // Получите username пользователя
        String username = getUsername(userChatId);

        SendMessage message = new SendMessage();
        message.setChatId(groupChatId);
        message.setText("Пользователь (@" + username + ") нуждается в помощи волонтера!");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        InlineKeyboardButton volunteerButton = new InlineKeyboardButton();
        volunteerButton.setText("Помочь пользователю");
        volunteerButton.setUrl("https://t.me/" + username); // Замените на ссылку на чат пользователя

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(volunteerButton);

        keyboard.add(row);
        inlineKeyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            bot.execute(message); // отправка сообщения с помощью экземпляра Telegram-бота
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private String getUsername(long userChatId) {
        GetChat getChat = new GetChat();
        getChat.setChatId(String.valueOf(userChatId));

        try {
            Chat chat = bot.execute(getChat);
            return chat.getUserName();
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void processUserReport(Update update) {
        if (update.getMessage().hasPhoto() && update.getMessage().getCaption() != null) {
            List<PhotoSize> photos = update.getMessage().getPhoto();
            PhotoSize photo = photos.stream()
                    .max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);
            String fileId = photo.getFileId();
            String userName = update.getMessage().getFrom().getUserName();
            Long userChatId = update.getMessage().getChatId();


            try {
                messageService.sendMessage(Long.parseLong(groupChatId), "Пользователь (@" + userName + ") направил отчет о питомце");
                bot.execute(
                        SendPhoto.builder()
                        .chatId(groupChatId)
                        .caption(update.getMessage().getCaption())
                        .photo(new InputFile(fileId))
                                .replyMarkup(messageService.createButtons(2, new ArrayList<>(Arrays.asList(
                                        new Button("Принять отчет", "ACCEPT_REPORT/" + userChatId),
                                        new Button("Отклонить", "REJECT_REPORT/" + userChatId)))))
                        .build());
                messageService.sendMessage(userChatId, "Отчет отправлен, ожидайте ответа волонтёра", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                        new Button("Назад", "MAIN_MENU")))));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }


        } else {
            messageService.sendMessage(update.getMessage().getChatId(),
                    "Необходимо отправить и фото животного, и описание рациона и условий содержания",
                    messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "MAIN_MENU")))));
        }
    }

    public boolean checkVolunteer(Long chatId) {
        Volunteer volunteer = new Volunteer();
        volunteer.setId(chatId);
        ArrayList<Volunteer> volunteers = (ArrayList<Volunteer>) volunteerDAO.getAllVolunteers();

        return volunteers.contains(volunteer);
    }
}
