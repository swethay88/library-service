package library.books;

import library.books.exceptions.BookCopyNotAvailableException;
import library.books.exceptions.BookNotCheckedoutException;
import library.books.exceptions.BookTitleNotAvailableException;
import library.books.exceptions.UserDoesNotExistException;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.*;

public class BookServiceTest {

    private static final String USER1 = "user1@gmail.com";
    
    private LibraryService service;

    @Before
    public void setUp(){
       service = new LibraryService();
        service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, USER1);
    }
    
    //User signedup, added a book, checkedout the added book
    @Test
    public void chekoutBookSimpleTest() {
        service.getBookService().addBook(USER1, "Chicken Squad", "Cronin", "fiction", 2);
        Instant currentTime = Instant.now();
        CheckedoutBook c = service.getBookService().checkoutBook(USER1, "Chicken Squad", currentTime);
        //assert c variables
        assertEquals(c.getEmail(), USER1);
        assertEquals(c.getTitle(), "Chicken Squad");
        assertEquals(c.getCheckedoutDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList = service.getBookService().listOfCheckedoutBooks(USER1);
        assertEquals(checkedoutBookList.size(), 1);
    }

    //Test if user doesn't exist ie., if user didn't signup, but trying to checkout a book
    @Test
    public void chekoutBookUserDoesntExistTest() {
        try {
            service.getBookService().checkoutBook("user2@gmail.com", "Chicken Squad", Instant.now());
            fail("Expecting exception");
        } catch (UserDoesNotExistException ex) {
            // expected exception
            assertEquals("Invalid user", ex.getMessage());
        }
    }

    //Test if user doesn't exist ie., if user didn't signup, but trying to return a book
    @Test
    public void returnBookUserDoesntExistTest() {
        try {
            service.getBookService().returnBook("user2@gmail.com", "Chicken Squad", Instant.now());
            fail("Expecting exception");
        } catch (UserDoesNotExistException ex) {
            // expected exception
            assertEquals("Invalid user", ex.getMessage());
        }
    }

    //Test if book doesn't exist ie., if the user is trying to checkout a book which is not added in the library
    @Test
    public void titleDoesntExistTest() {
        try {
            service.getBookService().checkoutBook(USER1, "Chicken Squad", Instant.now());
            fail("Expecting exception");
        } catch (BookTitleNotAvailableException ex) {
            assertEquals("The book title is not available", ex.getMessage());
        }
    }

    //Test if the user is trying to checkout a book but copies of the book aren't available in the library
    @Test
    public void copiesNotAvailableTest() {
        service.getBookService().addBook(USER1, "Chicken Squad", "Cronin", "fiction", 1);
        service.getBookService().checkoutBook(USER1, "Chicken Squad", Instant.now());
        try {
            service.getBookService().checkoutBook(USER1, "Chicken Squad", Instant.now());
            fail("Expecting exception");
        } catch (BookCopyNotAvailableException ex) {
            assertEquals("This book copy is not available to checkout in the library", ex.getMessage());
        }
    }

    //checkedout books are null i.e., List is empty
    @Test
    public void noCheckedoutBooksTest() {
            List<CheckedoutBook> checkedoutBookList = service.getBookService().listOfCheckedoutBooks(USER1);
            assertNull(checkedoutBookList);
        }

    //one user checking out multiple books and returning a book
    @Test
    public void oneUserMultipleBooksCheckoutAndReturnTest() {
        service.getBookService().addBook(USER1, "Chicken Squad", "Cronin", "fiction", 2);
        Instant currentTime = Instant.now();
        CheckedoutBook c = service.getBookService().checkoutBook(USER1, "Chicken Squad", currentTime);
        assertEquals(c.getEmail(), USER1);
        assertEquals(c.getTitle(), "Chicken Squad");
        assertEquals(c.getCheckedoutDate(), currentTime);
        service.getBookService().addBook(USER1, "Petu Pumpkin Tiffin Thief", "Arundathi", "fiction", 3);
        CheckedoutBook ch = service.getBookService().checkoutBook(USER1, "Petu Pumpkin Tiffin Thief", currentTime);
        assertEquals(ch.getEmail(), USER1);
        assertEquals(ch.getTitle(), "Petu Pumpkin Tiffin Thief");
        assertEquals(ch.getCheckedoutDate(), currentTime);
        CheckedoutBook r = service.getBookService().returnBook(USER1, "Petu Pumpkin Tiffin Thief", currentTime);
        assertEquals(r.getEmail(), USER1);
        assertEquals(r.getTitle(), "Petu Pumpkin Tiffin Thief");
        assertEquals(r.getReturnDate(), currentTime);
        List<CheckedoutBook> returnedBookList = service.getBookService().listOfReturnedBooks(USER1);
        assertEquals(returnedBookList.size(), 1);
        List<CheckedoutBook> checkedoutBookList = service.getBookService().listOfCheckedoutBooks(USER1);
        assertEquals(checkedoutBookList.size(), 1);
    }

    //one user checking out multiple copies
    @Test
    public void oneUserMultipleCopiesCheckoutAndReturnTest() {
        service.getBookService().addBook(USER1, "Chicken Squad", "Cronin", "fiction", 3);
        Instant currentTime = Instant.now();
        CheckedoutBook c = service.getBookService().checkoutBook(USER1, "Chicken Squad", currentTime);
        assertEquals(c.getEmail(), USER1);
        assertEquals(c.getTitle(), "Chicken Squad");
        assertEquals(c.getCheckedoutDate(), currentTime);
        service.getBookService().addBook(USER1, "Chicken Squad", "Cronin", "fiction", 3);
        CheckedoutBook ch = service.getBookService().checkoutBook(USER1, "Chicken Squad", currentTime);
        assertEquals(ch.getEmail(), USER1);
        assertEquals(ch.getTitle(), "Chicken Squad");
        assertEquals(ch.getCheckedoutDate(), currentTime);
        service.getBookService().addBook(USER1, "Chicken Squad", "Cronin", "fiction", 3);
        CheckedoutBook che = service.getBookService().checkoutBook(USER1, "Chicken Squad", currentTime);
        assertEquals(che.getEmail(), USER1);
        assertEquals(che.getTitle(), "Chicken Squad");
        assertEquals(che.getCheckedoutDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList = service.getBookService().listOfCheckedoutBooks(USER1);
        assertEquals(checkedoutBookList.size(), 3);
        CheckedoutBook r = service.getBookService().returnBook(USER1, "Chicken Squad", currentTime);
        assertEquals(r.getEmail(), USER1);
        assertEquals(r.getTitle(), "Chicken Squad");
        assertEquals(r.getReturnDate(), currentTime);
        List<CheckedoutBook> returnedBookList = service.getBookService().listOfReturnedBooks(USER1);
        assertEquals(returnedBookList.size(), 1);
        List<CheckedoutBook> checkedoutBookList2 = service.getBookService().listOfCheckedoutBooks(USER1);
        assertEquals(checkedoutBookList.size(), 2);
    }

    //multiple users checking out different books
    @Test
    public void multipleUsersCheckingoutAndReturningDifferentBooksTest() {
        service.getBookService().addBook(USER1, "Chicken Squad", "Cronin", "fiction", 2);
        Instant currentTime = Instant.now();
        CheckedoutBook c = service.getBookService().checkoutBook(USER1, "Chicken Squad", currentTime);
        assertEquals(c.getEmail(), USER1);
        assertEquals(c.getTitle(), "Chicken Squad");
        assertEquals(c.getCheckedoutDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList1 = service.getBookService().listOfCheckedoutBooks(USER1);
        assertEquals(checkedoutBookList1.size(), 1);
        CheckedoutBook r =service.getBookService().returnBook(USER1, "Chicken Squad", currentTime);
        assertEquals(r.getEmail(), USER1);
        assertEquals(r.getTitle(), "Chicken Squad");
        assertEquals(r.getReturnDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList2 = service.getBookService().listOfCheckedoutBooks(USER1);
        assertEquals(checkedoutBookList2.size(), 0);
        List<CheckedoutBook> returnedBookList1 = service.getBookService().listOfReturnedBooks(USER1);
        assertEquals(returnedBookList1.size(), 1);
        service.getUserService().signupUser("Phani", "yarlagadda", "1111 aa Newark CA", 32, "pp.yy@gmail.com");
        service.getBookService().addBook("pp.yy@gmail.com", "Petu Pumpkin Tiffin Thief", "Arundathi", "fiction", 3);
        CheckedoutBook ch = service.getBookService().checkoutBook("pp.yy@gmail.com", "Petu Pumpkin Tiffin Thief", currentTime);
        assertEquals(ch.getEmail(), "pp.yy@gmail.com");
        assertEquals(ch.getTitle(), "Petu Pumpkin Tiffin Thief");
        assertEquals(ch.getCheckedoutDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList3 = service.getBookService().listOfCheckedoutBooks("pp.yy@gmail.com");
        assertEquals(checkedoutBookList3.size(), 1);
        CheckedoutBook re =service.getBookService().returnBook("pp.yy@gmail.com", "Petu Pumpkin Tiffin Thief", currentTime);
        assertEquals(re.getEmail(), "pp.yy@gmail.com");
        assertEquals(re.getTitle(), "Petu Pumpkin Tiffin Thief");
        assertEquals(re.getReturnDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList4 = service.getBookService().listOfCheckedoutBooks("pp.yy@gmail.com");
        assertEquals(checkedoutBookList4.size(), 0);
        List<CheckedoutBook> returnedBookList2 = service.getBookService().listOfReturnedBooks("pp.yy@gmail.com");
        assertEquals(returnedBookList2.size(), 1);

    }

    //multiple users checking out same books
    @Test
    public void multipleUsersCheckingoutAndReturningSameBookTest() {
        service.getBookService().addBook(USER1, "Chicken Squad", "Cronin", "fiction", 2);
        Instant currentTime = Instant.now();
        CheckedoutBook c = service.getBookService().checkoutBook(USER1, "Chicken Squad", currentTime);
        assertEquals(c.getEmail(), USER1);
        assertEquals(c.getTitle(), "Chicken Squad");
        assertEquals(c.getCheckedoutDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList1 = service.getBookService().listOfCheckedoutBooks(USER1);
        assertEquals(checkedoutBookList1.size(), 1);
        CheckedoutBook r = service.getBookService().returnBook(USER1, "Chicken Squad", currentTime);
        assertEquals(r.getEmail(), USER1);
        assertEquals(r.getTitle(), "Chicken Squad");
        assertEquals(r.getReturnDate(), currentTime);
        List<CheckedoutBook> returnedBookList1 = service.getBookService().listOfReturnedBooks(USER1);
        assertEquals(returnedBookList1.size(), 1);
        List<CheckedoutBook> checkedoutBookList2 = service.getBookService().listOfCheckedoutBooks(USER1);
        assertEquals(checkedoutBookList2.size(), 0);
        service.getUserService().signupUser("Phani", "yarlagadda", "1111 aa Newark CA", 32, "pp.yy@gmail.com");
        CheckedoutBook ch = service.getBookService().checkoutBook("pp.yy@gmail.com", "Chicken Squad", currentTime);
        assertEquals(ch.getEmail(), "pp.yy@gmail.com");
        assertEquals(ch.getTitle(), "Chicken Squad");
        assertEquals(ch.getCheckedoutDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList3 = service.getBookService().listOfCheckedoutBooks("pp.yy@gmail.com");
        assertEquals(checkedoutBookList3.size(), 1);
        CheckedoutBook re = service.getBookService().returnBook("pp.yy@gmail.com", "Chicken Squad", currentTime);
        assertEquals(re.getEmail(), "pp.yy@gmail.com");
        assertEquals(re.getTitle(), "Chicken Squad");
        assertEquals(re.getReturnDate(), currentTime);
        List<CheckedoutBook> returnedBookList = service.getBookService().listOfReturnedBooks("pp.yy@gmail.com");
        assertEquals(returnedBookList.size(), 1);
        List<CheckedoutBook> checkedoutBookList4 = service.getBookService().listOfCheckedoutBooks("pp.yy@gmail.com");
        assertEquals(checkedoutBookList4.size(), 0);

    }

    //simple return book test
    @Test
    public void simpleReturnBookTest() {
        service.getBookService().addBook(USER1, "Chicken Squad", "Cronin", "fiction", 2);
        Instant currentTime = Instant.now();
        service.getBookService().checkoutBook(USER1, "Chicken Squad", currentTime);
        CheckedoutBook r = service.getBookService().returnBook(USER1, "Chicken Squad", currentTime);
        assertEquals(r.getEmail(), USER1);
        assertEquals(r.getTitle(), "Chicken Squad");
        assertEquals(r.getReturnDate(), currentTime);
        List<CheckedoutBook> returnedBookList = service.getBookService().listOfReturnedBooks(USER1);
        assertEquals(returnedBookList.size(), 1);
        List<CheckedoutBook> checkedoutBookList = service.getBookService().listOfCheckedoutBooks(USER1);
        assertEquals(checkedoutBookList.size(), 0);
    }

    //Return a different book that is not checked out
    @Test
    public void returnABookThatIsNotCheckedoutTest() {
        service.getBookService().addBook(USER1, "Chicken Squad", "Cronin", "fiction", 2);
        Instant currentTime = Instant.now();
        service.getBookService().checkoutBook(USER1, "Chicken Squad", currentTime);
        try {
            service.getBookService().returnBook(USER1, "Amelia Bedelia", currentTime);
        } catch (BookNotCheckedoutException ex) {
               assertEquals("This book is not checkedout by you", ex.getMessage());
        }
    }

    // If a user signedup but did not return books
    @Test
    public void noReturnedBooksTest() {
        List<CheckedoutBook> historyOfCheckedoutBookList = service.getBookService().listOfReturnedBooks(USER1);
        assertNull(historyOfCheckedoutBookList);
    }

    //Search book by genre
    @Test
    public void searchBookByGenreTest(){
        service.getBookService().addBook(USER1, "Chicken Squad", "Cronin", "fiction", 2);
        Instant currentTime = Instant.now();
        List<Book> booksByGenre = service.getBookService().searchBooksByGenre(USER1, "fiction");
        assertEquals(booksByGenre.size(), 1);
    }

    //If the specified genre is not available test
    @Test
    public void searchBookByGenreNotAvailableTest(){
        service.getBookService().addBook(USER1, "Chicken Squad", "Cronin", "fiction", 2);
        Instant currentTime = Instant.now();
        List<Book> booksByGenre = service.getBookService().searchBooksByGenre(USER1, "mystery");
        assertEquals(booksByGenre.size(), 0);
    }
}