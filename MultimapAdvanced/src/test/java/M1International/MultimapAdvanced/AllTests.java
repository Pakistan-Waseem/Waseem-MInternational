package M1International.MultimapAdvanced;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(AddingElement.class);
		suite.addTestSuite(RemoveElement.class);
		suite.addTestSuite(ContainsAction.class);
		suite.addTestSuite(Views.class);
		//$JUnit-END$
		return suite;
	}

}
