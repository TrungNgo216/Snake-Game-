import java.util.*;

/**
 * File: GameState.java
 * Author: Trung Ngo
 * Login: cs8bfa19dh
 * Date: October 24, 2019
 * Sources of Help:
 *
 * File that uses class GameState to implement methods that manipulate
 * GameState objects. Allows game, StreamLine to be played (snake-game)
 */

/**
 * Contains 2 constructors, 8 methods and 6 instance variables. This 
 * class manipulates GameState objects and allows movement of a player
 * character to maneuver through obstacles and reach the goal position
 */
public class GameState
{
    /* Provided constants */
    final static char PLAYER_CHAR = '@';
    final static char GOAL_CHAR = 'G';
    final static char SPACE_CHAR = ' ';
    final static char TRAIL_CHAR = '+';
    final static char OBSTACLE_CHAR = 'O';
    final static char DOWN_ZAP_CHAR = 'v'; 
    final static char UP_ZAP_CHAR = '^'; 
    final static char LEFT_ZAP_CHAR = '<'; 
    final static char RIGHT_ZAP_CHAR = '>'; 
    final static char NEWLINE_CHAR = '\n';
    final static char HORIZONTAL_BORDER_CHAR = '-';
    final static char SIDE_BORDER_CHAR = '|';

    /* Add your `final static` constants here */


    /* Instance variables, do not add any */
    char[][] board; // 2-D array containing the tiles of the board, 
    //each tile has a value
    int playerRow; // the row of the playerin the board (0-indexing), 
    //access using board[playerRow][playerCol]
    int playerCol; // the column of the player in the board (0-indexing)
    int goalRow; // the row of the goal in the board (0-indexing)
    int goalCol; // the colum of the goal in the board (0-indexing)
    boolean levelPassed; // denotes whether the level is passed

	/** 
     * Constructor that initializes GameState to 6 parameters and final
     * static chars
     * 
     * @param int height - height of game board
     * @param int width - width of game board
     * @param int playerRow - row position of player character
     * @param int playerCol - column position of player character
     * @param int goalRow - row position of goal 
     * @param int goalCol - column position of goal
     * @return none 
     */
    public GameState(int height, int width, int playerRow, 
    int playerCol, int goalRow, int goalCol)
    {
		//Initializing board
        this.board = new char [height][width];
        
        //Goes through each element in board[][]
        for (int i = 0; i < height; i++)
        {
			for (int j = 0; j < width; j++)
			{
				this.board[i][j] = SPACE_CHAR;
			}
		}
		  
		//Initializing variables
        this.playerRow = playerRow;
        this.playerCol = playerCol;
        this.goalRow = goalRow;
        this.goalCol = goalCol;
        this.board[playerRow][playerCol] = PLAYER_CHAR;
        this.board[goalRow][goalCol] = GOAL_CHAR;  
        this.levelPassed = false; 
    }

	/** 
     * Copy constructor that initializes GameState object to another
     * GameState object
     * 
     * @param GameState other - copied GameState object
     * @return none 
     */
    public GameState(GameState other)
    {
		//Initializing this.board with other
		this.board = new char 
			[other.board.length][other.board[0].length];
		
		//Goes through each element in other.board
        for (int i = 0; i < other.board.length; i++)
        {
			for (int j = 0; j < other.board[0].length; j++)
			{
				this.board[i][j] = other.board[i][j];
			}
		}	
		
		//Initializing variables
		this.playerRow = other.playerRow;
        this.playerCol = other.playerCol;
        this.goalRow = other.goalRow;
        this.goalCol = other.goalCol; 
        
        //Checking for Test Level Passed
        if(this.playerRow == other.goalRow && this.playerCol == 
        other.goalCol)
        {
			levelPassed = true;
		}
    }

	/** 
     * Method to count the number of empty space characters
     * 
     * @param none
     * @return count - number of empty characters in board[][]
     */
    int countEmptyTiles()
    {
		int count = 0;
		//Goes through each element in board[][] and counting spaces
        for (int i = 0; i < board.length; i++)
        {
			for (int j = 0; j < board[i].length; j++)
			{
				if(board[i][j] == SPACE_CHAR)
				{
					count++;
				}
			}
		}
        return count; 
    }

