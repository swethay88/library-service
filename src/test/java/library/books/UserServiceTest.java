package library.books;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryManagementTest {

    private UserService service = new UserService();
    private boolean validate;

    @Test
    public void testSignupUserSimple(){
        validate = service.signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
        assertEquals(validate, true);
    }

    @Test
    public void testSignupUserWrongEmailFormat(){
        validate = service.signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ssgmail.com");
        assertEquals(validate, false);
    }

    @Test
    public void testSignupUserEmailAlreadyExists() {
        validate = service.signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
        assertEquals(validate, true);
        service.signupUser("Swetha", "yarlagadda", "32434 GC FHills MI", 32, "yy.ss@gmail.com");
        assertEquals(validate, false);
    }
}
