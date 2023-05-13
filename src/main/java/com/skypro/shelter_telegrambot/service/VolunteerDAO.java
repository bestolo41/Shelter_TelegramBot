package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.model.Volunteer;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Интерфейс VolunteerDAO определяет методы для работы с волонтерами.
 * Отмечен аннотацией @Service для включения его в контекст приложения Spring.
 */
@Service
public interface VolunteerDAO {
    /**
     * Добавляет нового волонтера.
     *
     * @param newVolunteer объект волонтера, который следует добавить
     */
    void addVolunteer(Volunteer newVolunteer);

    /**
     * Получает список всех волонтеров.
     *
     * @return список объектов волонтеров
     */
    List<Volunteer> getAllVolunteers();
}
