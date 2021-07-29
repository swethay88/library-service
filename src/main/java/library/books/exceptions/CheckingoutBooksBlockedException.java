package library.books.exceptions;

public class CheckingoutBooksBlockedException extends RuntimeException{
    public CheckingoutBooksBlockedException(String message){
        super(message);
    }
}
