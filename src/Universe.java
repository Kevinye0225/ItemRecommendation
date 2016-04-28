import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


/*
 * This is where the user can see their recommendation score for certain movie,
 * and generate list of recommended movies
 * @author Kevin
 */
public class Universe {
	private ReadData data;
	private HashMap<Integer, User> users;
	private HashMap<String, Item> items;
	protected static final int kNeighbor = 20;
	private Scanner in;
	public double overallAvg;
	
	/*
	 * Constructor
	 */
	public Universe() throws IOException{
		data = new ReadData();
		data.readRating();
		in = null;
		this.overallAvg = data.totalScore/data.ratingCount;
		this.users = data.users;
		this.items = data.items;
	}
	
	/*
	 * returns a list contains n top recommended items for a user based on 
	 * the method user prefer
	 */
	public List<String> recommendList(User u){
		System.out.println("Please enter how many items you would like us to recommend: ");
		
		in = new Scanner(System.in);
		int j = in.nextInt();
		System.out.println("which recommended techinique would you like use: ");
		System.out.println("1: Baseline prediction, 2: KNN with cosine, 3: KNN with pearson");;
		int input = in.nextInt();
		
		List<String> res = new ArrayList<String>();
		List<Double> resValue = new ArrayList<Double>();
		List<Double> unwatchedItem = new ArrayList<Double>();
//		list to store items with same recommendation score
		HashMap<Double, ArrayList<String>> itemScore = new HashMap<Double, ArrayList<String>>();
		Set<String> itemList = items.keySet();
		Iterator<String> iterator = itemList.iterator();
		double predictionScore = 0;
		while (iterator.hasNext()){
			String itemId = iterator.next();
			if (!items.containsKey(itemId)){
				System.out.println("Please use a valid user id");
				return null;
			}
			if (!u.getItemList().containsKey(items.get(itemId))){
				if (input == 1){
					predictionScore = this.baselinePrediction(u, items.get(itemId));
				} else if (input == 2){
					predictionScore = this.predictionCosine(u, items.get(itemId));
				} else {
					predictionScore = this.prediction(u, items.get(itemId));
				}
				unwatchedItem.add(predictionScore);
				if (itemScore.containsKey(predictionScore)){
					itemScore.get(predictionScore).add(itemId);
				} else {
					ArrayList<String> newList = new ArrayList<String>();
					newList.add(itemId);
					itemScore.put(predictionScore, newList);
				}
			}
		}
		int i = 0;
		double currentMax = 0;
		if (j > unwatchedItem.size()){
			j = unwatchedItem.size();
		}
//		add the top value to result
		while (i < j){
			currentMax = 0;
			for (int k = 0; k < unwatchedItem.size(); k++){
				double tempValue = unwatchedItem.get(k);
				if (tempValue > currentMax && !resValue.contains(tempValue)){
					currentMax = tempValue;
				}
			}
			resValue.add(currentMax);
			for (int l = 0; l < itemScore.get(currentMax).size() && i < j; l++){
				res.add(itemScore.get(currentMax).get(l));
				
				i++;
			}
		}
		System.out.println("Here is the recommended list: ");
		for (int a = 0; a < res.size(); a++){
			int num = a+1;
			System.out.println("Movie " + num + ": " + res.get(a));
		}

		return res;
	}
	
	/*
	 * returns the prediction score using baseline predictor
	 */
	public double baselinePrediction(User u, Item item){
		double res = 0.0;
		if(!this.users.containsKey(u.getId())){
			System.out.println("User not exsits");
			return 0;
		}
		double userBaseline = 0;
		double itemBaseline = 0;
		
		Set<User> userList = item.getUser().keySet();
		Iterator<User> userIterator = userList.iterator();
		while (userIterator.hasNext()){
			User tempUser = userIterator.next();
			userBaseline = userBaseline(tempUser);
			double divValue = item.getRating(tempUser) - userBaseline - this.overallAvg;
			itemBaseline += divValue;
		}
		itemBaseline = 1/itemBaseline;
		res = this.overallAvg + userBaseline(u) + itemBaseline;
		
		return res;
	}
	
	/*
	 * returns user baseline score for a specific user
	 */
	public double userBaseline(User u){
		double res = 0.0;
		Set<Item> itemList = u.getItemList().keySet();
		Iterator<Item> iterator = itemList.iterator();
		while (iterator.hasNext()){
			Item tempItem = iterator.next();
			double divValue = u.getRating(tempItem) - this.overallAvg;
			res += divValue;
		}
		
		return 1/res;
	}
	
	/*
	 * returns prediction score using KNN from cosine similarity
	 */
	public double predictionCosine(User u, Item item){
		double res = 0.0;
		if(!this.users.containsKey(u.getId())){
			System.out.println("User not exsits");
			return 0;
		}
		List<Integer> userNeighbor = this.kNNCosine(kNeighbor, u, item);
		double top = 0.0;
		double bottom = 0.0;
		int userId = 0;
		for (int i = 0; i <userNeighbor.size(); i++){
		    userId = userNeighbor.get(i);
			double ratingValue = item.getRating((users.get(userId)));
			double averageRating = users.get(userId).getAverageRating();
			double pearsonValue = u.pearsonCorrelation(users.get(userId));
			top += pearsonValue*(ratingValue - averageRating);
			bottom += Math.abs(pearsonValue);
		}
		if (bottom == 0){
			return 0;
		}
		res = (top/bottom) + u.getAverageRating();
		return res;
	}
	
