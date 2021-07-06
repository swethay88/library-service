package library.books;

public class BookNotCheckedoutException extends RuntimeException{
    public BookNotCheckedoutException(String message){
        super(message);
    }

}
