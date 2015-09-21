import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Represents a generic missile object.
 * @author Nicholas Grant
 *
 */
public class Missile {
	/**
	 * The starting location of the missile (at the tank's barrel)
	 */
	private Point startLoc;
	/**
	 * The current location of the missile
	 */
	private Point currentLoc;
	/**
	 * The color of the missile
	 */
	private Color color;
	/**
	 * Determines the speed of the missile's movement
	 */
	private int move;
	/**
	 * The difference between target location and missile location's x-component
	 */
	private int dx;
	/**
	 * The difference between target location and missile location's y-component
	 */
	private int dy;
	/**
	 * The change in the missile location's x component
	 */
	private double newX;
	/**
	 * The change in the missile location's y component
	 */
	private double newY;
	/**
	 * Missile location x-component
	 */
	private int x;
	/**
	 * Missile location y-component
	 */
	private int y;
	/**
	 * Width of the missile's drawn representation
	 */
	private int width;
	/**
	 * Height of the missile's drawn representation
	 */
	private int height;
	/**
	 * Whether the missile is still moving or not
	 */
	private boolean shot;
	/**
	 * The missile's target
	 */
	private Point target;
	/**
	 * Missile constructor - initializes the necessary variables 
	 * for the missile to work
	 * @param x		initial missile location's x-component
	 * @param y		initial missile location's y-component
	 * @param c		missile color
	 */
	public Missile( int x, int y, Color c ) {
		this.x = x;
		this.y = y;
		color = c;
		width = height = 2;
		move = 5;
		startLoc = new Point( x, y );
		currentLoc = new Point( x, y );
		shot = false;
	}
	/**
	 * Returns the missile's starting location
	 * @return		missile starting location
	 */
	public Point getStartLoc() {
		return startLoc;
	}
	/**
	 * Returns the missile's current location
	 * @return		missile current location
	 */
	public Point getCurrentLoc() {
		return currentLoc;
	}
	/**
	 * Returns the missile's target location
	 * @return		missile target location
	 */
	public Point getTarget() {
		return target;
	}
	/**
	 * Returns whether or not the missile is still moving
	 * @return		whether missile is still moving or not
	 */
	public boolean isShot() {
		return shot;
	}
	/**
	 * Sets whether or not the missile is moving
	 * @param b
	 */
	public void setShot( boolean b ) {
		shot = b;
	}
	/**
	 * Sets the speed of the missile
	 * @param m
	 */
	public void setMove( int m ) {
		move = m;
	}
	/**
	 * Sets the missile's start location
	 * @param p		missile start location
	 */
	public void setStartLoc( Point p ) {
		startLoc = p;
	}
	/**
	 * Sets the missile's current location
	 * @param p		missile current location
	 */
	public void setCurrentLoc( Point p ) {
		currentLoc = p;
	}
	/**
	 * Sets the missile's target location
	 * @param p		missile target location
	 */
	public void setTarget( Point p ) {
		target = p;
	}
	/**
	 * Determines the change in x and y to get the missile
	 * to the target location
	 * @param x		target location's x-component
	 * @param y		target location's y-component
	 */
	public void findMoveRate( int x, int y ) {
		dx = ( x - (int)startLoc.getX() );
		dy = ( y - (int)startLoc.getY() );
		double magnitude = Math.sqrt( Math.pow( dx, 2 ) + Math.pow( dy, 2 ) );
		newX = (( dx * move ) / magnitude);
		newY = (( dy * move ) / magnitude);
	}
	/**
	 * Moves the missile based on the change in x and y values found
	 * in findMoveRate()
	 */
	public void moveMissile() {
		currentLoc.translate( (int)newX, (int)newY );
	}
	/**
	 * Draws the missile, then moves the missile.
	 * @param g		Graphics object necessary to draw the missile
	 */
	public void drawMissile( Graphics g ) {
		g.setColor( color );
		g.fillRect( (int)currentLoc.getX(), (int)currentLoc.getY(), width, height );
		moveMissile();
	}
	/**
	 * Once the missile has hit a tank or an obstacle, remove
	 * the missile from the screen.
	 */
	public void reset() {
		shot = false;
	}
}
