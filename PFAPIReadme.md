# Personal Finance API

## Overview

Welcome to the Personal Finance API! 
<p>This API is designed to help users manage their personal finances by allowing them to log expenses and create budgets. 
<p>It allows users to track and analyze financial transactions, helping users gain insights into their spending habits and stay on top of their budgeting goals.

## Getting Started

To use the Personal Finance API locally, follow these steps:

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/personal-finance-api.git
```

### 2. Set Up MySQL Database

- Create a MySQL database for the Personal Finance API.

```sql
CREATE DATABASE personal_finance_db;
```

- Open `src/main/resources/application.properties` and update the following properties with your MySQL configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/personal_finance_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

### 3. Build and Run the Application

```bash
./mvnw clean install
./mvnw spring-boot:run
```

The API will be accessible at `http://localhost:8080/swagger-ui/index.html#/`.

## Schema Overview



### Entities

#### 1. Base Entity

The `BaseEntity` class serves as the foundation for other entities within the application, providing common parameters and behaviors. Entities that extend BaseEntity inherit the common fields (Id, createdAt, updatedAt) and behaviors.

#### 2. User

The `User` entity represents a user in the system. It includes information such as the user's name, username, email, role and  associated budgets and expenses.
The user has a one-to-many relationship with both the Budget and Expenses entities.

#### 3. Expense

The `Expense` entity captures represents individual expenses logged by users in the system. It includes details such as the amount, category, subcategory, description, and associations with users and budgets.
The expense has a many-to-one relationship with both the Budget and User entities.

#### 4. Budget

The `Budget` entity represents a budget set by a user. It includes information such as the budget amount, category, subcategory, and associated expenses and user.
The budget has a many-to-one relationship with the User entity and a one-to-many relationship with Expenses entity, allowing multiple expenses to be associated with a single budget.


#### 5. SubCategories Enum

The `SubCategories` enum is a comprehensive representation of various categories and subcategories related to the expense and budget entities. 

##### Categories

The `SubCategories` enum is organized into the following categories:

- **Food**
- **Transport**
- **Utilities**
- **Housing**
- **Entertainment**
- **Shopping**
- **Savings**

Each category encompasses various subcategories to provide a detailed breakdown of expenses or budgets.

The **Food** category has the following subcategories:
**Groceries**, **Restaurant**, **OnlineOrder**

The **Transport** category has the following subcategories: 
**PublicTransport**, **CarHailing**, **Fuel**, **CarPayments**, **CarInsurance**

The **Utilities** category has the following subcategories:
**Water**, **Electricity**, **Gas**, **Interne:**

The **Housing** category has the following subcategories:
**Rent**, **MortgagePayment**, **CouncilTax**, **Repairs**

The **Entertainment** category has the following subcategories:
**TVLicense**, **Streaming**, **Music**, **Outside** 

The **Shopping** category has the following subcategories:
**Gifts**, **Personal**, **Clothes**

The **Savings** category has the following subcategories:
**Investments**, **Basic**


### DTOs (Data Transfer Objects)

DTOs are used to transfer data between different layers of the application.

#### 1. UserDTO

`UserDTO` is a Data Transfer Object for the `User` entity. It is used to transfer user-related information, excluding sensitive details, between different parts of the application.

#### 2. ExpenseDTO

`ExpenseDTO` is a Data Transfer Object for the `Expense` entity. It facilitates the transfer of expense-related information.

#### 3. BudgetDTO

`BudgetDTO` is a Data Transfer Object for the `Budget` entity. It simplifies the transfer of budget-related data within the application.


#### 4. Summary

The `Summary` entity provides an overview of financial information, consolidating data related to expenses and budgets for a user.

#### 5. BudgetSummary

The `BudgetSummary` offers a summarized view of budget-related information. It includes details about the budgets, the amount spent (From expenses), and the budgets.




## Data Flow
The Personal Finance API follows a structured flow for handling client requests. Below is an overview of how a typical request flows through the major components of the application.


