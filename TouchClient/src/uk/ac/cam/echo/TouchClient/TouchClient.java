/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.cam.echo.TouchClient;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Philip
 */
public class TouchClient extends Application {
    
    private String confrenceName = null;
    private Integer ip = null;
    private Integer port = null;
    private Integer confrenceID = null;
    private String url = null;
    
    public TouchClient(){}
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        ECHOResource echoresource = new ECHOResource(this); 
        Parent root = FXMLLoader.load(getClass().getResource("ConfrenceLoadScreen.fxml"),echoresource);
        
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("ECHO GUI v2");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public String getConfrenceName() throws NotInstantiatedYetException{
        if (confrenceName==null){
            throw new NotInstantiatedYetException();
        }else{
            return confrenceName;
        }
    }
    public String getConfrenceURL() throws NotInstantiatedYetException{
        if (url==null){
            throw new NotInstantiatedYetException();
        }else{
            return url;
        }
    }
    public int getConfrenceIP() throws NotInstantiatedYetException{
        if (ip==null){
            throw new NotInstantiatedYetException();
        }else{
            return ip.intValue();
        }
    }
    public int getConfrencePort() throws NotInstantiatedYetException{
        if (port==null){
            throw new NotInstantiatedYetException();
        }else{
            return port.intValue();
        }
    }
    public int getConfrenceID() throws NotInstantiatedYetException{
        if (confrenceID==null){
            throw new NotInstantiatedYetException();
        }else{
            return confrenceID.intValue();
        }
    }
    
    public void setConfrenceName(String name){
        confrenceName = name;
    }
    public void setConfrenceIP(int serverip){
        ip = Integer.valueOf(serverip);
    }
    public void setConfrencePort(int serverport){
        port = Integer.valueOf(serverport);
    }
    public void setConfrenceID(int id){
        confrenceID = Integer.valueOf(id);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    void setConfrenceURL(String text) {
        url = text;
    }
    
}