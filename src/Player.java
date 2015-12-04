import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Player represents a game character
 *
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class Player extends GameObject{
	
	/**
	 * Texture displayed when player is not moving in X axis
	 */
	BufferedImage standTexture_;
	
	/**
	 * Texture displayed when player is moving right
	 */
	BufferedImage moveRightTexture_;
	
	/**
	 * Texture displayed when player is moving left
	 */
	BufferedImage moveLeftTexture_;
	
	/**
	 * Simple enum that indicates player's moving state in X axis
	 */
	private enum PlayerState{
		/**
		 * Player's velocity in X axis is equal to 0
		 */
		STANDING,
		/**
		 * Player's velocity in X axis is equal to Player.VELOCITY
		 */
		MOVING_RIGHT,
		/**
		 * Player's velocity in X axis is equal to -Player.VELOCITY
		 */
		MOVING_LEFT,
	};
	
	/**
	 * Current player state;
	 */
	private PlayerState state_;
	
	private static final int STANDING = 0;
	private static final int MOVE_RIGHT_PRESSED = 1;
	private static final int MOVE_LEFT_PRESSED = 2;	
	private int mstate_;
	
	/**
	 * Indicates if player is not standing on platform
	 */
	private boolean falling_;
	
	/**
	 * Velocity of a player in X axis in pixels per second
	 */
	private final static float VELOCITY_X = 270.f;
	
	/**
	 * World's gravity
	 */
	private float worldGravity_;
	
	/**
	 * Velocity of a player in Y axis in pixels per second
	 */
	private float velocityY_;
	
	/**
	 * Player's X position in previous frame
	 */
	private float oldX_;
	
	/**
	 * Player's Y position in previous frame 
	 */
	private float oldY_;
	
	public Player(int xPos, int yPos, float worldGravity){
		super(Config.PLAYER_STAND_TEXTURE,xPos,yPos);
		try {
			moveRightTexture_ = ImageIO.read(new File(Config.PLAYER_MOVE_RIGHT_TEXTURE));
			moveLeftTexture_ = ImageIO.read(new File(Config.PLAYER_MOVE_LEFT_TEXTURE));			
		} catch (IOException e) {
			e.printStackTrace();
		}
		worldGravity_ = worldGravity;
		standTexture_ = texture_;
		oldX_ = this.x;
		oldY_ = this.y;
		state_ = PlayerState.STANDING;
		falling_ = false;
		velocityY_ = 0.f;
		
		//mstate_ =0;
	}
	
	public void startMovingRight(){
		//state_ = PlayerState.MOVING_RIGHT;
		//texture_ = moveRightTexture_;
		mstate_ |= MOVE_RIGHT_PRESSED;
		resolveMoveState();
	}
	
	public void startMovingLeft(){
		//state_ = PlayerState.MOVING_LEFT;
		//texture_ = moveLeftTexture_;
		mstate_ |= MOVE_LEFT_PRESSED;
		resolveMoveState();
	}

//	public void stopMoving() {
//		state_ = PlayerState.STANDING;
//		texture_ = standTexture_;
//	}
	
	public void stopMovingRight() {
		mstate_ &= ~MOVE_RIGHT_PRESSED;
		resolveMoveState();
	}
	
	public void stopMovingLeft(){
		mstate_ &= ~MOVE_LEFT_PRESSED;
		resolveMoveState();
	}
	
	private void resolveMoveState(){
		switch(mstate_){
		case STANDING:
			state_ = PlayerState.STANDING;
			texture_ = standTexture_;
			break;
		case MOVE_LEFT_PRESSED:
			state_ = PlayerState.MOVING_LEFT;
			texture_ = moveLeftTexture_;
			break;
		case MOVE_RIGHT_PRESSED:
			state_ = PlayerState.MOVING_RIGHT;
			texture_ = moveRightTexture_;
			break;
		}
	}
	
	public void jump(){
		if(!falling_){
			velocityY_ = -800.f;
			falling_ = true;
		}
	}
	
	public void move(){
		//updating old position
		oldX_ = this.x;
		oldY_ = this.y;
		
		//moving in X axis
		switch(state_){
		case MOVING_LEFT:
			this.x -= VELOCITY_X*World.UPDATE_DELTA_TIME/1000;
			break;
		case MOVING_RIGHT:
			this.x += VELOCITY_X*World.UPDATE_DELTA_TIME/1000;
			break;
		case STANDING:
			break;
		default:
			break;
		}
		
		//moving in Y axis
		if(falling_){
			velocityY_ += worldGravity_*World.UPDATE_DELTA_TIME/1000;
			this.y += velocityY_*World.UPDATE_DELTA_TIME/1000;
		}
	}

	public void onCollsionWithPlatform(Platform platform) {
		if(oldY_+this.height > platform.y && oldY_ < platform.y + platform.height){
			if(state_ == PlayerState.MOVING_RIGHT)
				this.x = platform.x - this.width;
			else if(state_ == PlayerState.MOVING_LEFT)
				this.x = platform.x + platform.width;
		}
		else if(oldX_+this.width> platform.x && oldX_ < platform.x + platform.width){
			if(velocityY_ < 0.f){
				this.y = platform.y + platform.height;
				velocityY_ = 0.f;
			}
			else{
				this.y = platform.y - this.height;
				velocityY_ = 0.f;
				falling_ = false;
			}
		}
	}

	public void checkForStanding(ArrayList<Platform> platforms) {
		if(falling_)
			return;
		//boolean standingOnPlatform = false;
		falling_ = true;
		for(Platform platform : platforms){
			if(this.x+this.width > platform.x && this.x < platform.x + platform.width && this.y + this.height == platform.y){
				//platform.onPlayerStand();
				falling_ = false;
			}
//			else{
//				
//			}
		}
	}
}