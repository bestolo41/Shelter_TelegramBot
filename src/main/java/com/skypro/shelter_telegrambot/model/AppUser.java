package com.skypro.shelter_telegrambot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

/**
 * Класс, представляющий пользователя.
 */
@Entity
@Table(name = "users")
@Data
public class AppUser extends User{
    /**
     * Идентификатор пользователя.
     */
    @Id
    @Column(name = "id")
    private long id;



    public AppUser(long id) {
        this.id = id;
    }

    /**
     * Конструктор по умолчанию класса User.
     */
    public AppUser() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AppUser appUser = (AppUser) o;
        return id == appUser.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
