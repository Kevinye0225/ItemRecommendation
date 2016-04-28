import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/*
 * This is the user class, it has a name, id and list of items with rating
 * @author Kevin
 */
public class User implements Comparator<User>{
	private int id;
	private int count;
	private double totalScore;
	private HashMap<Item, Double> ratedItems;
	private HashSet<String> movieId;
	
    /*
     * Constructor
     */
	public User(int id){
		this.id = id;
		count = 0;
		totalScore = 0;
		ratedItems = new HashMap<Item, Double>();
		movieId = new HashSet<String>();
	}
	

	
	/*
	 * add rating of a movie to the user collection
	 */
	public void addRating(Item m, double score){
		ratedItems.put(m,score);
		movieId.add(m.getId());
		count++;
		totalScore += score;
	}
	
	/*
	 * returns the rating of an item from the user
	 */
	public double getRating(Item m){
		if (!ratedItems.containsKey(m)){
			return 0.0;
		}
		return ratedItems.get(m);
	}
	
	public double getAverageRating(){
		if (count == 0){
			return 0;
		}
		return totalScore/count;
	}
	
	/*
	 * returns the cosine similarity level with user u
	 */
	public double cosineSimilarity(User u){
		Set<Item> itemList = this.commonItem(u);
		Iterator<Item> iterator = itemList.iterator();
		double top = 0;
		double bottomLeft = 0;
		double bottomRight = 0;
		while (iterator.hasNext()){
			Item item = iterator.next();
			top += (this.getRating(item)*u.getRating(item));
			bottomLeft += Math.pow(this.getRating(item),2);
			bottomRight += Math.pow(u.getRating(item), 2);
		}
		
		if (bottomLeft == 0 || bottomRight == 0){
			return 0;
		}
		bottomLeft = Math.sqrt(bottomLeft);
		bottomRight = Math.sqrt(bottomRight);
		
		return top/(bottomLeft*bottomRight);
	}
	
    /*
     * calculates the pearson correlation value of an user to the current user
     */
	public double pearsonCorrelation(User u){
		Set<Item> itemList = this.commonItem(u);
		Iterator<Item> iterator = itemList.iterator();
		double top = 0;
		double bottomLeft = 0;
		double bottomRight = 0;
		double currentAvg = this.getAverageRating();
		double compareAvg = u.getAverageRating();
		while (iterator.hasNext()){
			Item temp = (Item) iterator.next();
			top += ((this.getRating(temp) - currentAvg)*(u.getRating(temp)-compareAvg));
			bottomLeft += Math.pow(this.getRating(temp) - currentAvg,2);
			bottomRight += Math.pow(u.getRating(temp) - compareAvg, 2);
		}
		if (bottomLeft == 0 || bottomRight == 0){
			return 0;
		}
		bottomLeft = Math.sqrt(bottomLeft);
		bottomRight = Math.sqrt(bottomRight);
		
		return top/(bottomLeft*bottomRight);
	}
	
	/*
	 * return a set of items that two user have in common
	 */
	private Set<Item> commonItem(User u){
		HashSet<Item> res = new HashSet<Item>();
		Set<Item> itemList = u.ratedItems.keySet();
		Iterator<Item> iterator = itemList.iterator();
		while (iterator.hasNext()){
			Item temp = iterator.next();
			if (this.movieId.contains(temp.getId())){
				res.add(temp);
			}
		}
		
		return res;
	}
	
	public int getId(){
		return this.id;
	}
	
	public Map<Item, Double> getItemList(){
		return this.ratedItems;
	}

	@Override
	public int compare(User o1, User o2) {
		// TODO Auto-generated method stub
		if (o1.getId() < o2.getId()){
			return -1;
		} else if (o1.getId() == o2.getId()){
			return 0;
		} else {
			return 1;
		}
	}
}
