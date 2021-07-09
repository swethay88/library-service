package library.books.exceptions;

public class MaximumCheckedoutBooksReachedException extends RuntimeException{
    public MaximumCheckedoutBooksReachedException(String message){
        super(message);
    }
}
