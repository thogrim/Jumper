/**
 * Represents a player character
 *
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class Player extends GameObject{
	
	/**
	 * Stan poruszania siê gracza w osi X
	 */
	private enum PlayerState{
		/**
		 * Gracz stoi w miejscu
		 */
		STANDING,
		/**
		 * Gracz porusza siê w prawo
		 */
		MOVING_RIGHT,
		/**
		 * Gracz porusza siê w lewo
		 */
		MOVING_LEFT,
	};
	
	/**
	 * Aktualny stan poruszania siê w osi X
	 */
	private PlayerState state_;
	
	/**
	 * Sta³a wskazuj¹ca, ¿e ¿aden klawisz ruchu nie jest wciœniêty
	 */
	private static final int STANDING = 0;
	
	/**
	 * Sta³a wskazuj¹ca, ¿e tylko klawisz ruchu w prawo jest wciœniêty
	 */
	private static final int MOVE_RIGHT_PRESSED = 1;
	
	/**
	 * Sta³a wskazuj¹ca, ¿e tylko klawisz ruchu w lewo jest wciœniêty
	 */
	private static final int MOVE_LEFT_PRESSED = 2;
	
	/**
	 * Informacja o wciœniêtych klawiszach ruchu
	 */
	private int moveKeysState_;
	
	/**
	 * Wskazuje, czy gracz spada
	 */
	private boolean falling_;
	
	/**
	 * Prêdkoœæ gracza w osi X(w pikselach na sekundê)
	 */
	private final static float VELOCITY_X = 270.f;
	
	/**
	 * Grawitacja œwiata gry
	 */
	private float worldGravity_;
	
	/**
	 * Prêdkoœæ gracza w osi Y(w pikselach na sekundê)
	 */
	private float velocityY_;
	
	/**
	 * Pozycja X gracza w poprzedniej klatce
	 */
	private float oldX_;
	
	/**
	 * Pozycja Y gracza w poprzedniej klatce
	 */
	private float oldY_;
	
	/**
	 * Konstruuje postaæ gracza na pozycji x, y
	 * 
	 * @param xPos - Pozycja x(w kafelkach)
	 * @param yPos - Pozycja y(w kafelkach)
	 * @param worldGravity - grawitacja œwiata gry
	 */
	public Player(int xPos, int yPos, float worldGravity){
		super(Textures.PLAYER_STAND_TEXTURE,xPos,yPos);
		worldGravity_ = worldGravity;
		oldX_ = this.x;
		oldY_ = this.y;
		state_ = PlayerState.STANDING;
		falling_ = false;
		velocityY_ = 0.f;
	}
	
	/**
	 * Resetuje pozycjê gracza
	 * 
	 * @param xTilePos X position in tiles
	 * @param yTilePos Y position in tiles
	 */
	public void respawn(int xTilePos, int yTilePos){
		this.x = xTilePos*World.TILE_SIZE;
		this.y = yTilePos*World.TILE_SIZE;
		velocityY_ = 0.f;
		state_ = PlayerState.STANDING;
		falling_ = false;
	}
	
	/**
	 * Informuje o wciœniêciu klawisza poruszania siê w prawo
	 */
	public void startMovingRight(){
		moveKeysState_ |= MOVE_RIGHT_PRESSED;
		resolveMoveState();
	}
	
	/**
	 * Informuje o wciœniêciu klawisza poruszania siê w lewo
	 */
	public void startMovingLeft(){
		moveKeysState_ |= MOVE_LEFT_PRESSED;
		resolveMoveState();
	}
	
	/**
	 * Informuje o puszczeniu klawisza poruszania siê w prawo
	 */
	public void stopMovingRight() {
		moveKeysState_ &= ~MOVE_RIGHT_PRESSED;
		resolveMoveState();
	}
	
	/**
	 * Informuje o puszczeniu klawisza poruszania siê w lewo
	 */
	public void stopMovingLeft(){
		moveKeysState_ &= ~MOVE_LEFT_PRESSED;
		resolveMoveState();
	}
	
	/**
	 * Ustawia nowy stan poruszania siê gracza
	 */
	private void resolveMoveState(){
		switch(moveKeysState_){
		case STANDING:
			state_ = PlayerState.STANDING;
			texture_ = Textures.PLAYER_STAND_TEXTURE;
			break;
		case MOVE_LEFT_PRESSED:
			state_ = PlayerState.MOVING_LEFT;
			texture_ = Textures.PLAYER_MOVE_LEFT_TEXTURE;
			break;
		case MOVE_RIGHT_PRESSED:
			state_ = PlayerState.MOVING_RIGHT;
			texture_ = Textures.PLAYER_MOVE_RIGHT_TEXTURE;
			break;
		}
	}
	
	/**
	 * Ustawia flagê wskazuj¹c¹ czy gracz spada
	 * 
	 * @param falling - True jeœli gracz spada, false w przeciwnym wypdaku
	 */
	public void setFalling(boolean falling){
		falling_ = falling;
	}
	
	/**
	 * Powoduje skok gracza
	 */
	public void jump(){
		if(!falling_){
			velocityY_ = -800.f;
			falling_ = true;
		}
	}
	
	/**
	 * Aktualizuje pozycjê gracza
	 */
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

	/**
	 * Obs³uguje zdarzenie kolizji gracza z platform¹
	 * 
	 * @param platform - Platforma, z któr¹ dosz³o do kolzji
	 */
	public void onCollsionWithPlatform(Platform platform) {
		//if player collided from left or right
		if(oldY_+this.height > platform.y && oldY_ < platform.y + platform.height){
			if(state_ == PlayerState.MOVING_RIGHT)
				this.x = platform.x - this.width;
			else if(state_ == PlayerState.MOVING_LEFT)
				this.x = platform.x + platform.width;
		}
		//if player collided from up or down
		else if(oldX_+this.width> platform.x && oldX_ < platform.x + platform.width){
			//if player is moving up
			if(velocityY_ < 0.f){
				this.y = platform.y + platform.height;
				velocityY_ = 0.f;
			}
			//else if player is falling down
			else{
				this.y = platform.y - this.height;
				velocityY_ = 0.f;
				falling_ = false;
			}
		}
	}
	
}
