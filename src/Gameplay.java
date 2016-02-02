import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Reprezentuje stan gry, w kt�rym odbywa si� rozgrywka
 *
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class Gameplay extends GameState{
	
	/**
	 * Szeroko�� bocznego panelu
	 */
	private static final int OPTIONS_PANEL_WIDTH = 200;
	
	/**
	 * Panel pokazuj�cy informacj� o stanie rozgrywki
	 */
	private JPanel optionsPanel_;
	
	/**
	 * Przycisk do zatrzymywania rozgrywki
	 */
	private JButton pauseButton_;
	
	/**
	 * �wiat gry
	 */ 
	private World world_;
	
	/**
	 * Powi�kszenie oryginalnego rozmiaru okna w osi X
	 */
	private static double scaleX;
	
	/**
	 * Powi�kszenie oryginalnego rozmiaru okna w osi Y
	 */
	private static double scaleY;
	
	/**
	 * Pokazuje aktualn� liczb� platform
	 */
	private JLabel platformsNumberLabel_;
	
	/**
	 * Pokazuje liczb� �y� gracza
	 */
	private JLabel playerLifesNumberLabel_;
	
	/**
	 * Pokazuje czas trwania rozgrywki
	 */
	private JLabel timePassedLabel_;
	
	/**
	 * Pokazuje aktualn� cz�� poziomu
	 */
	private JLabel currentPartLabel_;
	
	/**
	 * Identyfikator poziomu
	 */
	private int levelId_;
	
	/**
	 * Konstruuje nowy stan rozgrywki
	 * 
	 * @param jumper - Referencja na g��wne okno gry
	 * @param id - Identyfikator poziomu
	 */
	public Gameplay(Jumper jumper, int id) {
		//setting window properties
		super(jumper);
		jumper.getContentPane().setPreferredSize(Config.GAMEPLAY_WINDOW_SIZE);
		jumper.pack();
		setSize(Config.GAMEPLAY_WINDOW_SIZE);
		setLayout(null);
		levelId_ = id;
		
		//Options panel 
		optionsPanel_ = new JPanel();
		optionsPanel_.setBounds(getWidth()-OPTIONS_PANEL_WIDTH, 0, OPTIONS_PANEL_WIDTH, getHeight());
		//Back to menu button
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jumper.getContentPane().setPreferredSize(Config.WINDOW_SIZE);
				jumper.pack();
				jumper.setGameState(new ChooseLevel(jumper));
			}
		});
		backButton.setFocusable(false);
		optionsPanel_.add(backButton);
		
		//Pause Button
		pauseButton_ = new JButton("Unpause");
		pauseButton_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pauseGame();
			}
		});
		pauseButton_.setFocusable(false);
		optionsPanel_.add(pauseButton_);	
		
		//Platforms number label
		platformsNumberLabel_ = new JLabel();
		optionsPanel_.add(platformsNumberLabel_);
		
		//player lifes label
		playerLifesNumberLabel_ = new JLabel();
		optionsPanel_.add(playerLifesNumberLabel_);
		
		//timer label
		timePassedLabel_ = new JLabel("0.0 s");
		optionsPanel_.add(timePassedLabel_);
		
		//current part label
		currentPartLabel_ = new JLabel("Part: 1 / 5");
		optionsPanel_.add(currentPartLabel_);
		
		//World
		world_ = new World(this,id);
		scaleX = 1;
		scaleY = 1;
		add(optionsPanel_);
		world_.start();
	}
	
	/**
	 * Zwraca powi�kszenie oryginalnego rozmiaru okna w osi X
	 * 
	 * @return Powi�kszenie oryginalnego rozmiaru okna w osi X
	 */
	public static double getScaleX(){
		return scaleX;
	}
	
	/**
	 * Zwraca powi�kszenie oryginalnego rozmiaru okna w osi Y
	 * 
	 * @return Powi�kszenie oryginalnego rozmiaru okna w osi Y
	 */
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
	 * Aktualizuje ilo�� platform w bocznym panelu 
	 * 
	 * @param alivePlatforms - Liczba pozosta�ych platform
	 * @param allPlatforms - Liczba wszystkich platform w danej cz�ci poziomu
	 */
	public void updatePlatformsLabel(int alivePlatforms, int allPlatforms){
		platformsNumberLabel_.setText("Platforms: "+alivePlatforms+" / "+allPlatforms);
	}
	
	/**
	 * Aktualizuje ilo�� �y� gracza w bocznym panelu
	 * 
	 * @param lifes - Liczba �y� gracza
	 */
	public void updatePlayerLifesLabel(int lifes){
		playerLifesNumberLabel_.setText("Lifes: "+lifes);
	}
	
	/**
	 * Aktualizuje licznik czasu w bocznym panelu
	 * 
	 * @param timer - Czas rozgrywki
	 */
	public void updateTimer(double timer){
		timer = Math.round(timer/10.d)/100.d;
		timePassedLabel_.setText("Time: "+timer+" s");
	}
	
	/**
	 * Aktualizuje numer cz�ci poziomu
	 * 
	 * @param part - numer cz�ci poziomu
	 */
	public void updatePartLabel(int part){
		currentPartLabel_.setText("Part: "+part+" / "+World.NUM_OF_LEVEL_PARTS);
	}
	
	/**
	 * Zatrzymuje/wznawia rozgrywk�
	 */
	public void pauseGame(){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(world_.paused()){
					pauseButton_.setText("Pause");
					world_.unpause();
				}
				else{
					pauseButton_.setText("Unpause");
					world_.pause();
				}
			}
		});
	}
	
	/**
	 * Obs�uguje zdarzenie uko�czenia poziomu przez gracza
	 * 
	 * @param time - Czas rozgrywki
	 * @param points - Liczba punkt�w, kt�r� gracz uzyska�
	 */
	public void onLevelComplete(double time, int points) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(null, "Level Complete!\nTime: "+Math.round(time/10.d)/100.d+"\nPoints: "+points);
				Profile profile = jumper_.getCurrentProfile();
				if(profile.getCompletedLevels() == levelId_)
					profile.unlockNextLevel();
				if(profile.getBestScores()[levelId_] < points)
					profile.setNewHighscore(points,levelId_);
				profile.saveProfile();
				JumperClient.processScore(levelId_, points, jumper_.getCurrentProfile().getName());
				jumper_.getContentPane().setPreferredSize(Config.WINDOW_SIZE);
				jumper_.pack();
				jumper_.setGameState(new ChooseLevel(jumper_));
			}
		});
	}
	
	/**
	 * Obs�uguje zdarzenie utraty wszystkich �y� przez gracza
	 */
	public void onPlayerDeath(){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				int option = JOptionPane.showConfirmDialog(jumper_,"You died. Would you like to start again","Game over!",JOptionPane.YES_NO_OPTION);
				if(option == JOptionPane.YES_OPTION){
					world_.reset();
					pauseButton_.setText("Unpause");
				}
				else{
					jumper_.getContentPane().setPreferredSize(Config.WINDOW_SIZE);
					jumper_.pack();
					jumper_.setGameState(new ChooseLevel(jumper_));
				}
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		world_.draw(g);
	}
}
