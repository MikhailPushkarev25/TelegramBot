package com.example.telegram;

import com.example.constants.bot.CallBackDataPartsEnum;
import com.example.constants.resources.DictionaryResourcePathEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

// клавиатуры формируемые в ленте телеграм для получения телеграм
@Component
public class InlineKeyBoardMarker {

    public InlineKeyboardMarkup getInlineMessageButtonsWithTemplate(String prefix, boolean isUserDictionaryNeed) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineMessageButtons(prefix, isUserDictionaryNeed);
        inlineKeyboardMarkup.getKeyboard().add(getButton(
                "шаблон",
                prefix + CallBackDataPartsEnum.TASK_.name()
        ));
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getInlineMessageButtons(String prefix, boolean isUserDictionaryNeed) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (DictionaryResourcePathEnum dictionary : DictionaryResourcePathEnum.values()) {
            rowList.add(getButton(
                    dictionary.getButtonName(),
                    prefix + dictionary.name()
            ));
        }

        if (!rowList.isEmpty()) {
            rowList.add(getButton(
                   "Все классы",
                    prefix + CallBackDataPartsEnum.ALL_GRADES.name()
            ));
        }

        if (isUserDictionaryNeed) {
            rowList.add(getButton(
                    "Ваш словарь",
                    prefix + CallBackDataPartsEnum.USER_DICTIONARY.name()
            ));
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private List<InlineKeyboardButton> getButton(String buttonName, String buttonCallbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(buttonCallbackData);

        List<InlineKeyboardButton> keyBoardButtonsRow = new ArrayList<>();
        keyBoardButtonsRow.add(button);
        return keyBoardButtonsRow;
    }
}
