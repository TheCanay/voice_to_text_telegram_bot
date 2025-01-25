# Hello there!

You're annoyed that someone is sending voice messages into the chat? You're not able to listen to them? Here's a solution! This bot will respond to someone's voice message with a text that was contained in there! Isn't it cool?

## P.S.

Most likely I'll add some more features later that would mean to be used only in my friends group, that will not be in a master branch. (Or maybe not ever will be pushed to remote). Major functionality updates - they're going to be here.

Have a nice day everyone!

## How to use?

1. Build your app with:  
```gradle clean shadowJar```
2. Build docker image!  
```docker build -t voice_to_text_telegram_bot_image .```
3. Run docker container with -e parameter for each of the mentioned below environment variables to set values. Command example:  
```docker run -d -e BOT_TOKEN=<value> -e OPEN_AI_API_TOKEN=<value> voice_to_text_telegram_bot_image```  

### Environment variables list:  
BOT_TOKEN - acquired in Telegram from @BotFather  
OPEN_AI_API_TOKEN - acquired in OpenAI personal cabinet
