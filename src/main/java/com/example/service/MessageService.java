package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;
import com.example.exception.MessageCreationException;
import com.example.exception.MessageNotFoundException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
@Transactional
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
     * This method queries the database for all messages
     * @return a List of all messages in the database
     */
    @Transactional(readOnly = true)
    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    /**
     * This method queries the database for a specific message and returns it
     * @param messageId the ID of the message we wish to find
     * @return an Optional representation of the message, if it exists.
     */
    @Transactional(readOnly = true)
    public Optional<Message> getMessageById(int messageId) {
        return messageRepository.findById(messageId);
    }

    /**
     * This method tells the database to delete a specific message
     * @param messageId the ID of the Message we wish to delete
     * @return the number of Messages deleted (should be at most 1)
     */
    public Integer deleteMessageById(int messageId) {
        return messageRepository.deleteByMessageId(messageId);
    }

    /**
     * This method will attempt to update a Message in the database
     * @param messageId the ID of the Message we wish to update
     * @param messageText the new text of the Message
     * @return the number of Messages updated (should only be 1)
     * @throws MessageNotFoundException thrown if no Message with messageId exists
     * @throws MessageCreationException thrown if messageText is either empty or mor than 255 characters
     */
    public Integer updateMessage(int messageId, String messageText) throws MessageNotFoundException, MessageCreationException {
        validateMessage(messageId, messageText);

        return messageRepository.updateByMessageIdAndMessageText(messageId, messageText);
    }

    /**
     * This method queries the database for all Messages posted by a specific Account
     * @param accountId the ID of the Account whose Messages we wish to see
     * @return a List of all Messages posted by accountId
     */
    @Transactional(readOnly = true)
    public List<Message> getAllMessagesFromUser(int accountId) {
        return messageRepository.findAllByPostedBy(accountId);
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

    /**
     * This method validates if a Message can be updated, and throws the appropriate exceptions if it cannot
     * @param messageId the ID of the Message we wish to update
     * @param messageText the new text of the Message
     * @throws MessageNotFoundException thrown if no Message with messageId exists
     * @throws MessageCreationException thrown if the messageText is empty or more than 255 characters
     */
    private void validateMessage(int messageId, String messageText) throws MessageNotFoundException, MessageCreationException{
        if (!messageExists(messageId)) {
            throw new MessageNotFoundException("The message you're trying to update was not found");
        }

        if (!isValidMessageText(messageText)) {
            throw new MessageCreationException("Sorry, we could not update that message " +
                    "because it is either empty or more than 255 characters.");
        }
    }

    /**
     * This method checks if a Message already exists
     * @param messageId the ID of the Message we want to check exists
     * @return true if the message exists, false otherwise
     */
    private boolean messageExists(int messageId) {
        return messageRepository.findById(messageId).isPresent();
    }
}
