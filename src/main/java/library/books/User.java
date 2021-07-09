package library.books;

public class User {
    private String firstName;
    private String lastName;
    private String address;
    private int age;
    private String email;

    public User(String firstName, String lastName, String address, int age, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.age = age;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public int getAge() {
        return age;
    }

    public String getEmail(){
        return email;
    }
}