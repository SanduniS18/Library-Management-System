package com.library.services;

import com.library.dao.BookDAO;
import com.library.dao.MemberDAO;
import com.library.dao.TransactionDAO;
import com.library.models.Book;
import com.library.models.Member;
import com.library.models.Transaction;

import java.util.List;

public class LibraryService {
    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;
    private final TransactionDAO transactionDAO;

    public LibraryService() {
        this.bookDAO = new BookDAO();
        this.memberDAO = new MemberDAO();
        this.transactionDAO = new TransactionDAO();
    }

    // Book-related methods
    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    public int getTotalBooks() {
        return bookDAO.getTotalBooks();
    }

    public int getAvailableBooksCount() {
        return bookDAO.getAvailableBooksCount();
    }

    public Book getBookById(int bookId) {
        return bookDAO.getBookById(bookId);
    }

    public boolean addNewBook(Book book) {
        return bookDAO.addBook(book);
    }

    public boolean updateBook(Book book) {
        return bookDAO.updateBook(book);
    }

    public boolean deleteBook(int bookId) {
        return bookDAO.deleteBook(bookId);
    }

    public List<Book> searchBooks(String searchTerm) {
        return bookDAO.searchBooks(searchTerm);
    }

    // Member-related methods
    public List<Member> getAllMembers() {
        return memberDAO.getAllMembers();
    }

    public int getActiveMembers() {
        return memberDAO.getActiveMembersCount();
    }

    public Member getMemberById(int memberId) {
        return memberDAO.getMemberById(memberId);
    }

    public boolean registerMember(Member member) {
        return memberDAO.addMember(member);
    }

    public boolean updateMember(Member member) {
        return memberDAO.updateMember(member);
    }

    public boolean deleteMember(int memberId) {
        return memberDAO.deleteMember(memberId);
    }

    public List<Member> searchMembers(String searchTerm) {
        return memberDAO.searchMembers(searchTerm);
    }

    // Transaction-related methods
    public List<Transaction> getAllTransactions() {
        return transactionDAO.getAllTransactions();
    }

    public List<Transaction> getOverdueBooks() {
        return transactionDAO.getOverdueBooks();
    }

    public boolean issueBook(int bookId, int memberId, int days) {
        return transactionDAO.issueBook(bookId, memberId, days);
    }

    public boolean returnBook(int transactionId, double finePerDay) {
        return transactionDAO.returnBook(transactionId, finePerDay);
    }

    public List<Transaction> searchTransactions(String searchTerm) {
        return transactionDAO.searchTransactions(searchTerm);
    }

    // Report generation
    public String generateLibraryReport() {
        int totalBooks = getTotalBooks();
        int availableBooks = getAvailableBooksCount();
        int borrowedBooks = totalBooks - availableBooks;
        int activeMembers = getActiveMembers();
        int overdueBooks = getOverdueBooks().size();

        StringBuilder report = new StringBuilder();
        report.append("=== LIBRARY STATISTICS REPORT ===\n\n");
        report.append("Total Books: ").append(totalBooks).append("\n");
        report.append("Available Books: ").append(availableBooks).append("\n");
        report.append("Borrowed Books: ").append(borrowedBooks).append("\n");
        report.append("Active Members: ").append(activeMembers).append("\n");
        report.append("Overdue Books: ").append(overdueBooks).append("\n\n");

        if (overdueBooks > 0) {
            report.append("⚠️  There are ").append(overdueBooks).append(" overdue books!\n");
        } else {
            report.append("✅ All books are returned on time!\n");
        }

        return report.toString();
    }
}