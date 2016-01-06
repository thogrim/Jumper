import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class World implements Runnable{
	/**
	 * Size of game's world in pixels
	 */
	public static final Dimension WORLD_SIZE = new Dimension(800,600);
	
	/**
	 * Size of tiles that game's world is divided into(in pixels)
	 */
	public static final int TILE_SIZE = 40;
	
	/**
	 * Number of parts that level has
	 */
	private static final int NUMBER_OF_LEVEL_PARTS = 5;
	
	/**
	 * Value of pixel color that corresponds to the player
	 */
	private static final int PLAYER_PIXEL_COLOR = 0xff00ff00;
	
	/**
	 * Value of pixel color that corresponds to the platform
	 */
	private static final int PLATFORM_PIXEL_COLOR = 0xffff0000;
	
	/**
	 * Value of pixel color that corresponds to the bonus
	 */
	private static final int BONUS_PIXEL_COLOR = 0xffffff00;
	
	/**
	 * Time between next updates of the game
	 */
	public static final long UPDATE_DELTA_TIME = 16;
	
	/**
	 * Game play panel
	 */
	Gameplay gamePanel_;
	
	/**
	 * Player's character
	 */
	private Player player_;
	
	/**
	 * List of player spawn positions(in tiles)
	 */
	private int[][] playerSpawnPositions_;
	
	/**
	 * List of platforms
	 */
	private ArrayList<ArrayList<Platform>> platforms_;
	
	/**
	 * List of bonuses
	 */
	private ArrayList<ArrayList<Bonus>> bonuses_;
	
	/**
	 * Number of lifes that player has left
	 */
	private int playerLifes_;
	
	/**
	 * Number of alive platforms
	 */
	private int alivePlatforms_;
	
	/**
	 * Current part of level
	 */
	private int currentPart_;
	
	/**
	 * Indicates if game is paused
	 */
	private boolean paused_;
	
	/**
	 * 
	 */
	private boolean completed_;
	
	/**
	 * 
	 * @param game
	 * @param levelName
	 */
	public World(Gameplay game, String levelName){
		gamePanel_ = game;
		paused_ = true;
		completed_ = false;
		playerSpawnPositions_ = new int [NUMBER_OF_LEVEL_PARTS][2];
		platforms_ = new ArrayList<>(NUMBER_OF_LEVEL_PARTS);
		bonuses_ = new ArrayList<>(NUMBER_OF_LEVEL_PARTS);
		playerLifes_ = Config.NUMBER_OF_PLAYER_LIFES;
		readLevel(levelName);
		gamePanel_.updatePlatformsLabel(alivePlatforms_, platforms_.get(0).size());
		gamePanel_.updatePlayerLifesLabel(playerLifes_);
		setKeyBindings();
	}

	private void setKeyBindings() {
		InputMap input = gamePanel_.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actions = gamePanel_.getActionMap();
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0), "playerMoveRight");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0), "playerMoveLeft");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0), "playerMoveRight");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_A,0), "playerMoveLeft");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0,true), "playerStopMovingRight");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0,true), "playerStopMovingLeft");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0,true), "playerStopMovingRight");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_A,0,true), "playerStopMovingLeft");
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z,0), "playerJump");
		actions.put("playerMoveRight",new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				player_.startMovingRight();
			}
		});
		actions.put("playerMoveLeft",new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				player_.startMovingLeft();
			}
		});
		actions.put("playerStopMovingRight",new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				player_.stopMovingRight();
			}
		});
		actions.put("playerStopMovingLeft",new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				player_.stopMovingLeft();
			}
		});
		actions.put("playerJump",new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				player_.jump();
			}
		});
	}

	private void readLevel(String levelName) {
		try {
			BufferedImage levelImg = ImageIO.read(new File(Config.LEVELS_PATH+"/"+levelName+".png"));
			int worldWidth = levelImg.getWidth();
			int worldHeight = levelImg.getHeight()/NUMBER_OF_LEVEL_PARTS;
			for(int levelPart = 0; levelPart < NUMBER_OF_LEVEL_PARTS; ++levelPart){
				ArrayList<Platform> levelPartPlatforms = new ArrayList<Platform>();
				ArrayList<Bonus> levelPartBonuses = new ArrayList<Bonus>();
				for(int j = levelPart*worldHeight; j < (levelPart+1)*worldHeight; ++j){
					for(int i = 0; i < worldWidth; ++i){
						int pixel = levelImg.getRGB(i,j);
						switch(pixel){
						case PLAYER_PIXEL_COLOR:
							playerSpawnPositions_[levelPart][0] = i;
							playerSpawnPositions_[levelPart][1] = j%worldHeight;
							break;
						case PLATFORM_PIXEL_COLOR:
							levelPartPlatforms.add(new Platform(i,j%worldHeight));
							break;
						case BONUS_PIXEL_COLOR:
							levelPartBonuses.add(new Bonus(i,j%worldHeight));
							break;
						}
					}
				}
				platforms_.add(levelPartPlatforms);
				bonuses_.add(levelPartBonuses);
			}
			alivePlatforms_ = platforms_.get(0).size();
			player_ = new Player(playerSpawnPositions_[0][0],playerSpawnPositions_[0][1],1500.f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g){
		player_.draw(g);
		for(Platform p : platforms_.get(currentPart_)){
			p.draw(g);
		}
		for(Bonus b : bonuses_.get(currentPart_)){
			b.draw(g);
		}
	}

	public boolean completed(){
		return completed_;
	}
	
	public synchronized boolean paused(){
		return paused_;
	}
	
	public synchronized void pause(){
		paused_ = true;
	}
	
	public synchronized void unpause(){
		paused_ = false;
		notifyAll();
	}
	
	public void start(){
		Thread t = new Thread(this);
		t.start();
	}
	
	private synchronized void waitIfPaused(){
		try {
			while(paused_)
				wait();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Handles player collisions with objects
	 */
	private void resolveCollisions(){
		//checking collision with platforms
		for(Platform platform : platforms_.get(currentPart_)){
			//skip dead platforms
			if(!platform.isAlive())
				continue;
			if(player_.intersects(platform))
				player_.onCollsionWithPlatform(platform);
		}
		//checking if player is standing on platform
		//player_.checkForStanding(platforms_.get(currentPart_));
		resolvePlatformsState();
		//checking collision with bonuses
		for(Bonus bonus : bonuses_.get(currentPart_)){
			if(!bonus.collected()){
				if(player_.intersects(bonus)){
//					score += Bonus.SCORE;
					bonus.collect();
				}
			}
		}
		//world collision
		if(player_.y > WORLD_SIZE.height){
			player_.respawn(playerSpawnPositions_[currentPart_][0],playerSpawnPositions_[currentPart_][1]);
			for(Platform platform : platforms_.get(currentPart_))
				platform.reset();
			alivePlatforms_ = platforms_.get(currentPart_).size();
			--playerLifes_;
			gamePanel_.updatePlayerLifesLabel(playerLifes_);
			gamePanel_.updatePlatformsLabel(alivePlatforms_, alivePlatforms_);
		}
		else if(player_.x + player_.width > World.WORLD_SIZE.width)
			player_.x = World.WORLD_SIZE.width - player_.width;
		else if(player_.x < 0)
			player_.x = 0;
	}
	
	/**
	 * Changes platforms state
	 */
	private void resolvePlatformsState(){
		//temporarily set player falling state to true
		player_.setFalling(true);
		for(Platform platform: platforms_.get(currentPart_)){
			//skip dead platforms
			if(!platform.isAlive())
				continue;
			//if player is standing on platform
			if(player_.x+player_.width > platform.x && player_.x < platform.x + platform.width && player_.y + player_.height == platform.y){
				player_.setFalling(false);
				platform.stepOn();
			}
			//else if player is not standing on platform
			else{
				//if player was standing on this platform, destroy it 
				if(platform.steppedOn()){
					platform.destroy();
					--alivePlatforms_;
					gamePanel_.updatePlatformsLabel(alivePlatforms_, platforms_.get(currentPart_).size());
				}
			}
		}
	}
	
	public void run() {
		while(!completed_){
			waitIfPaused();
			try {
				Thread.sleep(UPDATE_DELTA_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			waitIfPaused();
			synchronized (this){
				player_.move();
				resolveCollisions();
				if(alivePlatforms_==0){
					if(++currentPart_ == NUMBER_OF_LEVEL_PARTS){
						--currentPart_;
						completed_ = true;
						gamePanel_.onLevelComplete();
					}
					else{
						alivePlatforms_ = platforms_.get(currentPart_).size();
						gamePanel_.updatePlatformsLabel(alivePlatforms_, alivePlatforms_);
						player_.respawn(playerSpawnPositions_[currentPart_][0], playerSpawnPositions_[currentPart_][1]);
					}
				}
				else if(playerLifes_ == 0){
					completed_ = true;
					gamePanel_.onPlayerDeath();
				}
				gamePanel_.repaint();
			}
		}
	}
}
