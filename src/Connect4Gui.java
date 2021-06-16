import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.css.*;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import java.util.*;
import javafx.beans.*;

/**
 *  This class with create a Connect 4 GUI that will display the grid
 *  and give users the option to play against a computer player
 *  or alternate turns between 2 players and declare a winner
 *  
 *  Before that, it will ask the user (via Console) which UI they'd prefer
 *  
 * @author Amy Ma
 * @version 3.0
 *
 *
 */
public class Connect4Gui extends Application
{
	
	/** Store # of columns in the grid */
	private int gridCols;
	
	/** Store # of rows in the grid */
	private int gridRows;
	
	/** Create a Connect4 game */
	Connect4 aGame;
	
	/** Creates a 2D array of cell objects */
	private Cell[][] cell;
	
	/** Represents the current user turn in form of char*/
	private char currentTurn;
	
	/** Constructor */
	public Connect4Gui()
	{
		/** Instantiates class attributes */ 
		aGame = new Connect4();
		gridCols = aGame.getCols();
		gridRows = aGame.getRows();
		cell = new Cell[gridRows][gridCols];
	}

	/** 
	 * 
	 * Creates the Connect 4 Grid UI that opens up in a new window
	 * @param gameStage  
	 * */
	public void start(Stage gameStage)
	{
		

		GridPane pane = new GridPane();

		for (int i = 0; i < gridRows; i++)
		{
			for (int j = 0; j < gridCols; j++)
			{
				pane.add(cell[i][j] = new Cell(), j, i);
			}
		}

		BorderPane bPane = new BorderPane();
		bPane.setCenter(pane);

		Scene scene = new Scene(bPane, 410, 405);
		gameStage.setTitle("Connect 4"); 
		gameStage.setScene(scene);
		gameStage.show();

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
			okButton.setOnAction(e -> gameSelect(firstStage, stg1Grp, aGame));
			
			

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
	 * 
	 * Button event handling function for user selecting Game Type (PVP/PC) 
	 * @param s
	 * @param t
	 * @param game
	 * */
	
	public void gameSelect(Stage s, ToggleGroup t, Connect4 game)
	{
		s.close();
		GridPane gPane = new GridPane();
		gPane.setAlignment(Pos.CENTER.CENTER_LEFT);
		gPane.setPadding(new Insets(15, 15, 15, 15));
		gPane.setVgap(5.5);
		gPane.setHgap(20);
		Scene aScene = new Scene(gPane);
		TextField userInputField = new TextField();
		Text userInput = new Text();
		boolean validInput = false;
		currentTurn = 'X';
		
		Label gameLabel;
		Label playerTurn = new Label();
		
		/** Vs Player game Execution **/
		if (t.getSelectedToggle().getUserData().equals('P'))
		{
			
			Stage q = new Stage();
			gameLabel = new Label("PVP: ");
			gPane.add(gameLabel, 0, 0);
			playerTurn.setText("Player " + currentTurn + "\nPlease choose a column number from 1-7:");
			gPane.add(playerTurn, 0, 1);
			gPane.add(userInputField, 0, 2);
			userInputField.setOnAction(e -> fieldHandlerPVP(userInputField, game, playerTurn));
			q.setScene(aScene);
			q.show();
			
		}
		
		/** Vs computer game Execution **/
		
		if (t.getSelectedToggle().getUserData().equals('C'))
		{
			
			Stage q = new Stage();
			gameLabel = new Label("Vs. Computer: ");
			gPane.add(gameLabel, 0, 0);
			playerTurn.setText("Player " + currentTurn + "\nPlease choose a column number from 1-7:");
			gPane.add(playerTurn, 0, 1);
			gPane.add(userInputField, 0, 2);
			userInputField.setOnAction(e -> fieldHandlerComp(q, userInputField, game, playerTurn));
			q.setScene(aScene);
			q.show();
		}

	}
		
	/**
	 * 
	 * TextField event-handling function for when user is playing against the PC 
	 * @param s
	 * @param field
	 * @param game
	 * @param l
	 */
	public void fieldHandlerComp(Stage s, TextField field, Connect4 game, Label l)
	{
		Connect4ComputerPlayer player2 = new Connect4ComputerPlayer(1, 7);
		
		if (!game.isFull()) {
			try 
			{

				int userIntput = Integer.parseInt(field.getText());


				if (userIntput > 7 || userIntput < 1)
				{
					throw new ArrayIndexOutOfBoundsException("Invalid column number.");
				}

				game.insertCol(userIntput, currentTurn);
				cell[game.getLastRow()][game.getLastCol()].setToken(currentTurn);
				
				currentTurn = 'O';
				
				if(game.winningMove())
				{
					Stage winner = new Stage();
					VBox pane = new VBox();
					Scene bScene = new Scene(pane);
					pane.setAlignment(Pos.CENTER);
					pane.setPadding(new Insets(10, 10, 10, 10));
					pane.setSpacing(10);
					Button exitButton = new Button("Exit");
					exitButton.setOnAction(e -> System.exit(0));
					Label winMsg = new Label("You've won!!");
					pane.getChildren().addAll(winMsg, exitButton);
					winner.setScene(bScene);
					winner.show();
				}
				else {

				/** Computer move */
					
				Stage computersMove = new Stage();
				VBox pane = new VBox();
				Scene bScene = new Scene(pane);
				pane.setAlignment(Pos.CENTER);
				pane.setPadding(new Insets(10, 10, 10, 10));
				pane.setSpacing(10);
				Button exitButton = new Button("Exit");
				exitButton.setOnAction(new EventHandler<ActionEvent>() { 
					public void handle (ActionEvent e)
					{
						computersMove.close();
						int compInput = player2.generateMove();
						game.insertCol(compInput, currentTurn);
						cell[game.getLastRow()][game.getLastCol()].setToken(currentTurn);
						
						if(game.winningMove())
						{
							Stage winner = new Stage();
							VBox pane = new VBox();
							Scene bScene = new Scene(pane);
							pane.setAlignment(Pos.CENTER);
							pane.setPadding(new Insets(10, 10, 10, 10));
							pane.setSpacing(10);
							Button exitButton = new Button("Exit");
							exitButton.setOnAction(f -> System.exit(0));
							Label winMsg = new Label("Computer player has won");
							pane.getChildren().addAll(winMsg, exitButton);
							winner.setScene(bScene);
							winner.show();
						}
						field.clear();
						currentTurn = 'X';
					}
				});
				Label computerPlayMsg = new Label("Computer player has made its move.");
				pane.getChildren().addAll(computerPlayMsg, exitButton);
				computersMove.setScene(bScene);
				computersMove.show();
				}

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
		}
		
		else if (game.isFull())
		{
			Stage draw = new Stage();
			VBox pane = new VBox();
			Scene nScene = new Scene(pane);
			pane.setAlignment(Pos.CENTER);
			pane.setPadding(new Insets(10, 10, 10, 10));
			pane.setSpacing(10);
			Button okButton = new Button("OK.");
			okButton.setOnAction(e -> draw.close());
			Label drawMsg = new Label("It's a tie game!");
			pane.getChildren().addAll(drawMsg, okButton);
			draw.setScene(nScene);
			draw.show();
		}
		
	}
	
	/*
	 * TextField event-handling function for when user is playing against another user
	 * @param field
	 * @param game
	 * @param l 
	 */
	public void fieldHandlerPVP(TextField field, Connect4 game, Label l)
	{
		if (!game.isFull()) {
			try 
			{

				int userIntput = Integer.parseInt(field.getText());


				if (userIntput > 7 || userIntput < 1)
				{
					throw new ArrayIndexOutOfBoundsException("Invalid column number.");
				}

				game.insertCol(userIntput, currentTurn);
				cell[game.getLastRow()][game.getLastCol()].setToken(currentTurn);

				field.clear();

				if(currentTurn == 'X')
				{ currentTurn = 'O'; }
				else 
				{ currentTurn = 'X'; }

				l.setText("Player " + currentTurn + "\nPlease choose a column number from 1-7:");

				if(game.winningMove())
				{
					Stage winner = new Stage();
					VBox pane = new VBox();
					Scene bScene = new Scene(pane);
					pane.setAlignment(Pos.CENTER);
					pane.setPadding(new Insets(10, 10, 10, 10));
					pane.setSpacing(10);
					Button exitButton = new Button("Exit");
					exitButton.setOnAction(e -> System.exit(0));
					Label errmsg = new Label("Player " + currentTurn + " wins!!");
					pane.getChildren().addAll(errmsg, exitButton);
					winner.setScene(bScene);
					winner.show();
				}

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
		}
		
		else if (game.isFull())
		{
			Stage draw = new Stage();
			VBox pane = new VBox();
			Scene nScene = new Scene(pane);
			pane.setAlignment(Pos.CENTER);
			pane.setPadding(new Insets(10, 10, 10, 10));
			pane.setSpacing(10);
			Button okButton = new Button("OK.");
			okButton.setOnAction(e -> draw.close());
			Label drawMsg = new Label("It's a tie game!");
			pane.getChildren().addAll(drawMsg, okButton);
			draw.setScene(nScene);
			draw.show();
		}
		
	}
		


/*
 *   An inner class for a cell
 */
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

}

	
	



