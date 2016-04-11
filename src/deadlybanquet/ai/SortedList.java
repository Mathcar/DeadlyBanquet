package deadlybanquet.ai;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author omega
 * 
 */
public class SortedList<T extends Comparable<T>> implements Iterable<T>{
    private LinkedList<T> list= new LinkedList<>();
    public void add(T t){
        list.add(t);
        Collections.sort(list);
    }
    
    public Iterator<T> iterator(){
        return list.iterator();
    }
    
    public int size(){
        return list.size();
    }
    
    public T first(){
        return list.get(0);
    }
    
    public boolean isEmpty(){
        return list.isEmpty();
    }
    
    public boolean remove(T t){
        return list.remove(t);
    }
}
