package M1International.MultimapAdvanced;

import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	System.out.println( "Hello World!" );
        /*GuavaMultimap tester = new GuavaMultimap();
        tester.testMultimap();*/
        /*GuavaBimap test1=new GuavaBimap();
        test1.testBimap();*/
        /*MyMultimapTest test1=new MyMultimapTest();
        test1.testMultimap();*/
        
    	MyMultimapInterface<String,String> multimap = AdvancedMultimap.create();		

	      multimap.add("lower", "a");
	      multimap.add("upper", "b");
	      //multimap.add("lower", "c");
	      //multimap.add("lower", "d");
	     // multimap.add("lower", "e");
	      MyMultimapInterface<String,String> inverse= multimap.inverse();
	      List<String> lowerList = (List<String>)multimap.get("lower");
	      System.out.println("Initial lower case list");
	      System.out.println(lowerList.toString());
	      List<String> lowerList1 = (List<String>)inverse.get("a");
	      System.out.println(lowerList1.toString());
	      inverse.add("f", "lower");
	      lowerList = (List<String>)multimap.get("lower");
	      System.out.println(lowerList.toString());
	      

    }
}
