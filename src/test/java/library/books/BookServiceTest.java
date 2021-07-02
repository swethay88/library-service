package library.books;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
            if (service.getBookService().searchBook("yy.ss@gmail.com", "Chicken Squad").getNoOfCopies() == 0) {
                throw new BookCopyNotAvailableException("This book copy is not available to checkout in the library");
            }
            fail("Expecting exception");
        } catch (BookCopyNotAvailableException ex) {
            assertEquals("This book copy is not available to checkout in the library", ex.getMessage());
        }
    }

    //checkedout books are null i.e., List is empty
    @Test
    public void noCheckedoutBooks() {
        try {
            service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
            List<CheckedoutBook> checkedoutBookList = service.getBookService().listOfCheckedoutBooks("yy.ss@gmail.com");
            if (checkedoutBookList == null) {
                throw new NoCheckedoutBooksException("There are no books checkedout!");
            }
            fail("Expecting exception");
        } catch (NoCheckedoutBooksException ex) {
            assertEquals("There are no books checkedout!", ex.getMessage());
        }
    }

    //one user checking out multiple books
    @Test
    public void oneUserMultipleBooksCheckout() {
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
        List<CheckedoutBook> checkedoutBookList = service.getBookService().listOfCheckedoutBooks("yy.ss@gmail.com");
        assertEquals(checkedoutBookList.size(), 2);
    }

    //one user checking out multiple copies
    @Test
    public void oneUserMultipleCopiesCheckout() {
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
    }

    //multiple users checking out different books
    @Test
    public void multipleUsersCheckingoutDifferentBooks() {
        service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
        service.getBookService().addBook("yy.ss@gmail.com", "Chicken Squad", "Cronin", "fiction", 2);
        Instant currentTime = Instant.now();
        CheckedoutBook c = service.getBookService().checkoutBook("yy.ss@gmail.com", "Chicken Squad", currentTime);
        assertEquals(c.getEmail(), "yy.ss@gmail.com");
        assertEquals(c.getTitle(), "Chicken Squad");
        assertEquals(c.getCheckedoutDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList1 = service.getBookService().listOfCheckedoutBooks("yy.ss@gmail.com");
        assertEquals(checkedoutBookList1.size(), 1);
        service.getUserService().signupUser("Phani", "yarlagadda", "1111 aa Newark CA", 32, "pp.yy@gmail.com");
        service.getBookService().addBook("pp.yy@gmail.com", "Petu Pumpkin Tiffin Thief", "Arundathi", "fiction", 3);
        CheckedoutBook ch = service.getBookService().checkoutBook("pp.yy@gmail.com", "Petu Pumpkin Tiffin Thief", currentTime);
        assertEquals(ch.getEmail(), "pp.yy@gmail.com");
        assertEquals(ch.getTitle(), "Petu Pumpkin Tiffin Thief");
        assertEquals(ch.getCheckedoutDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList2 = service.getBookService().listOfCheckedoutBooks("pp.yy@gmail.com");
        assertEquals(checkedoutBookList2.size(), 1);
    }

    //multiple users checking out same books
    @Test
    public void multipleUsersCheckingoutSameBook() {
        service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
        service.getBookService().addBook("yy.ss@gmail.com", "Chicken Squad", "Cronin", "fiction", 2);
        Instant currentTime = Instant.now();
        CheckedoutBook c = service.getBookService().checkoutBook("yy.ss@gmail.com", "Chicken Squad", currentTime);
        assertEquals(c.getEmail(), "yy.ss@gmail.com");
        assertEquals(c.getTitle(), "Chicken Squad");
        assertEquals(c.getCheckedoutDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList1 = service.getBookService().listOfCheckedoutBooks("yy.ss@gmail.com");
        assertEquals(checkedoutBookList1.size(), 1);
        service.getUserService().signupUser("Phani", "yarlagadda", "1111 aa Newark CA", 32, "pp.yy@gmail.com");
        service.getBookService().addBook("pp.yy@gmail.com", "Chicken Squad", "Cronin", "fiction", 3);
        CheckedoutBook ch = service.getBookService().checkoutBook("pp.yy@gmail.com", "Chicken Squad", currentTime);
        assertEquals(ch.getEmail(), "pp.yy@gmail.com");
        assertEquals(ch.getTitle(), "Chicken Squad");
        assertEquals(ch.getCheckedoutDate(), currentTime);
        List<CheckedoutBook> checkedoutBookList2 = service.getBookService().listOfCheckedoutBooks("pp.yy@gmail.com");
        assertEquals(checkedoutBookList2.size(), 1);

    }
}