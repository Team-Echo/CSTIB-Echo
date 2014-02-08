package uk.ac.cam.echo.TouchClient;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
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
    
    //@FXML tag indicates that the veriable has been injected from the FXML code
    
    @FXML
    private ListView conversation1_messages;
    private ObservableList<String> messages1;
    @FXML
    private ListView conversation2_messages;
    private ObservableList<String> messages2;
    @FXML
    private ListView conversation3_messages;
    private ObservableList<String> messages3;
    @FXML
    private ListView conversation4_messages;
    private ObservableList<String> messages4;
    @FXML
    private ListView conversation5_messages;
    private ObservableList<String> messages5;
    
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
    
    //a hash map from the conversation id tot eh pane that it is displaed in
    private HashMap<Long,Integer> idtopane;
    
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
    
    /**
     * a function to setup all the event handlers for coversation1
     */
    private void setupConversationPane1(){
        final Delta dragDeltaMouse = new Delta();
        conversation1.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDeltaMouse.x = conversation1.getLayoutX() - mouseEvent.getSceneX();
                dragDeltaMouse.y = conversation1.getLayoutY() - mouseEvent.getSceneY();
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
                conversation1.setLayoutX(mouseEvent.getSceneX() + dragDeltaMouse.x);
                conversation1.setLayoutY(mouseEvent.getSceneY() + dragDeltaMouse.y);
            }
        });
        conversation1.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                conversation1.setCursor(Cursor.HAND);
            }
        });
        final Delta dragDeltaTouch = new Delta();
        conversation1.setOnTouchPressed(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.pressed){t.consume();return;}
                dragDeltaTouch.pressed = true;
                dragDeltaTouch.id = t.getTouchPoint().getId();
                dragDeltaTouch.x = conversation1.getLayoutX() - t.getTouchPoint().getSceneX();
                dragDeltaTouch.y = conversation1.getLayoutY() - t.getTouchPoint().getSceneY();
                t.consume();
           }
        });
        conversation1.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.pressed && (dragDeltaTouch.id==t.getTouchPoint().getId())){
                    conversation1.setLayoutX(t.getTouchPoint().getSceneX() + dragDeltaTouch.x);
                    conversation1.setLayoutY(t.getTouchPoint().getSceneY() + dragDeltaTouch.y);
                }
                t.consume();
           }
        });
        conversation1.setOnTouchReleased(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.id==t.getTouchPoint().getId()){
                    dragDeltaTouch.pressed = false;
                }
                t.consume();
           }
       });
        final RotateDelta rotatedelta = new RotateDelta();
        conversation1.setOnRotationStarted(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               rotatedelta.theta = conversation1.getRotate();
               t.consume();
           }
       });
        conversation1.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               conversation1.setRotate(t.getAngle()+(rotatedelta.theta*10));
               t.consume();
           }
       });
    }
    
    /**
     * a function to setup all the event handlers for coversation2
     */
    private void setupConversationPane2(){
        final Delta dragDeltaMouse = new Delta();
        conversation2.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDeltaMouse.x = conversation2.getLayoutX() - mouseEvent.getSceneX();
                dragDeltaMouse.y = conversation2.getLayoutY() - mouseEvent.getSceneY();
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
                conversation2.setLayoutX(mouseEvent.getSceneX() + dragDeltaMouse.x);
                conversation2.setLayoutY(mouseEvent.getSceneY() + dragDeltaMouse.y);
            }
        });
        conversation2.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                conversation2.setCursor(Cursor.HAND);
            }
        });
        final Delta dragDeltaTouch = new Delta();
        conversation2.setOnTouchPressed(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.pressed){t.consume();return;}
                dragDeltaTouch.pressed = true;
                dragDeltaTouch.id = t.getTouchPoint().getId();
                dragDeltaTouch.x = conversation2.getLayoutX() - t.getTouchPoint().getSceneX();
                dragDeltaTouch.y = conversation2.getLayoutY() - t.getTouchPoint().getSceneY();
                t.consume();
           }
        });
        conversation2.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.pressed && (dragDeltaTouch.id==t.getTouchPoint().getId())){
                    conversation2.setLayoutX(t.getTouchPoint().getSceneX() + dragDeltaTouch.x);
                    conversation2.setLayoutY(t.getTouchPoint().getSceneY() + dragDeltaTouch.y);
                }
                t.consume();
           }
        });
        conversation2.setOnTouchReleased(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.id==t.getTouchPoint().getId()){
                    dragDeltaTouch.pressed = false;
                }
                t.consume();
           }
       });
        final RotateDelta rotatedelta = new RotateDelta();
        conversation2.setOnRotationStarted(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               rotatedelta.theta = conversation2.getRotate();
               t.consume();
           }
       });
        conversation2.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               conversation2.setRotate(t.getAngle()+(rotatedelta.theta*10));
               t.consume();
           }
       });
    }
    
    /**
     * a function to setup all the event handlers for coversation3
     */
    private void setupConversationPane3(){
        final Delta dragDeltaMouse = new Delta();
        conversation3.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDeltaMouse.x = conversation3.getLayoutX() - mouseEvent.getSceneX();
                dragDeltaMouse.y = conversation3.getLayoutY() - mouseEvent.getSceneY();
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
                conversation3.setLayoutX(mouseEvent.getSceneX() + dragDeltaMouse.x);
                conversation3.setLayoutY(mouseEvent.getSceneY() + dragDeltaMouse.y);
            }
        });
        conversation3.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                conversation3.setCursor(Cursor.HAND);
            }
        });
        final Delta dragDeltaTouch = new Delta();
        conversation3.setOnTouchPressed(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.pressed){t.consume();return;}
                dragDeltaTouch.pressed = true;
                dragDeltaTouch.id = t.getTouchPoint().getId();
                dragDeltaTouch.x = conversation3.getLayoutX() - t.getTouchPoint().getSceneX();
                dragDeltaTouch.y = conversation3.getLayoutY() - t.getTouchPoint().getSceneY();
                t.consume();
           }
        });
        conversation3.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.pressed && (dragDeltaTouch.id==t.getTouchPoint().getId())){
                    conversation3.setLayoutX(t.getTouchPoint().getSceneX() + dragDeltaTouch.x);
                    conversation3.setLayoutY(t.getTouchPoint().getSceneY() + dragDeltaTouch.y);
                }
                t.consume();
           }
        });
        conversation3.setOnTouchReleased(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.id==t.getTouchPoint().getId()){
                    dragDeltaTouch.pressed = false;
                }
                t.consume();
           }
       });
        final RotateDelta rotatedelta = new RotateDelta();
        conversation3.setOnRotationStarted(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               rotatedelta.theta = conversation3.getRotate();
               t.consume();
           }
       });
        conversation3.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               conversation3.setRotate(t.getAngle()+(rotatedelta.theta*10));
               t.consume();
           }
       });
    }
    
    /**
     * a function to setup all the event handlers for coversation4
     */
    private void setupConversationPane4(){
        final Delta dragDeltaMouse = new Delta();
        conversation4.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDeltaMouse.x = conversation4.getLayoutX() - mouseEvent.getSceneX();
                dragDeltaMouse.y = conversation4.getLayoutY() - mouseEvent.getSceneY();
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
                conversation4.setLayoutX(mouseEvent.getSceneX() + dragDeltaMouse.x);
                conversation4.setLayoutY(mouseEvent.getSceneY() + dragDeltaMouse.y);
            }
        });
        conversation4.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                conversation4.setCursor(Cursor.HAND);
            }
        });
        final Delta dragDeltaTouch = new Delta();
        conversation4.setOnTouchPressed(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.pressed){t.consume();return;}
                dragDeltaTouch.pressed = true;
                dragDeltaTouch.id = t.getTouchPoint().getId();
                dragDeltaTouch.x = conversation4.getLayoutX() - t.getTouchPoint().getSceneX();
                dragDeltaTouch.y = conversation4.getLayoutY() - t.getTouchPoint().getSceneY();
                t.consume();
           }
        });
        conversation4.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.pressed && (dragDeltaTouch.id==t.getTouchPoint().getId())){
                    conversation4.setLayoutX(t.getTouchPoint().getSceneX() + dragDeltaTouch.x);
                    conversation4.setLayoutY(t.getTouchPoint().getSceneY() + dragDeltaTouch.y);
                }
                t.consume();
           }
        });
        conversation4.setOnTouchReleased(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.id==t.getTouchPoint().getId()){
                    dragDeltaTouch.pressed = false;
                }
                t.consume();
           }
       });
        final RotateDelta rotatedelta = new RotateDelta();
        conversation4.setOnRotationStarted(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               rotatedelta.theta = conversation4.getRotate();
               t.consume();
           }
       });
        conversation4.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               conversation4.setRotate(t.getAngle()+(rotatedelta.theta*10));
               t.consume();
           }
       });
    }
    
    /**
     * a function to setup all the event handlers for coversation5
     */
    private void setupConversationPane5(){
        final Delta dragDeltaMouse = new Delta();
        conversation5.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDeltaMouse.x = conversation5.getLayoutX() - mouseEvent.getSceneX();
                dragDeltaMouse.y = conversation5.getLayoutY() - mouseEvent.getSceneY();
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
                conversation5.setLayoutX(mouseEvent.getSceneX() + dragDeltaMouse.x);
                conversation5.setLayoutY(mouseEvent.getSceneY() + dragDeltaMouse.y);
            }
        });
        conversation5.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                conversation5.setCursor(Cursor.HAND);
            }
        });
        final Delta dragDeltaTouch = new Delta();
        conversation5.setOnTouchPressed(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.pressed){t.consume();return;}
                dragDeltaTouch.pressed = true;
                dragDeltaTouch.id = t.getTouchPoint().getId();
                dragDeltaTouch.x = conversation5.getLayoutX() - t.getTouchPoint().getSceneX();
                dragDeltaTouch.y = conversation5.getLayoutY() - t.getTouchPoint().getSceneY();
                t.consume();
           }
        });
        conversation5.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.pressed && (dragDeltaTouch.id==t.getTouchPoint().getId())){
                    conversation5.setLayoutX(t.getTouchPoint().getSceneX() + dragDeltaTouch.x);
                    conversation5.setLayoutY(t.getTouchPoint().getSceneY() + dragDeltaTouch.y);
                }
                t.consume();
           }
        });
        conversation5.setOnTouchReleased(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.id==t.getTouchPoint().getId()){
                    dragDeltaTouch.pressed = false;
                }
                t.consume();
           }
       });
        final RotateDelta rotatedelta = new RotateDelta();
        conversation5.setOnRotationStarted(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               rotatedelta.theta = conversation5.getRotate();
               t.consume();
           }
       });
        conversation5.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               conversation5.setRotate(t.getAngle()+(rotatedelta.theta*10));
               t.consume();
           }
       });
    }
    
    /**
     * a function to setup all the event handlers for statspane
     */
    private void setupStatsPane(){
        final Delta dragDeltaMouse = new Delta();
        stats_pane.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDeltaMouse.x = stats_pane.getLayoutX() - mouseEvent.getSceneX();
                dragDeltaMouse.y = stats_pane.getLayoutY() - mouseEvent.getSceneY();
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
                stats_pane.setLayoutX(mouseEvent.getSceneX() + dragDeltaMouse.x);
                stats_pane.setLayoutY(mouseEvent.getSceneY() + dragDeltaMouse.y);
            }
        });
        stats_pane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                stats_pane.setCursor(Cursor.HAND);
            }
        });
        final Delta dragDeltaTouch = new Delta();
        stats_pane.setOnTouchPressed(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.pressed){t.consume();return;}
                dragDeltaTouch.pressed = true;
                dragDeltaTouch.id = t.getTouchPoint().getId();
                dragDeltaTouch.x = stats_pane.getLayoutX() - t.getTouchPoint().getSceneX();
                dragDeltaTouch.y = stats_pane.getLayoutY() - t.getTouchPoint().getSceneY();
                t.consume();
           }
        });
        stats_pane.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.pressed && (dragDeltaTouch.id==t.getTouchPoint().getId())){
                    stats_pane.setLayoutX(t.getTouchPoint().getSceneX() + dragDeltaTouch.x);
                    stats_pane.setLayoutY(t.getTouchPoint().getSceneY() + dragDeltaTouch.y);
                }
                t.consume();
           }
        });
        stats_pane.setOnTouchReleased(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.id==t.getTouchPoint().getId()){
                    dragDeltaTouch.pressed = false;
                }
                t.consume();
           }
       });
        final RotateDelta rotatedelta = new RotateDelta();
        stats_pane.setOnRotationStarted(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               rotatedelta.theta = stats_pane.getRotate();
               t.consume();
           }
       });
        stats_pane.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
               stats_pane.setRotate(t.getAngle()+(rotatedelta.theta*10));
               t.consume();
           }
       });
    }
    
    /**
     * a function to initialize the controller and all the event handlers ect...
     */
    private void init(){
       messages1 = new MessageDisplayList();
       messages2 = new MessageDisplayList();
       messages3 = new MessageDisplayList();
       messages4 = new MessageDisplayList();
       messages5 = new MessageDisplayList();
       idtopane = new HashMap();
       
       setupConversationPane1();
       setupConversationPane2();
       setupConversationPane3();
       setupConversationPane4();
       setupConversationPane5();
       setupStatsPane();

    }
    
    private void addConversation1(String name,long conversationID){
        idtopane.put(new Long(conversationID), new Integer(1));
        boolean retry =true;
        conversation1_name.setText(name);
        messages1.clear();
    }
    private void addConversation2(String name,long conversationID){
        idtopane.put(new Long(conversationID), new Integer(2));
        conversation2_name.setText(name);
        messages1.clear();
    }
    private void addConversation3(String name,long conversationID){
        idtopane.put(new Long(conversationID), new Integer(3));
        conversation3_name.setText(name);
        messages1.clear();
    }
    private void addConversation4(String name,long conversationID){
        idtopane.put(new Long(conversationID), new Integer(4));
        conversation4_name.setText(name);
        messages1.clear();
    }
    private void addConversation5(String name,long conversationID){
        idtopane.put(new Long(conversationID), new Integer(5));
        conversation5_name.setText(name);
        messages1.clear();
    }
    
    /**
     * 
     * this function initializes the conversation panes if they are not initialized and returns true if they are it returns false
     * 
     * @param name1 name of the first conversation
     * @param conversationID1 id of the first conversation
     * @param name2 name of the second conversation
     * @param conversationID2 id of the second conversation
     * @param name3 name of the third conversation
     * @param conversationID3 id of the third conversation
     * @param name4 name of the fourth conversation
     * @param conversationID4 id of the fourth conversation
     * @param name5 name of the fifth conversation
     * @param conversationID5 id of the fifth conversation
     * @return returns true if it has initialized the conversations false otherwise
     */
    public boolean initConversations(String name1,long conversationID1,String name2,long conversationID2,String name3,long conversationID3,String name4,long conversationID4,String name5,long conversationID5){
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
    public void replaceConversation(long conversationID1,String name,long conversationID2) throws NoMessageListException, NotCurrentConversationException, ConversationAlredyDisplayedException{
        int pane = 10;
        try {//this NullPointerException ocurs if the item is not in the map
            pane = idtopane.get(conversationID1);
        } catch (NullPointerException e){
            throw new NotCurrentConversationException(conversationID1);
        }
        if (idtopane.containsKey(conversationID2)){
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
            ECHOResource er;
            if (rb instanceof ECHOResource){
                er = (ECHOResource)rb;
                er.getTouchClient().setGUI(this);
            }else {System.err.println("the wrong resource type has been provided to the confrenceloadscreencontroller class a resource of type ECHOResource must be provided");System.exit(1);}
            init();
    }    
    
    /**
    *@param s String to display in final formated form 
    * @param ConversationID the id of the conversation the message is from
    * @exception NoMessageListException happens if pane is not a number between 1 and 5
    */
    public void displayMessage(String s,long ConversationID) throws NoMessageListException{
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
    
    /**
     * @deprecated
     * 
     * currently dosen't work as the scrollto(int) command it uses just clears the list.
     * 
     * @param pane which pane you want to scroll to the bottom
     * @return 
     */
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
