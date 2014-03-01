package uk.ac.cam.echo.TouchClient;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import uk.ac.cam.echo.TouchClient.ConfrenceStats.Tuple;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;

/**
 * FXML Controller class
 *
 * @author Philip
 */
public class GUIController implements Initializable {
    
    private final int POLLDELAY = 60000;
    
    //@FXML tag indicates that the veriable has been injected from the FXML code
    
    @FXML
    private ListView conversation1_messages;
    private ObservableList<Message> messages1;
    @FXML
    private ListView conversation2_messages;
    private ObservableList<Message> messages2;
    @FXML
    private ListView conversation3_messages;
    private ObservableList<Message> messages3;
    @FXML
    private ListView conversation4_messages;
    private ObservableList<Message> messages4;
    @FXML
    private ListView conversation5_messages;
    private ObservableList<Message> messages5;
    
    @FXML
    private ImageView conversation1_QR;
    @FXML
    private ImageView conversation1_code_large;
    @FXML
    private ImageView conversation2_QR;
    @FXML
    private ImageView conversation2_code_large;
    @FXML
    private ImageView conversation3_QR;
    @FXML
    private ImageView conversation3_code_large;
    @FXML
    private ImageView conversation4_QR;
    @FXML
    private ImageView conversation4_code_large;
    @FXML
    private ImageView conversation5_QR;
    @FXML
    private ImageView conversation5_code_large;
    
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
    
    @FXML private PieChart global_stats_pie;
    private ObservableList<PieChart.Data> global_pie;
    @FXML private LineChart global_stats_line;
    private XYChart.Series<Number,Number> global_line;
    @FXML private ListView stats_conversationlist;
    private ObservableList<Conversation> conversationList;
    
    private TouchClient mTC;
    
    //a hash map from the conversation id to the pane that it is displaed in
    private HashMap<Long,Integer> idtopane = new HashMap();
    
    public boolean getIsMapEmpty(){
        boolean out;
        synchronized (idtopane){
            out = idtopane.isEmpty();
        }
        return out;
    }
    
    private void addMessage1(Message mess){
        messages1.add(mess);
        conversation1_messages.setItems(messages1);
    }
    private void addMessage2(Message mess){
        messages2.add(mess);
        conversation2_messages.setItems(messages2);
    }
    private void addMessage3(Message mess){
        messages3.add(mess);
        conversation3_messages.setItems(messages3);
    }   
    private void addMessage4(Message mess){
        messages4.add(mess);
        conversation4_messages.setItems(messages4);
    }   
    private void addMessage5(Message mess){
        messages5.add(mess);
        conversation5_messages.setItems(messages5);
    }
    
