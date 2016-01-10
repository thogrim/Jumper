import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This class represents gameplay state
 *
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class Gameplay extends GameState{
	
	/**
	 * Width of options panel displayed in the right side of window
	 */
	private static final int OPTIONS_PANEL_WIDTH = 200;
	
	/**
	 * Panel with options like pause the game, back to menu,
	 * and with information about level progress 
	 */
	private JPanel optionsPanel_;
	
	/**
	 * Game's world
	 */
	private World world_;
	
	/**
	 * Resize factor in X axis for drawing world
	 */
	private static double scaleX;
	
	/**
	 * Resize factor in Y axis for drawing world
	 */
	private static double scaleY;
	
	/**
	 * Label that shows number of alive platforms 
	 */
	private JLabel platformsNumberLabel_;
	
	/**
	 * Label that shows number of player lifes
	 */
	private JLabel playerLifesNumberLabel_;
	
	/**
	 * 
	 * @param jumper
	 * @param levelName
	 */
	public Gameplay(Jumper jumper, String levelName) {
		//setting window properties
		super(jumper);
		jumper.getContentPane().setPreferredSize(Config.getGameplayWindowSize());
		jumper.pack();
		setSize(Config.getGameplayWindowSize());
		setLayout(null);
		
		//Options panel 
		optionsPanel_ = new JPanel();
		optionsPanel_.setBounds(getWidth()-OPTIONS_PANEL_WIDTH, 0, OPTIONS_PANEL_WIDTH, getHeight());
		//Back to menu button
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jumper.getContentPane().setPreferredSize(Config.getWindowSize());
				jumper.pack();
				jumper.setGameState(new ChooseLevel(jumper));
			}
		});
		backButton.setFocusable(false);
		optionsPanel_.add(backButton);
		
		//Pause Button
		JButton pauseButton = new JButton("Unpause");
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(world_.paused()){
					pauseButton.setText("Pause");
					world_.unpause();
				}
				else{
					pauseButton.setText("Unpause");
					world_.pause();
				}
			}
		});
		pauseButton.setFocusable(false);
		optionsPanel_.add(pauseButton);	
		
		//"Platforms" label
		JLabel platforms = new JLabel("Platforms:");
		optionsPanel_.add(platforms);
		
		//Platforms number label
		platformsNumberLabel_ = new JLabel();
		optionsPanel_.add(platformsNumberLabel_);
		
		//player lifes label
		playerLifesNumberLabel_ = new JLabel();
		optionsPanel_.add(playerLifesNumberLabel_);
		
		//World
		world_ = new World(this,levelName);
		scaleX = 1;
		scaleY = 1;
		add(optionsPanel_);
		world_.start();
	}
	
	public static double getScaleX(){
		return scaleX;
	}
	
	public static double getScaleY(){
		return scaleY;
	}
	
	public void onFrameResize(int newWidth, int newHeight){
		setSize(newWidth,newHeight);
		optionsPanel_.setBounds(newWidth-OPTIONS_PANEL_WIDTH, 0, OPTIONS_PANEL_WIDTH, newHeight);
		scaleX = (double) (newWidth-OPTIONS_PANEL_WIDTH)/(double) World.WORLD_WIDTH;
		scaleY = (double) newHeight/(double) World.WORLD_HEIGHT;
	}
	
	/**
	 * 
	 * @param alivePlatforms
	 * @param allPlatforms
	 */
	public void updatePlatformsLabel(int alivePlatforms, int allPlatforms){
		platformsNumberLabel_.setText(alivePlatforms+" / "+allPlatforms);
	}
	
	/**
	 * 
	 * @param lifes
	 */
	public void updatePlayerLifesLabel(int lifes){
		playerLifesNumberLabel_.setText("Lifes: "+lifes);
	}
	
	/**
	 * 
	 */
	public void onLevelComplete() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jumper_.getContentPane().setPreferredSize(Config.getWindowSize());
				jumper_.pack();
				jumper_.setGameState(new ChooseLevel(jumper_));	
			}
		});
	}
	
	/**
	 * 
	 */
	public void onPlayerDeath(){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jumper_.getContentPane().setPreferredSize(Config.getWindowSize());
				jumper_.pack();
				jumper_.setGameState(new ChooseLevel(jumper_));	
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		world_.draw(g);
	}
}
