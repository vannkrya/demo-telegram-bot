package com.example.demotelegrambot.repository;

import com.example.demotelegrambot.model.Telegram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramRepository extends JpaRepository<Telegram, Long> {
    Object findByChatId(Long chatId);
}
