package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.configuration.InfoServiceConfig;

public class InfoService {

    final InfoServiceConfig infoServiceConfig;

    public InfoService(InfoServiceConfig infoServiceConfig) {
        this.infoServiceConfig = infoServiceConfig;
    }

    public String getDatingRules (String callbackData) {
        if (callbackData.equals("Dating_rules_cat")) {
            return infoServiceConfig.getRulesCat();
        }
        else if (callbackData.equals("Dating_rules_dog")) {
            return infoServiceConfig.getRulesDog();
        } return null;
    }

}
