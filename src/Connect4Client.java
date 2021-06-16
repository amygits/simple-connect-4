import java.io.*;
import java.lang.management.PlatformLoggingMXBean;
import java.net.*;
import javafx.application.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.css.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.*; 
import java.util.Date;
import java.util.Scanner;

/*
** This class contains a local client for the Connect 4 game GUI
*  It will prompt the user for preferred UI and launches the GUI 
*  And opens a socket connection to local server at port 8001
* 
* @author Amy Ma
* @version 1.0
*/


public class Connect4Client extends Application implements Connect4Constants {
	
	/** Grid and Stage variables **/
	private Cell[][] cell = new Cell[6][7];
	private int row, col;
	private BorderPane bPane;
	private GridPane pane;
	private Label lblStatus = new Label();
	private Label lblTitle = new Label();
	Scene scene;

	/** 'X'/'O' holders */
	private char currentToken;
	private char otherToken;

	/** Socket/connection symbols */
	public String host = "localhost";
	private DataInputStream fromServer;
	private DataOutputStream toServer;

	/** Game turn indicators */
	private boolean myTurn = false;
	private boolean continueToPlay = true;

	/** Holders for receiving player and client status from server*/
	private int player;
	private int status;

	/** Starts the program */
	public void start(Stage clientStage)
	{
		/** Creates the window that asks user for type of vs. game */
		pane = new GridPane();
		for (int i = 0; i < 6; i++)
		{
			for (int j = 0; j < 7; j++)
			{
				pane.add(cell[i][j] = new Cell(), j, i);
			}
		}

		bPane = new BorderPane();
		bPane.setCenter(pane); 
		bPane.setTop(lblTitle);
		bPane.setBottom(lblStatus);
		scene = new Scene(bPane, 410, 405);
		clientStage.setTitle("Connect 4"); 
		clientStage.setScene(scene);
		clientStage.show();

		Stage firstStage = new Stage();
		Text gameOptions = new Text("Choose who you're playing against:");

		ToggleGroup stg1Grp = new ToggleGroup();
		RadioButton option1 = new RadioButton("Player");
		option1.setToggleGroup(stg1Grp);
		option1.setUserData('P');
		option1.setSelected(true);
		RadioButton option2 = new RadioButton("Computer");
		option2.setUserData('C');
		option2.setToggleGroup(stg1Grp);

		Button okButton = new Button("OK");
		okButton.setOnAction(e -> gameSelect(firstStage, stg1Grp));


		HBox hBox = new HBox(20);
		hBox.getChildren().add(option1);
		hBox.getChildren().add(option2);

		VBox vBox = new VBox(25);
		vBox.getChildren().addAll(gameOptions, hBox, okButton);

		Scene aScene = new Scene(vBox, 305, 125);
		firstStage.setTitle("Connect 4 Start");
		firstStage.setScene(aScene);
		firstStage.show();
	}
	
	/**
	 * Action method for the versus game window
	 * @param s s should be current stage so it will close on action
	 * @param t Pulls data from ToggleGroup
	 */
	public void gameSelect(Stage s, ToggleGroup t)
	{
		s.close();

		if (t.getSelectedToggle().getUserData().equals('P'))
		{
			connectToServerPVP();
		}

		if (t.getSelectedToggle().getUserData().equals('C'))
		{
			connectToServerComp();
		}

	}

	/**
	 * Connects to server and alerts the server to start a computer game
	 */
	public void connectToServerComp() {

		try 
		{
			Socket socket = new Socket(host, 8001);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());

			toServer.writeInt(vsCOMPUTER);

		} catch (IOException ex)
		{
			ex.printStackTrace();
		}

