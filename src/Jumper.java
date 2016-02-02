import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * G³ówna klasa reprezentuj¹ca okno aplikacji Jumper
 * 
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class Jumper extends JFrame{
	
	/**
	 * Aktualny stan gry
	 */
	private GameState state_;
	
	/**
	 * Tablica przechowuj¹ca profile
	 */
	private Profile[] profiles_ = new Profile[Config.NUM_OF_PROFILES];
	
	/**
	 * Indeks aktualnie wybranego profilu
	 */
	private int currentProfile_; 
	
	/**
	 * Domyœlny konstruktor
	 */
	public Jumper(){
		super("Jumper!");
		//setting window properties
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		getContentPane().setLayout(null);
		setMinimumSize(new Dimension(300,500));
		getContentPane().setPreferredSize(Config.WINDOW_SIZE);
		pack();
		
		currentProfile_ = -1;
		readProfiles();
		state_ = new MainMenu(this);
		centerState();
		state_.setBounds((getContentPane().getWidth()-state_.getWidth())/2, (getContentPane().getHeight()-state_.getHeight())/2, state_.getWidth(), state_.getHeight());
		getContentPane().add(state_);
		getContentPane().addComponentListener(new ComponentListener() {
			public void componentShown(ComponentEvent e) {
				//do nothing
			}
			public void componentResized(ComponentEvent e) {
				state_.onFrameResize(e.getComponent().getWidth(), e.getComponent().getHeight());
			}
			public void componentMoved(ComponentEvent e) {	
				//do nothing
			}
			public void componentHidden(ComponentEvent e) {
				//do nothing
			}
		});
	}
	
	/**
	 * Zwraca indeks aktualnego profilu
	 * 
	 * @return Indeks aktualnego profilu
	 */
	public int getCurrentProfileIndex(){
		return currentProfile_;
	}
	
	/**
	 * Zwraca aktualnie wybrany profil
	 * 
	 * @return Aktualnie wybrany profil
	 */
	public Profile getCurrentProfile(){
		return currentProfile_ == -1 ? null : profiles_[currentProfile_];
	}
	
	/**
	 * Zwraca wszystkie profile w grze
	 * 
	 * @return Wszystkie profile
	 */
	public Profile[] getProfiles(){
		return profiles_;
	}
	
	/**
	 * Zwraca liczbê profili
	 * 
	 * @return Liczba profili
	 */
	public int getProfileCount(){
		int i = 0;
		for(i = 0; i < profiles_.length; ++i){
			if(profiles_[i]==null)
				break;
		}
		return i;
	}
	
	/**
	 * Ustawia aktualny profil
	 * 
	 * @param i Indeks profilu
	 */
	public void setCurrentProfile(int i){
		currentProfile_ = i;
	}
	
	/**
	 * Ustawia stan gry
	 * 
	 * @param state Nowy stan gry
	 */
	public void setGameState(GameState state){
		state_.setVisible(false);
		getContentPane().remove(state_);
		state_ = state;
		centerState();
		getContentPane().add(state_);
	}
	
	/**
	 * Ustawia panel stanu gry na œrodku okna
	 */
	public void centerState(){
		state_.setBounds((getContentPane().getWidth()-state_.getWidth())/2, (getContentPane().getHeight()-state_.getHeight())/2, state_.getWidth(), state_.getHeight());
	}	
	
	/**
	 * Wczytuje informacje o profilach
	 */
	private void readProfiles(){
		String[] filepaths = new File(Config.PROFILES_PATH).list();
		int numOfProfiles = filepaths.length;
		if(numOfProfiles > Config.NUM_OF_PROFILES)
			numOfProfiles = Config.NUM_OF_PROFILES;
		for(int i = 0; i < numOfProfiles; ++i){
			profiles_[i] = new Profile(filepaths[i]);
		}
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				JumperClient client = new JumperClient();
				Dimension d = client.getToolkit().getScreenSize();
				client.setLocation((int)(d.getWidth()/2-client.getWidth()/2),(int)(d.getHeight()/2-client.getHeight()/2));
			}
		});
	}
}
