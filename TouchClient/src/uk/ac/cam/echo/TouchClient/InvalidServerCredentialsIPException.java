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
class InvalidServerCredentialsIPException extends InvalidServerCredentialsException {

    public InvalidServerCredentialsIPException() {
        super("the ip is not in the correct format");
    }
    
}
