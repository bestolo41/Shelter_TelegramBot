package com.skypro.shelter_telegrambot.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Класс, представляющий пользователя.
 */
@Entity
@Table(name = "users")
public class User {
    /**
     * Идентификатор пользователя.
     */
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /**
     * Конструктор по умолчанию класса User.
     */
    public User() {
    }

    /**
     * Проверяет равенство объекта текущему объекту.
     *
     * @param o объект для сравнения.
     * @return true, если объекты равны; иначе false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    /**
     * Возвращает хеш-код объекта.
     *
     * @return хеш-код объекта.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Устанавливает идентификатор пользователя.
     *
     * @param id идентификатор пользователя.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Возвращает идентификатор пользователя.
     *
     * @return идентификатор пользователя.
     */
    public long getId() {
        return id;
    }

}
