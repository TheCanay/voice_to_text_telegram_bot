package com.liubomur.service;

import com.google.gson.Gson;
import okhttp3.*;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import static com.liubomur.constants.Constants.OPEN_AI_TOKEN;
import static com.liubomur.constants.Constants.OPEN_AI_WHISPER_URL;

public class WhisperConnectionService {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final Logger logger = LoggerFactory.getLogger(WhisperConnectionService.class);
    private final Gson gson = new Gson();

    public String getAudioTranscription(InputStream audioStream) throws IOException {
        RequestBody audioRequestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse("audio/ogg");
            }

            @Override
            public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = audioStream.read(buffer)) != -1) {
                    bufferedSink.write(buffer, 0, bytesRead);
                }
            }
        };

        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "audio.wav", audioRequestBody)
                .addFormDataPart("model", "whisper-1")
                .addFormDataPart("language", "uk")
                .build();

        // Create the request
        Request request = new Request.Builder()
                .url(OPEN_AI_WHISPER_URL)
                .header("Authorization", "Bearer " + OPEN_AI_TOKEN)
                .post(requestBody)
                .build();

        // Send the request and get the response
        try (Response response = httpClient.newCall(request).execute()) {
            assert response.body() != null;

            String jsonResponse = response.body().string();

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code: " + jsonResponse);
            }
            logger.info(jsonResponse);
            return gson.fromJson(jsonResponse, WhisperResponse.class).getText();
        }
    }

    private static class WhisperResponse {
        private String text;

        private WhisperResponse() {

        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
