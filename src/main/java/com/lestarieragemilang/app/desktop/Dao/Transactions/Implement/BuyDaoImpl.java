package com.lestarieragemilang.app.desktop.Dao.Transactions.Implement;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.lestarieragemilang.app.desktop.Configurations.DatabaseConfiguration;
import com.lestarieragemilang.app.desktop.Dao.Transactions.BuyDao;
import com.lestarieragemilang.app.desktop.Entities.Transactions.Invoice;
import com.lestarieragemilang.app.desktop.Entities.Transactions.InvoiceType;
import com.lestarieragemilang.app.desktop.Entities.Transactions.Purchasing;

public class BuyDaoImpl extends DatabaseConfiguration implements BuyDao {

    @Override
    public void addPurchasing(Purchasing purchasing) {
        String sql = "INSERT INTO purchasing (invoice_number, purchase_date, stock_id, brand, product_type, price, sub_total, price_total, supplier_id, supplier_name, quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfiguration.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            LocalDate invoiceDate = purchasing.getInvoice().getInvoiceDate();
            if (invoiceDate == null) {
                invoiceDate = LocalDate.now(); // Default to current date if not set
            }
            stmt.setInt(1, purchasing.getInvoice().getInvoiceNumber()); // Set invoice_number
            stmt.setDate(2, java.sql.Date.valueOf(invoiceDate));
            stmt.setInt(3, purchasing.getStockId());
            stmt.setString(4, purchasing.getBrand());
            stmt.setString(5, purchasing.getProductType());
            stmt.setBigDecimal(6, purchasing.getPrice());
            stmt.setBigDecimal(7, purchasing.getSubTotal());
            stmt.setBigDecimal(8, purchasing.getPriceTotal());
            stmt.setInt(9, purchasing.getSupplierId());
            stmt.setString(10, purchasing.getSupplierName());
            stmt.setInt(11, purchasing.getQuantity());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addInvoiceItem(int invoiceNumber, int stockId, int quantity, BigDecimal price, BigDecimal subTotal) {
        String sql = "INSERT INTO invoice_items (invoice_number, stock_id, quantity, price, sub_total) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfiguration.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, invoiceNumber);
            stmt.setInt(2, stockId);
            stmt.setInt(3, quantity);
            stmt.setBigDecimal(4, price);
            stmt.setBigDecimal(5, subTotal);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePurchasing(Purchasing purchasing) {
        String sql = "UPDATE purchasing SET purchase_date=?, stock_id=?, brand=?, product_type=?, price=?, sub_total=?, price_total=?, supplier_id=?, supplier_name=?, quantity=? WHERE invoice_number=?";

        try (Connection conn = DatabaseConfiguration.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, purchasing.getInvoice().getInvoiceDate()); // Assuming getInvoiceDate() returns a
                                                                         // LocalDate
            stmt.setInt(2, purchasing.getStockId());
            stmt.setString(3, purchasing.getBrand());
            stmt.setString(4, purchasing.getProductType());
            stmt.setBigDecimal(5, purchasing.getPrice());
            stmt.setBigDecimal(6, purchasing.getSubTotal());
            stmt.setBigDecimal(7, purchasing.getPriceTotal());
            stmt.setInt(8, purchasing.getSupplierId());
            stmt.setString(9, purchasing.getSupplierName());
            stmt.setInt(10, purchasing.getQuantity());
            stmt.setInt(11, purchasing.getInvoice().getInvoiceNumber());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePurchasing(int id) {
        String sql = "DELETE FROM purchasing WHERE invoice_number=?";

        try (Connection conn = DatabaseConfiguration.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Purchasing getPurchasingById(int id) {
        String sql = "SELECT * FROM purchasing WHERE invoice_number=?";

        try (Connection conn = DatabaseConfiguration.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceNumber(rs.getInt("invoice_number"));

                    invoice.setInvoiceType(InvoiceType.PURCHASE);

                    Purchasing purchasing = new Purchasing();
                    purchasing.setInvoice(invoice);
                    purchasing.setStockId(rs.getInt("stock_id"));
                    purchasing.setBrand(rs.getString("brand"));
                    purchasing.setProductType(rs.getString("product_type"));
                    purchasing.setPrice(rs.getBigDecimal("price"));
                    purchasing.setSubTotal(rs.getBigDecimal("sub_total"));
                    purchasing.setPriceTotal(rs.getBigDecimal("price_total"));
                    purchasing.setSupplierId(rs.getInt("supplier_id"));
                    purchasing.setSupplierName(rs.getString("supplier_name"));
                    purchasing.setQuantity(rs.getInt("quantity"));

                    return purchasing;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Purchasing> getAllPurchasing() {
        String sql = "SELECT * FROM purchasing";

        List<Purchasing> purchasingList = new ArrayList<>();

        try (Connection conn = DatabaseConfiguration.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceNumber(rs.getInt("invoice_number"));
                invoice.setInvoiceDate(rs.getDate("purchase_date").toLocalDate()); // Convert java.sql.Date to LocalDate
                invoice.setInvoiceType(InvoiceType.PURCHASE);

                Purchasing purchasing = new Purchasing();
                purchasing.setInvoice(invoice);
                purchasing.setStockId(rs.getInt("stock_id"));
                purchasing.setBrand(rs.getString("brand"));
                purchasing.setProductType(rs.getString("product_type"));
                purchasing.setPrice(rs.getBigDecimal("price"));
                purchasing.setSubTotal(rs.getBigDecimal("sub_total"));
                purchasing.setPriceTotal(rs.getBigDecimal("price_total"));
                purchasing.setSupplierId(rs.getInt("supplier_id"));
                purchasing.setSupplierName(rs.getString("supplier_name"));
                purchasing.setQuantity(rs.getInt("quantity"));

                purchasingList.add(purchasing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return purchasingList;
    }

    @Override
    public void confirmPurchasing(List<Purchasing> purchasingList) {
        int invoiceNumber = generateInvoiceNumber();

        for (Purchasing purchasing : purchasingList) {
            purchasing.getInvoice().setInvoiceNumber(invoiceNumber);
            addPurchasing(purchasing);
        }
    }

    private int generateInvoiceNumber() {
        String sql = "SELECT MAX(invoice_number) FROM purchasing";

        try (Connection conn = DatabaseConfiguration.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1;
    }

    // -----------------------------------------------------

    public List<Integer> getAllStockIds() {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT stock_id FROM stocks";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getInt("stock_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public List<String> getBrandTypePrice(String stockId) {
        List<String> brandTypePrice = new ArrayList<>();
        String sql = "SELECT c.brand, c.product_type, s.purchase_price FROM stocks s INNER JOIN categories c ON s.category_id = c.category_id WHERE s.stock_id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, stockId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                brandTypePrice.add(rs.getString("brand"));
                brandTypePrice.add(rs.getString("product_type"));
                brandTypePrice.add(rs.getString("purchase_price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brandTypePrice;
    }

    public List<Integer> getSupplierIds() {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT supplier_id FROM suppliers";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getInt("supplier_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public String getSupplierName(String supplierId) {
        String name = "";
        String sql = "SELECT supplier_name FROM suppliers WHERE supplier_id = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplierId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                name = rs.getString("supplier_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }
}
