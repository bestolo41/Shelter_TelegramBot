package com.skypro.shelter_telegrambot.test;

import com.skypro.shelter_telegrambot.model.User;
import com.skypro.shelter_telegrambot.service.UserDAO;
import com.skypro.shelter_telegrambot.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userDAO);
    }

    @Test
    public void testCheckUser() {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        when(userDAO.getAllUsers()).thenReturn(users);

        boolean result = userService.checkUser(userId);
        assertTrue(result);
    }
}
