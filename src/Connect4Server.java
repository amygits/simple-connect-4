import java.awt.BorderLayout;
import java.io.*;
import java.net.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*; 
import java.util.Date;

/**
 * This class will create and launch a Connect 4 server that will
 * send info to local Connect 4 clients
 * @author Amy Ma
 * @version 1.0
 *
 */
public class Connect4Server extends Application implements Connect4Constants {

	private int sessionNo = 1;
	private char UIchoice;

	@Override
	public void start(Stage primaryStage) throws ArrayIndexOutOfBoundsException
	{
		/** Creating server log window */
		TextArea serverLog = new TextArea();
		Scene serverScene = new Scene(new ScrollPane(serverLog), 420, 240);
		primaryStage.setScene(serverScene);
		primaryStage.show();

		/** Starting new thread 
		 * And opening up a socket at port 8001
		 * */
		new Thread( () -> {
			try {
				ServerSocket serverSocket = new ServerSocket(8001);
				Platform.runLater(() -> serverLog.appendText(new Date() 
						+ ": Server started at 8001\n"));

				while (true)
				{
					Socket player1 = serverSocket.accept();

					Platform.runLater(() -> serverLog.appendText(new Date() 
							+ ": Starting session" + sessionNo + '\n'));

					InetAddress inetaddress1 = player1.getInetAddress();

					Platform.runLater(() -> serverLog.appendText(new Date() 
							+ ": Player 1 " + inetaddress1.getHostName() + " joined server from " 
							+ inetaddress1.getHostAddress() + '\n'));


					new DataOutputStream(player1.getOutputStream()).writeInt(PLAYER1);

					/**/int P1Game = new DataInputStream(player1.getInputStream()).readInt();

					if (P1Game == vsCOMPUTER)
					{
						Platform.runLater(() -> serverLog.appendText(new Date() 
								+ ": Launching computer game for player 1 - Session: " + sessionNo++ + '\n'));
						Thread thread = new Thread (new runGameC(player1));
						thread.start();
					}
					/**/
					Socket player2 = serverSocket.accept();

					InetAddress inetaddress2 = player2.getInetAddress();

					Platform.runLater(() -> serverLog.appendText(new Date() 
							+ ": Player 2 " + inetaddress1.getHostName() + " joined server from " 
							+ inetaddress2.getHostAddress() + '\n'));

					new DataOutputStream(player2.getOutputStream()).writeInt(PLAYER2);

					/**/int P2Game= new DataInputStream(player2.getInputStream()).readInt();

					if (P2Game == vsCOMPUTER)
					{
						Platform.runLater(() -> serverLog.appendText(new Date() 
								+ ": Launching computer game for player 2 - Session: " + sessionNo++ + '\n'));
						Thread thread = new Thread (new runGameC(player1));
						thread.start();
					}
					else 
					{
						Platform.runLater(() -> serverLog.appendText(new Date() 
								+ ": Starting a new thread for session " + sessionNo++ + '\n'));

						Thread thread = new Thread(new runGameP(player1, player2));
						thread.start();
					}
					/**/		
				}
			} catch (IOException ex)
			{
				ex.printStackTrace();

			} catch (ArrayIndexOutOfBoundsException e)
			{
				throw e;
			}

		}).start();
	}


	/**
	 * This task starts the thread for a versus computer 
	 * Connect 4 game with a client
	 * @author Amy
	 *
	 */
	class runGameC implements Runnable, Connect4Constants {

		private Socket player;

		Connect4 aGame;
		Connect4ComputerPlayer computer;

		private DataInputStream fromPlayer;
		private DataOutputStream toPlayer;

		private boolean continueToPlay = true;

		public runGameC(Socket player)
		{
			this.player = player;
			aGame = new Connect4();
			computer = new Connect4ComputerPlayer(1, 7);
		}

