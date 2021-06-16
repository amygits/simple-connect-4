

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Amy Ma
 * @version 1.0
 * 
 * This class servers to test the Connect4ComputerPlayer class
 *
 */
public class Connect4ComputerPlayerTest {
	
	/** Declaring static variables and test classes */
	private static Connect4ComputerPlayer testComputer;
	private Connect4ComputerPlayer testComputer1;
	private static int testRuns = 10; // Number of times to generate move
	
	/** Initializes static testComputer object */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testComputer = new Connect4ComputerPlayer();
	}
	
	/** Deconstructors static testComputer object */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		testComputer = null;
	}
	
	/** Initializes testComputer1 object */
	@Before
	public void setUp() throws Exception {
		testComputer1 = new Connect4ComputerPlayer(1, 7);
	}
	
	/** Deconstructs testComputer1 object */
	@After
	public void tearDown() throws Exception {
		testComputer1 = null;
	}
	
	/** Tests that the generateMove method of the two testComputer objects
	 * The first if statement tests testComputer - default constructor object
	 * The second if statements tests testComputer1 - overloaded parameter object 
	 * Each test will run/generate move 10x to allow for incorrect generations
	 * */
	@Test
	public void testGenerateMove() {
		
		// Tests testComputer - Default values
		for (int i = 0; i < testRuns; i++) {
			int x = testComputer.generateMove();
			if (x >= 1 && x <= 23) {
				assertTrue(true);
			} else { assertTrue(false); }
		}
		
		// Tests testComputer1 - Overloaded values
		for (int i = 0; i < testRuns; i++) {
			int x = testComputer1.generateMove();
			if (x >= 1 && x <= 7) {
				assertTrue(true);
			} else { assertTrue(false); }
		}
	}
}
