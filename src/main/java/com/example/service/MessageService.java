package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.MessageCreationException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * This method attempts to persist a Message to the database
     * @param message the Message we wish to create
     * @throws MessageCreationException thrown if the Message is not valid
     */
    public void createMessage(Message message) throws MessageCreationException {
        validateMessage(message);
        messageRepository.save(message);
    }


    /**
     * This method will validate a Message to ensure that it can be added to the database
     * @param message the Message we wish to validate. A Message is valid if it is not blank, under 255 characters,
     *                and posted_by refers to a real, existing user.
     * @throws MessageCreationException thrown if the Message is invalid.
     */
    private void validateMessage(Message message) throws MessageCreationException {
        boolean validMessage = isValidMessageText(message.getMessageText()) && isValidPostedBy(message.getPostedBy());

        if (!validMessage) {
            throw new MessageCreationException("Sorry, we could not post that message. " +
                    "Please make sure that your message is not empty, is less than 255 characters, and that you are logged in.");
        }
    }

    /**
     * This method will validate the text of a message.
     * @param messageText the text of the message we wish to validate.
     *                    Message text is valid if it is not blank and under 255 characters.
     * @return true or false depending on whether the message text is valid
     */
    private boolean isValidMessageText(String messageText) {
        return 0 < messageText.length() && messageText.length() <= 255;
    }

    /**
     * This method will determine of the postedBy field of a Message refers to a real, existing user.
     * @param postedBy the ID of a user that we want to validate exists
     * @return true or false depending on whether postedBy refers to a real, existing user.
     */
    private boolean isValidPostedBy(int postedBy) {
        return accountRepository.findById(postedBy).isPresent();
    }
}
