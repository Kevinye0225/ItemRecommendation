import java.util.Comparator;
import java.util.HashMap;

/*
 * Items that contains rating from different users
 * @author Kevin
 */
public class Item implements Comparator<Item>{
//	private String name;
	private String id;
	private int count;
	private double totalScore;
	private HashMap<User, Double> rating;
	
	/*
	 * constructor
	 */
	public Item(String id){
//		this.name = name;
		this.id = id;
		this.count = 0;
		this.totalScore = 0;
		this.rating = new HashMap<User, Double>();
	}
	
	/*
	 * add rating from a user, update count and total score
	 */
	public void addRating(User u, double score){
		rating.put(u, score);
		count++;
		totalScore += score;
	}
	
	public double getAverageRating(){
		if (count == 0){
			return 0;
		}
		return this.totalScore/this.count;
	}
	
	public double getRating(User u){
		if (!rating.containsKey(u)){
			return 0.0;
		}
		return this.rating.get(u);
	}
	
	/*
	 * getter method
	 */
	public HashMap<User, Double> getUser(){
		return this.rating;
	}
	
//	public String getName(){
//		return this.name;
//	}
	
	public String getId(){
		return this.id;
	}

	@Override
	public int compare(Item o1, Item o2) {
		// TODO Auto-generated method stub
//		if (o1.getId() < o2.getId()){
//			return -1;
//		} else if (o1.getId() > o2.getId()){
//			return 1;
//		} else {
//			return 0;
//		}
		return o1.id.compareTo(o2.id);
	}
}
