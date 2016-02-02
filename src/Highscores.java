import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Reprezentuje stan gry, w którym gracz przegl¹da najlepsze wyniki
 * 
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class Highscores extends GameState{
	
	/**
	 * Konstruuje nowy stan przegl¹dania najlepszych wyników
	 * 
	 * @param jumper - Referencja na g³ówne okno gry
	 */
	public Highscores(Jumper jumper) {
		super(jumper);
		setSize(new Dimension(200,400));
		setVisible(true);
		setLayout(new BorderLayout());
		
		JLabel chooseLevelLabel = new JLabel("Choose level:",JLabel.CENTER);
		
		JPanel levelsPanel = new JPanel();
		GridLayout layout = new GridLayout(Config.NUM_OF_LEVELS,1);
		layout.setVgap(5);
		levelsPanel.setLayout(layout);
		
		for(int i=0; i<Config.NUM_OF_LEVELS; ++i){
			JButton levelButton = new JButton(Config.getLevelName(i));
			int id = i;
			levelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(jumper, JumperClient.getHighscores(id), Config.getLevelName(id), JOptionPane.PLAIN_MESSAGE);
				}
			});
			levelsPanel.add(levelButton);
		}
		
		//back button
		JPanel backPanel = new JPanel();
		JButton backButton = new JButton("Back");
		backButton.setPreferredSize(new Dimension(80,25));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jumper.setGameState(new MainMenu(jumper));
			}
		});
		backPanel.add(backButton);
		
		add(chooseLevelLabel, BorderLayout.NORTH);
		add(levelsPanel, BorderLayout.CENTER);
		add(backPanel, BorderLayout.SOUTH);
	}
	
}
