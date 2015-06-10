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
public class AddingElement extends TestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	MyMultimapInterface<String, String> myMultimap;
	MyMultimapInterface<String, String> inverseMyMultimap;

	protected void setUp() throws Exception {
		super.setUp();
		myMultimap = AdvancedMyMultimap.create();
	}

	public void testAdd() {
		
		myMultimap.put("Fruits", "Bannana");
		myMultimap.put("Fruits", "Apple");
		myMultimap.put("Fruits", "Pear");
		myMultimap.put("Vegetables", "Carrot");
		myMultimap.put("Vegetables", "Onion");
		myMultimap.put("Vegetables", "Potato");
		Collection<String> fruits = myMultimap.get("Fruits");
		assertTrue(fruits.size() == 3);
	}
	public void testInverseAdd()
	{
		myMultimap.put("Fruits", "Bannana");
		myMultimap.put("Fruits", "Apple");
		inverseMyMultimap=myMultimap.inverse();
		inverseMyMultimap.put("Orange", "Fruits");
		Collection<String> fruits = myMultimap.get("Fruits");
		System.out.print(fruits);
		assertTrue(fruits.size()==3);
		
	}
	public void testAdditionSize()
	{
		myMultimap.put("Fruits", "Bannana");
		myMultimap.put("Fruits", "Apple");
		inverseMyMultimap=myMultimap.inverse();
		inverseMyMultimap.put("Orange", "Fruits");
		
		System.out.println(inverseMyMultimap.size());
		System.out.println(myMultimap.size());
		assertTrue(inverseMyMultimap.size()==myMultimap.size());
		
	}
}
