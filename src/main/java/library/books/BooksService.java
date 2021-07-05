package library.books;

import java.time.Instant;
import java.util.*;

public class BooksService {
    private Map<String, Book> allBooks = new HashMap<>();
    private UserService userService;
    private Map<String, List<CheckedoutBook>> displayCheckedoutBooks = new HashMap<>();
    private Map<String, List<ReturnedBook>> returnedBooksMap = new HashMap<>();

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

    public Book searchBook(String email, String title) {
        if (!userService.doesUserExist(email)) {
            throw new UserDoesNotExistException("Invalid user");
        }
        return allBooks.get(title);
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
        CheckedoutBook checkedoutBook = new CheckedoutBook(email, title, checkedoutDate);
        if (!displayCheckedoutBooks.containsKey(email)) {
            displayCheckedoutBooks.put(email, new ArrayList<>());
        }
        displayCheckedoutBooks.get(email).add(checkedoutBook);
        int nCopies = allBooks.get(title).getNoOfCopies() - 1;
        allBooks.get(title).setNoOfCopies(nCopies);
        return checkedoutBook;
    }

    public List<CheckedoutBook> listOfCheckedoutBooks(String email) {
        List<CheckedoutBook> currentCheckedoutBookList = displayCheckedoutBooks.get(email);
        //to display list of checkedout books in descending order
        Collections.sort(currentCheckedoutBookList, new Comparator<CheckedoutBook>() {
            public int compare(CheckedoutBook o1, CheckedoutBook o2) {
                if (o1.getCheckedoutDate() == null || o2.getCheckedoutDate() == null)
                    return 0;
                return o1.getCheckedoutDate().compareTo(o2.getCheckedoutDate());
            }
        });
        //List of books checkedout by a particular user, in descending order of dates
        return currentCheckedoutBookList;
    }

   public ReturnedBook returnBook(String email, String title, Instant returnDate){
        //email check
       if (!userService.doesUserExist(email)) {
           throw new UserDoesNotExistException("Invalid user");
       }

       //verify if the book is checkedout by the user
       // i.e., a user can return books checkedout from the respective account
       // i.e., a user cannot return the books checkedout by other users
       List<CheckedoutBook> currentChekedoutBookList = displayCheckedoutBooks.get(email);
       boolean check = false;
       for(CheckedoutBook c : currentChekedoutBookList) {
           if (!c.getTitle().equals(title)) {
               check = true;
           }
       }
       if(!check){
           throw new BookNotCheckedoutException("This book is not checkedout by you");
       }

       //Returning book
       ReturnedBook returnedBook = new ReturnedBook(email, title, returnDate);
       if (!returnedBooksMap.containsKey(email)) {
           returnedBooksMap.put(email, new ArrayList<>());
       }
       returnedBooksMap.get(email).add(returnedBook);
       int nCopies = allBooks.get(title).getNoOfCopies() + 1;
       allBooks.get(title).setNoOfCopies(nCopies);
       return returnedBook;
   }

    public List<ReturnedBook> listOfReturnedBooks(String email) {
        return returnedBooksMap.get(email);
    }
}