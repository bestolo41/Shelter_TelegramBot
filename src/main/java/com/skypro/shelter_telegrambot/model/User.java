package com.skypro.shelter_telegrambot.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
@Data
public abstract class User {

        private long id;
        private String fullName;
        private int age;
        private String address;
        private String phoneNumber;

        public User(long id) {
        this.id = id;
    }
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
}
