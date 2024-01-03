package com.example.demotelegrambot.controller;

import com.example.demotelegrambot.model.User;
import com.example.demotelegrambot.service.TelegramBotService;
import com.example.demotelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TelegramBotService telegramBotService;

    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        return ResponseEntity.ok().body(userList);
    }

    @PutMapping("/change-firstname/{id}")
    public ResponseEntity<?> updateFirstName(@PathVariable Long id, @RequestParam String newFirstName) {
        try {
            User user = userService.getUserById(id);

            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            // Store the old first name for reference
            String oldFirstName = user.getFirstName();

            // Update the first name
            user.setFirstName(newFirstName);
            userService.saveUser(user);

               // Send a message to the user's Telegram chat
               try {
                   // Initialize your Telegram bot instance here
                   // TelegramBot bot = new TelegramBot(); // Replace with your bot initialization

                   // Create a message
                   SendMessage message = new SendMessage();

                   if(user.getTelegram().getChatId() != null) {
                       message.setChatId(user.getTelegram().getChatId());
                       message.setText("Your first name has been updated from " + oldFirstName + " to " + newFirstName);

                       // Send the message
                       telegramBotService.execute(message);
                   }

               } catch (TelegramApiException e) {
                   // Handle Telegram API exception
                   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending Telegram message");
               }

            return ResponseEntity.ok().body("First name updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating first name");
        }
    }


}
