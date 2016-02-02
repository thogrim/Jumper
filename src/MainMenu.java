import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Reprezentuje stan gry, w którym gracz jest w  menu g³ównym
 *
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class MainMenu extends GameState{
	
	/**
	 * Konstruuje nowy stan menu g³ównego
	 * 
	 * @param jumper - Referencja na g³ówne okno gry
	 */
	public MainMenu(Jumper jumper){
		super(jumper);
		setSize(new Dimension(200,400));
		setVisible(true);

		setLayout(new BorderLayout());
		
		//JLabel showing currently chosen profile
		String profileName;
		if(jumper.getCurrentProfileIndex() == -1)
			profileName = "NONE";
		else
			profileName = jumper.getCurrentProfile().getName(); 
		
		JLabel chosenProfile = new JLabel("Current Profile: "+profileName,JLabel.CENTER);
		
		//creating buttons
		JButton playButton_ = new JButton("Play");
		JButton chooseProfileButton_ = new JButton("Choose Profile");
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
		
		
		highScoresButton_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jumper_.setGameState(new Highscores(jumper_));
			}
		});
		
		//TODO
//		replaysButton_.addActionListener(this);
		replaysButton_.setEnabled(false);
		
		quitButton_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		//Panel for chosen profile
		JPanel chosenProfilePanel = new JPanel();
		chosenProfilePanel.add(chosenProfile);
		
		//Panel for buttons
		JPanel buttonsPanel = new JPanel();
		GridLayout layout = new GridLayout(8,1);
		layout.setVgap(10);
		buttonsPanel.setLayout(layout);
		buttonsPanel.add(chosenProfile);
		buttonsPanel.add(playButton_);
		buttonsPanel.add(chooseProfileButton_);
		buttonsPanel.add(highScoresButton_);
		buttonsPanel.add(replaysButton_);
		buttonsPanel.add(quitButton_);
		
		//adding panels
		add(buttonsPanel,BorderLayout.CENTER);
		add(chosenProfilePanel,BorderLayout.NORTH);
	}
}
