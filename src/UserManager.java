import java.nio.file.*;
import java.io.IOException;

public class UserManager {
    private static final String USER_FILE = "users_the_library_system.txt";
    //Just nu kan man skapa flera användare med samma namn
    public void saveUser(User user) {
        try {
            String userLine = user.getUserName() + "|" + user.getPassword() + System.lineSeparator();

            Files.write(
                    Paths.get(USER_FILE),
                    userLine.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}