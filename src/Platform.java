import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class Platform extends GameObject{
	
	/**
	 * Simple enum that indicates Platform's current state
	 */
	private enum PlatformState{
		/**
		 * Platform is normally displayed on screen and interacts with player.
		 */
		ALIVE,
		/**
		 * Player is currently standing on this platform.
		 */
		STEPPED_ON,
		/**
		 * Platform has been destroyed and is no longer displayed on screen and nor it interacts with player.
		 */
		DEAD
	}
	
	/**
	 * Current platform state
	 * 
	 * @see PlatformState
	 */
	private PlatformState state_;
	
	private BufferedImage aliveTexture_;
	
	public Platform(int xPos, int yPos) {
		super(Config.PLATFORM_TEXTURE,xPos,yPos);
		state_ = PlatformState.ALIVE;
		aliveTexture_ = texture_;
	}
	
	/**
	 * 
	 * 
	 * @return 
	 */
	public boolean isAlive(){
		return state_ != PlatformState.DEAD;
	}
	
	/**
	 * Sets platform's state to PlatformState.STEPPED_ON. This method is invoked when player steps onto platform.
	 */
	public void stepOn(){
		state_ = PlatformState.STEPPED_ON;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean steppedOn(){
		return state_ == PlatformState.STEPPED_ON;
	}
	
	/**
	 * 
	 */
	public void destroy() {
		state_ = PlatformState.DEAD;
		texture_ = null;
	}
	
	/**
	 * 
	 */
	public void reset(){
		state_ = PlatformState.ALIVE;
		texture_ = aliveTexture_;
	}

}
