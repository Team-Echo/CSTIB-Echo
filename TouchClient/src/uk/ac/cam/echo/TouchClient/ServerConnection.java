/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.cam.echo.TouchClient;

/**
 *the class that connects to the server will eventually implement the ClientApi
 * 
 * @author Philip
 */
public class ServerConnection implements Runnable{
    
    private final TouchClient mTC;
    private GUIController mGUI;

    public ServerConnection(TouchClient tc){
        mTC = tc;
    }
    
    @Override
    public void run() {
        //tries to get the GUI repetedly untill the gui has been initalized
        while (true){
            try {
                mGUI = mTC.getGUI();
            } catch (NotInstantiatedYetException ex) {
                continue;
            }
            break;
        }
        System.out.println("the server is connected");
        MessageTest mt = new MessageTest(mGUI);
        (new Thread(mt)).start();
    }
    
    
    
}
