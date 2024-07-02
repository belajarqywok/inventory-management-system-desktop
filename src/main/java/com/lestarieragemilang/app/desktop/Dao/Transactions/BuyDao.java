package com.lestarieragemilang.app.desktop.Dao.Transactions;

import java.math.BigDecimal;
import java.util.List;

import com.lestarieragemilang.app.desktop.Entities.Transactions.Purchasing;

public interface BuyDao {
    void addPurchasing(Purchasing purchasing);

    void updatePurchasing(Purchasing purchasing);

    void deletePurchasing(int id);

    Purchasing getPurchasingById(int id);

    List<Purchasing> getAllPurchasing();

    void confirmPurchasing(List<Purchasing> purchasingList);

    void addInvoiceItem(int invoiceNumber, int stockId, int quantity, BigDecimal price, BigDecimal subTotal);

}