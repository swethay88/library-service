package library.books;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooksService {
    private Map<String, Book> allBooks = new HashMap<>();
    private UserService userService;
    private Map<String, List<CheckedoutBook>> displayCheckedoutBooks = new HashMap<>();

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void addBook(String email, String title, String author, String genre, int noOfCopies) {
        if (!userService.doesUserExist(email)) {
            throw new UserDoesNotExistException("Invalid user");
        }
        Book book = new Book(title, author, genre, noOfCopies);
        allBooks.put(title, book);
    }

    public Book searchBook(String email, String title) {
        if (!userService.doesUserExist(email)) {
            throw new UserDoesNotExistException("Invalid user");
        }
        return allBooks.get(title);
    }

    public void deleteBooks(String title, String author, String genre, int noOfCopies) {
        Book book = new Book(title, author, genre, noOfCopies);
        if (allBooks.containsKey(title)) {
            allBooks.remove(title);
        } else {
            throw new BookTitleNotAvailableException("The book title is not available");
        }
    }

    public Map<String, List<CheckedoutBook>> checkoutBook(String email, String title, Instant checkedoutDate) {
        //email check
        if (!userService.doesUserExist(email)) {
            throw new UserDoesNotExistException("Invalid user");
        }
        //title check
        if (!allBooks.containsKey(title)) {
            throw new BookTitleNotAvailableException("The book title is not available");
        }
        //copy avaialable check
        if (allBooks.get(title).getNoOfCopies() == 0) {
            throw new BookCopyNotAvailableException("This book copy is not available to checkout in the library");
        }
        CheckedoutBook checkedoutBook = new CheckedoutBook(email, title, checkedoutDate);
        List<CheckedoutBook> checkedoutBookList = new ArrayList<>();
        int nCopies = allBooks.get(title).getNoOfCopies() - 1;
        Book book = allBooks.get(title);
        book.setNoOfCopies(nCopies);
        displayCheckedoutBooks.put(email, checkedoutBookList);
        return displayCheckedoutBooks;
    }
}