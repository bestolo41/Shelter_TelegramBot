package com.skypro.shelter_telegrambot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static jdk.javadoc.internal.tool.Main.execute;

/**
 * Сервис, предоставляющий информацию о приюте для собак.
 */
@Service
public class DogShelterService {

    /**
     * Возвращает общую информацию о приюте.
     *
     * @return строка с общей информацией о приюте.
     */
    public String getGeneralInfo() {
        return "Добро пожаловать в приют для собак!\n\n"
                + "Мы рады видеть вас в нашем приюте. Здесь вы можете познакомиться с собаками, "
                + "которые ищут свой новый дом и заботливых хозяев. Мы предоставляем услуги по "
                + "усыновлению, а также обучению и реабилитации собак.";
    }

    /**
     * Возвращает адрес приюта и схему проезда.
     *
     * @return строка с адресом и схемой проезда к приюту.
     */
    public String getAddressAndDirections() {
        return "Адрес приюта для собак: г.Москва ул. Мира, 10\n\n" +
                "Схема проезда: \n" +
                "1. Проезд на автобусе №32 до остановки 'Приют собак'\n" +
                "2. Пройти 150 метров вдоль улицы Мира\n" +
                "3. Приют расположен на левой стороне улицы" +
                "Расписание работы приюта для собак:\n\n" +
                "Понедельник - пятница: с 9:00 до 17:00\n" +
                "Суббота: с 10:00 до 15:00\n" +
                "Воскресенье: выходной";
    }

    /**
     * Возвращает правила безопасности приюта.
     *
     * @return строка с правилами безопасности.
     */
    public String getSafetyRules() {
        return "Правила пропуска и безопасности в приюте для собак:\n\n" +
                "1. Заполнить анкету посетителя при входе\n" +
                "2. Использовать предоставленные средства индивидуальной защиты (маски, перчатки)\n" +
                "3. Соблюдать инструкции персонала приюта и правила поведения";
    }

    /**
     * Возвращает контактные данные охраны приюта.
     *
     * @return строка с контактами охраны.
     */
    public String getSecurityContacts() {
        return "Контакты охраны приюта для собак:\n\n" +
                "Телефон: +7 (987) 654-32-10\n" +
                "E-mail: security@dogshelter.org";
    }

}
