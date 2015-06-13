package M1International.MultimapAdvanced;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(AddKeyValue.class);
		suite.addTestSuite(RemoveKeyValue.class);
		suite.addTestSuite(ContainActions.class);
		suite.addTestSuite(ViewActions.class);
		//$JUnit-END$
		return suite;
	}

}