![Data Flow Diagram](https://github.com/cbfacademy/java-rest-api-assessment-ehadsaloF/blob/main/Blank%20diagram-4.png)

1. **Controller:**
   - The process begins when a client sends an HTTP request to the appropriate endpoint.
   - The Controller receives the request, extracts relevant information, and prepares it for further processing.

2. **Service:**
   - The Controller delegates the request to the corresponding Service.
   - The Service contains business logic and acts as an intermediary between the Controller and Repository.
   - It performs additional processing or validation based on the request.

3. **Repository:**
   - The Service communicates with the Repository to perform database operations.
   - The Repository handles the interaction with the database, executing queries, and returning results to the Service.

4. **Database:**
   - The Repository interacts with the underlying database to retrieve or update data based on the request.
   - Database operations are executed, and the results are sent back to the Repository.

5. **Service:**
   - The Service receives the results from the Repository and processes them further if necessary.
   - It may apply additional business logic, transformations, or aggregations before returning the data.

6. **Controller:**
   - The Controller receives the final response from the Service.
   - It converts the raw data, if needed, into Data Transfer Objects (DTOs) to shape the response in a client-friendly format.

7. **Client Response:**
   - The Controller sends the response, in the form of DTOs, back to the client.



## Endpoints
There are four controllers in this project with different endpoints

### 1. Admin Controller

The Admin Controller in the Personal Finance API provides endpoints for managing user-related operations. 
This includes finding users by email or username, retrieving a list of all users, and downloading all users as a JSON file.


#### - Find User by Email or Username - GET
- **Endpoint:** /PF/admin/find/{emailOrUsername}
- **Description:**: Returns details of a user based on the provided email or username.

#### - Get All Users - GET
- **Endpoint:** /PF/admin/getAllUsers
- **Description:** Returns a list of all users with their respective details.

#### - Download All Users as JSON - GET
- **Endpoint:** /PF/admin/getAllUsers/download
- **Description:** Initiates the download of a JSON file containing information about all users.


#### _Error Handling_

- **_404_ Not Found:** If the requested user or users are not found.
- **_500_ Internal Server Error:** If an error occurs while processing a request.


### 2. User Controller

The User Controller in the Personal Finance API manages user-specific operations, allowing users to create accounts, retrieve user details, obtain a summary of expenses and budgets, update user information, and delete user accounts.

#### - Create a User - POST
- **Endpoint:** `/PF/addUser`
- **Description:** Creates a new user with the provided name, username, email, and password.

#### - Get a User - GET
- **Endpoint:** `/PF/user/{emailOrUsername}`
- **Description:** Retrieves user details based on the provided username or email.

#### - Get User Expense and Budget Summary - GET
- **Endpoint:** `/PF/user/{emailOrUsername}/summary`
- **Description:** Retrieves a summary of the user's expenses and budgets.

#### - Update a User - PUT
- **Endpoint:** `/PF/user/{emailOrUsername}/update`
- **Description:** Updates the user's name based on the provided username or email.

#### - Delete User - DELETE
- **Endpoint:** `/PF/user/{emailOrUsername}/delete`
- **Description:** Deletes the user based on the provided username or email.

#### _Error Handling_

- **_404_ Not Found:** If the requested user does not exist.

- **_500_ Internal Server Error:** If an error occurs while processing a request.

  

### 3. Budget Controller

The Budget Controller in the Personal Finance API provides endpoints for managing budget-related operations. This includes creating, updating, retrieving, and deleting budgets for a user.

#### - Create a Budget - POST
- **Endpoint:** /PF/user/{usernameOrEmail}/budget/create
- **Description:** Creates a new budget for the specified user with the provided details.

#### - Update a Budget - PATCH
- **Endpoint:** /PF/user/{usernameOrEmail}/budget/update/{budgetId}
- **Description:** Updates the specified budget details(amount, category, subcategory, description, or budget)for the user based on the provided parameters.

#### - Get a Budget by ID - GET
- **Endpoint:** /PF/user/{usernameOrEmail}/budget/getByID/{budgetId}
- **Description:** Retrieves a budget with the specified ID.

#### - Get a Budget by Category - GET
- **Endpoint:** /PF/user/{usernameOrEmail}/budget/getByCat/{category}
- **Description:** Retrieves a list of budgets with the specified category.

#### - Get Budgets within a Price Range - GET
- **Endpoint:** /PF/user/{usernameOrEmail}/budget/getByAmount
- **Description:** Retrieves a list of budgets within the specified price range.

#### - Get Budgets Greater than an Amount - GET
- **Endpoint:** /PF/user/{usernameOrEmail}/budget/getByAmount/>
- **Description:** Retrieves a list of budgets greater than the specified amount.

#### - Get Budgets Less than an Amount - GET
- **Endpoint:** /PF/user/{usernameOrEmail}/budget/getByAmount/<
- **Description:** Retrieves a list of budgets less than the specified amount.

#### - Get Budgets Created within a Date Range - GET
- **Endpoint:** /PF/user/{usernameOrEmail}/budget/getByDate
- **Description:** Retrieves a list of budgets created within the specified date range.

#### - Get Budgets Created Before a Date - GET
- **Endpoint:** /PF/user/{usernameOrEmail}/budget/getByDate/before
- **Description:** Retrieves a list of budgets created before the specified date.

#### - Get Budgets Created After a Date - GET
- **Endpoint:** /PF/user/{usernameOrEmail}/budget/getByDate/after
- **Description:** Retrieves a list of budgets created after the specified date.

#### - Get All Budgets - GET
- **Endpoint:** /PF/user/{usernameOrEmail}/budget/getAll
- **Description:** Retrieves a list of all budgets for the user.

#### - Get All Budgets as JSON - GET
- **Endpoint:** /PF/user/{usernameOrEmail}/budget/getAll/download
- **Description:** Initiates the download of a JSON file containing information about all budgets for the user.

#### - Sort Budgets by Specified Criteria - GET
- **Endpoint:** /PF/user/{usernameOrEmail}/budget/sort/{sortBy}
- **Description:** Retrieves a sorted list of budgets based on the specified criteria (amount, category, subcategory, or date).

#### - Delete Budget - DELETE
- **Endpoint:** /PF/user/{usernameOrEmail}/budget/delete/{budgetId}
- **Description:** Deletes the specified budget.

#### _Error Handling_

- **_404_ Not Found:** If the requested user, or budgets are not found.
- **_422_ Unprocessable Entity:** If there are invalid parameters in the request.
- **_500_ Internal Server Error:** If an error occurs while processing a request.


### 3. Expenses Controller

The Expenses Controller in the Personal Finance API manages operations related to user expenses, allowing users to create, update, retrieve, and delete expenses. Additionally, users can retrieve expense details based on various criteria, such as category, amount range, date range, and more.

#### - Create an Expense - POST
- **Endpoint:** `/PF/user/{usernameOrEmail}/expenses/create`
- **Description:** Creates a new expense with the provided amount, category, subcategory, and description.

#### - Create an Expense Connected to a Budget - POST
- **Endpoint:** `/PF/user/{usernameOrEmail}/expenses/create/{budgetId}`
- **Description:** Creates a new expense connected to a specific budget with the provided amount, category, subcategory, and description.

#### - Update an Expense - PATCH
- **Endpoint:** `/PF/user/{usernameOrEmail}/expenses/update/{expensesId}`
- **Description:** Updates the specified fields (amount, category, subcategory, description, or budget) of an expense.

#### - Get an Expense by ID - GET
- **Endpoint:** `/PF/user/{usernameOrEmail}/expenses/getByID/{expensesId}`
- **Description:** Retrieves details of an expense based on the provided expense ID.

#### - Get Expenses Associated With a Budget - GET
- **Endpoint:** `/PF/user/{usernameOrEmail}/expenses/getByBudget/{budgetId}`
- **Description:** Retrieves expenses associated with a specific budget.

#### - Get Expenses by Category - GET
- **Endpoint:** `/PF/user/{usernameOrEmail}/expenses/getByCat/{category}`
- **Description:** Retrieves expenses based on the specified category.

#### - Get Expenses within a Price Range - GET
- **Endpoint:** `/PF/user/{usernameOrEmail}/expenses/getByAmount`
- **Description:** Retrieves expenses within the specified price range.

#### - Get Expenses Greater than an Amount - GET
- **Endpoint:** `/PF/user/{usernameOrEmail}/expenses/getByAmount/>`
- **Description:** Retrieves expenses greater than the specified amount.

#### - Get Expenses Less than an Amount - GET
- **Endpoint:** `/PF/user/{usernameOrEmail}/expenses/getByAmount/<`
- **Description:** Retrieves expenses less than the specified amount.

#### - Get Expenses Created within a Date Range - GET
- **Endpoint:** `/PF/user/{usernameOrEmail}/expenses/getByDate`
- **Description:** Retrieves expenses created within the specified date range.

#### - Get Expenses Created Before a Date - GET
- **Endpoint:** `/PF/user/{usernameOrEmail}/expenses/getByDate/before`
- **Description:** Retrieves expenses created before the specified date.

#### - Get Expenses Created After a Date - GET
- **Endpoint:** `/PF/user/{usernameOrEmail}/expenses/getByDate/after`
- **Description:** Retrieves expenses created after the specified date.

#### - Get All Expenses - GET
- **Endpoint:** `/PF/user/{usernameOrEmail}/expenses/getAll`
- **Description:** Retrieves all expenses associated with the user.

#### - Get All Expenses as JSON - GET
- **Endpoint:** `/PF/user/{usernameOrEmail}/expenses/getAll/download`
- **Description:** Initiates the download of a JSON file containing information about all user expenses.

#### - Sort Expenses by Specified Criteria - GET
- **Endpoint:** `/PF/user/{usernameOrEmail}/expenses/sort/{sortBy}`
- **Description:** Sorts expenses based on the specified criteria (amount, category, subcategory, or date).

#### - Delete Expense - DELETE
- **Endpoint:** `/PF/user/{usernameOrEmail}/expenses/delete/{expensesId}`
- **Description:** Deletes the specified expense based on the provided expense ID.

#### _Error Handling_

- **_404_ Not Found:** If the user or expense does not exist, or if no expenses match the specified criteria.

- **_422_ Unprocessable Entity:** If there are invalid parameters in the request.

- **_500_ Internal Server Error:** If an error occurs while processing the request.

- 
