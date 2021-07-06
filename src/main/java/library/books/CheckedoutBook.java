package library.books;

import java.time.Instant;

public class CheckedoutBook {
    private String email;
    private String title;
    private Instant checkedoutDate;
    private Instant returnDate;

    public CheckedoutBook(String email, String title, Instant checkedoutDate) {
        this.email = email;
        this.title = title;
        this.checkedoutDate = checkedoutDate;
    }

    public CheckedoutBook(String title, Instant returnDate){
        this.title = title;
        this.returnDate = returnDate;
    }

    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }

    public Instant getCheckedoutDate() {
        return checkedoutDate;
    }

    public Instant getReturnDate() {
        return returnDate;
    }

    public void setEmail(String email){
        this.email = email;
    }

}
