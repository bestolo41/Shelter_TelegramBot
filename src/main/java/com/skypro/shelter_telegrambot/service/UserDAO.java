package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.model.CatShelterUser;
import com.skypro.shelter_telegrambot.model.DogShelterUser;
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
    void addUser(User newUser);

    /**
     * Добавляет нового пользователя-владельца кошки в базу данных.
     *
     * @param newUser Новый пользователь-владелец кошки
     */
    void addCatUser(CatShelterUser newUser);

    /**
     * Добавляет нового пользователя-владельца собаки в базу данных.
     *
     * @param newUser Новый пользователь-владелец собаки
     */
    void addDogUser(DogShelterUser newUser);

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя
     * @return Пользователь
     */
    User getUserById(long id);

    /**
     * Возвращает список всех пользователей.
     *
     * @return Список пользователей
     */
    List<User> getAllUsers();

    /**
     * Возвращает список всех пользователей-владельцев кошек.
     *
     * @return Список пользователей-владельцев кошек
     */
    List<CatShelterUser> getAllCatUsers();

    /**
     * Возвращает список всех пользователей-владельцев собак.
     *
     * @return Список пользователей-владельцев собак
     */
    List<DogShelterUser> getAllDogUsers();

    /**
     * Обновляет информацию о пользователе.
     *
     * @param updatedUser Обновленный пользователь
     */
    void updateUser(User updatedUser);

    /**
     * Удаляет пользователя из базы данных.
     *
     * @param user Пользователь для удаления
     */
    void deleteUser(User user);
}
