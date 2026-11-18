# My Digital Bookshelf

## Team Information

### Team Members
- Allison Au
- Evana Lai
- Wineta Pangestu
- Annie Song
- Lanqing Liu

---
## Project Description

### Domain: Organization

BookShelf is a personal book-tracking and library management application that allows users to discover, review, and organize books they have read.

Using the Open Library API, users can search for books by title and view information about different books, including their title, author, publication year, and cover image. Users can select books to add to their personal bookshelf, assign ratings, and write reviews or comments to record their reading experiences.

The application provides a visual representation of the user's bookshelf, allowing them to browse their collection and sort books by different attributes such as title, author, rating, genre, and date added. Users can also manually rearrange the order of books to personalize their bookshelf.

The goal of BookShelf is to provide users with an interactive and customizable space to document and organize their reading experiences.

---

## User Stories

### User Story 1: Search

As a user, I want to search for books by title and view their details so that I can discover books and add them to my personal bookshelf.

### User Story 2: Rate and Review Books

As a user, I want to rate books and write reviews or comments so that I can record and reflect on my reading experiences.

### User Story 3: Add Book to Shelf
As a user, I want to add books to my personal bookshelf so that I can maintain a collection of books I have read.

### User Story 4: Delete Book from Shelf
As a user, I want to delete books from my shelf as a way of managing what I own.

### User Story 5: View and Organize Bookshelf

As a user, I want to view my personal bookshelf in a visual layout and sort my books by different attributes so that I can easily browse and organize my collection.

---

## Use Cases

### Use Case 1: Search for Book

- User enters a book title into the search bar.
- System sends a search request to the Open Library API.
- System displays matching books and their title, author, and cover.
- User selects a book to view its details.

### Use Case 2: Rate and Review Books

- User selects a book through search or bookshelf.
- User assigns a numerical rating (e.g., 1–5 stars).
- User writes a review or comment.
- System saves the rating and review alongside the book.
- User can view their existing rating and review.

### Use Case 3: Add Book

- User chooses to add the book to their personal bookshelf.
- System saves the book to the user's library and prevents duplicate entries.
- Takes information from API, rating, review, and date added.
- Book is displayed on bookshelf.

### Use Case 4: Delete Book
- User selects a book they want to remove.
- User chooses the remove option.
- System removes the selected book from their bookshelf.
- User cannot remove book that was not on shelf previously.

### Use Case 5: View and Organize Bookshelf

- User navigates to their bookshelf.
- System retrieves the user's saved books.
- System displays the books in a visual bookshelf layout, including their cover images.
- User selects a sorting option.
- Available sorting options include:
  - Title (alphabetical order)
  - Author (alphabetical order)
  - Rating (highest to lowest or lowest to highest)
  - Genre
  - Date added (most recent first or oldest first)
- System rearranges the displayed books according to the selected criteria.


---

## Minimal Viable Product (MVP)

The MVP will focus on implementing the essential functionality needed for users to discover books, maintain a personal library, and document their reading experiences.

| Team Member | MVP Use Case |
|-------------|--------------|
| Lanqing | Search for books using the Open Library API |
| Lanqing| View book details and retrieve book cover images |
| Allison | Add boioks to the personal bookshelf |
| Wineta | Remove books from the personal bookshelf|
| Evana | Rate and review books |
| Annie | Display the bookshelf and implement sorting by title, author, and rating |
| Wineta | Manage bookshelf data and integrate the application's functionality |

**Post-MVP Features:**
- Sorting by genre and date added.
- Custom bookshelf ordering through drag-and-drop functionality.
- Additional visual bookshelf customization.

---

## Entities

### Book
- Book ID
- Title
- Author
- ISBN
- Genre
- Publication Year
- Cover Image
- Open Library ID
- Date Added

### User
- User ID
- Personal Bookshelf (collection of books)

### Review
- Review ID
- Book ID
- Rating
- Comment

---

## API Usage

**API Name:** Open Library API

**Documentation:** https://openlibrary.org/developers/api

**How it is used in the program:**

The Open Library API will be used to search for books and retrieve relevant book information.

- The Search API will retrieve books matching the user's search query.
- Book metadata will be used to display information such as title, author, publication year, and ISBN.
- The Covers API will be used to retrieve book cover images.
- Retrieved book information will be stored locally in the application's database alongside user-generated ratings and reviews.

---

## Design Diagrams

### UML Class Diagram

![UML Diagram.png](docs/UML%20Diagram.png)

### Sequence Diagrams

(To be added)

---

## How to Run

(To be added once the development environment, dependencies, and setup instructions have been finalized.)

---

## Notes

- Open Library will serve as the external source for book information.
- User-generated information, including ratings and reviews, will be stored within the application.
- Each book should have a unique identifier (preferably its Open Library Work ID or edition ID) to prevent duplicate entries.
- The personal bookshelf will contain only books the user has read.
- Each book can have one associated rating and review.
- Custom bookshelf ordering will be considered a post-MVP feature.