package com.example.demotelegrambot.service;

import com.example.demotelegrambot.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(Long id);

    void saveUser(User user);
}
