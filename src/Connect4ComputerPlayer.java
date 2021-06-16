import java.util.Random;

/** This class will create a Connect 4 Computer Player object 
 *  with a method that will generate a Connect 4 column 
 *  move randomly based on the range of the columns
 *  
 *   @author Amy Ma
 *   @version 4.0
 *   
 */

public class Connect4ComputerPlayer {
	/** Lowest point of column range */
	private int min;
	/** Highest point of column range */
	private int max;
	

	/**
	 * Default constructor with a column range of 1 - 23
	 */
	public Connect4ComputerPlayer()
	{
		min = 1;
		max = 23;
	}
	
	/**
	 * Overloaded constructor that assigns new min/max column range
	 * @param minX
	 * @param maxX
	 */
	public Connect4ComputerPlayer(int minX, int maxX)
	{
		min = minX;
		max = maxX;
	}
	
	/**
	 * @return a random generated int between the min and max range
	 */
	public int generateMove()
	{
		return new Random().nextInt(max - min + 1) + min;
	}
	
}


