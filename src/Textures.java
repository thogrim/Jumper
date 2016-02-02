import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Przechowuje tektury 
 * 
 * @author Mateusz Antoniak
 */
public class Textures {
	
	/**
	 * Wskazuje, czy tekstury zosta³y za³adowane do pamiêci
	 */
	private static boolean texturesLoaded_ = false;
	
	/**
	 * Tekstura platformy
	 */
	public static BufferedImage PLATFORM_TEXTURE;
	
	/**
	 * Tekstura bonusu
	 */
	public static BufferedImage BONUS_TEXTURE;
	
	/**
	 * Tekstura gracza(podczas stania w miejscu)
	 */
	public static BufferedImage PLAYER_STAND_TEXTURE;
	
	/**
	 * Tekstura gracza(podczas poruszania siê w lewo)
	 */
	public static BufferedImage PLAYER_MOVE_LEFT_TEXTURE;
	
	/**
	 * Tekstura gracza(podczas poruszania siê w prawo)
	 */
	public static BufferedImage PLAYER_MOVE_RIGHT_TEXTURE;
	
	/**
	 * Wczytuje tekstury
	 */
	public static void readTextures(){
		if(!texturesLoaded_){
			try{
				BONUS_TEXTURE = ImageIO.read(new File(Config.BONUS_TEXTURE));
				PLATFORM_TEXTURE = ImageIO.read(new File(Config.PLATFORM_TEXTURE));
				PLAYER_MOVE_LEFT_TEXTURE = ImageIO.read(new File(Config.PLAYER_MOVE_LEFT_TEXTURE));
				PLAYER_MOVE_RIGHT_TEXTURE = ImageIO.read(new File(Config.PLAYER_MOVE_RIGHT_TEXTURE));
				PLAYER_STAND_TEXTURE = ImageIO.read(new File(Config.PLAYER_STAND_TEXTURE));
			}
			catch(IOException e){
				e.printStackTrace();
			}
			texturesLoaded_ = true;
		}
	}

}
