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
class ErrorResourceBundle extends ListResourceBundle {

    private final Object[][] contents;
    
    public ErrorResourceBundle(String error) {
        contents = new Object[][]{{"Error_Message", error}};
    }

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    
}
