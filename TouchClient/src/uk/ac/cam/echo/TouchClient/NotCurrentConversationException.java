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
class NotCurrentConversationException extends Exception {

    public NotCurrentConversationException(int conversationID) {
        super("the conversation represented by the ID " + conversationID + " is not currently displayed on screen so cannot be replaced");
    }
    
}
