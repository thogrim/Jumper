import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * This class represents main menu panel 
 *
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class MainMenu extends GameState{
	
	/**
	 * Main menu constructor
	 * 
	 * @param jumper Reference to Jumper application
	 */
	public MainMenu(Jumper jumper){
		super(jumper);
		setSize(new Dimension(200,400));
		setVisible(true);
		GridLayout layout = new GridLayout(7,1);
		layout.setVgap(10);
		setLayout(layout);
		
		//JLabel showing currently chosen profile
		String profileName;
		if(jumper.getCurrentProfileIndex() == -1)
			profileName = "NONE";
		else
			profileName = jumper.getCurrentProfile().getProfileName(); 
		JLabel chosenProfile = new JLabel("Current Profile: "+profileName,JLabel.CENTER);
		
		//creating buttons
		JButton playButton_ = new JButton("Play");
		JButton chooseProfileButton_ = new JButton("Choose Profile");
		JButton instructionsButton_ = new JButton("Instructions");
		JButton highScoresButton_ = new JButton("High Scores");
		JButton replaysButton_ = new JButton("Replays");
		JButton quitButton_ = new JButton("Quit");

		//adding action listeners
		playButton_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Choose profile first if none is chosen 
				if(jumper_.getCurrentProfile()==null)
					jumper_.setGameState(new ChooseProfile(jumper_,true));
				else
					jumper_.setGameState(new ChooseLevel(jumper_));
			}
		});
		chooseProfileButton_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jumper_.setGameState(new ChooseProfile(jumper_,false));
			}
		});
		//TODO
//		instructionsButton_.addActionListener(this);
//		highScoresButton_.addActionListener(this);
//		replaysButton_.addActionListener(this);
		instructionsButton_.setEnabled(false);
		highScoresButton_.setEnabled(false);
		replaysButton_.setEnabled(false);
		
		quitButton_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		add(chosenProfile);
		add(playButton_);
		add(chooseProfileButton_);
		add(instructionsButton_);
		add(highScoresButton_);
		add(replaysButton_);
		add(quitButton_);
	}
	
//	public Dimension getPreferredSize(){
//		return new Dimension(400,500);
//	}
}
