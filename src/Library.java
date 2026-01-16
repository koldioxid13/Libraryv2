import java.nio.file.*;
import java.util.List;

public class Library {
    private Book[] books;

    public String loadText() throws Exception {
        return Files.readString(Path.of("books_the_library_system.txt"));
    }

    public Book[] getBooks() throws Exception {
        String content = loadText();
        String[] lines = content.split("\n");
        this.books = new Book[lines.length];

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().isEmpty()) continue;

            String[] parts = lines[i].split("\\|");
            Book b = new Book();

            for (String part : parts) {
                String[] details = part.split(":");

                if (details[0].contains("Title")) b.setTitle(details[1].trim());
                if (details[0].contains("Author")) b.setAuthor(details[1].trim());
                if (details[0].contains("Language")) b.setGenre(details[1].trim());
                if (details[0].contains("ISBN")) b.setISBN(details[1].trim());
            }

            this.books[i] = b;
        }
        return books;
    }

    public void setBooks(Book[] books) {
        this.books = books;
    }
}