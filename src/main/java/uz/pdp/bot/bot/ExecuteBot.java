package uz.pdp.bot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class ExecuteBot {

    public static SendMessage execute(
            String chatId,
            String text,
            ReplyKeyboardMarkup replyKeyboardMarkup
    ) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText(text);
        return sendMessage;
    }

    public static SendMessage execute(
            Long chatId,
            String text,
            InlineKeyboardMarkup inlineKeyboardMarkup
    ) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText(text);
        return sendMessage;
    }

    public static EditMessageText execute(
            CallbackQuery callbackQuery,
            String text,
            InlineKeyboardMarkup inlineKeyboardMarkup
    ) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageText.setText(text);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        return editMessageText;
    }

    public static EditMessageCaption execute(
            InlineKeyboardMarkup inlineKeyboardMarkup,
            CallbackQuery callbackQuery,
            String caption
    ) {
        EditMessageCaption editMessageCaption = new EditMessageCaption();
        editMessageCaption.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageCaption.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageCaption.setCaption(caption);
        editMessageCaption.setReplyMarkup(inlineKeyboardMarkup);
        return editMessageCaption;
    }

    public static SendMessage execute(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        return sendMessage;
    }
}
