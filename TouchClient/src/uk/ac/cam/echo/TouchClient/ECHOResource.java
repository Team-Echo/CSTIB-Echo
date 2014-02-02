/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.cam.echo.TouchClient;

import java.util.ListResourceBundle;

/**
 *
 * @author Philip
 */
public class ECHOResource extends ListResourceBundle {
    private Object[][] resources;
    private final TouchClient tc;
    @Override
     protected Object[][] getContents() {
         return resources;
    }
    public TouchClient getTouchClient(){
        return tc;
    } 
    public ECHOResource(TouchClient touchclient){
        tc = touchclient;
    }
    
}
