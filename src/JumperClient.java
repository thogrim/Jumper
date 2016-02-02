import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Klient aplikacji Jumper
 * 
 * @author Mateusz Antoniak
 */
@SuppressWarnings("serial")
public class JumperClient extends JFrame{
	
	/**
	 * Adres IP serwera
	 */
	private static String SERVER_IP = "localhost";
	
	/**
	 * Numer portu serwera
	 */
	private static int SERVER_PORT = 5555;
	
	/**
	 * Referencja na siebie
	 */
	private static JumperClient CLIENT_WINDOW;
	
	/**
	 * Przechowuje liczbê punktów z listy najlepszych wyników
	 */
	private static int[] HIGHSCORES_VALUES = new int[Config.NUM_OF_HIGHSCORES*Config.NUM_OF_LEVELS];
	
	/**
	 * Przechowuje imiona z listy najlepszych wyników 
	 */
	private static String[] HIGHSCORES_NAMES = new String[Config.NUM_OF_HIGHSCORES*Config.NUM_OF_LEVELS];
	
	/**
	 * Wskazuje, czy aplikacja komunikuje siê z serwerem
	 */
	private static boolean ONLINE;
	
	/**
	 * Konstruuje nowego klienta
	 */
	public JumperClient(){
		super("Jumper");
		CLIENT_WINDOW = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setResizable(false);
		Container mainPanel = getContentPane();
		mainPanel.setLayout(new BorderLayout());
		setSize(400,150);
		
		//reading local highscores
		try {
			Scanner scanner = new Scanner(new File(Config.HIGHSCORES_PATH));
			for(int i=0; i<Config.NUM_OF_HIGHSCORES*Config.NUM_OF_LEVELS; ++i){
				scanner.nextLine();
				HIGHSCORES_VALUES[i] = scanner.nextInt();
				HIGHSCORES_NAMES[i] = scanner.next();
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//Network settings label
		JLabel settingsLabel = new JLabel("Network Settings",JLabel.CENTER);
		
		//Server response label
		JLabel serverResponseLabel = new JLabel("Press \"Check\" to see if server is available",JLabel.CENTER);
		
		//IP label
		JLabel serverIPLabel = new JLabel("Server IP: ",JLabel.RIGHT);
		
		//Port label
		JLabel serverPortLabel = new JLabel("Server port: ",JLabel.RIGHT);
		
		//test1 label
		JTextField serverIPField = new JTextField("localhost",10);

		//test2 label
		JTextField serverPortField = new JTextField("5555",15);
		
		//quit button
		JButton quitButton = new JButton("Quit");
		quitButton.setPreferredSize(new Dimension(80,25));
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		//check button
		JButton checkButton = new JButton("Check");
		checkButton.setPreferredSize(new Dimension(80,25));
		checkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serverResponseLabel.setForeground(Color.BLACK);
				serverResponseLabel.setText("Checking...");
				SERVER_IP = serverIPField.getText();
				try{
					SERVER_PORT = Integer.parseInt(serverPortField.getText());
					new Thread(new Runnable() {
						public void run() {
							try {
								Socket socket = new Socket(SERVER_IP, SERVER_PORT);
								OutputStream os = socket.getOutputStream();
								PrintWriter pw = new PrintWriter(os,true);
								pw.println(Protocol.GREETING);
								InputStream is = socket.getInputStream();
								BufferedReader br = new BufferedReader(new InputStreamReader(is));
								serverResponseLabel.setForeground(Color.GREEN);
								serverResponseLabel.setText("Server responded: \""+br.readLine()+"\"");
								socket.close();
							} catch (IOException e) {
								serverResponseLabel.setForeground(Color.RED);
								serverResponseLabel.setText("Server is not available.");
							}
						}
					}).start();
				} catch(NumberFormatException ex){
					serverResponseLabel.setText("Wrong port format!");
				}
			}
		});
		
