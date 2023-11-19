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


## Data Flow


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
