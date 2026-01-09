import java.nio.file.*;
import java.util.List;
public class Library {
    private Book[] books;

    public String loadText() throws Exception {
        return Files.readString(Path.of("books_the_library_system.txt"));
    }

    public Book[] getBooks() {
        String content = loadText();
        String[] lines = content.split("\n");

        for (String line : lines) {
            String[] parts = line.split("\\|");
        return books;
    }


    public void setBooks(Book[] books) {
        this.books = books;
    }
}
