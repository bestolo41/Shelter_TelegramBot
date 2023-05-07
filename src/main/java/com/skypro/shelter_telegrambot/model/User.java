package com.skypro.shelter_telegrambot.model;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "users")
public class User {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
