import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

public class UniverseTester {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Universe test = new Universe();
		System.out.println("Read success");
//		System.out.println(movieReader.users.get(276727).getAverageRating());
		
//		System.out.println(tester.getItemList().size());
//		test.readMovie();
//		User u1 = new User(5);
//		test.items.get("3499225905").addRating(u1, 1.0);
		// movies set test cases
		User t1= test.getUserList().get(1);
//		test.recommendList(t1);
		Set<Item> set = t1.getItemList().keySet();
		Iterator<Item> iterator = set.iterator();
		Item item = iterator.next();
		double cosine = Math.abs(test.predictionCosine(t1, item) - t1.getRating(item));
		double pearson = Math.abs(test.prediction(t1, item) - t1.getRating(item));
		double baseline = Math.abs(test.baselinePrediction(t1, item) - t1.getRating(item));
		if (cosine < pearson && cosine < baseline){
			System.out.println("cosine is better");
		} else if (pearson < cosine && pearson < baseline){
			System.out.println("pearson is better"); 
		} else {
			System.out.println("baseline is better");
		}



	
	}

}
