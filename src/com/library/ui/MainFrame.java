package com.library.ui;

import com.library.models.Book;
import com.library.models.Member;
import com.library.models.Transaction;
import com.library.services.LibraryService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainFrame extends JFrame {
    private final LibraryService libraryService;
    private JTabbedPane tabbedPane;

    // Tables
    private JTable booksTable;
    private DefaultTableModel booksTableModel;
    private JTable membersTable;
    private DefaultTableModel membersTableModel;
    private JTable transactionsTable;
    private DefaultTableModel transactionsTableModel;

    // Search fields
    private JTextField bookSearchField;
    private JTextField memberSearchField;
    private JTextField transactionSearchField;

    public MainFrame() {
        libraryService = new LibraryService();
        initializeUI();
        loadInitialData();
    }

    private void initializeUI() {
        setTitle("📚 Library Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createMenuBar();
        createMainContent();
        createStatusBar();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(240, 240, 240));

        // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic('X');
        exitItem.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(exitItem);

        // Manage Menu
        JMenu manageMenu = new JMenu("Manage");
        manageMenu.setMnemonic('M');

        JMenuItem manageBooksItem = new JMenuItem("Books");
        JMenuItem manageMembersItem = new JMenuItem("Members");
        JMenuItem manageTransactionsItem = new JMenuItem("Transactions");

        manageBooksItem.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        manageMembersItem.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        manageTransactionsItem.addActionListener(e -> tabbedPane.setSelectedIndex(3));

        manageMenu.add(manageBooksItem);
        manageMenu.add(manageMembersItem);
        manageMenu.add(manageTransactionsItem);

        // Reports Menu
        JMenu reportsMenu = new JMenu("Reports");
        reportsMenu.setMnemonic('R');

        JMenuItem overdueReportItem = new JMenuItem("Overdue Books");
        JMenuItem statsReportItem = new JMenuItem("Statistics");
        JMenuItem fullReportItem = new JMenuItem("Full Report");

        overdueReportItem.addActionListener(e -> showOverdueReport());
        statsReportItem.addActionListener(e -> showStatistics());
        fullReportItem.addActionListener(e -> showFullReport());

        reportsMenu.add(overdueReportItem);
        reportsMenu.add(statsReportItem);
        reportsMenu.add(fullReportItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(manageMenu);
        menuBar.add(reportsMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void createMainContent() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Books", createBooksPanel());
        tabbedPane.addTab("Members", createMembersPanel());
        tabbedPane.addTab("Transactions", createTransactionsPanel());
        tabbedPane.addTab("Reports", createReportsPanel());

        add(tabbedPane);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(245, 245, 245));

        // Top statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        statsPanel.setBorder(new TitledBorder("Library Statistics"));
        statsPanel.setBackground(Color.WHITE);

        int totalBooks = libraryService.getTotalBooks();
        int availableBooks = libraryService.getAvailableBooksCount();
        int activeMembers = libraryService.getActiveMembers();
        List<Transaction> overdueBooks = libraryService.getOverdueBooks();

        statsPanel.add(createStatCard("📊 Total Books", String.valueOf(totalBooks), new Color(70, 130, 180)));
        statsPanel.add(createStatCard("📚 Available Books", String.valueOf(availableBooks), new Color(60, 179, 113)));
        statsPanel.add(createStatCard("🔒 Borrowed Books", String.valueOf(totalBooks - availableBooks), new Color(255, 140, 0)));
        statsPanel.add(createStatCard("👥 Active Members", String.valueOf(activeMembers), new Color(138, 43, 226)));
        statsPanel.add(createStatCard("⏰ Overdue Books", String.valueOf(overdueBooks.size()), new Color(220, 20, 60)));
        statsPanel.add(createStatCard("💰 Total Fines", "$0.00", new Color(255, 215, 0)));

        // Quick Actions Panel
        JPanel quickActionsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        quickActionsPanel.setBorder(new TitledBorder("Quick Actions"));
        quickActionsPanel.setBackground(Color.WHITE);

        JButton quickIssueBtn = createStyledButton("📖 Issue Book", new Color(70, 130, 180));
        JButton quickReturnBtn = createStyledButton("↩️ Return Book", new Color(60, 179, 113));
        JButton quickAddBookBtn = createStyledButton("➕ Add Book", new Color(138, 43, 226));

        quickIssueBtn.addActionListener(e -> showIssueBookDialog());
        quickReturnBtn.addActionListener(e -> showReturnBookDialog());
        quickAddBookBtn.addActionListener(e -> showAddBookDialog());

        quickActionsPanel.add(quickIssueBtn);
        quickActionsPanel.add(quickReturnBtn);
        quickActionsPanel.add(quickAddBookBtn);

        // Recent Activities Panel
        JPanel activitiesPanel = new JPanel(new BorderLayout());
        activitiesPanel.setBorder(new TitledBorder("📋 Recent Activities"));
        activitiesPanel.setBackground(Color.WHITE);

        JTextArea activitiesArea = new JTextArea();
        activitiesArea.setEditable(false);
        activitiesArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        activitiesArea.setText(getRecentActivities());

        JScrollPane scrollPane = new JScrollPane(activitiesArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        activitiesPanel.add(scrollPane);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.add(quickActionsPanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(activitiesPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top panel with buttons and search
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton addButton = createStyledButton("➕ Add New", new Color(60, 179, 113));
        JButton editButton = createStyledButton("✏️ Edit", new Color(255, 140, 0));
        JButton deleteButton = createStyledButton("🗑️ Delete", new Color(220, 20, 60));
        JButton refreshButton = createStyledButton("🔄 Refresh", new Color(70, 130, 180));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        searchPanel.setBackground(new Color(240, 240, 240));

        bookSearchField = new JTextField(20);
        JButton searchButton = createStyledButton("🔍 Search", new Color(138, 43, 226));
        JButton clearButton = createStyledButton("🗑️ Clear", Color.GRAY);

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(bookSearchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        // Table for books
        String[] columns = {"ID", "Title", "Author", "Category", "ISBN", "Year", "Available", "Total", "Status"};
        booksTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        booksTable = new JTable(booksTableModel);
        booksTable.setRowHeight(25);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        booksTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JTableHeader header = booksTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);

        JScrollPane tableScrollPane = new JScrollPane(booksTable);

        // Action listeners
        addButton.addActionListener(e -> showAddBookDialog());
        editButton.addActionListener(e -> editSelectedBook());
        deleteButton.addActionListener(e -> deleteSelectedBook());
        refreshButton.addActionListener(e -> loadBooksData());
        searchButton.addActionListener(e -> searchBooks());
        clearButton.addActionListener(e -> {
            bookSearchField.setText("");
            loadBooksData();
        });

        // Double-click to edit
        booksTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editSelectedBook();
                }
            }
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton addMemberBtn = createStyledButton("➕ Register Member", new Color(60, 179, 113));
        JButton editMemberBtn = createStyledButton("✏️ Edit", new Color(255, 140, 0));
        JButton deleteMemberBtn = createStyledButton("🗑️ Delete", new Color(220, 20, 60));
        JButton refreshMemberBtn = createStyledButton("🔄 Refresh", new Color(70, 130, 180));

        buttonPanel.add(addMemberBtn);
        buttonPanel.add(editMemberBtn);
        buttonPanel.add(deleteMemberBtn);
        buttonPanel.add(refreshMemberBtn);

        // Search for members
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        searchPanel.setBackground(new Color(240, 240, 240));

        memberSearchField = new JTextField(20);
        JButton searchMemberBtn = createStyledButton("🔍 Search", new Color(138, 43, 226));
        JButton clearMemberBtn = createStyledButton("🗑️ Clear", Color.GRAY);

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(memberSearchField);
        searchPanel.add(searchMemberBtn);
        searchPanel.add(clearMemberBtn);

        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        // Members table
        String[] columns = {"ID", "Name", "Email", "Phone", "Join Date", "Status", "Max Books"};
        membersTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        membersTable = new JTable(membersTableModel);
        membersTable.setRowHeight(25);

        JScrollPane tableScrollPane = new JScrollPane(membersTable);

        // Action listeners
        addMemberBtn.addActionListener(e -> showAddMemberDialog());
        editMemberBtn.addActionListener(e -> editSelectedMember());
        deleteMemberBtn.addActionListener(e -> deleteSelectedMember());
        refreshMemberBtn.addActionListener(e -> loadMembersData());
        searchMemberBtn.addActionListener(e -> searchMembers());
        clearMemberBtn.addActionListener(e -> {
            memberSearchField.setText("");
            loadMembersData();
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton issueBtn = createStyledButton("📖 Issue Book", new Color(60, 179, 113));
        JButton returnBtn = createStyledButton("↩️ Return Book", new Color(255, 140, 0));
        JButton refreshTransBtn = createStyledButton("🔄 Refresh", new Color(70, 130, 180));

        buttonPanel.add(issueBtn);
        buttonPanel.add(returnBtn);
        buttonPanel.add(refreshTransBtn);

        // Search for transactions
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        searchPanel.setBackground(new Color(240, 240, 240));

        transactionSearchField = new JTextField(20);
        JButton searchTransBtn = createStyledButton("🔍 Search", new Color(138, 43, 226));
        JButton clearTransBtn = createStyledButton("🗑️ Clear", Color.GRAY);

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(transactionSearchField);
        searchPanel.add(searchTransBtn);
        searchPanel.add(clearTransBtn);

        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        // Transactions table
        String[] columns = {"ID", "Book", "Member", "Issue Date", "Due Date", "Return Date", "Fine", "Status"};
        transactionsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionsTable = new JTable(transactionsTableModel);
        transactionsTable.setRowHeight(25);

        JScrollPane tableScrollPane = new JScrollPane(transactionsTable);

        // Action listeners
        issueBtn.addActionListener(e -> showIssueBookDialog());
        returnBtn.addActionListener(e -> showReturnBookDialog());
        refreshTransBtn.addActionListener(e -> loadTransactionsData());
        searchTransBtn.addActionListener(e -> searchTransactions());
        clearTransBtn.addActionListener(e -> {
            transactionSearchField.setText("");
            loadTransactionsData();
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(245, 245, 245));

        JTabbedPane reportsTabbedPane = new JTabbedPane();

        // Overdue Books Report
        JPanel overduePanel = new JPanel(new BorderLayout());
        JTextArea overdueArea = new JTextArea();
        overdueArea.setEditable(false);
        overdueArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        overduePanel.add(new JScrollPane(overdueArea), BorderLayout.CENTER);

        JButton generateOverdueBtn = createStyledButton("🔄 Generate Overdue Report", new Color(220, 20, 60));
        generateOverdueBtn.addActionListener(e -> {
            List<Transaction> overdue = libraryService.getOverdueBooks();
            StringBuilder report = new StringBuilder("=== OVERDUE BOOKS REPORT ===\n\n");
            report.append("Generated: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
            report.append("Total Overdue: ").append(overdue.size()).append("\n\n");

            for (Transaction t : overdue) {
                report.append(String.format("Transaction ID: %d\n", t.getTransactionId()));
                report.append(String.format("Book ID: %d\n", t.getBookId()));
                report.append(String.format("Member ID: %d\n", t.getMemberId()));
                report.append(String.format("Due Date: %s\n", t.getDueDate()));
                report.append("------------------------------------\n");
            }

            overdueArea.setText(report.toString());
        });
        overduePanel.add(generateOverdueBtn, BorderLayout.SOUTH);

        // Statistics Report
        JPanel statsPanel = new JPanel(new BorderLayout());
        JTextArea statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statsPanel.add(new JScrollPane(statsArea), BorderLayout.CENTER);

        JButton generateStatsBtn = createStyledButton("📊 Generate Statistics", new Color(70, 130, 180));
        generateStatsBtn.addActionListener(e -> {
            statsArea.setText(libraryService.generateLibraryReport());
        });
        statsPanel.add(generateStatsBtn, BorderLayout.SOUTH);

        // Transaction History
        JPanel historyPanel = new JPanel(new BorderLayout());
        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        historyPanel.add(new JScrollPane(historyArea), BorderLayout.CENTER);

        JButton generateHistoryBtn = createStyledButton("📜 Generate History", new Color(138, 43, 226));
        generateHistoryBtn.addActionListener(e -> {
            List<Transaction> transactions = libraryService.getAllTransactions();
            StringBuilder report = new StringBuilder("=== TRANSACTION HISTORY ===\n\n");

            for (Transaction t : transactions) {
                report.append(String.format("ID: %d | Book: %d | Member: %d\n",
                        t.getTransactionId(), t.getBookId(), t.getMemberId()));
                report.append(String.format("Issue: %s | Due: %s | Status: %s\n",
                        t.getIssueDate(), t.getDueDate(), t.getStatus()));
                report.append("------------------------------------\n");
            }

            historyArea.setText(report.toString());
        });
        historyPanel.add(generateHistoryBtn, BorderLayout.SOUTH);

        reportsTabbedPane.addTab("Overdue Books", overduePanel);
        reportsTabbedPane.addTab("Statistics", statsPanel);
        reportsTabbedPane.addTab("Transaction History", historyPanel);

        panel.add(reportsTabbedPane, BorderLayout.CENTER);

        return panel;
    }

    private void createStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusPanel.setBackground(new Color(240, 240, 240));

        JLabel statusLabel = new JLabel(" Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        JLabel timeLabel = new JLabel();
        timeLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        // Update time every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLabel.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            }
        });
        timer.start();

        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(timeLabel, BorderLayout.EAST);

        add(statusPanel, BorderLayout.SOUTH);
    }

    // ========== HELPER METHODS ==========

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private String getRecentActivities() {
        StringBuilder activities = new StringBuilder();
        activities.append("• Library system started\n");
        activities.append("• Loaded all books and members\n");
        activities.append("• Database connection established\n");
        activities.append("• Ready for operations\n");
        return activities.toString();
    }

    // ========== DATA LOADING METHODS ==========

    private void loadInitialData() {
        loadBooksData();
        loadMembersData();
        loadTransactionsData();
    }

    private void loadBooksData() {
        booksTableModel.setRowCount(0);
        List<Book> books = libraryService.getAllBooks();
        for (Book book : books) {
            Object[] row = {
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getCategory(),
                    book.getIsbn(),
                    book.getPublicationYear(),
                    book.getAvailableCopies(),
                    book.getTotalCopies(),
                    book.getStatus()
            };
            booksTableModel.addRow(row);
        }
    }

    private void loadMembersData() {
        membersTableModel.setRowCount(0);
        List<Member> members = libraryService.getAllMembers();
        for (Member member : members) {
            Object[] row = {
                    member.getMemberId(),
                    member.getName(),
                    member.getEmail(),
                    member.getPhone(),
                    member.getJoinDate(),
                    member.getStatus(),
                    member.getMaxBooksAllowed()
            };
            membersTableModel.addRow(row);
        }
    }

    private void loadTransactionsData() {
        transactionsTableModel.setRowCount(0);
        List<Transaction> transactions = libraryService.getAllTransactions();
        for (Transaction t : transactions) {
            Object[] row = {
                    t.getTransactionId(),
                    "Book #" + t.getBookId(),
                    "Member #" + t.getMemberId(),
                    t.getIssueDate(),
                    t.getDueDate(),
                    t.getReturnDate(),
                    String.format("$%.2f", t.getFineAmount()),
                    t.getStatus()
            };
            transactionsTableModel.addRow(row);
        }
    }

    // ========== MISSING METHOD IMPLEMENTATIONS ==========

    private void editSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to edit!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookId = (int) booksTableModel.getValueAt(selectedRow, 0);
        Book book = libraryService.getBookById(bookId);
        if (book == null) {
            JOptionPane.showMessageDialog(this, "Book not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Book", true);
        dialog.setSize(400, 450);
        dialog.setLayout(new GridLayout(9, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField titleField = new JTextField(book.getTitle());
        JTextField authorField = new JTextField(book.getAuthor());
        JTextField isbnField = new JTextField(book.getIsbn());
        JTextField yearField = new JTextField(String.valueOf(book.getPublicationYear()));
        JTextField categoryField = new JTextField(book.getCategory());
        JSpinner totalCopiesSpinner = new JSpinner(new SpinnerNumberModel(book.getTotalCopies(), 1, 100, 1));
        JSpinner availableCopiesSpinner = new JSpinner(new SpinnerNumberModel(book.getAvailableCopies(), 0, 100, 1));

        dialog.add(new JLabel("Title:"));
        dialog.add(titleField);
        dialog.add(new JLabel("Author:"));
        dialog.add(authorField);
        dialog.add(new JLabel("ISBN:"));
        dialog.add(isbnField);
        dialog.add(new JLabel("Publication Year:"));
        dialog.add(yearField);
        dialog.add(new JLabel("Category:"));
        dialog.add(categoryField);
        dialog.add(new JLabel("Total Copies:"));
        dialog.add(totalCopiesSpinner);
        dialog.add(new JLabel("Available Copies:"));
        dialog.add(availableCopiesSpinner);

        JButton saveButton = createStyledButton("💾 Save", new Color(60, 179, 113));
        JButton cancelButton = createStyledButton("❌ Cancel", new Color(220, 20, 60));

        dialog.add(saveButton);
        dialog.add(cancelButton);

        saveButton.addActionListener(e -> {
            try {
                book.setTitle(titleField.getText());
                book.setAuthor(authorField.getText());
                book.setIsbn(isbnField.getText());
                book.setPublicationYear(Integer.parseInt(yearField.getText()));
                book.setCategory(categoryField.getText());
                book.setTotalCopies((int) totalCopiesSpinner.getValue());
                book.setAvailableCopies((int) availableCopiesSpinner.getValue());

                if (libraryService.updateBook(book)) {
                    JOptionPane.showMessageDialog(dialog, "Book updated successfully!");
                    loadBooksData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update book!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid year!");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void deleteSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookId = (int) booksTableModel.getValueAt(selectedRow, 0);
        String bookTitle = (String) booksTableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete book: " + bookTitle + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (libraryService.deleteBook(bookId)) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully!");
                loadBooksData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete book!");
            }
        }
    }

    private void searchBooks() {
        String searchTerm = bookSearchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadBooksData();
            return;
        }

        List<Book> books = libraryService.searchBooks(searchTerm);
        booksTableModel.setRowCount(0);

        for (Book book : books) {
            Object[] row = {
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getCategory(),
                    book.getIsbn(),
                    book.getPublicationYear(),
                    book.getAvailableCopies(),
                    book.getTotalCopies(),
                    book.getStatus()
            };
            booksTableModel.addRow(row);
        }
    }

    private void editSelectedMember() {
        int selectedRow = membersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member to edit!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int memberId = (int) membersTableModel.getValueAt(selectedRow, 0);
        Member member = libraryService.getMemberById(memberId);
        if (member == null) {
            JOptionPane.showMessageDialog(this, "Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Member", true);
        dialog.setSize(400, 350);
        dialog.setLayout(new GridLayout(8, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField nameField = new JTextField(member.getName());
        JTextField emailField = new JTextField(member.getEmail());
        JTextField phoneField = new JTextField(member.getPhone());
        JTextField addressField = new JTextField(member.getAddress());
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Inactive", "Suspended"});
        statusCombo.setSelectedItem(member.getStatus());
        JSpinner maxBooksSpinner = new JSpinner(new SpinnerNumberModel(member.getMaxBooksAllowed(), 1, 10, 1));

        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);
        dialog.add(new JLabel("Phone:"));
        dialog.add(phoneField);
        dialog.add(new JLabel("Address:"));
        dialog.add(addressField);
        dialog.add(new JLabel("Status:"));
        dialog.add(statusCombo);
        dialog.add(new JLabel("Max Books Allowed:"));
        dialog.add(maxBooksSpinner);

        JButton saveBtn = createStyledButton("💾 Save", new Color(60, 179, 113));
        JButton cancelBtn = createStyledButton("❌ Cancel", new Color(220, 20, 60));

        dialog.add(saveBtn);
        dialog.add(cancelBtn);

        saveBtn.addActionListener(e -> {
            member.setName(nameField.getText());
            member.setEmail(emailField.getText());
            member.setPhone(phoneField.getText());
            member.setAddress(addressField.getText());
            member.setStatus((String) statusCombo.getSelectedItem());
            member.setMaxBooksAllowed((int) maxBooksSpinner.getValue());

            if (libraryService.updateMember(member)) {
                JOptionPane.showMessageDialog(dialog, "Member updated successfully!");
                loadMembersData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to update member!");
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void deleteSelectedMember() {
        int selectedRow = membersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member to delete!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int memberId = (int) membersTableModel.getValueAt(selectedRow, 0);
        String memberName = (String) membersTableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete member: " + memberName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (libraryService.deleteMember(memberId)) {
                JOptionPane.showMessageDialog(this, "Member deleted successfully!");
                loadMembersData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete member!");
            }
        }
    }

    private void searchMembers() {
        String searchTerm = memberSearchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadMembersData();
            return;
        }

        List<Member> members = libraryService.searchMembers(searchTerm);
        membersTableModel.setRowCount(0);

        for (Member member : members) {
            Object[] row = {
                    member.getMemberId(),
                    member.getName(),
                    member.getEmail(),
                    member.getPhone(),
                    member.getJoinDate(),
                    member.getStatus(),
                    member.getMaxBooksAllowed()
            };
            membersTableModel.addRow(row);
        }
    }

    private void searchTransactions() {
        String searchTerm = transactionSearchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadTransactionsData();
            return;
        }

        List<Transaction> transactions = libraryService.searchTransactions(searchTerm);
        transactionsTableModel.setRowCount(0);

        for (Transaction t : transactions) {
            Object[] row = {
                    t.getTransactionId(),
                    "Book #" + t.getBookId(),
                    "Member #" + t.getMemberId(),
                    t.getIssueDate(),
                    t.getDueDate(),
                    t.getReturnDate(),
                    String.format("$%.2f", t.getFineAmount()),
                    t.getStatus()
            };
            transactionsTableModel.addRow(row);
        }
    }

    // ========== DIALOG METHODS ==========

    private void showIssueBookDialog() {
        JDialog dialog = new JDialog(this, "Issue Book", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField bookIdField = new JTextField();
        JTextField memberIdField = new JTextField();
        JSpinner daysSpinner = new JSpinner(new SpinnerNumberModel(14, 1, 60, 1));

        dialog.add(new JLabel("Book ID:"));
        dialog.add(bookIdField);
        dialog.add(new JLabel("Member ID:"));
        dialog.add(memberIdField);
        dialog.add(new JLabel("Days to Issue:"));
        dialog.add(daysSpinner);

        JButton issueBtn = createStyledButton("📖 Issue", new Color(60, 179, 113));
        JButton cancelBtn = createStyledButton("❌ Cancel", new Color(220, 20, 60));

        dialog.add(issueBtn);
        dialog.add(cancelBtn);

        issueBtn.addActionListener(e -> {
            try {
                int bookId = Integer.parseInt(bookIdField.getText());
                int memberId = Integer.parseInt(memberIdField.getText());
                int days = (int) daysSpinner.getValue();

                if (libraryService.issueBook(bookId, memberId, days)) {
                    JOptionPane.showMessageDialog(dialog, "Book issued successfully!");
                    loadBooksData();
                    loadTransactionsData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to issue book!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers!");
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void showReturnBookDialog() {
        JDialog dialog = new JDialog(this, "Return Book", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField transIdField = new JTextField();
        JSpinner fineSpinner = new JSpinner(new SpinnerNumberModel(0.50, 0.0, 10.0, 0.10));

        dialog.add(new JLabel("Transaction ID:"));
        dialog.add(transIdField);
        dialog.add(new JLabel("Fine per Day ($):"));
        dialog.add(fineSpinner);

        JButton returnBtn = createStyledButton("↩️ Return", new Color(60, 179, 113));
        JButton cancelBtn = createStyledButton("❌ Cancel", new Color(220, 20, 60));

        dialog.add(returnBtn);
        dialog.add(cancelBtn);

        returnBtn.addActionListener(e -> {
            try {
                int transId = Integer.parseInt(transIdField.getText());
                double fine = (double) fineSpinner.getValue();

                if (libraryService.returnBook(transId, fine)) {
                    JOptionPane.showMessageDialog(dialog, "Book returned successfully!");
                    loadBooksData();
                    loadTransactionsData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to return book!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid transaction ID!");
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void showAddMemberDialog() {
        JDialog dialog = new JDialog(this, "Register New Member", true);
        dialog.setSize(400, 350);
        dialog.setLayout(new GridLayout(8, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Inactive", "Suspended"});
        JSpinner maxBooksSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));

        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);
        dialog.add(new JLabel("Phone:"));
        dialog.add(phoneField);
        dialog.add(new JLabel("Address:"));
        dialog.add(addressField);
        dialog.add(new JLabel("Status:"));
        dialog.add(statusCombo);
        dialog.add(new JLabel("Max Books Allowed:"));
        dialog.add(maxBooksSpinner);

        JButton saveBtn = createStyledButton("💾 Save", new Color(60, 179, 113));
        JButton cancelBtn = createStyledButton("❌ Cancel", new Color(220, 20, 60));

        dialog.add(saveBtn);
        dialog.add(cancelBtn);

        saveBtn.addActionListener(e -> {
            Member member = new Member();
            member.setName(nameField.getText());
            member.setEmail(emailField.getText());
            member.setPhone(phoneField.getText());
            member.setAddress(addressField.getText());
            member.setStatus((String) statusCombo.getSelectedItem());
            member.setMaxBooksAllowed((int) maxBooksSpinner.getValue());
            member.setJoinDate(new java.sql.Date(System.currentTimeMillis()));

            if (libraryService.registerMember(member)) {
                JOptionPane.showMessageDialog(dialog, "Member registered successfully!");
                loadMembersData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to register member!");
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void showAddBookDialog() {
        JDialog dialog = new JDialog(this, "Add New Book", true);
        dialog.setSize(400, 450);
        dialog.setLayout(new GridLayout(9, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField categoryField = new JTextField();
        JSpinner totalCopiesSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JSpinner availableCopiesSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));

        dialog.add(new JLabel("Title:"));
        dialog.add(titleField);
        dialog.add(new JLabel("Author:"));
        dialog.add(authorField);
        dialog.add(new JLabel("ISBN:"));
        dialog.add(isbnField);
        dialog.add(new JLabel("Publication Year:"));
        dialog.add(yearField);
        dialog.add(new JLabel("Category:"));
        dialog.add(categoryField);
        dialog.add(new JLabel("Total Copies:"));
        dialog.add(totalCopiesSpinner);
        dialog.add(new JLabel("Available Copies:"));
        dialog.add(availableCopiesSpinner);

        JButton saveButton = createStyledButton("💾 Save", new Color(60, 179, 113));
        JButton cancelButton = createStyledButton("❌ Cancel", new Color(220, 20, 60));

        dialog.add(saveButton);
        dialog.add(cancelButton);

        saveButton.addActionListener(e -> {
            try {
                Book book = new Book();
                book.setTitle(titleField.getText());
                book.setAuthor(authorField.getText());
                book.setIsbn(isbnField.getText());
                book.setPublicationYear(Integer.parseInt(yearField.getText()));
                book.setCategory(categoryField.getText());
                book.setTotalCopies((int) totalCopiesSpinner.getValue());
                book.setAvailableCopies((int) availableCopiesSpinner.getValue());

                if (libraryService.addNewBook(book)) {
                    JOptionPane.showMessageDialog(dialog, "Book added successfully!");
                    loadBooksData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add book!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid year!");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    // ========== OTHER METHODS ==========

    private void showOverdueReport() {
        JOptionPane.showMessageDialog(this,
                "Overdue Report feature will be implemented soon!",
                "Overdue Report",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showStatistics() {
        JOptionPane.showMessageDialog(this,
                libraryService.generateLibraryReport(),
                "Library Statistics",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showFullReport() {
        JOptionPane.showMessageDialog(this,
                "Full Report feature will be implemented soon!",
                "Full Report",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAboutDialog() {
        String aboutMessage = "📚 Library Management System\n" +
                "Version: 1.0.0\n" +
                "Developed for CS50 Final Project\n" +
                "Features:\n" +
                "• Book Management\n" +
                "• Member Management\n" +
                "• Transaction Tracking\n" +
                "• Report Generation\n" +
                "• Database Integration";

        JOptionPane.showMessageDialog(this,
                aboutMessage,
                "About Library System",
                JOptionPane.INFORMATION_MESSAGE);
    }
}