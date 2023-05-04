package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.configuration.InfoServiceConfig;
import org.springframework.stereotype.Service;

@Service
public class InfoService {

    final InfoServiceConfig infoServiceConfig;

    public InfoService(InfoServiceConfig infoServiceConfig) {
        this.infoServiceConfig = infoServiceConfig;
    }

    public String getDatingRules (String callbackData) {
        if (callbackData.equals("DATING_RULES_CAT")) {
            return infoServiceConfig.getRulesCat();
        }
        else if (callbackData.equals("DATING_RULES_DOG")) {
            return infoServiceConfig.getRulesDog();
        } return null;
    }

}
