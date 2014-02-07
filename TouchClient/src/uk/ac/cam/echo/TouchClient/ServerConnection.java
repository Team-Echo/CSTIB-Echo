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
    //private final ClientApi mClient;

    public ServerConnection(TouchClient tc){
        mTC = tc;
        
        while (true){
            try {
                mGUI = mTC.getGUI();
            } catch (NotInstantiatedYetException ex) {
                continue;
            }
            break;
        }
        
        String url = "127.0.0.1";
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
        
        //mClient = new ClientApi(url);
    }
    
    @Override
    public void run() {
        //tries to get the GUI repetedly untill the gui has been initalized
        System.out.println("the server is connected");
        MessageTest mt = new MessageTest(mGUI);
        (new Thread(mt)).start();
    }
    
    
    
}
