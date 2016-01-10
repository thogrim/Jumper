import javax.swing.JPanel;

/**
 * Abstract class that represents game state
 *
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public abstract class GameState extends JPanel{ 
	
	/**
	 * Reference to Jumper application
	 */
	protected Jumper jumper_;
	
	public GameState(Jumper jumper){
		super();
		jumper_ = jumper;
	}
	
	public void onFrameResize(int newWidth, int newHeight){
		jumper_.centerState();
	}
}
