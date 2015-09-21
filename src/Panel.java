import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JPanel;

/**
 * Panel handles painting the components to the frame and running the game itself.
 * @author Nicholas Grant
 *
 */
public class Panel extends JPanel implements Runnable, KeyListener, MouseListener,
MouseMotionListener {
	/**
	 * Thread used to continuously run the game.
	 */
	private Thread t;
	/**
	 * Represents the game map in 2D array form.
	 */
	private int[][] map;
	/**
	 * Holds the tank enemies.
	 */
	private ArrayList<Tank> enemies;
	/**
	 * Remembers the keys currently being pressed.
	 */
	private Set<Character> pressed;
	/**
	 * Represents the user's tank.
	 */
	private Tank player;
	/**
	 * Represents the location of the mouse.
	 */
	private Point mouseLoc;
	/**
	 * Indicates the difficulty level of the game. (easy = 1 tank, medium = 2 tanks,
	 * hard = 3 tanks)
	 */
	private int difficulty;
	/**
	 * True = player has been defeated, false otherwise
	 */
	private boolean gameOver;
	/**
	 * True = player has won, false otherwise
	 */
	private boolean win;
	/**
	 * Panel constructor, initializes game board and all elements necessary.
	 * @param d		difficulty level
	 */
	public Panel( int d ) {
		difficulty = d;
		enemies = new ArrayList<Tank>();
		pressed = new HashSet<Character>();
		setBackground( Color.BLACK );
		loadMap();
		t = new Thread(this);
		player = initializeTanks();
		t.start();
		mouseLoc = new Point( 400, 400 );
		gameOver = false;
		win = false;
		addMouseMotionListener(this);
		addKeyListener(this);
		addMouseListener(this);
		setFocusable(true);
	}
	/**
	 * Paints the player tank and enemy tanks. Also paints the number of lives
	 * the player has. If the player loses, "Game over!" is displayed at the top.
	 * Otherwise, "You win!" is displayed.
	 * 
	 * @param g		the Graphics object necessary to paint to the frame
	 */
	public void paintComponent( Graphics g ) {
		super.paintComponent( g );
		drawMap( g );
		if ( player.getLives() > 0 ) {
			player.drawTank( g, (int)mouseLoc.getX(), (int)mouseLoc.getY() );
			player.drawLives( g );
		}
		Iterator<Tank> iter = enemies.iterator();
		while ( iter.hasNext() ) {
			Tank e = iter.next();
			e.drawTank( g, (int)player.getLocation().getX(),
			(int)player.getLocation().getY() );
		}
		if ( gameOver ) {
			g.setColor( Color.RED );
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 18));
			g.drawString( "Game over!", 385, 50);
		}
		if ( win ) {
			g.setColor( Color.RED );
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 18));
			g.drawString( "You win!", 390, 50);
		}
	}
	/**
	 * Initialize the player and enemy tanks.
	 * @return		returns the player's tank
	 */
	public Tank initializeTanks() {
		Tank player = new Tank( 360, 360, 0, Color.RED );
		if ( difficulty == 1 ) {
			enemies.add( new Tank ( 360, 490, 2, Color.GRAY ) );
		}
		else if ( difficulty == 2 ) {
			enemies.add( new Tank ( 360, 490, 2, Color.GRAY ) );
			enemies.add( new Tank ( 490, 490, -2, Color.GRAY ) );
		}
		else {
			enemies.add( new Tank ( 360, 490, 2, Color.GRAY ) );
			enemies.add( new Tank ( 490, 490, -2, Color.GRAY ) );
			enemies.add( new Tank ( 490, 360, -2, Color.GRAY ) );
		}
		return player;
	}
	/**
	 * Iterates over the map array to draw it to the frame
	 * @param g		the Graphics object necessary to paint to the frame
	 */
	public void drawMap( Graphics g ) {
		for ( int i = 0; i < map.length; i++ ) {
			for( int j = 0; j < map.length; j++ ) {
				switch( map[i][j] ) {
			    	case 0:
			     		g.setColor( Color.BLACK );
			     		g.fillRect( j*65, i*65, 65, 65 );
			     		break;
			     	case 1:
			     		g.setColor( Color.YELLOW );
			     		g.fillRect( j*65, i*65, 65, 65 );
			     		break;
			     }
			}  
		}
	}
	/**
	 * Loads the given text file into the map array
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
	 * Access the map array
	 * @return		2D array game map
	 */
	public int[][] getMap() {
		return map;
	}
	/**
	 * Not used for this program.
	 */
	public void mouseDragged(MouseEvent e) {
	}
	/**
	 * When the mouse is moved, its location is recorded to mouseLoc.
	 * Then, drawTank() uses this position to draw the barrel appropriately.
	 */
	public void mouseMoved( MouseEvent e ) {
		int x = e.getX();
		int y = e.getY();
		mouseLoc = new Point( x, y );
	}
	/**
	 * When the mouse is clicked, a missile object is created for the player
	 * with the mouse's position upon clicking set up as the target, calculates the
	 * dx and dy necessary to reach that point, and sets the start location to be the 
	 * current position of the barrel. If a missile is already active, another missile
	 * won't be shot until there are no missiles active.
	 */
	public void mouseClicked( MouseEvent e ) {
		if ( player.getMissile().isShot() ) {
			return;
		}
		player.setMissile( new Missile( (int)player.getBarrel().getX(), (int)player.getBarrel().getY(), Color.WHITE ) );
		player.getMissile().setCurrentLoc( player.getMissile().getStartLoc() );
		player.getMissile().findMoveRate( e.getX(), e.getY() );
		player.getMissile().setTarget( new Point ( e.getX(), e.getY() ) );
		player.getMissile().setShot( true );
	}
	/**
	 * Not used for this program.
	 */
	public void mouseEntered(MouseEvent e) {
	}
	/**
	 * Not used for this program.
	 */
	public void mouseExited(MouseEvent e) {
	}
	/**
	 * Not used for this program.
	 */
	public void mousePressed(MouseEvent e) {
	}
	/**
	 * Not used for this program.
	 */
	public void mouseReleased(MouseEvent e) {
	}
	/**
	 * When the user presses a key, that key is placed into the 
	 * Set of keys pressed. As that key is pressed, the user's tank moves in that
	 * direction. Also allows for ordinal directions in addition to cardinal directions.
	 * When the key is released, the tank stops moving.
	 */
	public void keyPressed( KeyEvent e ) {
		pressed.add( e.getKeyChar() );
		determineDirection();
		if ( pressed.size() >= 4 ) {
			return;
		}
	}
	/**
	 * Once the key is released, that key is removed from
	 * the Set of keys pressed, then the current direction
	 * is determined from the remaining keys.
	 */
	public void keyReleased(KeyEvent e) {
		pressed.remove( e.getKeyChar() );
		if ( pressed.size() == 0 ) {
			player.setDirection( 0 );
		}
		else {
			determineDirection();
		}
	}
	/**
	 * Not used for this program.
	 */
	public void keyTyped(KeyEvent e) {
	}
	/**
	 * Determines the direction the tank will move based
	 * on the current keys pressed.
	 */
	public void determineDirection() {
		if ( pressed.size() == 1 ) {
			if ( pressed.contains( 'w' ) ) {
				player.setDirection( -1 );
			}
			else if ( pressed.contains( 'a' ) ) {
				player.setDirection( -2 );
			}
			else if ( pressed.contains( 's' ) ) {
				player.setDirection( 1 );
			}
			else if ( pressed.contains( 'd' ) ) {
				player.setDirection( 2 );
			}
		}
		
		if ( pressed.size() == 2 ) {
			if ( pressed.contains( 'w' ) && pressed.contains( 'd' ) ) {
				player.setDirection(3);
			}
			else if ( pressed.contains( 'w' ) && pressed.contains( 'a' ) ) {
				player.setDirection(-3);
			}
			else if ( pressed.contains( 's' ) && pressed.contains( 'd' ) ) {
				player.setDirection(4);
			}
			else if ( pressed.contains( 's' ) && pressed.contains( 'a' ) ) {
				player.setDirection(-4);
			}
		}
		
		
		if ( pressed.size() == 3 ) {
			if ( pressed.contains( 'a' ) && pressed.contains( 'd' ) ) {
				if ( pressed.contains( 'w' ) ) {
					player.setDirection( -1 );
				}
				if (pressed.contains( 's' ) ) {
					player.setDirection( 1 );
				}
			}
			if ( pressed.contains( 'w' ) && pressed.contains( 's' ) ) {
				if ( pressed.contains( 'a' ) ) {
					player.setDirection( -2 );
				}
				if (pressed.contains( 'd' ) ) {
					player.setDirection( 2 );
				}
			}
		}
		if ( pressed.size() >= 4 ) {
			return;
		}
	}
	/**
	 * Runs the thread necessary to run the game.
	 */
	public void run() {
		while ( true ) {
			repaint();
			checkHits();
			if ( player.getLives() == 0 ) {
				gameOver = true;
			}
			if ( enemies.size() == 0 ) {
				win = true;
			}
			enemyActions();
			try {
				Thread.sleep( 50 );
			} catch (InterruptedException e) {
				System.out.println("Program interrupted.");
			}
		}
	}
	/**
	 * Handles the enemy movements and missile firing.
	 */
	public void enemyActions() {
		for ( Tank e : enemies ) {
			if ( !gameOver && !win ) {
				e.moveRandom( );
				if ( !e.getMissile().isShot() ) {
					e.shootToward( player );
				}
			}
			else {
				e.getMissile().setShot( false );
				e.setDirection( 0 );
			}
		}
	}
	/**
	 * Checks to see if a tank has been hit by a missile.
	 */
	public void checkHits() {
		Iterator<Tank> iter = enemies.iterator();
		while ( iter.hasNext() ) {
			Tank e = iter.next();
			if ( player.getMissile().isShot() ) {
				if ( e.testHit( player.getMissile().getCurrentLoc() ) ) {
					iter.remove();
					player.getMissile().reset( );
				}
			}
			if ( e.getMissile().isShot() ) {
				if ( player.testHit( e.getMissile().getCurrentLoc() ) ) {
					player.reduceLives();
					e.getMissile().reset( );
				}
			}
		}
	}
}
