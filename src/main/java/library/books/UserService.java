package library.books;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Users should be able to sign up. For signup users will need to give first name, last name, address, age.
 * Email should be of correct format with @ and end with .com, else exceptions should be thrown
 * If user tries to give an existing email, exceptions should be thrown.
 */

public class UserService {

    /**
     * userDetails map is used to store details of user with email as key and other details as value.
     */

    private Map<String, List<String>> userDetails = new HashMap<>();

    public boolean signupUser(String firstName, String lastName, String address, int age, String email) {

        /**
         * validate if the email already exists in userDetails, throws an exception. If not, details will be put to userDetails map.
         */

        if (userDetails.containsKey(email)) {
            throw new EmailAlreadyExistsException("email already exists. Please use another email");
        } else {

            /**
             * Validate if the email format is correct
             */

            if (!email.contains("@") || !email.endsWith(".com")) {
                throw new InvalidEmailException("Invalid email");
            } else {
                List<String> list = new ArrayList<>();
                list.add(firstName);
                list.add(lastName);
                list.add(address);
                list.add(Integer.toString(age));
                userDetails.put(email, list);
            }
        }
        return true;
    }
}
