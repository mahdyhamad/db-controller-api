package Auth;

import java.util.List;

public class AuthManager {

    List<User> users;

    public AuthManager(){
        loadUsers();
    }

    void loadUsers(){
        // TODO: implement functionality
        // load users from the database
    }

    User getUserByUsername(String username){
        for (User user: users){
            if (user.getUsername() == username)
                return user;
        }
        return null;
    }

    public boolean authenticate(String username, String password){
        User user = getUserByUsername(username);
        if (user == null){
            // user does not exist
            return false;
        }
        return user.authenticate(password);
    }

    public boolean canPerformOperation(User user){
        boolean canPerformOperation = true;

        if (user.shouldChangePassword())
            canPerformOperation = false;

        return canPerformOperation;
    }

}
