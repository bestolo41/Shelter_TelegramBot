package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.TelegramBotConfig.TelegramBotConfig;
import com.skypro.shelter_telegrambot.model.Button;
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

    final TelegramBotConfig telegramBotConfig;
    final UserDAO userDAO;
    final InfoService infoService;

    public TelegramBot(TelegramBotConfig telegramBotConfig, UserDAO userDAO, InfoService infoService) {
        this.telegramBotConfig = telegramBotConfig;
        this.userDAO = userDAO;
        this.infoService = infoService;
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


            switch (messageText) {
                case "/start":
                    try {
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName(), update);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    try {
                        sendMessage(chatId, "Пока все");
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
            }

        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callbackData) {
                case "CAT_SHELTER":
                    editMessage(chatId, messageId, "Выберите действие для приюта для кошек:", createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Узнать информацию о приюте", "INFO_CAT"),
                            new Button("Как взять кошку из приюта", "HOW_CAT"),
                            new Button("Прислать отчет о питомце", "REPORT_CAT"),
                            new Button("Позвать волонтера", "VOLUNTEER_CAT")))));
                    break;
                case "DOG_SHELTER":
                    editMessage(chatId, messageId, "Выберите действие для приюта для собак:", createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Узнать информацию о приюте", "INFO_DOG"),
                            new Button("Как взять собаку из приюта", "HOW_DOG"),
                            new Button("Прислать отчет о питомце", "REPORT_DOG"),
                            new Button("Позвать волонтера", "VOLUNTEER_DOG")))));
                    break;
                case "INFO_CAT":
                    editMessage(chatId, messageId, "Какую информацию хотите узнать о приюте для кошек?", createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Общая информация о приюте", "GEN_INFO_CAT"),
                            new Button("График, адрес и схема проезда", "TIME_ADDRESS_DIRECTION_CAT"),
                            new Button("Контакты охраны", "SECURITY_CAT"),
                            new Button("Техника безопасности", "SAFETY_CAT"),
                            new Button("Оставить контакты", "SET_CONTACT_CAT"),
                            new Button("Позвать волонтера", "VOLUNTEER_CAT")))));
                    break;
                case "INFO_DOG":
                    editMessage(chatId, messageId, "Какую информацию хотите узнать о приюте для собак?", createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Общая информация о приюте", "GEN_INFO_DOG"),
                            new Button("График, адрес и схема проезда", "TIME_ADDRESS_DIRECTION_DOG"),
                            new Button("Контакты охраны", "SECURITY_DOG"),
                            new Button("Техника безопасности", "SAFETY_DOG"),
                            new Button("Оставить контакты", "SET_CONTACT_DOG"),
                            new Button("Позвать волонтера", "VOLUNTEER_DOG")))));
                    break;
                case "HOW_CAT":
                    editMessage(chatId, messageId, "Информация для получения кошки из приюта:", createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Правила знакомства", "DATING_RULES_CAT"),
                            new Button("Необходимые документы", "DOCS"),
                            new Button("Рекомендации по транспортировке кошки", "TRANSPORTING_CAT"),
                            new Button("Рекомендации по обустройству дома для котенка", "HOME_LITTLE_CAT"),
                            new Button("Рекомендации по обустройству дома для взрослого кота", "HOME_ADULT_CAT"),
                            new Button("Рекомендации по обустройству дома для кота с ограниченными возможностями", "HOME_INVALID_CAT"),
                            new Button("Причины для отказа", "REASONS_CAT"),
                            new Button("Оставить контакты", "SET_CONTACT_CAT"),
                            new Button("Позвать волонтера", "VOLUNTEER_CAT")))));
                    break;
                case "HOW_DOG":
                    editMessage(chatId, messageId, "Информация для получения собаки из приюта:", createButtons(1, new ArrayList<>(Arrays.asList(
                            new Button("Правила знакомства", "DATING_RULES_DOG"),
                            new Button("Необходимые документы", "DOCS"),
                            new Button("Рекомендации по транспортировке собаки", "TRANSPORTING_DOG"),
                            new Button("Рекомендации по обустройству дома для щенка", "HOME_LITTLE_DOG"),
                            new Button("Рекомендации по обустройству дома для взрослой собаки", "HOME_ADULT_DOG"),
                            new Button("Рекомендации по обустройству дома для собаки с ограниченными возможностями", "HOME_INVALID_DOG"),
                            new Button("Причины для отказа", "REASONS_DOG"),
                            new Button("Оставить контакты", "SET_CONTACT_DOG"),
                            new Button("Позвать волонтера", "VOLUNTEER_DOG")))));
                    break;

                case "DATING_RULES_CAT":

                case "DATING_RULES_DOG":
                    editMessage(chatId, messageId, infoService.getDatingRules(callbackData));

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

        if (checkUser(update.getMessage().getFrom().getId())) {
            sendMessage(chatId, greeting);
            sendMessage(chatId, question, catOrDogShelterButtons);
        } else {
            sendMessage(chatId, greeting);
            sendMessage(chatId, infoAboutBot);
            sendMessage(chatId, question, catOrDogShelterButtons);

            User newUser = new User();
            newUser.setId(update.getMessage().getFrom().getId());
            newUser.setFullName(update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName());
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

    /**
     * Проверяет пользовался ли ботом этот пользователь или нет
     *
     * @param userId
     * @return
     */
    private boolean checkUser(long userId) {
        User u = new User();
        u.setId(userId);
        ArrayList<User> users = (ArrayList<User>) userDAO.getAllUsers();

        return users.contains(u);
    }

}
