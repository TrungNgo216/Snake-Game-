/**
 * File: GuiStreamline.java
 * Author: Trung Ngo
 * Login: cs8bfa19dh
 * Date: November 25, 2019
 * Sources of Help:
 *
 * File that provides the backend snake game, Streamline, with a 
 * frontend. It allows the user to play Streamline with a visually
 * appealing platform using keyboard commands.
 */
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.*;
import javafx.util.Duration;

/**
 * This class defines the properties of GuiStreamline by initalizing 
 * many different types and giving 12 instance variables controlling 
 * color, default board size and name of the application. It also 
 * contains 8 methods, 3 getters and 1 helper, and a nested class.
 */
public class GuiStreamline extends Application {
    static final double MAX_SCENE_WIDTH = 600;
    static final double MAX_SCENE_HEIGHT = 600;
    static final double PREFERRED_SQUARE_SIZE = 100;

    static final String TITLE = "CSE 8b Streamline GUI";
    static final String USAGE = 
        "Usage: \n" + 
        "> java GuiStreamline               - to start a game with default" +
            " size 6*5 and random obstacles\n" + 
        "> java GuiStreamline <filename>    - to start a game by reading g" +
            "ame state from the specified file\n" +
        "> java GuiStreamline <directory>   - to start a game by reading a" +
            "ll game states from files in\n" +
        "                                     the specified directory and " +
            "playing them in order\n";

    static final Color TRAIL_COLOR = Color.PALETURQUOISE;
    static final Color GOAL_COLOR = Color.VIOLET;
    static final Color OBSTACLE_COLOR = Color.SLATEGREY;
    static final Color ZAPPER_COLOR = Color.DEEPPINK; 

    // Trail radius will be set to this fraction of the size of a board square.
    static final double TRAIL_RADIUS_FRACTION = 0.1;

    // Squares will be resized to this fraction of the size of a board square.
    static final double SQUARE_FRACTION = 0.8;
    static final double TRIANGLE_FRACTION = 0.8;
    
    Stage mainStage;
    Scene mainScene;
    Group levelGroup;                   // For obstacles and trails
    Group rootGroup;                    // Parent group for everything else
    Player playerRect;                  // GUI representation of the player
    RoundedSquare goalRect;             // GUI representation of the goal

    Shape[][] grid;                     // Same dimensions as the game board
    Shape[][] trailsGrid;               // Same dimensions as the game board
    
    Streamline game;                    // The current level
    ArrayList<Streamline> nextGames;    // Future levels

    MyKeyHandler myKeyHandler;          // for keyboard input

    /**
     * Getter for the current Streamline board width
     * 
     * @return  width of current board
     */
    public int getBoardWidth() {
		return game.currentState.board[0].length;
    }

    /**
     * Getter for the current Streamline board height
     * 
     * @return  height of current board
     */
    public int getBoardHeight() {
		return game.currentState.board.length;
    }
    
    /**
     * Getter for the min square size by comparing the max square length
     * 
     * @return  length of square
     */
    public double getSquareSize() {
		double maxSquareWidth = 
			mainScene.getWidth()/this.getBoardWidth();
		double maxSquareHeight = 
			mainScene.getHeight()/this.getBoardHeight();
			
        return maxSquareWidth >= maxSquareHeight 
			? maxSquareHeight:maxSquareWidth;
    }
    
