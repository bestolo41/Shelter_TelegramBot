package com.skypro.shelter_telegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Класс, представляющий волонтера.
 */
@Entity
@Table(name = "volunteer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Volunteer {
    /**
     * Идентификатор волонтера.
     */
    @Id
    @Column(name = "id")
    private long id;
    /**
     * Имя волонтера.
     */
    @Column(name = "name")
    private String name;
    /**
     * Идентификатор чата волонтера.
     */
    @Column(name = "chat_id")
    private long chatId;
}
