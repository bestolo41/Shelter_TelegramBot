package com.skypro.shelter_telegrambot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class ShelterBot extends TelegramLongPollingBot {
    private final String botToken = "6226311250:AAFKTTFvV0JHHkZlbyNgG-7-s1nN3kvKpOI";

    // Хранение данных пользователя (chatId -> тип приюта)
    private Map<Long, String> userData = new HashMap<>();
    // Хранение информации о приютах (тип приюта -> информация о приюте)
    private Map<String, ShelterInfo> shelters = new HashMap<>();

    public ShelterBot() {
        initShelters();
    }

    // Инициализация информации о приютах
    private void initShelters() {
        ShelterInfo catShelter = new ShelterInfo("CatsShelter","Moscow","08:00-20:00","No Smoling","Security");
        ShelterInfo dogShelter = new ShelterInfo("DogsShelter","Moscow","08:00-20:00","No Smoling","Security");
        shelters.put("кошки", catShelter);
        shelters.put("собаки", dogShelter);
    }

    // Отображение главного меню
    private void showMainMenu(SendMessage message, String shelterType) {
        String mainMenuText = String.format("Выбран приют для %s. Воспользуйтесь меню:\n\n" +
                "1. Узнать информацию о приюте\n" +
                "2. Как взять животное из приюта\n" +
                "3. Прислать отчет о питомце\n" +
                "4. Позвать волонтера", shelterType);
        message.setText(mainMenuText);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            SendMessage message = new SendMessage();
            message.setChatId(chatId);


            // Обработка команд
            switch (messageText) {
                case "/start":
                    handleStart(message);
                    break;
                case "1":
                case "Приют для кошек":
                    userData.put(chatId, "кошки");
                    showMainMenu(message, "кошки");
                    break;
                case "2":
                case "Приют для собак":
                    userData.put(chatId, "собаки");
                    showMainMenu(message, "собаки");
                    break;
                default:
                    String shelterType = userData.get(chatId);

                    switch (messageText) {
                        case "1":
                            showShelterInfoMenu(message, shelterType);
                            break;
                        case "2":
                            showShelterInfo(message, shelterType);
                            break;
                        case "3":
                            showAdoptionInfo(message, shelterType);
                            break;
                        case "4":
                            requestPetReport(message, shelterType);
                            break;
                        case "5":
                            callVolunteer(message, shelterType);
                            break;
                        default:
                            showUnknownCommand(message);
                            break;
                    }
            }

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
    private void showAdoptionInfo(SendMessage message, String shelterType) {
        String adoptionInfo = String.format("Информация о том, как взять животное из приюта для %s: [Текст с информацией]", shelterType);
        message.setText(adoptionInfo);
    }

    private void requestPetReport(SendMessage message, String shelterType) {
        String reportRequest = String.format("Пожалуйста, отправьте отчет о питомце из приюта для %s. [Текст с инструкциями]", shelterType);
        message.setText(reportRequest);
    }

    private void callVolunteer(SendMessage message, String shelterType) {
        String volunteerCall = String.format("Вызов волонтера из приюта для %s. [Текст с информацией о вызове волонтера]", shelterType);
        message.setText(volunteerCall);
    }

    private void showUnknownCommand(SendMessage message) {
        String unknownCommandText = "Извините, я не понимаю эту команду. Пожалуйста, воспользуйтесь предоставленным меню.";
        message.setText(unknownCommandText);
    }


    // Обработка команды /start
    private void handleStart(SendMessage message) {
        String welcomeText = "Привет! Я бот-помощник приютов для животных. Пожалуйста, выберите приют:\n\n" +
                "1. Приют для кошек\n" +
                "2. Приют для собак";
        message.setText(welcomeText);
    }

    @Override
    public String getBotUsername() {
        return "ShelterBot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    // Отображение информации о выбранном приюте
    private void showShelterInfo(SendMessage message, String shelterType) {
        ShelterInfo shelterInfo = shelters.get(shelterType);
        String infoText = String.format("Информация о приюте для %s:\n\n" +
                        "Адрес: %s\n" +
                        "Рабочие часы: %s\n" +
                        "Правила: %s\n" +
                        "Контакты охраны: %s",
                shelterType, shelterInfo.getLocation(), shelterInfo.getWorkingHours(),
                shelterInfo.getRules(), shelterInfo.getSecurityContacts());
        message.setText(infoText);
    }
}
