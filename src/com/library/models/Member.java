package com.library.models;

import java.sql.Date;

public class Member {
    private int memberId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Date joinDate;
    private String status;
    private int maxBooksAllowed;

    // Constructors
    public Member() {}

    public Member(int memberId, String name, String email, String phone,
                  String address, Date joinDate, String status, int maxBooksAllowed) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.joinDate = joinDate;
        this.status = status;
        this.maxBooksAllowed = maxBooksAllowed;
    }

    // Overloaded constructor without ID (for creating new members)
    public Member(String name, String email, String phone,
                  String address, Date joinDate, String status, int maxBooksAllowed) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.joinDate = joinDate;
        this.status = status;
        this.maxBooksAllowed = maxBooksAllowed;
    }

    // Getters and Setters
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Date getJoinDate() { return joinDate; }
    public void setJoinDate(Date joinDate) { this.joinDate = joinDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getMaxBooksAllowed() { return maxBooksAllowed; }
    public void setMaxBooksAllowed(int maxBooksAllowed) { this.maxBooksAllowed = maxBooksAllowed; }

    // Utility method to check if member can borrow more books
    public boolean canBorrowMoreBooks(int currentlyBorrowed) {
        return "Active".equals(status) && currentlyBorrowed < maxBooksAllowed;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                ", maxBooksAllowed=" + maxBooksAllowed +
                '}';
    }
}
