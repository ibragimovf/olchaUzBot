package uz.pdp.bot.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class UserServiceBot {
    public static List<KeyboardRow> shareContact() {

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow keyboardButtons = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setRequestContact(true);
        keyboardButton.setText("Share contact");
        keyboardButtons.add(keyboardButton);
        keyboardRows.add(keyboardButtons);

        return keyboardRows;
    }
}