	/** 
     * Method to add randomly placed obstacles on the game board
     * 
     * @param count - number of random obstacles
     * @return none
     */
    void addRandomObstacles(int count)
    {
		//checks to make sure count != 0 and there are enough empty
		//spaces to add
        if (count <= countEmptyTiles() && count > 0)
		{
			//completely fills board if count is = to # of empty tiles
			if(count == countEmptyTiles())
			{
				for (int i = 0; i < board.length; i++)
				{
					for (int j = 0; j < board[0].length; j++)
					{
						if(board[i][j] == SPACE_CHAR)
						{
							board[i][j] = OBSTACLE_CHAR;
						}
					}
				}
			}
			else 
			{
				for (int k = 0; k < count; k++)
				{
					//Add number count of obstacles in any random 
					//position in the board if it is a space char
					int randRow = new Random().nextInt(board.length);
					int randCol = new Random().nextInt(board[0].length);
						
					if(board[randRow][randCol] == SPACE_CHAR)
					{
						board[randRow][randCol] = OBSTACLE_CHAR;
					}
					//continues loop if random location is not a space
					else
					{
						count++;
					}
				}
			}	
		}
		else
		{
			return;
		}	
    }

	/** 
     * Method to add randomly placed zapper position and direction on 
     * the game board
     * 
     * @param count - number of random zappers
     * @return none
     */
    void addRandomZappers(int count)
    {
		int up = 0; int down = 1; int left = 2; int right = 3;
		//Random Number generator between 1 and 4
		int rng = 4;
		//checks to make sure count != 0 and there are enough empty
		//spaces to add
        if (count <= countEmptyTiles() && count > 0)
		{
			//completely fills board if count is = to # of empty tiles
			if(count == countEmptyTiles())
			{
				for (int i = 0; i < board.length; i++)
				{
					for (int j = 0; j < board[0].length; j++)
					{
						if(board[i][j] == SPACE_CHAR)
						{
							char zap = (char) new Random().nextInt(rng);
							
							if (zap == right)
							{
								board[i][j] = RIGHT_ZAP_CHAR;
							}
							
							else if (zap == down)
							{
								board[i][j] = DOWN_ZAP_CHAR;
							}
							
							else if (zap == left)
							{
								board[i][j] = LEFT_ZAP_CHAR;
							}
						
							else 
							{
								board[i][j] = UP_ZAP_CHAR;
							}
						}
					}
				}
			}
			else
			{
				for (int k = 0; k < count; k++)
				{
					char zap = (char) new Random().nextInt(rng);
					
					//Add number count of zappers in any random position
					//in the board as long as it is a space char
					int randRow = new Random().nextInt(board.length);
					int randCol = new Random().nextInt(board[0].length);
					
					//adds random direction zappers
					if(board[randRow][randCol] == SPACE_CHAR)
					{
						if (zap == right)
						{
							board[randRow][randCol] = RIGHT_ZAP_CHAR;
						}
						
						else if (zap == down)
						{
							board[randRow][randCol] = DOWN_ZAP_CHAR;
						}
						
						else if (zap == left)
						{
							board[randRow][randCol] = LEFT_ZAP_CHAR;
						}
						
						else if (zap == up)
						{
							board[randRow][randCol] = UP_ZAP_CHAR;
						}
					}
					//continues loop if random location is not a space
					else
					{
						count++;
					}
				}
			}	
		}  
		else
		{
			return;
		}
    }
	
	/* helper method
    int indexOfZapper(char zapChar)
    {
        // TODO
        return -1; 
    }
    * */ 
	
