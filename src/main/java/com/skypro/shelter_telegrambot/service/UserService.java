package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.model.User;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

/**
 * Класс UserService обеспечивает сервисные функции для работы с пользователями.
 * Отмечен аннотацией @Service, что позволяет Spring включить его в контекст приложения.
 */
@Service
public class UserService {
    final UserDAO userDAO;
    private final MessageService messageService;

    /**
     * Конструктор класса UserService.
     *
     * @param userDAO        объект DAO для работы с пользователями
     * @param messageService
     */
    public UserService(UserDAO userDAO, MessageService messageService) {
        this.userDAO = userDAO;
        this.messageService = messageService;
    }

    /**
     * Метод checkUser проверяет, использовал ли пользователь уже бота или нет.
     *
     * @param user Пользователь
     * @return true, если пользователь использовал бота; false в противном случае
     */

    public <T extends User> boolean checkUser(T user) {
        ArrayList<T> users = (ArrayList<T>) userDAO.getAllUsers(user);
        return users.contains(user);
    }

    public <T extends User> void saveContacts(Update update, T user) {
        long chatId = update.getMessage().getChatId();
        if (user.getFullName() == null) {
            user.setFullName(update.getMessage().getText());
            messageService.sendMessage(chatId, "Имя и фамилия сохранены");
            messageService.sendMessage(chatId, "Введите возраст:");
        } else if (user.getAge() <= 0) {
            user.setAge(Integer.parseInt(update.getMessage().getText()));
            messageService.sendMessage(chatId, "Возраст сохранен");
            messageService.sendMessage(chatId, "Введите номер телефона:");
        } else if (user.getPhoneNumber() == null) {
            user.setPhoneNumber(update.getMessage().getText());
            messageService.sendMessage(chatId, "Номер телефона сохранен");
            messageService.sendMessage(chatId, "Введите адрес проживания:");
        } else if (user.getAddress() == null) {
            user.setAddress(update.getMessage().getText());
            userDAO.addUser(user);
        }
    }

    public <T extends User> void saveParent(Update update, T user) {
        long chatId = update.getMessage().getChatId();
        if (user.getFullName() == null) {
            user.setFullName(update.getMessage().getText());
            messageService.sendMessage(chatId, "Имя и фамилия сохранены");
            messageService.sendMessage(chatId, "Введите номер телефона:");
        } else if (user.getPhoneNumber() == null) {
            user.setPhoneNumber(update.getMessage().getText());
            messageService.sendMessage(chatId, "Номер телефона сохранен");
            messageService.sendMessage(chatId, "Введите @TelegramID клиента." +
                    "Клиент может получить его по ссылке (@userinfobot)");
        } else if (user.getParentId == 0) {
            user.setParentId(update.getMessage().getText());
            messageService.sendMessage(chatId, "ID сохранен");
        }
            userDAO.addUser(user);
        }
    }

}
