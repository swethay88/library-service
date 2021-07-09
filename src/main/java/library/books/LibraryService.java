package library.books;

public class LibraryService {
    private BooksService bookService;
    private UserService userService;
    private boolean isApplicationStarted;

    public LibraryService() {
        bookService = new BooksService();
        userService = new UserService();
        bookService.setUserService(userService);
    }

    public void start() {
        isApplicationStarted = true;
    }

    public void stop() {
        isApplicationStarted = false;
        // TODO: clear data
    }

    public BooksService getBookService() {
        return bookService;
    }

    public UserService getUserService() {
        return userService;
    }
}
