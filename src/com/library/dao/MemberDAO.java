package com.library.dao;

import com.library.db.DatabaseConnection;
import com.library.models.Member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    // Add new member
    public boolean addMember(Member member) {
        String sql = "INSERT INTO members (name, email, phone, address, join_date, status, max_books_allowed) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getPhone());
            pstmt.setString(4, member.getAddress());
            pstmt.setDate(5, member.getJoinDate());
            pstmt.setString(6, member.getStatus());
            pstmt.setInt(7, member.getMaxBooksAllowed());

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Error adding member: " + e.getMessage());
            return false;
        } finally {
            closeResources(null, pstmt, conn);
        }
    }

    // Get all members
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY name";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return members;

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setName(rs.getString("name"));
                member.setEmail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setAddress(rs.getString("address"));
                member.setJoinDate(rs.getDate("join_date"));
                member.setStatus(rs.getString("status"));
                member.setMaxBooksAllowed(rs.getInt("max_books_allowed"));
                members.add(member);
            }

        } catch (SQLException e) {
            System.out.println("Error getting members: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return members;
    }

    // Get member by ID
    public Member getMemberById(int memberId) {
        String sql = "SELECT * FROM members WHERE member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return null;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setName(rs.getString("name"));
                member.setEmail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setAddress(rs.getString("address"));
                member.setJoinDate(rs.getDate("join_date"));
                member.setStatus(rs.getString("status"));
                member.setMaxBooksAllowed(rs.getInt("max_books_allowed"));
                return member;
            }

        } catch (SQLException e) {
            System.out.println("Error getting member: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, conn);
        }
        return null;
    }

    // Update member
    public boolean updateMember(Member member) {
        String sql = "UPDATE members SET name = ?, email = ?, phone = ?, address = ?, " +
                "status = ?, max_books_allowed = ? WHERE member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getPhone());
            pstmt.setString(4, member.getAddress());
            pstmt.setString(5, member.getStatus());
            pstmt.setInt(6, member.getMaxBooksAllowed());
            pstmt.setInt(7, member.getMemberId());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.out.println("Error updating member: " + e.getMessage());
            return false;
        } finally {
            closeResources(null, pstmt, conn);
        }
    }

    // Delete member
    public boolean deleteMember(int memberId) {
        String sql = "DELETE FROM members WHERE member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting member: " + e.getMessage());
            return false;
        } finally {
            closeResources(null, pstmt, conn);
        }
    }

    // Get count of active members
    public int getActiveMembersCount() {
        String sql = "SELECT COUNT(*) as count FROM members WHERE status = 'Active'";

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
            System.out.println("Error getting active members count: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return 0;
    }

    // Add searchMembers method to MemberDAO
    public List<Member> searchMembers(String searchTerm) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE name LIKE ? OR email LIKE ? OR phone LIKE ? ORDER BY name";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return members;

            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setName(rs.getString("name"));
                member.setEmail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setAddress(rs.getString("address"));
                member.setJoinDate(rs.getDate("join_date"));
                member.setStatus(rs.getString("status"));
                member.setMaxBooksAllowed(rs.getInt("max_books_allowed"));
                members.add(member);
            }

        } catch (SQLException e) {
            System.out.println("Error searching members: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, conn);
        }
        return members;
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