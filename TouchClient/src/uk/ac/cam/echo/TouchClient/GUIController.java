/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.cam.echo.TouchClient;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.FocusModel;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Philip
 */
public class GUIController implements Initializable {
    
    @FXML
    private ListView conversation1_messages;
    private ObservableList<String> messages1;
    private FocusModel messages1_focus;
    @FXML
    private ListView conversation2_messages;
    private ObservableList<String> messages2;
    private FocusModel messages2_focus;
    @FXML
    private ListView conversation3_messages;
    private ObservableList<String> messages3;
    private FocusModel messages3_focus;
    @FXML
    private ListView conversation4_messages;
    private ObservableList<String> messages4;
    private FocusModel messages4_focus;
    @FXML
    private ListView conversation5_messages;
    private ObservableList<String> messages5;
    private FocusModel messages5_focus;
    
    @FXML
    private Label conversation1_name;
    @FXML
    private Label conversation2_name;
    @FXML
    private Label conversation3_name;
    @FXML
    private Label conversation4_name;
    @FXML
    private Label conversation5_name;
    
    @FXML
    private TabPane conversation1;
    @FXML
    private TabPane conversation2;
    @FXML
    private TabPane conversation3;
    @FXML
    private TabPane conversation4;
    @FXML
    private TabPane conversation5;
    @FXML
    private Pane stats_pane;
    
    private HashMap<Integer,Integer> idtopane;
    
