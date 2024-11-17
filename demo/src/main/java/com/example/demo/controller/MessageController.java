package com.example.demo.controller;

import com.example.demo.dto.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final List<Message> messages = new ArrayList<>();
    private final AtomicInteger nextMessageId = new AtomicInteger(1);

    @GetMapping
    public List<Message> getAllMessages() {
        return messages;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int id) {
        return messages.stream()
                .filter(message -> message.getId() == id)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        message.setId(nextMessageId.getAndIncrement());
        message.setTime(LocalDateTime.now()); 
        messages.add(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable int id, @RequestBody Message updatedMessage) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId() == id) {
                updatedMessage.setId(id);
                updatedMessage.setTime(LocalDateTime.now());
                messages.set(i, updatedMessage);
                return ResponseEntity.ok(updatedMessage);
            }
        }
        return ResponseEntity.notFound().build();

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable int id) {
        if(messages.removeIf(message -> message.getId() == id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