    /**
     * Resets board then adds in trail circles, zappers and obstacles
     * according to their current board position
     */
    public void resetGrid() {
		levelGroup.getChildren().clear();
		
		//resizing grid and trailsGrid to current board
        grid = new Shape[getBoardHeight()][getBoardWidth()];
        trailsGrid = new Shape[getBoardHeight()][getBoardWidth()];
        
        for (int y = 0; y < getBoardHeight(); y++)
        {
			for (int x = 0; x < getBoardWidth(); x++)
			{
				//Adds trail circles according to their current board
				//position and fills them with the desired color
				double[] position = boardIdxToScenePos(x,y);
				
				Circle trailMarker = 
					new Circle(position[0], position[1], 
					getSquareSize()* TRAIL_RADIUS_FRACTION, 
					Color.TRANSPARENT);
					
				trailsGrid[y][x] = trailMarker;
				levelGroup.getChildren().add(trailMarker);
				
				//Adds obstacles according to their current board
				//position and fills them with the desired color
				if (game.currentState.board[y][x] == 
				GameState.OBSTACLE_CHAR)
				{
					RoundedSquare obstacle = 
						new RoundedSquare(position[0], position[1],
						getSquareSize()*SQUARE_FRACTION);
					
					obstacle.setFill(OBSTACLE_COLOR);
					grid[y][x] = obstacle;
					levelGroup.getChildren().add(obstacle);
				}
				
				//Adds an up zapper according to its current board
				//position and fills it with the desired color
				else if (game.currentState.board[y][x] == 
				GameState.UP_ZAP_CHAR)
				{
					ZapperTriangle zapper = 
						new ZapperTriangle(Direction.UP, position[0], 
						position[1],getSquareSize()*TRIANGLE_FRACTION);
					
					zapper.setFill(ZAPPER_COLOR);
					grid[y][x] = zapper;
					levelGroup.getChildren().add(zapper);
				}
				
				//Adds a down zapper according to its current board
				//position and fills it with the desired color
				else if (game.currentState.board[y][x] == 
				GameState.DOWN_ZAP_CHAR)
				{
					ZapperTriangle zapper = 
						new ZapperTriangle(Direction.DOWN, position[0], 
						position[1],getSquareSize()*TRIANGLE_FRACTION);
					
					zapper.setFill(ZAPPER_COLOR);
					grid[y][x] = zapper;
					levelGroup.getChildren().add(zapper);
				}
				
				//Adds a right zapper according to its current board
				//position and fills it with the desired color
				else if (game.currentState.board[y][x] == 
				GameState.RIGHT_ZAP_CHAR)
				{
					ZapperTriangle zapper = 
						new ZapperTriangle(Direction.RIGHT, position[0], 
						position[1], getSquareSize()*TRIANGLE_FRACTION);
					
					zapper.setFill(ZAPPER_COLOR);
					grid[y][x] = zapper;
					levelGroup.getChildren().add(zapper);
				}
				
				//Adds a left zapper according to its current board
				//position and fills it with the desired color
				else if (game.currentState.board[y][x] == 
				GameState.LEFT_ZAP_CHAR)
				{
					ZapperTriangle zapper = 
						new ZapperTriangle(Direction.LEFT, position[0], 
						position[1],getSquareSize()*TRIANGLE_FRACTION);
					
					zapper.setFill(ZAPPER_COLOR);
					grid[y][x] = zapper;
					levelGroup.getChildren().add(zapper);
				}		
			}
		}
    }


   /**
    * Sets board of invisible trail circles, indicating trail char, to 
    * visibile after a player has moved at least once
    */
   public void updateTrailColors() {
    for (int x = 0; x < getBoardWidth(); x++)
        {
			for (int y = 0; y < getBoardHeight(); y++)
			{
				//If player on top of zapper, then zapper should no
				//longer be visible
				if (game.currentState.board[y][x] == 
				GameState.PLAYER_CHAR && grid[y][x] 
				instanceof ZapperTriangle)
				{
					grid[y][x].setFill(Color.TRANSPARENT);
				}
				
				//If player travels across a zapper, then zapper
				//should no longer be visible
				else if (game.currentState.board[y][x] == 
				GameState.TRAIL_CHAR && grid[y][x] 
				instanceof ZapperTriangle)
				{
					trailsGrid[y][x].setFill(TRAIL_COLOR);
					grid[y][x].setFill(Color.TRANSPARENT);
				}
				
				//If player did not travel across a zapper, then zapper
				//should be visible
				else if (game.currentState.board[y][x] != 
				GameState.TRAIL_CHAR && grid[y][x] 
				instanceof ZapperTriangle)
				{
					trailsGrid[y][x].setFill(Color.TRANSPARENT);
					grid[y][x].setFill(ZAPPER_COLOR);
				}
				
				//If player has traveled, then trail circles should be 
				//visible
				else if (game.currentState.board[y][x] == 
				GameState.TRAIL_CHAR)
				{
					trailsGrid[y][x].setFill(TRAIL_COLOR);
				}		
				
				//Otherwise, trail circles are invisible
				else
				{
					trailsGrid[y][x].setFill(Color.TRANSPARENT);
				}
			}
		} 
    }
    