    @FXML Text pie_chart_lable;
    @FXML Button return_button;
    @FXML Button Activity_button;
    @FXML Button ConversationList_button;
    @FXML Button messageBreakdown_button;
    @FXML Text ECHO;
    @FXML Text Confrence_Name;
    @FXML WebView htmlviewer;
    @FXML WebView webviewtwo;
    @FXML Button webview_button;
    private void setupGlobalStats(){
        return_button.setVisible(false);
        stats_conversationlist.setVisible(false);
        global_stats_pie.setVisible(false);
        global_stats_line.setVisible(false);
        htmlviewer.setVisible(false);
        webviewtwo.setVisible(false);
        (new Thread(new Runnable(){
            @Override
            public void run() {
                boolean notfound = true;
                while(notfound){
                    try{
                    Confrence_Name.setText(mTC.getServerConnection().getConfrenceName());
                    ((Stage)Confrence_Name.getScene().getWindow()).setTitle(mTC.getServerConnection().getConfrenceName());
                    }catch (NullPointerException e){
                        continue;
                    }
                    notfound = false;
                }
            }
        })).start();
        
        try {
            ImageView rbi = (new ImageView());
            rbi.setImage(new Image(new FileInputStream(new File("./res/drawable/menusmall.png"))));
            return_button.setGraphic(rbi);
            return_button.setText("");
        } catch (FileNotFoundException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
        
        try {
            ImageView abi = (new ImageView());
            abi.setImage(new Image(new FileInputStream(new File("./res/drawable/linegraphsmall.png"))));
            Activity_button.setGraphic(abi);
            Activity_button.setText("");
        } catch (FileNotFoundException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
        
        try {
            ImageView mlbi = (new ImageView());
            mlbi.setImage(new Image(new FileInputStream(new File("./res/drawable/edgebundlesmall.png"))));
            ConversationList_button.setGraphic(mlbi);
            ConversationList_button.setText("");
        } catch (FileNotFoundException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
        
        try {
            ImageView mbbi = (new ImageView());
            mbbi.setImage(new Image(new FileInputStream(new File("./res/drawable/piechartsmall.png"))));
            messageBreakdown_button.setGraphic(mbbi);
            messageBreakdown_button.setText("");
        } catch (FileNotFoundException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
        
        try {
            ImageView mbbi = (new ImageView());
            mbbi.setImage(new Image(new FileInputStream(new File("./res/drawable/connectionssmall.png"))));
            webview_button.setGraphic(mbbi);
            webview_button.setText("");
        } catch (FileNotFoundException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
        
        Activity_button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                if (Activity_button.visibleProperty().get()){
                    return_button.setVisible(true);
                    stats_conversationlist.setVisible(false);
                    global_stats_pie.setVisible(false);
                    global_stats_line.setVisible(true);
                    Activity_button.setVisible(false);
                    ConversationList_button.setVisible(false);
                    messageBreakdown_button.setVisible(false);
                    ECHO.setVisible(false);
                    Confrence_Name.setVisible(false);
                    htmlviewer.setVisible(false);
                    webviewtwo.setVisible(false);
                    webview_button.setVisible(false);
                }
            }
        });
        
        ConversationList_button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                if (ConversationList_button.visibleProperty().get()){
                    return_button.setVisible(true);
                    stats_conversationlist.setVisible(false);
                    global_stats_pie.setVisible(false);
                    global_stats_line.setVisible(false);
                    Activity_button.setVisible(false);
                    ConversationList_button.setVisible(false);
                    messageBreakdown_button.setVisible(false);
                    ECHO.setVisible(false);
                    Confrence_Name.setVisible(false);
                    htmlviewer.setVisible(true);
                    webviewtwo.setVisible(false);
                    webview_button.setVisible(false);
                }
            }
        });
        
