package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.TelegramBotConfig.TelegramBotConfig;
import com.skypro.shelter_telegrambot.model.Button;
import com.skypro.shelter_telegrambot.model.CatShelterUser;
import com.skypro.shelter_telegrambot.model.DogShelterUser;
import com.skypro.shelter_telegrambot.model.User;

import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
    private final CatShelterUser catUser = new CatShelterUser();
    private final DogShelterUser dogUser = new DogShelterUser();
    private boolean dialogueMode = false;
    private long volunteerChatId;
    private final TelegramBotConfig telegramBotConfig;
    private final UserDAO userDAO;
    private final VolunteerDAO volunteerDAO;
    private final InfoService infoService;
    private final CatShelterService catShelterService;
    private final DogShelterService dogShelterService;
    private final UserService userService;
    private final MessageService messageService;
    private final VolunteerBot volunteerBot;

    /**
     * Конструктор TelegramBot.
     * Инициализирует объект с помощью указанных зависимостей.
     *
     * @param telegramBotConfig Конфигурация бота Telegram, содержащая имя и токен бота.
     * @param userDAO           DAO для работы с пользователями.
     * @param volunteerDAO      DAO для работы с волонтерами.
     * @param infoService       Сервис для работы с информацией о приютах.
     * @param catShelterService Сервис для работы с приютами для кошек.
     * @param dogShelterService Сервис для работы с приютами для собак.
     * @param userService       Сервис для работы с пользователями.
     * @param messageService    Сервис для обработки сообщений.
     * @param volunteerBot      Бот-волонтер.
     */
    @Lazy
    public TelegramBot(TelegramBotConfig telegramBotConfig, UserDAO userDAO, VolunteerDAO volunteerDAO, InfoService infoService, CatShelterService catShelterService, DogShelterService dogShelterService, UserService userService, MessageService messageService, VolunteerBot volunteerBot) {
        this.telegramBotConfig = telegramBotConfig;
        this.userDAO = userDAO;
        this.volunteerDAO = volunteerDAO;
        this.infoService = infoService;
        this.catShelterService = catShelterService;
        this.dogShelterService = dogShelterService;
        this.userService = userService;
        this.messageService = messageService;
        this.volunteerBot = volunteerBot;
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
            if ("/call".equals(messageText)) { // Замените на нужную команду
                catShelterService.requestVolunteer(chatId);
            }

            switch (messageText) {
                case "/start":
                    // Обработка команды /start
                    try {
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName(), update);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    dogUsersForSaving.remove(chatId);
                    catUsersForSaving.remove(chatId);
                    break;
                case "/stop":
                    // Обработка команды /stop
                    dialogueMode = false;
                    volunteerBot.setDialogueMode(false);
                    volunteerBot.sendMessage(volunteerChatId, "Пользователь завершил чат");
                    sendMessage(chatId, "/start");
                    break;
                default:
                    // Обработка других сообщений
                    if (catUsersForSaving.containsKey(chatId)) {
                        // Обработка сообщений от пользователей, сохраняющих данные о приюте для кошек
                        // ...
                        if (catUsersForSaving.get(chatId).getFullName() == null) {
                            catUsersForSaving.get(chatId).setFullName(update.getMessage().getText());
                            sendMessage(chatId, "Имя и фамилия сохранены");
                            sendMessage(chatId, "Введите возраст:");
                        } else if (catUsersForSaving.get(chatId).getAge() <= 0) {
                            catUsersForSaving.get(chatId).setAge(Integer.parseInt(update.getMessage().getText()));
                            sendMessage(chatId, "Возраст сохранен");
                            sendMessage(chatId, "Введите номер телефона:");
                        } else if (catUsersForSaving.get(chatId).getPhoneNumber() == null) {
                            catUsersForSaving.get(chatId).setPhoneNumber(update.getMessage().getText());
                            sendMessage(chatId, "Номер телефона сохранен");
                            sendMessage(chatId, "Введите адрес проживания:");
                        } else if (catUsersForSaving.get(chatId).getAddress() == null) {
                            catUsersForSaving.get(chatId).setAddress(update.getMessage().getText());
                            sendMessage(chatId, "КОНТАКТ СОХРАНЕН", createButtons(1, new ArrayList<>(Arrays.asList(
                                    new Button("Назад", "BACK_INFO_CAT")))));
                            userDAO.addCatUser(catUsersForSaving.get(chatId));
                            catUsersForSaving.remove(chatId);
                        }
                    } else if (dogUsersForSaving.containsKey(chatId)) {
                        // Обработка сообщений от пользователей, сохраняющих данные о приюте для собак
                        // ...
                        if (dogUsersForSaving.get(chatId).getFullName() == null) {
                            dogUsersForSaving.get(chatId).setFullName(update.getMessage().getText());
                            sendMessage(chatId, "Имя и фамилия сохранены");
                            sendMessage(chatId, "Введите возраст:");
                        } else if (dogUsersForSaving.get(chatId).getAge() <= 0) {
                            dogUsersForSaving.get(chatId).setAge(Integer.parseInt(update.getMessage().getText()));
                            sendMessage(chatId, "Возраст сохранен");
                            sendMessage(chatId, "Введите номер телефона:");
                        } else if (dogUsersForSaving.get(chatId).getPhoneNumber() == null) {
                            dogUsersForSaving.get(chatId).setPhoneNumber(update.getMessage().getText());
                            sendMessage(chatId, "Номер телефона сохранен");
                            sendMessage(chatId, "Введите адрес проживания:");
                        } else if (dogUsersForSaving.get(chatId).getAddress() == null) {
                            dogUsersForSaving.get(chatId).setAddress(update.getMessage().getText());
                            sendMessage(chatId, "КОНТАКТ СОХРАНЕН", createButtons(1, new ArrayList<>(Arrays.asList(
                                    new Button("Назад", "BACK_INFO_DOG")))));
                            userDAO.addDogUser(dogUsersForSaving.get(chatId));
                            dogUsersForSaving.remove(chatId);
                        }
                    } else if (dialogueMode) {
                        // Обработка сообщений в режиме диалога с волонтером
                        // ...
                        volunteerBot.sendMessage(volunteerChatId, update.getMessage().getText());
                    } else {
                        sendMessage(chatId, "ничего не понял");
                    }
            }

        } else if (update.hasCallbackQuery())
        // Обработка коллбэк-запроса
        // ...
        {
            String callbackData = update.getCallbackQuery().getData();
            long callbackQueryMessageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callbackData) {
                case "MAIN_MENU":
                    editMessage(chatId, callbackQueryMessageId, "Выбери какой приют тебя интересует:", createButtons(2, new ArrayList<>(Arrays.asList(
                            new Button("Приют для кошек", "CAT_SHELTER"),
                            new Button("Приют для собак", "DOG_SHELTER")))));
                    break;

                case "BACK_GENERAL_CAT":

                case "CAT_SHELTER":
                    editMessage(chatId, callbackQueryMessageId, "Выберите действие для приюта для кошек:", createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Узнать информацию о приюте", "INFO_CAT"),
                            new Button("Как взять кошку из приюта", "HOW_CAT"),
                            new Button("Прислать отчет о питомце", "REPORT_CAT"),
                            new Button("Позвать волонтера", "VOLUNTEER_CAT"),
                            new Button("Главное меню", "MAIN_MENU")))));
                    break;

                case "BACK_GENERAL_DOG":

                case "DOG_SHELTER":
                    editMessage(chatId, callbackQueryMessageId, "Выберите действие для приюта для собак:", createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Узнать информацию о приюте", "INFO_DOG"),
                            new Button("Как взять собаку из приюта", "HOW_DOG"),
                            new Button("Прислать отчет о питомце", "REPORT_DOG"),
                            new Button("Позвать волонтера", "VOLUNTEER_DOG"),
                            new Button("Главное меню", "MAIN_MENU")))));
                    break;

                case "BACK_INFO_CAT":

                case "INFO_CAT":
                    editMessage(chatId, callbackQueryMessageId, "Какую информацию хотите узнать о приюте для кошек?", createButtons(1, new ArrayList<>(Arrays.asList(
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
                    editMessage(chatId, callbackQueryMessageId, "Какую информацию хотите узнать о приюте для собак?", createButtons(1, new ArrayList<>(Arrays.asList(
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
                    editMessage(chatId, callbackQueryMessageId, "Информация для получения кошки из приюта:", createButtons(1, new ArrayList<>(Arrays.asList(
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
                    editMessage(chatId, callbackQueryMessageId, "Информация для получения собаки из приюта:", createButtons(1, new ArrayList<>(Arrays.asList(
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
                    editMessage(chatId, callbackQueryMessageId, infoService.getDatingRules(callbackData), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_CAT")))));
                    break;

                case "DATING_RULES_DOG":
                    editMessage(chatId, callbackQueryMessageId, infoService.getDatingRules(callbackData), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "GEN_INFO_CAT":
                    editMessage(chatId, callbackQueryMessageId, catShelterService.getGeneralInfo(), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_CAT")))));
                    break;

                case "GEN_INFO_DOG":
                    editMessage(chatId, callbackQueryMessageId, dogShelterService.getGeneralInfo(), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_DOG")))));
                    break;

                case "TIME_ADDRESS_DIRECTION_CAT":
                    editMessage(chatId, callbackQueryMessageId, catShelterService.getAddressAndDirections(), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_CAT")))));
                    break;

                case "TIME_ADDRESS_DIRECTION_DOG":
                    editMessage(chatId, callbackQueryMessageId, dogShelterService.getAddressAndDirections(), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_DOG")))));
                    break;

                case "SECURITY_CAT":
                    editMessage(chatId, callbackQueryMessageId, catShelterService.getSecurityContacts(), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_CAT")))));
                    break;

                case "SECURITY_DOG":
                    editMessage(chatId, callbackQueryMessageId, dogShelterService.getSecurityContacts(), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_DOG")))));
                    break;

                case "SAFETY_CAT":
                    editMessage(chatId, callbackQueryMessageId, catShelterService.getSafetyRules(), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_CAT")))));
                    break;

                case "SAFETY_DOG":
                    editMessage(chatId, callbackQueryMessageId, dogShelterService.getSafetyRules(), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_DOG")))));
                    break;

                case "SET_CONTACT_CAT":
                    if (userService.checkCatUser(update.getCallbackQuery().getFrom().getId())) {
                        editMessage(chatId, callbackQueryMessageId, "Контакт уже сохранен", createButtons(1, new ArrayList<>(Arrays.asList(
                                new Button("Назад", "BACK_INFO_CAT")))));
                        break;
                    }
                    catUsersForSaving.put(chatId, new CatShelterUser(chatId));

                    try {
                        execute(messageService.deleteMessage(chatId, callbackQueryMessageId));
                        SendMessage fullNameRequestMessage = new SendMessage();
                        fullNameRequestMessage.setText("Введите имя и фамилию:");
                        fullNameRequestMessage.setChatId(chatId);
                        execute(fullNameRequestMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "SET_CONTACT_DOG":
                    if (userService.checkDogUser(update.getCallbackQuery().getFrom().getId())) {
                        editMessage(chatId, callbackQueryMessageId, "Контакт уже сохранен", createButtons(1, new ArrayList<>(Arrays.asList(
                                new Button("Назад", "BACK_INFO_DOG")))));
                        break;
                    }
                    dogUsersForSaving.put(chatId, new DogShelterUser());
                    try {
                        execute(messageService.deleteMessage(chatId, callbackQueryMessageId));
                        SendMessage fullNameRequestMessage = new SendMessage();
                        fullNameRequestMessage.setText("Введите имя и фамилию:");
                        fullNameRequestMessage.setChatId(chatId);
                        execute(fullNameRequestMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "VOLUNTEER_CAT":
                    volunteerChatId = volunteerDAO.getAllVolunteers().get(1).getChatId(); //
                    dialogueMode = true;

                    sendMessage(chatId, "Ожидайте ответа волонтера");
                    volunteerBot.sendMessage(chatId, volunteerChatId, "Вас вызывает пользователь. Принять запрос?");


                    break;


                case "VOLUNTEER_DOG":
                    editMessage(chatId, callbackQueryMessageId, "!!!В РАЗРАБОТКЕ!!!", createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_DOG")))));
                    break;

                case "DOCS_CAT":
                    editMessage(chatId, callbackQueryMessageId, infoService.getDocuments(callbackData), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_CAT")))));
                    break;
                case "DOCS_DOG":
                    editMessage(chatId, callbackQueryMessageId, infoService.getDocuments(callbackData), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "TRANSPORTING_CAT":
                    editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationForTransportingAnimal(callbackData), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_CAT")))));
                    break;

                case "TRANSPORTING_DOG":
                    editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationForTransportingAnimal(callbackData), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "HOME_LITTLE_CAT":
                    editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationHomeImprovementForLittleAnimal(callbackData), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_CAT")))));
                    break;

                case "HOME_LITTLE_DOG":
                    editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationHomeImprovementForLittleAnimal(callbackData), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "HOME_ADULT_CAT":
                    editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationHomeImprovementForAdultAnimal(callbackData), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_CAT")))));
                    break;

                case "HOME_ADULT_DOG":
                    editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationHomeImprovementForAdultAnimal(callbackData), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "HOME_INVALID_CAT":
                    editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationHomeImprovementForDisabilityAnimal(callbackData), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_CAT")))));
                    break;

                case "HOME_INVALID_DOG":
                    editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationHomeImprovementForDisabilityAnimal(callbackData), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "REASONS_CAT":
                    editMessage(chatId, callbackQueryMessageId, infoService.getListOfReason(callbackData), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_CAT")))));
                    break;

                case "REASONS_DOG":
                    editMessage(chatId, callbackQueryMessageId, infoService.getListOfReason(callbackData), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "PRIMARY_RECOMMENDATION":
                    editMessage(chatId, callbackQueryMessageId, infoService.getPrimaryRecommendationDogHandler(callbackData), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;

                case "PROVEN_DOG_HANDLER_RECOMMENDATION":
                    editMessage(chatId, callbackQueryMessageId, infoService.getRecommendationProvenDogHandler(callbackData), createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_HOW_DOG")))));
                    break;
                default:
                    sendMessage(chatId, "Извини, я не понял");
                    break;
            }
        }

    }


    /**
     * Отправляет сообщение-приветствие и начальное меню после апдейта /start
     *
     * @param chatId Идентификатор чата
     * @param name   Имя пользователя
     * @throws TelegramApiException Если возникла ошибка при отправке сообщения
     */
    private void startCommandReceived(long chatId, String name, Update update) throws TelegramApiException {

        String greeting = "Привет, " + name + "!";
        String infoAboutBot = """
                Я могу рассказать тебе, что нужно знать и уметь для того, чтобы забрать животное из приюта.
                                
                Также я могу предоставить всю информацию о всех доступных приютах города.
                                
                А еще ты можешь отправлять мне ежедневные отчеты о том, как животное приспосабливается к новой обстановке.
                """;
        String question = "Выбери какой приют тебя интересует:";
        InlineKeyboardMarkup catOrDogShelterButtons = createButtons(2, new ArrayList<>(Arrays.asList(
                new Button("Приют для кошек", "CAT_SHELTER"),
                new Button("Приют для собак", "DOG_SHELTER"))));

        if (userService.checkUser(update.getMessage().getFrom().getId())) {
            sendMessage(chatId, greeting);
            sendMessage(chatId, question, catOrDogShelterButtons);
        } else {
            sendMessage(chatId, greeting);
            sendMessage(chatId, infoAboutBot);
            sendMessage(chatId, question, catOrDogShelterButtons);

            User newUser = new User();
            newUser.setId(update.getMessage().getFrom().getId());
            userDAO.addUser(newUser);

        }


    }

    /**
     * Создает и отправляет сообщение, которое содержит только текст
     *
     * @param chatId     Идентификатор чата
     * @param textToSend Текст сообщения
     * @throws TelegramApiException Если возникла ошибка при отправке сообщения
     */
    public void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Перегрузка метода отправки сообщений, создает и отправляет сообщение с кнопками
     *
     * @param chatId     Идентификатор чата
     * @param textToSend Текст сообщения
     * @param buttons    Разметка с кнопками
     * @throws TelegramApiException Если возникла ошибка при отправке сообщения
     */
    public void sendMessage(long chatId, String textToSend, InlineKeyboardMarkup buttons) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        sendMessage.setReplyMarkup(buttons);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Изменяет текстовое сообщение по его идентификатору
     *
     * @param chatId        Идентификатор чата
     * @param messageId     Идентификатор сообщения
     * @param textToReplace Текст для замены
     */
    private void editMessage(long chatId, long messageId, String textToReplace) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToReplace);
        message.setMessageId((int) messageId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Изменяет сообщение с кнопками по его идентификатору
     *
     * @param chatId        Идентификатор чата
     * @param messageId     Идентификатор сообщения
     * @param textToReplace Текст для замены
     * @param buttons       Разметка с кнопками
     */
    private void editMessage(long chatId, long messageId, String textToReplace, InlineKeyboardMarkup buttons) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToReplace);
        message.setReplyMarkup(buttons);
        message.setMessageId((int) messageId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Создает кнопки к сообщениям.
     * Результат метода необходимо указать в setReplyMarkup сообщения.
     *
     * @param maxButtonsInLine Максимальное количество кнопок в одной строке
     * @param buttons          Массив кнопок
     * @return Разметка с кнопками
     */
    private InlineKeyboardMarkup createButtons(int maxButtonsInLine, ArrayList<Button> buttons) {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();

        //создаем коллекцию коллекций для markupInLine

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        //высчитываем количество нужных строк

        int linesCount;
        if (buttons.size() % maxButtonsInLine == 0) {
            linesCount = (buttons.size() / maxButtonsInLine);
        } else {
            linesCount = (buttons.size() / maxButtonsInLine) + 1;
        }

        //заполняем коллекцию необходимым количеством коллекций. Их количесвто соответсвует количеству строк

        for (int i = 0; i < linesCount; i++) {
            rowsInLine.add(new ArrayList<>());
        }

        //заполняем каждую коллекцию данными из массива
        Iterator<Button> iterator = buttons.iterator();
        for (int i = 0; i < linesCount; i++) {
            for (int j = 0; j < maxButtonsInLine; j++) {
                if (iterator.hasNext()) {
                    var button = new InlineKeyboardButton();
                    Button newButton = iterator.next();
                    button.setText(newButton.getText());
                    button.setCallbackData(newButton.getCallbackData());
                    rowsInLine.get(i).add(j, button);
                } else break;
            }
        }
        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }
}