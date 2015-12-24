import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * This class holds all configuration data and constants
 * 
 * @author Mateusz Antoniak
 */
public class Config {
	
	/**
	 * Number of levels in game
	 */
	public static final int NUMBER_OF_LEVELS = 10;
	
	/**
	 * Maximum number of profiles in game
	 */
	public static final int NUMBER_OF_PROFILES = 6;
	
	/**
	 * Number of lifes that player has at the start of the game
	 */
	public static final int NUMBER_OF_PLAYER_LIFES = 10;
	
	/**
	 * Path to folder containing level data
	 */
	public static final String LEVELS_PATH = "res/levels";
	
	/**
	 * Path to folder containing profiles data
	 */
	public static final String PROFILES_PATH = "res/profiles";
	
	/**
	 * Path to image containing player's texture
	 */
	public static final String PLAYER_STAND_TEXTURE = "res/img/player.png";
	
	/**
	 * Path to image containing player's texture
	 */
	public static final String PLAYER_MOVE_RIGHT_TEXTURE = "res/img/playerRight.png";
	
	/**
	 * Path to image containing player's texture
	 */
	public static final String PLAYER_MOVE_LEFT_TEXTURE = "res/img/playerLeft.png";
	
	/**
	 * Path to image containing platform's texture
	 */
	public static final String PLATFORM_TEXTURE = "res/img/platform.png";
	
	/**
	 * Path to image containing platform's texture
	 */
	public static final String BONUS_TEXTURE = "res/img/bonus.png";
	
	/**
	 * Initial window size
	 */
	private static Dimension windowSize_= new Dimension(600, 600);
	
	/**
	 * Window size when application enters gameplay state
	 */
	private static Dimension gameplayWindowSize_ = new Dimension(1000,600);
	
	/**
	 * Reads main configuration data from "config.txt" file. If "config.txt"
	 * is not found, configuration data holds default values.
	 */
	public static void readConfiguration(){
		try {
			Scanner scanner = new Scanner(new File("config.txt"));
			//skip comments
			System.out.println(scanner.nextLine());
			System.out.println(scanner.nextLine());
			//read initial window size
			windowSize_.width = scanner.nextInt();
			windowSize_.height = scanner.nextInt();
			System.out.println(windowSize_.width+" "+windowSize_.height);
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Main configuration file \"config.txt\" not found!");
			e.printStackTrace();
		} catch(InputMismatchException e){
			System.out.println("Error in reading config.txt");
			e.printStackTrace();
		}
	}
	
	/**
	 * @return Initial window size
	 */
	public static Dimension getWindowSize(){
		return windowSize_;
	}
	
	/**
	 * @return Initial window size when in Gameplay state
	 */
	public static Dimension getGameplayWindowSize(){
		return gameplayWindowSize_;
	}
}
