package com.liubomur.bot;

import com.google.gson.Gson;
import com.liubomur.service.WhisperConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import static com.liubomur.constants.Constants.BOT_TOKEN;


public class VoiceConvertingBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient = new OkHttpTelegramClient(BOT_TOKEN);
    private final Gson gson = new Gson();
    private final Logger logger = LoggerFactory.getLogger(VoiceConvertingBot.class);
    private final WhisperConnectionService whisperConnectionService = new WhisperConnectionService();

    @Override
    public void consume(Update update) {
        logger.info("consume: " + gson.toJson(update));
        if (update.hasMessage() && update.getMessage().hasVoice()) {
            Message message = update.getMessage();
            String text = voiceMessageToTextConversion(update);
            executeMethod(SendMessage.builder()
                    .chatId(message.getChatId())
                    .replyToMessageId(message.getMessageId())
                    .text(text)
                    .build());
        }
    }

    private String  voiceMessageToTextConversion(Update update) {
        try {
            return whisperConnectionService.getAudioTranscription(downloadVoiceMessageAudio(update.getMessage().getVoice()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream downloadVoiceMessageAudio(Voice voiceMessage) {
        logger.info("downloadVoiceMessageAudio: "+gson.toJson(voiceMessage));
        File voiceMessageAudio = executeMethod(new GetFile(voiceMessage.getFileId()));
        return downloadFile(voiceMessageAudio);
    }

    private <T extends Serializable, Method extends BotApiMethod<T>> T executeMethod(Method botApiMethod) {
        try {
            return telegramClient.execute(botApiMethod);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream downloadFile(File file) {
        try {
            return telegramClient.downloadFileAsStream(file);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
