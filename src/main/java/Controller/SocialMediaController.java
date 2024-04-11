package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::userRegisterHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromUserHandler);

        return app;
    }

    /**
     * Handler to create a new user Account.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST
     * request into an Account object.
     * If a null account is returned, the API will return a 400 client error
     * message.
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP
     *            request and response.
     * @throws JsonProcessingException will be thrown if there is an issue
     *                                 converting JSON into an object.
     */
    private void userRegisterHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount != null) {
            ctx.json(mapper.writeValueAsString(addedAccount));
        } else {
            ctx.status(400);
        }
    }

    /**
     * Handler to verify login.
     * Login will be successful if the username and password provided in the request
     * body matches a real account on the database.
     * If login is not successful, the response status should be 401.
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP
     *            request and response.
     * @throws JsonProcessingException will be thrown if there is an issue
     *                                 converting JSON into an object.
     */
    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.login(account);
        if (addedAccount != null) {
            ctx.json(mapper.writeValueAsString(addedAccount));
        } else {
            ctx.status(401);
        }
    }

    /**
     * Handler to post a new message.
     * If a null message is returned, the API will return a 400 client error
     * message.
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP
     *            request and response.
     * @throws JsonProcessingException will be thrown if there is an issue
     *                                 converting JSON into an object.
     */
    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);
        if (createdMessage != null) {
            ctx.json(mapper.writeValueAsString(createdMessage));
        } else {
            ctx.status(400);
        }
    }

    /**
     * Handler to retrieve all messages.
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP
     *            request and response.
     * @throws JsonProcessingException will be thrown if there is an issue
     *                                 converting JSON into an object.
     */
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        ctx.json(messageService.getAllMessages());
    }

    /**
     * Handler to get a message identified by a given message_id.
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP
     *            request and response.
     * @throws JsonProcessingException will be thrown if there is an issue
     *                                 converting JSON into an object.
     */
    private void getMessageHandler(Context ctx) throws JsonProcessingException {
        if (messageService.getMessagegivenMessageId(Integer.parseInt(ctx.pathParam("message_id"))) != null) {
            ctx.json(messageService.getMessagegivenMessageId(Integer.parseInt(ctx.pathParam("message_id"))));
        }
    }

    /**
     * Handler to delete an existing message and remove the message from the
     * database.
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP
     *            request and response.
     * @throws JsonProcessingException will be thrown if there is an issue
     *                                 converting JSON into an object.
     */
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        if (messageService.getMessagegivenMessageId(Integer.parseInt(ctx.pathParam("message_id"))) != null) {
            ctx.json(messageService.getMessagegivenMessageId(Integer.parseInt(ctx.pathParam("message_id"))));
            messageService.deleteMessagegivenMessageId(Integer.parseInt(ctx.pathParam("message_id")));
        }
    }

    /**
     * Handler to update a message identified by a message_id. The request body
     * should contain the new message_text to
     * replace the message identified by the message_id.
     * If message returned is null, response status will be 400.
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP
     *            request and response.
     * @throws JsonProcessingException will be thrown if there is an issue
     *                                 converting JSON into an object.
     */
    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        message.setMessage_id(Integer.parseInt(ctx.pathParam("message_id")));
        message = messageService.updateMessageGivenMessageId(message);

        if (message == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(message));
        }
    }

    /**
     * Handler to get all messages posted by a particular user given the Account
     * Id.
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP
     *            request and response.
     * @throws JsonProcessingException will be thrown if there is an issue
     *                                 converting JSON into an object.
     */
    private void getAllMessagesFromUserHandler(Context ctx) throws JsonProcessingException {
        ctx.json(messageService.getAllMessagesFromUser(Integer.parseInt(ctx.pathParam("account_id"))));
    }
}