		public void run()
		{
			try {
				
				//boolean valid;
				fromPlayer = new DataInputStream(player.getInputStream());
				toPlayer = new DataOutputStream(player.getOutputStream());

				while (true)
				{
					int playerCol = fromPlayer.readInt();
					aGame.insertCol(playerCol, 'X');
					int row = aGame.getLastRow();
					int col = aGame.getLastCol();

					if (aGame.winningMove())
					{
						toPlayer.writeInt(PLAYER1WINS);
						sendMove(toPlayer, row, col);
						break;
					}

					else if (aGame.isFull())
					{
						toPlayer.writeInt(DRAW);
						sendMove(toPlayer, row, col);
						break;
					}
					// If move is not win or draw, then add move to client grids
					else 
					{
						toPlayer.writeInt(WAIT);
						sendMove(toPlayer, row, col);
						
						try {
							Thread.sleep(800);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						int cMove = computer.generateMove();
						aGame.insertCol(cMove, 'O');
						int cRow = aGame.getLastRow();
						int cCol = aGame.getLastCol();

						
						if(aGame.winningMove())
						{
							toPlayer.writeInt(PLAYER2WINS);
							sendMove(toPlayer, cRow, cCol);
							break;
						}
						else
						{
							toPlayer.writeInt(CONTINUE);
							sendMove(toPlayer, cRow, cCol);
						} 
					}
				}
					
					

			} catch (IOException e)
			{
				e.printStackTrace();
			} 
			
			
		}
		
		public void computerMove()
		{
			int move = computer.generateMove();
			aGame.insertCol(move, 'O');
			
		}

		public void sendMove(DataOutputStream out, int row, int column) throws IOException
		{	
			out.writeInt(row);
			out.writeInt(column);
		}

	}


	/**
	 * This task starts the thread for a versus computer 
	 * Connect 4 game with a client
	 * @author Amy
	 */
	class runGameP implements Runnable, Connect4Constants {

		private Socket player1;
		private Socket player2;

		Connect4 aGame;

		private DataInputStream fromPlayer1;
		private DataOutputStream toPlayer1;
		private DataInputStream fromPlayer2;
		private DataOutputStream toPlayer2;

		private boolean continueToPlay = true;

		public runGameP(Socket player1, Socket player2)
		{
			this.player1 = player1;
			this.player2 = player2;
			aGame = new Connect4();
		}


		public void run()
		{
			try {
				fromPlayer1 = new DataInputStream(player1.getInputStream());
				toPlayer1 = new DataOutputStream(player1.getOutputStream());
				fromPlayer2 = new DataInputStream(player2.getInputStream());
				toPlayer2 = new DataOutputStream(player2.getOutputStream());

				toPlayer1.writeInt(1);


				while (true)
				{
					int playerCol = fromPlayer1.readInt();
					aGame.insertCol(playerCol, 'X');
					int row = aGame.getLastRow();
					int col = aGame.getLastCol();

					if (aGame.winningMove())
					{
						toPlayer1.writeInt(PLAYER1WINS);
						toPlayer2.writeInt(PLAYER1WINS);
						sendMove(toPlayer1, row, col);
						sendMove(toPlayer2, row, col);
						break;
					}

					else if (aGame.isFull())
					{
						toPlayer1.writeInt(DRAW);
						toPlayer2.writeInt(DRAW);
						sendMove(toPlayer2, row, col);
						sendMove(toPlayer1, row, col);
						break;
					}
					// If move is not win or draw, then add move to both client grids
					else {
						toPlayer1.writeInt(WAIT);
						toPlayer2.writeInt(CONTINUE);
						sendMove(toPlayer1, row, col);
						sendMove(toPlayer2, row, col);
					}

					playerCol = fromPlayer2.readInt();
					aGame.insertCol(playerCol, 'O');
					row = aGame.getLastRow();
					col = aGame.getLastCol();

					if(aGame.winningMove())
					{
						toPlayer1.writeInt(PLAYER2WINS);
						toPlayer2.writeInt(PLAYER2WINS);
						sendMove(toPlayer1, row, col);
						sendMove(toPlayer2, row, col);
						break;
					}
					else
					{
						toPlayer1.writeInt(CONTINUE);
						toPlayer2.writeInt(WAIT);
						sendMove(toPlayer2, row, col);
						sendMove(toPlayer1, row, col);
					}

				}

			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		private void sendMove(DataOutputStream out, int row, int column) throws IOException
		{	
			out.writeInt(row);
			out.writeInt(column);
		}
	}


	public static void main(String[] args)
	{
		launch(args);	
	}

}
