package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.TelegramBotConfig.TelegramBotConfig;
import com.skypro.shelter_telegrambot.model.Button;
import com.skypro.shelter_telegrambot.model.CatShelterUser;
import com.skypro.shelter_telegrambot.model.DogShelterUser;

import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

/**
 * Класс TelegramBot представляет собой бота для Telegram, осуществляющего взаимодействие с пользователями.
 * Он является подклассом TelegramLongPollingBot и реализует его абстрактные методы getBotUsername() и getBotToken().
 * Класс также содержит различные поля и зависимости для обработки взаимодействия с пользователями.
 */
@Data
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final Map<Long, CatShelterUser> catUsersForSaving = new HashMap<>();
    private final Map<Long, DogShelterUser> dogUsersForSaving = new HashMap<>();
    private final Map<Long, CatShelterUser> catUsersToSendTheReport = new HashMap<>();
    private final Map<Long, DogShelterUser> dogUsersToSendTheReport = new HashMap<>();
    private final CatShelterUser catUser = new CatShelterUser();
    private final DogShelterUser dogUser = new DogShelterUser();
    private final TelegramBotConfig telegramBotConfig;
    private final UserDAO userDAO;
    private final InfoService infoService;
    private final CatShelterService catShelterService;
    private final DogShelterService dogShelterService;
    private final UserService userService;
    private final MessageService messageService;
    private final VolunteerService volunteerService;

    /**
     * Конструктор TelegramBot.
     * Инициализирует объект с помощью указанных зависимостей.
     *
     * @param telegramBotConfig Конфигурация бота Telegram, содержащая имя и токен бота.
     * @param userDAO           DAO для работы с пользователями.
     * @param infoService       Сервис для работы с информацией о приютах.
     * @param catShelterService Сервис для работы с приютами для кошек.
     * @param dogShelterService Сервис для работы с приютами для собак.
     * @param userService       Сервис для работы с пользователями.
     * @param messageService    Сервис для обработки сообщений.
     * @param volunteerService
     */
    @Lazy
    public TelegramBot(TelegramBotConfig telegramBotConfig, UserDAO userDAO, InfoService infoService, CatShelterService catShelterService, DogShelterService dogShelterService, UserService userService, MessageService messageService, VolunteerService volunteerService) {
        this.telegramBotConfig = telegramBotConfig;
        this.userDAO = userDAO;
        this.infoService = infoService;
        this.catShelterService = catShelterService;
        this.dogShelterService = dogShelterService;
        this.userService = userService;
        this.messageService = messageService;
        this.volunteerService = volunteerService;
    }

    /**
     * Метод для получения имени бота.
     *
     * @return Имя бота.
     */
    @Override
    public String getBotUsername() {
        return telegramBotConfig.getName();
    }

    /**
     * Метод для получения токена бота.
     *
     * @return Токен бота
     */
    @Override
    public String getBotToken() {
        return telegramBotConfig.getToken();
    }

    /**
     * Метод, вызываемый при получении обновления от Telegram.
     * Обрабатывает полученные сообщения и коллбэк-запросы.
     *
     * @param update Объект Update, содержащий информацию об обновлении от Telegram.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    // Обработка команды /start
                    messageService.startCommandReceived(chatId, update.getMessage().getChat().getFirstName(), update);
                    dogUsersForSaving.remove(chatId);
                    catUsersForSaving.remove(chatId);
                    break;

                //Обработка других сообщений
                default:

                    // Обработка сообщений от пользователей, сохраняющих контакты для приюта для кошек
                    // ...

                    if (catUsersForSaving.containsKey(chatId)) {

                        if (catUsersForSaving.get(chatId).getFullName() == null) {
                            catUsersForSaving.get(chatId).setFullName(update.getMessage().getText());
                            messageService.sendMessage(chatId, "Имя и фамилия сохранены");
                            messageService.sendMessage(chatId, "Введите возраст:");
                        } else if (catUsersForSaving.get(chatId).getAge() <= 0) {
                            catUsersForSaving.get(chatId).setAge(Integer.parseInt(update.getMessage().getText()));
                            messageService.sendMessage(chatId, "Возраст сохранен");
                            messageService.sendMessage(chatId, "Введите номер телефона:");
                        } else if (catUsersForSaving.get(chatId).getPhoneNumber() == null) {
                            catUsersForSaving.get(chatId).setPhoneNumber(update.getMessage().getText());
                            messageService.sendMessage(chatId, "Номер телефона сохранен");
                            messageService.sendMessage(chatId, "Введите адрес проживания:");
                        } else if (catUsersForSaving.get(chatId).getAddress() == null) {
                            catUsersForSaving.get(chatId).setAddress(update.getMessage().getText());
                            messageService.sendMessage(chatId, "КОНТАКТ СОХРАНЕН", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                                    new Button("Назад", "BACK_INFO_CAT")))));
                            userDAO.addCatUser(catUsersForSaving.get(chatId));
                            catUsersForSaving.remove(chatId);
                        }
                    } else if (dogUsersForSaving.containsKey(chatId)) {


                        // Обработка сообщений от пользователей, сохраняющих контакты для приюта для собак
                        // ...
                        if (dogUsersForSaving.get(chatId).getFullName() == null) {
                            dogUsersForSaving.get(chatId).setFullName(update.getMessage().getText());
                            messageService.sendMessage(chatId, "Имя и фамилия сохранены");
                            messageService.sendMessage(chatId, "Введите возраст:");
                        } else if (dogUsersForSaving.get(chatId).getAge() <= 0) {
                            dogUsersForSaving.get(chatId).setAge(Integer.parseInt(update.getMessage().getText()));
                            messageService.sendMessage(chatId, "Возраст сохранен");
                            messageService.sendMessage(chatId, "Введите номер телефона:");
                        } else if (dogUsersForSaving.get(chatId).getPhoneNumber() == null) {
                            dogUsersForSaving.get(chatId).setPhoneNumber(update.getMessage().getText());
                            messageService.sendMessage(chatId, "Номер телефона сохранен");
                            messageService.sendMessage(chatId, "Введите адрес проживания:");
                        } else if (dogUsersForSaving.get(chatId).getAddress() == null) {
                            dogUsersForSaving.get(chatId).setAddress(update.getMessage().getText());
                            messageService.sendMessage(chatId, "КОНТАКТ СОХРАНЕН", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                                    new Button("Назад", "BACK_INFO_DOG")))));
                            userDAO.addDogUser(dogUsersForSaving.get(chatId));
                            dogUsersForSaving.remove(chatId);
                        }

                        // Обработка сообщений от пользователей, отправляющих отчет о питомце
                        // ...
                    } else if (catUsersToSendTheReport.containsKey(chatId) || dogUsersToSendTheReport.containsKey(chatId)) {
                        messageService.sendMessage(update.getMessage().getChatId(),
                                "Необходимо отправить и фото животного, и описание рациона и условий содержания",
                                messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                                        new Button("Назад", "MAIN_MENU")))));
                    } else {
                        messageService.sendMessage(chatId, "ничего не понял");
                    }
            }

        }

        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            Long chatId = update.getMessage().getChatId();
            if (catUsersToSendTheReport.containsKey(chatId) || dogUsersToSendTheReport.containsKey(chatId)) {
                volunteerService.processUserReport(update);

            }
        } else if (update.hasCallbackQuery())


        // Обработка коллбэк-запроса
        // ...
        {
            String callbackData = update.getCallbackQuery().getData();
            String trueCallbackData;
            long userChatId = 0;
            long callbackQueryMessageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            if (callbackData.contains("/")) {
                String[] splitCallbackData = callbackData.split("/");
                trueCallbackData = splitCallbackData[0];
                userChatId = Long.parseLong(splitCallbackData[1]);
            } else {
                trueCallbackData = callbackData;
            }

            switch (trueCallbackData) {
                case "MAIN_MENU":
                    messageService.editMessage(chatId, callbackQueryMessageId, "Выбери какой приют тебя интересует:", messageService.createButtons(2, new ArrayList<>(Arrays.asList(
                            new Button("Приют для кошек", "CAT_SHELTER"),
                            new Button("Приют для собак", "DOG_SHELTER")))));
                    catUsersToSendTheReport.remove(chatId);
                    dogUsersToSendTheReport.remove(chatId);
                    break;

                case "BACK_GENERAL_CAT":

                case "CAT_SHELTER":
                    messageService.editMessage(chatId, callbackQueryMessageId, "Выберите действие для приюта для кошек:", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Узнать информацию о приюте", "INFO_CAT"),
                            new Button("Как взять кошку из приюта", "HOW_CAT"),
                            new Button("Прислать отчет о питомце", "REPORT_CAT"),
                            new Button("Позвать волонтера", "VOLUNTEER_CAT"),
                            new Button("Главное меню", "MAIN_MENU")))));
                    catUsersToSendTheReport.remove(chatId);
                    break;

                case "BACK_GENERAL_DOG":

                case "DOG_SHELTER":
                    messageService.editMessage(chatId, callbackQueryMessageId, "Выберите действие для приюта для собак:", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Узнать информацию о приюте", "INFO_DOG"),
                            new Button("Как взять собаку из приюта", "HOW_DOG"),
                            new Button("Прислать отчет о питомце", "REPORT_DOG"),
                            new Button("Позвать волонтера", "VOLUNTEER_DOG"),
                            new Button("Главное меню", "MAIN_MENU")))));
                    dogUsersToSendTheReport.remove(chatId);
                    break;

                case "BACK_INFO_CAT":

                case "INFO_CAT":
                    messageService.editMessage(chatId, callbackQueryMessageId, "Какую информацию хотите узнать о приюте для кошек?", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Общая информация о приюте", "GEN_INFO_CAT"),
                            new Button("График, адрес и схема проезда", "TIME_ADDRESS_DIRECTION_CAT"),
                            new Button("Контакты охраны", "SECURITY_CAT"),
                            new Button("Техника безопасности", "SAFETY_CAT"),
                            new Button("Оставить контакты", "SET_CONTACT_CAT"),
                            new Button("Позвать волонтера", "VOLUNTEER_CAT"),
                            new Button("Назад", "BACK_GENERAL_CAT")))));
                    break;

                case "BACK_INFO_DOG":

                case "INFO_DOG":
                    messageService.editMessage(chatId, callbackQueryMessageId, "Какую информацию хотите узнать о приюте для собак?", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Общая информация о приюте", "GEN_INFO_DOG"),
                            new Button("График, адрес и схема проезда", "TIME_ADDRESS_DIRECTION_DOG"),
                            new Button("Контакты охраны", "SECURITY_DOG"),
                            new Button("Техника безопасности", "SAFETY_DOG"),
                            new Button("Оставить контакты", "SET_CONTACT_DOG"),
                            new Button("Позвать волонтера", "VOLUNTEER_DOG"),
                            new Button("Назад", "BACK_GENERAL_DOG")))));
                    break;

                case "BACK_HOW_CAT":

                case "HOW_CAT":
                    messageService.editMessage(chatId, callbackQueryMessageId, "Информация для получения кошки из приюта:", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Правила знакомства", "DATING_RULES_CAT"),
                            new Button("Необходимые документы", "DOCS_CAT"),
                            new Button("Рекомендации по транспортировке кошки", "TRANSPORTING_CAT"),
                            new Button("Рекомендации по обустройству дома для котенка", "HOME_LITTLE_CAT"),
                            new Button("Рекомендации по обустройству дома для взрослого кота", "HOME_ADULT_CAT"),
                            new Button("Рекомендации по обустройству дома для кота с ограниченными возможностями", "HOME_INVALID_CAT"),
                            new Button("Причины для отказа", "REASONS_CAT"),
                            new Button("Оставить контакты", "SET_CONTACT_CAT"),
                            new Button("Позвать волонтера", "VOLUNTEER_CAT"),
                            new Button("Назад", "BACK_GENERAL_CAT")))));
                    break;

                case "BACK_HOW_DOG":

                case "HOW_DOG":
                    messageService.editMessage(chatId, callbackQueryMessageId, "Информация для получения собаки из приюта:", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Правила знакомства", "DATING_RULES_DOG"),
                            new Button("Необходимые документы", "DOCS_DOG"),
                            new Button("Рекомендации по транспортировке собаки", "TRANSPORTING_DOG"),
                            new Button("Рекомендации по обустройству дома для щенка", "HOME_LITTLE_DOG"),
                            new Button("Рекомендации по обустройству дома для взрослой собаки", "HOME_ADULT_DOG"),
                            new Button("Рекомендации по обустройству дома для собаки с ограниченными возможностями", "HOME_INVALID_DOG"),
                            new Button("Причины для отказа", "REASONS_DOG"),
                            new Button("Советы кинолога по первичному общению с собакой", "PRIMARY_RECOMMENDATION"),
                            new Button("Рекомендованные кинологи", "PROVEN_DOG_HANDLER_RECOMMENDATION"),
                            new Button("Оставить контакты", "SET_CONTACT_DOG"),
                            new Button("Позвать волонтера", "VOLUNTEER_DOG"),
                            new Button("Назад", "BACK_GENERAL_DOG")))));
                    break;

                case "DATING_RULES_CAT":
                    messageService.editMessage(chatId, callbackQueryMessageId, infoService.getDatingRules(callbackData), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_CAT")))));
                    break;

                case "DATING_RULES_DOG":
                    messageService.editMessage(chatId, callbackQueryMessageId, infoService.getDatingRules(callbackData), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "GEN_INFO_CAT":
                    messageService.editMessage(chatId, callbackQueryMessageId, catShelterService.getGeneralInfo(), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_CAT")))));
                    break;

                case "GEN_INFO_DOG":
                    messageService.editMessage(chatId, callbackQueryMessageId, dogShelterService.getGeneralInfo(), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_DOG")))));
                    break;

                case "TIME_ADDRESS_DIRECTION_CAT":
                    messageService.editMessage(chatId, callbackQueryMessageId, catShelterService.getAddressAndDirections(), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_CAT")))));
                    break;

                case "TIME_ADDRESS_DIRECTION_DOG":
                    messageService.editMessage(chatId, callbackQueryMessageId, dogShelterService.getAddressAndDirections(), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_DOG")))));
                    break;

                case "SECURITY_CAT":
                    messageService.editMessage(chatId, callbackQueryMessageId, catShelterService.getSecurityContacts(), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_CAT")))));
                    break;

                case "SECURITY_DOG":
                    messageService.editMessage(chatId, callbackQueryMessageId, dogShelterService.getSecurityContacts(), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_DOG")))));
                    break;

                case "SAFETY_CAT":
                    messageService.editMessage(chatId, callbackQueryMessageId, catShelterService.getSafetyRules(), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_CAT")))));
                    break;

                case "SAFETY_DOG":
                    messageService.editMessage(chatId, callbackQueryMessageId, dogShelterService.getSafetyRules(), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_DOG")))));
                    break;

                case "SET_CONTACT_CAT":
                    if (userService.checkCatUser(update.getCallbackQuery().getFrom().getId())) {
                        messageService.editMessage(chatId, callbackQueryMessageId, "Контакт уже сохранен", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                                new Button("Назад", "BACK_INFO_CAT")))));
                        break;
                    }
                    catUsersForSaving.put(chatId, new CatShelterUser(chatId));
                    messageService.deleteMessage(chatId, callbackQueryMessageId);
                    messageService.sendMessage(chatId, "Введите имя и фамилию:");
                    break;

                case "SET_CONTACT_DOG":
                    if (userService.checkDogUser(update.getCallbackQuery().getFrom().getId())) {
                        messageService.editMessage(chatId, callbackQueryMessageId, "Контакт уже сохранен", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                                new Button("Назад", "BACK_INFO_DOG")))));
                        break;
                    }
                    dogUsersForSaving.put(chatId, new DogShelterUser());
                    messageService.deleteMessage(chatId, callbackQueryMessageId);
                    messageService.sendMessage(chatId, "Введите имя и фамилию:");
                    break;

                case "REPORT_CAT":
                    catUsersToSendTheReport.put(chatId, new CatShelterUser());
                    messageService.editMessage(chatId, callbackQueryMessageId, "Опишите рацион и условия содержания животного, приложите фото животного",
                            messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                                    new Button("Назад", "BACK_GENERAL_CAT")))));
                    break;
                case "REPORT_DOG":
                    dogUsersToSendTheReport.put(chatId, new DogShelterUser());
                    messageService.editMessage(chatId, callbackQueryMessageId, "Опишите рацион и условия содержания животного, приложите фото животного",
                            messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                                    new Button("Назад", "BACK_GENERAL_DOG")))));
                    break;

                case "VOLUNTEER_CAT":
                    volunteerService.requestVolunteer(chatId);
                    messageService.editMessage(chatId, callbackQueryMessageId, "Волонетёр напишет Вам в ближайшее время", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_CAT")))));
                    break;

                case "VOLUNTEER_DOG":
                    volunteerService.requestVolunteer(chatId);
                    messageService.editMessage(chatId, callbackQueryMessageId, "Волонетёр напишет Вам в ближайшее время", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_DOG")))));
                    break;

                case "DOCS_CAT":
                    messageService.editMessage(chatId, callbackQueryMessageId, infoService.getDocuments(callbackData), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_CAT")))));
                    break;
                case "DOCS_DOG":
                    messageService.editMessage(chatId, callbackQueryMessageId, infoService.getDocuments(callbackData), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "TRANSPORTING_CAT":
                    messageService.editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationForTransportingAnimal(callbackData), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_CAT")))));
                    break;

                case "TRANSPORTING_DOG":
                    messageService.editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationForTransportingAnimal(callbackData), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "HOME_LITTLE_CAT":
                    messageService.editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationHomeImprovementForLittleAnimal(callbackData), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_CAT")))));
                    break;

                case "HOME_LITTLE_DOG":
                    messageService.editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationHomeImprovementForLittleAnimal(callbackData), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "HOME_ADULT_CAT":
                    messageService.editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationHomeImprovementForAdultAnimal(callbackData), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_CAT")))));
                    break;

                case "HOME_ADULT_DOG":
                    messageService.editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationHomeImprovementForAdultAnimal(callbackData), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "HOME_INVALID_CAT":
                    messageService.editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationHomeImprovementForDisabilityAnimal(callbackData), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_CAT")))));
                    break;

                case "HOME_INVALID_DOG":
                    messageService.editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationHomeImprovementForDisabilityAnimal(callbackData), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "REASONS_CAT":
                    messageService.editMessage(chatId, callbackQueryMessageId, infoService.getListOfReason(callbackData), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_CAT")))));
                    break;

                case "REASONS_DOG":
                    messageService.editMessage(chatId, callbackQueryMessageId, infoService.getListOfReason(callbackData), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "PRIMARY_RECOMMENDATION":
                    messageService.editMessage(chatId, callbackQueryMessageId, infoService.getPrimaryRecommendationDogHandler(callbackData), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "PROVEN_DOG_HANDLER_RECOMMENDATION":
                    messageService.editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationProvenDogHandler(callbackData), messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "ACCEPT_REPORT":
                    messageService.sendMessage(userChatId, "Отчет принят волонтером");
                    break;

                case "REJECT_REPORT":
                    messageService.sendMessage(userChatId, "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
                            "Пожалуйста, подойди ответственнее к этому занятию. " +
                            "В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного");
                    break;
                default:
                    messageService.sendMessage(chatId, "Извини, я не понял");
                    break;
            }
        }
    }
}