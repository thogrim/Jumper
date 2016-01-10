
@SuppressWarnings("serial")
public class Bonus extends GameObject{
	
	public static final int SCORE = 1000;
	
	private boolean collected_;

	public Bonus(int xPos, int yPos) {
		super(Textures.bonusTexture_, xPos, yPos);
		collected_ = false;
	}
	
	public boolean collected(){
		return collected_;
	}
	
	public void collect(){
		collected_= true;
		texture_ = null;
	}
	
	public void reset(){
		collected_ = false;
		texture_ = Textures.bonusTexture_;
	}
}