	/*
	 * returns recommendation score of an item to a user
	 */
	public double prediction(User u, Item item){
		double res = 0.0;
		if(!this.users.containsKey(u.getId())){
			System.out.println("User not exsits");
			return 0;
		}
		List<Integer> userNeighbor = this.kNearestNeighbor(kNeighbor, u, item);
		double top = 0.0;
		double bottom = 0.0;
		int userId = 0;
		for (int i = 0; i <userNeighbor.size(); i++){
		    userId = userNeighbor.get(i);
			double ratingValue = item.getRating((users.get(userId)));
			double averageRating = users.get(userId).getAverageRating();
			double pearsonValue = u.pearsonCorrelation(users.get(userId));
			top += pearsonValue*(ratingValue - averageRating);
			bottom += Math.abs(pearsonValue);
		}
		if (bottom == 0){
			return 0;
		}
		res = (top/bottom) + u.getAverageRating();
//		System.out.println("The recommendation score for this movie is: ");
//		if (res > 5){
//			System.out.println(5.0);
//		} else {
//			System.out.println(res);
//		}
		return res;
	}
	
	/*
	 * returns a list containing k neighbors using cosine similarity
	 */
	public List<Integer> kNNCosine(int k, User u, Item item){
		int s = k;
		List<Integer> res = new ArrayList<Integer>();
		List<Double> resValue = new ArrayList<Double>();
//		ensure user with same pearsoncorrelation is captured
		HashMap<Double, ArrayList<Integer>> userValueMap = new HashMap<Double, ArrayList<Integer>>();
		Set<User> userSet = item.getUser().keySet();
		if (s > item.getUser().size()){
			s = item.getUser().size();
		}
		Iterator<User> iterator = userSet.iterator();
		List<Double> userValueList = new ArrayList<Double>();
		double value = 0;
		int userId = 0;
		double currentMax = 0;
//		store pearson value of every user other than its own
		while (iterator.hasNext()){
			userId = iterator.next().getId();
			value = u.cosineSimilarity(users.get(userId));
			if (userId != u.getId()){
				userValueList.add(value);
				if (userValueMap.containsKey(value)){
					userValueMap.get(value).add(userId);
				} else {
					ArrayList<Integer> userIdList = new ArrayList<Integer>();
					userIdList.add(userId);
					userValueMap.put(value, userIdList);
				}
//				userValueMap.put(value, userId);
			} else {
				s--;
			}
		}
//		find the top kth value in the user list
		int i = 0;
		while (i < s){
			currentMax = Integer.MIN_VALUE;
			for (int j = 0; j < userValueList.size(); j++){
				double tempValue = userValueList.get(j);
				if (tempValue > currentMax && !resValue.contains(tempValue)){
					currentMax = tempValue;
				}
			}
//			add all elements 
			for (int l = 0; l < userValueMap.get(currentMax).size() && i < s; l++){
				resValue.add(currentMax);
				res.add(userValueMap.get(currentMax).get(l));
				i++;
			}
		}
		
		return res;
	}
	
	/*
	 * returns a list contains k userId that are most correlated to user u.
	 */
	public List<Integer> kNearestNeighbor(int k, User u, Item item){
		int s = k;
		List<Integer> res = new ArrayList<Integer>();
		List<Double> resValue = new ArrayList<Double>();
//		ensure user with same pearsoncorrelation is captured
		HashMap<Double, ArrayList<Integer>> userValueMap = new HashMap<Double, ArrayList<Integer>>();
		Set<User> userSet = item.getUser().keySet();
		if (s > item.getUser().size()){
			s = item.getUser().size();
		}
		Iterator<User> iterator = userSet.iterator();
		List<Double> userValueList = new ArrayList<Double>();
		double value = 0;
		int userId = 0;
		double currentMax = 0;
//		store pearson value of every user other than its own
		while (iterator.hasNext()){
			userId = iterator.next().getId();
			value = u.pearsonCorrelation(users.get(userId));
			if (userId != u.getId()){
				userValueList.add(value);
				if (userValueMap.containsKey(value)){
					userValueMap.get(value).add(userId);
				} else {
					ArrayList<Integer> userIdList = new ArrayList<Integer>();
					userIdList.add(userId);
					userValueMap.put(value, userIdList);
				}
//				userValueMap.put(value, userId);
			} else {
				s--;
			}
		}
//		find the top kth value in the user list
		int i = 0;
		while (i < s){
			currentMax = Integer.MIN_VALUE;
			for (int j = 0; j < userValueList.size(); j++){
				double tempValue = userValueList.get(j);
				if (tempValue > currentMax && !resValue.contains(tempValue)){
					currentMax = tempValue;
				}
			}
//			add all elements 
			for (int l = 0; l < userValueMap.get(currentMax).size() && i < s; l++){
				resValue.add(currentMax);
				res.add(userValueMap.get(currentMax).get(l));
				i++;
			}
		}
		
		return res;
	}
	
	public HashMap<Integer, User> getUserList(){
		return this.users;
	}
	
	public HashMap<String, Item> getItemList(){
		return this.items;
	}
	
}
