# SteadyLeafs
## ~online library~

A web app built with Java, Spring Boot and Thymeleaf where members can borrow books and librarians manage the system.

## Features
- Member registration and login;
- Role-based access: member / librarian|admin;
- Member's panel - view available and borrowed books;
- Librarian's panel for managing books and members;
- Admin panel for managing users and librarians;
- Responsive frontend with bootstrap;


- ️👤 Member can:
  - view all books;
  - borrow available books (one at the time);
  - view their borrowed books;
- 📚 Librarian can:
  - create books;
  - view and manage all books / loaned books / available books;
  - check book statistics (by category and quantity);
  - view and manage members;
  - view a member's book list;
- 🛠 Admin can:
  - view and manage users;
  - change a user's role;
  - view and delete librarians;
  

- When created or updated, a book receives its cover from OpenLibrary external API;
- When a member is deleted, the corresponding user is also removed, and their borrowed books are returned to the library as available;

## Tech Stack
- Java 21
- Spring Boot
- PostgreSql
- Thymeleaf
- Bootstrap


## To Do
- [ ] Confirm book creation + show spinner;
- [ ] Add pagination to book list;
- [ ] Implement search functionality in the book list (by title or author);