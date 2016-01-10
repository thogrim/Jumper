import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 
 * @author Mateusz Antoniak
 */
public class Textures {
	
	/**
	 * Indicates if textures have already been loaded into memory
	 */
	private static boolean texturesLoaded_ = false;
	
	/**
	 * Platform texture
	 */
	public static BufferedImage platformTexture_;
	
	/**
	 * Bonus texture
	 */
	public static BufferedImage bonusTexture_;
	
	/**
	 * Player texture(while standing)
	 */
	public static BufferedImage playerStandTexture_;
	
	/**
	 * Player texture(while moving left)
	 */
	public static BufferedImage playerMoveLeftTexture_;
	
	/**
	 * Player texture(while moving right)
	 */
	public static BufferedImage playerMoveRightTexture_;
	
	/**
	 * Reads texture from file located in specified path
	 * 
	 * @param texture - reference to the texture
	 * @param filepath - path to the texture
	 */
//	private static void readTexture(BufferedImage texture, String filepath){
//		//if(texture!=null){
//			try{
//				texture = ImageIO.read(new File(filepath));
//			}
//			catch(IOException e){
//				e.printStackTrace();
//			}
//		//}
//	}
	
	/**
	 * Loads all textures used in game
	 */
	public static void readTextures(){
		if(!texturesLoaded_){
//			readTexture(bonusTexture_,Config.BONUS_TEXTURE);
//			readTexture(platformTexture_,Config.PLATFORM_TEXTURE);
//			readTexture(playerMoveLeftTexture_,Config.PLAYER_MOVE_LEFT_TEXTURE);
//			readTexture(playerMoveRightTexture_,Config.PLAYER_MOVE_RIGHT_TEXTURE);
//			readTexture(playerStandTexture_,Config.PLAYER_STAND_TEXTURE);
			try{
				bonusTexture_ = ImageIO.read(new File(Config.BONUS_TEXTURE));
				platformTexture_ = ImageIO.read(new File(Config.PLATFORM_TEXTURE));
				playerMoveLeftTexture_ = ImageIO.read(new File(Config.PLAYER_MOVE_LEFT_TEXTURE));
				playerMoveRightTexture_ = ImageIO.read(new File(Config.PLAYER_MOVE_RIGHT_TEXTURE));
				playerStandTexture_ = ImageIO.read(new File(Config.PLAYER_STAND_TEXTURE));
			}
			catch(IOException e){
				e.printStackTrace();
			}
			texturesLoaded_ = true;
		}
	}

}
