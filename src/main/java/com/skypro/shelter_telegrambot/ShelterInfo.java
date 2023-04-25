package com.skypro.shelter_telegrambot;

public class ShelterInfo {
    private String name;
    private String location;
    private String workingHours;
    private String rules;
    private String securityContacts;

    // Конструктор для создания объекта ShelterInfo с указанием всех атрибутов
    public ShelterInfo(String name, String location, String workingHours, String rules, String securityContacts) {
        this.name = name;
        this.location = location;
        this.workingHours = workingHours;
        this.rules = rules;
        this.securityContacts = securityContacts;
    }

    // Геттеры и сеттеры для доступа и изменения атрибутов объекта ShelterInfo

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getSecurityContacts() {
        return securityContacts;
    }

    public void setSecurityContacts(String securityContacts) {
        this.securityContacts = securityContacts;
    }
}
