package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.configuration.HibernateSessionFactoryUtil;
import com.skypro.shelter_telegrambot.model.Button;
import com.skypro.shelter_telegrambot.model.CatParent;
import com.skypro.shelter_telegrambot.model.DogParent;
import com.skypro.shelter_telegrambot.model.User;
import org.hibernate.Session;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
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
            List<? super User> allParents = new ArrayList<>();
            allParents.addAll(allCatParents);
            allParents.addAll(allDogParents);

            for (Object allParent : allParents) {
                User parent = null;
                if (allParent.getClass().equals(CatParent.class)) {
                    parent = (CatParent) allParent;
                } else if (allParent.getClass().equals(DogParent.class)) {
                    parent = (DogParent) allParent;
                }
                assert parent != null;
                LocalDate reportDate = Instant.ofEpochMilli(parent
                                .getReportDate().getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                LocalDate trialEndDate = Instant.ofEpochMilli(parent
                                .getTrialEndDate().getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                if (trialEndDate.equals(LocalDate.now())) {
                    messageService.sendMessage(parent.getTutor_id(), "Усыновитель " + parent.getFullName() + " прошел испытательный срок. Подтвердить или продлить срок?",
                            messageService.createButtons(1, new ArrayList<Button>(Arrays.asList(
                                    new Button("Подтвердить", "CONFIRM/" + parent.getId()),
                                    new Button("Продлить на 14 дней", "PROLONG_14/" + parent.getId()),
                                    new Button("Продлить на 30 дней", "PROLONG_30/" + parent.getId())
                            ))));
                }
                else if (LocalDate.now().isBefore(trialEndDate)) {
                    if (reportDate.plusDays(1).equals(LocalDate.now()) || reportDate == null) {
                        messageService.sendMessage(parent.getId(), "Вы сегодня не отправили отчет о питомце. Поскорее сделайте это");
                    }
                    else if (reportDate.plusDays(1).isBefore(LocalDate.now())) {
                        messageService.sendMessage(parent.getId(), "Вы сегодня не отправили отчет о питомце. Поскорее сделайте это");
                        messageService.sendMessage(parent.getTutor_id(), parent.getFullName() + " не присылает отчет уже более 2х дней. Свяжитесь с ним по тел." + parent.getPhoneNumber());
                    }

                }
            }


        }
    }
}


