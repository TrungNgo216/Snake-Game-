/**
 * File: Streamline.java
 * Author: Trung Ngo
 * Login: cs8bfa19dh
 * Date: November 2, 2019
 * Sources of Help:
 *
 * File that uses class Streamline to implement methods that manipulate
 * GameState objects. Allows game, StreamLine, to be played (snake-game)
 */
import java.util.*;
import java.io.*;

/**
 * Contains 2 constructors, 5 methods, 3 final static variables and 2 
 * instance variables. This class manipulates GameState objects to 
 * create a Streamline object with default settings including dimensions
 * and corresponding game pieces (zappers, obstacles, goal, etc.).
 * This game can be loaded up or saved and played on a visual terminal.
 */
public class Streamline
{
    /* Provided constants */
    final static int DEFAULT_HEIGHT = 6;
    final static int DEFAULT_WIDTH = 5;
    final static String OUTFILE_NAME = "saved_streamline_game";

    /* Add your `final static` constants here */


    /* Instance variables, do not add any */
    GameState currentState;
    List<GameState> previousStates;

	/** 
     * Constructor that initializes currentState to final static
     * variables and corresponding rows and columns of the player and 
     * goal character
     * 
     * @param none
     * @return none 
     */
    public Streamline()
    { 
		//Initializing currentState properties with GameState params
		currentState = new GameState(DEFAULT_HEIGHT, DEFAULT_WIDTH,
		DEFAULT_HEIGHT - 1, 0 , 0, DEFAULT_WIDTH - 1);
	
		//Adds 3 random obstacles and zappers
        currentState.addRandomObstacles(3);
        currentState.addRandomZappers(3);
        
        //Creates an empty list of GameStates
        previousStates = new ArrayList <GameState>();
    }

	/** 
     * Constructor that loads a Streamline object from a file
     * 
     * @param filename - file with proper game format
     * @return none 
     */
    public Streamline(String filename)
    {
		try
		{
		   loadFromFile(filename);
		}
		catch (IOException e)
		{
		   e.printStackTrace();
		}
		this.previousStates = new ArrayList<GameState>();
	}

	/** 
     * Method to load a Streamline object from a file
     * 
     * @param filename - file with proper game format
     * @return none
     */
    protected void loadFromFile(String filename) throws IOException
    {
		//Scans file and initializes the first 6 integers found, then
		//places scanner on the next line and creates a new GameState
        Scanner scan = new Scanner(new File(filename));
		
		int height = scan.nextInt();
		int width = scan.nextInt();
		int playerRow = scan.nextInt();
		int playerCol = scan.nextInt();
		int goalRow = scan.nextInt();
		int goalCol = scan.nextInt();
		scan.nextLine();
		
		currentState = new GameState(height, width, playerRow, 
		playerCol, goalRow, goalCol);

		//Prints the rows and columns of the new GameState
		while (scan.hasNextLine())
		{
			for (int i = 0; i < currentState.board.length; i++)
			{
				currentState.board[i] = scan.nextLine().toCharArray();
			}		
		}
		scan.close();
		
		//Checks to see if the level has already been completed
		if (currentState.board[currentState.playerRow]
		[currentState.playerCol] == currentState.board
		[currentState.goalRow][currentState.goalCol])
		{
			currentState.levelPassed = true;
		}
        //throw IOException;
    }
    
    /** 
     * Constructor that adds the current state of the GameState object
     * to a list
     * 
     * @param direction - any of the four arrow directions
     * @return none 
     */
    void recordAndMove(Direction direction)
    {
		if (direction != null)
        {
			//Adds a copy of currentState to previousState list and allows
			//current state to move as long as it is not the same as it's
			//previous state 
			GameState copy = new GameState(currentState);
			previousStates.add(copy);
			
			currentState.move(direction);
			
			if(currentState.equals(copy) == true)
			{
				previousStates.remove(copy);
			}
		}
    } 

	/** 
     * Constructor that assigns currentState to it's previousState as 
     * long as it has moved at least once
     * 
     * @param none
     * @return none 
     */
    void undo()
    {
		if (previousStates.size() > 0) 
		{
			currentState = previousStates.get(previousStates.size() - 1);
			previousStates.remove(previousStates.get(previousStates.size() - 1));
		}
    }

