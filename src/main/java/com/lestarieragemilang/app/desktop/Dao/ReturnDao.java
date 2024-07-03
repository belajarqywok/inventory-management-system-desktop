package com.lestarieragemilang.app.desktop.Dao;

import com.lestarieragemilang.app.desktop.Entities.Return;
import com.lestarieragemilang.app.desktop.Configurations.DatabaseConfiguration;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReturnDao {
    private Connection conn;
    public ReturnDao() throws SQLException { conn = DatabaseConfiguration.getConnection(); }

    public List<Return> getAllReturns() throws SQLException {
        String sql = "SELECT DISTINCT return_date, return_id, return_type, invoice_number, reason FROM returns";
        List<Return> returnsList = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                LocalDate returnDate = rs.getDate("return_date").toLocalDate();
                String returnID = rs.getString("return_id");
                String returnType = rs.getString("return_type");
                String invoiceNumber = rs.getString("invoice_number");
                String reason = rs.getString("reason");

                returnsList.add(new Return(returnDate, returnID, returnType, invoiceNumber, reason));
            }
        }

        return returnsList;
    }

    public List<Return> searchReturns(String key) throws SQLException {
        String sql = "SELECT DISTINCT return_date, return_id, return_type, invoice_number, reason FROM returns " +
                     "WHERE return_id LIKE ? OR return_type LIKE ? OR invoice_number LIKE ? OR reason LIKE ?";
        List<Return> returnsList = new ArrayList<>();
    
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String likeKey = "%" + key + "%";
            stmt.setString(1, likeKey);
            stmt.setString(2, likeKey);
            stmt.setString(3, likeKey);
            stmt.setString(4, likeKey);
    
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate returnDate = rs.getDate("return_date").toLocalDate();
                    String returnID = rs.getString("return_id");
                    String returnType = rs.getString("return_type");
                    String invoiceNumber = rs.getString("invoice_number");
                    String reason = rs.getString("reason");
    
                    returnsList.add(new Return(returnDate, returnID, returnType, invoiceNumber, reason));
                }
            }
        }
    
        return returnsList;
    }
    

    public List<String> getInvoiceNumbers(String type) throws SQLException {
        String sql = type.equals("sales") ? 
            "SELECT DISTINCT invoice_number FROM sales" :
            "SELECT DISTINCT invoice_number FROM purchasing";

        List<String> invoiceNumbers = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) invoiceNumbers.add(rs.getString("invoice_number"));
        }

        return invoiceNumbers;
    }

    public void getSalesByInvoice(String invoiceNumber) throws SQLException {
        String sql = "SELECT * FROM sales WHERE invoice_number = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, invoiceNumber);
            try (ResultSet rs = stmt.executeQuery()) { rs.next(); }
        }
    }

    public void getPurchasingByInvoice(String invoiceNumber) throws SQLException {
        String sql = "SELECT * FROM purchasing WHERE invoice_number = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, invoiceNumber);
            try (ResultSet rs = stmt.executeQuery()) { rs.next(); }
        }
    }

    public void addReturn(Return returnItem) throws SQLException {
        String sql = "INSERT INTO returns (return_date, return_type, invoice_number, reason) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(returnItem.getReturnDate()));
            stmt.setString(2, returnItem.getReturnType());
            stmt.setString(3, returnItem.getInvoiceNumber());
            stmt.setString(4, returnItem.getReason());

            stmt.executeUpdate();
        }
    }

    public void updateReturn(Return returnItem) throws SQLException {
        String sql = "UPDATE returns SET return_date = ?, return_type = ?, invoice_number = ?, reason = ? WHERE return_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(returnItem.getReturnDate()));
            stmt.setString(2, returnItem.getReturnType());
            stmt.setString(3, returnItem.getInvoiceNumber());
            stmt.setString(4, returnItem.getReason());
            stmt.setString(5, returnItem.getReturnID());

            stmt.executeUpdate();
        }
    }

    public void deleteReturn(String returnID) throws SQLException {
        String sql = "DELETE FROM returns WHERE return_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, returnID);
            stmt.executeUpdate();
        }
    }
}
