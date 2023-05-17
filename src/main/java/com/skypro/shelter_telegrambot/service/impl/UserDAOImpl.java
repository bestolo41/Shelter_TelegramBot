package com.skypro.shelter_telegrambot.service.impl;

import com.skypro.shelter_telegrambot.configuration.HibernateSessionFactoryUtil;
import com.skypro.shelter_telegrambot.model.AppUser;
import com.skypro.shelter_telegrambot.model.CatShelterUser;
import com.skypro.shelter_telegrambot.model.DogShelterUser;
import com.skypro.shelter_telegrambot.model.User;
import com.skypro.shelter_telegrambot.service.UserDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реализация сервиса для работы с пользователями.
 * Предоставляет методы для добавления, обновления и удаления пользователей из базы данных.
 */
@Service
public class UserDAOImpl  implements UserDAO {
    /**
     * Добавляет нового пользователя в базу данных.
     *
     * @param newUser новый пользователь.
     */
    @Override
    public <T extends User> void addUser(T newUser) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            Transaction transaction = session.beginTransaction();
            session.save(newUser);
            transaction.commit();
        }
    }

    @Override
    public <T extends User> List<T> getAllUsers(T o) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {


            List<T> users = (List<T>) session.createQuery("From " + o.getClass().getName()).list();
            return users;
        }
    }
}
