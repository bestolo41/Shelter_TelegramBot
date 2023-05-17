package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.model.CatShelterUser;
import com.skypro.shelter_telegrambot.model.DogShelterUser;
import com.skypro.shelter_telegrambot.model.AppUser;
import com.skypro.shelter_telegrambot.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserDAO {
    /**
     * Добавляет нового пользователя в базу данных.
     *
     * @param newUser Новый пользователь
     */
    <T extends User> void  addUser(T newUser);

    /**
     * Возвращает список всех пользователей.
     *
     * @return Список пользователей
     */
    <T extends User> List<T> getAllUsers(T o);

//
}
