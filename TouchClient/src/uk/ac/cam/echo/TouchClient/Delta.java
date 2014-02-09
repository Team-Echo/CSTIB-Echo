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
class Delta {
    private boolean pressed = false;
    public synchronized boolean testAndPress(){
        if (pressed){
            return false;
        }else{
            pressed = true;
            return true;
        }
    }
    public synchronized boolean isPressed(){
        return pressed;
    }
    public synchronized void unPress(){
        pressed = false;
    }
    public int id;
    public double x;
    public double y;
}
