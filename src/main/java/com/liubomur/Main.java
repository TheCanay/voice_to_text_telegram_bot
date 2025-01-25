package com.liubomur;

import com.liubomur.bot.VoiceConvertingBot;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.liubomur.constants.Constants.BOT_TOKEN;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(BOT_TOKEN, new VoiceConvertingBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}