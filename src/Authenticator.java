public class Authenticator {
    private static User[] userList = {
            new User("user", "123")
    };

    public static User[] getUserList() {
        return userList;
    }

    public static void setUserList(User[] setUserList) {
        userList = setUserList;
    }

    public static String isValidUser(String username, String password) {
        if (userList == null) return "invalidUser";

        for (User user : userList) {
            if (user.getUserName().equals(username)) {
                if (user.getPassword().equals(password)) {
                    return "valid";
                } else {
                    return "wrongPassword";
                }
            }
        }
        return "invalidUser";
    }
}
