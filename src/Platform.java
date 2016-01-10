@SuppressWarnings("serial")
public class Platform extends GameObject{
	
	/**
	 * Indicates Platform's current state
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
		 * Platform has been destroyed; It is no longer displayed on screen and it does not interact with player.
		 */
		DEAD
	}
	
	/**
	 * Current platform state
	 * 
	 * @see PlatformState
	 */
	private PlatformState state_;
	
	public Platform(int xPos, int yPos) {
		super(Textures.platformTexture_,xPos,yPos);
		state_ = PlatformState.ALIVE;
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
	 * Sets platform's state to PlatformState.STEPPED_ON
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
		texture_ = Textures.platformTexture_;
	}

}
