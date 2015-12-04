import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This class represents gameplay state
 *
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class Gameplay extends GameState/* implements KeyListener*/{

//	/**
//	 * Size of game's world in pixels
//	 */
//	public static final Dimension WORLD_SIZE = new Dimension(800,600);
//	
//	/**
//	 * Size of tiles that game's world is divided into(in pixels)
//	 */
//	public static final int TILE_SIZE = 40;
	
	/**
	 * Width of options panel displayed in the right side of window
	 */
	private static final int OPTIONS_PANEL_WIDTH = 200;
	
//	/**
//	 * Number of parts that level has
//	 */
//	private static final int NUMBER_OF_LEVEL_PARTS = 2;
//	
//	/**
//	 * Value of pixel color that corresponds to the player's position
//	 */
//	private static final int PLAYER_PIXEL_COLOR = 0xff00ff00;
//	
//	/**
//	 * Value of pixel color that corresponds to the platform's position
//	 */
//	private static final int PLATFORM_PIXEL_COLOR = 0xffff0000;
//	
//	/**
//	 * Value of pixel color that corresponds to the bonus' position
//	 */
//	private static final int BONUS_PIXEL_COLOR = 0xffffff00;
//	
//	/**
//	 * Player'a character
//	 */
//	private Player player_;
//	
//	/**
//	 * List of platforms
//	 */
//	private ArrayList<ArrayList<Platform>> platforms_; 
//	
//	/**
//	 * List of bonuses
//	 */
//	private ArrayList<ArrayList<Bonus>> bonuses_;
//	
//	/**
//	 * Current part of level
//	 */
//	private int currentPart_;
	
	/**
	 * Panel with options like pause the game, back to menu,
	 * and with information about level progress 
	 */
	private JPanel optionsPanel_;
	
	private World world_;
	
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
		
		
		//World
		world_ = new World(this,levelName);
		//Switch current part button
//		JButton switchButton = new JButton("Switch");
//		switchButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if(++currentPart_ >= NUMBER_OF_LEVEL_PARTS)
//					currentPart_ = 0;
//				repaint();
//			}
//		});
//		optionsPanel_.add(switchButton);
		
		add(optionsPanel_);
		world_.start();
//		platforms_ = new ArrayList<ArrayList<Platform>>(NUMBER_OF_LEVEL_PARTS);
//		bonuses_ = new ArrayList<ArrayList<Bonus>>(NUMBER_OF_LEVEL_PARTS);
//		readLevel(levelName);
//		setKeyBindings();
//		currentPart_ = 0;
	}

//	private void readLevel(String levelName){
//		try {
//			BufferedImage levelImg = ImageIO.read(new File(Config.LEVELS_PATH+"/"+levelName+".png"));
//			int worldWidth = levelImg.getWidth();
//			int worldHeight = levelImg.getHeight()/NUMBER_OF_LEVEL_PARTS;
//			for(int levelPart = 0; levelPart < NUMBER_OF_LEVEL_PARTS; ++levelPart){
//				ArrayList<Platform> levelPartPlatforms = new ArrayList<Platform>();
//				ArrayList<Bonus> levelPartBonuses = new ArrayList<Bonus>();
//				for(int j = levelPart*worldHeight; j < (levelPart+1)*worldHeight; ++j){
//					for(int i = 0; i < worldWidth; ++i){
//						int pixel = levelImg.getRGB(i,j);
//						switch(pixel){
//						case PLAYER_PIXEL_COLOR:
//							player_ = new Player(i,j);
//							break;
//						case PLATFORM_PIXEL_COLOR:
//							levelPartPlatforms.add(new Platform(i,j%worldHeight));
//							break;
//						case BONUS_PIXEL_COLOR:
//							levelPartBonuses.add(new Bonus(i,j%worldHeight));
//							break;
//						}
//					}
//				}
//				platforms_.add(levelPartPlatforms);
//				bonuses_.add(levelPartBonuses);
//			}
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}	
//	
//	private void setKeyBindings() {
//		InputMap input = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//		ActionMap actions = getActionMap();
//		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0), "playerMoveRight");
//		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0), "playerMoveLeft");
//		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0), "playerMoveRight");
//		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_A,0), "playerMoveLeft");
//		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0,true), "playerStop");
//		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0,true), "playerStop");
//		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0,true), "playerStop");
//		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_A,0,true), "playerStop");
//		actions.put("playerMoveRight",new AbstractAction() {
//			public void actionPerformed(ActionEvent e) {
//				player_.startMovingRight();
//			}
//		});
//		actions.put("playerMoveLeft",new AbstractAction() {
//			public void actionPerformed(ActionEvent e) {
//				player_.startMovingLeft();
//			}
//		});
//		actions.put("playerStop",new AbstractAction() {
//			public void actionPerformed(ActionEvent e) {
//				player_.stopMoving();
//			}
//		});
//	}
	
//	public void keyPressed(KeyEvent arg0) {
//		player_.move(50.f,0.f);
//		System.out.println("aaa");
//		this.repaint();
//	}
//	
//	public void keyReleased(KeyEvent arg0) {
//		
//	}
//	
//	public void keyTyped(KeyEvent arg0) {
//		
//	}
	
	/**
	 * 
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//System.out.println("paint!");
		//draw world background
		g.setColor(Color.CYAN);
		g.fillRect(0,0,800,600);
		//draw world
//		player_.draw(g);
//		for(Platform p : platforms_.get(currentPart_)){
//			p.draw(g);
//		}
//		for(Bonus b : bonuses_.get(currentPart_)){
//			b.draw(g);
//		}
		world_.draw(g);
	}
}
