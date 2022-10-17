## DISCLAIMER!
This application was created during the bootcamp for backend development at IronHack and it's purpose is only educational.<br>
The bootcamp lasted for 4 months and during this time we learned how to use Java, JPA, Maven, MySQL, BootSpring and KeyCloak to create backend solutions in form of APIs. This application is the result of my work on the final project for the bootcamp which we had to complete individually and for which we only had three weeks time to finish it. <br> 
The aplication had to resemble a Bank.

## Ports use in this App:
Application: 4041<br>
Keycloack: 8081<br>
MySql: 3306<br>

# IRONBANK
## Requirements
1- The system must have 4 types of accounts: StudentChecking, Checking, Savings, and Credit Account.<br>
2- The system must have 3 types of Users: Admins, AccountHolders and ThirdParty.<br>
3- Admins can create new accounts. When creating a new account they can create Checking, Savings, or Credit Accounts.<br>

## Extra information on the requirements:
### Checking Accounts should have:
A balance <br>
A secretKey<br>
A PrimaryOwner<br>
An optional SecondaryOwner<br>
A minimumBalance<br>
A penaltyFee<br>
A monthlyMaintenanceFee<br>
A creationDate<br>
A status (FROZEN, ACTIVE)<br>

### Student Checking Accounts are identical to Checking Accounts except that they do NOT have:
A monthlyMaintenanceFee<br>
A minimumBalance<br>

### Savings are identical to Checking accounts except that they
Do NOT have a monthlyMaintenanceFee<br>
Do have an interestRate<br>

### Credit Accounts have:
A balance<br>
A PrimaryOwner<br>
An optional SecondaryOwner<br>
A creditLimit<br>
An interestRate<br>
A penaltyFee<br>

### Admins: 
should be able to access the balance for any account and to modify it.<br>

### AccountHolders: 
should be able to access their own account balance. They should also be able to transfer money from any of their accounts to any other account (regardless of owner). The transfer should only be processed if the account has sufficient funds.<br>

### Third-Party Users: 
There must be a way for third-party users to receive and send money to other accounts. Third-party users must be added to the database by an admin. In order to be able to receive and send money, Third-Party Users must provide their hashed key. They also must provide the amount, the Account id and the account secret key.
<br>
There where more requirements but this where the most important ones.

## Main Technologies Used:
For this project I used IntelliJ IDEA to create the code and Postman to access the APIs. <br>
- JAVA <br>
- MySQL <br>
- SpringBoot <br>
- KeyCloak <br>

## Structure
At the beginning this is the Structure I used to organize the code. The endresult is not exactly organized in this way, but this did help me to have a solid structure of what and how I wanted my code to be created.
![IronBank drawio](https://user-images.githubusercontent.com/69363801/196229386-18c34b22-3b43-4a4e-92d1-942a1dafc360.png)

# Some Images of the Results

## Admin creating an User:
![Captura de Pantalla 2022-10-17 a las 18 24 53](https://user-images.githubusercontent.com/69363801/196232099-ada5c117-4e83-44e6-bd82-322bc06fd34e.png)

## Admin finding User by username:

![Captura de Pantalla 2022-10-17 a las 18 25 38](https://user-images.githubusercontent.com/69363801/196232230-59668cfa-7ebe-4338-a56a-eb119ef9c17a.png)

## User geting Token:

![Captura de Pantalla 2022-10-17 a las 18 26 19](https://user-images.githubusercontent.com/69363801/196232329-4584c2e2-0748-48f3-aff4-b9b7bd4fa5b0.png)

## User getting only his information by token:

![Captura de Pantalla 2022-10-17 a las 18 26 47](https://user-images.githubusercontent.com/69363801/196232452-2f51f2cb-f962-4d61-9c00-5642bd908b85.png)

## Admin creating an account for an User using the User-ID:

![Captura de Pantalla 2022-10-17 a las 18 27 50](https://user-images.githubusercontent.com/69363801/196232565-91c254a4-2e4d-4dd0-bca8-41f320c1963c.png)

## User viewing his account information: 


![Captura de Pantalla 2022-10-17 a las 18 28 02](https://user-images.githubusercontent.com/69363801/196232672-af5cdea3-0487-4e54-8f91-58ee99e53e88.png)






