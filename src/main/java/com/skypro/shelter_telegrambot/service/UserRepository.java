package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
/**
 * Класс UserRepository управляет сохранением объектов User в базу данных.
 * Использует Hibernate для взаимодействия с базой данных.
 */

public class UserRepository {
    private static SessionFactory sessionFactory;
    /**
     * В статическом блоке инициализации настраивается фабрика сессий Hibernate
     * с использованием файла конфигурации hibernate.cfg.xml.
     */
    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml"); // Замените на имя вашего файла конфигурации Hibernate
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод saveUser сохраняет пользователя в базе данных.
     *
     * @param user Пользователь для сохранения в базе данных.
     */
    public static void saveUser(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
