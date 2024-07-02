package com.lestarieragemilang.app.desktop.Utilities;

import java.sql.SQLException;
import java.util.List;

import com.lestarieragemilang.app.desktop.Dao.Transactions.Implement.BuyDaoImpl;
import com.lestarieragemilang.app.desktop.Entities.Transactions.Purchasing;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class BuyTablePopulator {
    BuyDaoImpl buyDao = new BuyDaoImpl();

    public void populateBuyTable(TableColumn<?, ?> buyDateCol, TableColumn<?, ?> buyInvoiceCol,
            TableColumn<?, ?> buyOnSupplierNameCol, TableColumn<?, ?> buyBrandCol,
            TableColumn<?, ?> buyTypeCol, TableColumn<?, ?> buyTotalCol, TableColumn<?, ?> buyPriceCol,
            TableColumn<?, ?> buySubTotalCol, TableView<Purchasing> buyTable) throws SQLException {
        ObservableList<Purchasing> buyData = FXCollections.observableArrayList();
        List<Purchasing> buys = buyDao.getAllPurchasing();

        buyDateCol.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
        buyInvoiceCol.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        buyOnSupplierNameCol.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        buyBrandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        buyTypeCol.setCellValueFactory(new PropertyValueFactory<>("productType"));
        buyTotalCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        buyPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        buySubTotalCol.setCellValueFactory(new PropertyValueFactory<>("subTotal"));

        buyData.addAll(buys);
        buyTable.setItems(buyData);
    }
}
