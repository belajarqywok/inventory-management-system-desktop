package com.lestarieragemilang.app.desktop.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lestarieragemilang.app.desktop.Configurations.DatabaseConfiguration;
import com.lestarieragemilang.app.desktop.Entities.Supplier;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class SupplierDao extends DatabaseConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierDao.class);

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("address"),
                        rs.getString("contact"),
                        rs.getString("email"));

                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to fetch all suppliers", e);
        }
        return suppliers;
    }

    public void addSupplier(Supplier supplier) {
        String sql = "INSERT INTO suppliers (supplier_id, supplier_name, address, contact, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, supplier.getSupplierId());
            stmt.setString(2, supplier.getSupplierName());
            stmt.setString(3, supplier.getSupplierAddress());
            stmt.setString(4, supplier.getSupplierContact());
            stmt.setString(5, supplier.getSupplierEmail());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to add a new supplier", e);
        }
    }

    public void updateSupplier(Supplier supplier) {
        String sql = "UPDATE suppliers SET supplier_name = ?, address = ?, contact = ?, email = ? WHERE supplier_id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getSupplierName());
            stmt.setString(2, supplier.getSupplierAddress());
            stmt.setString(3, supplier.getSupplierContact());
            stmt.setString(4, supplier.getSupplierEmail());
            stmt.setInt(5, supplier.getSupplierId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to update supplier", e);
        }
    }

    public void removeSupplier(Supplier supplier) {
        String sql = "DELETE FROM suppliers WHERE supplier_id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, supplier.getSupplierId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchSupplier(ObservableList<Supplier> supplierData, FilteredList<Supplier> filteredData,
            String search) {
        filteredData.setPredicate(supplier -> {
            if (search == null || search.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = search.toLowerCase();

            return supplier.getSupplierName().toLowerCase().contains(lowerCaseFilter)
                    || supplier.getSupplierAddress().toLowerCase().contains(lowerCaseFilter)
                    || supplier.getSupplierContact().toLowerCase().contains(lowerCaseFilter)
                    || supplier.getSupplierEmail().toLowerCase().contains(lowerCaseFilter);
        }); 
    }
}