    /** 
     * Coverts the given board column and row into scene coordinates.
     * Gives the center of the corresponding tile.
     * 
     * @param boardCol a board column to be converted to a scene x
     * @param boardRow a board row to be converted to a scene y
     * @return scene coordinates as length 2 array where index 0 is x
     */
    static final double MIDDLE_OFFSET = 0.5;
    public double[] boardIdxToScenePos (int boardCol, int boardRow) {
        double sceneX = ((boardCol + MIDDLE_OFFSET) * 
            (mainScene.getWidth() - 1)) / getBoardWidth();
        double sceneY = ((boardRow + MIDDLE_OFFSET) * 
            (mainScene.getHeight() - 1)) / getBoardHeight();
        return new double[]{sceneX, sceneY};
    }
    
    /**
     * Updates trail circles by calling updateTrailColors() and player 
     * rectangle when player has moved. In addition, it checks if the 
     * player has reached the goal and therefore calls onLevelFinished()
     * 
     * @param fromCol old player column position
     * @param fromRow old player row position
     * @param toCol new player column position
     * @param toRow new player row position
     */
    public void onPlayerMoved(int fromCol, int fromRow, int toCol, int toRow)
    {  
		updateTrailColors();
		
		//Sets playerRect new position to passed in column and row value
		double[] playerPos = boardIdxToScenePos(toCol, toRow);
		playerRect.setCenterX(playerPos[0]);
		playerRect.setCenterY(playerPos[1]);
		
		
		if (toCol == game.currentState.goalCol && 
		toRow == game.currentState.goalRow)
		{
			onLevelFinished();
		}
    }
    
    /**
     * Updates the Gui representation of Streamline, by calling 
     * Streamline's movement methods with onPlayerMoved(), depending on 
     * keyCode passed in
     * 
     * @param keyCode keyCode of button on keyboard pressed
     */
    void handleKeyCode(KeyCode keyCode)
    {
        int tempRow = game.currentState.playerRow;
        int tempCol = game.currentState.playerCol;

		//Move up if w or up arrow is pressed
        switch (keyCode) {
            case UP: case W:
				game.recordAndMove(Direction.UP);
				break;
			
			//Move down if s or down arrow is pressed
			case DOWN: case S:
				game.recordAndMove(Direction.DOWN);
				break;
			
			//Move right if d or right arrow is pressed
			case RIGHT: case D:
				game.recordAndMove(Direction.RIGHT);
				break;
		
			//Move left if a or left arrow is pressed
			case LEFT: case A:
				game.recordAndMove(Direction.LEFT);
				break;
			
			//undo if u is pressed
			case U:
				game.undo();
				break;
			
			//save game if o is pressed
			case O:
				game.saveToFile();
				break;
				
			//exit game if q is pressed
			case Q:
				System.exit(0);
				break;
			
			//Print statement if any other key is pressed
            default:
                System.out.println("Possible commands:\n w - up\n " + 
                    "a - left\n s - down\n d - right\n u - undo\n " + 
                    "o - save\n q - quit level");
                break;
        }
		
		//Updates playerRect accordingly
        onPlayerMoved(tempCol, tempRow, game.currentState.playerCol, 
			game.currentState.playerRow);
    }

