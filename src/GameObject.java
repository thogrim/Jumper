import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Reprezentuje abstrakcyjny obiekt w grze
 *
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public abstract class GameObject extends Rectangle2D.Float{
	
	/**
	 * Referencja na tekturê
	 */
	protected BufferedImage texture_;
	
	/**
	 * Konstruuje nowy obiekt na pozycji x, y i z podan¹ tekstur¹
	 * 
	 * @param texture - Tekstura obiektu
	 * @param xPos - Pozycja X(w kafelkach)
	 * @param yPos - Pozycja Y(w kafelkach)
	 */
	public GameObject(BufferedImage texture, int xPos, int yPos){
		super(xPos*World.TILE_SIZE,yPos*World.TILE_SIZE,World.TILE_SIZE,World.TILE_SIZE);
		texture_ = texture;
	}
	
//	/**
//	 * Constructs new game object with texture 
//	 * 
//	 */
//	public GameObject(BufferedImage texture, int xPos, int yPos, int width, int height){
//		super(xPos*World.TILE_SIZE,yPos*World.TILE_SIZE,width,height);
//		texture_ = texture;
//	}
	
	/**
	 * Rysuje obiekt
	 * 
	 * @param g Kontekst graficzny
	 */
	public void draw(Graphics g){
		if(texture_!=null)
			g.drawImage(texture_,
				(int)(x*Gameplay.getScaleX()),
				(int)(y*Gameplay.getScaleY()),
				(int)(texture_.getWidth()*Gameplay.getScaleX()),
				(int)(texture_.getHeight()*Gameplay.getScaleY()),
				null);
	}
}
