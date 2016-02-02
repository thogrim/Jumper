import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Reprezentuje profil gracza
 *
 * @author Mateusz Antoniak
 */
public class Profile {
	
	/**
	 * Nazwa profilu
	 */
	private String profileName_;
	
	/**
	 * Liczba ukoñczonych poziomów
	 */
	private int completedLevels_;
	
	/**
	 * Najlepszy wynik gracza w ka¿dym poziomie
	 */
	private int[] bestScores_;
	
	/**
	 * Zwraca nazwê profilu
	 * 
	 * @return Nazwa profilu
	 */
	public String getName() {
		return profileName_;
	}

	/**
	 * Zwraca liczbê ukoñczonych poziomów
	 * 
	 * @return Liczba ukoñczonych poziomów
	 */
	public int getCompletedLevels() {
		return completedLevels_;
	}

	/**
	 * Zwraca najlepsze wyniki gracza 
	 * 
	 * @return Najlepsze wyniki gracza
	 */
	public int[] getBestScores() {
		return bestScores_;
	}
	
	/**
	 * Odblokowuje kolejny poziom
	 */
	public void unlockNextLevel(){
		++completedLevels_;
	}
	
	
	/**
	 * Ustawia nowy najlepszy wynik gracza
	 * 
	 * @param points - Liczba uzyskanych punktów
	 * @param levelId - Identyfikator poziomu
	 */
	public void setNewHighscore(int points, int levelId){
		bestScores_[levelId] = points;
	}
	
	/**
	 * Konstruuje profil gracza na podstawie danych zawartych w pliku
	 * 
	 * @param filename - Nazwa pliku z danymi profilowymi
	 */
	public Profile(String filename){
		try {
			Scanner scanner = new Scanner(new File(Config.PROFILES_PATH+"/"+filename));
			profileName_ = filename.substring(0, filename.indexOf("."));
			completedLevels_ = scanner.nextInt();
			bestScores_ = new int[Config.NUM_OF_LEVELS];
			for(int i = 0; i < Config.NUM_OF_LEVELS; ++i){
				bestScores_[i] = scanner.nextInt();
			}
			scanner.close();
		} catch (FileNotFoundException e) {	
			e.printStackTrace();
		}
	}
	
	/**
	 * Prywatny domyœlny konstruktor 
	 */
	private Profile(){
	}
	
	/**
	 * Tworzy nowy profil i jego plik z danymi
	 * 
	 * @param profileName - Nazwa profilu
	 * @throws IOException
	 */
	public static Profile create(String profileName) throws IOException{
		//if profile exists or string is empty throw exception
		//if(profileName==null)
		//	throw new IOException("Name cannot be null!");
		if(profileName.length()==0)
			throw new IOException("Name cannot be null!");
		if(new File(Config.PROFILES_PATH+"/"+profileName+".txt").exists())
			throw new IOException("Name already taken! Choose something else.");
		
		//else create file with profile information
		FileWriter writer = new FileWriter(new File(Config.PROFILES_PATH+"/"+profileName+".txt"));
		for(int i = 0; i < Config.NUM_OF_LEVELS+1; ++i){
			writer.write(0+"\r\n");
		}
		writer.close();
		
		//and create profile
		Profile newProfile = new Profile();
		newProfile.profileName_ = profileName;
		newProfile.completedLevels_ = 0;
		newProfile.bestScores_ = new int[Config.NUM_OF_LEVELS];
		Arrays.fill(newProfile.bestScores_, 0);
		return newProfile;
	}
	
	/**
	 * Zapisuje dane profilu do pliku
	 */
	void saveProfile(){
		try {
			FileWriter writer = new FileWriter(new File(Config.PROFILES_PATH+"/"+profileName_+".txt"));
			writer.write(completedLevels_+"\n");
			for(int i = 0; i < Config.NUM_OF_LEVELS; ++i){
				writer.write(bestScores_[i]+"\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
