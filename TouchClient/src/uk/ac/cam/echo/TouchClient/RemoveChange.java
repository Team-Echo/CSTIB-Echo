/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.cam.echo.TouchClient;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;

/**
 *a class that represents an change to a observable list where something has been removed
 * 
 * @author Philip
 * @param <T> The type of the list contents
 */
public class RemoveChange<T> extends Change<String>{

    private final T removed;
    private final ObservableList mlist;
            
    public RemoveChange(T message, ObservableList list){
                super(list);
                mlist = list;
                removed = message;
    }

            @Override
            public boolean next() {
                return false;
            }

            @Override
            public void reset() {
                mlist.add(removed);
            }

            @Override
            public int getFrom() {
                return mlist.size();
            }

            @Override
            public int getTo() {
                return mlist.size()-1;
            }

            @Override
            public List getRemoved() {
                ArrayList<T> out = new ArrayList();
                out.add(removed);
                return out;
            }

            @Override
            protected int[] getPermutation() {
                return new int[0];
            }
    
}
