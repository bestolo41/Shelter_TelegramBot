package com.skypro.shelter_telegrambot.model;

public class Button {
    private String text;
    private String callbackData;

    public Button(String text, String callbackData) {
        this.text = text;
        this.callbackData = callbackData;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }

    public String getText() {
        return text;
    }

    public String getCallbackData() {
        return callbackData;
    }
}
