package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.model.User;

import java.util.List;

public interface UserDAO {

    void addUser(User newUser);

    User getUserById(long id);

    List<User> getAllUsers();

    void updateUser(User updatedUser);

    void deleteUser(User user);
}