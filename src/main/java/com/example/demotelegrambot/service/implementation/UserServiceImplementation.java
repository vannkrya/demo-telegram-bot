package com.example.demotelegrambot.service.implementation;

import com.example.demotelegrambot.model.User;
import com.example.demotelegrambot.repository.UserRepository;
import com.example.demotelegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList;
    }

    @Override
    public User getUserById(Long id) {
        // Replace this with your actual logic to fetch a user by their ID from a repository or database
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            return null; // Return null if the user is not found
        }
    }

    @Override
    public void saveUser(User user) {
        // Replace this with your actual logic to save the user to the repository or database
        userRepository.save(user);
    }
}
