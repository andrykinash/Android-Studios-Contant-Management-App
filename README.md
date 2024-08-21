# Contact Management System - Android Application

## Overview
This Android application is a robust contact management system that supports user authentication, contact storage, and retrieval. It allows users to create an account, log in, add new contacts, view a list of contacts, and see detailed information about each contact. Contacts are unique to each individual user. The application securely stores user credentials and contact details in a local SQLite database, using an SHA-256 encryption algorithm to ensure privacy and data integrity.

## Features
- **User Authentication:** Secure login and registration system with SHA-256 password hashing.
- **Contact Management:** Add, view, update, and delete contacts.
- **SQLite Database:** Efficient storage and retrieval of user and contact data.
- **Exception Handling:** Robust error handling across the application to ensure smooth operation.
- **User Experience:** Simple, intuitive user interface designed for ease of use.

## Application Structure (Path= app\src\main\java\com\example\hw2)

### 1. LoginActivity.java
**Purpose:** Manages user login functionality, including authentication, registration, and the option to remember login details.  
**Key Features:**
- `authenticateUser(String username, String password)`: Authenticates user credentials against the database.
- `saveLoginDetails(String username, String password)`: Saves login credentials to `SharedPreferences` for auto-login.
- `clearLoginDetails()`: Clears saved login details from `SharedPreferences`.

### 2. RegistrationActivity.java
**Purpose:** Handles user registration, including username and password input.  
**Key Features:**
- Validates user input and ensures username uniqueness.
- Inserts new user data into the SQLite database.

### 3. MainActivity.java
**Purpose:** Displays the list of contacts for the logged-in user. Allows navigation to add new contacts and delete existing contacts.  
**Key Features:**
- `loadContacts(long userId)`: Fetches and displays contacts associated with the logged-in user.
- Provides a feature to delete contacts via a long press on the contact item.

### 4. SecondActivity.java
**Purpose:** Allows users to enter details for a new contact.  
**Key Features:**
- Validates input fields and saves contact details to the database.

### 5. ThirdActivity.java
**Purpose:** Displays detailed information for a selected contact.  
**Key Features:**
- Retrieves and displays contact details from the database based on the contact ID passed from `MainActivity`.

### 6. DBAdapter.java
**Purpose:** Manages all database interactions, including user and contact operations.  
**Key Features:**
- User-related methods: `insertUser(String username, String password)`, `checkUser(String username, String password)`
- Contact-related methods: `insertContact(String name, String phone, String email, long userId)`, `getAllContacts(long userId)`
- `hashPassword(String password)`: Implements SHA-256 hashing for secure password storage.

## XML Layout Files (Path= app\src\main\res\layout)
### Key XML Layouts
- `activity_login.xml`: Layout for the login screen.
- `activity_registration.xml`: Layout for the registration screen.
- `activity_main.xml`: Main screen showing the list of contacts.
- `activity_second.xml`: Screen for adding a new contact.
- `activity_third.xml`: Screen for displaying detailed contact information.

## Technical Details
### Security
- **Password Security:** Utilizes SHA-256 cryptographic hash function for secure password storage, making it practically impossible to reverse the hash back to the original password.

### Database Management
- **SQLite Database:** Efficiently handles user and contact data storage, providing fast retrieval and manipulation.

### Error Handling
- **Robust Error Handling:** Extensive use of exception handling ensures that the application operates smoothly, even under unexpected conditions.
### Demonstration

![Screenshot (2219)](https://github.com/user-attachments/assets/fc8ae510-e54b-4946-a817-5ade1b2abede)


![Screenshot (2220)](https://github.com/user-attachments/assets/dd695ddf-f0c3-4b73-8301-6ca445fc8b4c)


![Screenshot (2221)](https://github.com/user-attachments/assets/0cb903d5-566b-493a-a0f9-346dfe3bed4d)