    /**
     * This nested class handles keyboard input and calls handleKeyCode()
     */
    class MyKeyHandler implements EventHandler<KeyEvent>
    {
        public void handle(KeyEvent e)
        {
			handleKeyCode(e.getCode());				
        }
    }


   /**
    * When a new level is loaded, grid must be reset, calling 
    * resetGrid(), and playerRect and goalRect must be updated according
    * to their new current board position. In addition, 
    * updateTrailColors() is called in the case of opening a saved file
    * where player movement has more than likely occurred
    */
   public void onLevelLoaded()
   {
        resetGrid();

        double squareSize = getSquareSize() * SQUARE_FRACTION;

        //Updates the player position
        double[] playerPos = boardIdxToScenePos(
			game.currentState.playerCol, game.currentState.playerRow);
       
        playerRect.setSize(squareSize);
        playerRect.setCenterX(playerPos[0]);
        playerRect.setCenterY(playerPos[1]);

        //Updates the goal position
        double[] goalPos = boardIdxToScenePos(
			game.currentState.goalCol, game.currentState.goalRow);
       
		goalRect.setSize(squareSize);
		goalRect.setCenterX(goalPos[0]);
		goalRect.setCenterY(goalPos[1]);
       
		//Updates trail circle characters if a level was loaded in from 
		//a saved state
		updateTrailColors();
   }

    /** 
     * Called when the player reaches the goal. Shows the winning animation
     * and loads the next level if there is one.
     */
    static final double SCALE_TIME = 175;  // milliseconds for scale animation
    static final double FADE_TIME = 250;   // milliseconds for fade animation
    static final double DOUBLE_MULTIPLIER = 2;
    public void onLevelFinished() {
   // Clone the goal rectangle and scale it up until it covers the screen

        // Clone the goal rectangle
        Rectangle animatedGoal = new Rectangle(
            goalRect.getX(),
            goalRect.getY(),
            goalRect.getWidth(),
            goalRect.getHeight()
        );
        animatedGoal.setFill(goalRect.getFill());

        // Scope for children
        {
            // Add the clone to the scene
            List<Node> children = rootGroup.getChildren();
            children.add(children.indexOf(goalRect), animatedGoal);
        }

        // Create the scale animation
        ScaleTransition st = new ScaleTransition(
            Duration.millis(SCALE_TIME), animatedGoal
        );
        st.setInterpolator(Interpolator.EASE_IN);
        
        // Scale enough to eventually cover the entire scene
        st.setByX(DOUBLE_MULTIPLIER * 
            mainScene.getWidth() / animatedGoal.getWidth());
        st.setByY(DOUBLE_MULTIPLIER * 
            mainScene.getHeight() / animatedGoal.getHeight());

        /*
         * This will be called after the scale animation finishes.
         * If there is no next level, quit. Otherwise switch to it and
         * fade out the animated cloned goal to reveal the new level.
         */
        st.setOnFinished(e1 -> {
			//Checks if there is a next game to load
			if (nextGames.size() >= 1) 
			{
				game = nextGames.get(0);
				nextGames.remove(0);
				rootGroup.getChildren().remove(animatedGoal);
			}
			
			//Otherwise quit game
			else
			{
				handleKeyCode(KeyCode.Q);
			}

            // DO NOT MODIFY ANYTHING BELOW THIS LINE IN THIS METHOD

            // Update UI to the next level, but it won't be visible yet
            // because it's covered by the animated cloned goal
            onLevelLoaded();

            Rectangle fadeRect = new Rectangle(0, 0, 
                mainScene.getWidth(), mainScene.getHeight());
            fadeRect.setFill(goalRect.getFill());
            
            // Scope for children
            {
                // Add the fading rectangle to the scene
                List<Node> children = rootGroup.getChildren();
                children.add(children.indexOf(goalRect), fadeRect);
            }

            FadeTransition ft = new FadeTransition(
                Duration.millis(FADE_TIME), fadeRect
            );
            ft.setFromValue(1);
            ft.setToValue(0);
            
            // Remove the cloned goal after it's finished fading out
            ft.setOnFinished(e2 -> {
                rootGroup.getChildren().remove(fadeRect);
            });
            
            // Start the fade-out now
            ft.play();
        });
        
        // Start the scale animation
        st.play();
    }


