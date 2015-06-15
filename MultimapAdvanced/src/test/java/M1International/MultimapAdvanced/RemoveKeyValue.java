/**
 * 
 */
package M1International.MultimapAdvanced;

import java.util.Collection;

import junit.framework.TestCase;

/**
 * @author waseem
 *
 */
public class RemoveKeyValue extends TestCase {

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	MyBiMultimap<String, String> myMultimap;
	MyBiMultimap<String, String> inverseMyMultimap;

	protected void setUp() throws Exception {
		super.setUp();
		myMultimap = AdvancedMyBiMultimap.create();
	}

	public void testRemoveElement() {
		
		myMultimap.put("Fruits", "Banana");
		myMultimap.put("Fruits", "Apple");
		myMultimap.put("Fruits", "Pear");
		myMultimap.put("Vegetables", "Carrot");
		myMultimap.put("Vegetables", "Onion");
	    myMultimap.remove("Fruits", "Pear");
		Collection<String> fruits = myMultimap.get("Fruits");
		assertFalse(fruits.size() == 3);
	}
public void testRemoveElementInverse() {
		
		myMultimap.put("Fruits", "Banana");
		myMultimap.put("Fruits", "Apple");
		myMultimap.put("Fruits", "Pear");
		myMultimap.put("Vegetables", "Carrot");
		myMultimap.put("Vegetables", "Onion");
		inverseMyMultimap=myMultimap.inverse();
		inverseMyMultimap.remove("Banana", "Fruits");
		Collection<String> fruits = myMultimap.get("Fruits");
		assertTrue(fruits.size() == 2);
	}

}
