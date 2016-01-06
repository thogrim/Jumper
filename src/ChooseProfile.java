import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

/**
 * This class represents panel where you can choose profile
 *
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class ChooseProfile extends GameState{
	
	/**
	 * Buttons that let you choose profile
	 */
	JRadioButton[] profileButtons_;
	
	public ChooseProfile(Jumper jumper, boolean playButtonPressed) {
		//setting Panel properties
		super(jumper);
		setSize(new Dimension(200,300));
		setVisible(true);
		GridLayout layout = new GridLayout(Config.NUMBER_OF_PROFILES+3,1);
		layout.setVgap(10);
		setLayout(layout);
		
		//"OK" button
		JButton okButton = new JButton("OK");
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
				else{
					
					jumper_.setGameState(new MainMenu(jumper_));
				}
			}
		});
		okButton.setEnabled(false);
		
		//Profiles Radio Buttons
		ButtonGroup group = new ButtonGroup();
		profileButtons_ = new JRadioButton[Config.NUMBER_OF_PROFILES];
		//create "Empty" radio buttons
		for(int i = 0; i < Config.NUMBER_OF_PROFILES; ++i){
			profileButtons_[i] = new JRadioButton("Empty");
			profileButtons_[i].setEnabled(false);
			profileButtons_[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					okButton.setEnabled(true);
				}
			});
			group.add(profileButtons_[i]);
			add(profileButtons_[i]);
		}
		//create radio buttons for actual profiles
		for(int i = 0; i < jumper.getProfileCount(); ++i){
			profileButtons_[i].setText(jumper.getProfiles()[i].getProfileName());
			profileButtons_[i].setEnabled(true);
		}
		if(jumper.getCurrentProfileIndex()!=-1)
			profileButtons_[jumper.getCurrentProfileIndex()].setSelected(true);
		
		//"Create" button
		JButton createButton = new JButton("Create");
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String profileName = JOptionPane.showInputDialog(ChooseProfile.this, "Type your profile's name", "Create new profile", JOptionPane.QUESTION_MESSAGE);
				try {
					//create new profile 
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
				} catch (IOException e) {
					JOptionPane.showMessageDialog(ChooseProfile.this, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
					//e.printStackTrace();
				}
				
			}
		});
		//disable "Create" button if there is no space for new profiles 
		if(jumper.getProfileCount()==Config.NUMBER_OF_PROFILES)
			createButton.setEnabled(false);
		
		//"Back" button
		JButton backButton = new JButton("Back to menu");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jumper_.setGameState(new MainMenu(jumper_));
			}
		});
		
		//add buttons
		add(createButton);
		add(okButton);
		add(backButton);
	}

}
