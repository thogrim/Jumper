/**
 * Represents a player character
 *
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class Player extends GameObject{
	
	/**
	 * Stan poruszania si� gracza w osi X
	 */
	private enum PlayerState{
		/**
		 * Gracz stoi w miejscu
		 */
		STANDING,
		/**
		 * Gracz porusza si� w prawo
		 */
		MOVING_RIGHT,
		/**
		 * Gracz porusza si� w lewo
		 */
		MOVING_LEFT,
	};
	
	/**
	 * Aktualny stan poruszania si� w osi X
	 */
	private PlayerState state_;
	
	/**
	 * Sta�a wskazuj�ca, �e �aden klawisz ruchu nie jest wci�ni�ty
	 */
	private static final int STANDING = 0;
	
	/**
	 * Sta�a wskazuj�ca, �e tylko klawisz ruchu w prawo jest wci�ni�ty
	 */
	private static final int MOVE_RIGHT_PRESSED = 1;
	
	/**
	 * Sta�a wskazuj�ca, �e tylko klawisz ruchu w lewo jest wci�ni�ty
	 */
	private static final int MOVE_LEFT_PRESSED = 2;
	
	/**
	 * Informacja o wci�ni�tych klawiszach ruchu
	 */
	private int moveKeysState_;
	
	/**
	 * Wskazuje, czy gracz spada
	 */
	private boolean falling_;
	
	/**
	 * Pr�dko�� gracza w osi X(w pikselach na sekund�)
	 */
	private final static float VELOCITY_X = 270.f;
	
	/**
	 * Grawitacja �wiata gry
	 */
	private float worldGravity_;
	
	/**
	 * Pr�dko�� gracza w osi Y(w pikselach na sekund�)
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
	 * Konstruuje posta� gracza na pozycji x, y
	 * 
	 * @param xPos - Pozycja x(w kafelkach)
	 * @param yPos - Pozycja y(w kafelkach)
	 * @param worldGravity - grawitacja �wiata gry
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
	 * Resetuje pozycj� gracza
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
	 * Informuje o wci�ni�ciu klawisza poruszania si� w prawo
	 */
	public void startMovingRight(){
		moveKeysState_ |= MOVE_RIGHT_PRESSED;
		resolveMoveState();
	}
	
	/**
	 * Informuje o wci�ni�ciu klawisza poruszania si� w lewo
	 */
	public void startMovingLeft(){
		moveKeysState_ |= MOVE_LEFT_PRESSED;
		resolveMoveState();
	}
	
	/**
	 * Informuje o puszczeniu klawisza poruszania si� w prawo
	 */
	public void stopMovingRight() {
		moveKeysState_ &= ~MOVE_RIGHT_PRESSED;
		resolveMoveState();
	}
	
	/**
	 * Informuje o puszczeniu klawisza poruszania si� w lewo
	 */
	public void stopMovingLeft(){
		moveKeysState_ &= ~MOVE_LEFT_PRESSED;
		resolveMoveState();
	}
	
	/**
	 * Ustawia nowy stan poruszania si� gracza
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
	 * Ustawia flag� wskazuj�c� czy gracz spada
	 * 
	 * @param falling - True je�li gracz spada, false w przeciwnym wypdaku
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
	 * Aktualizuje pozycj� gracza
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
	 * Obs�uguje zdarzenie kolizji gracza z platform�
	 * 
	 * @param platform - Platforma, z kt�r� dosz�o do kolzji
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
