package library.books.exceptions;

public class BookTitleNotAvailableException extends RuntimeException{

    public BookTitleNotAvailableException(String message) {
        super(message);
    }
}
