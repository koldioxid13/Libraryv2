import java.nio.file.*;
import java.io.IOException;

public class LoanManager {
    private static final String FILE_PATH = "loans.txt";

    public void saveLoan(LoanedBook loan) {
        try {
            String line = loan.toFileFormat() + System.lineSeparator();

            Files.write(
                    Paths.get(FILE_PATH),
                    line.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
            System.out.println("Lånet sparades i " + FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}