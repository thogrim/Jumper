/**
 * Reprezentuje platformy w grze
 * 
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class Platform extends GameObject{
	
	/**
	 * Wskazuje aktualny stan platformy
	 */
	private enum PlatformState{
		/**
		 * Platforma oddzia³uje z graczem
		 */
		ALIVE,
		/**
		 * Na platformie stoi gracz.
		 */
		STEPPED_ON,
		/**
		 * Platforma zosta³a znisczona
		 */
		DEAD
	}
	
	/**
	 * Aktualny stan platformy
	 */
	private PlatformState state_;
	
	/**
	 * Konstruuje platformê na pozycji x, y
	 * 
	 * @param xPos - Pozycja x(w kafelkach)
	 * @param yPos - Pozycja y(w kafelkach)
	 */
	public Platform(int xPos, int yPos) {
		super(Textures.PLATFORM_TEXTURE,xPos,yPos);
		state_ = PlatformState.ALIVE;
	}
	
	/**
	 * Zwraca true jeœli platforma nie zosta³a zniszczona.
	 * 
	 * @return True jeœli platforma nie zosta³a zniszczona, false w przeciwnym wypadku
	 */
	public boolean isAlive(){
		return state_ != PlatformState.DEAD;
	}
	
	/**
	 * Ustawia stan platformy na STEPPED_ON
	 */
	public void stepOn(){
		state_ = PlatformState.STEPPED_ON;
	}
	
	/**
	 * Zwraca true jeœli na platformie znajduje siê gracz
	 * 
	 * @return True jeœli na platformie znajduje siê gracz, false w przeciwnym wypadku
	 */
	public boolean steppedOn(){
		return state_ == PlatformState.STEPPED_ON;
	}
	
	/**
	 * Niszczy platformê
	 */
	public void destroy() {
		state_ = PlatformState.DEAD;
		texture_ = null;
	}
	
	/**
	 * Resetuje platformê do pocz¹tkowego stanu
	 */
	public void reset(){
		state_ = PlatformState.ALIVE;
		texture_ = Textures.PLATFORM_TEXTURE;
	}
}
