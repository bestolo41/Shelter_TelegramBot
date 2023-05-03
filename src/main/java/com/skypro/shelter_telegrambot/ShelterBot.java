package com.skypro.shelter_telegrambot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class ShelterBot extends TelegramLongPollingBot {
    private final String botToken = "6226311250:AAFKTTFvV0JHHkZlbyNgG-7-s1nN3kvKpOI";
    private static final String CAT_SHELTER = "Кошки";
    private static final String DOG_SHELTER = "Собаки";

    // Хранение данных пользователя (chatId -> тип приюта)
    private Map<Long, String> userData = new HashMap<>();
    // Хранение информации о приютах (тип приюта -> информация о приюте)
    private Map<String, ShelterInfo> shelters = new HashMap<>();

    public ShelterBot() {
        initShelters();
    }
    // Инициализация информации о приютах
    private void initShelters() {
        ShelterInfo catShelter = new ShelterInfo("CatsShelter", "Moscow", "08:00-20:00", "No Smoling", "Security");
        ShelterInfo dogShelter = new ShelterInfo("DogsShelter", "Тула", "08:00-20:00", "No Smoling", "Security");
        shelters.put(CAT_SHELTER, catShelter);
        shelters.put(DOG_SHELTER, dogShelter);
    }

    // Отображение главного меню
    private void showMainMenu(SendMessage message, String shelterType) {
        String mainMenuText = String.format("Выбран приют для %s. Воспользуйтесь меню:\n\n" +
                "1. Информация о приюте\n" +
                "2. Как взять животное из приюта\n" +
                "3. Прислать отчет о питомце\n" +
                "4. Стать волонтером\n" +
                "0. Вернуться назад", shelterType);
        message.setText(mainMenuText);
    }
    private void showShelterInfoMenu(SendMessage message, String shelterType) {
        String shelterInfoMenuText = String.format("Информация о приюте для %s:\n\n" +
                "1.1. Адрес и схема проезда\n" +
                "1.2. Расписание работы\n" +
                "1.3. Правила пропуска и безопасности\n" +
                "1.4. Контакты охраны\n" +
                "0. Вернуться назад", shelterType);
        message.setText(shelterInfoMenuText);
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
                default:
                    String shelterType = userData.get(chatId);

                    if (shelterType == null) {
                        handleMenuCommand(message, messageText, chatId);
                    } else {
                        handleShelterInfoCommand(message, messageText, chatId);
                    }
            }

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
    private void handleMenuCommand(SendMessage message, String messageText, long chatId) {
        switch (messageText) {
            case "1":
            case "Приют для кошек":
                userData.put(chatId, CAT_SHELTER);
                showMainMenu(message, CAT_SHELTER);
                break;
            case "2":
            case "Приют для собак":
                userData.put(chatId, DOG_SHELTER);
                showMainMenu(message, DOG_SHELTER);
                break;
            default:
                showUnknownCommand(message);
                break;
        }
    }

    private void handleStart(SendMessage message) {
        String welcomeText = "Привет! Я бот-помощник приютов для животных. Пожалуйста, выберите приют:\n\n" +

                "1. Приют для кошек\n" +
                "2. Приют для собак";
        message.setText(welcomeText);
    }
    private void handleShelterInfoCommand(SendMessage message, String messageText, long chatId) {
        String shelterType = userData.get(chatId);
        switch (messageText) {
            case "1":
                showShelterInfoMenu(message, shelterType);
                break;
            case "1.1":
                showShelterLocation(message, shelterType);
                break;
            case "1.2":
                showShelterWorkingHours(message, shelterType);
                break;
            case "1.3":
                showShelterRules(message, shelterType);
                break;
            case "1.4":
                showShelterSecurityContacts(message, shelterType);
                break;
            case "2":
                showAdoptionInfo(message, shelterType);
                break;
            case "3":
                requestPetReport(message, shelterType);
                break;
            case "4":
                callVolunteer(message, shelterType);
                break;
            case "0":
                handleStart(message);
                userData.remove(chatId);
                break;
            default:
                showUnknownCommand(message);
                break;
        }
    }
    // Отображение информации о выбранном приюте
    private void showShelterLocation(SendMessage message, String shelterType) {
        ShelterInfo shelterInfo = shelters.get(shelterType);
        String locationText = String.format("Адрес приюта для %s:\n\n%s", shelterType, shelterInfo.getLocation());
        message.setText(locationText);
    }
    private void showShelterWorkingHours(SendMessage message, String shelterType) {
        ShelterInfo shelterInfo = shelters.get(shelterType);
        String workingHoursText = String.format("Рабочие часы приюта для %s:\n\n%s", shelterType, shelterInfo.getWorkingHours());
        message.setText(workingHoursText);
    }
    private void showShelterRules(SendMessage message, String shelterType) {
        ShelterInfo shelterInfo = shelters.get(shelterType);
        String rulesText = String.format("Правила пропуска и безопасности приюта для %s:\n\n%s", shelterType, shelterInfo.getRules());
        message.setText(rulesText);
    }
    private void showShelterSecurityContacts(SendMessage message, String shelterType) {
        ShelterInfo shelterInfo = shelters.get(shelterType);
        String securityContactsText = String.format("Контакты охраны приюта для %s:\n\n%s", shelterType, shelterInfo.getSecurityContacts());
        message.setText(securityContactsText);
    }
    private void showAdoptionInfo(SendMessage message, String shelterType) {
        String adoptionInfoText = String.format("Информация о том, как взять животное из приюта для %s:\n\n" +
                "1. Приходите в приют в рабочее время.\n" +
                "2. Ознакомьтесь с условиями содержания и ухода за животными.\n" +
                "3. Выберите животное, которое хотите забрать.\n" +
                "4. Оформите документы на усыновление животного.\n" +
                "5. Заберите животное и заботьтесь о нем.", shelterType);
        message.setText(adoptionInfoText);
    }
    private void requestPetReport(SendMessage message, String shelterType) {
        String reportRequestText = String.format("Чтобы прислать отчет о питомце из приюта для %s, отправьте фотографии и информацию о питомце на почту приюта: %s@example.com.", shelterType, shelterType.toLowerCase());
        message.setText(reportRequestText);
    }
    private void callVolunteer(SendMessage message, String shelterType) {
        String callVolunteerText = String.format("Если вы хотите стать волонтером в приюте для %s, пожалуйста, свяжитесь с координатором волонтеров по телефону +7(123)456-78-90.", shelterType);
        message.setText(callVolunteerText);
    }
    private void showUnknownCommand(SendMessage message) {
        String unknownCommandText = "Извините, я не понимаю эту команду. Пожалуйста, воспользуйтесь предоставленным меню.";
        message.setText(unknownCommandText);
    }
    @Override
    public String getBotUsername() {
        return "ShelterBot";
    }
    @Override
    public String getBotToken() {
        return botToken;
    }
}
