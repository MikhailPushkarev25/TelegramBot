package com.example.telegram;

import com.example.api.DictionaryAdditionService;
import com.example.api.DictionaryExelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MessageHandler {
    DictionaryAdditionService dictionaryAdditionService;
    DictionaryExelService dictionaryExelService;

    TelegramApiClient telegramApiClient;
    ReplayKeyBoardMaker replayKeyBoardMaker;
    InlineKeyboardMaker inlineKeyboardMaker;
}
