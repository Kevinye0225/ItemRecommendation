This is readme file for itemRecommendation.

Change in design:
I added a readData class in addition to my original design, as it should be separated from the main universe class due to its different behavior. Also, I change the data structure I used to store, the reason I choose HashMap(Id, Item/User) is that it is easier to look for an existing user/item, so adding rating is faster. 
The User and Item class implement comparator interface and have it compared by id, so they could be found on the hashmap by its id. 

ReadData class:
Instance variable: HashMap<Integer, Item> items matches item id with item, and it keeps getting modified(addRating) as the scanner is going through the rating documents. HashMap<Integer, User> users matches user id with user, and it keeps getting modified(addRating) as the scanner is going through the rating dats. Both of them are made public so it is directly accessible by the Universe class. Scanner in is the scanner it used to scan the documents. 

readMovie goes through the movies file and store movies into the items hashmap. 
readRating goes through the rating file, it store the users into the users hashmap, and keeps updating both items and users.

User Class:
Instance variable: int id, it is an unique identifier for each user. int count, keep tracks of the total number of rating the user has. double totalScore, keep tracks of the total rating score of this user. ratedItems is a hashmap that maps Item with rating(double). movieId is a set that contains all the unique movie id. 

Constructor: takes in an int and set it to id. 
addRating method takes in an item and a double variable score, which gets put into the ratedItems, and also updates movieId, count and totalScore. 
getRating takes in an Item variable and returns the rating of this item from the user, it returns 0 if the user has not rated this item.
getAverageRating method returns average rating of the user, it returns 0 if the user has not rated anything.
pearsonCorrelation method takes in a User and returns the correlation value of the user.
It also implements comparator interface and sets the comparator method to compare by its id. 


Item Class:
Instance variable: int id, it is an unique identifier for each item. int count, keep tracks of the total number of rating the item has. double totalScore, keep tracks of the total rating score of this item. rating is a hashmap that maps users with their rating of the movie. 

Constructor takes in a String name and an int id, which initalized movie name and movie id. 
addRating takes in a user and a Double score, which gets put into rating, count and totalScore gets updated as well.
getAverageRating returns average rating of this movie.
getRating takes in a User and returns the rating from this user, it returns 0 if it has not been rated by this user.
It also implements comparator interface and sets the comparator method to compare by its id. 


Universe Class:
Instance variable: data is a readData class, users is a hashmap that maps userID with users, items is a hashmap that maps itemId with items. It also has a final variable kNeighbor which is the number of neighbors that will be used in calculating the recommendation score, it is set to 20. 

Constructor takes in no argument, calls readMovie and readRating method on data. users is set to users from data, items is set to items from data.

recommendList takes in a user and return a list of top recommended movies. It started with asking user's input on how many recommended items to be shown. res is the final output. resValue is a list that contains the top prediction Score so it could keep track of duplicate prediction value. itemScore is a hashmap that maps prediction value with a list of itemId. unwatchedItem is list of prediction score of unrated items from the user. 

prediction takes in a user and an item, returns the prediction value of the item for this user. 

kNearestNeighbor takes in an int k, a User and an item, the logic is same as the recommendList, it traverse through every user who has rated the item, and returns a list of users that are most similar. 