        messageBreakdown_button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                if (messageBreakdown_button.visibleProperty().get()){
                    return_button.setVisible(true);
                    stats_conversationlist.setVisible(false);
                    global_stats_pie.setVisible(true);
                    global_stats_line.setVisible(false);
                    Activity_button.setVisible(false);
                    ConversationList_button.setVisible(false);
                    messageBreakdown_button.setVisible(false);
                    ECHO.setVisible(false);
                    Confrence_Name.setVisible(false);
                    htmlviewer.setVisible(false);
                    webviewtwo.setVisible(false);
                    webview_button.setVisible(false);
                }
            }
        });
        
        return_button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                if (return_button.visibleProperty().get()){
                    return_button.setVisible(false);
                    stats_conversationlist.setVisible(false);
                    global_stats_pie.setVisible(false);
                    global_stats_line.setVisible(false);
                    Activity_button.setVisible(true);
                    ConversationList_button.setVisible(true);
                    messageBreakdown_button.setVisible(true);
                    ECHO.setVisible(true);
                    Confrence_Name.setVisible(true);
                    htmlviewer.setVisible(false);
                    webviewtwo.setVisible(false);
                    webview_button.setVisible(true);
                }
            }
        });
        
        webview_button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                if (webview_button.visibleProperty().get()){
                    return_button.setVisible(true);
                    stats_conversationlist.setVisible(false);
                    global_stats_pie.setVisible(false);
                    global_stats_line.setVisible(false);
                    Activity_button.setVisible(false);
                    ConversationList_button.setVisible(false);
                    messageBreakdown_button.setVisible(false);
                    ECHO.setVisible(false);
                    Confrence_Name.setVisible(false);
                    htmlviewer.setVisible(false);
                    webviewtwo.setVisible(true);
                    webview_button.setVisible(false);
                }
            }
        });
        
        
        conversationList = new MessageDisplayList();
        stats_conversationlist.setCellFactory(new ConversationListCellFactory());
        
        global_pie = new MessageDisplayList();
        global_stats_pie.setLegendSide(Side.LEFT);
        global_stats_pie.setLabelLineLength(5);
        global_stats_pie.setLegendVisible(false);
        
        global_stats_pie.setLabelsVisible(true);

        global_line = new XYChart.Series();
        global_line.setName("Activity");
        global_stats_line.getData().add(global_line);
        global_stats_line.setLegendVisible(false);
        
        try {
            htmlviewer.getEngine().load(new File("./res/tags/index.html").toURI().toURL().toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            webviewtwo.getEngine().load(new File("./res/overview/index.html").toURI().toURL().toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * a function to setup all the event handlers for coversation1
     */
    private void setupConversationPane1(){
        conversation1_name.setWrapText(true);
        conversation1_messages.setCellFactory(new messageCellFactory());
        final Delta dragDeltaMouse = new Delta();
        conversation1.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                conversation1.toFront();
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
                if (dragDeltaTouch.testAndPress()){
                    dragDeltaTouch.time = System.currentTimeMillis();
                    conversation1.toFront();
                    dragDeltaTouch.id = t.getTouchPoint().getId();
                    dragDeltaTouch.x = conversation1.getLayoutX() - t.getTouchPoint().getSceneX();
                    dragDeltaTouch.y = conversation1.getLayoutY() - t.getTouchPoint().getSceneY();
                }
                t.consume();
           }
        });
        conversation1.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.isPressed() && (dragDeltaTouch.id==t.getTouchPoint().getId())){
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
                    dragDeltaTouch.unPress();
                }
                t.consume();
           }
       });
        conversation1.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
                conversation1.setRotate(conversation1.getRotate() + t.getAngle());
            t.consume();
            }
       });
    }
    
    /**
     * a function to setup all the event handlers for coversation2
     */
    private void setupConversationPane2(){
        conversation2_name.setWrapText(true);
        conversation2_messages.setCellFactory(new messageCellFactory());
        final Delta dragDeltaMouse = new Delta();
        conversation2.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                conversation2.toFront();
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
                if (dragDeltaTouch.testAndPress()){
                    dragDeltaTouch.time = System.currentTimeMillis();
                    conversation2.toFront();
                    dragDeltaTouch.id = t.getTouchPoint().getId();
                    dragDeltaTouch.x = conversation2.getLayoutX() - t.getTouchPoint().getSceneX();
                    dragDeltaTouch.y = conversation2.getLayoutY() - t.getTouchPoint().getSceneY();
                }
                t.consume();
           }
        });
        conversation2.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.isPressed() && (dragDeltaTouch.id==t.getTouchPoint().getId())){
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
                    dragDeltaTouch.unPress();
                }
                t.consume();
           }
       });
        conversation2.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
                conversation2.setRotate(conversation2.getRotate() + t.getAngle());
            t.consume();
            }
       });
    }
    
    /**
     * a function to setup all the event handlers for coversation3
     */
    private void setupConversationPane3(){
        conversation3_name.setWrapText(true);
        conversation3_messages.setCellFactory(new messageCellFactory());
        final Delta dragDeltaMouse = new Delta();
        conversation3.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                conversation3.toFront();
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
                if (dragDeltaTouch.testAndPress()){
                    dragDeltaTouch.time = System.currentTimeMillis();
                    conversation3.toFront();
                    dragDeltaTouch.id = t.getTouchPoint().getId();
                    dragDeltaTouch.x = conversation3.getLayoutX() - t.getTouchPoint().getSceneX();
                    dragDeltaTouch.y = conversation3.getLayoutY() - t.getTouchPoint().getSceneY();
                }
                t.consume();
           }
        });
        conversation3.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.isPressed() && (dragDeltaTouch.id==t.getTouchPoint().getId())){
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
                    dragDeltaTouch.unPress();
                }
                t.consume();
           }
       });
        conversation3.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
                conversation3.setRotate(conversation3.getRotate() + t.getAngle());
            t.consume();
            }
       });
    }
    
    /**
     * a function to setup all the event handlers for coversation4
     */
    private void setupConversationPane4(){
        conversation4_name.setWrapText(true);
        conversation4_messages.setCellFactory(new messageCellFactory());
        final Delta dragDeltaMouse = new Delta();
        conversation4.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                conversation4.toFront();
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
                if (dragDeltaTouch.testAndPress()){
                    dragDeltaTouch.time = System.currentTimeMillis();
                    conversation4.toFront();
                    dragDeltaTouch.id = t.getTouchPoint().getId();
                    dragDeltaTouch.x = conversation4.getLayoutX() - t.getTouchPoint().getSceneX();
                    dragDeltaTouch.y = conversation4.getLayoutY() - t.getTouchPoint().getSceneY();
                }
                t.consume();
           }
        });
        conversation4.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.isPressed() && (dragDeltaTouch.id==t.getTouchPoint().getId())){
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
                    dragDeltaTouch.unPress();
                }
                t.consume();
           }
       });
        conversation4.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
                conversation4.setRotate(conversation4.getRotate() + t.getAngle());
            t.consume();
            }
       });
    }
    
    /**
     * a function to setup all the event handlers for coversation5
     */
    private void setupConversationPane5(){
        conversation5_name.setWrapText(true);
        conversation5_messages.setCellFactory(new messageCellFactory());
        final Delta dragDeltaMouse = new Delta();
        conversation5.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override 
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                conversation5.toFront();
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
                if (dragDeltaTouch.testAndPress()){
                    dragDeltaTouch.time = System.currentTimeMillis();
                    conversation5.toFront();
                    dragDeltaTouch.id = t.getTouchPoint().getId();
                    dragDeltaTouch.x = conversation5.getLayoutX() - t.getTouchPoint().getSceneX();
                    dragDeltaTouch.y = conversation5.getLayoutY() - t.getTouchPoint().getSceneY();
                }
                t.consume();
           }
        });
        conversation5.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.isPressed() && (dragDeltaTouch.id==t.getTouchPoint().getId())){
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
                    dragDeltaTouch.unPress();
                }
                t.consume();
           }
       });
        conversation5.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
                conversation5.setRotate(conversation5.getRotate() + t.getAngle());
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
                stats_pane.toFront();
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
                if (dragDeltaTouch.testAndPress()){
                    dragDeltaTouch.time = System.currentTimeMillis();
                    stats_pane.toFront();
                    dragDeltaTouch.id = t.getTouchPoint().getId();
                    dragDeltaTouch.x = stats_pane.getLayoutX() - t.getTouchPoint().getSceneX();
                    dragDeltaTouch.y = stats_pane.getLayoutY() - t.getTouchPoint().getSceneY();
                }
                t.consume();
           }
        });
        stats_pane.setOnTouchMoved(new EventHandler<TouchEvent>(){
           @Override
           public void handle(TouchEvent t) {
                if (dragDeltaTouch.isPressed() && (dragDeltaTouch.id==t.getTouchPoint().getId())){
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
                    dragDeltaTouch.unPress();
                }
                t.consume();
           }
       });
        stats_pane.setOnRotate(new EventHandler<RotateEvent>() {
           @Override
           public void handle(RotateEvent t) {
                stats_pane.setRotate(stats_pane.getRotate() + t.getAngle());
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
       
       setupConversationPane1();
       setupConversationPane2();
       setupConversationPane3();
       setupConversationPane4();
       setupConversationPane5();
       setupStatsPane();
       setupGlobalStats();

    }
    
    private void addConversation1(final String name,final long conversationID){
        setStatsConv1(mTC.getServerConnection().getStats(conversationID));
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                messages1.clear();
                synchronized (idtopane){
                    idtopane.put(new Long(conversationID), new Integer(1));
                }
                conversation1_name.setText(name);
                conversation1_QR.setImage(genarateQRCode(conversationID,(int)(conversation1_QR.getFitHeight())*3));
                conversation1_code_large.setImage(genarateQRCode(conversationID,(int)(conversation1_code_large.getFitHeight()-1)));
            }
        });
        
    }
    private void addConversation2(final String name,final long conversationID){
        setStatsConv2(mTC.getServerConnection().getStats(conversationID));
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                messages2.clear();
                synchronized (idtopane){
                    idtopane.put(new Long(conversationID), new Integer(2));
                }
                conversation2_name.setText(name);
                conversation2_QR.setImage(genarateQRCode(conversationID,(int)(conversation2_QR.getFitHeight())*3));
                conversation2_code_large.setImage(genarateQRCode(conversationID,(int)(conversation2_code_large.getFitHeight()-1)));
            }
        }); 
    }
    private void addConversation3(final String name,final long conversationID){
        setStatsConv3(mTC.getServerConnection().getStats(conversationID));
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                messages3.clear();
                synchronized (idtopane){
                    idtopane.put(new Long(conversationID), new Integer(3));
                }
                conversation3_name.setText(name);
                conversation3_QR.setImage(genarateQRCode(conversationID,(int)(conversation3_QR.getFitHeight())*3));
                conversation3_code_large.setImage(genarateQRCode(conversationID,(int)(conversation3_code_large.getFitHeight()-1)));
            }
        });   
    }
    private void addConversation4(final String name,final long conversationID){
        setStatsConv4(mTC.getServerConnection().getStats(conversationID));
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                messages4.clear();
                synchronized (idtopane){
                    idtopane.put(new Long(conversationID), new Integer(4));
                }
                conversation4_name.setText(name);
                conversation4_QR.setImage(genarateQRCode(conversationID,(int)(conversation4_QR.getFitHeight())*3));
                conversation4_code_large.setImage(genarateQRCode(conversationID,(int)(conversation4_code_large.getFitHeight()-1)));
            }
        });  
    }
    private void addConversation5(final String name,final long conversationID){
        setStatsConv5(mTC.getServerConnection().getStats(conversationID));
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                messages5.clear();
                synchronized (idtopane){
                    idtopane.put(new Long(conversationID), new Integer(5));
                }
                conversation5_name.setText(name);
                conversation5_QR.setImage(genarateQRCode(conversationID,(int)(conversation5_QR.getFitHeight())*3));
                conversation5_code_large.setImage(genarateQRCode(conversationID,(int)(conversation5_code_large.getFitHeight()-1)));
            }
        });
    }
    
    /**
     * this function initialises the conversation panes if they are not initialised and returns true if they are it returns false
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
     * @return returns true if it has initialised the conversations false otherwise
     */
    public boolean initConversations(String name1,long conversationID1,String name2,long conversationID2,String name3,long conversationID3,String name4,long conversationID4,String name5,long conversationID5){
        if (!idtopane.isEmpty()){return false;}
        synchronized (idtopane){
            addConversation1(name1,conversationID1);
            addConversation2(name2,conversationID2);
            addConversation3(name3,conversationID3);
            addConversation4(name4,conversationID4);
            addConversation5(name5,conversationID5);
        }
        pollStats();
        pollConvStats();
        setupUserLists();
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
        synchronized (idtopane){
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
                default:break;
            }
            scrollToEnd(conversationID2);
        }
    }
    
    /**
     * a method to create an QR Code of an conversation ID
     * 
     * @param conversationID the number to be converted into the QR Code
     * @param wandh size of the image both width and height
     * @return an image that represents the QRcode of the conversation
     */
    private static Image genarateQRCode(long conversationID,int wandh){
        BitMatrix bitMatrix;
        ByteArrayOutputStream image = new ByteArrayOutputStream();
        try {
            bitMatrix = new QRCodeWriter().encode(Long.toString(conversationID),BarcodeFormat.QR_CODE,wandh,wandh,null);
            
            MatrixToImageWriter.writeToStream(bitMatrix, "png", image);
        } catch (IOException | WriterException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
        
        return new Image(new ByteArrayInputStream(image.toByteArray()));
    }
    
    /**
     * Initialises the controller class.
     * @param url currently unused
     * @param rb this must be of type ECHOResource!!!
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            ECHOResource er;
            if (rb instanceof ECHOResource){
                er = (ECHOResource)rb;
                er.getTouchClient().setGUI(this);
                mTC = er.getTouchClient();
            }else {System.err.println("the wrong resource type has been provided to the confrenceloadscreencontroller class a resource of type ECHOResource must be provided");System.exit(1);}
            init();
            conversation1.getParent().setOnKeyPressed(new EventHandler<KeyEvent>(){
                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode().equals(KeyCode.F11)){
                        ((Stage)conversation1.getScene().getWindow()).setFullScreen(true);
                    }
                }
            });
    }    
    
    /**
    *@param s String to display in final formated form 
    * @param ConversationID the id of the conversation the message is from
    * @exception NoMessageListException happens if pane is not a number between 1 and 5
    */
    public void displayMessage(Message m,long ConversationID) throws NoMessageListException{
        int pane = 10;
        try{//this NullPointerException ocurs if the item requested is not in the map
            pane = idtopane.get(ConversationID).intValue();
        } catch(NullPointerException e) {throw new NoMessageListException();}
        synchronized (idtopane){
            switch (pane){
                case 1: addMessage1(m);break;
                case 2: addMessage2(m);break;
                case 3: addMessage3(m);break;
                case 4: addMessage4(m);break;
                case 5: addMessage5(m);break;
                default: break;
            }
        }
        scrollToEnd(ConversationID);
    }
    
    /**
     * this method may scroll to the end of a conversation and return true but it may not
     * 
     * @param conversationID the id of the conversation you want to scroll to the end
     * @return boolean representing if it has been scrolled or not
     */
    public boolean scrollToEnd(final long conversationID){
            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    int pane;
                    try{
                        pane = idtopane.get(conversationID);
                    } catch (NullPointerException e){return;/*do nothing as nothing can be done*/}
                    switch (pane){
                        case 1: conversation1_messages.scrollTo(messages1.size()-1);break;
                        case 2: conversation2_messages.scrollTo(messages2.size()-1);break;
                        case 3: conversation3_messages.scrollTo(messages3.size()-1);break;
                        case 4: conversation4_messages.scrollTo(messages4.size()-1);break;
                        case 5: conversation5_messages.scrollTo(messages5.size()-1);break;
                        default: break;
                    }
                }
            });
        return true; 
    }
    
    @FXML Label conversation1_stat_1;
    @FXML Label conversation1_stat_2;
    @FXML Label conversation1_stat_3;
    @FXML PieChart conversation1_stat_4;
    
    private void setStatsConv1(final ConvStats s){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                conversation1_stat_1.setText(s.getCurrentUsers());
                conversation1_stat_2.setText(s.getContributingUsers());
                conversation1_stat_3.setText(s.getNumberOfMessages());
                ObservableList<PieChart.Data> pieData = new MessageDisplayList();
                pieData.add(new PieChart.Data("Male",s.getMaleRatio()));
                pieData.add(new PieChart.Data("Female",s.getFemaleRatio()));
                conversation1_stat_4.setData(pieData);
            }
        });
    }
    
    @FXML Label conversation2_stat_1;
    @FXML Label conversation2_stat_2;
    @FXML Label conversation2_stat_3;
    @FXML PieChart conversation2_stat_4;
    
    private void setStatsConv2(final ConvStats s){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                conversation2_stat_1.setText(s.getCurrentUsers());
                conversation2_stat_2.setText(s.getContributingUsers());
                conversation2_stat_3.setText(s.getNumberOfMessages());
                ObservableList<PieChart.Data> pieData = new MessageDisplayList();
                pieData.add(new PieChart.Data("Male",s.getMaleRatio()));
                pieData.add(new PieChart.Data("Female",s.getFemaleRatio()));
                conversation2_stat_4.setData(pieData);            }
        });
    }
    
    @FXML Label conversation3_stat_1;
    @FXML Label conversation3_stat_2;
    @FXML Label conversation3_stat_3;
    @FXML PieChart conversation3_stat_4;
    
    private void setStatsConv3(final ConvStats s){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                conversation3_stat_1.setText(s.getCurrentUsers());
                conversation3_stat_2.setText(s.getContributingUsers());
                conversation3_stat_3.setText(s.getNumberOfMessages());
                ObservableList<PieChart.Data> pieData = new MessageDisplayList();
                pieData.add(new PieChart.Data("Male",s.getMaleRatio()));
                pieData.add(new PieChart.Data("Female",s.getFemaleRatio()));
                conversation3_stat_4.setData(pieData);            }
        });
    }
    
    @FXML Label conversation4_stat_1;
    @FXML Label conversation4_stat_2;
    @FXML Label conversation4_stat_3;
    @FXML PieChart conversation4_stat_4;
    
    private void setStatsConv4(final ConvStats s){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                conversation4_stat_1.setText(s.getCurrentUsers());
                conversation4_stat_2.setText(s.getContributingUsers());
                conversation4_stat_3.setText(s.getNumberOfMessages());
                ObservableList<PieChart.Data> pieData = new MessageDisplayList();
                pieData.add(new PieChart.Data("Male",s.getMaleRatio()));
                pieData.add(new PieChart.Data("Female",s.getFemaleRatio()));
                conversation4_stat_4.setData(pieData);
            }
        });
        
    }
    
    @FXML Label conversation5_stat_1;
    @FXML Label conversation5_stat_2;
    @FXML Label conversation5_stat_3;
    @FXML PieChart conversation5_stat_4;
    
    private void setStatsConv5(final ConvStats s){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                conversation5_stat_1.setText(s.getCurrentUsers());
                conversation5_stat_2.setText(s.getContributingUsers());
                conversation5_stat_3.setText(s.getNumberOfMessages());
                ObservableList<PieChart.Data> pieData = new MessageDisplayList();
                pieData.add(new PieChart.Data("Male",s.getMaleRatio()));
                pieData.add(new PieChart.Data("Female",s.getFemaleRatio()));
                conversation5_stat_4.setData(pieData);            }
        });
    }
    
    @FXML Label mCaption;
    
    private void setStatsGlobal(final ConfrenceStats s, final long val){
        if (s==null){return;}
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                global_pie.clear();
                for (Tuple<String,Long> t: s.getUsersInConv()){
                    global_pie.add(new PieChart.Data(t.getVal1(), mTC.getServerConnection().getNumberOfMessages(t.getVal2())));
                }
                global_stats_pie.setData(global_pie);
                for (Node node : global_stats_pie.lookupAll(".text.chart-pie-label")) {
                    if (node instanceof Text){
                        for (PieChart.Data data : global_pie){ 
                            if (data.getName().equals(((Text) node).getText())){
                                final String name = data.getName();
                                ((Text) node).setText(String.format("%s", name));
                            }
                        }
                    }
                }
                
                conversationList.clear();
                conversationList.addAll(s.getConversations());
                stats_conversationlist.setItems(conversationList);
                
                if (val%5 == 0){
                    global_line.getData().add(new XYChart.Data<Number,Number>((int)(val/5),mTC.getServerConnection().getActivity()));
                }
                }
        });    
    }

    private void pollStats() {
        (new Thread(new Runnable(){
            @Override
            public void run() {
                long val =0;
                while (true){
                    
                    setStatsGlobal(mTC.getServerConnection().getGlobalStats(),(int)(val));
                    
                    val++;
                    try {
                        Thread.sleep(POLLDELAY);
                    } catch (InterruptedException ex) {
                        Logger.getGlobal().log(Level.SEVERE, null, ex);
                    }
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            htmlviewer.getEngine().reload();
                            webviewtwo.getEngine().reload();
                        }
                    });
                }
            }
        })).start();
    }

    
    @FXML private ListView conversation1_avitars;
    private ObservableList<User> avitars1;
    @FXML private ListView conversation2_avitars;
    private ObservableList<User> avitars2;
    @FXML private ListView conversation3_avitars;
    private ObservableList<User> avitars3;
    @FXML private ListView conversation4_avitars;
    private ObservableList<User> avitars4;
    @FXML private ListView conversation5_avitars;
    private ObservableList<User> avitars5;
    
    private void setupUserLists(){
        conversation1_avitars.setCellFactory(new avitarCellFactory());
        avitars1 = new MessageDisplayList();
        conversation2_avitars.setCellFactory(new avitarCellFactory());
        avitars2 = new MessageDisplayList();
        conversation3_avitars.setCellFactory(new avitarCellFactory());
        avitars3 = new MessageDisplayList();
        conversation4_avitars.setCellFactory(new avitarCellFactory());
        avitars4 = new MessageDisplayList();
        conversation5_avitars.setCellFactory(new avitarCellFactory());
        avitars5 = new MessageDisplayList();
        pollAvitars();
    }
    
    private void pollAvitars(){
        (new Thread(new Runnable(){
            @Override
            public void run() {
                while (true){
                    refreshAvitars1(findId(1));
                    refreshAvitars2(findId(2));
                    refreshAvitars3(findId(3));
                    refreshAvitars4(findId(4));
                    refreshAvitars5(findId(5));
                    try {
                        Thread.sleep(POLLDELAY);
                    } catch (InterruptedException ex) {
                        Logger.getGlobal().log(Level.SEVERE, null, ex);
                    }
                }
            }
        })).start();
    }
    
    private void pollConvStats(){
        (new Thread(new Runnable(){
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(POLLDELAY);
                    } catch (InterruptedException ex) {
                        Logger.getGlobal().log(Level.SEVERE, null, ex);
                    }
                    setStatsConv1(mTC.getServerConnection().getStats(findId(1)));
                    setStatsConv2(mTC.getServerConnection().getStats(findId(2)));
                    setStatsConv3(mTC.getServerConnection().getStats(findId(3)));
                    setStatsConv4(mTC.getServerConnection().getStats(findId(4)));
                    setStatsConv5(mTC.getServerConnection().getStats(findId(5)));
                }
            }
        })).start();
    }
    
    private long findId(int pane){
        Set<Long> ids = idtopane.keySet();
        for(Long l: ids){
            if (idtopane.get(l).equals(Integer.valueOf(pane))){return l;}
        }
        return -1;
    }
    
    private void refreshAvitars1(long id){
        final List<User> users = mTC.getServerConnection().getUsers(id);
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                if (users!=null){
                    System.out.println();
                    if (!avitars1.isEmpty()){avitars1.clear();}
                    avitars1.addAll(users);
                    conversation1_avitars.setItems(avitars1);
                }
            }
        });
    }
    
    private void refreshAvitars2(long id){
        final List<User> users = mTC.getServerConnection().getUsers(id);
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                if (users!=null){
                    if (!avitars2.isEmpty()){avitars2.clear();}
                    avitars2.addAll(users);
                    conversation2_avitars.setItems(avitars2);
                }
            }
        });
    }
    
    private void refreshAvitars3(long id){
        final List<User> users = mTC.getServerConnection().getUsers(id);
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                if (users!=null){
                    if (!avitars3.isEmpty()){avitars3.clear();}
                    avitars3.addAll(users);
                    conversation3_avitars.setItems(avitars3);
                }
            }
        });
    }
    
    private void refreshAvitars4(long id){
        final List<User> users = mTC.getServerConnection().getUsers(id);
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                if (users!=null){
                    if (!avitars4.isEmpty()){avitars4.clear();}
                    avitars4.addAll(users);
                    conversation4_avitars.setItems(avitars4);
                }
            }
        });
    }
    
    private void refreshAvitars5(long id){
        final List<User> users = mTC.getServerConnection().getUsers(id);
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                if (users!=null){
                    if (!avitars5.isEmpty()){avitars5.clear();}
                    avitars5.addAll(users);
                    conversation5_avitars.setItems(avitars5);
                }
            }
        });
    }

    public HashMap<Long,Integer> getMap() {
        return (HashMap<Long,Integer>)idtopane.clone();
    }
    
}