		//Play online
		JButton playOnline = new JButton("Play online");
		playOnline.setPreferredSize(new Dimension(100,25));
		playOnline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serverResponseLabel.setForeground(Color.BLACK);
				serverResponseLabel.setText("Getting data...");
				SERVER_IP = serverIPField.getText();
				try{
					SERVER_PORT = Integer.parseInt(serverPortField.getText());
					new Thread(new Runnable() {
						public void run() {
							try {
								Socket socket = new Socket(SERVER_IP, SERVER_PORT);
								OutputStream os = socket.getOutputStream();
								PrintWriter pw = new PrintWriter(os,true);
								InputStream is = socket.getInputStream();
								BufferedReader br = new BufferedReader(new InputStreamReader(is));
								getConfig(pw,br);
								socket.close();
								socket = new Socket(SERVER_IP, SERVER_PORT);
								os = socket.getOutputStream();
								pw = new PrintWriter(os,true);
								is = socket.getInputStream();
								br = new BufferedReader(new InputStreamReader(is));
								getLevelDefinitions(pw,br);
								socket.close();
								ONLINE = true;
								initJumper();
							} catch (IOException e) {
								serverResponseLabel.setForeground(Color.RED);
								serverResponseLabel.setText("Server is not available.");
							}
						}
					}).start();
				} catch(NumberFormatException ex){
					serverResponseLabel.setText("Wrong port format!");
				}
			}
		});
		
		//play offline
		JButton playOffline = new JButton("Play offline");
		playOffline.setPreferredSize(new Dimension(100,25));
		playOffline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ONLINE = false;
				initJumper();
			}
		});
		
		//panel for server settings
		JPanel serverSettingsPanel = new JPanel();
		serverSettingsPanel.setLayout(new GridLayout(2, 2));
		serverSettingsPanel.add(serverIPLabel);
		serverSettingsPanel.add(serverIPField);
		serverSettingsPanel.add(serverPortLabel);
		serverSettingsPanel.add(serverPortField);
		
		//panel for buttons
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(checkButton);
		buttonsPanel.add(playOnline);;
		buttonsPanel.add(playOffline);
		buttonsPanel.add(quitButton);
		
		//center panel
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(serverResponseLabel,BorderLayout.NORTH);
		centerPanel.add(serverSettingsPanel, BorderLayout.CENTER);
		
		//south panel
		JPanel southPanel = new JPanel(); 
		southPanel.add(buttonsPanel,BorderLayout.EAST);
		
		//adding all components
		mainPanel.add(settingsLabel,BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	/**
	 * Zwraca adres IP serwera
	 * 
	 * @return Adres IP serwera
	 */
	public static String getServerIP(){
		return SERVER_IP;
	}
	
	/**
	 * Zwraca numer portu serwera
	 * 
	 * @return Numer portu serwera
	 */
	public static int getServerPort(){
		return SERVER_PORT;
	}
	
	/**
	 * Œci¹ga plik konfiguracyjny z serwera
	 * 
	 * @param pw - Strumieñ wyjœciowy gniazdka klienta
	 * @param br - Strumieñ wejœciowy gniazdka klienta
	 */
	private void getConfig(PrintWriter pw, BufferedReader br){
		try{
			FileWriter fw = new FileWriter(new File("config.txt"));
			pw.println(Protocol.GET_CONFIG);
			fw.write("// Number of player lifes");
			fw.write(Config.NEW_LINE+br.readLine());
			fw.write(Config.NEW_LINE+"// Level names");
			for(int i = 0; i < Config.NUM_OF_LEVELS; ++i){
				fw.write(Config.NEW_LINE+br.readLine());
			}
			fw.close();
		} catch(IOException ex){
			
		}
	}
	
	/**
	 * Œci¹ga defnicje poziomów z serwera
	 * 
	 * @param pw - Strumieñ wyjœciowy gniazdka klienta
	 * @param br - Strumieñ wejœciowy gniazdka klienta
	 */
	private void getLevelDefinitions(PrintWriter pw, BufferedReader br){
		pw.println(Protocol.GET_LEVEL_DEFINITIONS);
		for(int i = 0; i<Config.NUM_OF_LEVELS; ++i){
			try{
				String levelName = br.readLine();
				FileWriter fw = new FileWriter(new File(Config.LEVELS_PATH+"/"+levelName+".txt"));
				//fw.write(br.readLine());
				for(int j=0; j< 1+(World.WORLD_HEIGHT/World.TILE_SIZE)*World.NUM_OF_LEVEL_PARTS; ++j){
					fw.write(Config.NEW_LINE+br.readLine());
				}
				fw.close();
			} catch(IOException ex){
				
			}
		}
	}
	
	/**
	 * Zwraca listê najlepszych wyników danego poziomu
	 * 
	 * @param levelID - Identyfikator poziomu
	 * @return Lista najlepszych wyników danego poziomu
	 */
	public static String getHighscores(int levelID){
		if(ONLINE){
			try {
				//getting highscores from server
				Socket socket = new Socket(SERVER_IP, SERVER_PORT);
				OutputStream os = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(os,true);
				pw.println(Protocol.GET_HIGHSCORES);
				InputStream is = socket.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				for(int i=0; i<Config.NUM_OF_HIGHSCORES*Config.NUM_OF_LEVELS; ++i){
					HIGHSCORES_NAMES[i] = br.readLine();
					HIGHSCORES_VALUES[i] = Integer.parseInt(br.readLine());
				}
				//and saving it to local file
				try {
					FileWriter fw = new FileWriter(new File(Config.HIGHSCORES_PATH));
					fw.write("#High Scores - Value - Player Name\n");
					for(int j=0; j<Config.NUM_OF_HIGHSCORES*Config.NUM_OF_LEVELS; ++j){
						fw.write(HIGHSCORES_VALUES[j]+" "+HIGHSCORES_NAMES[j]+"\n");
					}
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				socket.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,"Could not get highscores from server! Showing local highscores instead","Connection problem",JOptionPane.ERROR_MESSAGE);
			}
		}
		String highscores = "";
		for(int i=Config.NUM_OF_HIGHSCORES-1; i>=0; --i){
			String name = HIGHSCORES_NAMES[i+levelID*Config.NUM_OF_HIGHSCORES];
			int points = HIGHSCORES_VALUES[i+levelID*Config.NUM_OF_HIGHSCORES];
			int place = Config.NUM_OF_HIGHSCORES-i;
			highscores = highscores.concat(place+". "+name+" "+points+Config.NEW_LINE);
		}
		return highscores;
	}
	
	/**
	 * Przetwarza uzyskany wynik gracza. Wysy³a wynik do serwera i zapisuje na
	 * lokalnej liœcie najlepszych wyników
	 * 
	 * @param levelID - Identyfokator poziomu
	 * @param score - Liczba uzyskanych punktów
	 * @param profileName - Nazwa gracza
	 */
	public static void processScore(int levelID, int score, String profileName){
		new Thread(new Runnable() {
			public void run() {
				//sending highscores to server
				if(ONLINE){
					try {
						Socket socket = new Socket(SERVER_IP, SERVER_PORT);
						OutputStream os = socket.getOutputStream();
						PrintWriter pw = new PrintWriter(os,true);
						pw.println(Protocol.PROCESS_SCORE);
						pw.println(levelID);
						pw.println(score);
						pw.println(profileName);
						socket.close();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null,"Could not send score to server!","Connection problem",JOptionPane.ERROR_MESSAGE);
					}
				}
				//processing local highscores
				//check if highscore
				int i=0;
				for(i = 0; i<Config.NUM_OF_HIGHSCORES; ++i){
					if(score < HIGHSCORES_VALUES[i+levelID*Config.NUM_OF_HIGHSCORES])
						break;
				}
				//if yes then save it 
				if(i>0){
					--i;
					HIGHSCORES_VALUES[i+levelID*Config.NUM_OF_HIGHSCORES]=score;
					HIGHSCORES_NAMES[i+levelID*Config.NUM_OF_HIGHSCORES]=profileName;
					try {
						FileWriter fw = new FileWriter(new File(Config.HIGHSCORES_PATH));
						fw.write("#High Scores - Value - Player Name\n");
						for(int j=0; j<Config.NUM_OF_HIGHSCORES*Config.NUM_OF_LEVELS; ++j){
							fw.write(HIGHSCORES_VALUES[j]+" "+HIGHSCORES_NAMES[j]+"\n");
						}
						fw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	/**
	 * Startuje g³ówn¹ aplikacjê Jumper
	 */
	private void initJumper(){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Config.readConfiguration();
				Jumper jumper = new Jumper();
				Dimension d = jumper.getToolkit().getScreenSize();
				jumper.setLocation((int)(d.getWidth()/2-jumper.getWidth()/2),(int)(d.getHeight()/2-jumper.getHeight()/2));
				CLIENT_WINDOW.dispose();
			}
		});
	}
}
