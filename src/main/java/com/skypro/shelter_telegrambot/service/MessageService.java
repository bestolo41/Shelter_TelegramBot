package com.skypro.shelter_telegrambot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

@Service
public class MessageService {

    public MessageService() {
    }

    /**
     * Создает объект DeleteMessage для удаления сообщения из чата.
     *
     * @param chatId    идентификатор чата, в котором нужно удалить сообщение.
     * @param messageId идентификатор сообщения, которое нужно удалить.
     * @return объект DeleteMessage с указанными параметрами chatId и messageId.
     */
    public DeleteMessage deleteMessage(long chatId, long messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId((int) messageId);
        return deleteMessage;
    }

}
