/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.cam.echo.TouchClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;

/**
 *
 * @author Philip
 * @param <T> the type of the list members
 */
public class MessageDisplayList<T> implements ObservableList<T>{
    
    private final ArrayList<T> mlist;
    private final ArrayList<InvalidationListener> mInvalidationListenerList;
    private final ArrayList<ListChangeListener> mListChangeListenerList;
    
    private void onChange(Change change){
        for (ListChangeListener l:mListChangeListenerList){
            l.onChanged(change);
        }
    }
    
    private void onInvalidation(Observable ob){
        for (InvalidationListener l : mInvalidationListenerList){
            l.invalidated(ob);
        }
    }
    
    public MessageDisplayList(){
        mlist = new ArrayList();
        mInvalidationListenerList = new ArrayList();
        mListChangeListenerList = new ArrayList();
    }

    @Override
    public void addListener(ListChangeListener<? super T> ll) {
        mListChangeListenerList.add(ll);
    }

    @Override
    public void removeListener(ListChangeListener<? super T> ll) {
        mListChangeListenerList.remove(ll);
    }

    @Override
    public boolean addAll(T... es) {
        for (T e : es) {
            if (!add(e)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean setAll(T... es) {
        clear();
        return addAll(es);
    }

    @Override
    public boolean setAll(Collection<? extends T> clctn) {
        clear();
        return addAll(clctn);
    }

    @Override
    public boolean removeAll(T... es) {
        for (T e : es) {
            if (!remove(e)){
                return false;
            }
        }
        return true;    
    }

    @Override
    public boolean retainAll(T... es) {
        ArrayList<T> c = new ArrayList();
        c.addAll(Arrays.asList(es));
        return retainAll(c);
    }

    @Override
    public void remove(int i, int i1) {
        removeAll(subList(i,i1));
    }

    @Override
    public int size() {
        return mlist.size();
    }

    @Override
    public boolean isEmpty() {
        return mlist.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return mlist.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return mlist.iterator();
    }

    @Override
    public Object[] toArray() {
        return mlist.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return mlist.toArray(a);
    }

    @Override
    public boolean add(T e) {
        boolean out = mlist.add(e);
        AddChange<T> c = new AddChange(e,this);
        onChange(c);
        return out;
    }

    @Override
    public boolean remove(Object o) {
        boolean out = mlist.remove(o);
        RemoveChange<T> c = new RemoveChange((T)o,this);
        onChange(c);
        return out;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return mlist.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T s:c){
            if (!(add(s))){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        Iterator i = c.iterator();
        int temp = 0;
        while (temp<index && i.hasNext()){
            i.next();
        }
        while (i.hasNext()){
            if(!(add((T)(i.next())))){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Iterator i = c.iterator();
        while (i.hasNext()){
            if (!add((T)(i.next()))){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return mlist.retainAll(c);
    }

    @Override
    public void clear() {
        mlist.clear();
        for (InvalidationListener l:mInvalidationListenerList){
            l.invalidated(this);
        }
    }

    @Override
    public T get(int index) {
        return mlist.get(index);
    }

    @Override
    public T set(int index, T element) {
        return mlist.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        AddChange<T> c = new AddChange(element,this);
        onChange(c);
        mlist.add(index, element);
    }

    @Override
    public T remove(int index) {
        RemoveChange<T> c = new RemoveChange(get(index),this);
        onChange(c);
        return mlist.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return mlist.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return mlist.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return mlist.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return mlist.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return mlist.subList(fromIndex, toIndex);
    }

    @Override
    public void addListener(InvalidationListener il) {
        mInvalidationListenerList.add(il);
    }

    @Override
    public void removeListener(InvalidationListener il) {
        mInvalidationListenerList.remove(il);
    }
    
}
