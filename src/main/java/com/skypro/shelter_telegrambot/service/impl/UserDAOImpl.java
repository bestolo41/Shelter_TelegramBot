package com.skypro.shelter_telegrambot.service.impl;

import com.skypro.shelter_telegrambot.configuration.HibernateSessionFactoryUtil;
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
public class UserDAOImpl implements UserDAO {
    /**
     * Добавляет нового пользователя в базу данных.
     *
     * @param newUser новый пользователь.
     */
    @Override
    public void addUser(User newUser) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            Transaction transaction = session.beginTransaction();
            session.save(newUser);
            transaction.commit();
        }
    }

    /**
     * Добавляет нового пользователя кошачьего приюта в базу данных.
     *
     * @param newUser новый пользователь.
     */
    @Override
    public void addCatUser(CatShelterUser newUser) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            Transaction transaction = session.beginTransaction();
            session.save(newUser);
            transaction.commit();
        }
    }

    /**
     * Добавляет нового пользователя собачьего приюта в базу данных.
     *
     * @param newUser новый пользователь.
     */
    @Override
    public void addDogUser(DogShelterUser newUser) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            Transaction transaction = session.beginTransaction();
            session.save(newUser);
            transaction.commit();
        }
    }

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return пользователь.
     */
    @Override
    public User getUserById(long id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            return session.get(User.class, id);
        }
    }

    /**
     * Возвращает список всех пользователей из базы данных.
     *
     * @return список пользователей.
     */
    @Override
    public List<User> getAllUsers() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            List<User> users = (List<User>) session.createQuery("From User").list();
            return users;
        }
    }

    /**
     * Возвращает список всех пользователей кошачьего приюта из базы данных.
     *
     * @return список пользователей.
     */
    @Override
    public List<CatShelterUser> getAllCatUsers() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            List<CatShelterUser> users = (List<CatShelterUser>) session.createQuery("From CatShelterUser").list();
            return users;
        }
    }

    /**
     * Возвращает список всех пользователей собачьего приюта из базы данных.
     *
     * @return список пользователей.
     */
    @Override
    public List<DogShelterUser> getAllDogUsers() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            List<DogShelterUser> users = (List<DogShelterUser>) session.createQuery("From DogShelterUser").list();
            return users;
        }
    }

    /**
     * Обновляет информацию о пользователе в базе данных.
     *
     * @param updatedUser обновленный пользователь.
     */
    @Override
    public void updateUser(User updatedUser) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            Transaction transaction = session.beginTransaction();
            session.update(updatedUser);
            transaction.commit();
        }
    }

    /**
     * Удаляет пользователя из базы данных.
     *
     * @param user пользователь для удаления.
     */
    @Override
    public void deleteUser(User user) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
        }
    }
}
