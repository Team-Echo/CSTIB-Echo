/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.cam.echo.TouchClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Philip
 */
public class ConfrenceLoadScreenController implements Initializable {
    
    @FXML
    private Button Open_button;
    @FXML
    private Button Launch_button;
    @FXML
    private TextField Confrence_Name_textfield;
    @FXML
    private TextField IP_Adress_textfield1;
    @FXML
    private TextField IP_Adress_textfield2;
    @FXML
    private TextField IP_Adress_textfield3;
    @FXML
    private TextField IP_Adress_textfield4;
    @FXML
    private TextField Port_textfield;
    @FXML
    private TextField Confrence_ID_textfield;
    @FXML
    private TextField textfield_url;
    @FXML
    private RadioButton radio_button_url;
    @FXML
    private RadioButton radio_button_ip;
    @FXML
    private Label error_message;
    
    private ECHOResource er;

    /**
     * Initializes the controller class.
     * @param url currently unused
     * @param rb MUST be of type ECHOResource to allow correct operation
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup group = new ToggleGroup();
        radio_button_url.setToggleGroup(group);
        radio_button_ip.setToggleGroup(group);
        
        radio_button_url.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                textfield_url.setDisable(false);
                IP_Adress_textfield1.setDisable(true);
                IP_Adress_textfield2.setDisable(true);
                IP_Adress_textfield3.setDisable(true);
                IP_Adress_textfield4.setDisable(true);
                Port_textfield.setDisable(true);
            }
        });
        
        radio_button_ip.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                textfield_url.setDisable(true);
                IP_Adress_textfield1.setDisable(false);
                IP_Adress_textfield2.setDisable(false);
                IP_Adress_textfield3.setDisable(false);
                IP_Adress_textfield4.setDisable(false);
                Port_textfield.setDisable(false);
            }
        });
        
        if (rb instanceof ECHOResource){
            er = (ECHOResource)rb;
        }else {System.err.println("the wrong resource type has been provided to the confrenceloadscreencontroller class a resource of type ECHOResource must be provided");System.exit(1);}
        final FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ECHOFilter = new FileChooser.ExtensionFilter("ECHO file (*.echo)","*.echo");
        fileChooser.getExtensionFilters().add(ECHOFilter);
        fileChooser.setTitle("Load a confrence settings file (.echo)");
        Open_button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                File file = fileChooser.showOpenDialog(Open_button.getScene().getWindow());
                    if (file != null) {
                        load(file);
                    }
            }});
        Launch_button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                Stage stage = (Stage) Launch_button.getScene().getWindow();
                
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("GUI.fxml"),er);
                } catch (IOException ex) {
                    Logger.getLogger(ConfrenceLoadScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
        
                Scene scene = new Scene(root);
                try {
                    er.getTouchClient().setConfrenceName(Confrence_Name_textfield.getText());
                    if (!IP_Adress_textfield1.isDisabled()){
                        er.getTouchClient().setConfrenceIP(ip());
                        er.getTouchClient().setConfrencePort(port(Port_textfield.getText()));
                    } else {
                        er.getTouchClient().setConfrenceURL(textfield_url.getText());
                    }
                    er.getTouchClient().setConfrenceID(id(Confrence_ID_textfield.getText()));
                } catch(InvalidServerCredentialsException ex){
                    error_message.setText("The inputted values are incorrect or incomplete");  return;
                }
                
                ServerConnection sc = new ServerConnection(er.getTouchClient());
                (new Thread(sc)).start();
                try {
                    stage.setTitle(er.getTouchClient().getConfrenceName());
                } catch (NotInstantiatedYetException ex) {
                    stage.setTitle("ECHO");
                }
                stage.setFullScreen(true);
                stage.setScene(scene);
                stage.show();
            }});
      }
      private void load(File f){
        try {
            BufferedReader in = new BufferedReader(new FileReader(f));
            Confrence_Name_textfield.setText(in.readLine());
            radio_button_url.fire();
            textfield_url.setText(in.readLine());
            Confrence_ID_textfield.setText(in.readLine());
            Launch_button.fire();
        } catch (FileNotFoundException ex) {
            error_message.setText("The file could not be found");
        } catch (IOException ex) {
            error_message.setText("The file could not be Read from");
        }
      }
      private int ip() throws InvalidServerCredentialsIPException{
          int ip,ip1,ip2,ip3,ip4;
          try{
            ip1 = Integer.parseInt(IP_Adress_textfield1.getText());
            ip2 = Integer.parseInt(IP_Adress_textfield2.getText());
            ip3 = Integer.parseInt(IP_Adress_textfield3.getText());
            ip4 = Integer.parseInt(IP_Adress_textfield4.getText());
          }catch(NumberFormatException e){
              throw new InvalidServerCredentialsIPException();
          }
          ip = (ip4&0xff)&((ip3&0xff)<<8)&((ip2&0xff)<<16)&((ip1&0xff)<<24);
          return ip;
      }
      private int port(String val) throws InvalidServerCredentialsPortException{
          try{
            return Integer.parseInt(val);
          }catch (NumberFormatException e){
              throw new InvalidServerCredentialsPortException();
          }
      }
      private int id(String val) throws InvalidServerCredentialsIDException{
          try{
            return Integer.parseInt(val);
          }catch (NumberFormatException e){
              throw new InvalidServerCredentialsIDException();
          }
      }
}
