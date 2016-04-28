This is the readme for the enhanced version of item recommendation.

On top of what has been built for the first version, few other features are added: cosine recommendation method, baseline predictor, reads both dat and csv file, performance improvement.

1. ReadData class: the readRating method prompts user to enter which category they would like to get recommendation on, and the system will open the file and read the file accordingly. Instead of using Scanner, it uses bufferedReader so it could read the file faster. The line is splited based on what delimiters it detects, and it treat all itemID as String instead of integer since some of those contain letters. 

2. User Class: add the cosineSimilarity method that takes in a user and returns the similarity score between the two using cosine method. 

3. Universe Class: 
Method added: baselinePrediction: takes in a user and an item, returns the prediction score using the baseline prediction technique.
              predictionCosine: takes in a user and an item, returns the prediction score by getting the neighbor list from kNNCosine
              userBaseline: takes in a user, returns the b_u value of the user.
              recommendationList: prompts the user to enter which recommendation technique he would like to use, and return the list of recommended item based on the method. 

4. Tester: The way I test which method works better is getting the prediction score of an existing item on an user list using the three different methods, and see which one is the closest to the user rating of that item. 

5. Performance improvement: I use bufferedReader instead of scanner to read the file faster. In my original design, I have chosen hashmap over arraylist to improve the performance so I cannot think of anything else I could do to improve it further. 