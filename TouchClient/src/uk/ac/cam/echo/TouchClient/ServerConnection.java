/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.cam.echo.TouchClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.async.Handler;
import uk.ac.cam.echo.data.async.Subscription;

/**
 *the class that connects to the server will eventually implement the ClientApi
 * 
 * @author Philip
 */
public class ServerConnection implements Runnable{
    
    private final TouchClient mTC;
    private GUIController mGUI;
    private ClientApi mAPI;

    public ServerConnection(TouchClient tc){
        mTC = tc;
        mTC.regesterServerConnection(this);
    }
    
    @Override
    public void run() {
        //tries to get the GUI repetedly untill the gui has been initalized
        while (true){
            try {
                mGUI = mTC.getGUI();
            } catch (NotInstantiatedYetException ex) {
                continue;
            }
            break;
        }
        
        //some code to get the url/ip to connect to the srver
        String url = "http://127.0.0.1";
        boolean retry = true;
        while (retry){
            try {
                url = mTC.getConfrenceURL();
            } catch (NotInstantiatedYetException ex) {
                try {
                    url = mTC.getConfrenceIP();
                } catch (NotInstantiatedYetException ex1) {
                    continue;
                }
            }
            retry = false;
        }
        
        //the connection to the server
        mAPI = new ClientApi(url);
        
        retry = true;
        while(retry){
            try {
                Conference confrence = configureConference();
            } catch (NotInstantiatedYetException ex) {
                continue;
            } catch (ConfrenceNotFoundException ex) {
                //TODO handel this error better
                mTC.exit(1,"the confrence has not been foud");
            }
           retry=false;
        }
        
        Collection<Conversation> conversations = mAPI.conversationResource.getAll();
        
        final Conversation conversation1;
        final Conversation conversation2;
        final Conversation conversation3;
        final Conversation conversation4;
        final Conversation conversation5;       
        
        //gets the first conversations and if there are not enugh it replaces them with placeholders
        Iterator it = conversations.iterator();
        if (it.hasNext()){
            conversation1 = (Conversation)it.next();
            if (it.hasNext()){
                conversation2 = (Conversation)it.next();
                if (it.hasNext()){
                    conversation3 = (Conversation)it.next();
                    if (it.hasNext()){
                        conversation4 = (Conversation)it.next();
                        if (it.hasNext()){
                            conversation5 = (Conversation)it.next();
                        }else{
                            conversation5 = new ConversationPlaceHolder(-5,"Not connected yet");
                        }
                    }else{
                        conversation4 = new ConversationPlaceHolder(-4,"Not connected yet");
                        conversation5 = new ConversationPlaceHolder(-5,"Not connected yet");
                    }
                }else{
                    conversation3 = new ConversationPlaceHolder(-3,"Not connected yet");
                    conversation4 = new ConversationPlaceHolder(-4,"Not connected yet");
                    conversation5 = new ConversationPlaceHolder(-5,"Not connected yet");
                }
            }else{
                conversation2 = new ConversationPlaceHolder(-2,"Not connected yet");
                conversation3 = new ConversationPlaceHolder(-3,"Not connected yet");
                conversation4 = new ConversationPlaceHolder(-4,"Not connected yet");
                conversation5 = new ConversationPlaceHolder(-5,"Not connected yet");
            }    
        }else{
            conversation1 = new ConversationPlaceHolder(-1,"Not connected yet");
            conversation2 = new ConversationPlaceHolder(-2,"Not connected yet");
            conversation3 = new ConversationPlaceHolder(-3,"Not connected yet");
            conversation4 = new ConversationPlaceHolder(-4,"Not connected yet");
            conversation5 = new ConversationPlaceHolder(-5,"Not connected yet");
        }   
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mGUI.initConversations(conversation1.getName(), conversation1.getId(),
                                       conversation2.getName(), conversation2.getId(),
                                       conversation3.getName(), conversation3.getId(),
                                       conversation4.getName(), conversation4.getId(),
                                       conversation5.getName(), conversation5.getId());
            }
        });

        
        listenToConversation(conversation1);
        listenToConversation(conversation2);
        listenToConversation(conversation3);
        listenToConversation(conversation4);
        listenToConversation(conversation5);
        
        
        System.out.println("the server is connected");
        /*MessageTest mt = new MessageTest(mGUI);
        (new Thread(mt)).start();*/
    }

    private Conference configureConference() throws NotInstantiatedYetException, ConfrenceNotFoundException {
        try {
            List<Conference> conferences = mAPI.conferenceResource.getAll();
            for (Conference c : conferences){
                if (mTC.getConfrenceID() == c.getId()){
                    return c;
                }
            }
            throw new ConfrenceNotFoundException();
        }catch (Exception e){
            throw new ConfrenceNotFoundException();
        }
    }

    private Subscription listenToConversation(final Conversation c) {
        displayPreviousMessages(c);
        Handler<Message> handler = new Handler<Message>(){
            @Override
            public void handle(Message t) {
                final String sender = t.getSender() == null ? "Anonymous" :t.getSender().getUsername();
                final String message = (sender+" : "+t.getContents());
                final long id = c.getId();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            mGUI.displayMessage(message, id);
                            mGUI.scrollToEnd(id);
                        } catch (NoMessageListException ex) {
                            System.err.println(id);
                            System.err.println(mGUI.getMap());
                            Logger.getLogger(ServerConnection.class.getName()).log (Level.SEVERE, "message from conversation "+id+" could not be displayed ", ex);
                        }
                    }
                });
            }
        };
        return mAPI.conversationResource.listenToMessages(c.getId()).subscribe(handler);
    }

    void kill() {
        Runtime.getRuntime().exit(1);
    }

    private void displayPreviousMessages(final Conversation c) {
        int count = 0;
        ArrayList<Message> list = new ArrayList();
        for (Message msg: c.getMessages()) {
            count++;
            list.add(msg);
            if (count > 50){ break;}
        }
        Collections.reverse(list);
        for (Message msg: list){
            String sender = msg.getSender() == null ? "Anonymous" : msg.getSender().getUsername();
            final String message = sender.concat(" : ".concat(msg.getContents()));
            Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            mGUI.displayMessage(message, c.getId());
                        } catch (NoMessageListException ex) {
                            System.err.println(c.getId());
                            System.err.println(mGUI.getMap());
                            Logger.getLogger(ServerConnection.class.getName()).log (Level.SEVERE, "message from conversation "+c.getId()+" could not be displayed ", ex);
                        }
                    mGUI.scrollToEnd(c.getId());
                    }
                });
        }
    }
    
}
