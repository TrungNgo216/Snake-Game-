/**
 * An enumerator defining the 4 possible move directions in a 
 * Streamline game
 * @author Charles Tianchen Yu
 * 
 * DO NOT MODIFY THIS FILE
 */
public enum Direction
{
    LEFT    (0),
    UP      (1), 
    RIGHT   (2), 
    DOWN    (3); 

    private int rotationCount;

    /**
     * Constructor for the enum
     * 
     * @param rotationCount see getRotationCount()
     */
    Direction(int rotationCount)
    {
        this.rotationCount = rotationCount;
    }

    /**
     * Returns rotationCount, the number of counterclockwise rotations 
     * needed to rotate the game state before moving up (e.g. to 
     * move right, rotate once then move up). 
     * 
     * @return counterclockwise rotation count
     */
    public int getRotationCount()
    {
        return this.rotationCount;
    }
}