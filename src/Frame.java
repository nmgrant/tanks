import javax.swing.JFrame;

/**
 * Sets up the frame for the game to be displayed.
 * @author Nicholas Grant
 *
 */
public class Frame extends JFrame {
	/**
	 * Frame constructor - Resolution: 845x845
	 * Uses a panel to paint the game elements to the frame.
	 * @param d			difficulty of the game
	 */
	public Frame( int d ) {
		add( new Panel( d ) );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setSize( 845, 845 );
		setVisible( true );
	}
}
