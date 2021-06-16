
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
 * This class serves to test the Connect4.java logic class methods
 *
 */
public class Connect4Test {
	
	/** Declaring static variables and test class */ 
	private static Connect4 testLogic;
	private static char player1;
	private static char player2;
	
	/** Constructs variables and test class */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testLogic = new Connect4();
		player1 = 'X';
		player2 = 'O';
	}
	
	/** Deconstructs the objects/variables set up in the setUpBefore class */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		testLogic.clear();
		player1 = ' ';
		player2 = ' ';
	}
	
	/** Tests the getRow and getCol methods */ 
	@Test
	public void testGetRowNCol(){
		assertEquals(6, testLogic.getRows());
		assertEquals(7, testLogic.getCols());
	}
	
	/** Tests the getLastRow, getLastCol, and insertCol methods*/
	@Test
	public void testGetLastRowNCol(){
		
		testLogic.clear();
		
		// Initial values - tests for correct constructor inputs 
		assertEquals(-1, testLogic.getLastCol());
		assertEquals(-1, testLogic.getLastRow());
		
		// Test Values (Within range) 
		testLogic.insertCol(1, player1);
		assertEquals(5, testLogic.getLastRow());
		assertEquals(0, testLogic.getLastCol());

		testLogic.insertCol(1, player2);
		assertEquals(4, testLogic.getLastRow());
		assertEquals(0, testLogic.getLastCol());
		
		testLogic.insertCol(2, player1);
		assertEquals(5, testLogic.getLastRow());
		assertEquals(1, testLogic.getLastCol());
		
		testLogic.insertCol(3, player2);
		assertEquals(5, testLogic.getLastRow());
		assertEquals(2, testLogic.getLastCol());
		
		testLogic.insertCol(4, player1);
		assertEquals(5, testLogic.getLastRow());
		assertEquals(3, testLogic.getLastCol());
		
		testLogic.insertCol(5, player2);
		assertEquals(5, testLogic.getLastRow());
		assertEquals(4, testLogic.getLastCol());
		
		testLogic.insertCol(5, player1);
		assertEquals(4, testLogic.getLastRow());
		assertEquals(4, testLogic.getLastCol());
		
		testLogic.insertCol(6, player2);
		assertEquals(5, testLogic.getLastRow());
		assertEquals(5, testLogic.getLastCol());
		
		testLogic.insertCol(7, player1);
		assertEquals(5, testLogic.getLastRow());
		assertEquals(6, testLogic.getLastCol());
		
		testLogic.insertCol(1, player2);
		assertEquals(3, testLogic.getLastRow());
		assertEquals(0, testLogic.getLastCol());
		
		// (Above range) 
		try {
			testLogic.insertCol(8, player1);
			fail("Should throw ArrayIndexOutOfBoundsExceptions");
		} catch (final ArrayIndexOutOfBoundsException e){
			assertTrue(true);
		}
		
		try {
			testLogic.insertCol(480, player2);
			fail("Should throw ArrayIndexOutOfBoundsExceptions");
		} catch (final ArrayIndexOutOfBoundsException e){
			assertTrue(true);
		}
		
		testLogic.insertCol(1, player1);
		testLogic.insertCol(1, player2);
		testLogic.insertCol(1, player1);
		
		try {
			testLogic.insertCol(1, player2);
			fail("Should throw ArrayIndexOutOfBoundsExceptions");
		} catch (final ArrayIndexOutOfBoundsException e){
			assertTrue(true);
		}
		
		// (Below range)
		try {
			testLogic.insertCol(-1, player1);
			fail("Should throw ArrayIndexOutOfBoundsExceptions");
		} catch (final ArrayIndexOutOfBoundsException e){
			assertTrue(true);
		}
		try {
			testLogic.insertCol(-101, player2);
			fail("Should throw ArrayIndexOutOfBoundsExceptions");
		} catch (final ArrayIndexOutOfBoundsException e){
			assertTrue(true);
		}
		
	}
	
	/** Tests for expected output from horizontal method after inputting arbitrary values into game grid*/
	@Test 
	public void testHorizontal() {
		
		testLogic.clear();
		
		testLogic.insertCol(2, player2);
		assertEquals(" O     ", testLogic.horizontal());
		
		testLogic.insertCol(7, player1);
		assertEquals(" O    X", testLogic.horizontal());
		
		testLogic.insertCol(4, player2);
		assertEquals(" O O  X", testLogic.horizontal());
		
		testLogic.insertCol(2, player1);
		assertEquals(" X     ", testLogic.horizontal());
		
		testLogic.insertCol(1, player2);
		assertEquals("OO O  X", testLogic.horizontal());
		
		testLogic.insertCol(6, player1);
		assertEquals("OO O XX", testLogic.horizontal());
	}
	
	/** Test for expected output from vertical method after inputting arbitrary values into game grid */
	@Test
	public void testVertical() {
		
		testLogic.clear();
		
		testLogic.insertCol(4, player2);
		assertEquals("     O", testLogic.vertical());
		
		testLogic.insertCol(4, player1);
		assertEquals("    XO", testLogic.vertical());
		
		testLogic.insertCol(4, player2);
		assertEquals("   OXO", testLogic.vertical());
		
		testLogic.insertCol(4, player1);
		assertEquals("  XOXO", testLogic.vertical());
		
		testLogic.insertCol(4, player2);
		assertEquals(" OXOXO", testLogic.vertical());
	}
	
	/** Tests for expected output from forwarddiag method after inputting arbitrary values into game grid*/
	@Test
	public void testForwardDiag() {
		
		testLogic.clear();

		testLogic.insertCol(1, player1);
		testLogic.insertCol(2, player2);
		testLogic.insertCol(4, player1);
		testLogic.insertCol(3, player2);
		testLogic.insertCol(5, player1);
		testLogic.insertCol(1, player2);
		testLogic.insertCol(2, player1);
		testLogic.insertCol(4, player2);
		testLogic.insertCol(3, player1);
		testLogic.insertCol(7, player2);
		testLogic.insertCol(1, player1);
		testLogic.insertCol(5, player2);
		testLogic.insertCol(5, player1);
		testLogic.insertCol(4, player2);
		assertEquals("   OXO", testLogic.forwardDiag());
	}
	
	/** Tests for expected output from backdiag method after inputting arbitrary values into game grid*/
	@Test	
	public void testBackDiag() {
		
		testLogic.clear();

		testLogic.insertCol(1, player1);
		testLogic.insertCol(2, player2);
		testLogic.insertCol(4, player1);
		testLogic.insertCol(3, player2);
		testLogic.insertCol(5, player1);
		testLogic.insertCol(1, player2);
		testLogic.insertCol(2, player1);
		testLogic.insertCol(4, player2);
		testLogic.insertCol(3, player1);
		testLogic.insertCol(7, player2);
		testLogic.insertCol(1, player1);
		testLogic.insertCol(5, player2);
		testLogic.insertCol(5, player1);
		testLogic.insertCol(4, player2);
		assertEquals("   OO ", testLogic.backDiag());
	}
	
	/** Tests the contains method using inputs in and out of context*/
	@Test
	public void testContains() {
		
		assertTrue(testLogic.contains("backpack", "back"));
		assertFalse(testLogic.contains("Ostrich", "soy"));
		assertTrue(testLogic.contains("XO OOOX", "OX"));
		assertFalse(testLogic.contains("XOXO ","XOXO  "));
	}
	
	/** Simulates game moves and tests the winningMove method after some moves*/
	@Test
	public void testWininngMove() {
		testLogic.clear();
		
		testLogic.insertCol(7, player1);
		testLogic.insertCol(2, player2);
		testLogic.insertCol(6, player1);
		assertFalse(testLogic.winningMove());
		testLogic.insertCol(2, player2);
		testLogic.insertCol(5, player1);
		testLogic.insertCol(2, player2);
		testLogic.insertCol(4, player1); 
		assertTrue(testLogic.winningMove());
		testLogic.insertCol(2, player2);
		assertTrue(testLogic.winningMove());
		testLogic.insertCol(4, player1);
		testLogic.insertCol(3, player2);
		testLogic.insertCol(5, player1);
		testLogic.insertCol(4, player2);
		testLogic.insertCol(5, player1);
		testLogic.insertCol(3, player2);
		assertFalse(testLogic.winningMove());
		testLogic.insertCol(6, player1);
		testLogic.insertCol(5, player2);
		assertTrue(testLogic.winningMove());
		testLogic.insertCol(4, player1);
		assertTrue(testLogic.winningMove());
	}
	
	/** Tests toString method with expected input  */
	@Test public void testToString() {
		testLogic.fill('X');
		
		String expected = "|X|X|X|X|X|X|X|\n"
				+ "|X|X|X|X|X|X|X|\n"
				+ "|X|X|X|X|X|X|X|\n"
				+ "|X|X|X|X|X|X|X|\n"
				+ "|X|X|X|X|X|X|X|\n"
				+ "|X|X|X|X|X|X|X|\n";
		
		assertEquals(expected, testLogic.toString());
	}

	/** Checks to see if isFull method is working */
	@Test 
	public void testFull() {
		testLogic.clear();
		assertFalse(testLogic.isFull());
		testLogic.fill('X');
		assertTrue(testLogic.isFull());
	}
}
