import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Main {

    private ArrayList<Book> books = new ArrayList<>();

    public static void main(String[] args) {
        // Set the look and feel to Nimbus for a modern theme
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Main().createGUI();
    }

    // Create GUI method
    public void createGUI() {
        // Create the main JFrame
        JFrame frame = new JFrame("Library Management System");
        frame.setSize(600, 450); // Reduced width and height
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Top panel for the title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(50, 150, 250)); // Sky Blue
        JLabel title = new JLabel("Library Management System");
        title.setFont(new Font("Verdana", Font.BOLD, 28));
        title.setForeground(Color.WHITE); // White text
        titlePanel.add(title);
        frame.add(titlePanel, BorderLayout.NORTH);

        // Middle panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 2, 20, 20)); // More spacing between buttons
        buttonPanel.setBackground(new Color(240, 248, 255)); // Alice Blue
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the button panel

        // Add buttons with customized color and rounded corners
        String[] buttonLabels = {"Add Book", "Display Books", "Search Book", "Issue Book",
                "Return Book", "View Issued Books", "Delete Book", "Exit"};
        JButton[] buttons = new JButton[buttonLabels.length];

        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = createRoundedButton(buttonLabels[i]);
            buttonPanel.add(buttons[i]);
        }

        frame.add(buttonPanel, BorderLayout.CENTER);

        // Footer panel for extra info or branding
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(70, 130, 180)); // Steel Blue
        JLabel footerLabel = new JLabel("Developed by Mohd Junaid Khan");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Verdana", Font.ITALIC, 12));
        footerPanel.add(footerLabel);
        frame.add(footerPanel, BorderLayout.SOUTH);

        // Set action listeners for each button
        buttons[0].addActionListener(e -> addBook());
        buttons[1].addActionListener(e -> displayBooks());
        buttons[2].addActionListener(e -> searchBook());
        buttons[3].addActionListener(e -> issueBook());
        buttons[4].addActionListener(e -> returnBook());
        buttons[5].addActionListener(e -> viewIssuedBooks());
        buttons[6].addActionListener(e -> deleteBook());
        buttons[7].addActionListener(e -> System.exit(0));  // Exit the application

        // Set window visible
        frame.setVisible(true);
    }

    // Method to create rounded buttons
    private JButton createRoundedButton(String label) {
        JButton button = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Rounded corners
                super.paintComponent(g);
            }
        };
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 144, 255)); // Dodger Blue
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false); // Removes border highlight
        return button;
    }

    // Methods for adding, displaying, searching, issuing, returning, and deleting books

    private void addBook() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                // Perform long-running task (e.g., adding a book)
                String id = JOptionPane.showInputDialog("Enter Book ID:");
                String title = JOptionPane.showInputDialog("Enter Book Title:");
                String author = JOptionPane.showInputDialog("Enter Author:");

                // Simulate a slight delay for demonstration purposes
                try {
                    Thread.sleep(200); // Example: simulate some processing time
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Book newBook = new Book(Integer.parseInt(id), title, author);
                books.add(newBook);
                return null;
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(null, "Book added successfully!");
            }
        };
        worker.execute();
    }

    private void displayBooks() {
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No books available.");
        } else {
            JTable table = new JTable(new BookTableModel(books));
            JOptionPane.showMessageDialog(null, new JScrollPane(table));
        }
    }

    private void searchBook() {
        String searchTerm = JOptionPane.showInputDialog("Enter title or author to search:");
        ArrayList<Book> results = new ArrayList<>();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) ||
                book.getAuthor().toLowerCase().contains(searchTerm.toLowerCase())) {
                results.add(book);
            }
        }

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No books found.");
        } else {
            JTable table = new JTable(new BookTableModel(results));
            JOptionPane.showMessageDialog(null, new JScrollPane(table));
        }
    }

    private void issueBook() {
        String id = JOptionPane.showInputDialog("Enter Book ID to Issue:");
        for (Book book : books) {
            if (book.getId() == Integer.parseInt(id) && !book.isIssued()) {
                book.setIssued(true);
                JOptionPane.showMessageDialog(null, "Book issued successfully!");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Book not found or already issued.");
    }

    private void returnBook() {
        String id = JOptionPane.showInputDialog("Enter Book ID to Return:");
        for (Book book : books) {
            if (book.getId() == Integer.parseInt(id) && book.isIssued()) {
                book.setIssued(false);
                JOptionPane.showMessageDialog(null, "Book returned successfully!");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Book not found or not issued.");
    }

    private void viewIssuedBooks() {
        ArrayList<Book> issuedBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isIssued()) {
                issuedBooks.add(book);
            }
        }

        if (issuedBooks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No books are currently issued.");
        } else {
            JTable table = new JTable(new BookTableModel(issuedBooks));
            JOptionPane.showMessageDialog(null, new JScrollPane(table));
        }
    }

    private void deleteBook() {
        String id = JOptionPane.showInputDialog("Enter Book ID to Delete:");
        for (Book book : books) {
            if (book.getId() == Integer.parseInt(id)) {
                books.remove(book);
                JOptionPane.showMessageDialog(null, "Book deleted successfully!");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Book not found.");
    }
}

// Additional classes for Book and BookTableModel

class Book {
    private int id;
    private String title;
    private String author;
    private boolean issued;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.issued = false;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isIssued() { return issued; }
    public void setIssued(boolean issued) { this.issued = issued; }
}

class BookTableModel extends javax.swing.table.AbstractTableModel {
    private final String[] columnNames = {"ID", "Title", "Author", "Issued"};
    private final ArrayList<Book> books;

    public BookTableModel(ArrayList<Book> books) {
        this.books = books;
    }

    @Override
    public int getRowCount() {
        return books.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = books.get(rowIndex);
        switch (columnIndex) {
            case 0: return book.getId();
            case 1: return book.getTitle();
            case 2: return book.getAuthor();
            case 3: return book.isIssued() ? "Yes" : "No";
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
