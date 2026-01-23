import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main2 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main2().createLoginView());
    }

    private void createLoginView() {
        JFrame frame = new JFrame("Login Window");
        frame.setSize(600, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalGlue());

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JLabel statusLabel = new JLabel();
        JButton loginButton = new JButton("Log In");

        usernameField.setMaximumSize(new Dimension(200, 30));
        passwordField.setMaximumSize(new Dimension(200, 30));

        panel.add(centerComponent(new JLabel("Username:")));
        panel.add(centerComponent(usernameField));
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        panel.add(centerComponent(new JLabel("Password:")));
        panel.add(centerComponent(passwordField));
        panel.add(centerComponent(statusLabel));
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        panel.add(centerComponent(loginButton));

        panel.add(Box.createVerticalGlue());

        frame.add(panel);
        frame.setVisible(true);

        loginButton.addActionListener(e -> {
            System.out.println("User: " + usernameField.getText());
            System.out.println("Password: " + passwordField.getText());

            String isValid = Authenticator.isValidUser(usernameField.getText(), passwordField.getText());

            if (isValid.equals("valid")) {
                frame.getContentPane().removeAll();
                createSearchView(frame);
                frame.revalidate();
                frame.repaint();
            } else if (isValid.equals("wrongPassword")) {
                statusLabel.setText("Wrong password");
            } else if (isValid.equals("invalidUser")) {
                statusLabel.setText("User does not exist");
            } else {
                statusLabel.setText("Error");
            }
        });
    }

    private void createSearchView(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Top search bar
        JPanel topPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchField);
        topPanel.add(searchButton);


        // Center scrollable area for the 3 boxes
        JPanel boxesContainer = new JPanel();
        boxesContainer.setLayout(new BoxLayout(boxesContainer, BoxLayout.Y_AXIS));

        try {
            Book[] allBooks = new Library().getBooks();
            for (Book b : allBooks) {
                boxesContainer.add(createBookBox(b));
                boxesContainer.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Search bookService = new Search();

        searchButton.addActionListener(e -> {
            String query = searchField.getText();

            List<Book> results = bookService.searchBooks(query);
            boxesContainer.removeAll();
            for (Book b : results) {
                boxesContainer.add(createBookBox(b));
                boxesContainer.add(Box.createRigidArea(new Dimension(0, 15)));
            }

            boxesContainer.revalidate();
            boxesContainer.repaint();
        });



        JScrollPane scrollPane = new JScrollPane(boxesContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.add(panel);
    }

    private JPanel createSampleBox(String title) {
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        boxPanel.setBorder(BorderFactory.createTitledBorder(title));

        boxPanel.add(new JLabel("Sample Line 1 Sample Line 1 Sample Line 1 Sample Line 1" ));
        boxPanel.add(new JLabel("Sample Line 2"));
        boxPanel.add(new JLabel("Sample Line 3"));
        boxPanel.add(new JLabel("Sample Line 4"));
        boxPanel.add(new JLabel("Sample Line 5"));

        return boxPanel;
    }

    private JPanel createBookBox(Book book) {
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

        boxPanel.setBorder(BorderFactory.createTitledBorder(book.getTitle()));

        boxPanel.add(new JLabel("Författare: " + book.getAuthor()));
        boxPanel.add(new JLabel("Genre: " + book.getGenre()));
        boxPanel.add(new JLabel("ISBN: " + book.getISBN()));

        boxPanel.setMaximumSize(new Dimension(550, 120));

        return boxPanel;
    }

    // Utility to horizontally center components
    private Component centerComponent(Component comp) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(comp);
        return wrapper;
    }
}