package com.skypro.shelter_telegrambot.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("recommendations.properties")
@Data
public class InfoServiceConfig {
    @Value("${rules.for.getting.to.know.an.cat}")
    String rulesCat;

    @Value("${rules.for.getting.to.know.an.cat}")
    String rulesDog;

    @Value("${list.of.documents}")
    String documents;

    @Value("${transportation.cat}")
    String transportationCat;

    @Value("${transportation.dog}")
    String transportationDog;

    @Value("${home.improvement.cat}")
    String homeLittleCat;

    @Value("${home.improvement.dog}")
    String homeLittleDog;

    @Value("${home.improvement.adult.cat}")
    String homeAdultCat;

    @Value("${home.improvement.adult.dog}")
    String homeAdultDog;

    @Value("${home.improvement.disability.cat}")
    String disabilityCat;

    @Value("${home.improvement.disability.dog}")
    String disabilityDog;

    @Value("${tips.from.a.dog.handler.on.primary.communication.with.a.dog}")
    String primaryCommunicationDogHandler;

    @Value("${recommendations.for.proven.dog.handlers}")
    String provenDogHandler;

    @Value("${list.of.reasons.why.they.may.refuse.cat}")
    String listOfReasonCat;

    @Value("${list.of.reasons.why.they.may.refuse.dog}")
    String listOfReasonDog;


}
