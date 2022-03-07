package com.example.telegram;

import com.example.api.DictionaryExelService;
import com.example.api.DictionaryResourceFileService;
import com.example.api.TasksService;
import com.example.constants.bot.BotMessageEnum;
import com.example.constants.bot.CallBackDataPartsEnum;
import com.example.constants.resources.DictionaryResourcePathEnum;
import com.example.exceptions.UserDictionaryNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.IOException;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CallbackQueryHandler {

    TelegramApiClient telegramApiClient;
    TasksService tasksService;
    DictionaryExelService dictionaryExelService;
    DictionaryResourceFileService dictionaryResourceFileService;

    // Обработка запроса приходящего от сервера
    public BotApiMethod<?> processCallBackQuery(CallbackQuery buttonQuery) throws IOException {
        final String chatId = buttonQuery.getMessage().getChatId().toString();

        String data = buttonQuery.getData();

        if (data.equals(CallBackDataPartsEnum.TASK_.name() + CallBackDataPartsEnum.USER_DICTIONARY.name())) {
            return getDictionaryTasks(chatId, chatId, "personal dictionary");
        } else if (data.equals(CallBackDataPartsEnum.TASK_.name() + CallBackDataPartsEnum.ALL_GRADES.name())) {
            return getAllDictionaryTasks(chatId);
        } else if (data.equals(CallBackDataPartsEnum.DICTIONARY_.name() + CallBackDataPartsEnum.USER_DICTIONARY.name())) {
            return getDictionary(chatId, chatId);
        } else if (data.equals(CallBackDataPartsEnum.DICTIONARY_.name() + CallBackDataPartsEnum.ALL_GRADES.name())) {
            return getAllDefaultDictionaries(chatId);
        } else if (data.equals(CallBackDataPartsEnum.DICTIONARY_.name() + CallBackDataPartsEnum.TEMPLATE.name())) {
            return getTemplate(chatId);
        } else {
            return handleDefaultDictionary(chatId, data);
        }
    }

    private SendMessage handleDefaultDictionary(String chatId, String data) throws IOException {
        if (data.startsWith(CallBackDataPartsEnum.TASK_.name())) {
            DictionaryResourcePathEnum resourcePathEnum = DictionaryResourcePathEnum.valueOf(
                    data.substring(CallBackDataPartsEnum.TASK_.name().length())
            );
            return getDictionaryTasks(chatId, resourcePathEnum.name(), resourcePathEnum.getFileName());
        } else if (data.startsWith(CallBackDataPartsEnum.DICTIONARY_.name())) {
            return getDictionary(chatId, data.substring(CallBackDataPartsEnum.DICTIONARY_.name().length()));
        } else {
            return new SendMessage(chatId, BotMessageEnum.EXCEPTION_BAD_BUTTON_NAME_MESSAGE.getMessage());
        }
    }

    private SendMessage getDictionaryTasks(String chatId, String dictionaryId, String fileName) throws IOException {
        try {
            telegramApiClient.uploadFile(chatId, tasksService.getTaskDocument(dictionaryId, fileName));
        } catch (Exception e) {
            return new SendMessage(chatId, BotMessageEnum.EXCEPTION_TASKS_WTF_MESSAGE.getMessage());
        }
        return null;
    }

    private SendMessage getAllDictionaryTasks(String chatId) throws IOException {
        try {
            telegramApiClient.uploadFile(chatId, tasksService.getAllDefaultDictionaryTaskDocument());
        } catch (Exception e) {
            return new SendMessage(chatId, BotMessageEnum.EXCEPTION_TASKS_WTF_MESSAGE.getMessage());
        }
        return null;
    }

    private SendMessage getDictionary(String chatId, String dictionaryId) throws IOException {
        try {
            telegramApiClient.uploadFile(chatId, dictionaryExelService.getDictionaryWorkbook(dictionaryId));
        } catch (UserDictionaryNotFoundException e) {
            return new SendMessage(chatId, BotMessageEnum.EXCEPTION_DICTIONARY_NOT_FOUND_MESSAGE.getMessage());
        } catch (Exception e) {
            return new SendMessage(chatId, BotMessageEnum.EXCEPTION_DICTIONARY_WTF_MESSAGE.getMessage());
        }
        return null;
    }

    private SendMessage getAllDefaultDictionaries(String chatId) throws IOException {
        try {
            telegramApiClient.uploadFile(chatId, dictionaryExelService.getAllDefaultDictionariesWorkbook());
        } catch (UserDictionaryNotFoundException e) {
            return new SendMessage(chatId, BotMessageEnum.EXCEPTION_DICTIONARY_NOT_FOUND_MESSAGE.getMessage());
        } catch (Exception e) {
            return new SendMessage(chatId, BotMessageEnum.EXCEPTION_DICTIONARY_WTF_MESSAGE.getMessage());
        }
        return null;
    }

    private SendMessage getTemplate(String chatId) throws IOException {
        try {
            telegramApiClient.uploadFile(chatId, dictionaryResourceFileService.getTemplateWorkbook());
        } catch (Exception e) {
            return new SendMessage(chatId, BotMessageEnum.EXCEPTION_TEMPLATE_WTF_MESSAGE.getMessage());
        }
        return null;
    }
 }