    private void addMessage1(String mess){
        messages1.add(mess);
        conversation1_messages.setItems(messages1);
    }
    private void addMessage2(String mess){
        messages2.add(mess);
        conversation2_messages.setItems(messages2);
    }
    private void addMessage3(String mess){
        messages3.add(mess);
        conversation3_messages.setItems(messages3);
    }   
    private void addMessage4(String mess){
        messages4.add(mess);
        conversation4_messages.setItems(messages4);
    }   
    private void addMessage5(String mess){
        messages5.add(mess);
        conversation5_messages.setItems(messages5);
    }  
    private void init(){
       idtopane = new HashMap();
       messages1_focus = new FocusModel() {

        @Override
        protected int getItemCount() {
            return messages1.size();
        }

           @Override
           protected Object getModelItem(int i) {
               return (Object)"";
           }
       };
       messages2_focus = new FocusModel() {

        @Override
        protected int getItemCount() {
            return messages2.size();
        }

           @Override
           protected Object getModelItem(int i) {
               return (Object)"";
           }
       };
       messages3_focus = new FocusModel() {

        @Override
        protected int getItemCount() {
            return messages3.size();
        }

           @Override
           protected Object getModelItem(int i) {
               return (Object)"";
           }
       };
       messages4_focus = new FocusModel() {

        @Override
        protected int getItemCount() {
            return messages4.size();
        }

           @Override
           protected Object getModelItem(int i) {
               return (Object)"";
           }
       };
       messages5_focus = new FocusModel() {

        @Override
        protected int getItemCount() {
            return messages5.size();
        }

           @Override
           protected Object getModelItem(int i) {
               return (Object)"";
           }
       };
       conversation1_messages.setFocusModel(messages1_focus);
       conversation2_messages.setFocusModel(messages2_focus);
       conversation3_messages.setFocusModel(messages3_focus);
       conversation4_messages.setFocusModel(messages4_focus);
       conversation5_messages.setFocusModel(messages5_focus);
       messages1 = new MessageDisplayList();
       messages2 = new MessageDisplayList();
       messages3 = new MessageDisplayList();
       messages4 = new MessageDisplayList();
       messages5 = new MessageDisplayList();
       
        final Delta dragDelta1 = new Delta();
        conversation1.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta1.x = conversation1.getLayoutX() - mouseEvent.getSceneX();
                dragDelta1.y = conversation1.getLayoutY() - mouseEvent.getSceneY();
                conversation1.setCursor(Cursor.MOVE);
            }
        });
        conversation1.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent mouseEvent) {
                conversation1.setCursor(Cursor.HAND);
            }
        });
        conversation1.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                conversation1.setLayoutX(mouseEvent.getSceneX() + dragDelta1.x);
                conversation1.setLayoutY(mouseEvent.getSceneY() + dragDelta1.y);
            }
        });
        conversation1.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                conversation1.setCursor(Cursor.HAND);
            }
        });
        final Delta dragDelta2 = new Delta();
        conversation2.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta2.x = conversation2.getLayoutX() - mouseEvent.getSceneX();
                dragDelta2.y = conversation2.getLayoutY() - mouseEvent.getSceneY();
                conversation2.setCursor(Cursor.MOVE);
            }
        });
        conversation2.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent mouseEvent) {
                conversation2.setCursor(Cursor.HAND);
            }
        });
        conversation2.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                conversation2.setLayoutX(mouseEvent.getSceneX() + dragDelta2.x);
                conversation2.setLayoutY(mouseEvent.getSceneY() + dragDelta2.y);
            }
        });
        conversation2.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                conversation2.setCursor(Cursor.HAND);
            }
        });
        final Delta dragDelta3 = new Delta();
        conversation3.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta3.x = conversation3.getLayoutX() - mouseEvent.getSceneX();
                dragDelta3.y = conversation3.getLayoutY() - mouseEvent.getSceneY();
                conversation3.setCursor(Cursor.MOVE);
            }
        });
        conversation3.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent mouseEvent) {
                conversation3.setCursor(Cursor.HAND);
            }
        });
        conversation3.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                conversation3.setLayoutX(mouseEvent.getSceneX() + dragDelta3.x);
                conversation3.setLayoutY(mouseEvent.getSceneY() + dragDelta3.y);
            }
        });
        conversation3.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                conversation3.setCursor(Cursor.HAND);
            }
        });
        final Delta dragDelta4 = new Delta();
        conversation4.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta4.x = conversation4.getLayoutX() - mouseEvent.getSceneX();
                dragDelta4.y = conversation4.getLayoutY() - mouseEvent.getSceneY();
                conversation4.setCursor(Cursor.MOVE);
            }
        });
        conversation4.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent mouseEvent) {
                conversation4.setCursor(Cursor.HAND);
            }
        });
        conversation4.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                conversation4.setLayoutX(mouseEvent.getSceneX() + dragDelta4.x);
                conversation4.setLayoutY(mouseEvent.getSceneY() + dragDelta4.y);
            }
        });
        conversation4.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                conversation4.setCursor(Cursor.HAND);
            }
        });
        final Delta dragDelta5 = new Delta();
        conversation5.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta5.x = conversation5.getLayoutX() - mouseEvent.getSceneX();
                dragDelta5.y = conversation5.getLayoutY() - mouseEvent.getSceneY();
                conversation5.setCursor(Cursor.MOVE);
            }
        });
        conversation5.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent mouseEvent) {
                conversation5.setCursor(Cursor.HAND);
            }
        });
        conversation5.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                conversation5.setLayoutX(mouseEvent.getSceneX() + dragDelta5.x);
                conversation5.setLayoutY(mouseEvent.getSceneY() + dragDelta5.y);
            }
        });
        conversation5.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                conversation5.setCursor(Cursor.HAND);
            }
        });
        final Delta dragDelta6 = new Delta();
        stats_pane.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta6.x = stats_pane.getLayoutX() - mouseEvent.getSceneX();
                dragDelta6.y = stats_pane.getLayoutY() - mouseEvent.getSceneY();
                stats_pane.setCursor(Cursor.MOVE);
            }
        });
        stats_pane.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent mouseEvent) {
                stats_pane.setCursor(Cursor.HAND);
            }
        });
        stats_pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                stats_pane.setLayoutX(mouseEvent.getSceneX() + dragDelta6.x);
                stats_pane.setLayoutY(mouseEvent.getSceneY() + dragDelta6.y);
            }
        });
        stats_pane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                stats_pane.setCursor(Cursor.HAND);
            }
        });
        final Delta dragDelta7 = new Delta();
        conversation1.setOnTouchPressed(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                dragDelta7.x = conversation1.getLayoutX() - t.getTouchPoint().getSceneX();
                dragDelta7.y = conversation1.getLayoutY() - t.getTouchPoint().getSceneY();
           }
        });
        conversation1.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                conversation1.setLayoutX(t.getTouchPoint().getSceneX() + dragDelta7.x);
                conversation1.setLayoutY(t.getTouchPoint().getSceneY() + dragDelta7.y);
           }
        });
        final Delta dragDelta8 = new Delta();
        conversation2.setOnTouchPressed(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                dragDelta8.x = conversation2.getLayoutX() - t.getTouchPoint().getSceneX();
                dragDelta8.y = conversation2.getLayoutY() - t.getTouchPoint().getSceneY();
           }
        });
        conversation2.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                conversation2.setLayoutX(t.getTouchPoint().getSceneX() + dragDelta8.x);
                conversation2.setLayoutY(t.getTouchPoint().getSceneY() + dragDelta8.y);
           }
        });
        final Delta dragDelta9 = new Delta();
        conversation3.setOnTouchPressed(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                dragDelta9.x = conversation3.getLayoutX() - t.getTouchPoint().getSceneX();
                dragDelta9.y = conversation3.getLayoutY() - t.getTouchPoint().getSceneY();
           }
        });
        conversation3.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                conversation3.setLayoutX(t.getTouchPoint().getSceneX() + dragDelta9.x);
                conversation3.setLayoutY(t.getTouchPoint().getSceneY() + dragDelta9.y);
           }
        });
        final Delta dragDelta10 = new Delta();
        conversation4.setOnTouchPressed(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                dragDelta10.x = conversation4.getLayoutX() - t.getTouchPoint().getSceneX();
                dragDelta10.y = conversation4.getLayoutY() - t.getTouchPoint().getSceneY();
           }
        });
        conversation4.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                conversation4.setLayoutX(t.getTouchPoint().getSceneX() + dragDelta10.x);
                conversation4.setLayoutY(t.getTouchPoint().getSceneY() + dragDelta10.y);
           }
        });
        final Delta dragDelta11 = new Delta();
        conversation5.setOnTouchPressed(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                dragDelta11.x = conversation5.getLayoutX() - t.getTouchPoint().getSceneX();
                dragDelta11.y = conversation5.getLayoutY() - t.getTouchPoint().getSceneY();
           }
        });
        conversation5.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                conversation5.setLayoutX(t.getTouchPoint().getSceneX() + dragDelta11.x);
                conversation5.setLayoutY(t.getTouchPoint().getSceneY() + dragDelta11.y);
           }
        });
        final Delta dragDelta12 = new Delta();
        stats_pane.setOnTouchPressed(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                dragDelta12.x = stats_pane.getLayoutX() - t.getTouchPoint().getSceneX();
                dragDelta12.y = stats_pane.getLayoutY() - t.getTouchPoint().getSceneY();
           }
        });
        stats_pane.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                stats_pane.setLayoutX(t.getTouchPoint().getSceneX() + dragDelta12.x);
                stats_pane.setLayoutY(t.getTouchPoint().getSceneY() + dragDelta12.y);
           }
        });
        //TODO add code to rotate panes
        final RotateDelta rotatedelta1 = new RotateDelta();
        conversation1.setOnRotationStarted(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               rotatedelta1.theta = conversation1.getRotate();
           }
       });
        conversation1.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               conversation1.setRotate(t.getAngle()+rotatedelta1.theta);
           }
       });
        final RotateDelta rotatedelta2 = new RotateDelta();
        conversation2.setOnRotationStarted(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               rotatedelta2.theta = conversation2.getRotate();
           }
       });
        conversation2.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               conversation2.setRotate(t.getAngle()+rotatedelta2.theta);
           }
       });
       final RotateDelta rotatedelta3 = new RotateDelta();
        conversation3.setOnRotationStarted(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               rotatedelta3.theta = conversation3.getRotate();
           }
       });
        conversation3.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               conversation3.setRotate(t.getAngle()+rotatedelta3.theta);
           }
       });
       final RotateDelta rotatedelta4 = new RotateDelta();
        conversation4.setOnRotationStarted(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               rotatedelta4.theta = conversation4.getRotate();
           }
       });
        conversation4.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               conversation4.setRotate(t.getAngle()+rotatedelta4.theta);
           }
       });
       final RotateDelta rotatedelta5 = new RotateDelta();
        conversation5.setOnRotationStarted(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               rotatedelta5.theta = conversation5.getRotate();
           }
       });
        conversation5.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               conversation5.setRotate(t.getAngle()+rotatedelta5.theta);
           }
       });
       final RotateDelta rotatedelta6 = new RotateDelta();
        stats_pane.setOnRotationStarted(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               rotatedelta6.theta = stats_pane.getRotate();
           }
       });
        stats_pane.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               stats_pane.setRotate(t.getAngle()+rotatedelta6.theta);
           }
       });
    }
    
    private void addConversation1(String name,int conversationID){
        idtopane.put(new Integer(conversationID), new Integer(1));
        conversation1_name.setText(name);
        messages1.clear();
    }
    private void addConversation2(String name,int conversationID){
        idtopane.put(new Integer(conversationID), new Integer(2));
        conversation2_name.setText(name);
        messages1.clear();
    }
    private void addConversation3(String name,int conversationID){
        idtopane.put(new Integer(conversationID), new Integer(3));
        conversation3_name.setText(name);
        messages1.clear();
    }
    private void addConversation4(String name,int conversationID){
        idtopane.put(new Integer(conversationID), new Integer(4));
        conversation4_name.setText(name);
        messages1.clear();
    }
    private void addConversation5(String name,int conversationID){
        idtopane.put(new Integer(conversationID), new Integer(5));
        conversation5_name.setText(name);
        messages1.clear();
    }
    
    public boolean initConversations(String name1,int conversationID1,String name2,int conversationID2,String name3,int conversationID3,String name4,int conversationID4,String name5,int conversationID5){
        if (!idtopane.isEmpty()){return false;}
        addConversation1(name1,conversationID1);
        addConversation2(name2,conversationID2);
        addConversation3(name3,conversationID3);
        addConversation4(name4,conversationID4);
        addConversation5(name5,conversationID5);
        return true;
    }
    
    
    /**
     * @param conversationID1 the conversation to be removed
     * @param name the name of the conversation that will replace it
     * @param conversationID2 the conversation to be added
     * @throws NoMessageListException an exception that should never be thrown and means that a non-supported conversation pane has been used (this should have already been stopped by this point)
     * @throws uk.ac.cam.echo.TouchClient.NotCurrentConversationException an exception that is thrown if the conversation to be replaced is not currently displayed on screen
     * @throws uk.ac.cam.echo.TouchClient.ConversationAlredyDisplayedException an exception thrown if the conversation you tried to add is already shown on the screen
     */
    public void replaceConversation(int conversationID1,String name,int conversationID2) throws NoMessageListException, NotCurrentConversationException, ConversationAlredyDisplayedException{
        int pane = 10;
        try {//this NullPointerException ocurs if the item is not in the map
            pane = idtopane.get(conversationID1);
        } catch (NullPointerException e){
            throw new NotCurrentConversationException(conversationID1);
        }
        if (idtopane.containsValue(conversationID2)){
            throw new ConversationAlredyDisplayedException();
        }
        idtopane.remove(conversationID1);
        switch (pane){
            case 1:addConversation1(name,conversationID2);break;
            case 2:addConversation2(name,conversationID2);break;
            case 3:addConversation3(name,conversationID2);break;
            case 4:addConversation4(name,conversationID2);break;
            case 5:addConversation5(name,conversationID2);break;
            default:/*should never happen*/ throw new NoMessageListException();
        }
    }
    
    /**
     * Initialises the controller class.
     * @param url currently unused
     * @param rb currently unused
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            init();
            MessageTest test = new MessageTest(this);
            (new Thread(test)).start();
    }    
    
    /**
    *@param s String to display in final formated form 
    * @param ConversationID the id of the conversation the message is from
    * @exception NoMessageListException happens if pane is not a number between 1 and 5
    */
    public void displayMessage(String s,int ConversationID) throws NoMessageListException{
        int pane = 10;
        try{//this NullPointerException ocurs if the item requested is not in the map
            pane = idtopane.get(ConversationID).intValue();
        } catch(NullPointerException e) {throw new NoMessageListException();}
        
        switch (pane){
            case 1: addMessage1(s);break;
            case 2: addMessage2(s);break;
            case 3: addMessage3(s);break;
            case 4: addMessage4(s);break;
            case 5: addMessage5(s);break;
            default: throw new NoMessageListException();
        }          
    }
    
    public boolean scrollToEnd(int pane){
        switch (pane){
            case 1: conversation1_messages.scrollTo(messages1.size()-1);return true;
            case 2: conversation2_messages.scrollTo(messages2.size()-1);return true;
            case 3: conversation3_messages.scrollTo(messages3.size()-1);return true;
            case 4: conversation4_messages.scrollTo(messages4.size()-1);return true;
            case 5: conversation5_messages.scrollTo(messages5.size()-1);return true;
            default: return false;
        }
    }
    
}
