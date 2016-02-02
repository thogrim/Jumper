/**
 * Reprezentuje bonusy punktowe w grze
 * 
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class Bonus extends GameObject{
	
	/**
	 * Iloœæ punktów za zdobycie bonusu
	 */
	public static final int SCORE = 1000;
	
	/**
	 * Wskazuje, czy bonus zosta³ zdobyty 
	 */
	private boolean collected_;

	/**
	 * Konstruuje bonus na pozycji x, y
	 * 
	 * @param xPos - Pozycja x(w kafelkach)
	 * @param yPos - Pozycja y(w kafelkach)
	 */
	public Bonus(int xPos, int yPos) {
		super(Textures.BONUS_TEXTURE, xPos, yPos);
		collected_ = false;
	}
	
	/**
	 * Zwraca informacjê o zdobyciu bonusu
	 * 
	 * @return Informacja o zdobyciu bonusu
	 */
	public boolean collected(){
		return collected_;
	}
	
	/**
	 * Ustawia bonus w stan zdobyty
	 */
	public void collect(){
		collected_= true;
		texture_ = null;
	}
	
	/**
	 * Resetuje bonus do pocz¹tkowego stanu
	 */
	public void reset(){
		collected_ = false;
		texture_ = Textures.BONUS_TEXTURE;
	}
}
