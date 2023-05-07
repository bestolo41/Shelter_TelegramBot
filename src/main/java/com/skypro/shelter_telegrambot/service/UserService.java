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
@Service
public class UserService {
    final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Проверяет пользовался ли ботом этот пользователь или нет
     *
     * @param userId
     * @return
     */
    public boolean checkUser(long userId) {
        User u = new User();
        u.setId(userId);
        ArrayList<User> users = (ArrayList<User>) userDAO.getAllUsers();

        return users.contains(u);
    }

    public boolean checkCatUser(long userId) {
        CatShelterUser u = new CatShelterUser();
        u.setId(userId);
        ArrayList<CatShelterUser> users = (ArrayList<CatShelterUser>) userDAO.getAllCatUsers();

        return users.contains(u);
    }

    public boolean checkDogUser(long userId) {
        DogShelterUser u = new DogShelterUser();
        u.setId(userId);
        ArrayList<DogShelterUser> users = (ArrayList<DogShelterUser>) userDAO.getAllDogUsers();

        return users.contains(u);
    }
}
