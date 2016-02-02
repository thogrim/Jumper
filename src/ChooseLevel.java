import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Reprezentuje stan gry, w którym gracz wybiera poziom
 *
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class ChooseLevel extends GameState{

	/**
	 * Konstruuje nowy stan wyboru poziomu
	 * 
	 * @param jumper - Referencja na g³ówne okno gry
	 */
	public ChooseLevel(Jumper jumper){
		super(jumper);
		//setting panel properties
		setSize(200,400);
		setVisible(true);
		setLayout(new BorderLayout());
		Profile profile = jumper.getCurrentProfile();
		
		//profile name label
		JLabel profileName = new JLabel("Current Profile: "+profile.getName(),JLabel.CENTER);
		
		//Level label
		JLabel levelLabel = new JLabel("Level",JLabel.CENTER);
		
		//Best score label
		JLabel bestScoreLabel = new JLabel("Best Score",JLabel.CENTER);
		
		//level buttons
		JButton[] levelButtons = new JButton[Config.NUM_OF_LEVELS];
		for(int i = 0; i < Config.NUM_OF_LEVELS; ++i){
			int levelID = i;
			levelButtons[i] = new JButton(Config.getLevelName(i));
			levelButtons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jumper.setGameState(new Gameplay(jumper,levelID));
				}
			});
			if(i>profile.getCompletedLevels())
				levelButtons[i].setEnabled(false);
		}
		
		//best scores labels
		JLabel[] bestScoresLabels = new JLabel[Config.NUM_OF_LEVELS];
		for(int i = 0; i < Config.NUM_OF_LEVELS; ++i){
			bestScoresLabels[i] = new JLabel(Integer.toString(profile.getBestScores()[i]),JLabel.CENTER);
		}
		
		//back button
		JButton backButton = new JButton("Back");
		backButton.setPreferredSize(new Dimension(80, 25));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jumper.setGameState(new MainMenu(jumper));
			}
		});
		
		//panel for level buttons and best scores
		JPanel levelButtonsScoresPanel = new JPanel();
		GridLayout layout = new GridLayout(Config.NUM_OF_LEVELS+1,2);
		layout.setVgap(5);
		levelButtonsScoresPanel.setLayout(layout);
		levelButtonsScoresPanel.add(levelLabel);
		levelButtonsScoresPanel.add(bestScoreLabel);
		for(int i = 0; i<Config.NUM_OF_LEVELS; ++i ){
			levelButtonsScoresPanel.add(levelButtons[i]);
			levelButtonsScoresPanel.add(bestScoresLabels[i]);
		}
		
		//back Panel
		JPanel backPanel = new JPanel();
		backPanel.add(backButton);
		
		//south panel
		JPanel southPanel = new JPanel();
		southPanel.add(backPanel,BorderLayout.EAST);
		
		//adding all panels
		add(profileName,BorderLayout.NORTH);
		add(levelButtonsScoresPanel,BorderLayout.CENTER);
		add(southPanel,BorderLayout.SOUTH);
	}

}
