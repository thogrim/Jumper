import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Profile class represents player's profile information
 *
 * @author Mateusz Antoniak
 */
public class Profile {
	
	/**
	 * Name of the profile
	 */
	private String profileName_;
	
	/**
	 * Number of levels that player has completed so far
	 */
	private int completedLevels_;
	
	/**
	 * Best scores for each level
	 */
	private int[] bestScores_ = new int[Config.NUMBER_OF_LEVELS];
	
	public String getProfileName() {
		return profileName_;
	}

	public int getCompletedLevels() {
		return completedLevels_;
	}

	public int[] getBestScores() {
		return bestScores_;
	}

	/**
	 * Constructs profile from information stored in text file
	 * 
	 * @param filename Name of file that contains profile's information
	 */
	public Profile(String filename){
		try {
			Scanner scanner = new Scanner(new File(Config.PROFILES_PATH+"/"+filename));
			profileName_ = filename.substring(0, filename.indexOf("."));
			completedLevels_ = scanner.nextInt();
			for(int i = 0; i < Config.NUMBER_OF_LEVELS; ++i){
				bestScores_[i] = scanner.nextInt();
			}
			scanner.close();
		} catch (FileNotFoundException e) {	
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates new profile
	 * 
	 * @param profileName Name of the profile
	 * @throws IOException
	 */
	public static void create(String profileName) throws IOException{
		//if profile exists throw exception
		if(new File(profileName+".txt").exists())
			throw new IOException("Name already taken! Choose something else.");
		
		//else create profile
		FileWriter writer = new FileWriter(new File(profileName+".txt"));
		writer.write(1+"\r\n");
		for(int i = 0; i < Config.NUMBER_OF_LEVELS; ++i){
			writer.write(0+"\r\n");
		}
		writer.close();
	}
	
	/**
	 * Saves profile information
	 */
	void saveProfile(){
		try {
			FileWriter writer = new FileWriter(new File(profileName_+".temp"));
			writer.write(completedLevels_+"\r\n");
			for(int i = 0; i < Config.NUMBER_OF_LEVELS; ++i){
				writer.write(bestScores_[i]+"\r\n");
			}
			writer.close();
			File filename = new File(profileName_+".txt");
			filename.delete();
			new File(profileName_+".temp").renameTo(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
