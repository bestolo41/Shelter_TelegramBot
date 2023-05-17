package com.skypro.shelter_telegrambot.configuration;

import com.skypro.shelter_telegrambot.model.CatShelterUser;
import com.skypro.shelter_telegrambot.model.DogShelterUser;
import com.skypro.shelter_telegrambot.model.User;
import com.skypro.shelter_telegrambot.model.Volunteer;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Класс HibernateSessionFactoryUtil предназначен для создания единственного экземпляра SessionFactory
 * для работы с базой данных через Hibernate. Реализован по шаблону Singleton.
 */
public class HibernateSessionFactoryUtil {
    /**
     * Экземпляр SessionFactory
     */
    private static SessionFactory sessionFactory;

    /**
     * Приватный конструктор класса, который запрещает создание экземпляров данного класса извне.
     */

    private HibernateSessionFactoryUtil() {
    }

    /**
     * Метод getSessionFactory для получения экземпляра SessionFactory.
     *
     * @return экземпляр SessionFactory. Если текущий экземпляр не существует, то происходит его создание.
     */

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(CatShelterUser.class);
                configuration.addAnnotatedClass(DogShelterUser.class);
                configuration.addAnnotatedClass(Volunteer.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
//                sessionFactory = new Configuration().configure().addAnnotatedClass(User.class).buildSessionFactory();

            } catch (Exception e) {
                System.out.println("Исключение!" + e);
            }
        }
        return sessionFactory;
    }
}
