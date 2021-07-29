package library.books;

import library.books.exceptions.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BooksService {
    private Map<String, Book> allBooks = new HashMap<>();
    private UserService userService;
    private Map<String, List<CheckedoutBook>> displayCheckedoutBooks = new HashMap<>();
    private Map<String, List<CheckedoutBook>> returnedBooksMap = new HashMap<>();

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void addBook(String email, String title, String author, String genre, int noOfCopies) {
        if (!userService.doesUserExist(email)) {
            throw new UserDoesNotExistException("Invalid user");
        }
        Book book = new Book(title, author, genre, noOfCopies);
        allBooks.put(title, book);
    }

    //Search book by title
    public Book searchBookByTitle(String email, String title) {
        if (!userService.doesUserExist(email)) {
            throw new UserDoesNotExistException("Invalid user");
        }
        return allBooks.get(title);
    }

    //Search for list of books by genre
    public List<Book> searchBooksByGenre(String email, String genre){
        List<Book> bookListGenre = new ArrayList<>();
        if (!userService.doesUserExist(email)) {
            throw new UserDoesNotExistException("Invalid user");
        }
        //send key and value of allBooks map into entry
        for (Map.Entry<String,Book> entry : allBooks.entrySet()){
            //verify if the genre from value(Book) is same as the given genre
            if(entry.getValue().getGenre().equals(genre)){
                //if equal add it to the list to be displayed
                bookListGenre.add(entry.getValue());
            }
        }
        return bookListGenre;
    }

    public void deleteBooks(String title, String author, String genre, int noOfCopies) {
        Book book = new Book(title, author, genre, noOfCopies);
        if (allBooks.containsKey(title)) {
            allBooks.remove(title);
        } else {
            throw new BookTitleNotAvailableException("The book title is not available");
        }
    }

    public CheckedoutBook checkoutBook(String email, String title, Instant checkedoutDate) {
        //email check
        if (!userService.doesUserExist(email)) {
            throw new UserDoesNotExistException("Invalid user");
        }
        //title check
        if (!allBooks.containsKey(title)) {
            throw new BookTitleNotAvailableException("The book title is not available");
        }
        //copy avaialable check
        if (allBooks.get(title).getNoOfCopies() == 0) {
            throw new BookCopyNotAvailableException("This book copy is not available to checkout in the library");
        }
        //maximium books check
        if (displayCheckedoutBooks.containsKey(email) && displayCheckedoutBooks.get(email).size() > 5) {
            throw new MaximumCheckedoutBooksReachedException("You can only checkout maximum of 5 books");
        }
        //checkingout book
        List<CheckedoutBook> rBooks = listOfReturnedBooks(email);
        boolean isAllowed = true;
        for (CheckedoutBook ch : rBooks) {
            String t = ch.getTitle();
            Instant rDate = ch.getReturnDate();
            Instant chDate = ch.getCheckedoutDate();
            int x = returnBookWithinTenDays(email, t, chDate, rDate);
            Instant limitDate = rDate.plus(7, ChronoUnit.DAYS);
            int temp = Instant.now().compareTo(limitDate);
            if (x > 0 && temp <= 0) {
                isAllowed = false;
                break;
            }
        }

        CheckedoutBook checkedoutBook = null;
        if (isAllowed) {
            checkedoutBook = new CheckedoutBook(email, title, checkedoutDate);
            if (!displayCheckedoutBooks.containsKey(email)) {
                displayCheckedoutBooks.put(email, new ArrayList<>());
            }
            displayCheckedoutBooks.get(email).add(checkedoutBook);
            int nCopies = allBooks.get(title).getNoOfCopies() - 1;
            allBooks.get(title).setNoOfCopies(nCopies);
        }
        else{
            throw new CheckingoutBooksBlockedException("Sorry! You can not checkout books because of late return");
        }
        return checkedoutBook;
    }

    public List<CheckedoutBook> listOfCheckedoutBooks(String email) {
        //if the user did not sign up
        if (!userService.doesUserExist(email)) {
            throw new UserDoesNotExistException("Invalid user");
        }
        List<CheckedoutBook> currentCheckedoutBookList = displayCheckedoutBooks.get(email);

        if (currentCheckedoutBookList != null) {
            //to display list of checkedout books in descending order
            Collections.sort(currentCheckedoutBookList, new Comparator<CheckedoutBook>() {
                public int compare(CheckedoutBook o1, CheckedoutBook o2) {
                    if (o1.getCheckedoutDate() == null || o2.getCheckedoutDate() == null)
                        return 0;
                    return o1.getCheckedoutDate().compareTo(o2.getCheckedoutDate());
                }
            });
        }
        return currentCheckedoutBookList;
    }

    public CheckedoutBook returnBook(String email, String title, Instant returnDate) {
        //email check
        if (!userService.doesUserExist(email)) {
            throw new UserDoesNotExistException("Invalid user");
        }

        //verify if the book is checkedout by the user
        // i.e., a user can return books checkedout from the respective account
        // i.e., a user cannot return the books checkedout by other users
        List<CheckedoutBook> currentChekedoutBookList = displayCheckedoutBooks.get(email);
        boolean isCheckedOut = false;
        for (CheckedoutBook c : currentChekedoutBookList) {
            if (c.getTitle().equals(title)) {
                isCheckedOut = true;
            }
        }
        if (!isCheckedOut) {
            throw new BookNotCheckedoutException("This book is not checkedout by you");
        }

        //Returning book
        CheckedoutBook returnedBook = new CheckedoutBook(title, returnDate);
        returnedBook.setEmail(email);
        if (!returnedBooksMap.containsKey(email)) {
            returnedBooksMap.put(email, new ArrayList<>());
        }
        returnedBooksMap.get(email).add(returnedBook);
        int nCopies = allBooks.get(title).getNoOfCopies() + 1;
        allBooks.get(title).setNoOfCopies(nCopies);

        //remove the returned book from current chekedout list
        List<CheckedoutBook> currentCheckedoutBookList = displayCheckedoutBooks.get(email);
        CheckedoutBook temp = null;
        for (CheckedoutBook c : currentChekedoutBookList) {
            if (c.getEmail().equals(returnedBook.getEmail())) {
                temp = c;
            }
        }
        currentCheckedoutBookList.remove(temp);
        return returnedBook;
    }

    public List<CheckedoutBook> listOfReturnedBooks(String email) {
        //if the user did not sign up
        if (!userService.doesUserExist(email)) {
            throw new UserDoesNotExistException("Invalid user");
        }

        List<CheckedoutBook> historyOfCheckedoutBookList = returnedBooksMap.get(email);
        if (historyOfCheckedoutBookList != null) {
            //to display list of returned books (or in other words history of checkedout books) in descending order
            Collections.sort(historyOfCheckedoutBookList, new Comparator<CheckedoutBook>() {
                public int compare(CheckedoutBook o1, CheckedoutBook o2) {
                    if (o1.getReturnDate() == null || o2.getReturnDate() == null)
                        return 0;
                    return o1.getReturnDate().compareTo(o2.getReturnDate());
                }
            });
        }
        return historyOfCheckedoutBookList;
    }

    public int returnBookWithinTenDays(String email, String title, Instant checkedoutDate, Instant returnDate){
        Instant dueDate = checkedoutDate.plus(10, ChronoUnit.DAYS);
        int value = returnDate.compareTo(dueDate);
        //if value is greater than 0 then returnDate is greater than limitDate
        // i.e., user didn't return book by due date
        return value;
    }
}