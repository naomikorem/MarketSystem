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

- defaultAdminUser="admin"
- defaultAdminPassword="admin"
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
 
 
