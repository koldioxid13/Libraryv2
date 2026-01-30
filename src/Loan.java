import java.io.*;
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

    public LoanedBook[] getLoans() throws Exception {
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

                if (details[0].contains("User")) b.setUser(new User(details[1].trim()));
                if (details[0].contains("Date")) b.setLoanDate(details[1].trim());
                if (details[0].contains("Time")) b.setTimeLeft(Double.parseDouble(details[1].trim()));
                if (details[0].contains("Book")) {
                    List<Book> results = bookService.searchBooks(details[1].trim());
                    b.setBook(results.getFirst());
                }
            }

            this.loans[i] = b;
        }
        return loans;
    }

    public boolean saveLoan(LoanedBook loan) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(LOAN_FILE));
            String currentLine;
            while((currentLine = reader.readLine()) != null) {
                // trim newline when comparing with lineToRemove
                String trimmedLine = currentLine.trim();
                if(trimmedLine.contains(loan.getUser().getUserName()) && trimmedLine.contains(loan.getBook().getTitle())) {
                    reader.close();
                    return false;
                }
            }
            reader.close();
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
        return true;
    }

    public void returnLoan(LoanedBook loan) {
        try {
            File inputFile = new File(LOAN_FILE);
            File tempFile = new File("temp_loans.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while((currentLine = reader.readLine()) != null) {
                // trim newline when comparing with lineToRemove
                String trimmedLine = currentLine.trim();
                if(trimmedLine.contains(loan.getUser().getUserName()) && trimmedLine.contains(loan.getBook().getTitle())) continue;
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
            if (inputFile.delete()) {
                if (tempFile.renameTo(inputFile)){
                    System.out.println("Lånet togs bort från " + LOAN_FILE);
                }
            } else {
                System.out.println("Error");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setLoans(LoanedBook[] loans) {
        this.loans = loans;
    }
}