    /** 
     * Performs file IO to populate game and nextGames using filenames from
     * command line arguments.
     */
    public void loadLevels() {
        game = null;
        nextGames = new ArrayList<Streamline>();
        
        List<String> args = getParameters().getRaw();
        if (args.size() == 0) {
            System.out.println("Starting a default-sized random game...");
            game = new Streamline();
            return;
        }

        // at this point args.length == 1
        
        File file = new File(args.get(0));
        if (!file.exists()) {
            System.out.printf("File %s does not exist. Exiting...", 
                args.get(0));
            return;
        }

        // if is not a directory, read from the file and start the game
        if (!file.isDirectory()) {
            System.out.printf("Loading single game from file %s...\n", 
                args.get(0));
            game = new Streamline(args.get(0));
            return;
        }

        // file is a directory, walk the directory and load from all files
        File[] subfiles = file.listFiles();
        Arrays.sort(subfiles);
        for (int i=0; i<subfiles.length; i++) {
            File subfile = subfiles[i];
            
            // in case there's a directory in there, skip
            if (subfile.isDirectory()) continue;

            // assume all files are properly formatted games, 
            // create a new game for each file, and add it to nextGames
            System.out.printf("Loading game %d/%d from file %s...\n",
                i+1, subfiles.length, subfile.toString());
            nextGames.add(new Streamline(subfile.toString()));
        }

        // Switch to the first level
        game = nextGames.get(0);
        nextGames.remove(0);
    }


    /** 
     * The main entry point for all JavaFX Applications
     * Initializes instance variables, creates the scene, and sets up the UI
     * 
     * @param  primaryStage The window for this application
     * @throws Exception    [description]
     */
    public void start(Stage primaryStage) throws Exception {
        // Populate game and nextGames
        loadLevels();
		
        // Initialize the scene and our groups
        rootGroup = new Group();
        mainScene = new Scene(rootGroup, MAX_SCENE_WIDTH, MAX_SCENE_HEIGHT, 
            Color.LIGHTYELLOW);
        levelGroup = new Group();
        rootGroup.getChildren().add(levelGroup);
        
        //Adding playerRect and goalRect to the game with desired goal
        //color and player default color
        goalRect = new RoundedSquare();
        goalRect.setFill(GOAL_COLOR);
		playerRect = new Player();
		
        rootGroup.getChildren().add(goalRect);
        rootGroup.getChildren().add(playerRect);
        
        //Resets grids and recreates to prepare for next game
        onLevelLoaded();
         
        //Sets up keyboard input 
        myKeyHandler = new MyKeyHandler();
        mainScene.setOnKeyPressed(myKeyHandler);
       
        // Make the scene visible
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(true);      
        primaryStage.show();
     
		//Allows game to be played with a resizeable window
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) 
        -> {onLevelLoaded();});

		primaryStage.heightProperty().addListener((obs, oldVal, newVal) 
		-> {onLevelLoaded();});
    }

    /** 
     * Execution begins here, but at this point we don't have a UI yet
     * The only thing to do is call launch() which will eventually result in
     * start() above being called.
     */
    public static void main(String[] args) {
        if (args.length != 0 && args.length != 1) {
            System.out.print(USAGE);
            return;
        }

        launch(args);
        /* Testing other board sizes and multiple levels
        launch("saved_streamline_game");
        launch("sample_levels");*/
    }
}
