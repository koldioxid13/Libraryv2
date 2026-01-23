import java.nio.file.*;
import java.util.List;

public class Authenticator {
    private static final String USER_FILE = "users_the_library_system.txt";

    public static String isValidUser(String username, String password) {
        try {
            if (!Files.exists(Paths.get(USER_FILE))) return "invalidUser";

            List<String> lines = Files.readAllLines(Paths.get(USER_FILE));

            for (String line : lines) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String storedName = parts[0];
                    String storedPass = parts[1];

                    if (storedName.equals(username)) {
                        if (storedPass.equals(password)) {
                            return "valid";
                        } else {
                            return "wrongPassword";
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "invalidUser";
    }
}