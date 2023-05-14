package com.skypro.shelter_telegrambot.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static jdk.javadoc.internal.tool.Main.execute;

/**
 * Сервис, предоставляющий информацию о приюте для кошек.
 */
@Service
public class CatShelterService {
    // Добавьте экземпляр вашего бота
    private final TelegramBot bot;

    public CatShelterService(@Lazy TelegramBot bot) {
        this.bot = bot;
    }
    /**
     * Возвращает общую информацию о приюте.
     *
     * @return строка с общей информацией о приюте.
     */
    public String getGeneralInfo() {
        return "Добро пожаловать в приют для кошек!\n\n"
                + "Мы рады видеть вас в нашем приюте. Здесь вы можете познакомиться с кошками, "
                + "которые ищут свой новый дом и заботливых хозяев. Мы предоставляем услуги по "
                + "усыновлению, а также обучению и реабилитации кошек.";
    }

    /**
     * Возвращает адрес приюта и схему проезда.
     *
     * @return строка с адресом и схемой проезда к приюту.
     */
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

    /**
     * Возвращает правила безопасности приюта.
     *
     * @return строка с правилами безопасности.
     */
    public String getSafetyRules() {
        return "Правила пропуска и безопасности в приюте для кошек:\n\n" +
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
        return "Контакты охраны приюта для кошек:\n\n" +
                "Телефон: +7 (123) 456-78-90\n" +
                "E-mail: security@catshelter.org";
    }

    /**
     * Отправляет запрос на помощь волонтеру.
     *
     * @param userChatId идентификатор чата.
     */
    public void requestVolunteer(long userChatId) {
        // Замените GROUP_CHAT_ID на идентификатор чата вашей группы
//        String groupChatId = "GROUP_CHAT_ID";
        String groupChatId = "-1001972925833";

        // Получите username пользователя
        String username = getUsername(userChatId);

        SendMessage message = new SendMessage();
        message.setChatId(groupChatId);
        message.setText("Пользователь (@" + username + ") нуждается в помощи волонтера!");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        InlineKeyboardButton volunteerButton = new InlineKeyboardButton();
        volunteerButton.setText("Помочь пользователю");
        volunteerButton.setUrl("https://t.me/" + username); // Замените на ссылку на чат пользователя

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(volunteerButton);

        keyboard.add(row);
        inlineKeyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            bot.execute(message); // отправка сообщения с помощью экземпляра Telegram-бота
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private String getUsername(long userChatId) {
        GetChat getChat = new GetChat();
        getChat.setChatId(String.valueOf(userChatId));

        try {
            Chat chat = bot.execute(getChat);
            String username = chat.getUserName();
            return username;
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }
}

