package library.books;

import library.books.exceptions.EmailAlreadyExistsException;
import library.books.exceptions.InvalidEmailException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class UserServiceTest {

    private LibraryService service;

    @Before
    public void setUp(){
        service = new LibraryService();
    }

    @Test
    public void testSignupUserSimple() {
        service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
        assertEquals(service.getUserService().doesUserExist("yy.ss@gmail.com"), true);
    }

    @Test
    public void testSignupUserWrongEmailFormat() {
        try {
            service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ssgmail.com");
            fail("Expecting exception");
        } catch (InvalidEmailException ex) {
            assertEquals("Invalid email", ex.getMessage());
        }
    }

    @Test
    public void testSignupUserEmailAlreadyExists() {
        try {
            service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
            service.getUserService().signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
            fail("Expecting exception");
        } catch (EmailAlreadyExistsException ex){
            assertEquals("email already exists. Please use another email", ex.getMessage());
        }
    }
}

