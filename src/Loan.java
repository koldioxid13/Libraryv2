import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Loan {
    private static final String LOAN_FILE = "loans.txt";
    private LoanedBook[] loans;

    public String loadText() throws Exception {
        return Files.readString(Path.of(LOAN_FILE));
    }

    public LoanedBook[] getloans() throws Exception {
        String content = loadText();
        String[] lines = content.split("\n");
        this.loans = new LoanedBook[lines.length];
        Search bookService = new Search();

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().isEmpty()) continue;

            String[] parts = lines[i].split("\\|");
            LoanedBook b = new LoanedBook();

            for (String part : parts) {
                String[] details = part.split(":");

                if (details[0].contains("User")) b.setUser(new User(details[1].trim(), details[2].trim()));
                if (details[0].contains("LoanDate")) b.setLoanDate(details[1].trim());
                if (details[0].contains("TimeLeft")) b.setTimeLeft(Double.parseDouble(details[1].trim()));
                if (details[0].contains("Book")) {
                    List<Book> results = bookService.searchBooks(details[1].trim());
                    b.setBook(results.get(0));
                }
            }

            this.loans[i] = b;
        }
        return loans;
    }

    public void saveLoan(LoanedBook loan) {
        try {
            String line = loan.toFileFormat() + System.lineSeparator();

            Files.write(
                    Paths.get(LOAN_FILE),
                    line.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
            System.out.println("Lånet sparades i " + LOAN_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLoans(LoanedBook[] loans) {
        this.loans = loans;
    }
}