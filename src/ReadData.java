import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;



/*
 * This class reads the file and store it in different data structure in java
 * @author Kevin
 */
public class ReadData {
	public HashMap<String, Item> items;
	public HashMap<Integer, User> users;
	private Scanner in;
	public double totalScore;
	public int ratingCount;
	private BufferedReader bf;
	
	/*
	 * Constructor
	 */
	public ReadData(){
		this.items = new HashMap<String, Item>();
		this.users = new HashMap<Integer, User>();
		totalScore = 0;
		ratingCount = 0;
		in = null;
		bf = null;
	}
	
	/*
	 * reads the main rating file and stores users rating on both item and user class
	 */
	public void readRating(){

		int userId = 0;
		double rating = 0.0;
		String itemId = null;
		in = new Scanner(System.in);
		String fileName = null;
		
		System.out.println("Please select books(1) or latestmovies(2) or latestmoviesLarge(3): ");
		int selection = in.nextInt();
		if (selection == 1){
			fileName = "BX-CSV-Dump/BX-Book-Ratings.csv";
		} else if (selection == 2){
			fileName = "ml-latest-small/ratings.csv";
		} else if (selection == 3){
			fileName = "ml-latest/ratings.csv";
		} else {
			fileName = "ml-10M100K/ratings.dat";
		}
//		in.close();
		try {
			File inputFile = new File(fileName);
			bf = new BufferedReader(new FileReader(inputFile));
			String line = bf.readLine();
			while ((line = bf.readLine()) != null){
				if (line.contains(";")){
					String[] lineSeparator = line.split(";");
					String[] userIdList = lineSeparator[0].split("\"");
					String[] itemIdList = lineSeparator[1].split("\"");
					String[] ratingList = lineSeparator[2].split("\"");
					userId = Integer.parseInt(userIdList[1]);
					itemId = itemIdList[1];
					rating = Double.parseDouble(ratingList[1]);
				} else {
					String[] lineSeparator = line.split("::|,");
					userId = Integer.parseInt(lineSeparator[0]);
					itemId = lineSeparator[1];
					rating = Double.parseDouble(lineSeparator[2]);
				} 
				
				if (!items.containsKey(itemId)){
					Item newItem = new Item(itemId);
					items.put(itemId, newItem);
				}
				
				if (!users.containsKey(userId)){
					User newUser = new User(userId);
					users.put(userId, newUser);
				}
				

				this.items.get(itemId).addRating(users.get(userId), rating);
				this.users.get(userId).addRating(items.get(itemId), rating);
				totalScore += rating;
				ratingCount ++;
			}
		} catch(IOException e){
			e.printStackTrace();
		}
		
		
	}
	
	
}
