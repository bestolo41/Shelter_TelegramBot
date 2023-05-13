package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.configuration.InfoServiceConfig;
import com.skypro.shelter_telegrambot.exception.ExceptionInfoService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Сервис, предоставляющий информацию о процессе взаимодействия с животными в приюте.
 */
@Service
public class InfoService {

    final InfoServiceConfig infoServiceConfig;

    public InfoService(InfoServiceConfig infoServiceConfig) {
        this.infoServiceConfig = infoServiceConfig;
    }

    /**
     * Возвращает правила знакомства с животными.
     *
     * @param callbackData обозначение типа животного.
     * @throws ExceptionInfoService если предоставлены некорректные данные.
     */
    public String getDatingRules(String callbackData) {
        if (callbackData.equals("DATING_RULES_CAT")) {
            return infoServiceConfig.getRulesCat();
        } else if (callbackData.equals("DATING_RULES_DOG")) {
            return infoServiceConfig.getRulesDog();
        } else throw new ExceptionInfoService("Ошибка в правилах знакомства в животными");
    }

    /**
     * Возвращает необходимые документы для получения животного.
     *
     * @param callbackData обозначение типа животного.
     * @throws ExceptionInfoService если предоставлены некорректные данные.
     */
    public String getDocuments(String callbackData) {
        if (callbackData.equals("DOCS_CAT") || callbackData.equals("DOCS_DOG")) {
            return infoServiceConfig.getDocuments();
        } else
            throw new ExceptionInfoService("Ошибка в необходимых документах");
    }

    /**
     * Возвращает рекомендации по транспортировке животного.
     *
     * @param callbackData обозначение типа животного.
     * @throws ExceptionInfoService если предоставлены некорректные данные.
     */
    public String getRecommendationForTransportingAnimal(String callbackData) {
        if (callbackData.equals("TRANSPORTING_CAT")) {
            return infoServiceConfig.getTransportationCat();
        } else if (callbackData.equals("TRANSPORTING_DOG")) {
            return infoServiceConfig.getTransportationDog();
        } else throw new ExceptionInfoService("Ошибка в рекомендациях по транспортировке животного");
    }

    /**
     * Возвращает рекомендации по обустройству дома для маленьких животных.
     *
     * @param callbackData обозначение типа животного.
     * @throws ExceptionInfoService если предоставлены некорректные данные.
     */
    public String getRecommendationHomeImprovementForLittleAnimal(String callbackData) {
        if (callbackData.equals("HOME_LITTLE_CAT")) {
            return infoServiceConfig.getHomeLittleCat();
        } else if (callbackData.equals("HOME_LITTLE_DOG")) {
            return infoServiceConfig.getHomeLittleDog();
        } else throw new ExceptionInfoService("Ошибка в рекомендациях по обустройству дома для маленьких животных");
    }

    /**
     * Возвращает рекомендации по обустройству дома для взрослых животных.
     *
     * @param callbackData обозначение типа животного.
     * @throws ExceptionInfoService если предоставлены некорректные данные.
     */
    public String getRecommendationHomeImprovementForAdultAnimal(String callbackData) {
        if (callbackData.equals("HOME_ADULT_CAT")) {
            return infoServiceConfig.getHomeAdultCat();
        } else if (callbackData.equals("HOME_ADULT_DOG")) {
            return infoServiceConfig.getHomeAdultDog();
        } else throw new ExceptionInfoService("Ошибка в рекомендациях по обустройству дома для взрослых животных");
    }

    /**
     * Возвращает рекомендации по обустройству дома для животных с ОВЗ.
     *
     * @param callbackData обозначение типа животного.
     * @throws ExceptionInfoService если предоставлены некорректные данные.
     */
    public String getRecommendationHomeImprovementForDisabilityAnimal(String callbackData) {
        if (callbackData.equals("HOME_INVALID_CAT")) {
            return infoServiceConfig.getDisabilityCat();
        } else if (callbackData.equals("HOME_INVALID_DOG")) {
            return infoServiceConfig.getDisabilityDog();
        } else throw new ExceptionInfoService("Ошибка в рекомендациях по обустройству дома для животных с ОВЗ");
    }

    /**
     * Возвращает советы кинолога по первичному общению с собакой.
     *
     * @param callbackData обозначение советов кинолога.
     * @throws ExceptionInfoService если предоставлены некорректные данные.
     */
    public String getPrimaryRecommendationDogHandler(String callbackData) {
        if (callbackData.equals("PRIMARY_RECOMMENDATION")) {
            return infoServiceConfig.getPrimaryCommunicationDogHandler();
        } else throw new ExceptionInfoService("Ошибка в советах кинолога по первичному общению с собакой");
    }

    /**
     * Возвращает рекомендации по проверенным кинологам для дальнейшего обращения к ним.
     *
     * @param callbackData обозначение рекомендаций кинолога.
     * @throws ExceptionInfoService если предоставлены некорректные данные.
     */
    public String getRecommendationProvenDogHandler(String callbackData) {
        if (callbackData.equals("PROVEN_DOG_HANDLER_RECOMMENDATION")) {
            return infoServiceConfig.getProvenDogHandler();
        } else
            throw new ExceptionInfoService("Ошибка в рекомендациях по проверенным кинологам для дальнейшего обращения к ним");
    }

    /**
     * Возвращает список причин, по которым могут отказать в передаче животного из приюта.
     *
     * @param callbackData обозначение типа животного.
     * @throws ExceptionInfoService если предоставлены некорректные данные.
     */
    public String getListOfReason(String callbackData) {
        if (callbackData.equals("REASONS_CAT")) {
            return infoServiceConfig.getListOfReasonCat();
        } else if (callbackData.equals("REASONS_DOG")) {
            return infoServiceConfig.getListOfReasonDog();
        } else
            throw new ExceptionInfoService("Ошибка со списком причин, почему могут отказать и не дать забрать собаку из приюта");
    }


}
