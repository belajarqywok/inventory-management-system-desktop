package com.lestarieragemilang.app.desktop.Dao.Transactions;

import java.util.List;

import com.lestarieragemilang.app.desktop.Entities.Transactions.Sales;

public interface SaleDao {
    void addSales(Sales sales);
    void updateSales(Sales sales);
    void deleteSales(int id);
    Sales getSalesById(int id);
    List<Sales> getAllSales();
    void confirmSales(List<Sales> salesList);
    
}
