/**
 * File: Player.java
 * Author: Trung Ngo
 * Login: cs8bfa19dh
 * Date: November 25, 2019
 * Sources of Help:
 *
 * File that defines a specific RoundedSquare object. This is done by
 * giving this unique RoundedSquare a different color and stroke color
 * compared with other RoundedSquare objects.
 */
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

/**
 * This class defines a player character, playerRect, by giving it a
 * unique fill and stroke color. It contains 1 no agrs constructor, 1
 * isntance variable for stroke width and 1 method to change the size of
 * this player object.
 */
public class Player extends RoundedSquare {
	final static double STROKE_FRACTION = 0.1;

	/**
	 * Default constructor. Creates a default Player object with fill
	 * color pale turquoise and stroke color medium blue. Stroke is type
	 * centered.
	 */
	public Player() {
		setFill(Color.PALETURQUOISE);
		setStroke(Color.MEDIUMBLUE);
		StrokeType Centered;
	}

	/**
	 * Sets the size of the player square
	 * 
	 * @param size desired playere square size
	 */
	@Override
	public void setSize(double size) {
		setStrokeWidth(size*STROKE_FRACTION);

		//Does not include fraction since this will be taken care of in
		//parent class
		super.setSize(size);
	}
}