		Thread thread = new Thread(new clientHandlingComputer(fromServer, toServer));
		thread.start();

	}
	/**
	 * Connects to server and alerts the server to start a PVP game
	 */
	public void connectToServerPVP() {

		try 
		{
			Socket socket = new Socket(host, 8001);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());

			toServer.writeInt(vsPLAYER);

		} catch (IOException ex)
		{
			ex.printStackTrace();
		}

		Thread thread = new Thread(new clientHandlingPlayer(fromServer, toServer));
		thread.start();
	}

	/**
	 * Receives info from server 
	 * Updates client status and game GUI 
	 * Called only during versus player option
	 * @throws IOException
	 */
	public void receiveInfoFromServer() throws IOException
	{
		status = fromServer.readInt();

		if (status == PLAYER1WINS)
		{	
			if (player == PLAYER1) 
			{ 	setMove(currentToken);
			Platform.runLater(() -> lblStatus.setText("You've won!")); 
			} 
			else if (player == PLAYER2) { 
				setMove(otherToken);
				Platform.runLater(() -> lblStatus.setText("Game over - Player X has won!"));
			}

			continueToPlay = false;
		}

		else if (status == PLAYER2WINS)
		{

			if (player == PLAYER2) 
			{ 
				Platform.runLater(() -> lblStatus.setText("You've won!")); 
				setMove(currentToken);

			}
			else if (player == PLAYER1) { 
				setMove(otherToken);
				Platform.runLater(() -> lblStatus.setText("Game over - Player O has won!")); 
			}
			continueToPlay = false;
		}

		else if (status == DRAW)
		{

			Platform.runLater(() -> lblStatus.setText("Game over - It's a tie!"));
			setMove(otherToken);
			continueToPlay = false;

		}
		else if (status == WAIT)
		{

			setMove(currentToken);
			Platform.runLater(() -> lblStatus.setText("Other player's turn."));
			myTurn = false; 

		}

		else if (status == CONTINUE)
		{
			setMove(otherToken);
			Platform.runLater(() -> lblStatus.setText("Your turn."));
			myTurn = true;
		}
	}

	/**
	 * Receives info from server 
	 * Updates client status and game GUI 
	 * Called only during versus computer option
	 * @throws IOException
	 */
	public void receiveInfoFromServerC() throws IOException
	{
		status = fromServer.readInt();

		if (status == PLAYER1WINS)
		{	
			setMove(currentToken);
			Platform.runLater(() -> lblStatus.setText("You've won!")); 
			continueToPlay = false;
		}

		else if (status == PLAYER2WINS)
		{
			setMove(otherToken);
			Platform.runLater(() -> lblStatus.setText("Game over - Player O has won!"));
			continueToPlay = false;
		}

		else if (status == DRAW)
		{
			Platform.runLater(() -> lblStatus.setText("Game over - It's a tie!"));
			setMove(otherToken);
			continueToPlay = false;
		}
		else if (status == WAIT)
		{
			setMove(currentToken);
			Platform.runLater(() -> lblStatus.setText("Other player's turn."));
			myTurn = false; 
		}
		else if (status == CONTINUE)
		{
			setMove(otherToken);
			Platform.runLater(() -> lblStatus.setText("Your turn."));
			myTurn = true;
		}
	}

	/**
	 * @param token Sets token at position on game board based on info from server
	 * @throws IOException
	 */
	public void setMove(char token) throws IOException
	{	
		row = fromServer.readInt();
		col = fromServer.readInt();
		Platform.runLater(() -> cell[row][col].setToken(token));
	}

	
	/**
	 * @author Amy
	 * Thread that starts a versus player game
	 */
	class clientHandlingPlayer implements Runnable {
	
		private DataInputStream in;
		private DataOutputStream out;	
	
		public clientHandlingPlayer(DataInputStream in, DataOutputStream out)
		{
			this.in = in;
			this.out = out;
		}
	
		public void run()
		{
			try {
	
				player = in.readInt();
	
	
				if (player == PLAYER1)
				{
					currentToken = 'X';
					otherToken = 'O';
					Platform.runLater(() -> { lblTitle.setText("Player 1 - Token: X");
					lblStatus.setText("Waiting for player 2 to join.");
					});
	
					myTurn = true;
					in.readInt(); 
					Platform.runLater(() ->  lblStatus.setText("Player 2 has joined.  Your turn."));
				}
	
				else if (player == PLAYER2)
				{
					currentToken = 'O';
					otherToken = 'X';
					Platform.runLater(() -> { 
						lblTitle.setText("Player 2 - Token: O");
						lblStatus.setText("Waiting for player 1 to move.");
					});
	
				}
	
	
				while (continueToPlay)
				{
					if (player == PLAYER1) {
	
						if (myTurn)
						{
							Platform.runLater( () -> new newPlayWindow(currentToken, out).show());
						}
						receiveInfoFromServer();
					}
	
					else if (player == PLAYER2)
					{
						if (myTurn) 
						{
							Platform.runLater( () -> new newPlayWindow(currentToken, out).show());
						}
						receiveInfoFromServer();
					}
				}
	
			} catch (IOException ex)
			{
				ex.printStackTrace();
			} 
		}
	
	
	}

	
	/**
	 * @author Amy
	 * Thread that starts a versus computer game
	 *
	 */
	class clientHandlingComputer implements Runnable 
	{
		private DataInputStream in;
		private DataOutputStream out;
	
		public clientHandlingComputer(DataInputStream in, DataOutputStream out)
		{
			this.in = in;
			this.out = out;
		}
	
		public void run()
		{
			try {
				player = in.readInt();
	
				currentToken = 'X';
				otherToken = 'O';
				myTurn = true;
				Platform.runLater(() ->
				{ 
					lblTitle.setText("Player 1 - Token: X");
					lblStatus.setText("My turn.");
				});
	
	
				while (continueToPlay)
				{
					if (player == PLAYER1)
					{
						if(myTurn)
						{
							Platform.runLater( () -> new newPlayWindow(currentToken, out).show());
						}
						receiveInfoFromServerC();
					}
					
					else if (player == PLAYER2)
					{
						if(myTurn)
						{
							Platform.runLater( () -> new newPlayWindow(currentToken, out).show());
						}

						receiveInfoFromServerC();
					}
				}
				
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	private class exitOnCloseWindow
	{
	
		private Stage q;
		private VBox pane;
		private Scene bScene;
		Label msg;
		Button closeButton;
	
		protected exitOnCloseWindow(String label)
		{
			q = new Stage();
			pane = new VBox();
			bScene = new Scene(pane);
			msg  = new Label(label);
			closeButton = new Button("Close");
			closeButton.setOnAction(e -> q.close());
			q.setScene(bScene);
			pane.setAlignment(Pos.CENTER);
			pane.setPadding(new Insets(10, 10, 10, 10));
			pane.setSpacing(10);
			pane.getChildren().addAll(msg, closeButton);
			q.setScene(bScene);
		}
		public void show()
		{
			q.show();
		}
		public void close()
		{
			q.close();
		}
	}

	public class newPlayWindow
	{
		private GridPane gPane;
		private Scene aScene;
		private Stage aStage;
		private TextField userInputField;
		private Label gameLabel;
		private Label playerTurn;
		private char currentToken;
		private int userIntput;
		DataOutputStream out;
	
		public newPlayWindow(char currentToken, DataOutputStream out) {
	
			aStage = new Stage();
			this.out = out;
			gPane = new GridPane();
			gPane.setAlignment(Pos.CENTER.CENTER_LEFT);
			gPane.setPadding(new Insets(15, 15, 15, 15));
			gPane.setVgap(5.5);
			gPane.setHgap(20);
			aScene = new Scene(gPane);
			userInputField = new TextField();
			playerTurn = new Label("Player " + currentToken + ", please use a column between 1-7: ");
			gameLabel = new Label("Your turn!\n ");
			this.currentToken = currentToken;
			userInputField.setOnAction(e -> fieldHandler(aStage, userInputField));
	
			gPane.add(gameLabel, 0, 0);
			gPane.add(playerTurn, 0, 1);
			gPane.add(userInputField, 0, 2);
			aStage.setScene(aScene);
		}
	
		public void show()
		{
			aStage.show();
		}
	
		private void fieldHandler(Stage s, TextField field) 
		{
			
			try {
	
				userIntput = Integer.parseInt(field.getText());
	
				if (userIntput > 7 || userIntput < 1)
				{
					throw new ArrayIndexOutOfBoundsException("Invalid column number.");
				}
				out.writeInt(userIntput);
				s.close();
				myTurn = false;
	
				
	
			} catch(NumberFormatException a)
			{
				Stage error = new Stage();
				VBox pane = new VBox();
				Scene nScene = new Scene(pane);
				pane.setAlignment(Pos.CENTER);
				pane.setPadding(new Insets(10, 10, 10, 10));
				pane.setSpacing(10);
				Button okButton = new Button("OK.");
				okButton.setOnAction(e -> error.close());
				Label errmsg = new Label("Not a valid number.  Try again..");
				pane.getChildren().addAll(errmsg, okButton);
	
				error.setScene(nScene);
				error.show();
			}
	
			catch (ArrayIndexOutOfBoundsException a)
			{
				Stage error = new Stage();
				VBox pane = new VBox();
				Scene nScene = new Scene(pane);
				pane.setAlignment(Pos.CENTER);
				pane.setPadding(new Insets(10, 10, 10, 10));
				pane.setSpacing(10);
				Button okButton = new Button("OK.");
				okButton.setOnAction(e -> error.close());
				Label errmsg = new Label("Column number out of range.  Try again..");
				pane.getChildren().addAll(errmsg, okButton);
	
				error.setScene(nScene);
				error.show();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	
		public int getUserCol()
		{
			return userIntput;
		}
	
	}

	public class exitAppWindow
	{
	
		private Stage q;
		private VBox pane;
		private Scene bScene;
		Label msg;
		Button closeButton;
	
		public exitAppWindow(String label)
		{
			q = new Stage();
			pane = new VBox();
			bScene = new Scene(pane);
			msg  = new Label(label);
			closeButton = new Button("Close");
			closeButton.setOnAction(e -> System.exit(0));
			q.setScene(bScene);
			pane.setAlignment(Pos.CENTER);
			pane.setPadding(new Insets(10, 10, 10, 10));
			pane.setSpacing(10);
			pane.getChildren().addAll(msg, closeButton);
			q.setScene(bScene);
		}
	
		public void show()
		{
			q.show();
		}
		public void close()
		{
			q.close();
		}
	}

	public class Cell extends Pane
	{
		// Token used for this cell
		private char token = ' ';
	
		public Cell()
		{
			setStyle("-fx-border-color: black");
			this.setPrefSize(2000, 2000);
		}
	
		//Return token
		public char getToken() {
			return token;
		}
	
		// Set a new token
		public void setToken(char c)
		{
			token = c;
	
			if (token == 'X')
			{
				Line line1 = new Line(10, 10, this.getWidth() - 10, this.getHeight() - 10);
				line1.endXProperty().bind(this.widthProperty().subtract(10));
				line1.endYProperty().bind(this.heightProperty().subtract(10));
				Line line2 = new Line(10, this.getHeight() - 10, this.getWidth() - 10, 10);
				line2.startYProperty().bind(this.heightProperty().subtract(10));
				line2.endXProperty().bind(this.widthProperty().subtract(10));
	
				// Add the lines to the pane
				this.getChildren().addAll(line1, line2);
			}
			else if (token == 'O') {
				Ellipse ellipse = new Ellipse(this.getWidth() / 2,
						this.getHeight() / 2, this.getWidth() / 2 - 10,
						this.getHeight() / 2 - 10);
				ellipse.centerXProperty().bind(this.widthProperty().divide(2));
				ellipse.centerYProperty().bind(this.heightProperty().divide(2));
				ellipse.radiusXProperty().bind(this.widthProperty().divide(2).subtract(10));
				ellipse.radiusYProperty().bind(this.heightProperty().divide(2).subtract(10));
				ellipse.setStroke(Color.BLACK);
				ellipse.setFill(Color.WHITE);
				getChildren().add(ellipse); // Add the ellipse to the pane
			}
	
		}
	}

	public static void main(String[] args)
	
	{
		Scanner userIn = new Scanner(System.in);
	
		String userInput;
		/** Dummy variable to compare against for Console game */
		String a = "A";
		/** Dummy variable to compare against for GUI game */
		String b = "B";
	
	
		boolean validInput = false;
	
	
		/** Prompting user for UI preference (Via Console) */
		while (!validInput) 
		{
	
			System.out.print("Please choose whether you'd like a Console UI ('A') or a Graphical UI('B'): ");
			userInput = userIn.next();
			try	
			{
				if (userInput.compareToIgnoreCase(a) != 0 && userInput.compareToIgnoreCase(b) != 0)
				{ 
					throw new IllegalArgumentException("Invalid game selection.");
				}
				validInput = true;
	
			} catch (IllegalArgumentException ex)
			{
				System.out.println("You didn't make a valid UI selection - Please try again.");
			}
	
			if (userInput.compareToIgnoreCase(a) == 0)
			{
				Connect4TextConsole consoleGame = new Connect4TextConsole();
			}
	
			if (userInput.compareToIgnoreCase(b) == 0)
			{
	
				launch(args);
			}
		}
	}
}

