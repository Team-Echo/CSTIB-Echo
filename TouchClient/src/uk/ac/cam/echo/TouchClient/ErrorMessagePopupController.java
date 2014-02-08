/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.cam.echo.TouchClient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Philip
 */
public class ErrorMessagePopupController implements Initializable {

    @FXML
    Label error_message;
    @FXML
    Button close;

    
    /**
     * Initializes the controller class.
     * @param url not used 
     * @param rb the resource bundle that contains the error message with the key "Error_Message"
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String message = (String)rb.getObject("Error_Message");
        error_message.setText(message);
        close.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                Runtime.getRuntime().exit(1);
            }
        });
    }
    
    public void setErrorMessage(final String error){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                error_message.setText(error);
            }
        });
    }
    
}
