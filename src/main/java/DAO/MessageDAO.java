package DAO;

import java.util.List;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;

public class MessageDAO {

    /**
     * Retrieve all messages in the database.
     * 
     * @return all messages.
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Retrive all messages from a particular user given the account id.
     * 
     * @param account_id a account ID.
     * @return all messages from the particular user.
     */
    public List<Message> getAllMessagesFromUser(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Retrieve a message given the message ID.
     * 
     * @param message_id a message ID.
     * @return the particular message with matching message_id.
     */
    public Message getOneMessageGivenMessageId(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Update a message given the message_id with a new message text.
     * 
     * @param new_message_text a message text to replace the old message text.
     * @param message_id       a message ID.
     * @return message with updated message text.
     */
    public Message updateMessageGivenMessageId(String new_message_text, int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        if (getOneMessageGivenMessageId(message_id) != null) {

            try {

                String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, new_message_text);
                preparedStatement.setInt(2, message_id);

                preparedStatement.executeUpdate();
                return getOneMessageGivenMessageId(message_id);

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    /**
     * Create a new message to add to database.
     * 
     * @param message a message.
     * @return created message.
     */
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {

            // Message text cannot be blank.
            if (message.getMessage_text().isEmpty()) {
                return null;
            }

            // Check if the message_text meets the length requirement.
            if (message.getMessage_text().length() > 255) {
                return null;
            }

            String sql = "INSERT INTO message(posted_by,message_text,time_posted_epoch) VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(),
                        message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Delete a message given the message ID.
     */
    public void deleteOneMessageGivenMessageId(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
