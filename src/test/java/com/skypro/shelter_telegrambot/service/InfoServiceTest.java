package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.configuration.InfoServiceConfig;
import com.skypro.shelter_telegrambot.exception.ExceptionInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


class InfoServiceTest {

    InfoService infoService = new InfoService(new InfoServiceConfig());

    @Test
    void RulesForGettingToKnowAnAnimal() {
       assertThatThrownBy(() -> infoService.getDatingRules("")).
                isInstanceOf(ExceptionInfoService.class)
               .hasMessage("Ошибка в правилах знакомства в животными");

        Assertions.assertEquals(infoService.getDatingRules("DATING_RULES_CAT"), infoService.infoServiceConfig.getRulesCat());
        Assertions.assertEquals(infoService.getDatingRules("DATING_RULES_DOG"), infoService.infoServiceConfig.getRulesDog());
    }


    @Test
    void getListOfDocuments() {
        assertThatThrownBy(() -> infoService.getDocuments("")).
                isInstanceOf(ExceptionInfoService.class)
                .hasMessage("Ошибка в необходимых документах");

        Assertions.assertEquals(infoService.getDocuments("DOCS"), infoService.infoServiceConfig.getDocuments());

    }

    @Test
    void getRecommendationForTransportingAnimal() {
        assertThatThrownBy(() -> infoService.getRecommendationForTransportingAnimal("")).
                isInstanceOf(ExceptionInfoService.class)
                .hasMessage("Ошибка в рекомендациях по транспортировке животного");

        Assertions.assertEquals(infoService.getRecommendationForTransportingAnimal("TRANSPORTING_CAT"), infoService.infoServiceConfig.getTransportationCat());
        Assertions.assertEquals(infoService.getRecommendationForTransportingAnimal("TRANSPORTING_DOG"), infoService.infoServiceConfig.getTransportationDog());
    }

    @Test
    void getRecommendationHomeImprovementForLittleAnimal() {
        assertThatThrownBy(() -> infoService.getRecommendationHomeImprovementForLittleAnimal("")).
                isInstanceOf(ExceptionInfoService.class)
                .hasMessage("Ошибка в рекомендациях по обустройству дома для маленьких животных");

        Assertions.assertEquals(infoService.getRecommendationHomeImprovementForLittleAnimal("HOME_LITTLE_CAT"), infoService.infoServiceConfig.getHomeLittleCat());
        Assertions.assertEquals(infoService.getRecommendationHomeImprovementForLittleAnimal("HOME_LITTLE_DOG"), infoService.infoServiceConfig.getHomeLittleDog());
    }


    @Test
    void getRecommendationHomeImprovementForAdultAnimal() {
        assertThatThrownBy(() -> infoService.getRecommendationHomeImprovementForAdultAnimal("")).
                isInstanceOf(ExceptionInfoService.class)
                .hasMessage("Ошибка в рекомендациях по обустройству дома для взрослых животных");

        Assertions.assertEquals(infoService.getRecommendationHomeImprovementForAdultAnimal("HOME_ADULT_CAT"), infoService.infoServiceConfig.getHomeAdultCat());
        Assertions.assertEquals(infoService.getRecommendationHomeImprovementForAdultAnimal("HOME_ADULT_DOG"), infoService.infoServiceConfig.getHomeAdultDog());
    }

    @Test
    void getRecommendationHomeImprovementForDisabilityAnimal() {
        assertThatThrownBy(() -> infoService.getRecommendationHomeImprovementForDisabilityAnimal("")).
                isInstanceOf(ExceptionInfoService.class)
                .hasMessage("Ошибка в рекомендациях по обустройству дома для животных с ОВЗ");

        Assertions.assertEquals(infoService.getRecommendationHomeImprovementForDisabilityAnimal("HOME_INVALID_CAT"), infoService.infoServiceConfig.getDisabilityCat());
        Assertions.assertEquals(infoService.getRecommendationHomeImprovementForDisabilityAnimal("HOME_INVALID_DOG"), infoService.infoServiceConfig.getDisabilityDog());
    }

    @Test
    void getPrimaryRecommendationDogHandler() {
        assertThatThrownBy(() -> infoService.getPrimaryRecommendationDogHandler("")).
                isInstanceOf(ExceptionInfoService.class)
                .hasMessage("Ошибка в советах кинолога по первичному общению с собакой");

        Assertions.assertEquals(infoService.getPrimaryRecommendationDogHandler("PRIMARY_RECOMMENDATION"), infoService.infoServiceConfig.getPrimaryCommunicationDogHandler());
    }

    @Test
    void getRecommendationProvenDogHandler() {
        assertThatThrownBy(() -> infoService.getRecommendationProvenDogHandler("")).
                isInstanceOf(ExceptionInfoService.class)
                .hasMessage("Ошибка в рекомендациях по проверенным кинологам для дальнейшего обращения к ним");

        Assertions.assertEquals(infoService.getRecommendationProvenDogHandler("PROVEN_DOG_HANDLER_RECOMMENDATION"), infoService.infoServiceConfig.getProvenDogHandler());
    }


    @Test
    void getListOfReason() {
        assertThatThrownBy(() -> infoService.getListOfReason("")).
                isInstanceOf(ExceptionInfoService.class)
                .hasMessage("Ошибка со списком причин, почему могут отказать и не дать забрать собаку из приюта");

        Assertions.assertEquals(infoService.getListOfReason("REASONS_CAT"), infoService.infoServiceConfig.getListOfReasonCat());
        Assertions.assertEquals(infoService.getListOfReason("REASONS_DOG"), infoService.infoServiceConfig.getListOfReasonDog());
    }
}