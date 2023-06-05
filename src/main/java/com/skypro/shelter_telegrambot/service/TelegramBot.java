package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.TelegramBotConfig.TelegramBotConfig;
import com.skypro.shelter_telegrambot.model.*;

import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Класс TelegramBot представляет собой бота для Telegram, осуществляющего взаимодействие с пользователями.
 * Он является подклассом TelegramLongPollingBot и реализует его абстрактные методы getBotUsername() и getBotToken().
 * Класс также содержит различные поля и зависимости для обработки взаимодействия с пользователями.
 */
@Data
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final Map<Long, ? super User> usersForContactSaving = new HashMap<>();
    private final Map<Long, ? super User> usersToSendTheReport = new HashMap<>();
    private final Map<Long, ? super User> parentsForSaving = new HashMap<>();

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
    private final VolunteerDAO volunteerDAO;

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
     * @param volunteerService  Сервис для взаимодействия пользователей с волонтером.
     * @param volunteerDAO      DAO для работы с волонтерами.
     */
    @Lazy
    public TelegramBot(TelegramBotConfig telegramBotConfig, UserDAO userDAO, InfoService infoService, CatShelterService catShelterService, DogShelterService dogShelterService, UserService userService, MessageService messageService, VolunteerService volunteerService, VolunteerDAO volunteerDAO) {
        this.telegramBotConfig = telegramBotConfig;
        this.userDAO = userDAO;
        this.infoService = infoService;
        this.catShelterService = catShelterService;
        this.dogShelterService = dogShelterService;
        this.userService = userService;
        this.messageService = messageService;
        this.volunteerService = volunteerService;
        this.volunteerDAO = volunteerDAO;
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
                    usersForContactSaving.remove(chatId);
                    break;

                //команда для волонтеров
                case "/*volunteer_mode*":
                    messageService.sendMessage(chatId, "Выберите действия:",
                            messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                                    new Button("Стать волонтёром", "BECOME_A_VOLUNTEER"),
                                    new Button("Добавить усыновителя", "ADD_NEW_PARENT")
                            ))));
                    break;

                //Обработка других сообщений
                default:

                    // Обработка сообщений от пользователей, сохраняющих контакты для приюта для кошек
                    // ...

                    if (usersForContactSaving.containsKey(chatId)) {
                        User user = null;
                        if (usersForContactSaving.get(chatId).getClass().equals(CatShelterUser.class)) {
                            user = (CatShelterUser) usersForContactSaving.get(chatId);
                        } else if (usersForContactSaving.get(chatId).getClass().equals(DogShelterUser.class)) {
                            user = (DogShelterUser) usersForContactSaving.get(chatId);
                        }

                        assert user != null;
                        userService.saveContacts(update, user);


                    } else if (parentsForSaving.containsKey(chatId)) {
                        User user = null;
                        if (parentsForSaving.get(chatId).getClass().equals(CatParent.class)) {
                            user = (CatParent) parentsForSaving.get(chatId);
                        } else if (parentsForSaving.get(chatId).getClass().equals(DogParent.class)) {
                            user = (DogParent) parentsForSaving.get(chatId);
                        }
                        userService.saveParent(update, user);


                    // Обработка сообщений от пользователей, отправляющих отчет о питомце, если отправили отчет без фото
                        // ...
                    } else if (usersToSendTheReport.containsKey(chatId)) {
                        messageService.sendMessage(update.getMessage().getChatId(),
                                "Необходимо отправить и фото животного, и описание рациона и условий содержания",
                                messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                                        new Button("Назад", "MAIN_MENU")))));
                    } else {
                        messageService.sendMessage(chatId, "ничего не понял");
                    }
            }

        }

        //обработка сообщения с фото
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            Long chatId = update.getMessage().getChatId();
            if (usersToSendTheReport.containsKey(chatId)) {
                User user = null;
                if (usersToSendTheReport.get(chatId).getClass().equals(CatParent.class)) {
                    user = (CatParent) usersToSendTheReport.get(chatId);
                } else if (usersToSendTheReport.get(chatId).getClass().equals(DogParent.class)) {
                    user = (DogParent) usersToSendTheReport.get(chatId);
                }
                volunteerService.processUserReport(update, user);
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
                    break;

                case "BACK_GENERAL_CAT":

                case "CAT_SHELTER":
                    messageService.editMessage(chatId, callbackQueryMessageId, "Выберите действие для приюта для кошек:", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Узнать информацию о приюте", "INFO_CAT"),
                            new Button("Как взять кошку из приюта", "HOW_CAT"),
                            new Button("Прислать отчет о питомце", "REPORT_CAT"),
                            new Button("Позвать волонтера", "VOLUNTEER_CAT"),
                            new Button("Главное меню", "MAIN_MENU")))));
                    break;

                case "BACK_GENERAL_DOG":

                case "DOG_SHELTER":
                    messageService.editMessage(chatId, callbackQueryMessageId, "Выберите действие для приюта для собак:", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Узнать информацию о приюте", "INFO_DOG"),
                            new Button("Как взять собаку из приюта", "HOW_DOG"),
                            new Button("Прислать отчет о питомце", "REPORT_DOG"),
                            new Button("Позвать волонтера", "VOLUNTEER_DOG"),
                            new Button("Главное меню", "MAIN_MENU")))));
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
                    if (userService.checkUser(new CatShelterUser(chatId))) {
                        messageService.editMessage(chatId, callbackQueryMessageId, "Контакт уже сохранен", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                                new Button("Назад", "BACK_INFO_CAT")))));
                        break;
                    }
                    usersForContactSaving.put(chatId, new CatShelterUser(chatId));
                    messageService.deleteMessage(chatId, callbackQueryMessageId);
                    messageService.sendMessage(chatId, "Введите имя и фамилию:");
                    break;

                case "SET_CONTACT_DOG":
                    if (userService.checkUser(new DogShelterUser(chatId))) {
                        messageService.editMessage(chatId, callbackQueryMessageId, "Контакт уже сохранен", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                                new Button("Назад", "BACK_INFO_DOG")))));
                        break;
                    }
                    usersForContactSaving.put(chatId, new DogShelterUser(chatId));
                    messageService.deleteMessage(chatId, callbackQueryMessageId);
                    messageService.sendMessage(chatId, "Введите имя и фамилию:");
                    break;

                case "REPORT_CAT":
                    CatParent catParent = new CatParent(chatId);
                    if (userService.checkUser(catParent)) {
                        catParent = userDAO.getUser(catParent);
                        usersToSendTheReport.put(chatId, catParent);
                        messageService.editMessage(chatId, callbackQueryMessageId, "Опишите рацион и условия содержания животного, приложите фото животного",
                                messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                                        new Button("Назад", "BACK_GENERAL_CAT")))));
                    } else {
                        messageService.editMessage(chatId, callbackQueryMessageId, "Вы не являетесь владельцем кошки. Обратитесь к волонтеру",
                                messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                                        new Button("Назад", "BACK_GENERAL_CAT")))));
                    }
                    break;
                case "REPORT_DOG":
                    DogParent dogParent = new DogParent(chatId);
                    if (userService.checkUser(dogParent)) {
                        dogParent = userDAO.getUser(dogParent);
                        usersToSendTheReport.put(chatId, dogParent);
                        messageService.editMessage(chatId, callbackQueryMessageId, "Опишите рацион и условия содержания животного, приложите фото животного",
                                messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                                        new Button("Назад", "BACK_GENERAL_DOG")))));
                    } else {
                        messageService.editMessage(chatId, callbackQueryMessageId, "Вы не являетесь владельцем собаки. Обратитесь к волонтеру",
                                messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                                        new Button("Назад", "BACK_GENERAL_DOG")))));
                    }
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
                    User user = null;
                    if (usersToSendTheReport.get(userChatId).getClass().equals(CatParent.class)) {
                        user = (CatParent) usersToSendTheReport.get(userChatId);
                    } else if (usersToSendTheReport.get(userChatId).getClass().equals(DogParent.class)) {
                        user = (DogParent) usersToSendTheReport.get(userChatId);
                    }
                    LocalDate now = LocalDate.now();
                    Date date = java.util.Date.from(now.atStartOfDay()
                            .atZone(ZoneId.systemDefault())
                            .toInstant());
                    user.setReportDate(date);
                    userDAO.updateUser(user);
                    usersToSendTheReport.remove(user.getId());
                    EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
                    editMessageReplyMarkup.setReplyMarkup(null);
                    editMessageReplyMarkup.setChatId(chatId);
                    editMessageReplyMarkup.setMessageId((int) callbackQueryMessageId);
                    try {
                        execute(editMessageReplyMarkup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    messageService.sendMessage(chatId, "Отчет принят");
                    messageService.sendMessage(userChatId, "Отчет принят волонтером");
                    break;

                case "REJECT_REPORT":
                    messageService.sendMessage(userChatId, "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
                            "Пожалуйста, подойди ответственнее к этому занятию. " +
                            "В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного. " +
                            "Отчет не принят.");
                    usersToSendTheReport.remove(userChatId);
                    break;


                case "BECOME_A_VOLUNTEER":
                    if (volunteerService.checkVolunteer(chatId)) {
                        messageService.editMessage(chatId, callbackQueryMessageId, "Вы уже являетесь волонтером");
                    } else {
                        Volunteer volunteer = new Volunteer(
                                chatId,
                                update.getCallbackQuery().getFrom().getFirstName() + " " + update.getCallbackQuery().getFrom().getLastName(),
                                chatId);
                        volunteerDAO.addVolunteer(volunteer);
                        messageService.editMessage(chatId, callbackQueryMessageId, "Поздравляем! Вы теперь волонтер");
                    }
                    break;

                case "ADD_NEW_PARENT":
                    messageService.editMessage(chatId, callbackQueryMessageId, "Кого нужно добавить?", messageService.createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Кошатника", "NEW_CAT_PARENT"),
                            new Button("Собачника", "NEW_DOG_PARENT")
                    ))));
                    break;

                case "NEW_CAT_PARENT":
                    messageService.editMessage(chatId, callbackQueryMessageId,
                            "Введите имя и фамилию клиента:");
                    parentsForSaving.put(chatId, new CatParent());
                    break;

                case "NEW_DOG_PARENT":
                    messageService.editMessage(chatId, callbackQueryMessageId,
                            "Введите имя и фамилию клиента:");
                    parentsForSaving.put(chatId, new DogParent());
                    break;

                case "CONFIRM":
                    messageService.sendMessage(userChatId, "Поздравляем! Вы прошли испытательный срок!");
                    EditMessageText editMessageText3 = new EditMessageText();
                    editMessageText3.setText(update.getCallbackQuery().getMessage().getText());
                    editMessageText3.setChatId(chatId);
                    editMessageText3.setMessageId((int) callbackQueryMessageId);
                    editMessageText3.setReplyMarkup(null);
                    try {
                        execute(editMessageText3);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    messageService.sendMessage(chatId, "Испытательный срок завершен");
                    break;

                case "PROLONG_14":
                    messageService.sendMessage(userChatId, "Волонтер продлил Вам испытательный срок еще на 14 дней. Вам необходимо внимательнее обращаться с животным и ответственнее относиться к отчетам о питомце");
                    CatParent catParent1 = new CatParent(userChatId);
                    DogParent dogParent1 = new DogParent(userChatId);
                    List<CatParent> allCatParents = userDAO.getAllUsers(catParent1);
                    List<DogParent> allDogParents = userDAO.getAllUsers(dogParent1);
                    if (allCatParents.contains(catParent1)) {
                        catParent1 = allCatParents.get(allCatParents.indexOf(catParent1));
                        LocalDate trialEndDate = Instant.ofEpochMilli(catParent1
                                        .getTrialEndDate().getTime())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();


                            LocalDate newTrialEndDate = trialEndDate.plusDays(14);
                            Date newDate = java.util.Date.from(newTrialEndDate.atStartOfDay()
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant());
                            catParent1.setTrialEndDate(newDate);
                            userDAO.updateUser(catParent1);

                    } else if (allDogParents.contains(dogParent1)) {
                        dogParent1 = allDogParents.get(allDogParents.indexOf(dogParent1));
                        LocalDate trialEndDate = Instant.ofEpochMilli(dogParent1
                                        .getTrialEndDate().getTime())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();


                            LocalDate newTrialEndDate = trialEndDate.plusDays(14);
                            Date newDate = java.util.Date.from(newTrialEndDate.atStartOfDay()
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant());
                            dogParent1.setTrialEndDate(newDate);
                            userDAO.updateUser(dogParent1);

                    }

                    EditMessageText editMessageText = new EditMessageText();
                    editMessageText.setText(update.getCallbackQuery().getMessage().getText());
                    editMessageText.setChatId(chatId);
                    editMessageText.setMessageId((int) callbackQueryMessageId);
                    editMessageText.setReplyMarkup(null);
                    try {
                        execute(editMessageText);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    messageService.sendMessage(chatId, "Испытательный срок продлен на 14 дней");
                    break;

                case "PROLONG_30":
                    messageService.sendMessage(userChatId, "Волонтер продлил Вам испытательный срок еще на 30 дней. Вам необходимо внимательнее обращаться с животным и ответственнее относиться к отчетам о питомце");
                    CatParent catParent2 = new CatParent(userChatId);
                    DogParent dogParent2 = new DogParent(userChatId);
                    List<CatParent> allCatParents2 = userDAO.getAllUsers(catParent2);
                    List<DogParent> allDogParents2 = userDAO.getAllUsers(dogParent2);
                    if (allCatParents2.contains(catParent2)) {
                        catParent2 = allCatParents2.get(allCatParents2.indexOf(catParent2));
                        LocalDate trialEndDate = Instant.ofEpochMilli(catParent2
                                        .getTrialEndDate().getTime())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();


                            LocalDate newTrialEndDate = trialEndDate.plusDays(30);
                            Date newDate = java.util.Date.from(newTrialEndDate.atStartOfDay()
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant());
                            catParent2.setTrialEndDate(newDate);
                            userDAO.updateUser(catParent2);

                    } else if (allDogParents2.contains(dogParent2)) {
                        dogParent2 = allDogParents2.get(allDogParents2.indexOf(dogParent2));
                        LocalDate trialEndDate = Instant.ofEpochMilli(dogParent2
                                        .getTrialEndDate().getTime())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();


                            LocalDate newTrialEndDate = trialEndDate.plusDays(30);
                            Date newDate = java.util.Date.from(newTrialEndDate.atStartOfDay()
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant());
                            dogParent2.setTrialEndDate(newDate);
                            userDAO.updateUser(dogParent2);

                    }

                    EditMessageText editMessageText1 = new EditMessageText();
                    editMessageText1.setText(update.getCallbackQuery().getMessage().getText());
                    editMessageText1.setChatId(chatId);
                    editMessageText1.setMessageId((int) callbackQueryMessageId);
                    editMessageText1.setReplyMarkup(null);
                    try {
                        execute(editMessageText1);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    messageService.sendMessage(chatId, "Испытательный срок продлен на 30 дней");
                    break;

                default:
                    messageService.sendMessage(chatId, "Извини, я не понял");
                    break;
            }
        }
    }
}