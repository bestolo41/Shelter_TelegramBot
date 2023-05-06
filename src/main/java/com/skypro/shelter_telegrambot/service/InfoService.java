package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.configuration.InfoServiceConfig;
import com.skypro.shelter_telegrambot.exception.ExceptionInfoService;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class InfoService {

    final InfoServiceConfig infoServiceConfig;

    public InfoService(InfoServiceConfig infoServiceConfig) {
        this.infoServiceConfig = infoServiceConfig;
    }

    /**
     * Создает и отправляет сообщение, которое содержит только текст по знакомству с животными
     *
     * @param callbackData
     * @throws ExceptionInfoService
     */
    public String getDatingRules(String callbackData) {
        if (callbackData.equals("Dating_rules_cat")) {
            return infoServiceConfig.getRulesCat();
        } else if (callbackData.equals("Dating_rules_dog")) {
            return infoServiceConfig.getRulesDog();
        }
       else throw new ExceptionInfoService("Ошибка в правилах знакомства в животными");
    }

    /**
     * Создает и отправляет сообщение, которое содержит только текст для необходимых документов, чтобы получить животное
     *
     * @param callbackData
     * @throws ExceptionInfoService
     */
    public String getDocuments(String callbackData) {
        if (callbackData.equals("DOCS")) {
            return infoServiceConfig.getDocuments();
        } else
            throw new ExceptionInfoService("Ошибка в необходимых документах");
    }

    /**
     * Создает и отправляет сообщение, которое содержит только текст с рекомендациями по транспортировке животного
     *
     * @param callbackData
     * @throws ExceptionInfoService
     */
    public String getRecommendationForTransportingAnimal(String callbackData) {
        if (callbackData.equals("TRANSPORTING_CAT")) {
            return infoServiceConfig.getTransportationCat();
        } else if (callbackData.equals("TRANSPORTING_DOG")) {
            return infoServiceConfig.getTransportationDog();
        }
        else throw new ExceptionInfoService("Ошибка в рекомендациях по транспортировке животного");
    }

    /**
     * Создает и отправляет сообщение, которое содержит только текст с рекомендациями по обустройству дома для маленьких животных
     *
     * @param callbackData
     * @throws ExceptionInfoService
     */
    public String getRecommendationHomeImprovementForLittleAnimal(String callbackData) {
        if (callbackData.equals("HOME_LITTLE_CAT")) {
            return infoServiceConfig.getHomeLittleCat();
        } else if (callbackData.equals("HOME_LITTLE_DOG")) {
            return infoServiceConfig.getHomeLittleDog();
        }
        else throw new ExceptionInfoService("Ошибка в рекомендациях по обустройству дома для маленьких животных");
    }

    /**
     * Создает и отправляет сообщение, которое содержит только текст с рекомендациями по обустройству дома для взрослых животных
     *
     * @param callbackData
     * @throws ExceptionInfoService
     */
    public String getRecommendationHomeImprovementForAdultAnimal(String callbackData) {
        if (callbackData.equals("HOME_ADULT_CAT")) {
            return infoServiceConfig.getHomeAdultCat();
        } else if (callbackData.equals("HOME_ADULT_DOG")) {
            return infoServiceConfig.getHomeAdultDog();
        }
        else throw new ExceptionInfoService("Ошибка в рекомендациях по обустройству дома для взрослых животных");
    }

    /**
     * Создает и отправляет сообщение, которое содержит только текст с рекомендациями по обустройству дома для животных с ОВЗ
     *
     * @param callbackData
     * @throws ExceptionInfoService
     */
    public String getRecommendationHomeImprovementForDisabilityAnimal(String callbackData) {
        if (callbackData.equals("HOME_INVALID_CAT")) {
            return infoServiceConfig.getDisabilityCat();
        } else if (callbackData.equals("HOME_INVALID_DOG")) {
            return infoServiceConfig.getDisabilityDog();
        }
        else throw new ExceptionInfoService("Ошибка в рекомендациях по обустройству дома для животных с ОВЗ");
    }

    /**
     * Создает и отправляет сообщение, которое содержит только текст по советам кинолога по первичному общению с собакой
     *
     * @param callbackData
     * @throws ExceptionInfoService
     */
    public String getPrimaryRecommendationDogHandler(String callbackData) {
        if (callbackData.equals("PRIMARY_RECOMMENDATION")) {
            return infoServiceConfig.getPrimaryCommunicationDogHandler();
        }
        else throw new ExceptionInfoService("Ошибка в советах кинолога по первичному общению с собакой");
    }

    /**
     * Создает и отправляет сообщение, которое содержит только текст рекомендации по проверенным кинологам для дальнейшего обращения к ним
     *
     * @param callbackData
     * @throws ExceptionInfoService
     */
    public String getRecommendationProvenDogHandler(String callbackData) {
        if (callbackData.equals("PROVEN_DOG_HANDLER_RECOMMENDATION")) {
            return infoServiceConfig.getProvenDogHandler();
        }
        else throw new ExceptionInfoService("Ошибка в рекомендациях по проверенным кинологам для дальнейшего обращения к ним");
    }

    /**
     * Создает и отправляет сообщение, которое содержит только текст со списком причин, почему могут отказать и не дать забрать собаку из приюта
     *
     * @param callbackData
     * @throws ExceptionInfoService
     */
    public String getListOfReason(String callbackData) {
        if (callbackData.equals("REASONS_CAT")) {
            return infoServiceConfig.getListOfReasonCat();
        } else if (callbackData.equals("REASONS_DOG")) {
            return infoServiceConfig.getListOfReasonDog();
        }
        else throw new ExceptionInfoService("Ошибка со списком причин, почему могут отказать и не дать забрать собаку из приюта");
    }




}
