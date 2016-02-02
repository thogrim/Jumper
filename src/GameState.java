import javax.swing.JPanel;

/**
 * Reprezentuje abstrakcyjny stan gry
 *
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public abstract class GameState extends JPanel{ 
	
	/**
	 * Referencja na okno g��wne gry
	 */
	protected Jumper jumper_;
	
	/**
	 * Konstruuje nowy stan gry
	 * 
	 * @param jumper - Referencja na okno g��wne gry
	 */
	public GameState(Jumper jumper){
		super();
		jumper_ = jumper;
	}
	
	/**
	 * Obs�uguje zdarzenie zmiany rozmiaru okna aplikacji
	 * 
	 * @param newWidth - Nowa szeroko�� okna
	 * @param newHeight - Nowa d�ugo�� okna
	 */
	public void onFrameResize(int newWidth, int newHeight){
		jumper_.centerState();
	}
}
