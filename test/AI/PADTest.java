package AI;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author omega
 */
public class PADTest {
    
    public PADTest() {
    }

    /**
     * Test of setP method, of class PAD.
     */
    @Test
    public void testSetP() {
        System.out.println("setP");
        double i = 0.0;
        PAD instance = null;
        instance.setP(i);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setA method, of class PAD.
     */
    @Test
    public void testSetA() {
        System.out.println("setA");
        double i = 0.0;
        PAD instance = null;
        instance.setA(i);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setD method, of class PAD.
     */
    @Test
    public void testSetD() {
        System.out.println("setD");
        double i = 0.0;
        PAD instance = null;
        instance.setD(i);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of moveInTheDirectionOf method, of class PAD.
     */
    @Test
    public void testMoveInTheDirectionOf() {
        System.out.println("moveInTheDirectionOf");
        PAD newfeeling = new PAD(1,-1,1);
        double howmuch = 0.5;
        PAD instance = new PAD(0.5,0,0);
        instance.moveInTheDirectionOf(newfeeling, howmuch);
        // TODO review the generated test code and remove the default call to fail.
        System.out.println(instance);
    }

    /**
     * Test of getP method, of class PAD.
     */
    @Test
    public void testGetP() {
        System.out.println("getP");
        PAD instance = null;
        double expResult = 0.0;
        double result = instance.getP();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getA method, of class PAD.
     */
    @Test
    public void testGetA() {
        System.out.println("getA");
        PAD instance = null;
        double expResult = 0.0;
        double result = instance.getA();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getD method, of class PAD.
     */
    @Test
    public void testGetD() {
        System.out.println("getD");
        PAD instance = null;
        double expResult = 0.0;
        double result = instance.getD();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of murderousness method, of class PAD.
     */
    @Test
    public void testMurderousness() {
        System.out.println("murderousness");
        PAD instance = null;
        double expResult = 0.0;
        double result = instance.murderousness();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class PAD.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        PAD instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
