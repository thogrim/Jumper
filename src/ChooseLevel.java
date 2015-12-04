import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * This class represents Choose Level menu
 *
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class ChooseLevel extends GameState{

	public ChooseLevel(Jumper jumper){
		super(jumper);
		//setting panel properties
		setSize(300,600);
		setVisible(true);
		//GridBagLayout layout = new GridBagLayout();
		//layout.setVgap(10);
		//setLayout(layout);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);
		
		Profile profile = jumper.getCurrentProfile();
		
		//JLabel showing currently chosen profile
		JLabel profileName = new JLabel("       Current Profile: "+profile.getProfileName()+"       ",JLabel.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(profileName, gbc);
		
		//JLabels "Level" and "Best Score"
		JLabel level = new JLabel("Level",JLabel.CENTER);
		JLabel bestScore = new JLabel("Best Score",JLabel.CENTER);
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		add(level, gbc);
		gbc.gridx = 1;
		add(bestScore, gbc);
		
		//Level buttons and best scores labels
		String[] levelNames = new File(Config.LEVELS_PATH).list();
		for(int i = 0; i < Config.NUMBER_OF_LEVELS; i++){
			gbc.gridx = 0;
			gbc.gridy = i+2;
			JButton levelButton = new JButton(levelNames[i].substring(0, levelNames[i].indexOf(".")));
			levelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jumper.setGameState(new Gameplay(jumper,e.getActionCommand()));
				}
			});
			add(levelButton, gbc);
			gbc.gridx = 1;
			JLabel score = new JLabel(Integer.toString(profile.getBestScores()[i]),JLabel.CENTER);
			add(score, gbc);
		}
		
		//Back button
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jumper_.setGameState(new MainMenu(jumper_));
			}
		});
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = Config.NUMBER_OF_LEVELS+2;
		add(backButton, gbc);
	}

}
