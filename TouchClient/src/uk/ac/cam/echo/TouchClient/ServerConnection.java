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
import uk.ac.cam.echo.data.User;
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
    private List<Conversation> mConversations;
    private Conference mConfrence;
    private List<Subscription> mSub;

    @SuppressWarnings("LeakingThisInConstructor")
    public ServerConnection(TouchClient tc){
        mTC = tc;
        mTC.regesterServerConnection(this);
    }
    
    @Override
    public void run() {
        mConversations = Collections.synchronizedList(new ArrayList<Conversation>());
        mSub = Collections.synchronizedList(new ArrayList<Subscription>());
        
        //tries to get the GUI repetedly untill the gui has been initalized
        while (true){
            try {
                mGUI = mTC.getGUI();
            } catch (NotInstantiatedYetException ex) {
                continue;
            }
            break;
        }
        
        //some code to get the url/ip to connect to the server defalut local host
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
                mConfrence = configureConference();
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
        
        while (mGUI.getMap().isEmpty()){}
        
        Logger.getLogger(ServerConnection.class.getName()).log(Level.INFO, mGUI.getMap().toString());
        
        synchronized (mConversations){
            synchronized (mSub){
                mConversations.add(conversation1);
                mConversations.add(conversation2);
                mConversations.add(conversation3);
                mConversations.add(conversation4);
                mConversations.add(conversation5);
        
                for (Conversation c: mConversations){
                    mSub.add(listenToConversation(c));
                }
            }
        }
        
        replaceConversations();
        Logger.getLogger(ServerConnection.class.getName()).log(Level.INFO, "the server ("+url+") is connected");
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
                synchronized (mConversations){
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
            }
        };
        return mAPI.conversationResource.listenToMessages(c.getId()).subscribe(handler);
    }

    void kill() {
        Runtime.getRuntime().exit(1);
    }

    private void displayPreviousMessages(final Conversation c) {
        if (c.getId()<0){return;}
        List<Message> list = (List)c.getMessages(50);
        
        if (list.isEmpty()){
            synchronized (mConversations){ 
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            mGUI.displayMessage("you have connected to "+c.getName(), c.getId());
                        } catch (NoMessageListException ex) {
                            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        }
        
        for (Message msg: list){
            String sender = msg.getSender() == null ? "Anonymous" : msg.getSender().getUsername();
            final String message = sender.concat(" : ".concat(msg.getContents()));
            Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            mGUI.displayMessage(message, c.getId());
                        } catch (NoMessageListException ex) {
                            Logger.getLogger(ServerConnection.class.getName()).log (Level.SEVERE, "message from conversation "+c.getId()+" could not be displayed the map contains " +mGUI.getMap().toString(), ex);
                        }
                    mGUI.scrollToEnd(c.getId());
                    }
                });
        }
    }

    private void replaceConversations() {
        (new Thread(new Runnable(){
            @Override
            @SuppressWarnings("SleepWhileInLoop")
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000*60);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    List<Conversation> conv = null;
                    try {
                        conv = mAPI.conferenceResource.mostUsers(mTC.getConfrenceID(), 10);
                    } catch (NotInstantiatedYetException ex) {//should never happen
                        Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    /*synchronized (mConversations){
                        synchronized (mSub){*/
                            for (int c = 0; c<mConversations.size(); c++){
                                if (!isContainedIn(conv,mConversations.get(c))){
                                    for (int c2 = 0; c2<conv.size(); c2++){
                                        //the conversation to be replaced
                                        Conversation currentConversation = mConversations.get(c);
                                        
                                        //the conversation to replace
                                        Conversation newConversation = conv.get(c2);
                                        
                                        //new lists
                                        List<Conversation> newmConversations = new ArrayList();
                                        List<Subscription> newmSub = new ArrayList();
                                        
                                        //unsubscribe to old subscription
                                        mSub.get(c).unsubscribe();
                                        
                                        //replace conversation in mConversations
                                        for (int i = 0; i < c; i++){newmConversations.add(mConversations.get(i));}
                                        newmConversations.add(newConversation);
                                        for (int i = c+1;i<mConversations.size();i++){newmConversations.add(mConversations.get(i));}
                                        
                                        //map the conversation to the correct GUI pane
                                        try{
                                            mGUI.replaceConversation(currentConversation.getId(), newConversation.getName(), newConversation.getId());
                                        } catch (NoMessageListException ex) {
                                            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, "there has been an issue while replacing conversation "+mConversations.get(c).getName(), ex);
                                        } catch (NotCurrentConversationException ex) {
                                            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex); break;
                                        } catch (ConversationAlredyDisplayedException ex) {
                                            continue;//this ocurs if the replacement conversation is alredy on screen
                                        }
                                        
                                        
                                        //replace subscription in mSub
                                        for (int i = 0; i < c; i++){newmSub.add(mSub.get(i));}
                                        newmSub.add(listenToConversation(newConversation));
                                        for (int i = c+1;i<mSub.size();i++){newmSub.add(mSub.get(i));}
                                        
                                        //set mSub and mConversation
                                        synchronized (mConversations){
                                            synchronized (mSub){
                                                mSub.clear();
                                                mConversations.clear();
                                                mSub.addAll(newmSub);
                                                mConversations.addAll(newmConversations);
                                            }
                                        }
                                        
                                        break;
                                    }  
                                }
                            }
                       /* }
                    }*/
                }
            }

            private boolean isContainedIn(List<Conversation> conv, Conversation val) {
                for (Conversation c: conv){
                    if (c.getId()==val.getId()){return true;}
                }
                return false;
            }
        
        })).start();
    }

    public ConvStats getStats(long conversationID) {
        if (conversationID<0){return new ConvStats(0,0,0,0.5);}
        Conversation c = mAPI.conversationResource.get(conversationID);
        return new ConvStats(c.getUsers().size(),0,c.getMessages().size(),0.5);
    }

    public ConfrenceStats getGlobalStats() {
        return new ConfrenceStats(mAPI.conferenceResource.getConversations(mConfrence.getId())); 
    }

    double getNumberOfMessages(Long val2) {
        for (Conversation c:mAPI.conferenceResource.getConversations(mConfrence.getId())){
            if (c.getId()==val2){
                return c.getMessages().size();
            }
        }
        return 0;
    }

    Number getActivity() {
        return mAPI.conferenceResource.activity(mConfrence.getId(), (1000*60*5));
    }

    List<User> getUsers(long id) {
        for (Conversation c: mAPI.conferenceResource.getConversations(mConfrence.getId())){
            if (c.getId()==id){return (List<User>)c.getUsers();}
        }
        return new ArrayList<User>();
    }
    
}
