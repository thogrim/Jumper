import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Abstract class that represents game object
 *
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public abstract class GameObject extends Rectangle2D.Float{

	protected BufferedImage texture_;
	
	/**
	 * Constructs new game object with texture 
	 * 
	 * @param textureFilePath Path to the texture
	 */
	public GameObject(String textureFilePath, int xPos, int yPos){
		super(xPos*World.TILE_SIZE,yPos*World.TILE_SIZE,World.TILE_SIZE,World.TILE_SIZE);
		try {
			texture_ = ImageIO.read(new File(textureFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
//		xPos_ = xPos*World.TILE_SIZE;
//		yPos_ = yPos*World.TILE_SIZE;
	}
	
	/**
	 * Returns position x of game object
	 * 
	 * @return position in X axis
	 */
//	public float getX(){
//		return xPos_;
//	}
	
	/**
	 * Returns position y of game object
	 * 
	 * @return position in Y axis
	 */
//	public float getY(){
//		return yPos_;
//	}
	
	/**
	 * Draws object's texture
	 * 
	 * @param g Graphics context
	 */
	public void draw(Graphics g){
		g.drawImage(texture_, (int)getX(), (int)getY() ,texture_.getWidth(), texture_.getHeight(), null);
	}
}