	/** 
     * Method to rotate board[][] counterclockwise
     * 
     * @param none
     * @return none
     */
    void rotateCounterClockwise()
    {
		//rotate [][] keeps track of original board data but rotated
        char [][] rotate = new char [board[0].length][board.length];
        
        for (int i = 0; i < board[0].length; i++)
        {
			//for all elements in the board, rotate it including zapper
			//direction
			for (int j = 0; j < board.length; j++)
			{
				if (board[j][board[0].length - 1 - i] == LEFT_ZAP_CHAR)
				{
					rotate[i][j] = DOWN_ZAP_CHAR;
				}
				else if (board[j][board[0].length - 1 - i] 
				== DOWN_ZAP_CHAR)
				{
					rotate[i][j] = RIGHT_ZAP_CHAR;
				}
				else if (board[j][board[0].length - 1 - i] 
				== UP_ZAP_CHAR)
				{
					rotate[i][j] = LEFT_ZAP_CHAR;
				}
				else if (board[j][board[0].length - 1 - i] 
				== RIGHT_ZAP_CHAR)
				{
					rotate[i][j] = UP_ZAP_CHAR;
				}
				else
				{
					rotate[i][j] = board[j][board[0].length - 1 - i];
				}
			}
		}
		
		//board is now reassigned to rotate to keep track of the new
		//positions and player is updated
		board = new char [rotate.length][rotate[0].length];
		for (int i = 0; i < rotate.length; i++)
        {
			for (int j = 0; j < rotate[0].length; j++)
			{
				board[i][j] = rotate[i][j];
				if (board[i][j] == PLAYER_CHAR)
				{
					this.playerRow = i;
					this.playerCol = j;
					this.goalRow = playerRow;
					this.goalCol = playerCol;
				}
			}
		}
		
		//goal character is updated
		for (int i = 0; i < board.length; i++)
        {
			for (int j = 0; j < board[0].length; j++)
			{
				if (board[i][j] == GOAL_CHAR)
				{
					this.goalRow = i;
					this.goalCol = j;
				}
			}
		}
    }
	
	/** 
     * Method to move player character left while it is a zapper or 
     * space character
     * 
     * @param none
     * @return none
     */
    void moveLeft()
    {	
		if (playerCol > 0)
		{
			//Moves player character left as long as it is a space
			int checkLeft = playerCol - 1;
			while(board[playerRow][checkLeft] == SPACE_CHAR)
			{
				board[playerRow][checkLeft] = PLAYER_CHAR;
				board[playerRow][checkLeft + 1] = TRAIL_CHAR;
				
				if (checkLeft != 0)
				{		
					checkLeft --;
					playerCol--;
				}
				else
				{
					playerCol = 0;
				}
			}
		}
				if (playerCol > 0)
				{
					//Moves player char left if left zap was touched
					if (board[playerRow][playerCol - 1] 
					== LEFT_ZAP_CHAR)
					{
						board[playerRow][playerCol] = TRAIL_CHAR;	
						playerCol --;
						board[playerRow][playerCol] = PLAYER_CHAR;		
						
						move(Direction.values()[0]);
					}
					
					//Moves player char right if right zap was touched
					else if (board[playerRow][playerCol - 1] 
					== RIGHT_ZAP_CHAR)
					{
						board[playerRow][playerCol] = TRAIL_CHAR;
						playerCol --;
						board[playerRow][playerCol] = PLAYER_CHAR;			
						
						move(Direction.values()[2]);
					}
					
					//Moves player char up if up zap was touched
					else if (board[playerRow][playerCol - 1] 
					== UP_ZAP_CHAR)
					{
						board[playerRow][playerCol] = TRAIL_CHAR;
						playerCol --;
						board[playerRow][playerCol] = PLAYER_CHAR;
						
						move(Direction.values()[1]);
					}
					
					//Moves player char down if down zap was touched
					else if (board[playerRow][playerCol - 1] 
					== DOWN_ZAP_CHAR)
					{
						board[playerRow][playerCol] = TRAIL_CHAR;
						playerCol --;
						board[playerRow][playerCol] = PLAYER_CHAR;
						
						move(Direction.values()[3]);
					}
					
					//Checks to see if player has reached goal, then
					//turns levelPassed into true
					else if (board[playerRow][playerCol - 1] 
					== GOAL_CHAR)
					{
						board[playerRow][playerCol] = TRAIL_CHAR;
						playerCol --;
						board[playerRow][playerCol] = PLAYER_CHAR;
						
						//System.out.println("Level Passed");
						levelPassed = true;
						return;
					}
				}
    }

