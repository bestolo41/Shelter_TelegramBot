package com.skypro.shelter_telegrambot.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Класс InfoServiceConfig предназначен для загрузки конфигурационных данных из файла recommendations.properties.
 * Эти данные используются в качестве рекомендаций и правил, связанных с взаимодействием и уходом за кошками и собаками.
 */
@Configuration
@PropertySource("recommendations.properties")
@Data
public class InfoServiceConfig {

    /**
     * Правила знакомства с кошкой.
     */
    @Value("${rules.for.getting.to.know.an.cat}")
    String rulesCat;

    /**
     * Правила знакомства с собакой.
     */
    @Value("${rules.for.getting.to.know.an.dog}")
    String rulesDog;

    /**
     * Список необходимых документов.
     */
    @Value("${list.of.documents}")
    String documents;

    /**
     * Рекомендации по транспортировке кошки.
     */
    @Value("${transportation.cat}")
    String transportationCat;

    /**
     * Рекомендации по транспортировке собаки.
     */
    @Value("${transportation.dog}")
    String transportationDog;

    /**
     * Рекомендации по обустройству дома для маленькой кошки.
     */
    @Value("${home.improvement.cat}")
    String homeLittleCat;

    /**
     * Рекомендации по обустройству дома для маленькой собаки.
     */
    @Value("${home.improvement.dog}")
    String homeLittleDog;

    /**
     * Рекомендации по обустройству дома для взрослой кошки.
     */
    @Value("${home.improvement.adult.cat}")
    String homeAdultCat;

    /**
     * Рекомендации по обустройству дома для взрослой собаки.
     */
    @Value("${home.improvement.adult.dog}")
    String homeAdultDog;

    /**
     * Рекомендации по обустройству дома для кошки с инвалидностью.
     */
    @Value("${home.improvement.disability.cat}")
    String disabilityCat;

    /**
     * Рекомендации по обустройству дома для собаки с инвалидностью.
     */
    @Value("${home.improvement.disability.dog}")
    String disabilityDog;

    /**
     * Советы от собачника по первичному общению с собакой.
     */
    @Value("${tips.from.a.dog.handler.on.primary.communication.with.a.dog}")
    String primaryCommunicationDogHandler;

    /**
     * Рекомендации для опытных собачников.
     */
    @Value("${recommendations.for.proven.dog.handlers}")
    String provenDogHandler;

    /**
     * Список причин, по которым могут отказать в приеме кошки.
     */
    @Value("${list.of.reasons.why.they.may.refuse.cat}")
    String listOfReasonCat;

    /**
     * Список причин, по которым могут отказать в приеме собаки.
     */
    @Value("${list.of.reasons.why.they.may.refuse.dog}")
    String listOfReasonDog;
}