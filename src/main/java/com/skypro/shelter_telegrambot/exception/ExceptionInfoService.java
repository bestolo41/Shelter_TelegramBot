package com.skypro.shelter_telegrambot.exception;

/**
 * Класс исключения, которое выбрасывается, когда происходит ошибка в сервисе информации.
 */
public class ExceptionInfoService extends RuntimeException {
    /**
     * Конструктор класса ExceptionInfoService.
     *
     * @param message Сообщение об ошибке, которое будет передано в конструктор суперкласса.
     */
    public ExceptionInfoService(String message) {
        super(message);
    }
}
