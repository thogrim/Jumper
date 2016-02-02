import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Reprezentuje stan gry, w którym gracz wybiera profil
 *
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class ChooseProfile extends GameState{
	
	/**
	 * Przyciski do wyboru profilu
	 */
	private JRadioButton[] profileButtons_;
	
	/**
	 * Konstruuje nowy stan wyboru profilu
	 * 
	 * @param jumper - Referencja na g³ówne okno gry
	 * @param playButtonPressed - Wskazuje czy przejœcie do tego stanu odby³o siê 
	 * 			poprzez wciœniêcie przycisku "Play" z menu g³ównego
	 */
	public ChooseProfile(Jumper jumper, boolean playButtonPressed) {
		//setting Panel properties
		super(jumper);
		setSize(new Dimension(200,400));
		setVisible(true);
		
		//choose profile label
		JLabel chooseLabel = new JLabel("Choose profile:",JLabel.CENTER);
		
		//panel for profiles
		JPanel profilesPanel = new JPanel();
		GridLayout layout = new GridLayout(Config.NUM_OF_PROFILES+1,1);
		layout.setVgap(10);
		profilesPanel.setLayout(layout);
		
		//"OK" button
		JButton okButton = new JButton("OK");
		okButton.setPreferredSize(new Dimension(80,25));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//determine which profile is chosen
				int i;
				for(i = 0; i < profileButtons_.length; ++i){
					if(profileButtons_[i].isSelected()){
						jumper_.setCurrentProfile(i);
					}
				}
				if(playButtonPressed)
					jumper_.setGameState(new ChooseLevel(jumper_));
				else
					jumper_.setGameState(new MainMenu(jumper_));
			}
		});
		okButton.setEnabled(false);
		
		//Profiles Radio Buttons
		ButtonGroup group = new ButtonGroup();
		profileButtons_ = new JRadioButton[Config.NUM_OF_PROFILES];
		//create "Empty" radio buttons
		for(int i = 0; i < Config.NUM_OF_PROFILES; ++i){
			profileButtons_[i] = new JRadioButton("Empty");
			profileButtons_[i].setEnabled(false);
			profileButtons_[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					okButton.setEnabled(true);
				}
			});
			group.add(profileButtons_[i]);
			profilesPanel.add(profileButtons_[i]);
		}
		//create radio buttons for actual profiles
		for(int i = 0; i < jumper.getProfileCount(); ++i){
			profileButtons_[i].setText(jumper.getProfiles()[i].getName());
			profileButtons_[i].setEnabled(true);
		}
		if(jumper.getCurrentProfileIndex()!=-1)
			profileButtons_[jumper.getCurrentProfileIndex()].setSelected(true);
		
		//"Create" button
		JButton createButton = new JButton("Create new profile");
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String profileName = JOptionPane.showInputDialog(jumper_, "Type your profile's name", "Create new profile", JOptionPane.QUESTION_MESSAGE);
				try {
					//create new profile
						if(profileName!=null){
						Profile newProfile = Profile.create(profileName);
						Profile[] profiles = jumper_.getProfiles();
						int profileIndex = jumper_.getProfileCount();
						profiles[profileIndex] = newProfile;
						//select new profile
						profileButtons_[profileIndex].setEnabled(true);
						profileButtons_[profileIndex].setText(profileName);
						profileButtons_[profileIndex].setSelected(true);
						okButton.setEnabled(true);
						if(profileIndex==jumper_.getProfileCount())
							createButton.setEnabled(false);
					}
				} catch (IOException e) {
					JOptionPane.showMessageDialog(ChooseProfile.this, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		//disable "Create" button if there is no space for new profiles 
		if(jumper.getProfileCount()==Config.NUM_OF_PROFILES)
			createButton.setEnabled(false);
		profilesPanel.add(createButton);
		
		//"Back" button
		JButton backButton = new JButton("Back");
		backButton.setPreferredSize(new Dimension(80,25));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jumper_.setGameState(new MainMenu(jumper_));
			}
		});
		
		//panel for other buttons
		JPanel otherPanel = new JPanel();
		otherPanel.add(okButton);
		otherPanel.add(backButton);
		
		add(chooseLabel,BorderLayout.NORTH);
		add(profilesPanel,BorderLayout.CENTER);
		add(otherPanel,BorderLayout.SOUTH);
	}

}
