package com.skypro.shelter_telegrambot.service;

import com.skypro.shelter_telegrambot.model.Button;
import com.skypro.shelter_telegrambot.model.User;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class MessageService {
    private final TelegramBot telegramBot;
    private final UserService userService;
    private final UserDAO userDAO;

    public MessageService(TelegramBot telegramBot, UserService userService, UserDAO userDAO) {
        this.telegramBot = telegramBot;
        this.userService = userService;
        this.userDAO = userDAO;
    }

    /**
     * Создает объект DeleteMessage для удаления сообщения из чата.
     *
     * @param chatId    идентификатор чата, в котором нужно удалить сообщение.
     * @param messageId идентификатор сообщения, которое нужно удалить.
     * @return объект DeleteMessage с указанными параметрами chatId и messageId.
     */
    public void deleteMessage(long chatId, long messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId((int) messageId);
        try {
            telegramBot.execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Отправляет сообщение-приветствие и начальное меню после апдейта /start
     *
     * @param chatId Идентификатор чата
     * @param name   Имя пользователя
     * @throws TelegramApiException Если возникла ошибка при отправке сообщения
     */
    public void startCommandReceived(long chatId, String name, Update update) {

        //проверяем пользовался ли юзер ботом ранее, елм да, то информационное сообщение не отправляем

        if (userService.checkUser(update.getMessage().getFrom().getId())) {
            sendMessage(chatId, """ 
                Привет, """ + name + """          
                ! \nВыбери какой приют тебя интересует:
                """, createButtons(2, new ArrayList<>(Arrays.asList(
                    new Button("Приют для кошек", "CAT_SHELTER"),
                    new Button("Приют для собак", "DOG_SHELTER")))));
        } else {
            sendMessage(chatId, """ 
                Привет, """ + name + """
                !
                Я могу рассказать тебе, что нужно знать и уметь для того, чтобы забрать животное из приюта.               
                Также я могу предоставить всю информацию о всех доступных приютах города.                                
                А еще ты можешь отправлять мне ежедневные отчеты о том, как животное приспосабливается к новой обстановке.
                
                Выбери какой приют тебя интересует:
                """, createButtons(2, new ArrayList<>(Arrays.asList(
                    new Button("Приют для кошек", "CAT_SHELTER"),
                    new Button("Приют для собак", "DOG_SHELTER")))));

            //если не пользовался, сохраняем его в базу

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
            telegramBot.execute(sendMessage);
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
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Изменяет текстовое сообщение по его идентификатору.
     * По факту удаляет предыдущее сообщение и вместо него присылает новое.
     * Сделано, чтобы предыдущие сообщения от бота не мозолили глаза и не было соблазна нажимать предыдущие кнопки
     *
     * @param chatId        Идентификатор чата
     * @param messageId     Идентификатор сообщения
     * @param textToReplace Текст для замены
     */
    public void editMessage(long chatId, long messageId, String textToReplace) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToReplace);
        message.setMessageId((int) messageId);
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Изменяет сообщение с кнопками по его идентификатору
     * Перегрузка предыдущего метода
     *
     * @param chatId        Идентификатор чата
     * @param messageId     Идентификатор сообщения
     * @param textToReplace Текст для замены
     * @param buttons       Разметка с кнопками
     */
    public void editMessage(long chatId, long messageId, String textToReplace, InlineKeyboardMarkup buttons) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToReplace);
        message.setReplyMarkup(buttons);
        message.setMessageId((int) messageId);
        try {
            telegramBot.execute(message);
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
    public InlineKeyboardMarkup createButtons(int maxButtonsInLine, ArrayList<Button> buttons) {
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
