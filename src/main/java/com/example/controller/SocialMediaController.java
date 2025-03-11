package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
 public class SocialMediaController {
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private MessageService messageService;


    /**
     * Handler for POST localhost:8080/register.
     */
    @PostMapping("register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        accountService.register(account);
        return ResponseEntity.ok()
                .body(account);
    }

    /**
     * Handler for POST localhost:8080/login
     */
    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        return ResponseEntity.ok()
                .body(accountService.login(account));
    }

    /**
     * Handler for POST localhost:8080/messages
     */
    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        messageService.createMessage(message);
        return ResponseEntity.ok()
                .body(message);
    }

    /**
     * Handler for GET localhost:8080/messages
     */
    @GetMapping("messages")
    public ResponseEntity<List<Message>> getMessages() {
        return ResponseEntity.ok()
                .body(messageService.getMessages());
    }

    /**
     * Handler for GET localhost:8080/messages/{message_id}
     */
    @GetMapping("messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int message_id) {
        Optional<Message> optionalMessage = messageService.getMessageById(message_id);

        if (optionalMessage.isPresent()) {
            return ResponseEntity.ok()
                    .body(optionalMessage.get());
        } else {
            return ResponseEntity.ok().build();
        }
    }

    /**
     * Handler for DELETE localhost:8080/messages{message_id}
     */
    @DeleteMapping("messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int message_id) {
        Integer messagesDeleted = messageService.deleteMessageById(message_id);

        if (messagesDeleted == 0) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.ok().body(messagesDeleted);
        }
    }

    /**
     * Handler for PATCH localhost:8080/messages/{message_id}
     */
    @PatchMapping("messages/{message_id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable int message_id, @RequestBody Message message) {
        return ResponseEntity.ok().body(
                messageService.updateMessage(message_id, message.getMessageText())
        );
    }
}
