package uk.ac.cam.echo.TouchClient;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *the main class that provides connections between classes and stores information about the Conference for both back and front ends to use 
 * 
 * @author Philip
 */
public class TouchClient extends Application {
    
    private String confrenceName = null;
    private String ip = null;
    private Integer port = null;
    private Integer confrenceID = null;
    private String url = null;
    private GUIController mGUI = null; 
    
    public TouchClient(){}
    
    /**
     * function loads the first gui to input the conference details
     * @param primaryStage the stage that the application runs in
     * @throws IOException if the FXML file cant be found
     */
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
    /**
     * to connect need ether this or the ip of the server
     * 
     * @return the url of the Conference if there is one
     * @throws NotInstantiatedYetException if it has not been set yet this exception is thrown
     */
    public String getConfrenceURL() throws NotInstantiatedYetException{
        if (url==null){
            throw new NotInstantiatedYetException();
        }else{
            return url;
        }
    }
    /**
     * to connect need this or the url of the server
     * 
     * @return the ip of the Conference if there is one
     * @throws NotInstantiatedYetException if i has not been set yet this exception is thrown
     */
    public String getConfrenceIP() throws NotInstantiatedYetException{
        if (ip==null){
            throw new NotInstantiatedYetException();
        }else{
            return ("http://").concat(ip.concat(Integer.toString(port)));
        }
    }
    
    public int getConfrenceID() throws NotInstantiatedYetException{
        if (confrenceID==null){
            throw new NotInstantiatedYetException();
        }else{
            return confrenceID.intValue();
        }
    }
    public GUIController getGUI() throws NotInstantiatedYetException{
        if (mGUI==null){
            throw new NotInstantiatedYetException();
        }else{
            return mGUI;
        }
    }
    
    /**
     * 
     * @param name the name of the Conference
     */
    public void setConfrenceName(String name){
        confrenceName = name;
    }
    /**
     * 
     * @param gui the guicontroller class must be given so the server connection can give it messages
     */
    public void setGUI(GUIController gui){
        mGUI=gui;
    }
    public void setConfrenceIP(String serverip){
        ip = serverip;
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

    public void setConfrenceURL(String text) {
        url = text;
    }
    
}