package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.model.Button;
import com.skypro.shelter_telegrambot.model.User;
import com.skypro.shelter_telegrambot.model.Volunteer;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Класс UserService обеспечивает сервисные функции для работы с пользователями.
 * Отмечен аннотацией @Service, что позволяет Spring включить его в контекст приложения.
 */
@Service
public class UserService {
    final UserDAO userDAO;
    final VolunteerDAO volunteerDAO;
    private final MessageService messageService;
    final TelegramBot bot;

    /**
     * Конструктор класса UserService.
     *
     * @param userDAO        объект DAO для работы с пользователями
     * @param volunteerDAO
     * @param messageService
     * @param bot
     */
    public UserService(UserDAO userDAO, VolunteerDAO volunteerDAO, MessageService messageService, TelegramBot bot) {
        this.userDAO = userDAO;
        this.volunteerDAO = volunteerDAO;
        this.messageService = messageService;
        this.bot = bot;
    }

    /**
     * Метод checkUser проверяет, использовал ли пользователь уже бота или нет.
     *
     * @param user Пользователь
     * @return true, если пользователь использовал бота; false в противном случае
     */

    public <T extends User> boolean checkUser(T user) {
        ArrayList<T> users = (ArrayList<T>) userDAO.getAllUsers(user);
        return users.contains(user);
    }

    public <T extends User> void saveContacts(Update update, T user) {
        long chatId = update.getMessage().getChatId();
        if (user.getFullName() == null) {
            user.setFullName(update.getMessage().getText());
            messageService.sendMessage(chatId, "Имя и фамилия сохранены");
            messageService.sendMessage(chatId, "Введите возраст:");
        } else if (user.getAge() <= 0) {
            user.setAge(Integer.parseInt(update.getMessage().getText()));
            messageService.sendMessage(chatId, "Возраст сохранен");
            messageService.sendMessage(chatId, "Введите номер телефона:");
        } else if (user.getPhoneNumber() == null) {
            user.setPhoneNumber(update.getMessage().getText());
            messageService.sendMessage(chatId, "Номер телефона сохранен");
            messageService.sendMessage(chatId, "Введите адрес проживания:");
        } else if (user.getAddress() == null) {
            user.setAddress(update.getMessage().getText());
            messageService.sendMessage(chatId, "КОНТАКТ СОХРАНЕН", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                    new Button("Главное меню", "MAIN_MENU")))));
            bot.getUsersForContactSaving().remove(chatId);
            userDAO.addUser(user);
        }
    }

    public <T extends User> void saveParent(Update update, T user) {
        long chatId = update.getMessage().getChatId();
        if (user.getFullName() == null) {
            user.setFullName(update.getMessage().getText());
            messageService.sendMessage(chatId, "Имя и фамилия сохранены");
            messageService.sendMessage(chatId, "Введите номер телефона:");
        } else if (user.getPhoneNumber() == null) {
            user.setPhoneNumber(update.getMessage().getText());
            messageService.sendMessage(chatId, "Номер телефона сохранен");
            messageService.sendMessage(chatId, "Введите TelegramID клиента." +
                    "Клиент может получить его по ссылке (@userinfobot)");
        } else if (user.getId() == 0) {
            user.setId(Long.parseLong(update.getMessage().getText()));
            messageService.sendMessage(chatId, "ID сохранен");
            user.setTutor_id(getRandomVolunteer());
            LocalDate now = LocalDate.now();
            LocalDate trialEndDate = now.plusDays(30);
            Date date = java.util.Date.from(trialEndDate.atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toInstant());
            user.setTrialEndDate(date);
            messageService.sendMessage(chatId, "КОНТАКТ СОХРАНЕН");
            userDAO.addUser(user);
            bot.getParentsForSaving().remove(chatId);
        }
    }

    public long getRandomVolunteer() {
        List<Volunteer> allVolunteers = volunteerDAO.getAllVolunteers();
        Random random = new Random();
        int range = random.nextInt(allVolunteers.size());
        return allVolunteers.get(range).getChatId();
    }
}


