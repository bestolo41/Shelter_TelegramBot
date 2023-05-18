package com.skypro.shelter_telegrambot.model;

import com.skypro.shelter_telegrambot.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "dog_parent")
@Data
@AllArgsConstructor
public class DogParent extends User {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "fullname")
    private String fullName;
    @Column(name = "phone")
    private String phoneNumber;
    @Column(name = "tutor")
    private long tutor_id;

    @Column(name = "report_date")
    @Temporal(TemporalType.DATE)
    private java.util.Date reportDate;

    @Column(name = "trial_end_date")
    @Temporal(TemporalType.DATE)
    private java.util.Date trialEndDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DogParent dogParent = (DogParent) o;
        return id == dogParent.id;
    }

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

    public void setPhone(String phone) {
        this.phoneNumber = phone;
    }

    public void setTutor_id(long tutor_id) {
        this.tutor_id = tutor_id;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public DogParent() {
    }

    public DogParent(long id) {
        this.id = id;
    }
}