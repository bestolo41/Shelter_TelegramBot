package com.skypro.shelter_telegrambot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static jdk.javadoc.internal.tool.Main.execute;

@Service
public class CatShelterService {
    public String getGeneralInfo() {
        return "Добро пожаловать в приют для кошек!\n\n"
                + "Мы рады видеть вас в нашем приюте. Здесь вы можете познакомиться с кошками, "
                + "которые ищут свой новый дом и заботливых хозяев. Мы предоставляем услуги по "
                + "усыновлению, а также обучению и реабилитации кошек.";
    }

    public String getAddressAndDirections() {
        return "Адрес приюта для кошек: г.Казань ул. Ленина, 5\n\n" +
                "Схема проезда: \n" +
                "1. Проезд на автобусе №25 до остановки 'Приют'\n" +
                "2. Пройти 200 метров вдоль улицы Ленина\n" +
                "3. Приют расположен на правой стороне улицы" +
                "Расписание работы приюта для кошек:\n\n" +
                "Понедельник - пятница: с 10:00 до 18:00\n" +
                "Суббота: с 11:00 до 16:00\n" +
                "Воскресенье: выходной";
    }

    public String getSafetyRules() {
        return "Правила пропуска и безопасности в приюте для кошек:\n\n" +
                "1. Заполнить анкету посетителя при входе\n" +
                "2. Использовать предоставленные средства индивидуальной защиты (маски, перчатки)\n" +
                "3. Соблюдать инструкции персонала приюта и правила поведения";
    }

    public String getSecurityContacts() {
        return "Контакты охраны приюта для кошек:\n\n" +
                "Телефон: +7 (123) 456-78-90\n" +
                "E-mail: security@catshelter.org";
    }
        public void requestVolunteer(long chatId) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Пользователь нуждается в помощи волонтера!");

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

            InlineKeyboardButton volunteerButton = new InlineKeyboardButton();
            volunteerButton.setText("Помочь пользователю");
            volunteerButton.setUrl("https://t.me/Digital20_group"); // Замените на ссылку вашей группы

            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(volunteerButton);

            keyboard.add(row);
            inlineKeyboardMarkup.setKeyboard(keyboard);

            message.setReplyMarkup(inlineKeyboardMarkup);

            execute(String.valueOf(message)); // отправка сообщения с помощью TelegramBotsApi
        }
    }

