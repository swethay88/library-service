package library.books;


public class Book {
    private String title;
    private String author;
    private String genre;
    private int noOfCopies;

    public Book(String title, String author, String genre, int noOfCopies) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.noOfCopies = noOfCopies;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public int getNoOfCopies() {
        return noOfCopies;
    }


}
