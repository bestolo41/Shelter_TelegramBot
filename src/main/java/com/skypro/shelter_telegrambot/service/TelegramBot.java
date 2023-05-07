package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.TelegramBotConfig.TelegramBotConfig;
import com.skypro.shelter_telegrambot.model.Button;
import com.skypro.shelter_telegrambot.model.CatShelterUser;
import com.skypro.shelter_telegrambot.model.DogShelterUser;
import com.skypro.shelter_telegrambot.model.User;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    static public long callbackQueryMessageId;
    static public long fullNameForCatShelterRequestId = 1;
    static public long ageForCatShelterRequestId;
    static public long phoneNumberForCatShelterRequestId;
    static public long addressForCatShelterRequestId;
    static public long fullNameForDogShelterRequestId = 1;
    static public long ageForDogShelterRequestId;
    static public long phoneNumberForDogShelterRequestId;
    static public long addressForDogShelterRequestId;
    static public  CatShelterUser catUser = new CatShelterUser();
    static public  DogShelterUser dogUser = new DogShelterUser();
    private final TelegramBotConfig telegramBotConfig;
    private final UserDAO userDAO;
    private final InfoService infoService;
    private final CatShelterService catShelterService;
    private final DogShelterService dogShelterService;
    private final UserService userService;
    private final MessageService messageService;

    public TelegramBot(TelegramBotConfig telegramBotConfig, UserDAO userDAO, InfoService infoService, CatShelterService catShelterService, DogShelterService dogShelterService, UserService userService, MessageService messageService) {
        this.telegramBotConfig = telegramBotConfig;
        this.userDAO = userDAO;
        this.infoService = infoService;
        this.catShelterService = catShelterService;
        this.dogShelterService = dogShelterService;
        this.userService = userService;
        this.messageService = messageService;
    }


    @Override
    public String getBotUsername() {
        return telegramBotConfig.getName();
    }

    @Override
    public String getBotToken() {
        return telegramBotConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            long messageId = update.getMessage().getMessageId();
            if ("/call_volunteer".equals(messageText)) { // Замените на нужную команду
                catShelterService.requestVolunteer(chatId);
            }


            switch (messageText) {
                case "/start":
                    try {
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName(), update);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    if (fullNameForCatShelterRequestId == callbackQueryMessageId) {
                        catUser.setFullName(update.getMessage().getText());
                        try {
                            sendMessage(chatId, "Имя и фамилия сохранены");
                            sendMessage(chatId, "Введите возраст:");
                            ageForCatShelterRequestId = messageId + 3;
                            callbackQueryMessageId = 0;
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (ageForCatShelterRequestId == messageId) {
                        catUser.setAge(Integer.parseInt(update.getMessage().getText()));
                        try {
                            sendMessage(chatId, "Возраст сохранен");
                            sendMessage(chatId, "Введите номер телефона:");
                            phoneNumberForCatShelterRequestId = messageId + 3;
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (phoneNumberForCatShelterRequestId == messageId) {
                        catUser.setPhoneNumber(update.getMessage().getText());
                        try {
                            sendMessage(chatId, "Номер телефона сохранен");
                            sendMessage(chatId, "Введите адрес проживания:");
                            addressForCatShelterRequestId = messageId + 3;
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (addressForCatShelterRequestId == messageId) {
                        catUser.setAddress(update.getMessage().getText());
                        catUser.setId(update.getMessage().getFrom().getId());
                        userDAO.addCatUser(catUser);

                        try {
                            sendMessage(chatId, "КОНТАКТ СОХРАНЕН", createButtons(1, new ArrayList<>(Arrays.asList(
                                    new Button("Назад", "BACK_INFO_CAT")))));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (fullNameForDogShelterRequestId == callbackQueryMessageId) {
                            dogUser.setFullName(update.getMessage().getText());
                            try {
                                sendMessage(chatId, "Имя и фамилия сохранены");
                                sendMessage(chatId, "Введите возраст:");
                                ageForDogShelterRequestId = messageId + 3;
                                callbackQueryMessageId = 0;
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        } else if (ageForDogShelterRequestId == messageId) {
                            dogUser.setAge(Integer.parseInt(update.getMessage().getText()));
                            try {
                                sendMessage(chatId, "Возраст сохранен");
                                sendMessage(chatId, "Введите номер телефона:");
                                phoneNumberForDogShelterRequestId = messageId + 3;
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        } else if (phoneNumberForDogShelterRequestId == messageId) {
                            dogUser.setPhoneNumber(update.getMessage().getText());
                            try {
                                sendMessage(chatId, "Номер телефона сохранен");
                                sendMessage(chatId, "Введите адрес проживания:");
                                addressForDogShelterRequestId = messageId + 3;
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        } else if (addressForDogShelterRequestId == messageId) {
                            dogUser.setAddress(update.getMessage().getText());
                            dogUser.setId(update.getMessage().getFrom().getId());
                            userDAO.addDogUser(dogUser);

                            try {
                                sendMessage(chatId, "КОНТАКТ СОХРАНЕН", createButtons(1, new ArrayList<>(Arrays.asList(
                                        new Button("Назад", "BACK_INFO_DOG")))));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                    } else {
                        try {
                            sendMessage(chatId, "ничего не понял");
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
            }

        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            callbackQueryMessageId = update.getCallbackQuery().getMessage().getMessageId();
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
                case "SET_CONTACT_DOG":
                    if (userService.checkDogUser(update.getCallbackQuery().getFrom().getId())) {
                        editMessage(chatId, callbackQueryMessageId, "Контакт уже сохранен", createButtons(1, new ArrayList<>(Arrays.asList(
                                new Button("Назад", "BACK_INFO_DOG")))));
                        break;
                    }
                    try {
                        execute(messageService.deleteMessage(chatId, callbackQueryMessageId));
                        execute(messageService.sendFullNameRequestMessage(chatId, callbackData));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "VOLUNTEER_CAT":
                    editMessage(chatId, callbackQueryMessageId, "!!!В РАЗРАБОТКЕ!!!", createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Назад", "BACK_INFO_CAT")))));
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
                    try {
                        sendMessage(chatId, "Извини, я не понял");
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }
        }
    }


    /**
     * Отправляет сообщение-приветствие и начальное меню после апдейта /start
     *
     * @param chatId
     * @param name
     * @throws TelegramApiException
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
     * @param chatId
     * @param textToSend
     * @throws TelegramApiException
     */
    private void sendMessage(long chatId, String textToSend) throws TelegramApiException {
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
     * @param chatId
     * @param textToSend
     * @param buttons
     * @throws TelegramApiException
     */
    private void sendMessage(long chatId, String textToSend, InlineKeyboardMarkup buttons) throws TelegramApiException {
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
     * Изменяет текстовое сообщение по его id
     *
     * @param chatId
     * @param textToReplace
     * @return
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
     * Изменяет сообщение с кнопками по его id
     *
     * @param chatId
     * @param textToReplace
     * @param buttons
     * @return
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
     * Создает кнопки к сообщениям, сам устанавливает их количесвто
     * Результат метода необходимо указать в setReplyMarkup сообщения
     *
     * @param maxButtonsInLine - Необходимое количесвто кнопок в одной строке
     * @param buttons          - Массив объектов Button, которые представляют собой создаваемые кнопки и собержат в себе название кнопки и его callbackData
     * @return - InlineKeyboardMarkup
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


    //Сохраняем пользователя в базе
    public void saveUserInformation(String userData, long chatId) throws TelegramApiException {
        // Разделяем полученные данные на части
        String[] parts = userData.split(",\\s*");
        if (parts.length != 4) {
            // Отправьте сообщение об ошибке
            sendMessage(chatId, "Неверный формат данных. Пожалуйста, введите данные в формате: Фамилия Имя,Возраст, Адрес, Телефон");
            return;
        }
        // Создаем объект User с полученными данными
        User user = new User();

        // Сохраняем данные пользователя в базе данных
        UserRepository.saveUser(user);

        // Отправляем сообщение с подтверждением
        sendMessage(chatId, "Ваши данные успешно сохранены.");
    }

    public void setCallbackQueryMessageId(long callbackQueryMessageId) {
        this.callbackQueryMessageId = callbackQueryMessageId;
    }

    public void setFullNameForCatShelterRequestId(long fullNameForCatShelterRequestId) {
        this.fullNameForCatShelterRequestId = fullNameForCatShelterRequestId;
    }

    public void setAgeForCatShelterRequestId(long ageForCatShelterRequestId) {
        this.ageForCatShelterRequestId = ageForCatShelterRequestId;
    }

    public void setPhoneNumberForCatShelterRequestId(long phoneNumberForCatShelterRequestId) {
        this.phoneNumberForCatShelterRequestId = phoneNumberForCatShelterRequestId;
    }

    public void setAddressForCatShelterRequestId(long addressForCatShelterRequestId) {
        this.addressForCatShelterRequestId = addressForCatShelterRequestId;
    }

    public void setFullNameForDogShelterRequestId(long fullNameForDogShelterRequestId) {
        this.fullNameForDogShelterRequestId = fullNameForDogShelterRequestId;
    }

    public void setAgeForDogShelterRequestId(long ageForDogShelterRequestId) {
        this.ageForDogShelterRequestId = ageForDogShelterRequestId;
    }

    public void setPhoneNumberForDogShelterRequestId(long phoneNumberForDogShelterRequestId) {
        this.phoneNumberForDogShelterRequestId = phoneNumberForDogShelterRequestId;
    }

    public void setAddressForDogShelterRequestId(long addressForDogShelterRequestId) {
        this.addressForDogShelterRequestId = addressForDogShelterRequestId;
    }

    public long getCallbackQueryMessageId() {
        return callbackQueryMessageId;
    }

    public long getFullNameForCatShelterRequestId() {
        return fullNameForCatShelterRequestId;
    }

    public long getAgeForCatShelterRequestId() {
        return ageForCatShelterRequestId;
    }

    public long getPhoneNumberForCatShelterRequestId() {
        return phoneNumberForCatShelterRequestId;
    }

    public long getAddressForCatShelterRequestId() {
        return addressForCatShelterRequestId;
    }

    public long getFullNameForDogShelterRequestId() {
        return fullNameForDogShelterRequestId;
    }

    public long getAgeForDogShelterRequestId() {
        return ageForDogShelterRequestId;
    }

    public long getPhoneNumberForDogShelterRequestId() {
        return phoneNumberForDogShelterRequestId;
    }

    public long getAddressForDogShelterRequestId() {
        return addressForDogShelterRequestId;
    }
}
