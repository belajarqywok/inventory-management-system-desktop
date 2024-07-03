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
import com.lestarieragemilang.app.desktop.Entities.Category;
import com.lestarieragemilang.app.desktop.Entities.Stock;

public class StockDao extends DatabaseConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockDao.class);

    public List<Stock> getAllStocks() {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT * FROM stocks";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Stock stock = new Stock(rs.getInt("stock_id"), rs.getInt("category_id"), null, null, null, null, null,
                        rs.getString("quantity"), rs.getString("purchase_price"), rs.getString("selling_price"));
                stocks.add(stock);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to get all stocks", e);

        }
        return stocks;

    }

    public void addStock(Stock stock) {
        String sql = "INSERT INTO stocks (stock_id, category_id, quantity, purchase_price, selling_price) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, stock.getStockID());
            stmt.setInt(2, stock.getStockOnCategoryID());
            stmt.setString(3, stock.getQuantity());
            stmt.setString(4, stock.getPurchasePrice());
            stmt.setString(5, stock.getPurchaseSell());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStock(Stock stock) {
        String sql = "UPDATE stocks SET stock_id = ?, category_id = ?, quantity = ?, purchase_price = ?, selling_price = ? WHERE stock_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, stock.getStockID());
            stmt.setInt(2, stock.getStockOnCategoryID());
            stmt.setString(3, stock.getQuantity());
            stmt.setString(4, stock.getPurchasePrice());
            stmt.setString(5, stock.getPurchaseSell());
            stmt.setInt(6, stock.getStockID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to update stock", e);
        }
    }

    public void removeStock(int... stockIds) {
        String[] tables = { "sales", "purchasing", "stocks" };
        for (int stockId : stockIds) {
            for (String table : tables) {
                deleteRecordById(table, stockId);
            }
        }
    }

    private void deleteRecordById(String tableName, int id) {
        String sql = String.format("DELETE FROM %s WHERE stock_id = ?", tableName);
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to delete record from " + tableName, e);
        }
    }

    public List<Stock> searchStock(String keyword) {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT s.*, c.brand, c.product_type, c.size, c.weight, c.weight_unit " +
                "FROM stocks s " +
                "LEFT JOIN categories c ON s.category_id = c.category_id " +
                "WHERE s.stock_id LIKE ? OR s.category_id LIKE ? OR s.quantity LIKE ? OR s.purchase_price LIKE ? OR s.selling_price LIKE ? "
                +
                "OR c.brand LIKE ? OR c.product_type LIKE ? OR c.size LIKE ? OR c.weight LIKE ? OR c.weight_unit LIKE ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 10; i++) {
                stmt.setString(i, "%" + keyword + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Stock stock = new Stock(0, 0, null, null, null, null, null, null, null, null);
                    stock.setStockID(rs.getInt("stock_id"));
                    stock.setStockOnCategoryID(rs.getInt("category_id"));
                    stock.setQuantity(rs.getString("quantity"));
                    stock.setPurchasePrice(rs.getString("purchase_price"));
                    stock.setPurchaseSell(rs.getString("selling_price"));
                    stock.setCategoryBrand(rs.getString("brand"));
                    stock.setCategoryType(rs.getString("product_type"));
                    stock.setCategorySize(rs.getString("size"));
                    stock.setCategoryWeight(rs.getString("weight"));
                    stock.setCategoryUnit(rs.getString("weight_unit"));

                    stocks.add(stock);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    public Stock getStockById(int stockId) {
        Stock stock = new Stock(0, 0, null, null, null, null, null, null, null, null);
        String sql = "SELECT * FROM stocks WHERE stock_id = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, stockId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stock.setStockID(rs.getInt("stock_id"));
                    stock.setStockOnCategoryID(rs.getInt("category_id"));
                    stock.setQuantity(rs.getString("quantity"));
                    stock.setPurchasePrice(rs.getString("purchase_price"));
                    stock.setPurchaseSell(rs.getString("selling_price"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stock;
    }

    public List<Category> getStockCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT category_id, brand, product_type FROM categories";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int categoryId = rs.getInt("category_id");
                    String brand = rs.getString("brand");
                    String productType = rs.getString("product_type");
                    categories.add(new Category(categoryId, brand, productType));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public List<Stock> getStocksWithCategoryDetails() {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT s.stock_id, s.category_id, c.brand, c.product_type, c.size, c.weight, c.weight_unit, s.quantity as stok, s.purchase_price as hargaBeli, s.selling_price as hargaJual "
                +
                "FROM stocks s " +
                "INNER JOIN categories c ON s.category_id = c.category_id";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Stock stock = new Stock(rs.getInt("stock_id"), rs.getInt("category_id"), rs.getString("brand"),
                            rs.getString("product_type"), rs.getString("size"), rs.getString("weight"),
                            rs.getString("weight_unit"), rs.getString("stok"), rs.getString("hargaBeli"),
                            rs.getString("hargaJual"));
                    stocks.add(stock);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

}
