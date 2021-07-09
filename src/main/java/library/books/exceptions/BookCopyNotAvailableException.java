package library.books.exceptions;

public class BookCopyNotAvailableException extends RuntimeException{
    public BookCopyNotAvailableException(String message) {
        super(message);
    }
}