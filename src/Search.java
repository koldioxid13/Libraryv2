import java.util.ArrayList;
import java.util.List;

public class Search {
    private Library library = new Library();

    public List<Book> searchBooks(String query) {
        List<Book> filteredBooks = new ArrayList<>();
        String lowerQuery = query.toLowerCase().trim();

        try {
            Book[] allBooks = library.getBooks();
            for (Book b : allBooks) {
                if (b.getTitle().toLowerCase().contains(lowerQuery) ||
                        b.getAuthor().toLowerCase().contains(lowerQuery) ||
                        b.getISBN().contains(lowerQuery)) {
                    filteredBooks.add(b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredBooks;
    }
}