    void play()
    {
		//Creates a game-like visual and scans user inputs
        System.out.print(currentState.toString());
        System.out.print("> ");
        Scanner scan = new Scanner(System.in);

        while (scan.hasNextLine())
		{
			String input = scan.nextLine();
			
			//Checks if user input is at least one char long
			if (input.length() != 1)
			{
				System.out.println("Command must be one char long.");
				System.out.print(currentState.toString());
				System.out.print("> ");
			}
			else
			{
				//Checks if user input is a valid input and reprompts
				//user input if game is not over, quitted, or saved
				if (input.contains("w"))
				{
					this.recordAndMove(Direction.UP);
					System.out.print(currentState.toString());
					
					if (currentState.board[currentState.playerRow]
					[currentState.playerCol] != currentState.board
					[currentState.goalRow][currentState.goalCol])
					{
						System.out.print("> ");
					}
				}
				else if (input.contains("a"))
				{
					this.recordAndMove(Direction.LEFT);
					System.out.print(currentState.toString());
					
					if (currentState.board[currentState.playerRow]
					[currentState.playerCol] != currentState.board
					[currentState.goalRow][currentState.goalCol])
					{
						System.out.print("> ");
					}
				}
				else if (input.contains("s"))
				{
					this.recordAndMove(Direction.DOWN);
					System.out.print(currentState.toString());
					
					if (currentState.board[currentState.playerRow]
					[currentState.playerCol] != currentState.board
					[currentState.goalRow][currentState.goalCol])
					{
						System.out.print("> ");
					}
				}
				else if (input.contains("d"))
				{
					this.recordAndMove(Direction.RIGHT);
					System.out.print(currentState.toString());
					
					if (currentState.board[currentState.playerRow]
					[currentState.playerCol] != currentState.board
					[currentState.goalRow][currentState.goalCol])
					{
						System.out.print("> ");
					}
				}
				else if (input.contains("u"))
				{
					this.undo();
					System.out.print(currentState.toString());
					System.out.print("> ");
				}
				else if (input.contains("o"))
				{
					this.saveToFile();
					System.out.print("> ");
				}
				else if (input.contains("q"))
				{
					return;
				}
				
				//Invalid inputs will be met with a print statement
				//corresponding to the issue
				else if (!(input.contains("w") || input.contains("a") ||
				input.contains("s") || input.contains("d") ||
				input.contains("u") || input.contains("o") ||
				input.contains("q")))
				{
					System.out.println("Possible commands:\n w - up\n a" 
					+ " - left\n s - down\n d - right\n u - undo\n o - "
					+ "save to file\n q - quit level");
					System.out.print(currentState.toString());
					System.out.print("> ");
				}
				
				//Checks if game is over
				if (currentState.board[currentState.playerRow]
				[currentState.playerCol] == currentState.board
				[currentState.goalRow][currentState.goalCol])
				{
					System.out.print(currentState.toString());
					System.out.println("Level passed!");
				}
			}
			
		}	
		scan.close();
    }

	/** 
     * Constructor that saves the currentState of the game to a file
     * 
     * @param none
     * @return none 
     */
    void saveToFile()
    {
		try
		{
			PrintWriter file = new PrintWriter(new File(OUTFILE_NAME));

			//PrintWriter writes the dimensions, player position and
			//goal position of currentState into a file
			file.println(currentState.board.length + " " + currentState.board[0].length);
			file.println(currentState.playerRow + " " 
			+ currentState.playerCol);
			file.print(currentState.goalRow + " " 
			+ currentState.goalCol);
			
			//PrintWriter copies the elements within the board
			for (int i = 0; i < currentState.board.length; i++)
			{
				file.println();
				for (int j = 0; j < currentState.board[0].length; j++)
				{
					file.print(currentState.board[i][j]);
				}
			}
			file.close();
			
			System.out.println("Saved current state to:"
			+ " saved_streamline_game");
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}	
    }
    
    //Testing Main Code
    public static void main(String[] args)
    {
		Streamline game = new Streamline();
		//Streamline game = new Streamline(OUTFILE_NAME);
		game.play();  
	}
}
