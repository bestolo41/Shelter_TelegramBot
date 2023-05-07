package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.model.CatShelterUser;
import com.skypro.shelter_telegrambot.model.DogShelterUser;
import com.skypro.shelter_telegrambot.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserDAO {

    void addUser(User newUser);

    void addCatUser(CatShelterUser newUser);

    void addDogUser(DogShelterUser newUser);

    User getUserById(long id);

    List<User> getAllUsers();

    List<CatShelterUser> getAllCatUsers();
    List<DogShelterUser> getAllDogUsers();

    void updateUser(User updatedUser);

    void deleteUser(User user);
}
