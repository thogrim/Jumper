import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This is the main class for Jumper application
 * 
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class Jumper extends JFrame{
	
	/**
	 * Current game state
	 */
	private GameState state_;
	
	/**
	 * Table that stores all profiles
	 */
	private Profile[] profiles_ = new Profile[Config.NUMBER_OF_PROFILES];
	
	/**
	 * Index of currently chosen profile in profiles_ table
	 */
	private int currentProfile_;
	
	/**
	 * Default constructor
	 */
	public Jumper(){
		super("Jumper!");
		//setting window properties
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		getContentPane().setLayout(null);
		getContentPane().setPreferredSize(Config.getWindowSize());
		pack();
		
		currentProfile_ = -1;
		readProfiles();
		state_ = new MainMenu(this);
		centerState();
		//System.out.println(state_.getWidth());
		//state_.setBounds((getContentPane().getWidth()-state_.getWidth())/2, (getContentPane().getHeight()-state_.getHeight())/2, state_.getWidth(), state_.getHeight());
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
	
	public int getCurrentProfileIndex(){
		return currentProfile_;
	}
	
	public Profile getCurrentProfile(){
		return currentProfile_ == -1 ? null : profiles_[currentProfile_];
	}
	
	public Profile[] getProfiles(){
		return profiles_;
	}
	
	/**
	 * Returns the number of profiles
	 * 
	 * @return Profile count
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
	 * Sets current profile to the profiles_[i] 
	 * 
	 * @param i index in profiles_ table
	 */
	public void setCurrentProfile(int i){
		currentProfile_ = i;
	}
	
	/**
	 * Sets current state of the game
	 * 
	 * @param state Next game state
	 */
	public void setGameState(GameState state){
		state_.setVisible(false);
		getContentPane().remove(state_);
		state_ = state;
		centerState();
		getContentPane().add(state_);
	}
	
	/**
	 * Sets current state's panel to the center of the window
	 */
	public void centerState(){
		state_.setBounds((getContentPane().getWidth()-state_.getWidth())/2, (getContentPane().getHeight()-state_.getHeight())/2, state_.getWidth(), state_.getHeight());
	}
	
	/**
	 * Pops up "Choose Profile" JDialog. Here you can choose profile
	 * from existing ones or create new 
	 */
//	public void chooseProfile(){
//		if(chooseProfileDialog_==null){
//			chooseProfileDialog_ = new JDialog(this,"aa", true);
//			chooseProfileDialog_.setSize(100, 300);
//		}
//		chooseProfileDialog_.setVisible(true);
//	}
	
	/**
	 * Reads information about profiles
	 */
	private void readProfiles(){
		String[] filepaths = new File(Config.PROFILES_PATH).list();
		int numOfProfiles = filepaths.length;
		if(numOfProfiles > Config.NUMBER_OF_PROFILES)
			numOfProfiles = Config.NUMBER_OF_PROFILES;
		for(int i = 0; i < numOfProfiles; ++i){
			System.out.println("Reading profile from: "+filepaths[i]);
			profiles_[i] = new Profile(filepaths[i]);
		}
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				Config.readConfiguration();
				new Jumper();
			}
		});
	}
}
