package library.books;

import java.time.Instant;

public class ReturnedBook {
    private String email;
    private String title;
    private Instant returnDate;

    public ReturnedBook(String email, String title, Instant returnDate) {
        this.email = email;
        this.title = title;
        this.returnDate = returnDate;
    }

    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }

    public Instant getReturnDate() {
        return returnDate;
    }
}
