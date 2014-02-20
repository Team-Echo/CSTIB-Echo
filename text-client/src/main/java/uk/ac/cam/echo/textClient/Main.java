package uk.ac.cam.echo.textClient;


import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.client.data.MessageData;
import uk.ac.cam.echo.client.data.UserData;
import uk.ac.cam.echo.data.*;
import uk.ac.cam.echo.data.async.Handler;
import uk.ac.cam.echo.data.async.Subscription;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class Main {
    private static ClientApi api = new ClientApi("http://localhost:8080");

    public static void main(String[] args) {
        Conference conference = configureConference();
        User user = configureUser();
        Conversation conversation = configureConversation(conference);

        user.setCurrentConversation(conversation);
        user.save(); // MUST call save after setting conversation!

        System.out.println("Welcome to conversation [" + conversation.getName() + "]:");

        displayPreviousMessages(conversation);

        listenToMessages(conversation);
        listenToInputs(conversation, user);
    }

    private static void listenToInputs(Conversation conversation, User user) {
        while (true) {
            String input = readline();
            Message msg = api.newMessage(conversation);
            msg.setSender(user);
            msg.setContents(input);
            msg.setSenderName(user.getDisplayName());
            msg.save();
        }
    }

    private static void displayPreviousMessages(Conversation conversation) {
        for (Message msg: conversation.getMessages()) {
            String sender = msg.getSenderName() == null ? "Anonymous" : msg.getSenderName();
            System.out.println(sender + ": " + msg.getContents());
        }
    }

    private static Subscription listenToMessages(Conversation conversation) {
        Handler<Message> handler = new Handler<Message>() {
            @Override
            public void handle(Message msg) {
                String sender = msg.getSenderName() == null ? "Anonymous" : msg.getSenderName();
                System.out.println(sender + ": " + msg.getContents());
            }
        };


        //TODO make a better api for this
        return api.conversationResource.listenToMessages(conversation.getId()).subscribe(handler);
    }

    private static DataInputStream in = new DataInputStream(new BufferedInputStream(System.in));
    private static String readline() {

        String input = null;
        while(input == null) {
            try {
                input = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return input;
    }

    private static Conversation configureConversation(Conference conference) {
        System.out.println("Insert the id of the conversation or a new name in order to create a new Conversation.");
        for (Conversation conversation : conference.getConversationSet()) {
            System.out.print(conversation.getId() + ": " + conversation.getName());
            System.out.print("| Tags: ");
            for (Tag t: conversation.getTags()){
                System.out.print(t.getName() + " ");
            }
            System.out.println();
        }

        System.out.print("Please insert your choice: ");
        String input = readline();
        Conversation conversation = null;
        try {
            long id = Long.parseLong(input);
            for (Conversation conv: conference.getConversationSet()) {
                if (conv.getId() == id) {
                    conversation = conv;
                    break;
                }
            }
        } catch (NumberFormatException e) {
            // Long.parseLong can throw an error
        }


        if (conversation == null) {
            conversation = api.newConversation();

            conversation.setConference(conference);
            conversation.setName(input);
            conversation.save();

            Tag t = api.newTag(conversation);
            t.setName("Happy tag");
            t.save();
        }

        return conversation;

    }

    private static User configureUser() {
        System.out.println("Insert the id of the user or a new name in order to create a new user.");
        for (User user : api.userResource.getAll()) {
            System.out.println(user.getId() + ": " + user.getUsername());
        }

        System.out.print("Please insert your choice: ");
        String username = readline();

        boolean auth = false;

        try {
            long id = Long.parseLong(username);
            for (User user : api.userResource.getAll()) {
                if (user.getId() == id) {
                    username = user.getUsername();
                    auth = true;
                    break;
                }
            }
        } catch (NumberFormatException e) {
            // Long.parseLong can throw an error
        }

        System.out.print("Enter your password, or a new password if you made a new user: ");
        String pass = readline();

        User ret = api.userResource.authenticate(username, pass);

        if (ret == null) {
            if (auth) {
                System.out.println("Username and password don't match!");
                System.exit(1);
            }
            UserData ret2 = (UserData)api.newUser();
            ret2.setUsername(username);
            ret2.setPassword(pass);
            ret2.setFirstName("Petar");
            ret2.setLastName("Velickovic");
            ret2.setDisplayName("Petar Velickovic");
            ret2.save();
            ret = ret2;
        }

        return ret;
    }

    private static Conference configureConference() {
        List<Conference> conferences = api.conferenceResource.getAll();
        if (conferences.size() > 0)
            return conferences.get(0);

        System.out.print("Input conference name: ");
        String name = readline();

        Conference conference = api.newConference();
        conference.setName(name);
        conference.save();

        return conference;
    }
}
