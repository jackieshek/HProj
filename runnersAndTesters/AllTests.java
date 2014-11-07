package runnersAndTesters;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PDGameTest1.class, PDGameTest2.class, PDGameTest3.class,
	AddVertexTest.class, RemoveVertexTest.class})
public class AllTests {

}
