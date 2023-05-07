package com.skypro.shelter_telegrambot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
@Service
public class MessageService {

    public MessageService() {
    }

    public DeleteMessage deleteMessage(long chatId, long messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId((int) messageId);
        return deleteMessage;
    }

    public SendMessage sendFullNameRequestMessage(long chatId, String catOrDogIdentifier) {
        SendMessage fullNameRequestMessage = new SendMessage();
        fullNameRequestMessage.setText("Введите имя и фамилию:");
        fullNameRequestMessage.setChatId(chatId);
        if (catOrDogIdentifier.equals("SET_CONTACT_CAT")) {
            TelegramBot.fullNameForCatShelterRequestId = TelegramBot.callbackQueryMessageId;
        } else if (catOrDogIdentifier.equals("SET_CONTACT_DOG")) {
            TelegramBot.fullNameForDogShelterRequestId = TelegramBot.callbackQueryMessageId;
        }
        return fullNameRequestMessage;
    }
}
