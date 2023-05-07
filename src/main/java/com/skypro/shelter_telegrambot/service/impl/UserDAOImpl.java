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
@Service
public class UserDAOImpl implements UserDAO {
    @Override
    public void addUser(User newUser) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            Transaction transaction = session.beginTransaction();
            session.save(newUser);
            transaction.commit();
        }
    }

    @Override
    public void addCatUser(CatShelterUser newUser) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            Transaction transaction = session.beginTransaction();
            session.save(newUser);
            transaction.commit();
        }
    }

    @Override
    public void addDogUser(DogShelterUser newUser) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            Transaction transaction = session.beginTransaction();
            session.save(newUser);
            transaction.commit();
        }
    }

    @Override
    public User getUserById(long id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            return session.get(User.class, id);
        }
    }
    @Override
    public List<User> getAllUsers() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            List<User> users = (List<User>) session.createQuery("From User").list();
            return users;
        }
    }

    @Override
    public List<CatShelterUser> getAllCatUsers() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            List<CatShelterUser> users = (List<CatShelterUser>) session.createQuery("From CatShelterUser").list();
            return users;
        }
    }

    @Override
    public List<DogShelterUser> getAllDogUsers() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            List<DogShelterUser> users = (List<DogShelterUser>) session.createQuery("From DogShelterUser").list();
            return users;
        }
    }


    @Override
    public void updateUser(User updatedUser) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            Transaction transaction = session.beginTransaction();
            session.update(updatedUser);
            transaction.commit();
        }
    }

    @Override
    public void deleteUser(User user) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
        }
    }
}
