# MarketSystem

DataBase:

During the intialization of the system we connect the data base. 

- In case of succeses we continue to the next step of the initaliztion system.   
- In case of failure we abourt and return an error massege.
 
 
 State init file: "Boot file version 3"
 
 - **register(String email, String userName, String firstName, String lastName, String password)** : Register a visitor to the system with the given user-name, first-name, last-name, email and password.
 - **login(String userName, String password)** : Logging into the system for the given user name and password. need to be register to the system and user-name, password to be correct for eachother.
 - **addNewStore(User owner, String storeName)** : Create a new store, the founder will be the User in the input and the store name is the second input.
 - **addManager(User owner, User manager, int storeId)**  : Adding a manager to an existing store, the founder/ owner with premitions should be logged in and the appointee manager should be ragister to the system.
 - **addOwner(User owner, User newOwner, int storeId)** : Adding an owner to an existing store, the founder/ owner with premitions should be logged in and the appointee owner should be ragister to the system.
 - **addItemToStore(int storeId, String name, String category, double price, int amount)** : Adding an item to a pre-existing store, given a Category, price(non-negative) and amount(non-negative).
 - **searchProducts(String productName, String category, List keywords)** : 
   - product name: a specific name. Return products with the given name.
   - Category: string reprasenting a category. Return all the items under the given category.
   - Keywords: List of keywords looking for in the existing items in the system.
 - **addDiscount(int storeId, double percentage)** : Adding a discount for an existing store.
 - **addPolicy(int storeId, int hour, Calendar date)** : Adding a policy for an existing store.
 - **addItemToCart(int storeId, int itemId, int amount)** : Adding an existing item (by id) to an existing store (by id) in the system for the given amount(non-negative).
 - **setItemRating(int storeId, int itemId, double rate)** : Add new rating for an item, update the existing rating according to new rate.
 - **closeStore(int storeId)** : Close an open store(by id).
 - **reopenStore(int storeId)** : Reopen a closed store.
