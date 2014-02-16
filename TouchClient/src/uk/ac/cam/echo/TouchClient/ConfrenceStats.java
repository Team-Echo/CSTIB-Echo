/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.cam.echo.TouchClient;

import java.util.ArrayList;
import java.util.List;
import uk.ac.cam.echo.data.Conversation;

/**
 *
 * @author Philip
 */
class ConfrenceStats {
    
    private List<Conversation> mConversations;
    
    public ConfrenceStats(List<Conversation> conversations){
        mConversations = conversations;
    }
    
    public List<Conversation> getConversations(){
        return mConversations;
    }
    
    public List<Tuple<String,Long>> getUsersInConv(){
        List<Tuple<String,Long>> out = new ArrayList();
        for (Conversation c :mConversations){
            out.add(new Tuple(c.getName(),c.getId()));
        }
        return out;
    }

    public static class Tuple<T,K> {

        private final T mval1;
        private final K mval2;
        
        public Tuple(T val1,K val2) {
            mval1 = val1; mval2 = val2;
        }
        
        public T getVal1(){return mval1;}
        public K getVal2(){return mval2;}
    }
    
}
