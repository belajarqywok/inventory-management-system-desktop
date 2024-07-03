package com.lestarieragemilang.app.desktop.Utilities;

import java.sql.SQLException;
import java.util.List;

import com.lestarieragemilang.app.desktop.Dao.Transactions.Implement.SaleDaoImpl;
import com.lestarieragemilang.app.desktop.Entities.Transactions.Sales;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SellTablePopulator {
    SaleDaoImpl sellDao = new SaleDaoImpl();

    public void populateSellTable(TableColumn<?, ?> sellDateCol, TableColumn<?, ?> sellInvoiceCol,
            TableColumn<?, ?> sellOnCustomerNameCol, TableColumn<?, ?> sellBrandCol,
            TableColumn<?, ?> sellTypeCol, TableColumn<?, ?> sellTotalCol, TableColumn<?, ?> sellPriceCol,
            TableColumn<?, ?> sellSubTotalCol, TableView<Sales> sellTable) throws SQLException {
        List<Sales> sells = sellDao.getAllSales();
        ObservableList<Sales> sellData = FXCollections.observableArrayList();

        sellDateCol.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
        sellInvoiceCol.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        sellOnCustomerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        sellBrandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        sellTypeCol.setCellValueFactory(new PropertyValueFactory<>("productType"));
        sellTotalCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        sellPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        sellSubTotalCol.setCellValueFactory(new PropertyValueFactory<>("subTotal"));

        sellData.addAll(sells);
        sellTable.setItems(sellData);
    }
}
