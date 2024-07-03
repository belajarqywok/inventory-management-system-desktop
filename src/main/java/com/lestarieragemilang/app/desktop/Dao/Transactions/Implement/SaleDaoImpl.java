package com.lestarieragemilang.app.desktop.Dao.Transactions.Implement;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.lestarieragemilang.app.desktop.Configurations.DatabaseConfiguration;
import com.lestarieragemilang.app.desktop.Dao.Transactions.SaleDao;
import com.lestarieragemilang.app.desktop.Entities.Transactions.Invoice;
import com.lestarieragemilang.app.desktop.Entities.Transactions.InvoiceType;
import com.lestarieragemilang.app.desktop.Entities.Transactions.Sales;

public class SaleDaoImpl extends DatabaseConfiguration implements SaleDao {

    @Override
    public void addSales(Sales sales) {
        String sql = "INSERT INTO sales (invoice_number, sale_date, stock_id, brand, product_type, price, sub_total, price_total, customer_id, customer_name, quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DatabaseConfiguration.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            LocalDate invoiceDate = sales.getInvoice().getInvoiceDate();
            if (invoiceDate == null) {
                invoiceDate = LocalDate.now(); // Default to current date if not set
            }
            stmt.setInt(1, sales.getInvoice().getInvoiceNumber()); // Set invoice_number
            stmt.setDate(2, java.sql.Date.valueOf(invoiceDate));
            stmt.setInt(3, sales.getStockId());
            stmt.setString(4, sales.getBrand());
            stmt.setString(5, sales.getProductType());
            stmt.setBigDecimal(6, sales.getPrice());
            stmt.setBigDecimal(7, sales.getSubTotal());
            stmt.setBigDecimal(8, sales.getPriceTotal());
            stmt.setInt(9, sales.getCustomerId());
            stmt.setString(10, sales.getCustomerName());
            stmt.setInt(11, sales.getQuantity());
    
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void updateSales(Sales sales) {
        String sql = "UPDATE sales SET sale_date=?, stock_id=?, brand=?, product_type=?, price=?, sub_total=?, price_total=?, customer_id=?, customer_name=?, quantity=? WHERE invoice_number=?";

        try (Connection conn = DatabaseConfiguration.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            // stmt.setDate(1, new
            // java.sql.Date(sales.getInvoice().getInvoiceDate().getTime()));
            stmt.setInt(2, sales.getStockId());
            stmt.setString(3, sales.getBrand());
            stmt.setString(4, sales.getProductType());
            stmt.setBigDecimal(5, sales.getPrice());
            stmt.setBigDecimal(6, sales.getSubTotal());
            stmt.setBigDecimal(7, sales.getPriceTotal());
            stmt.setInt(8, sales.getCustomerId());
            stmt.setString(9, sales.getCustomerName());
            stmt.setInt(10, sales.getQuantity());
            stmt.setInt(11, sales.getInvoice().getInvoiceNumber());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSales(int id) {
        String sql = "DELETE FROM sales WHERE invoice_number=?";

        try (Connection conn = DatabaseConfiguration.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Sales getSalesById(int id) {
        String sql = "SELECT * FROM sales WHERE invoice_number=?";

        try (Connection conn = DatabaseConfiguration.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Invoice invoice = new Invoice();

                    Sales sales = new Sales();
                    sales.setInvoice(invoice);
                    sales.setStockId(rs.getInt("stock_id"));
                    sales.setBrand(rs.getString("brand"));
                    sales.setProductType(rs.getString("product_type"));
                    sales.setPrice(rs.getBigDecimal("price"));
                    sales.setSubTotal(rs.getBigDecimal("sub_total"));
                    sales.setPriceTotal(rs.getBigDecimal("price_total"));
                    sales.setCustomerId(rs.getInt("customer_id"));
                    sales.setCustomerName(rs.getString("customer_name"));
                    sales.setQuantity(rs.getInt("quantity"));

                    return sales;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Sales> getAllSales() {
        String sql = "SELECT * FROM sales";

        List<Sales> salesList = new ArrayList<>();

        try (Connection conn = DatabaseConfiguration.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceNumber(rs.getInt("invoice_number"));
                // invoice.setInvoiceDate(rs.getDate("sale_date"));
                invoice.setInvoiceType(InvoiceType.SALE);

                Sales sales = new Sales();
                sales.setInvoice(invoice);
                sales.setStockId(rs.getInt("stock_id"));
                sales.setBrand(rs.getString("brand"));
                sales.setProductType(rs.getString("product_type"));
                sales.setPrice(rs.getBigDecimal("price"));
                sales.setSubTotal(rs.getBigDecimal("sub_total"));
                sales.setPriceTotal(rs.getBigDecimal("price_total"));
                sales.setCustomerId(rs.getInt("customer_id"));
                sales.setCustomerName(rs.getString("customer_name"));
                sales.setQuantity(rs.getInt("quantity"));

                salesList.add(sales);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salesList;
    }

    @Override
    public void confirmSales(List<Sales> salesList) {
        int invoiceNumber = generateInvoiceNumber();

        for (Sales sales : salesList) {
            sales.getInvoice().setInvoiceNumber(invoiceNumber);
            addSales(sales);
        }
    }

    public int generateInvoiceNumber() {
        String sql = "SELECT MAX(invoice_number) FROM sales";

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

    public List<Integer> getAllStockIds() {
        String sql = "SELECT stock_id FROM stocks";

        List<Integer> stockIds = new ArrayList<>();

        try (Connection conn = DatabaseConfiguration.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                stockIds.add(rs.getInt("stock_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockIds;
    }

    public List<String> getBrandTypePrice(int stockId) {
        String sql = "SELECT c.brand, c.product_type, s.selling_price FROM stocks s JOIN categories c ON s.category_id = c.category_id WHERE s.stock_id = ?";

        List<String> stockDetails = new ArrayList<>();

        try (Connection conn = DatabaseConfiguration.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, stockId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stockDetails.add(rs.getString("brand"));
                    stockDetails.add(rs.getString("product_type"));
                    stockDetails.add(rs.getBigDecimal("selling_price").toString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockDetails;
    }

    public List<Integer> getCustomerIds() {
        String sql = "SELECT customer_id FROM customers";

        List<Integer> customerIds = new ArrayList<>();

        try (Connection conn = DatabaseConfiguration.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                customerIds.add(rs.getInt("customer_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerIds;
    }

    public String getCustomerName(int customerId) {
        String name = "";
        String sql = "SELECT customer_name FROM customers WHERE customer_id=?";
        try (Connection conn = DatabaseConfiguration.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("customer_name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

}
