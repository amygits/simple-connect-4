
/** This contains the logic for the Connect4 game -
 *  This class will create the grid, input user choices 
 *  into the grid, and confirm whether or not the last move 
 *  made was a winning move 
 * 
 * @author Amy Ma
 * @version 4.0
 */

public class Connect4   {
	
	/** 2D Array "Grid" for Connect 4 game*/
	private char[][] grid; 
	/** Number of rows for grid */
	private int rows;
	/** Number of columns for grid */
	private int cols;
	/** Holds value of last column that input  */
	private int lastCol;
	/** Holds value of last row that input  */
	private int lastRow;
	
	/** This creates a new Connect 4 6x7 (By default) 
	 *  grid and fills each space with space ' ' **/
	
	public Connect4() 
	{
		rows = 6;
		cols = 7;
		lastCol = -1;
		lastRow = -1;
		
		grid = new char[rows][cols];
		
		for (int col = 0; col < cols; col++)
		{
			for (int row = 0; row < rows; row++)
			{
				grid[row][col] = ' ';
			}
		}
	}
	
	/** 
	 * 
	 * @return the row # that last received an input 
	 * */
	public int getLastRow() { return lastRow; }
	
	/** 
	 * 
	 * @return the column # that last received an input 
	 * */
	
	public int getLastCol() { return lastCol; }
	
	/** Returns number of rows
	 * @return rows
	 */
	
	public int getRows() { return rows; }
	
	/** Returns number of columns
	 * @return columns
	 */
	public int getCols( ) { return cols; }
	
	/** Inserts the player symbol into a column at the available row
	 * @param colNum
	 * @param player
	 */
	public void insertCol(int colNum, char player) 
	{
		colNum -= 1;
		int rowHolder = 5;	
		
		if (colNum < 0 || colNum > 6)
		{
			throw new ArrayIndexOutOfBoundsException("Invalid column number.");
		}
		
		
		while (grid[rowHolder][colNum] != ' ')
		{
			rowHolder--;
			
			if (rowHolder < 0)
			{
				throw new ArrayIndexOutOfBoundsException("Column number not available.");
			}
		}
		
		grid[rowHolder][colNum] = player;
		
		lastCol = colNum;
		lastRow = rowHolder;
	}
	
	
	/** 
	 * @return string of values contained in the last input row (Horizontal string)
	 */
	public String horizontal()
	{
		String horiStr = "";

		for (int x = 0; x < cols; x++)
		{
			horiStr += grid[lastRow][x];
		}

		return horiStr;
	}
	
	/**
	 * @return string of values contained in last input column (Vertical string)
	 */
	public String vertical()
	{
		String colString = "";
		
		for (int x = 0; x < rows; x++)
		{
			colString += grid[x][lastCol];
		}
		
		return colString;
	}
	
	/**
	 * @return string of values from a diagonal line through the last input's location (row, column) 
	 */
	public String forwardDiag()
	{
		String diagString = "";
		
		for (int x = 0; x < rows; x++)
		{
			int y = lastCol + lastRow - x;
			
			if (0 <= y && y < cols)
			{
				diagString += grid[x][y];
			}
		}
		
		return diagString;
	}
	
	/**
	 * @return string of values from a reverse diagonal line through the last input's location (row, column)
	 */
	public String backDiag()
	{
		
		String backString = "";
		
		for (int x = 0; x < rows; x++)
		{
			int y = lastCol - lastRow + x;
			
			if (0 <= y && y < cols)
			{
				backString += grid[x][y];
			}
		}
		
		return backString;
	}
	
	/**
	 * 
	 * @param str
	 * @param substr
	 * @return whether or not a string contains a substring
	 */
	public static boolean contains(String str, String substr)
	{
		return str.indexOf(substr) >= 0;
	}
	
	/**
	 * @return whether or not the most recent input (row, column) was a winning move (4 matching chars in a row)
	 */
	public boolean winningMove()
	{
		if (lastCol == -1)
		{
			System.err.println("No move has been made yet.");
			return false;
		}
		
		char lastPlay = grid[lastRow][lastCol];
		String streak = String.format("%c%c%c%c", lastPlay, lastPlay, lastPlay, lastPlay);
		
		return contains(horizontal(), streak) || contains(vertical(), streak) || contains(forwardDiag(), streak) || contains(backDiag(), streak);
	}

	public String toString() 
	{
		String gridContents = "";
		
		for (int row = 0; row < rows; row++)
		{
			for(int column = 0; column < cols; column++)
			{
				gridContents += "|" + grid[row][column];
			}
			
			gridContents += "|\n";
		}
		
		return gridContents;
	}
	
    public boolean isFull()
    {
	 	for (int i = 0; i < rows; i++)
	 		for (int j = 0; j < cols; j++)
	 		    if (grid[i][j] == ' ')
	 		    	return false;
	
	 	return true;
    }
    
    
    /** for testing only - Not used during run-time**
     * Clears the Connect4 grid (Replaces with ' ')
     * Resets lastRow/lastCol variables
     *  */
    protected void clear() {
    	for (int i = 0; i < rows; i++)
	 		for (int j = 0; j < cols; j++)
	 		    grid[i][j] = ' ';
    	
    	lastCol = -1;
    	lastRow = -1;
    }
    
    /** for testing only - Not used during run-time **
     * Fills the Connect4 grid with 'X'
     *  */
    protected void fill(char fillChar) {
    	for (int i = 0; i < rows; i++)
	 		for (int j = 0; j < cols; j++)
	 		    grid[i][j] = fillChar;
    }
    
    /* for testing only - Not used during run-time **
     * Returns contents at grid[row][col]
     *  
    protected char getAt(int row, int col){
    	return grid[row][col];
    }
    
    // for testing only - Not used during run-time **
     * Returns boolean value based on whether or not game grid is empty
     * 
    protected boolean isEmpty() {
    	boolean result = true;
    	
    	for (int x = 0; x < rows; x++) {
    		for (int y = 0; x < cols; y++) {
    			if (grid[x][y] != ' ') {
    				result = false;
    			}
    		}
    	}
    	return result;
    }
    */
    
 
}
