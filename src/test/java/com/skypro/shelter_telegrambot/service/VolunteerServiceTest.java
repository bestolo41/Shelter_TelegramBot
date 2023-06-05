package com.skypro.shelter_telegrambot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.mockito.Mockito.*;

public class VolunteerServiceTest {
    @Mock
    private TelegramBot bot;
    @Mock
    private MessageService messageService;

    private VolunteerService volunteerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        volunteerService = new VolunteerService(bot, messageService);
    }

    @Test
    public void testRequestVolunteer() throws TelegramApiException {
        long userChatId = 123456789L;

        // Set up the bot to return a chat with a username when getChat is called
        Chat chat = new Chat();
        chat.setUserName("username");
        when(bot.execute((SendDocument) any())).thenReturn(chat.getPinnedMessage());

        // Call the method under test
        volunteerService.requestVolunteer(userChatId);

        // Verify that the bot was asked to send a message
        verify(bot, times(1)).execute(any(SendMessage.class));
    }
}
