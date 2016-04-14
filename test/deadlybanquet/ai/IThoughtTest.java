package deadlybanquet.ai;

import static deadlybanquet.ai.BackStory.SNOWEDIN;
import static deadlybanquet.ai.BeingPolite.THANKS;
import java.util.SortedSet;
import java.util.TreeSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author omega
 */
public class IThoughtTest {
    
    public IThoughtTest() {
    }

    @Test
    //Really just a usage example.
    public void testCompareTo() {
        SortedList tree = new SortedList();
        tree.add(new Whereabouts("Bill", "Kitchen", null, 1.0, null));
        tree.add(new Whereabouts("Jane", "Kitchen", null, 0.0, null));
        tree.add(new Whereabouts("Oscar", "Kitchen", null, 0.5, null));
        tree.add(new Whereabouts("April", "Kitchen", null, 0.5, null));
        for (IThought i:tree)
            System.out.println(i);
    }
    
    @Test
    public void nonsenseTest(){
        SNOWEDIN.contains(null);
    } 
}
