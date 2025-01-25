package com.liubomur.constants;

public final class Constants {

    private static final String BOT_TOKEN_ENV_VARIABLE = "BOT_TOKEN";
    private static final String OPEN_AI_TOKEN_ENV_VARIABLE = "OPEN_AI_API_TOKEN";
    public static final String BOT_TOKEN = System.getenv(BOT_TOKEN_ENV_VARIABLE);
    public static final String OPEN_AI_TOKEN = System.getenv(OPEN_AI_TOKEN_ENV_VARIABLE);
    public static final String OPEN_AI_WHISPER_URL = "https://api.openai.com/v1/audio/transcriptions";

}
