package com.example.demotelegrambot.service;

import com.example.demotelegrambot.config.TelegramBotConfig;
import com.example.demotelegrambot.model.Telegram;
import com.example.demotelegrambot.repository.TelegramRepository;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


@Service
public class TelegramBotService extends TelegramLongPollingBot {

    private final TelegramBotConfig telegramBotConfig;
    private final TelegramRepository telegramRepository;


//    private final UserRepository userRepository;
//    private final AdsRepository adsRepository;

//    static final String HELP_TEXT = "this bot is created to demonstrate spring capabilities.\n\n" +
//            "you can execute commands from the main menu on the left or by typing a command:\n\n" +
//            "type /start to see welcome message\n" +
//            "type /mydata to see data stored about yourself\n" +
//            "type /help to see this message again\n";

    static final String YES_BUTTON = "YES_BUTTON";
    static final String NO_BUTTON = "NO_BUTTON";

    public TelegramBotService(TelegramBotConfig telegramBotConfig, TelegramRepository telegramRepository) throws TelegramApiException {
        this.telegramBotConfig = telegramBotConfig;
        this.telegramRepository = telegramRepository;

        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/start", "give your chat id to bot"));
//        botCommandList.add(new BotCommand("/mydata", "get your data stored"));
//        botCommandList.add(new BotCommand("/deletedata", "delete my data"));
//        botCommandList.add(new BotCommand("/help", "info how to use this bot"));
//        botCommandList.add(new BotCommand("/settings", "set your preferences"));

        this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return telegramBotConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

//            if(messageText.contains("/send")) {
//                var textToSend = EmojiParser.parseToUnicode(messageText.substring(messageText.indexOf(" ")));
////                var users = userRepository.findAll();
////                for (User user :
////                    users ) {
//                    prepareAndSendMessage(chatId, textToSend);
////                }
//            } else {
            switch (messageText) {
                case "/start" -> {
                    startCommandReceived(chatId, update.getMessage().getChat().getUserName());
                    registerUser(update.getMessage());
                }
//                    case "/help" -> prepareAndSendMessage(chatId, HELP_TEXT);
//                    case "/register" -> register(chatId);
                default -> prepareAndSendMessage(chatId, "Sorry, command was not recognized!");
//                }
            }
        }
//        } else if (update.hasCallbackQuery()) {
//            String callbackData = update.getCallbackQuery().getData();
//            Long messageId = Long.valueOf(update.getCallbackQuery().getMessage().getMessageId());
//            Long chatId = update.getCallbackQuery().getMessage().getChatId();
//
//            if(callbackData.equals(YES_BUTTON)) {
//                String text = "you pressed yes button";
//                executeEditMessageText(text, chatId, messageId);
//            } else if (callbackData.equals(NO_BUTTON)) {
//                String text = "you pressed no button";
//                executeEditMessageText(text, chatId, messageId);
//            }
//        }

    }

//    private void executeEditMessageText(String text, Long chatId, Long messageId) {
//        EditMessageText editMessageText = new EditMessageText();
//        editMessageText.setChatId(chatId);
//        editMessageText.setText(text);
//        editMessageText.setMessageId(Math.toIntExact(messageId));
//
//        try {
//            execute(editMessageText);
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private void registerUser(Message message) {
        Telegram telegram;
        telegram = new Telegram();

        Long chatId = message.getChatId();
//        var chat = message.getChat();

        telegram.setChatId(chatId);

        if(telegramRepository.findByChatId(chatId) == null) {
            telegramRepository.save(telegram);
        } else {
            prepareAndSendMessage(chatId, "your chat id was saved already!");
        }
    }

//    private void register(Long chatId) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(chatId);
//        sendMessage.setText("Do you really want to register?");
//
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
//
//        var yesButton = new InlineKeyboardButton();
//        yesButton.setText("yes");
//        yesButton.setCallbackData(YES_BUTTON);
//
//        var noButton = new InlineKeyboardButton();
//        noButton.setText("no");
//        noButton.setCallbackData(NO_BUTTON);
//
//        rowInLine.add(yesButton);
//        rowInLine.add(noButton);
//
//        rowsInline.add(rowInLine);
//
//        inlineKeyboardMarkup.setKeyboard(rowsInline);
//        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
//
//        try {
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private void startCommandReceived(Long chatId, String name) {
//        String answer = "Hi " + name + ", nice to meet you bro&sis!";
        String answer = EmojiParser.parseToUnicode("Hi, " + name + ", nice to meet you! " + ":blush:");

        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);

//        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
//        List<KeyboardRow> keyboardRowList = new ArrayList<>();
//        KeyboardRow keyboardRow = new KeyboardRow();
//
//        keyboardRow.add("weather");
//        keyboardRow.add("get random joke");
//
//        keyboardRowList.add(keyboardRow);
//
//        keyboardRow = new KeyboardRow();
//
//        keyboardRow.add("register");
//        keyboardRow.add("check my data");
//        keyboardRow.add("delete my data");
//
//        keyboardRowList.add(keyboardRow);
//
//        replyKeyboardMarkup.setKeyboard(keyboardRowList);
//
//        sendMessage.setReplyMarkup(replyKeyboardMarkup);


        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void prepareAndSendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

//    @Scheduled(cron = "0 * * * * *")
//    private void sendAds() {
//        var ads = adsRepository.findAll();
//        var users = userRepository.findAll();
//
//        for(Ads ad : ads) {
//            for(User user : users) {
//                prepareAndSendMessage(user.getChatId(), ad.getAd());
//            }
//        }
//    }

}
