
@SuppressWarnings("serial")
public class Bonus extends GameObject{
	
	public static final int SCORE = 1000;
	
	private boolean collected_;

	public Bonus(int xPos, int yPos) {
		super(Config.BONUS_TEXTURE, xPos, yPos);
		collected_ = false;
	}
	
	public boolean collected(){
		return collected_;
	}
	
	public void collect(){
		collected_= true;
	}
	
	public void reset(){
		collected_ = false;
	}
}
