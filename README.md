# MarketSystem

DataBase:

During the intialization of the system we connect the data base. 

- Data base info :  
  - ***server*** : sql11.freemysqlhosting.net
  - ***port*** : 3306
  - ***name*** : sql11498449
  
  - hibernate.dialect : org.hibernate.dialect.MySQLDialect
  - hibernate.connection.driver_class : com.mysql.jdbc.Driver

- In case of succeses we continue to the next step of the initaliztion system.   
- In case of failure we abourt and return an error massege.

Creating a default Admin user:

- defaultAdminUser="Admin1"
- defaultAdminPassword="Admin"
- defualtAdminFirstName="Admin"
- defualtAdminLastName="Admin"
- defualtAdminEmail="Admin@mycompany.com"
 
 External services:
 
- **handshake**:

  > This action type is used for check the availability of the external systems.
  > 
  > action_type = handshake
  > 
  > Additional Parameters: none
  > 
  > Output: “OK” message to signify that the handshake has been successful

- **pay**:
  > This action type is used for charging a payment for purchases.
  > 
  > action_type = pay
  > 
  > Additional Parameters: card_number, month, year, holder, ccv, id
  > 
  > Output: 
  >   transaction id - an integer in the range [10000, 100000] which indicates a transaction number if the transaction succeeds or -1 if the transaction has failed.
  
- **cancel_pay**:
  > This action type is used for cancelling a payment transaction.
  > 
  > action_type = cancel_pay
  > 
  > Additional Parameters: 
  >   transaction_id - the id of the transaction id of the transaction to be canceled.
  >
  > Output: 
  >   1 if the cancelation has been successful or -1 if the cancelation has failed.

- **supply**:
  > This action type is used for dispatching a delivery to a costumer.
  > 
  > action_type = supply
  >
  > Additional Parameters: name , address, city, country, zip
  >
  > Output: 
  >   transaction id - an integer in the range [10000, 100000] which indicates a transaction number if the transaction succeeds or -1 if the transaction has failed.

- **cancel_supply**:
  > This action type is used for cancelling a supply transaction.
  >
  > action_type = cancel_supply
  > 
  > Additional Parameters: 
  >   transaction_id - the id of the transaction id of the transaction to be canceled.
  > 
  > Output: 1 if the cancelation has been successful or -1 if the cancelation has failed.
 
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
