import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import static java.time.LocalTime.now;

public class Main2 {
    private User currentUser;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Library System");
            frame.setSize(600, 1000);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            new Main2().createLoginView(frame);
        });
    }

    private void createLoginView(JFrame frame) {
        frame.getContentPane().removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalGlue());

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JLabel statusLabel = new JLabel();
        JButton loginButton = new JButton("Log In");

        JButton goToRegisterButton = new JButton("Create account");
        panel.add(centerComponent(goToRegisterButton));
        goToRegisterButton.addActionListener(e -> {
            createRegisterView(frame);
        });

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

        frame.add(panel);
        frame.revalidate();
        frame.repaint();

        loginButton.addActionListener(e -> {
            System.out.println("User: " + usernameField.getText());
            System.out.println("Password: " + passwordField.getText());

            String isValid = Authenticator.isValidUser(usernameField.getText(), passwordField.getText());

            if (isValid.equals("valid")) {
                currentUser = new User(usernameField.getText(), passwordField.getText());
                frame.getContentPane().removeAll();
                createLoggedInView(frame);
                frame.revalidate();
                frame.repaint();
            } else if (isValid.equals("wrongPassword")) {
                setStatus("Wrong password", statusLabel);
            } else if (isValid.equals("invalidUser")) {
                setStatus("User does not exist", statusLabel);
            } else {
                setStatus("Error", statusLabel);
            }
        });
    }

    private void createLoggedInView(JFrame frame) {
        frame.getContentPane().removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalGlue());

        JButton logoutButton = new JButton("Log Out");
        JButton searchButton = new JButton("Search for books");
        JButton loansButton = new JButton("My loans");

        logoutButton.addActionListener(e -> {
            currentUser = null;
            createLoginView(frame);
        });
        searchButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            createSearchView(frame);
            frame.revalidate();
            frame.repaint();
        });
        loansButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            createLoanView(frame);
            frame.revalidate();
            frame.repaint();
        });

        panel.add(centerComponent(new JLabel("Logged in as: " + currentUser.getUserName())));
        panel.add(centerComponent(searchButton));
        panel.add(centerComponent(loansButton));
        panel.add(centerComponent(logoutButton));


        panel.add(Box.createVerticalGlue());

        frame.add(panel);
        frame.setVisible(true);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void createRegisterView(JFrame frame) {
        frame.getContentPane().removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalGlue());

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JLabel statusLabel = new JLabel();
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        usernameField.setMaximumSize(new Dimension(200, 30));
        passwordField.setMaximumSize(new Dimension(200, 30));

        panel.add(centerComponent(new JLabel("New username:")));
        panel.add(centerComponent(usernameField));
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        panel.add(centerComponent(new JLabel("New password:")));
        panel.add(centerComponent(passwordField));
        panel.add(centerComponent(statusLabel));
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        panel.add(centerComponent(registerButton));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(centerComponent(backButton));

        panel.add(Box.createVerticalGlue());

        frame.add(panel);

        frame.revalidate();
        frame.repaint();

        registerButton.addActionListener(e -> {
            String name = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if (name.isEmpty() || pass.isEmpty()) {
                setStatus("Username cannot be empty!", statusLabel);
            } else {
                User newUser = new User(name, pass);
                UserManager userManager = new UserManager();

                boolean success = userManager.saveUser(newUser);

                if (success) {
                    setStatus("User saved", statusLabel);
                } else {
                    setStatus("Username already taken", statusLabel);
                }

            }
        });

        backButton.addActionListener(e -> {
            createLoginView(frame);
        });

    }

    private void createSearchView(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Top search bar
        JPanel topPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");
        topPanel.add(backButton);
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

        backButton.addActionListener(e -> {
            createLoggedInView(frame);
        });

        JScrollPane scrollPane = new JScrollPane(boxesContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.add(panel);
    }

    private void createLoanView(JFrame frame){
        JPanel loanPanel = new JPanel();
        loanPanel.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel titleText = new JLabel();
        titleText.setText("MY LOANS");

        JButton backButton = new JButton("Back");

        headerPanel.add(backButton);
        headerPanel.add(titleText);

        JPanel boxesContainer = new JPanel();
        boxesContainer.setLayout(new BoxLayout(boxesContainer, BoxLayout.Y_AXIS));

        try {
            LoanedBook[] loanedBooks = new Loan().getloans();
            for (LoanedBook b : loanedBooks) {
                if (b.getUser().getUserName().equals(currentUser.getUserName())) {
                    boxesContainer.add(createLoanedBox(b));
                    boxesContainer.add(Box.createRigidArea(new Dimension(0, 15)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(boxesContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        loanPanel.add(headerPanel, BorderLayout.NORTH);
        loanPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(loanPanel);

        backButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            createLoggedInView(frame);
            frame.revalidate();
            frame.repaint();
        });
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

        JButton loanButton = new JButton("Loan book");
        JLabel statusLabel = new JLabel();

        boxPanel.add(new JLabel("Author: " + book.getAuthor()));
        boxPanel.add(new JLabel("Pages: " + book.getPages()));
        boxPanel.add(new JLabel("Language: " + book.getLanguage()));
        boxPanel.add(new JLabel("Year: " + book.getYear()));
        boxPanel.add(new JLabel("ISBN: " + book.getISBN()));
        boxPanel.add(loanButton);
        boxPanel.add(statusLabel);

        loanButton.addActionListener(e -> {
            LoanedBook newLoan = new LoanedBook(currentUser, LocalDate.now().toString(), 7.0, book);
            Loan loan = new Loan();
            loan.saveLoan(newLoan);
            setStatus("Loaned book!", statusLabel);
        });

        boxPanel.setMaximumSize(new Dimension(550, 150));

        return boxPanel;
    }

    private JPanel createLoanedBox(LoanedBook loanedBook) {
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

        boxPanel.setBorder(BorderFactory.createTitledBorder(loanedBook.getBook().getTitle()));

        JButton returnButton = new JButton("Return book");
        JLabel statusLabel = new JLabel();

        boxPanel.add(new JLabel("Loan date: " + loanedBook.getLoanDate()));
        boxPanel.add(new JLabel("Time left: " + loanedBook.getTimeLeft()));
        boxPanel.add(new JLabel("ISBN: " + loanedBook.getBook().getISBN()));
        boxPanel.add(returnButton);
        boxPanel.add(statusLabel);

        returnButton.addActionListener(e -> {
            setStatus("Returned book!", statusLabel);
        });

        boxPanel.setMaximumSize(new Dimension(550, 150));

        return boxPanel;
    }

    private void setStatus(String statusText, JLabel statusLabel) {
        statusLabel.setText(statusText);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        statusLabel.setText(null);
                    }
                },
                3000
        );
    }

    // Utility to horizontally center components
    private Component centerComponent(Component comp) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(comp);
        return wrapper;
    }
}