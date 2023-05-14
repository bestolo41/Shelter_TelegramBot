package com.skypro.shelter_telegrambot.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Класс, представляющий пользователя приюта для кошек.
 */
@Entity
@Table(name = "catShelterUsers")
@Data
public class CatShelterUser {
    /**
     * Конструктор класса CatShelterUser.
     *
     * @param id идентификатор пользователя.
     */
    public CatShelterUser(long id) {
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

    /**
     * Конструктор по умолчанию класса CatShelterUser.
     */
    public CatShelterUser() {
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
        CatShelterUser user = (CatShelterUser) o;
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

    public void setId(long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

