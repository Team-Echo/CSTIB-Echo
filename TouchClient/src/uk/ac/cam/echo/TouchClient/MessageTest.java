/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.cam.echo.TouchClient;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *a test class that inputs some dummy data for the touch client
 * 
 * @author Philip
 */
public class MessageTest implements Runnable{
    
    private GUIController mc;
    
    public MessageTest(GUIController c){
        mc = c;
    }

    @Override
    public void run() {
        mc.initConversations("test 1", 1, "test 2", 2, "test 3", 3, "test 4", 4, "test 5", 5);
        try{
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("Hello world",1);
            mc.displayMessage("finaly now",1);
            mc.displayMessage("Hello world",2);
            mc.displayMessage("Hello world",2);
            mc.displayMessage("Hello world",2);
            mc.displayMessage("Hello world",2);
            mc.displayMessage("Hello world",2);
            //mc.scrollToEnd(1);
        } catch (NoMessageListException ex) {
            Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
}
