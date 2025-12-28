package com.library.models;

import java.sql.Date;

public class Transaction {
    private int transactionId;
    private int bookId;
    private int memberId;
    private Date issueDate;
    private Date dueDate;
    private Date returnDate;
    private double fineAmount;
    private String status;
    private String notes;

    // Additional fields for display (not in database)
    private String bookTitle;    // For UI display
    private String memberName;   // For UI display

    // Constructors
    public Transaction() {}

    public Transaction(int transactionId, int bookId, int memberId, Date issueDate,
                       Date dueDate, Date returnDate, double fineAmount,
                       String status, String notes) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
        this.status = status;
        this.notes = notes;
    }

    // Constructor for creating new transactions
    public Transaction(int bookId, int memberId, Date issueDate,
                       Date dueDate, String status) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = status;
        this.fineAmount = 0.0;
    }

    // Getters and Setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }

    public double getFineAmount() { return fineAmount; }
    public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    // Additional getters and setters for display fields
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    // Utility methods
    public boolean isOverdue() {
        if (returnDate != null) return false; // Already returned
        if (dueDate == null) return false;

        Date today = new Date(System.currentTimeMillis());
        return today.after(dueDate) && ("Issued".equals(status) || "Overdue".equals(status));
    }

    public int calculateOverdueDays() {
        if (!isOverdue() || dueDate == null) return 0;

        Date today = new Date(System.currentTimeMillis());
        long diff = today.getTime() - dueDate.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24)); // Convert milliseconds to days
    }

    public double calculateFine(double finePerDay) {
        if (!isOverdue()) return 0.0;
        return calculateOverdueDays() * finePerDay;
    }

    public boolean isIssued() {
        return "Issued".equals(status);
    }

    public boolean isReturned() {
        return "Returned".equals(status);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", bookId=" + bookId +
                ", memberId=" + memberId +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", fineAmount=" + fineAmount +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}