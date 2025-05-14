# SteadyLeafs
## online library

This is a Java web application I built with Spring Boot and Thymeleaf. Members can browse and borrow books and librarians manage the books and the members. The login functionality is differentiated by roles, so members, librarians and admin each see their own custom dashboard with actions they are allowed to do.  

## Screenshots
### Home Page
![Home Page](screenshots/appHomePage.png)

### Member Login Page
![Member Login Page](screenshots/loginMember.png)

### Member Dashboard
![Member Dashboard](screenshots/memberDashboard.png)

### Librarian Dashboard
![Librarian Dashboard](screenshots/librarianDashboard.png)

## Features
- Member registration and login;
- Role-based access: member/librarian|admin;
- Member's panel - view available and borrowed books;
- Librarian's panel for managing books and members;
- Admin panel for managing users and librarians;
- Responsive frontend with bootstrap;


- **Member can:**
  - View all books;
  - Borrow available books (one at a time);
  - View their borrowed books;
- **Librarian can:**
  - Create books;
  - View and manage all books / loaned books / available books;
  - Check book statistics (by category and quantity);
  - View and manage members;
  - View a member's book list;
- **Admin can:**
  - View and manage users;
  - Change a user's role;
  - View and delete librarians;
  

- When created or updated, a book receives its cover from OpenLibrary external API;
- When a member is deleted, the corresponding user is also removed, and their borrowed books are returned to the library as available.

## Tech Stack
- Java 21
- Spring Boot
- PostgreSQL
- Thymeleaf
- Bootstrap


## To Do
- [ ] Confirm book creation + show spinner;
- [ ] Add pagination to book list;
- [ ] Implement search functionality in the book list (by title or author);