	/** 
     * Method to combine rotate counterclockwise and move left to move
     * player character around the gameboard in any of the four arrow
     * directions
     * 
     * @param direction - arrow key direction
     * @return none
     */
    void move(Direction direction)
    {
		//rotate counterclockwise a certain number of times with move
		//left to code for each direction
        if (direction == Direction.LEFT)
        {
			this.moveLeft();
		}
		
		else if (direction == Direction.UP)
        {
			this.rotateCounterClockwise();
			this.moveLeft();
			this.rotateCounterClockwise();
			this.rotateCounterClockwise();
			this.rotateCounterClockwise();
		}
		
		else if (direction == Direction.DOWN)
        {
			this.rotateCounterClockwise();
			this.rotateCounterClockwise();
			this.rotateCounterClockwise();
			this.moveLeft();
			this.rotateCounterClockwise();
		}
		
		else 
        {
			this.rotateCounterClockwise();
			this.rotateCounterClockwise();
			this.moveLeft();
			this.rotateCounterClockwise();
			this.rotateCounterClockwise();
		}
    }

	/** 
     * Method to turn board[][] into a readable and visible form
     * 
     * @param none
     * @return board as a string
     */
    @Override
    public String toString()
    {
		//String top horizontal border that takes into account spaces
		//inbetweem each board element
		StringBuilder formatBoard = new StringBuilder();
        int horizontalBorder = (this.board[0].length + 1)*2 + 1;
        
        for (int i = 0; i < horizontalBorder; i++)
        {
			formatBoard.append(HORIZONTAL_BORDER_CHAR);
        }
        formatBoard.append(NEWLINE_CHAR);
        
        //String middle of the board with side border characters on each
        //side as well as every element inside
        for (int i = 0; i < this.board.length; i++)
        {
			formatBoard.append(SIDE_BORDER_CHAR);
			formatBoard.append(SPACE_CHAR);
			for (int j = 0; j < this.board[i].length; j++)
			{
				formatBoard.append(this.board[i][j]);
				formatBoard.append(SPACE_CHAR);
			}
			formatBoard.append(SIDE_BORDER_CHAR);
			formatBoard.append(NEWLINE_CHAR);
		}
		
		//String bottom horizontal border that takes into account spaces
		//inbetweem each board element
		for (int i = 0; i < horizontalBorder; i++)
        {
			formatBoard.append(HORIZONTAL_BORDER_CHAR);
        }
        formatBoard.append(NEWLINE_CHAR);
			
        return formatBoard.toString(); 
    }

	/** 
     * Method to compare two GameState objects if they have the same
     * board dimensions and contents inside
     * 
     * @param other - GameState object being compared to
     * @return temp - true or false depending on conditions
     */
    @Override
    public boolean equals(Object other)
    {
		//temporary variable
		boolean temp = false;
		
		//checks all instance variables of this versus other GameStates
		if (other instanceof GameState)
		{
			if (this.board.length == ((GameState)other).board.length &&
			this.board[0].length == ((GameState)other).board[0].length
			&& this.playerRow == ((GameState)other).playerRow &&
			this.playerCol == ((GameState)other).playerCol &&
			this.goalRow == ((GameState)other).goalRow &&
			this.goalCol == ((GameState)other).goalCol && 
			this.levelPassed == ((GameState)other).levelPassed)
			{
				//checks contents of this versus other GameStates
				for (int i = 0; i < ((GameState)other).board.length; 
				i++)
				{
					for (int j = 0; j < 
					((GameState)other).board[i].length; j++)
					{
						if (this.board[i][j] != 
						((GameState)other).board[i][j])
						{
							temp = false;
							return temp;
						}
						else
						{
							temp = true;
						}
					}
				}
			}
			
		}
		
		//if input is null as well as other edge cases, return false
        else if (other == null || !(other instanceof GameState) )
        {
			temp = false;
		}	
		return temp;
    }
	/*
    //Main Test Code
    public static void main(String args[])
    {
		
		GameState game = new GameState(8, 8, 5, 4, 0, 4);
		game.addRandomZappers(30);
		System.out.println(game.toString());
		game.addRandomObstacles(10);
		System.out.println(game.toString());
		game.move(Direction.LEFT);
		System.out.println(game.toString());
		game.move(Direction.RIGHT);
		System.out.println(game.toString());
		game.move(Direction.UP);
		up and up goes right
		System.out.println(game.toString());
		game.move(Direction.DOWN);
		System.out.println(game.toString()); 
		System.out.println(game.toString());
		game.move(Direction.UP);
		game.moveLeft();
		System.out.println(game.toString());
	}*/
    
   
}
