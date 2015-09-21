import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Represents a generic tank object
 * @author Nicholas Grant
 *
 */
public class Tank {
	/**
	 * Size of the obstacles
	 */
	private final int OBSTACLE_SIZE = 65;
	/**
	 * Stores the 2D array representation of the game map
	 */
	private int[][] map;
	/**
	 * Stores the obstacles in Rectangle form
	 */
	private ArrayList<Rectangle> obstacles;
	/**
	 * Location of the tank's center
	 */
	private Point location;
	/**
	 * Color of the tank
	 */
	private Color color;
	/**
	 * Represents the direction of the tank
	 */
	private int direction;
	/**
	 * Width of the tank's bounding rectangle
	 */
	private int width;
	/**
	 * Height of the tank's bounding rectangle
	 */
	private int height;
	/**
	 * The tank's bounding rectangle
	 */
	private Rectangle tank;
	/**
	 * Determines the change in the tank's x-component when moving
	 */
	private int dx;
	/**
	 * Determines the change in the tank's y-component when moving
	 */
	private int dy;
	/**
	 * The tank's missile
	 */
	private Missile missile;
	/**
	 * The tank's barrel end point location
	 */
	private Point barrel;
	/**
	 * The tank's missile target location
	 */
	private Point target;
	/**
	 * Number of lives of the tank
	 */
	private int lives;
	/**
	 * Initializes the tank with its necessary elements
	 * @param x		x-component of tank center
	 * @param y		y-component of tank center
	 * @param d		direction of tank
	 * @param c		color of tank;
	 */
	public Tank( int x, int y, int d, Color c ) {
		location = new Point( x, y );
		color = c;
		direction = d;
		width = height = 13;
		barrel = new Point( x, y );
		dx = dy;
		lives = 5;
		missile = new Missile( x, y, Color.WHITE );
		tank = new Rectangle( x - (width/2), y - (height/2), width, height);
		makeObstacles();
	}
	/**
	 * Returns the location of the center of the tank
	 * @return		center of tank location
	 */
	public Point getLocation() {
		return location;
	}
	/**
	 * Returns the tank's missile
	 * @return		tank's missile
	 */
	public Missile getMissile() {
		return missile;
	}
	/**
	 * Returns the location of the tank's barrel's end point
	 * @return		end point location of tank's barrel
	 */
	public Point getBarrel() {
		return barrel;
	}
	/**
	 * Returns the width of the tank's bounding rectangle
	 * @return		width of tank's bounding rectangle
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * Returns the height of the tank's bounding rectangle
	 * @return		height of tank's bounding rectangle
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * Sets the direction of the tank
	 * @param d		desired tank direction
	 */
	public void setDirection( int d ) {
		direction = d;
	}
	/**
	 * Sets the location of the tank's barrel's end point
	 * @param x		x-component of tank's barrel's end point
	 * @param y		y-component of tank's barrel's end point
	 */
	public void setBarrel( int x, int y ) {
		double magnitude = (int)Math.sqrt( ( Math.pow( x - (int)location.getX() , 2 )
				 + Math.pow( y - (int)location.getY(), 2 ) ) );
		double R = width / magnitude;
		double newX = ( ( 1 - R ) * (int)location.getX() ) + ( R * x );
		double newY = ( ( 1 - R ) * (int)location.getY() ) + ( R * y );
		barrel.setLocation( newX, newY );
	}
	/**
	 * Set the location of the tank's missile's target
	 * @param p		location of the tank's missile's target
	 */
	public void setTarget( Point p ) {
		target = p;
	}
	/**
	 * Set the tank's missile
	 * @param m		tank's missile
	 */
	public void setMissile( Missile m ) {
		missile = m;
	}
	/**
	 * Returns the tank's lives
	 * @return		tank's lives
	 */
	public int getLives() {
		return lives;
	}
	/**
	 * Reduces the tank's lives by 1 when shot
	 */
	public void reduceLives() {
		lives--;
	}
	/**
	 * Draws the tank's number of lives to the frame
	 * @param g		Graphics object necessary to draw the number of lives
	 */
	public void drawLives( Graphics g ) {
		g.setColor( Color.RED );
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 18));
		g.drawString("Lives: "+lives, 390, 25);
	}
	/**
	 * Draws the tank to the frame
	 * @param g			Graphics object necessary to draw the tank
	 * @param mouseLocX	current location of the mouse's x-component
	 * @param mouseLocY	current location of the mouse's y-component
	 */
	public void drawTank( Graphics g, int mouseLocX, int mouseLocY ) {
			g.setColor(color);
			g.fillRect( (int)tank.getX(), (int)tank.getY(), width, height);
			drawBarrel( g, mouseLocX, mouseLocY );
			moveTank();
			if ( missile.isShot() ) {
				for ( Rectangle o : obstacles ) {
					if ( o.contains( missile.getCurrentLoc() ) ) {
						missile.reset();
						return;
					}
				}
				missile.drawMissile( g );
			}
	}
	/**
	 * Checks to see if the missile hit its target or an obstacle
	 * @param p		the missile location
	 * @return		true if hit, false if not hit
	 */
	public boolean testHit( Point p ) {
		if ( tank.contains( p ) ) {
			return true;
		}
		if ( p == target ) {
			return true;
		}
		return false;
	}
	/**
	 * Draws the tank's barrel to the frame based on the mouse cursor
	 * @param g		Graphics object necessary to draw the barrel
	 * @param x		x-component of the mouse location
	 * @param y		y-component of the mouse location
	 */
	public void drawBarrel( Graphics g, int x, int y ) {
		int dx = ( x - (int)location.getX() );
		int dy = ( y - (int)location.getY() );
		double magnitude = Math.sqrt( Math.pow( dx, 2 ) + Math.pow( dy, 2 ) );
		int newX = (int)(( dx * width ) / magnitude);
		int newY = (int)(( dy * height ) / magnitude);
		barrel.setLocation( (int)newX + (int)location.getX(), (int)newY + (int)location.getY() );
		g.setColor(color);
		g.drawLine( (int)newX + (int)location.getX(), (int)newY + (int)location.getY(), (int)location.getX(), (int)location.getY());
	}
	/**
	 * Loads the map text file into the 2D array
	 */
	public void loadMap() {
		map = new int[13][13];
		try {
			Scanner scan = new Scanner(new File("map1.txt"));
			scan.useDelimiter(" ");
			scan.nextLine();
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map.length; j++) {
					map[i][j] = scan.nextInt();
				}
				scan.nextLine();
			}
		} catch (FileNotFoundException fnf) {
			System.out.println("File not found.");
		}
	}
	/**
	 * Makes the obstacles based on the loaded map text file
	 */
	public void makeObstacles() {
		loadMap();
		obstacles = new ArrayList<Rectangle>();
		for ( int i = 0; i < map.length; i++ ) {
			for( int j = 0; j < map.length; j++ ) {
				if ( map[i][j] == 1 ) {
					obstacles.add( new Rectangle( j*OBSTACLE_SIZE, i*OBSTACLE_SIZE, OBSTACLE_SIZE, OBSTACLE_SIZE ) );
				}
			}
		}  
	}
	/**
	 * Checks to see if the tanks collide with an obstacle
	 * @param dx		tank movement in x direction
	 * @param dy		tank movement in y direction
	 * @return			true if collide, false if not
	 */
	public boolean checkCollision( int dx, int dy ) {
		location.translate( dx, dy );
		tank.setLocation( (int)location.getX() - (width/2), (int)location.getY() - (height/2) );
		for ( Rectangle o : obstacles ) {
			if ( tank.intersects( o ) ) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Moves the tank based on its direction
	 */
	public void moveTank( ) {
		boolean collide = false;
		if ( direction == -2 ) {
			if ( checkCollision( -2, 0 ) ) {
				dx = 2;
				dy = 0;
				collide = true;
			}
			else {
				dx = -2;
				dy = 0;
			}
		}
		
		if ( direction == -1 ) {
			if ( checkCollision( 0, -2 ) ) {
				dx = 0;
				dy = 2;
				collide = true;
			}
			else {
				dx = 0;
				dy = -2;
			}
		}
		
		if ( direction == 1 ) {
			if ( checkCollision( 0, 2 ) ) {
				dx = 0;
				dy = -2;
				collide = true;
			}
			else {
				dx = 0;
				dy = 2;
			}
		}
		
		if ( direction == 2 ) {
			if ( checkCollision( 2, 0 ) ) {
				dx = -2;
				dy = 0;
				collide = true;
			}
			else {
				dx = 2;
				dy = 0;
			}
		}
		
		if ( direction == 4 ) {
			if ( checkCollision( 2, 2 ) ) {
				dx = -2;
				dy = -2;
				collide = true;
			}
			else {
				dx = 2;
				dy = 2;
			}
		}
		
		if ( direction == 3 ) {
			if ( checkCollision( 2, -2 ) ) {
				dx = -2;
				dy = 2;
				collide = true;
			}
			else {
				dx = 2;
				dy = -2;
			}
		}
		
		if ( direction == -3 ) {
			if ( checkCollision( -2, -2 ) ) {
				dx = 2;
				dy = 2;
				collide = true;
			}
			else {
				dx = -2;
				dy = -2;
			}
		}
		
		if ( direction == -4 ) {
			if ( checkCollision( -2, 2 ) ) {
				dx = 2;
				dy = -2;
				collide = true;
			}
			else {
				dx = -2;
				dy = 2;
			}
		}
		
		if ( collide ) {
			location.translate( dx, dy );
			tank.setLocation( (int)location.getX() - (width/2), (int)location.getY() - (height/2) );
		}
	}
	/**
	 * Moves the tank randomly
	 */
	public void moveRandom( ) {
		Random rand = new Random();
		direction = rand.nextInt( 9 ) - 4;
		moveTank();
	}
	/**
	 * Shoots a missile towards the target tank
	 * @param t		target tank
	 */
	public void shootToward( Tank t ) {
		missile = ( new Missile( (int)getBarrel().getX(), (int)getBarrel().getY(),
		 Color.WHITE ) );
		missile.setCurrentLoc( missile.getStartLoc() );
		missile.findMoveRate( (int)t.location.getX(), (int)t.location.getY() );
		missile.setTarget( new Point ( (int)t.location.getX(),(int)t.location.getY() ) );
		missile.setShot( true );
	}
}
