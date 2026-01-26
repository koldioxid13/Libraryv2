public class LoanedBook {
    private User user;
    private String loanDate;
    private Double timeLeft;
    private Book book;

    public LoanedBook(User user, String loanDate, Double timeLeft, Book book) {
        this.user = user;
        this.loanDate = loanDate;
        this.timeLeft = timeLeft;
        this.book = book;
    }

    public LoanedBook() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public Double getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Double timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String toFileFormat() {
        return "User:" + user.getUserName() +
                " | Date:" + loanDate +
                " | Book:" + book.getTitle() +
                " | ISBN:" + book.getISBN();
    }
}
