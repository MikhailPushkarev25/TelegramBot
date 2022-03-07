package com.example.telegram;

import com.example.api.DictionaryAdditionService;
import com.example.api.DictionaryExelService;
import com.example.constants.bot.BotMessageEnum;
import com.example.constants.bot.ButtonNameEnum;
import com.example.constants.bot.CallBackDataPartsEnum;
import com.example.exceptions.DictionaryTooBigException;
import com.example.exceptions.TelegramFileNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MessageHandler {
    DictionaryAdditionService dictionaryAdditionService;
    DictionaryExelService dictionaryExelService;

    TelegramApiClient telegramApiClient;
    ReplyKeyBoardMarker replyKeyBoardMarker;
    InlineKeyBoardMarker inlineKeyBoardMarker;

    public BotApiMethod<?> answerMessage(Message message) {
        String chatId = message.getChatId().toString();

        if (message.hasDocument()) {
            return addUserDictionary(chatId, message.getDocument().getFileId());
        }

        String inputText = message.getText();

        if (inputText == null) {
            throw new IllegalArgumentException();
        } else if (inputText.equals("/start")) {
            return getStartMessage(chatId);
        } else if (inputText.equals(ButtonNameEnum.GET_TASKS_BUTTON.getButtonName())) {
            return getTaskMessage(chatId);
        } else if (inputText.equals(ButtonNameEnum.GET_DICTIONARY_BUTTON.getButtonName())) {
            return getDictionaryMessage(chatId);
        } else if (inputText.equals(ButtonNameEnum.UPLOAD_DICTIONARY_BUTTON.getButtonName())) {
            return new SendMessage(chatId, BotMessageEnum.UPLOAD_DICTIONARY_MESSAGE.getMessage());
        } else if (inputText.equals(ButtonNameEnum.HELP_BUTTON.getButtonName())) {
            SendMessage sendMessage = new SendMessage(chatId, BotMessageEnum.HELP_MESSAGE.getMessage());
            sendMessage.enableMarkdown(true);
            return sendMessage;
        } else {
            return new SendMessage(chatId, BotMessageEnum.NON_COMMAND_MESSAGE.getMessage());
        }
    }

    private SendMessage getStartMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, BotMessageEnum.HELP_MESSAGE.getMessage());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyBoardMarker.getMainMenuKeyBoard());
        return sendMessage;
    }

    private SendMessage getTaskMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, BotMessageEnum.CHOOSE_DICTIONARY_MESSAGE.getMessage());
        sendMessage.setReplyMarkup(inlineKeyBoardMarker.getInlineMessageButtons(
                CallBackDataPartsEnum.TASK_.name(),
                dictionaryExelService.isUserDictionaryExist(chatId)
        ));
        return sendMessage;
    }

    private SendMessage getDictionaryMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, BotMessageEnum.CHOOSE_DICTIONARY_MESSAGE.getMessage());
        sendMessage.setReplyMarkup(inlineKeyBoardMarker.getInlineMessageButtonsWithTemplate(
                CallBackDataPartsEnum.DICTIONARY_.name(),
                dictionaryExelService.isUserDictionaryExist(chatId)
        ));
        return sendMessage;
    }

    private SendMessage addUserDictionary(String chatId, String filedId) {
        try {
            dictionaryAdditionService.addUserDictionary(chatId, telegramApiClient.getDocumentFile(filedId));
            return new SendMessage(chatId, BotMessageEnum.SUCCESS_UPLOAD_MESSAGE.getMessage());
        } catch (TelegramFileNotFoundException e) {
            return new SendMessage(chatId, BotMessageEnum.EXCEPTION_TELEGRAM_API_MESSAGE.getMessage());
        } catch (DictionaryTooBigException e) {
            return new SendMessage(chatId, BotMessageEnum.EXCEPTION_TOO_LARGE_DICTIONARY_MESSAGE.getMessage());
        } catch (Exception e) {
            return new SendMessage(chatId, BotMessageEnum.EXCEPTION_BAD_FILE_MESSAGE.getMessage());
        }
    }
}
