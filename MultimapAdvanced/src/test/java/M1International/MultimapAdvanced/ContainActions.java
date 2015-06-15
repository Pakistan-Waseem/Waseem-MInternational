/**
 * 
 */
package M1International.MultimapAdvanced;

import junit.framework.TestCase;

/**
 * @author waseem
 *
 */
public class ContainActions extends TestCase {

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	MyBiMultimap<String, String> myMultimap;
	MyBiMultimap<String, String> inverseMyMultimap;

	protected void setUp() throws Exception {
		super.setUp();
		myMultimap = AdvancedMyBiMultimap.create();
	}
    public void testContainsKey()
    {
    	myMultimap.put("Fruits", "Banana");
		myMultimap.put("Fruits", "Apple");
		myMultimap.put("Fruits", "Pear");
		assertFalse(myMultimap.containsKey("Vegetables"));
    }
    public void testContainsValue()
    {
    	myMultimap.put("Fruits", "Banana");
		myMultimap.put("Fruits", "Apple");
		myMultimap.put("Fruits", "Pear");
		assertTrue(myMultimap.containsValue("Pear"));
    }
    public void testContainsEntry()
    {
    	myMultimap.put("Fruits", "Banana");
		myMultimap.put("Fruits", "Apple");
		myMultimap.put("Fruits", "Pear");
		inverseMyMultimap=myMultimap.inverse();
		inverseMyMultimap.remove("Apple","Fruits");
		assertEquals(true,inverseMyMultimap.containsEntry("Apple","Fruits"));
    }

}
