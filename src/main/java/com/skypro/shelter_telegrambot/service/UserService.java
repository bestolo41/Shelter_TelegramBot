package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.model.CatShelterUser;
import com.skypro.shelter_telegrambot.model.DogShelterUser;
import com.skypro.shelter_telegrambot.model.User;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

/**
 * Класс UserService обеспечивает сервисные функции для работы с пользователями.
 * Отмечен аннотацией @Service, что позволяет Spring включить его в контекст приложения.
 */
@Service
public class UserService {
    final UserDAO userDAO;

    /**
     * Конструктор класса UserService.
     *
     * @param userDAO объект DAO для работы с пользователями
     */
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Метод checkUser проверяет, использовал ли пользователь уже бота или нет.
     *
     * @param userId Идентификатор пользователя
     * @return true, если пользователь использовал бота; false в противном случае
     */
    public boolean checkUser(long userId) {
        User u = new User();
        u.setId(userId);
        ArrayList<User> users = (ArrayList<User>) userDAO.getAllUsers();

        return users.contains(u);
    }

    /**
     * Метод checkCatUser проверяет, является ли пользователь пользователем приюта для кошек.
     *
     * @param userId Идентификатор пользователя
     * @return true, если пользователь является пользователем приюта для кошек; false в противном случае
     */
    public boolean checkCatUser(long userId) {
        CatShelterUser u = new CatShelterUser();
        u.setId(userId);
        ArrayList<CatShelterUser> users = (ArrayList<CatShelterUser>) userDAO.getAllCatUsers();

        return users.contains(u);
    }

    /**
     * Метод checkDogUser проверяет, является ли пользователь пользователем приюта для собак.
     *
     * @param userId Идентификатор пользователя
     * @return true, если пользователь является пользователем приюта для собак; false в противном случае
     */
    public boolean checkDogUser(long userId) {
        DogShelterUser u = new DogShelterUser();
        u.setId(userId);
        ArrayList<DogShelterUser> users = (ArrayList<DogShelterUser>) userDAO.getAllDogUsers();

        return users.contains(u);
    }
}
