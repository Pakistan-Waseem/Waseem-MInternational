package M1International.MultimapAdvanced;

import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Hello world!
 *
 */
public class App {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		System.out.println("Hello World!");
		/*
		 * GuavaMultimap tester = new GuavaMultimap(); tester.testMultimap();
		 */
		/*
		 * GuavaBimap test1=new GuavaBimap(); test1.testBimap();
		 */
		/*
		 * MyMultimapTest test1=new MyMultimapTest(); test1.testMultimap();
		 */

		MyBiMultimap<String, String> multimap = AdvancedMyBiMultimap
				.create();

		multimap.put("lower", "a");
		multimap.put("upper", "b");
		// multimap.add("lower", "c");
		// multimap.add("lower", "d");
		// multimap.add("lower", "e");
		MyBiMultimap<String, String> inverse = multimap.inverse();
		List<String> lowerList = (List<String>) multimap.get("lower");
		System.out.println(lowerList.toString());
		List<String> lowerList1 = (List<String>) inverse.get("a");
		System.out.println(lowerList1.toString());
		inverse.put("f", "lower");
		lowerList = (List<String>) multimap.get("lower");
		System.out.println(lowerList.toString());

	}
}
