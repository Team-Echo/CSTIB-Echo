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
 *a class that represents an change to a observable list where something has been added
 * 
 * @author Philip
 */
public class AddChange<T> extends Change{
    
    private final T added;
    private final ObservableList mlist;
    
            public AddChange(T message, ObservableList list){
                super(list);
                mlist = list;
                added = message;
            }

            @Override
            public boolean next() {
                return false;
            }

            @Override
            public void reset() {
                mlist.remove(added);
            }

            @Override
            public int getFrom() {
                return mlist.size()-1;
            }

            @Override
            public int getTo() {
                return mlist.size();
            }

            @Override
            public List<T> getRemoved() {
                return new ArrayList();
            }

            @Override
            protected int[] getPermutation() {
                return new int[0];
            }
}
