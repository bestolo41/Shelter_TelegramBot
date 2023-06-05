
package com.skypro.shelter_telegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "cat_parent")
@Data
@AllArgsConstructor
public class CatParent extends User {
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
        CatParent catParent = (CatParent) o;
        return id == catParent.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public CatParent() {
    }

    public CatParent(long id) {
        this.id = id;
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
}