package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    /**
     * No-args contructor for a messageSevice instantiates a plain messageDAO.
     */
    public MessageService() {
        messageDAO = new MessageDAO();
    }

    /**
     * Constructor for a messageService when a messageDAO is provided.
     * 
     * @param messageDAO a messageDAO.
     */
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    /**
     * Use the messageDAO to retrieve all messages.
     *
     * @return all Messages in database.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public List<Message> getAllMessagesFromUser(int account_id) {
        return messageDAO.getAllMessagesFromUser(account_id);
    }

    /**
     * Use messageDAO to create and add a message.
     *
     * @param message an message object.
     * @return Message if created succesfully.
     */
    public Message createMessage(Message message) {
        return messageDAO.createMessage(message);
    }

    /**
     * Use messageDAO to update the message text of an existing message.
     * 
     * @param message a message.
     * @return Message if update was successful.
     */
    public Message updateMessageGivenMessageId(Message message) {
        if (message.getMessage_text().length() > 255 || message.getMessage_text().isEmpty()) {
            return null;
        } else {
            return messageDAO.updateMessageGivenMessageId(message.getMessage_text(), message.getMessage_id());
        }
    }

    /**
     * Use messageDAO to get a message with given message_id.
     * 
     * @param message_id a message ID.
     * @return Message if getting message was successful.
     */
    public Message getMessagegivenMessageId(int message_id) {

        List<Message> messages = messageDAO.getAllMessages();
        for (Message m : messages) {
            if (m.getMessage_id() == message_id) {
                return messageDAO.getOneMessageGivenMessageId(message_id);
            }
        }
        return null;
    }

    /**
     * Use messageDAO to delete a message with given message_id.
     * 
     * @param message_id a message ID.
     */
    public void deleteMessagegivenMessageId(int message_id) {
        if (getMessagegivenMessageId(message_id) != null) {
            messageDAO.deleteOneMessageGivenMessageId(message_id);
        }
    }
}
