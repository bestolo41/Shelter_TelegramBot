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

    @Value("${rules.for.getting.to.know.an.dog}")
    String rulesDog;












}
