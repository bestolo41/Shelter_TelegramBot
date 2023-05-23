package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.configuration.HibernateSessionFactoryUtil;
import com.skypro.shelter_telegrambot.model.CatParent;
import com.skypro.shelter_telegrambot.model.DogParent;
import org.hibernate.Session;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
@PropertySource("application.properties")
public class TimeCheck {
    private final MessageService messageService;


    public TimeCheck(MessageService messageService) {
        this.messageService = messageService;


    }

    @Scheduled(cron = "${interval-in-cron}")
    void timeCheck() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();) {
            List<CatParent> allCatParents = (List<CatParent>) session.createQuery("From CatParent").list();
            List<DogParent> allDogParents = (List<DogParent>) session.createQuery("From DogParent").list();
            for (CatParent catParent : allCatParents) {
                LocalDate reportDate = Instant.ofEpochMilli(catParent
                                .getReportDate().getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                LocalDate trialEndDate = Instant.ofEpochMilli(catParent
                                .getTrialEndDate().getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                if (trialEndDate.equals(LocalDate.now())) {
                    messageService.sendMessage(catParent.getTutor_id(), "Продлить срок!!!!!!!!");
                }
                else if (LocalDate.now().isBefore(trialEndDate)) {
                    if (reportDate.plusDays(1).equals(LocalDate.now())) {
                        messageService.sendMessage(catParent.getId(), "Где отчет????");
                    }
                    else if (reportDate.plusDays(1).isBefore(LocalDate.now())) {
                        messageService.sendMessage(catParent.getId(), "Где отчет????");
                        messageService.sendMessage(catParent.getTutor_id(), "Этот не присылает отчет");
                    }

                }
            }


        }
    }
}


