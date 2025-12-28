package com.library.dao;

import com.library.db.DatabaseConnection;
import com.library.models.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    // Add new book
    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, publication_year, " +
                "category, total_copies, available_copies) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setInt(4, book.getPublicationYear());
            pstmt.setString(5, book.getCategory());
            pstmt.setInt(6, book.getTotalCopies());
            pstmt.setInt(7, book.getAvailableCopies());

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
            return false;
        } finally {
            closeResources(null, pstmt, conn);
        }
    }

    // Get all books
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY title";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return books;

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setPublicationYear(rs.getInt("publication_year"));
                book.setCategory(rs.getString("category"));
                book.setTotalCopies(rs.getInt("total_copies"));
                book.setAvailableCopies(rs.getInt("available_copies"));
                book.setStatus(rs.getString("status"));
                books.add(book);
            }

        } catch (SQLException e) {
            System.out.println("Error getting books: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return books;
    }

    // Search books
    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ? OR category LIKE ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return books;

            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setPublicationYear(rs.getInt("publication_year"));
                book.setCategory(rs.getString("category"));
                book.setTotalCopies(rs.getInt("total_copies"));
                book.setAvailableCopies(rs.getInt("available_copies"));
                book.setStatus(rs.getString("status"));
                books.add(book);
            }

        } catch (SQLException e) {
            System.out.println("Error searching books: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, conn);
        }
        return books;
    }

    // Update book
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, " +
                "publication_year = ?, category = ?, total_copies = ?, " +
                "available_copies = ?, status = ? WHERE book_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setInt(4, book.getPublicationYear());
            pstmt.setString(5, book.getCategory());
            pstmt.setInt(6, book.getTotalCopies());
            pstmt.setInt(7, book.getAvailableCopies());
            pstmt.setString(8, book.getStatus());
            pstmt.setInt(9, book.getBookId());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.out.println("Error updating book: " + e.getMessage());
            return false;
        } finally {
            closeResources(null, pstmt, conn);
        }
    }

    // Delete book
    public boolean deleteBook(int bookId) {
        // First check if the book is currently issued
        String checkSql = "SELECT COUNT(*) as count FROM transactions WHERE book_id = ? AND status IN ('Issued', 'Overdue')";
        String deleteSql = "DELETE FROM books WHERE book_id = ?";

        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement deleteStmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            // Check if book is issued
            checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, bookId);
            rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt("count") > 0) {
                System.out.println("Cannot delete book: It is currently issued to a member");
                return false;
            }

            // Delete the book
            deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmt.setInt(1, bookId);
            int rowsDeleted = deleteStmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting book: " + e.getMessage());
            return false;
        } finally {
            closeResources(rs, checkStmt, null);
            closeResources(null, deleteStmt, conn);
        }
    }

    // Get total books count
    public int getTotalBooks() {
        String sql = "SELECT COUNT(*) as count FROM books";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return 0;

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return rs.getInt("count");
            }

        } catch (SQLException e) {
            System.out.println("Error getting total books count: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return 0;
    }

    // Get available books count
    public int getAvailableBooksCount() {
        String sql = "SELECT SUM(available_copies) as count FROM books";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return 0;

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return rs.getInt("count");
            }

        } catch (SQLException e) {
            System.out.println("Error getting available books count: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return 0;
    }

    // Get book by ID
    public Book getBookById(int bookId) {
        String sql = "SELECT * FROM books WHERE book_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return null;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setPublicationYear(rs.getInt("publication_year"));
                book.setCategory(rs.getString("category"));
                book.setTotalCopies(rs.getInt("total_copies"));
                book.setAvailableCopies(rs.getInt("available_copies"));
                book.setStatus(rs.getString("status"));
                return book;
            }

        } catch (SQLException e) {
            System.out.println("Error getting book by ID: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, conn);
        }
        return null;
    }

    // Get book by ISBN
    public Book getBookByISBN(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return null;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isbn);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setPublicationYear(rs.getInt("publication_year"));
                book.setCategory(rs.getString("category"));
                book.setTotalCopies(rs.getInt("total_copies"));
                book.setAvailableCopies(rs.getInt("available_copies"));
                book.setStatus(rs.getString("status"));
                return book;
            }

        } catch (SQLException e) {
            System.out.println("Error getting book by ISBN: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, conn);
        }
        return null;
    }

    // Update book status
    public boolean updateBookStatus(int bookId, String status) {
        String sql = "UPDATE books SET status = ? WHERE book_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, bookId);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.out.println("Error updating book status: " + e.getMessage());
            return false;
        } finally {
            closeResources(null, pstmt, conn);
        }
    }

    // Update available copies (when book is issued or returned)
    public boolean updateAvailableCopies(int bookId, int change) {
        String sql = "UPDATE books SET available_copies = available_copies + ? WHERE book_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, change);
            pstmt.setInt(2, bookId);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.out.println("Error updating available copies: " + e.getMessage());
            return false;
        } finally {
            closeResources(null, pstmt, conn);
        }
    }

    // Check if book exists
    public boolean bookExists(int bookId) {
        String sql = "SELECT COUNT(*) as count FROM books WHERE book_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error checking if book exists: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, conn);
        }
        return false;
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