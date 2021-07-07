package library.books;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {

    private LibraryService service = new LibraryService();

    //User signedup, added a book, checkedout the added book
    @Test
    public void chekoutBookSimpleTest() {
        service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
        service.getBookService().addBook("yy.ss@gmail.com", "Chicken Squad", "Cronin", "fiction", 2);
        Instant currentTime = Instant.now();
        CheckedoutBook c = service.getBookService().checkoutBook("yy.ss@gmail.com", "Chicken Squad", currentTime);
        //assert c variables
        assertEquals(c.getEmail(), "yy.ss@gmail.com");
        assertEquals(c.getTitle(), "Chicken Squad");
        assertEquals(c.getCheckedoutDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList = service.getBookService().listOfCheckedoutBooks("yy.ss@gmail.com");
        assertEquals(checkedoutBookList.size(), 1);
    }

    //Test if user doesn't exist
    @Test
    public void chekoutBookUserDoesntExistTest() {
        try {
            if (!service.getUserService().doesUserExist("yy.ss@gmail.com")) {
                throw new InvalidEmailException("invalid email");
            }
            fail("Expecting exception");
        } catch (InvalidEmailException ex) {
            // expected exception
            assertEquals("invalid email", ex.getMessage());
        }
    }

    //Test if book doesn't exist
    @Test
    public void titleDoesntExistTest() {
        try {
            service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
            if (service.getBookService().searchBook("yy.ss@gmail.com", "Aaa") == null) {
                throw new BookTitleNotAvailableException("Book title is not available");
            }
            fail("Expecting exception");
        } catch (BookTitleNotAvailableException ex) {
            assertEquals("Book title is not available", ex.getMessage());
        }
    }

    //Test if no of copies aren't available in the library
    @Test
    public void copiesNotAvailableTest() {
        try {
            service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
            service.getBookService().addBook("yy.ss@gmail.com", "Chicken Squad", "Cronin", "fiction", 1);
            service.getBookService().checkoutBook("yy.ss@gmail.com", "Chicken Squad", Instant.now());
            service.getBookService().checkoutBook("yy.ss@gmail.com", "Chicken Squad", Instant.now());
            fail("Expecting exception");
        } catch (BookCopyNotAvailableException ex) {
            assertEquals("This book copy is not available to checkout in the library", ex.getMessage());
        }
    }

    //checkedout books are null i.e., List is empty
    /*@Test
    public void noCheckedoutBooksTest() {
        try {
            service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
            List<CheckedoutBook> checkedoutBookList = service.getBookService().listOfCheckedoutBooks("yy.ss@gmail.com");
            assertNotNull(checkedoutBookList);
            fail("Expecting exception");
        } catch (NoCheckedoutBooksException ex) {

        }

    }*/

    //one user checking out multiple books
    @Test
    public void oneUserMultipleBooksCheckoutAndReturnTest() {
        service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
        service.getBookService().addBook("yy.ss@gmail.com", "Chicken Squad", "Cronin", "fiction", 2);
        Instant currentTime = Instant.now();
        CheckedoutBook c = service.getBookService().checkoutBook("yy.ss@gmail.com", "Chicken Squad", currentTime);
        assertEquals(c.getEmail(), "yy.ss@gmail.com");
        assertEquals(c.getTitle(), "Chicken Squad");
        assertEquals(c.getCheckedoutDate(), currentTime);
        service.getBookService().addBook("yy.ss@gmail.com", "Petu Pumpkin Tiffin Thief", "Arundathi", "fiction", 3);
        CheckedoutBook ch = service.getBookService().checkoutBook("yy.ss@gmail.com", "Petu Pumpkin Tiffin Thief", currentTime);
        assertEquals(ch.getEmail(), "yy.ss@gmail.com");
        assertEquals(ch.getTitle(), "Petu Pumpkin Tiffin Thief");
        assertEquals(ch.getCheckedoutDate(), currentTime);
        CheckedoutBook r = service.getBookService().returnBook("yy.ss@gmail.com", "Petu Pumpkin Tiffin Thief", currentTime);
        assertEquals(r.getEmail(), "yy.ss@gmail.com");
        assertEquals(r.getTitle(), "Petu Pumpkin Tiffin Thief");
        assertEquals(r.getReturnDate(), currentTime);
        List<CheckedoutBook> returnedBookList = service.getBookService().listOfReturnedBooks("yy.ss@gmail.com");
        assertEquals(returnedBookList.size(), 1);
        List<CheckedoutBook> checkedoutBookList = service.getBookService().listOfCheckedoutBooks("yy.ss@gmail.com");
        assertEquals(checkedoutBookList.size(), 1);
    }

    //one user checking out multiple copies
    @Test
    public void oneUserMultipleCopiesCheckoutAndReturnTest() {
        service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
        service.getBookService().addBook("yy.ss@gmail.com", "Chicken Squad", "Cronin", "fiction", 3);
        Instant currentTime = Instant.now();
        CheckedoutBook c = service.getBookService().checkoutBook("yy.ss@gmail.com", "Chicken Squad", currentTime);
        assertEquals(c.getEmail(), "yy.ss@gmail.com");
        assertEquals(c.getTitle(), "Chicken Squad");
        assertEquals(c.getCheckedoutDate(), currentTime);
        service.getBookService().addBook("yy.ss@gmail.com", "Chicken Squad", "Cronin", "fiction", 3);
        CheckedoutBook ch = service.getBookService().checkoutBook("yy.ss@gmail.com", "Chicken Squad", currentTime);
        assertEquals(ch.getEmail(), "yy.ss@gmail.com");
        assertEquals(ch.getTitle(), "Chicken Squad");
        assertEquals(ch.getCheckedoutDate(), currentTime);
        service.getBookService().addBook("yy.ss@gmail.com", "Chicken Squad", "Cronin", "fiction", 3);
        CheckedoutBook che = service.getBookService().checkoutBook("yy.ss@gmail.com", "Chicken Squad", currentTime);
        assertEquals(che.getEmail(), "yy.ss@gmail.com");
        assertEquals(che.getTitle(), "Chicken Squad");
        assertEquals(che.getCheckedoutDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList = service.getBookService().listOfCheckedoutBooks("yy.ss@gmail.com");
        assertEquals(checkedoutBookList.size(), 3);
        CheckedoutBook r = service.getBookService().returnBook("yy.ss@gmail.com", "Chicken Squad", currentTime);
        assertEquals(r.getEmail(), "yy.ss@gmail.com");
        assertEquals(r.getTitle(), "Chicken Squad");
        assertEquals(r.getReturnDate(), currentTime);
        List<CheckedoutBook> returnedBookList = service.getBookService().listOfReturnedBooks("yy.ss@gmail.com");
        assertEquals(returnedBookList.size(), 1);
        List<CheckedoutBook> checkedoutBookList2 = service.getBookService().listOfCheckedoutBooks("yy.ss@gmail.com");
        assertEquals(checkedoutBookList.size(), 2);
    }

    //multiple users checking out different books
    @Test
    public void multipleUsersCheckingoutAndReturningDifferentBooksTest() {
        service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
        service.getBookService().addBook("yy.ss@gmail.com", "Chicken Squad", "Cronin", "fiction", 2);
        Instant currentTime = Instant.now();
        CheckedoutBook c = service.getBookService().checkoutBook("yy.ss@gmail.com", "Chicken Squad", currentTime);
        assertEquals(c.getEmail(), "yy.ss@gmail.com");
        assertEquals(c.getTitle(), "Chicken Squad");
        assertEquals(c.getCheckedoutDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList1 = service.getBookService().listOfCheckedoutBooks("yy.ss@gmail.com");
        assertEquals(checkedoutBookList1.size(), 1);
        CheckedoutBook r =service.getBookService().returnBook("yy.ss@gmail.com", "Chicken Squad", currentTime);
        assertEquals(r.getEmail(), "yy.ss@gmail.com");
        assertEquals(r.getTitle(), "Chicken Squad");
        assertEquals(r.getReturnDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList2 = service.getBookService().listOfCheckedoutBooks("yy.ss@gmail.com");
        assertEquals(checkedoutBookList2.size(), 0);
        List<CheckedoutBook> returnedBookList1 = service.getBookService().listOfReturnedBooks("yy.ss@gmail.com");
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
        service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
        service.getBookService().addBook("yy.ss@gmail.com", "Chicken Squad", "Cronin", "fiction", 2);
        Instant currentTime = Instant.now();
        CheckedoutBook c = service.getBookService().checkoutBook("yy.ss@gmail.com", "Chicken Squad", currentTime);
        assertEquals(c.getEmail(), "yy.ss@gmail.com");
        assertEquals(c.getTitle(), "Chicken Squad");
        assertEquals(c.getCheckedoutDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList1 = service.getBookService().listOfCheckedoutBooks("yy.ss@gmail.com");
        assertEquals(checkedoutBookList1.size(), 1);
        CheckedoutBook r = service.getBookService().returnBook("yy.ss@gmail.com", "Chicken Squad", currentTime);
        assertEquals(r.getEmail(), "yy.ss@gmail.com");
        assertEquals(r.getTitle(), "Chicken Squad");
        assertEquals(r.getReturnDate(), currentTime);
        List<CheckedoutBook> returnedBookList1 = service.getBookService().listOfReturnedBooks("yy.ss@gmail.com");
        assertEquals(returnedBookList1.size(), 1);
        List<CheckedoutBook> checkedoutBookList2 = service.getBookService().listOfCheckedoutBooks("yy.ss@gmail.com");
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
        service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
        service.getBookService().addBook("yy.ss@gmail.com", "Chicken Squad", "Cronin", "fiction", 2);
        Instant currentTime = Instant.now();
        service.getBookService().checkoutBook("yy.ss@gmail.com", "Chicken Squad", currentTime);
        CheckedoutBook r = service.getBookService().returnBook("yy.ss@gmail.com", "Chicken Squad", currentTime);
        assertEquals(r.getEmail(), "yy.ss@gmail.com");
        assertEquals(r.getTitle(), "Chicken Squad");
        assertEquals(r.getReturnDate(), currentTime);
        List<CheckedoutBook> returnedBookList = service.getBookService().listOfReturnedBooks("yy.ss@gmail.com");
        assertEquals(returnedBookList.size(), 1);
        List<CheckedoutBook> checkedoutBookList = service.getBookService().listOfCheckedoutBooks("yy.ss@gmail.com");
        assertEquals(checkedoutBookList.size(), 0);
    }

    //Return a different book that is not checked out
    @Test
    public void returnABookThatIsNotCheckedoutTest() {
        try {
            service.getUserService().signupUser("Swetha", "Yarlagadda", "32422 GC FHills MI", 32, "yy.ss@gmail.com");
            service.getBookService().addBook("yy.ss@gmail.com", "Chicken Squad", "Cronin", "fiction", 2);
            Instant currentTime = Instant.now();
            service.getBookService().checkoutBook("yy.ss@gmail.com", "Chicken Squad", currentTime);
            service.getBookService().returnBook("yy.ss@gmail.com", "Amelia Bedelia", currentTime);
        } catch (BookNotCheckedoutException ex) {
               assertEquals("This book is not checkedout by you", ex.getMessage());
        }
    }

}