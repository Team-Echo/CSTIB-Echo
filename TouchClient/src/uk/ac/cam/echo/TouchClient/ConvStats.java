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
class ConvStats {
    
    private final String mCurrentUsers;
    private final String mContributingUsers;
    private final String mNumberOfMessages;
    private final double mMaleRatio;
    
    public ConvStats(int currentUsers, int contributingUsers, int numberOfMessages, double maleRatio){
        mCurrentUsers = Integer.toString(currentUsers);
        mContributingUsers = Integer.toString(contributingUsers);
        mNumberOfMessages = Integer.toString(numberOfMessages);
        mMaleRatio = maleRatio;
    }
    
    public String getCurrentUsers(){return mCurrentUsers;}
    public String getContributingUsers(){return mContributingUsers;}
    public String getNumberOfMessages(){return mNumberOfMessages;}
    public double getMaleRatio(){
        if (mMaleRatio == Double.POSITIVE_INFINITY){
            return 1;
        }else{
            return mMaleRatio;
        }
    }
    public double getFemaleRatio(){
        if (mMaleRatio == Double.POSITIVE_INFINITY){
            return 0;
        }else{
            return 1;
        }
    }
}
