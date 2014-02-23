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
    
    private int CONVREPLACEDELAY = 60000;
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
        try{
            mAPI = new ClientApi(url);
        }catch (Exception e){log(e);}
        
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
        
        Collection<Conversation> conversations = null;
        try{
            conversations = mAPI.conferenceResource.mostActiveRecently(mConfrence.getId(), 600000, 5);
        }catch (Exception e){log(e);}
        
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
        
        Logger.getGlobal().log(Level.INFO, mGUI.getMap().toString());
        
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
        Logger.getGlobal().log(Level.INFO, "the server ("+url+") is connected");
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
            public void handle(final Message t) {
                final long id = c.getId();
                synchronized (mConversations){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                mGUI.displayMessage(t, id);
                                mGUI.scrollToEnd(id);
                            } catch (NoMessageListException ex) {
                                System.err.println(id);
                                System.err.println(mGUI.getMap());
                                Logger.getGlobal().log (Level.SEVERE, "message from conversation "+id+" could not be displayed ", ex);
                            }
                        }
                    });
                }
            }
        };
        try{
            return mAPI.conversationResource.listenToMessages(c.getId()).subscribe(handler);
        } catch (Exception e){log(e);}
        return null;
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
                            mGUI.displayMessage(new Message() {
                                @Override
                                public long getId() {return -1;}
                                @Override
                                public long getTimeStamp() {return System.currentTimeMillis();}
                                @Override
                                public User getSender() {return null;}
                                @Override
                                public String getSenderName() {return "local";}
                                @Override
                                public void setSenderName(String senderName) {return;}
                                @Override
                                public void setSender(User user) {return;}
                                @Override
                                public Conversation getConversation() {return c;}
                                @Override
                                public void setConversation(Conversation conversation) {}
                                @Override
                                public String getContents() {
                                    return "you have connected to "+c.getName();
                                }
                                @Override
                                public void setContents(String contents) {
                                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                }
                                @Override
                                public void delete() {
                                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                }
                                @Override
                                public void save() {
                                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                }
                            }, c.getId());
                        } catch (NoMessageListException ex) {
                            Logger.getGlobal().log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        }
        
        for (final Message msg: list){
            Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            mGUI.displayMessage(msg, c.getId());
                        } catch (NoMessageListException ex) {
                            Logger.getGlobal().log (Level.SEVERE, "message from conversation "+c.getId()+" could not be displayed the map contains " +mGUI.getMap().toString(), ex);
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
                        Thread.sleep(CONVREPLACEDELAY);
                    } catch (InterruptedException ex) {
                        Logger.getGlobal().log(Level.SEVERE, null, ex);
                    }
                    List<Conversation> conv = null;
                    try {
                        conv = mAPI.conferenceResource.mostUsers(mTC.getConfrenceID(), 10);
                    } catch (Exception ex) {
                        log(ex);
                    }
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
                                    Logger.getGlobal().log(Level.SEVERE, "there has been an issue while replacing conversation "+mConversations.get(c).getName(), ex);
                                } catch (NotCurrentConversationException ex) {
                                    Logger.getGlobal().log(Level.SEVERE, null, ex); break;
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
        try{
            Conversation c = mAPI.conversationResource.get(conversationID);
            return new ConvStats(mAPI.conferenceResource.userCount(mConfrence.getId(), conversationID),
                    mAPI.conferenceResource.contributingUsers(mConfrence.getId(), conversationID, false),
                    mAPI.conferenceResource.messageCount(mConfrence.getId(), conversationID),
                    mAPI.conferenceResource.maleToFemaleRatio(mConfrence.getId()));
       } catch (Exception e){log(e);}
       return new ConvStats(0,0,0,0.5);
    }

    public ConfrenceStats getGlobalStats() {
        try{
            return new ConfrenceStats(mAPI.conferenceResource.getConversations(mConfrence.getId()));
        }catch (Exception e){log(e);}
        return null;
    }

    double getNumberOfMessages(Long val2) {
        try{
            for (Conversation c:mAPI.conferenceResource.getConversations(mConfrence.getId())){
                if (c.getId()==val2){
                    return c.getMessages().size();
                }
            }
        }catch (Exception e){log(e);}
        return 0;
    }

    Number getActivity() {
        try{
            return mAPI.conferenceResource.activity(mConfrence.getId(), (1000*60*5));
        }catch (Exception e){log(e);}
        return Integer.valueOf(0);
    }

    List<User> getUsers(long id) {
        try{
            for (Conversation c: mAPI.conferenceResource.getConversations(mConfrence.getId())){
                if (c.getId()==id){return (List<User>)c.getUsers();}
            }
        }catch (Exception e){log(e);}
        return new ArrayList<User>();
    }

    private void log(Exception e) {
        Logger.getGlobal().log(Level.SEVERE, "There has been an issue with the server", e);
        mTC.exit(5, "The connection to the server has failed");
    }
    
}
