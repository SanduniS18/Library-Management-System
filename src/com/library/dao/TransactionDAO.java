package com.library.dao;

import com.library.db.DatabaseConnection;
import com.library.models.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // Issue a book
    public boolean issueBook(int bookId, int memberId, int dueDays) {
        String sql = "CALL IssueBook(?, ?, CURDATE(), ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, bookId);
            cstmt.setInt(2, memberId);
            cstmt.setInt(3, dueDays);

            rs = cstmt.executeQuery();
            if (rs.next()) {
                String result = rs.getString("result");
                return result.contains("successfully");
            }

        } catch (SQLException e) {
            System.out.println("Error issuing book: " + e.getMessage());
        } finally {
            closeResources(rs, cstmt, conn);
        }
        return false;
    }

    // Return a book
    public boolean returnBook(int transactionId, double finePerDay) {
        String sql = "CALL ReturnBook(?, CURDATE(), ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, transactionId);
            cstmt.setDouble(2, finePerDay);

            rs = cstmt.executeQuery();
            if (rs.next()) {
                String result = rs.getString("result");
                return result.contains("successfully");
            }

        } catch (SQLException e) {
            System.out.println("Error returning book: " + e.getMessage());
        } finally {
            closeResources(rs, cstmt, conn);
        }
        return false;
    }

    // Get all transactions
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY issue_date DESC";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return transactions;

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setBookId(rs.getInt("book_id"));
                transaction.setMemberId(rs.getInt("member_id"));
                transaction.setIssueDate(rs.getDate("issue_date"));
                transaction.setDueDate(rs.getDate("due_date"));
                transaction.setReturnDate(rs.getDate("return_date"));
                transaction.setFineAmount(rs.getDouble("fine_amount"));
                transaction.setStatus(rs.getString("status"));
                transaction.setNotes(rs.getString("notes"));
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            System.out.println("Error getting transactions: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return transactions;
    }

    // Get overdue books
    public List<Transaction> getOverdueBooks() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, b.title, m.name " +
                "FROM transactions t " +
                "JOIN books b ON t.book_id = b.book_id " +
                "JOIN members m ON t.member_id = m.member_id " +
                "WHERE t.due_date < CURDATE() AND t.status = 'Issued'";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return transactions;

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setBookId(rs.getInt("book_id"));
                transaction.setMemberId(rs.getInt("member_id"));
                transaction.setIssueDate(rs.getDate("issue_date"));
                transaction.setDueDate(rs.getDate("due_date"));
                transaction.setStatus(rs.getString("status"));
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            System.out.println("Error getting overdue books: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return transactions;
    }

    // Get current issued books for a member
    public List<Transaction> getCurrentIssues(int memberId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions " +
                "WHERE member_id = ? AND status IN ('Issued', 'Overdue')";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return transactions;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setBookId(rs.getInt("book_id"));
                transaction.setMemberId(rs.getInt("member_id"));
                transaction.setIssueDate(rs.getDate("issue_date"));
                transaction.setDueDate(rs.getDate("due_date"));
                transaction.setStatus(rs.getString("status"));
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            System.out.println("Error getting current issues: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, conn);
        }
        return transactions;
    }

    // Add searchTransactions method to TransactionDAO
    public List<Transaction> searchTransactions(String searchTerm) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, b.title, m.name " +
                "FROM transactions t " +
                "JOIN books b ON t.book_id = b.book_id " +
                "JOIN members m ON t.member_id = m.member_id " +
                "WHERE b.title LIKE ? OR m.name LIKE ? OR t.status LIKE ? " +
                "ORDER BY t.issue_date DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return transactions;

            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setBookId(rs.getInt("book_id"));
                transaction.setMemberId(rs.getInt("member_id"));
                transaction.setIssueDate(rs.getDate("issue_date"));
                transaction.setDueDate(rs.getDate("due_date"));
                transaction.setReturnDate(rs.getDate("return_date"));
                transaction.setFineAmount(rs.getDouble("fine_amount"));
                transaction.setStatus(rs.getString("status"));
                transaction.setNotes(rs.getString("notes"));
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            System.out.println("Error searching transactions: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, conn);
        }
        return transactions;
    }

    // Helper method to close resources
    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseConnection.closeConnection(conn);
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}