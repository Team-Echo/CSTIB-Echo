/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.cam.echo.TouchClient;

/**
 *
 * @author Philip
 */
class ConversationAlredyDisplayedException extends Exception {

    public ConversationAlredyDisplayedException() {
        super("that conversation is alredy being displayed in another pane on this screen");
    }
    
}
