import java.util.Scanner;

/**
 * Used to run the game.
 * @author Nicholas Grant
 *
 */
public class TanksMain {
	/**
	 * Passes the user's difficulty choice into the frame object.
	 */
	public static void main( String[] args ) {
		
		int difficulty = difficultyMenu();
		new Frame( difficulty );
	}
	/**
	 * Handles getting the user's desired difficulty
	 * @return		the user's difficulty
	 */
	public static int difficultyMenu() {
		System.out.println("Select a difficulty: ");
		System.out.println("1. One enemy");
		System.out.println("2. Two enemies");
		System.out.println("3. Three enemies");
		return getValidInt( 1, 3 );
	}
	/**
	 * Error check the user's input
	 * @param low	lower-bound of user input
	 * @param high	upper-bound of user input
	 * @return		user's input
	 */
	public static int getValidInt( int low, int high ) {
		Scanner in = new Scanner( System.in );
		boolean invalid = true;
		int value = 0;
		while ( invalid ) {
	            if( in.hasNextInt() ) {
			value = in.nextInt();
			if ( value >= low && value <= high ) {
	                    invalid = false;
			}
			else {
	                    System.out.println("Invalid- Retry: ");
			}
	            }
	            else {
	                in.next();
	                System.out.println("Invalid input- Retry: ");
	            }
		}
		in.close();
		return value;
	    }
}
