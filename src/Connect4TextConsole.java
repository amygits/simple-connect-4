
import java.util.InputMismatchException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ArrayIndexOutOfBoundsException;
import java.net.Socket;
import java.util.Scanner;
import javafx.stage.Stage;
import javafx.application.*;


/** This class will create a Connect 4 Text UI that will display the Connect 4 grid, 
 *  give users the option to play against a computer player 
 *  or alternate turns between 2 players
 *  and declare a winner in either game selection
 *  
 *   @author Amy Ma
 *   @version 4.0
 *   
 */

public class Connect4TextConsole {

	/** This class will create a game of Connect4 
	 *  and give the user an option between starting a
	 *  PVP or PVComputer game 
	 *  **/
	public Connect4TextConsole()
	{
		
		Scanner colChoice = new Scanner(System.in);
		
		/** Create a new Connect4 object **/
		Connect4 aGame = new Connect4();
		
		/** Store player input once game starts */
		int playerInput;
		/** Store user input to decide on what type of game to start */
		String userInput;
		/** Dummy variable to compare against for player game */
		String p = "p";
		/** Dummy variable to compare against for computer game */
		String c = "c";
		/** Holder to check if user input correct game option*/
		boolean validInput = false;
		
		String host = "localhost";
		DataInputStream fromServer;
		DataOutputStream toServer;
		
		try{
			Socket socket = new Socket(host, 8001);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
		
		
		/** While there is no valid input from user */
		while (!validInput)
		{
			/** Display grid  
			System.out.print(aGame.toString());
			*/
			
			/** Start game */
			System.out.println("++ Begin Game ++\nEnter 'P' if you want to play against another player\nEnter 'C' if you want to play against computer.");
			userInput = colChoice.next();	
			
			/** Try block to vet userInput and handle any exceptions*/ 
			try	
			{
				if (userInput.compareToIgnoreCase(p) != 0 && userInput.compareToIgnoreCase(c) != 0)
						{ 
							throw new IllegalArgumentException("Invalid game selection.");
						}
				validInput = true;
				
			} catch (IllegalArgumentException a)
			{
				System.out.println("You didn't make a valid game selection - Please try again.\n");
			}
			
			int moves = aGame.getRows() * aGame.getCols();
			
			/** PVP game execution */
			if (userInput.compareToIgnoreCase(p) == 0)
			{
				
				for (int round = 1; round < (moves / 2); round++) 
				{
					System.out.println("Player X - Your turn.  Please choose a column number from 1-7");
					try {
						playerInput = colChoice.nextInt();
						if (playerInput > 7 || playerInput < 1)
						{
							throw new ArrayIndexOutOfBoundsException("Invalid column number.");
						}
						aGame.insertCol(playerInput, 'X');
						System.out.print(aGame.toString());
						
						if(aGame.winningMove())
						{
							System.out.println("\nPlayer X wins!");
							return;
						}
						
					}catch (ArrayIndexOutOfBoundsException a)
					{
						System.out.println("Your input was not a valid column number - Skipping turn..");
					}
					catch (InputMismatchException a)
					{
						System.out.println("Your input was not a valid column number - Skipping turn..");
					}
					
					System.out.println("Player O - Your turn.  Please choose a column number from 1-7");
					try {
						playerInput = colChoice.nextInt();
						if (playerInput > 7 || playerInput < 1)
						{
							throw new ArrayIndexOutOfBoundsException("Invalid column number.");
						}
						aGame.insertCol(playerInput, 'O');
						System.out.print(aGame.toString());
						
						if(aGame.winningMove())
						{
							System.out.println("\nPlayer O wins!");
							return;
						}
						
					}catch (ArrayIndexOutOfBoundsException a)
					{
						System.out.println("Your input was not a valid colunn number - Skipping turn..");
					}
					catch (InputMismatchException a)
					{
						System.out.println("Your input was not a valid column number - Skipping turn..");
					}
					
				}
				
				System.out.println("Game over. No winner. Try again!");
			}
			
			/** Player vs. Computer Execution */	
			if (userInput.compareToIgnoreCase(c) == 0)
			{
				Connect4ComputerPlayer player2 = new Connect4ComputerPlayer(1, 7);
				
				for (int round = 1; round < (moves / 2); round++) 
				{
					System.out.println("Player X - Your turn.  Please choose a column number from 1-7");
					try {
						playerInput = colChoice.nextInt();
						if (playerInput > 7 || playerInput < 1)
						{
							throw new ArrayIndexOutOfBoundsException("Invalid column number.");
						}
						aGame.insertCol(playerInput, 'X');
						System.out.print(aGame.toString());
						
						if(aGame.winningMove())
						{
							System.out.println("\nPlayer X wins!");
							return;
						}
						
					}catch (ArrayIndexOutOfBoundsException a)
					{
						System.out.println("Your input was not a valid column number - Skipping turn..");
					}
					catch (InputMismatchException a)
					{
						System.out.println("Your input was not a valid column number - Skipping turn..");
					}
					
					System.out.println("Computer Player turn: ");
					int compMove = player2.generateMove();
					boolean validCompMove = false;
					while (!validCompMove)
						{
							try 
							{
								aGame.insertCol(compMove, 'O');
								validCompMove = true;
							} catch (ArrayIndexOutOfBoundsException a)
							{
								validCompMove = false;
							}
						}
					System.out.print(aGame.toString());
					if(aGame.winningMove())
					{
						System.out.println("\nComputer player wins!");
						return;
					}
				}
				System.out.println("Game over. No winner. Try again!");
			}
			
		}
	}	

}

