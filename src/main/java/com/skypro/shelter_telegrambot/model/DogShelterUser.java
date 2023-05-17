package com.skypro.shelter_telegrambot.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Класс, представляющий пользователя приюта для собак.
 */
@Entity
@Table(name = "dogShelterUsers")
@Data
public class DogShelterUser extends User {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DogShelterUser that = (DogShelterUser) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    public DogShelterUser() {
    }

    public DogShelterUser(long id) {
        this.id = id;
    }

    /**
     * Идентификатор пользователя.
     */
    @Id
    @Column(name = "id")
    private long id;
    /**
     * Полное имя пользователя.
     */
    @Column(name = "full_name")
    private String fullName;
    /**
     * Возраст пользователя.
     */
    @Column(name = "age")
    private int age;
    /**
     * Адрес пользователя.
     */
    @Column(name = "address")
    private String address;
    /**
     * Номер телефона пользователя.
     */
    @Column(name = "phone_number")
    private String phoneNumber;

}



