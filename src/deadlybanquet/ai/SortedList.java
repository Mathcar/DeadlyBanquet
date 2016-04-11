package deadlybanquet.ai;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author omega
 * 
 */
public class SortedList implements Iterable<IThought>{
    private LinkedList<IThought> list= new LinkedList<>();
    
    public void add(IThought t){
        list.add(t);
        list.sort((a, b) -> b.compareTo(a));
    }
    
    public Iterator<IThought> iterator(){
        return list.iterator();
    }
    
    public int size(){
        return list.size();
    }
    
    public IThought first(){
        return list.get(0);
    }
    
    public boolean isEmpty(){
        return list.isEmpty();
    }
    
    public boolean remove(IThought t){
        return list.remove(t);
    }
}
