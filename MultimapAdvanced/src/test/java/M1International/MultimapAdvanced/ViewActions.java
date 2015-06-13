/**
 * 
 */
package M1International.MultimapAdvanced;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Multiset;

import junit.framework.TestCase;

/**
 * @author waseem
 *
 */
public class ViewActions extends TestCase {

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	MyMultimapInterface<String, String> myMultimap;
	MyMultimapInterface<String, String> inverseMyMultimap;

	protected void setUp() throws Exception {
		super.setUp();
		myMultimap = AdvancedMyMultimap.create();
	}
	
	public void testGetKeys()
	{
		myMultimap.put("Waseem", "Banana");
		myMultimap.put("Waseem", "Apple");
		myMultimap.put("Waseem", "Pear");
		myMultimap.put("Rene", "Apple");
		myMultimap.put("Rene", "Orange");
	    myMultimap.remove("Rene", "Pear");
	    myMultimap.remove("Zahid", "Banana");
	    
	    Set<String> Persons=myMultimap.keySet();
	    
	    assertNotNull(Persons);
	    
	    
	}
	public void testremoveKeyFromKeySet()
	{
		myMultimap.put("Waseem", "Banana");
		myMultimap.put("Waseem", "Apple");
		myMultimap.put("Waseem", "Pear");
		myMultimap.put("Rene", "Apple");
		myMultimap.put("Rene", "Orange");
	    myMultimap.put("Rene", "Pear");
	    myMultimap.put("Zahid", "Banana");
	    Set<String> Persons=myMultimap.keySet();
	    Persons.remove("Rene");
	    inverseMyMultimap=myMultimap.inverse();
	    Collection<String> peopleBanana=inverseMyMultimap.get("Apple");
	    assertEquals(4,myMultimap.size());
	}
	
	// testing the Keys method 
	// returning the multiset of keys
	
	public void testMultisetKeys()
	{
		myMultimap.put("Waseem", "Banana");
		myMultimap.put("Waseem", "Apple");
		myMultimap.put("Waseem", "Pear");
		myMultimap.put("Rene", "Apple");
		myMultimap.put("Rene", "Orange");
	    myMultimap.put("Zahid", "Banana");
	    Multiset<String> multiset=myMultimap.keys();
	    Collection<String> fruits=myMultimap.get("Waseem");
	    assertEquals(multiset.count("Waseem"),fruits.size());
	}
	
	public void testMultisetRemove()
	{
		myMultimap.put("Waseem", "Banana");
		myMultimap.put("Waseem", "Apple");
		myMultimap.put("Waseem", "Pear");
		myMultimap.put("Rene", "Apple");
		myMultimap.put("Rene", "Orange");
	    myMultimap.put("Zahid", "Banana");
	    Multiset<String> multiset=myMultimap.keys();
	    multiset.remove("Waseem", 2);
	    Collection<String> fruits=myMultimap.get("Waseem");
	    assertEquals(fruits.size(),1);
	}
	// following test created for testing the values method implementation
	// size of inverse is equals to size of collection returned by values method
	public void testValues()
	{
		myMultimap.put("Waseem", "Banana");
		myMultimap.put("Waseem", "Apple");
		myMultimap.put("Waseem", "Pear");
		myMultimap.put("Rene", "Apple");
		myMultimap.put("Rene", "Orange");
	    myMultimap.put("Zahid", "Banana");
	    Collection<String> values=myMultimap.values();
	    inverseMyMultimap=myMultimap.inverse();
	    assertEquals(values.size(),myMultimap.size());
	}
	public void testClearOnValuesCollection()
	{
		myMultimap.put("Waseem", "Banana");
		myMultimap.put("Waseem", "Apple");
		myMultimap.put("Waseem", "Pear");
		myMultimap.put("Rene", "Apple");
		myMultimap.put("Rene", "Orange");
	    myMultimap.put("Zahid", "Banana");
	    Collection<String> values=myMultimap.values();
	    values.clear();
	    myMultimap.put("Zahid", "Banana");
	    assertTrue(myMultimap.size()==1);
	}
	public void testValuesCollectionIterator()
	{
		myMultimap.put("Waseem", "Banana");
		myMultimap.put("Waseem", "Apple");
		myMultimap.put("Waseem", "Pear");
		myMultimap.put("Rene", "Apple");
		myMultimap.put("Rene", "Orange");
	    myMultimap.put("Zahid", "Banana");
	    Collection<String> values=myMultimap.values();
	    Iterator<String> iterator=values.iterator();
	    while(iterator.hasNext())
	    {
	    	System.out.println(iterator.next());
	    }
	    assertTrue(myMultimap.size()==6);
	}
	

}
