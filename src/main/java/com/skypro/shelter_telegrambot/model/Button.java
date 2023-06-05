package com.skypro.shelter_telegrambot.model;

/**
 * Класс, представляющий кнопку для inline-клавиатуры в Telegram.
 */
public class Button {
    /**
     * Текст, который будет отображаться на кнопке.
     */
    private String text;
    /**
     * Данные для обратного вызова, которые будут обрабатываться при нажатии на кнопку.
     */
    private String callbackData;

    /**
     * Конструктор класса Button.
     *
     * @param text         текст, который будет отображаться на кнопке.
     * @param callbackData данные для обратного вызова, которые будут обрабатываться при нажатии на кнопку.
     */
    public Button(String text, String callbackData) {
        this.text = text;
        this.callbackData = callbackData;
    }

    /**
     * Устанавливает текст кнопки.
     *
     * @param text текст кнопки.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Устанавливает данные для обратного вызова.
     *
     * @param callbackData данные для обратного вызова.
     */
    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }

    /**
     * Возвращает текст кнопки.
     *
     * @return текст кнопки.
     */
    public String getText() {
        return text;
    }

    /**
     * Возвращает данные для обратного вызова.
     *
     * @return данные для обратного вызова.
     */
    public String getCallbackData() {
        return callbackData;
    }
}
