import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Zawiera definicje wszystkich sta³ych u¿ytych w aplikacji
 * 
 * @author Mateusz Antoniak
 */
public class Config {
	
	/**
	 * Znak nowej linii
	 */
	public static final String NEW_LINE = System.getProperty("line.separator");
	
	/**
	 * Liczba poziomów w grze
	 */
	public static final int NUM_OF_LEVELS = 10;
	
	/**
	 * Maksymalna liczba profili
	 */
	public static int NUM_OF_PROFILES = 6;
	
	/**
	 * Liczba ¿yæ gracza na pocz¹tku gry
	 */
	public static int NUM_OF_PLAYER_LIFES = 10;
	
	/**
	 * Liczba wyœwietlanych najlepszych wyników
	 */
	public static final int NUM_OF_HIGHSCORES = 5; 
	
	/**
	 * Œcie¿ka do folderu z definicjami poziomów
	 */
	public static final String LEVELS_PATH = "res/levels";
	
	/**
	 * Œcie¿ka do folderu z zapisanymi profilami
	 */
	public static final String PROFILES_PATH = "res/profiles";
	
	/**
	 * Œcie¿ka do pliku z najlepszymi wynikami
	 */
	public static final String HIGHSCORES_PATH = "highscores.txt";
	
	/**
	 * Œcie¿ka do tekstury gracza(podczas stania w miejscu)
	 */
	public static final String PLAYER_STAND_TEXTURE = "res/img/player.png";
	
	/**
	 * Œcie¿ka do tekstury gracza(podczas poruszania siê w prawo)
	 */
	public static final String PLAYER_MOVE_RIGHT_TEXTURE = "res/img/playerRight.png";
	
	/**
	 * Œcie¿ka do tekstury gracza(podczas poruszania siê w lewo)
	 */
	public static final String PLAYER_MOVE_LEFT_TEXTURE = "res/img/playerLeft.png";
	
	/**
	 * Œcie¿ka do tekstury platformy
	 */
	public static final String PLATFORM_TEXTURE = "res/img/platform.png";
	
	/**
	 * Œcie¿ka do tekstury bonusu
	 */
	public static final String BONUS_TEXTURE = "res/img/bonus.png";
	
	/**
	 * Rozmiar okna na starcie
	 */
	public static final Dimension WINDOW_SIZE= new Dimension(600, 600);
	
	/**
	 * Rozmiar okna gdy gracz rozpoczyna rozgrywkê
	 */
	public static final Dimension GAMEPLAY_WINDOW_SIZE = new Dimension(1000,600);
	
	/**
	 * Przechowuje nazwy poziomów
	 */
	private static String[] LEVEL_NAMES = new String[NUM_OF_LEVELS];
	
	/**
	 * Wczytuje konfiguracjê
	 */
	public static void readConfiguration(){
		try {
			Scanner scanner = new Scanner(new File("config.txt"));
			//skip comments
			scanner.nextLine();
			Config.NUM_OF_PLAYER_LIFES = scanner.nextInt();
			scanner.nextLine();
			for(int i=0; i< NUM_OF_LEVELS; ++i){
				scanner.nextLine();
				LEVEL_NAMES[i] = scanner.next();
			}
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
	 * Returns level name
	 * 
	 * @param id - Id of a level
	 * @return Name of level
	 */
	public static String getLevelName(int id){
		return LEVEL_NAMES[id];
	}
	
}
