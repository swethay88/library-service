package library.books.exceptions;

public class BookNotCheckedoutException extends RuntimeException{
    public BookNotCheckedoutException(String message){
        super(message);
    }

}
