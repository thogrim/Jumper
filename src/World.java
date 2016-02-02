import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * Reprezentuje œwiat gry
 * 
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class World implements Runnable{
	/**
	 * Szerokoœæ œwiata w pikselach
	 */
	public static final int WORLD_WIDTH = 800;
	
	/**
	 * Wysokoœæ œwiata w pikselach
	 */
	public static final int WORLD_HEIGHT = 600;
	
	/**
	 * Bazowa grawitacja œwiata
	 */
	public static final float BASE_GRAVITY = 1500.f;
	
	/**
	 * Bazowy przelicznik czasu rozgrywki na punkty
	 */
	public static final float BASE_TIME_TO_POINTS = 10.f;
	
	/**
	 * Rozmiar kafelka œwiata w pikselach
	 */
	public static final int TILE_SIZE = 40;
	
	/**
	 * Liczba czêœci, z których sk³ada siê poziom
	 */
	public static final int NUM_OF_LEVEL_PARTS = 5;
	
	/**
	 * Identyfikator platformy w pliku definicj poziomu
	 */
	private static final int PLATFORM_ID  = 1;
	
	/**
	 * Identyfikator bonusu w pliku definicj poziomu
	 */
	private static final int BONUS_ID  = 2;
	
	/**
	 * Identyfikator gracza w pliku definicj poziomu
	 */
	private static final int PLAYER_ID = 3;
	
	/**
	 * Czas pomiêdzy kolejnymi aktualizacjami stanu gry
	 */
	public static final long UPDATE_DELTA_TIME = 16;
	
	/**
	 * Referencja na stan gry
	 */
	private Gameplay gamePanel_;
	
	/**
	 * Postaæ gracza
	 */
	private Player player_;
	
	/**
	 * Lista pocz¹tkowych pozycji gracza
	 */
	private int[][] playerSpawnPositions_;
	
	/**
	 * Lista platform
	 */
	private ArrayList<ArrayList<Platform>> platforms_;
	
	/**
	 * Lista bonusów
	 */
	private ArrayList<ArrayList<Bonus>> bonuses_;
	
	/**
	 * Liczba pozosta³ych ¿yæ gracza
	 */
	private int playerLifes_;
	
	/**
	 * Liczba punktów, które gracz aktualnie posiada
	 */
	private int playerPoints_;
	
	/**
	 * Liczba punktów, które gracz posiada na pocz¹tku ka¿dej czêœci poziomu
	 */
	private int playerResetPoints_;
	
	/**
	 * Wspó³czynnik konwersji czasu rozgrywki na punkty
	 */
	private float timeToPointsFactor_;
	
	/**
	 * Wspó³czynnik grawitacji
	 */
	private float worldGravityFactor_;
	
	/**
	 * Liczba aktywnych platform
	 */
	private int alivePlatforms_;
	
	/**
	 * Aktualna czêœæ poziomu
	 */
	private int currentPart_;
	
	/**
	 * Stoper mier¿acy czas rozgrywki
	 */
	private double timer_;
	
	/**
	 * Wskazuje, czy gra jest zatrzymana
	 */
	private boolean paused_;
	
	/**
	 * Wskazuje, czy gracz ukoñczy³ poziom
	 */
	private boolean completed_;

	/**
	 * Konstruuje nowy œwiat gry
	 * 
	 * @param game - Referencja na stan gry
	 * @param id - Identyfikator poziomu
	 */
	public World(Gameplay game, int id){
		Textures.readTextures();
		gamePanel_ = game;
		paused_ = true;
		completed_ = false;
		playerSpawnPositions_ = new int [NUM_OF_LEVEL_PARTS][2];
		platforms_ = new ArrayList<>(NUM_OF_LEVEL_PARTS);
		bonuses_ = new ArrayList<>(NUM_OF_LEVEL_PARTS);
		playerLifes_ = Config.NUM_OF_PLAYER_LIFES;
		playerPoints_ = 0;
		playerResetPoints_ = 0;
		timer_ = 0;
		readLevel(Config.getLevelName(id));
		player_ = new Player(playerSpawnPositions_[0][0],playerSpawnPositions_[0][1],BASE_GRAVITY*worldGravityFactor_);
		gamePanel_.updatePlatformsLabel(alivePlatforms_, platforms_.get(0).size());
		gamePanel_.updatePlayerLifesLabel(playerLifes_);
		setKeyBindings();
	}

	/**
	 * Przypisuje klawisze do akcji
	 */
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
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_P,0), "pauseGame");
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
		actions.put("pauseGame", new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				gamePanel_.pauseGame();
			}
		});
	}

	/**
	 * Wczytuje dane o poziomie
	 * 
	 * @param levelName - Nazwa pliku z danymi
	 */
	private void readLevel(String levelName) {
		try {
			Scanner scanner = new Scanner(new File(Config.LEVELS_PATH+"/"+levelName+".txt"));
			timeToPointsFactor_ = scanner.nextFloat();
			worldGravityFactor_ = scanner.nextFloat();
			int worldWidth = WORLD_WIDTH/TILE_SIZE;
			int worldHeight = WORLD_HEIGHT/TILE_SIZE;
			for(int levelPart = 0; levelPart < NUM_OF_LEVEL_PARTS; ++levelPart){
				ArrayList<Platform> levelPartPlatforms = new ArrayList<Platform>();
				ArrayList<Bonus> levelPartBonuses = new ArrayList<Bonus>();
				for(int j = 0; j < worldHeight; ++j){
					scanner.nextLine();
					for(int i = 0; i < worldWidth; ++i){
						int id = scanner.nextInt();
						switch(id){
						case PLAYER_ID:
							playerSpawnPositions_[levelPart][0] = i;
							playerSpawnPositions_[levelPart][1] = j;
							break;
						case PLATFORM_ID:
							levelPartPlatforms.add(new Platform(i,j));
							break;
						case BONUS_ID:
							levelPartBonuses.add(new Bonus(i,j));
							break;
						}
					}
				}
				platforms_.add(levelPartPlatforms);
				bonuses_.add(levelPartBonuses);
			}
			alivePlatforms_ = platforms_.get(0).size();
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Rysuje œwiat gry
	 * 
	 * @param g - Kontekst graficzny
	 */
	public void draw(Graphics g){
		//Graphics2D g = worldBuffer_.createGraphics();
		g.setColor(Color.CYAN);
		g.fillRect(0,0,(int)(WORLD_WIDTH*Gameplay.getScaleX()),(int)(WORLD_HEIGHT*Gameplay.getScaleY()));
		player_.draw(g);
		for(Platform p : platforms_.get(currentPart_)){
			p.draw(g);
		}
		for(Bonus b : bonuses_.get(currentPart_)){
			b.draw(g);
		}
	}
	
	/**
	 * Zwraca true jeœli poziom zosta³ ukoñczony
	 * 
	 * @return True jeœli poziom zosta³ ukoñczony, false w przeciwnym wypdaku
	 */
	public boolean completed(){
		return completed_;
	}
	
	/**
	 * Zwraca true jeœli rozgrywka jest zatrzymana
	 * 
	 * @return True jeœli rozgrywka jest zatrzymana, false w przeciwnym wypdaku
	 */
	public synchronized boolean paused(){
		return paused_;
	}
	
	/**
	 * Zatrzymuje rozgrywkê
	 */
	public synchronized void pause(){
		paused_ = true;
	}
	
	/**
	 * Wznawia rozgrywkê
	 */
	public synchronized void unpause(){
		paused_ = false;
		notifyAll();
	}
	
	/**
	 * Startuje w¹tek, w którym odbywa siê rozgrywka
	 */
	public void start(){
		Thread t = new Thread(this);
		t.start();
	}
	
	/**
	 * Utrzymuje watek œwiata gry w stanie uœpienia, gdy rozgrywka jest zatrzymana 
	 */
	private synchronized void waitIfPaused(){
		try {
			while(paused_)
				wait();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Resetuje œwiat gry do stanu pocz¹tkowego
	 */
	public void reset(){
		currentPart_ = 0;
		player_.respawn(playerSpawnPositions_[currentPart_][0],playerSpawnPositions_[currentPart_][1]);
		for(int i = 0; i < World.NUM_OF_LEVEL_PARTS; ++i){
			for(Platform platform : platforms_.get(i))
				platform.reset();
			for(Bonus bonus : bonuses_.get(i))
				bonus.reset();
		}
		alivePlatforms_ = platforms_.get(currentPart_).size();
		playerLifes_ = Config.NUM_OF_PLAYER_LIFES;
		playerResetPoints_ = 0;
		playerPoints_ = 0;
		timer_ = 0;
		gamePanel_.updatePlayerLifesLabel(playerLifes_);
		gamePanel_.updatePlatformsLabel(alivePlatforms_, alivePlatforms_);
		gamePanel_.repaint();
	}
	
	/**
	 * Sprawdza kolizje gracza z innymi obiektami
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
		resolvePlatformsState();
		
		//checking collision with bonuses
		for(Bonus bonus : bonuses_.get(currentPart_)){
			if(!bonus.collected()){
				if(player_.intersects(bonus)){
					playerPoints_ += Bonus.SCORE;
					bonus.collect();
				}
			}
		}
		//world collision
		if(player_.y > WORLD_HEIGHT){
			player_.respawn(playerSpawnPositions_[currentPart_][0],playerSpawnPositions_[currentPart_][1]);
			for(Platform platform : platforms_.get(currentPart_))
				platform.reset();
			for(Bonus bonus : bonuses_.get(currentPart_))
				bonus.reset();
			alivePlatforms_ = platforms_.get(currentPart_).size();
			--playerLifes_;
			playerPoints_ = playerResetPoints_;
			gamePanel_.updatePlayerLifesLabel(playerLifes_);
			gamePanel_.updatePlatformsLabel(alivePlatforms_, alivePlatforms_);
		}
		else if(player_.x + player_.width > World.WORLD_WIDTH)
			player_.x = World.WORLD_WIDTH - player_.width;
		else if(player_.x < 0)
			player_.x = 0;
	}
	
	/**
	 * Ustawia nowe stany platform
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
				//and if player was standing on this platform, destroy it 
				if(platform.steppedOn()){
					platform.destroy();
					--alivePlatforms_;
					gamePanel_.updatePlatformsLabel(alivePlatforms_, platforms_.get(currentPart_).size());
				}
			}
		}
	}
	
	/**
	 * Oblicza iloœæ punktów zdobyt¹ przez gracza
	 * 
	 * @return Iloœæ punktów zdobyt¹ przez gracza
	 */
	public int calculatePlayerPoints(){
		playerPoints_ += 100*playerLifes_;
		playerPoints_ -= (int)(timeToPointsFactor_*BASE_TIME_TO_POINTS*timer_/1000.f);
		return playerPoints_;
	}
	
	/**
	 * G³ówna pêtla gry
	 */
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
					if(++currentPart_ == NUM_OF_LEVEL_PARTS){
						--currentPart_;
						completed_ = true;
						gamePanel_.onLevelComplete(timer_,calculatePlayerPoints());
					}
					else{
						playerResetPoints_ = playerPoints_;
						alivePlatforms_ = platforms_.get(currentPart_).size();
						gamePanel_.updatePlatformsLabel(alivePlatforms_, alivePlatforms_);
						player_.respawn(playerSpawnPositions_[currentPart_][0], playerSpawnPositions_[currentPart_][1]);
						gamePanel_.updatePartLabel(currentPart_+1);
					}
				}
				else if(playerLifes_ == 0){
					paused_ = true;
					gamePanel_.onPlayerDeath();
				}
				timer_ += UPDATE_DELTA_TIME;
				gamePanel_.updateTimer(timer_);
				gamePanel_.repaint();
			}
		}
	}
}
