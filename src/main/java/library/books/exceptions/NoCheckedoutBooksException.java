package library.books.exceptions;

public class NoCheckedoutBooksException extends RuntimeException{
    public NoCheckedoutBooksException(String message) {
        super(message);
    }
}