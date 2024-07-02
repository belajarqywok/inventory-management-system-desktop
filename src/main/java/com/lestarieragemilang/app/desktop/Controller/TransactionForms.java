package com.lestarieragemilang.app.desktop.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.lestarieragemilang.app.desktop.Configurations.DatabaseConfiguration;
import com.lestarieragemilang.app.desktop.Dao.Transactions.BuyDao;
import com.lestarieragemilang.app.desktop.Dao.Transactions.SaleDao;
import com.lestarieragemilang.app.desktop.Dao.Transactions.Implement.BuyDaoImpl;
import com.lestarieragemilang.app.desktop.Dao.Transactions.Implement.SaleDaoImpl;
import com.lestarieragemilang.app.desktop.Entities.Customer;
import com.lestarieragemilang.app.desktop.Entities.Stock;
import com.lestarieragemilang.app.desktop.Entities.Supplier;
import com.lestarieragemilang.app.desktop.Entities.Transactions.Invoice;
import com.lestarieragemilang.app.desktop.Entities.Transactions.Purchasing;
import com.lestarieragemilang.app.desktop.Entities.Transactions.Sales;
import com.lestarieragemilang.app.desktop.Utilities.GenerateRandomID;

public class TransactionForms {

    private int currentInvoiceNumber;

    @FXML
    private DatePicker buyDate;

    @FXML
    private TextField buyInvoiceNumber;

    @FXML
    private ComboBox<Object> buyStockIDDropdown;

    @FXML
    private TextField buyBrandField;

    @FXML
    private TextField buyTypeField;

    @FXML
    private TextField buyPriceField;

    @FXML
    private ComboBox<Object> supplierIDDropDown;

    @FXML
    private TextField supplierNameField;

    @FXML
    private TextField buyTotalField;

    @FXML
    private TableView<Purchasing> buyTable;

    @FXML
    private TableColumn<Purchasing, LocalDate> buyDateCol;

    @FXML
    private TableColumn<Purchasing, Integer> buyInvoiceCol;

    @FXML
    private TableColumn<Purchasing, String> buyOnSupplierNameCol;

    @FXML
    private TableColumn<Purchasing, String> buyBrandCol;

    @FXML
    private TableColumn<Purchasing, String> buyTypeCol;

    @FXML
    private TableColumn<Purchasing, Integer> buyTotalCol;

    @FXML
    private TableColumn<Purchasing, BigDecimal> buyPriceCol;

    @FXML
    private TableColumn<Purchasing, BigDecimal> buySubTotalCol;

    @FXML
    private TextField buyTotalPrice;

    @FXML
    private DatePicker sellDate;

    @FXML
    private TextField sellInvoiceNumber;

    @FXML
    private ComboBox<Integer> sellStockIDDropdown;

    @FXML
    private TextField sellBrandField;

    @FXML
    private TextField sellTypeField;

    @FXML
    private TextField sellPriceField;

    @FXML
    private ComboBox<Integer> customerIDDropDown;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField sellTotalField;

    @FXML
    private TableView<Sales> sellTable;

    @FXML
    private TableColumn<Sales, LocalDate> sellDateCol;

    @FXML
    private TableColumn<Sales, Integer> sellInvoiceCol;

    @FXML
    private TableColumn<Sales, String> sellOnCustomerNameCol;

    @FXML
    private TableColumn<Sales, String> sellBrandCol;

    @FXML
    private TableColumn<Sales, String> sellTypeCol;

    @FXML
    private TableColumn<Sales, Integer> sellTotalCol;

    @FXML
    private TableColumn<Sales, BigDecimal> sellPriceCol;

    @FXML
    private TableColumn<Sales, BigDecimal> sellSubTotalCol;

    @FXML
    private TextField sellTotalPrice;

    @FXML
    private TabPane tabPane;

    private BuyDaoImpl buyDao = new BuyDaoImpl();
    private SaleDaoImpl saleDao = new SaleDaoImpl();

    private ObservableList<Purchasing> buyData = FXCollections.observableArrayList();
    private ObservableList<Sales> sellData = FXCollections.observableArrayList();

    public void setBuyDao(BuyDaoImpl buyDao) {
        this.buyDao = buyDao;
    }

    public void setSaleDao(SaleDaoImpl saleDao) {
        this.saleDao = saleDao;
    }

    @FXML
    private void initialize() {

        GenerateRandomID generateRandomID = new GenerateRandomID();

        buyInvoiceNumber.setText(String.valueOf(generateRandomID));
        sellInvoiceNumber.setText(String.valueOf(generateRandomID));

        buyDateCol.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
        buyInvoiceCol.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        buyOnSupplierNameCol.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        buyBrandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        buyTypeCol.setCellValueFactory(new PropertyValueFactory<>("productType"));
        buyTotalCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        buyPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        buySubTotalCol.setCellValueFactory(new PropertyValueFactory<>("subTotal"));

        sellDateCol.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
        sellInvoiceCol.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        sellOnCustomerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        sellBrandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        sellTypeCol.setCellValueFactory(new PropertyValueFactory<>("productType"));
        sellTotalCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        sellPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        sellSubTotalCol.setCellValueFactory(new PropertyValueFactory<>("subTotal"));

        buyTable.setItems(buyData);
        sellTable.setItems(sellData);

        loadBuyData();
        loadSellData();

        loadStockIDs();
        loadSupplierIDs();
        loadIDs();

        buyDate.setValue(LocalDate.now());
        sellDate.setValue(LocalDate.now());
    }

    private void loadBuyData() {
        buyData.clear();
        buyData.addAll(buyDao.getAllPurchasing());
    }

    private void loadSellData() {
        sellData.clear();
        sellData.addAll(saleDao.getAllSales());
    }

    private void loadStockIDs() {
        ObservableList<Object> stockIds = FXCollections.observableArrayList(buyDao.getAllStockIds());
        buyStockIDDropdown.setItems(stockIds);
        if (!stockIds.isEmpty()) {
            buyStockIDDropdown.getSelectionModel().selectFirst();
            String firstStockId = stockIds.get(0).toString();
            List<String> firstStockDetails = buyDao.getBrandTypePrice(firstStockId);
            buyBrandField.setText(firstStockDetails.get(0)); // Assuming the brand is the first item in the list
            buyTypeField.setText(firstStockDetails.get(1)); // Assuming the type is the second item in the list
            buyPriceField.setText(firstStockDetails.get(2)); // Assuming the price is the third item in the list
        }

        buyStockIDDropdown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String stockId = newValue.toString();
                List<String> stockDetails = buyDao.getBrandTypePrice(stockId);
                buyBrandField.setText(stockDetails.get(0)); // Assuming the brand is the first item in the list
                buyTypeField.setText(stockDetails.get(1)); // Assuming the type is the second item in the list
                buyPriceField.setText(stockDetails.get(2)); // Assuming the price is the third item in the list
            }
        });
    }

    private void loadSupplierIDs() {
        ObservableList<Object> supplierIds = FXCollections.observableArrayList(buyDao.getSupplierIds());
        supplierIDDropDown.setItems(supplierIds);
        if (!supplierIds.isEmpty()) {
            supplierIDDropDown.getSelectionModel().selectFirst();
            String firstSupplierId = supplierIds.get(0).toString();
            String firstSupplierName = buyDao.getSupplierName(firstSupplierId);
            supplierNameField.setText(firstSupplierName);
        }

        supplierIDDropDown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String supplierId = newValue.toString();
                String supplierName = buyDao.getSupplierName(supplierId);
                supplierNameField.setText(supplierName);
            }
        });
    }

    private void loadIDs() {
        // Load stock IDs for buying transactions
        ObservableList<Object> buyStockIds = FXCollections.observableArrayList(buyDao.getAllStockIds());
        buyStockIDDropdown.setItems(buyStockIds);
        if (!buyStockIds.isEmpty()) {
            buyStockIDDropdown.getSelectionModel().selectFirst();
            String firstBuyStockId = buyStockIds.get(0).toString();
            List<String> firstBuyStockDetails = buyDao.getBrandTypePrice(firstBuyStockId);
            buyBrandField.setText(firstBuyStockDetails.get(0));
            buyTypeField.setText(firstBuyStockDetails.get(1));
            buyPriceField.setText(firstBuyStockDetails.get(2));
        }

        buyStockIDDropdown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String stockId = newValue.toString();
                List<String> stockDetails = buyDao.getBrandTypePrice(stockId);
                buyBrandField.setText(stockDetails.get(0));
                buyTypeField.setText(stockDetails.get(1));
                buyPriceField.setText(stockDetails.get(2));
            }
        });

        // Load stock IDs for selling transactions
        ObservableList<Integer> sellStockIds = FXCollections.observableArrayList(saleDao.getAllStockIds());
        sellStockIDDropdown.setItems(sellStockIds);
        if (!sellStockIds.isEmpty()) {
            sellStockIDDropdown.getSelectionModel().selectFirst();
            int firstSellStockId = sellStockIds.get(0);
            List<String> firstSellStockDetails = saleDao.getBrandTypePrice(firstSellStockId);
            sellBrandField.setText(firstSellStockDetails.get(0));
            sellTypeField.setText(firstSellStockDetails.get(1));
            sellPriceField.setText(firstSellStockDetails.get(2));
        }

        sellStockIDDropdown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int stockId = newValue;
                List<String> stockDetails = saleDao.getBrandTypePrice(stockId);
                sellBrandField.setText(stockDetails.get(0));
                sellTypeField.setText(stockDetails.get(1));
                sellPriceField.setText(stockDetails.get(2));
            }
        });

        // Load supplier IDs for buying transactions
        ObservableList<Object> supplierIds = FXCollections.observableArrayList(buyDao.getSupplierIds());
        supplierIDDropDown.setItems(supplierIds);
        if (!supplierIds.isEmpty()) {
            supplierIDDropDown.getSelectionModel().selectFirst();
            String firstSupplierId = supplierIds.get(0).toString();
            String firstSupplierName = buyDao.getSupplierName(firstSupplierId);
            supplierNameField.setText(firstSupplierName);
        }

        supplierIDDropDown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String supplierId = newValue.toString();
                String supplierName = buyDao.getSupplierName(supplierId);
                supplierNameField.setText(supplierName);
            }
        });

        // Load customer IDs for selling transactions
        ObservableList<Integer> customerIds = FXCollections.observableArrayList(saleDao.getCustomerIds());
        customerIDDropDown.setItems(customerIds);
        if (!customerIds.isEmpty()) {
            customerIDDropDown.getSelectionModel().selectFirst();
            int firstCustomerId = customerIds.get(0);
            String firstCustomerName = saleDao.getCustomerName(firstCustomerId);
            customerNameField.setText(firstCustomerName);
        }

        customerIDDropDown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int customerId = newValue;
                String customerName = saleDao.getCustomerName(customerId);
                customerNameField.setText(customerName);
            }
        });
    }

    // private void loadCustomerIDs() {
    // ObservableList<Object> customerIds =
    // FXCollections.observableArrayList(sellDao.getCustomerIds());
    // customerIDDropDown.setItems(customerIds);
    // if (!customerIds.isEmpty()) {
    // customerIDDropDown.getSelectionModel().selectFirst();
    // String firstCustomerId = customerIds.get(0).toString();
    // String firstCustomerName = sellDao.getCustomerName(firstCustomerId);
    // customerNameField.setText(firstCustomerName);
    // }

    // customerIDDropDown.getSelectionModel().selectedItemProperty().addListener((observable,
    // oldValue, newValue) -> {
    // if (newValue != null) {
    // String customerId = newValue.toString();
    // String customerName = sellDao.getCustomerName(customerId);
    // customerNameField.setText(customerName);
    // }
    // });
    // }

    @FXML
    private void addBuyButton() {
        if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
            Purchasing purchasing = new Purchasing();

            // Initialize the Invoice object if it's null
            if (purchasing.getInvoice() == null) {
                purchasing.setInvoice(new Invoice());
            }

            purchasing.getInvoice().setInvoiceNumber(currentInvoiceNumber);
            purchasing.getInvoice().setInvoiceDate(buyDate.getValue());
            purchasing.setStockId((int) buyStockIDDropdown.getValue());
            purchasing.setBrand(buyBrandField.getText());
            purchasing.setProductType(buyTypeField.getText());
            purchasing.setPrice(new BigDecimal(buyPriceField.getText()));
            purchasing.setSupplierId((int) supplierIDDropDown.getValue());
            purchasing.setSupplierName(supplierNameField.getText());
            purchasing.setQuantity(Integer.parseInt(buyTotalField.getText()));
            purchasing.setSubTotal(purchasing.getPrice().multiply(BigDecimal.valueOf(purchasing.getQuantity())));
            purchasing.setPriceTotal(purchasing.getSubTotal());

            buyTable.getItems().add(purchasing);
            calculateTotalPrice();
        } else {
            // Add sales data to temporary UI table
            Sales sales = new Sales();
            sales.setInvoice(new Invoice());
            sales.getInvoice().setInvoiceDate(sellDate.getValue());
            sales.setStockId((int) sellStockIDDropdown.getValue());
            sales.setBrand(sellBrandField.getText());
            sales.setProductType(sellTypeField.getText());
            sales.setPrice(new BigDecimal(sellPriceField.getText()));
            sales.setCustomerId((int) customerIDDropDown.getValue());
            sales.setCustomerName(customerNameField.getText());
            sales.setQuantity(Integer.parseInt(sellTotalField.getText()));
            sales.setSubTotal(sales.getPrice().multiply(BigDecimal.valueOf(sales.getQuantity())));
            sales.setPriceTotal(sales.getSubTotal());
            sellTable.getItems().add(sales);
        }
        calculateTotalPrice();
    }

    @FXML
    private void editBuyButton() {
        Purchasing purchasing = buyTable.getSelectionModel().getSelectedItem();
        if (purchasing != null) {
            purchasing.getInvoice().setInvoiceDate((buyDate.getValue()));
            purchasing.setStockId((int) buyStockIDDropdown.getValue());
            purchasing.setBrand(buyBrandField.getText());
            purchasing.setProductType(buyTypeField.getText());
            purchasing.setPrice(new BigDecimal(buyPriceField.getText()));
            purchasing.setSupplierId((int) supplierIDDropDown.getValue());
            purchasing.setSupplierName(supplierNameField.getText());
            purchasing.setQuantity(Integer.parseInt(buyTotalField.getText()));
            purchasing.setSubTotal(purchasing.getPrice().multiply(BigDecimal.valueOf(purchasing.getQuantity())));
            purchasing.setPriceTotal(purchasing.getSubTotal());

            buyDao.updatePurchasing(purchasing);
            loadBuyData();
        }
    }

    @FXML
    private void removeBuyButton() {
        Purchasing purchasing = buyTable.getSelectionModel().getSelectedItem();
        if (purchasing != null) {
            buyDao.deletePurchasing(purchasing.getInvoice().getInvoiceNumber());
            loadBuyData();
        }
    }

    @FXML
    private void addSellButton() {
        Sales sales = new Sales();
        sales.setInvoice(new Invoice());
        sales.getInvoice().setInvoiceDate((sellDate.getValue()));
        sales.setStockId(sellStockIDDropdown.getValue());
        sales.setBrand(sellBrandField.getText());
        sales.setProductType(sellTypeField.getText());
        sales.setPrice(new BigDecimal(sellPriceField.getText()));
        sales.setCustomerId((int) customerIDDropDown.getValue());
        sales.setCustomerName(customerNameField.getText());
        sales.setQuantity(Integer.parseInt(sellTotalField.getText()));
        sales.setSubTotal(sales.getPrice().multiply(BigDecimal.valueOf(sales.getQuantity())));
        sales.setPriceTotal(sales.getSubTotal());

        saleDao.addSales(sales);
        loadSellData();
    }

    @FXML
    private void editSellButton() {
        Sales sales = sellTable.getSelectionModel().getSelectedItem();
        if (sales != null) {
            sales.getInvoice().setInvoiceDate((sellDate.getValue()));
            sales.setStockId(sellStockIDDropdown.getValue());
            sales.setBrand(sellBrandField.getText());
            sales.setProductType(sellTypeField.getText());
            sales.setPrice(new BigDecimal(sellPriceField.getText()));
            sales.setCustomerId((int) customerIDDropDown.getValue());
            sales.setCustomerName(customerNameField.getText());
            sales.setQuantity(Integer.parseInt(sellTotalField.getText()));
            sales.setSubTotal(sales.getPrice().multiply(BigDecimal.valueOf(sales.getQuantity())));
            sales.setPriceTotal(sales.getSubTotal());

            saleDao.updateSales(sales);
            loadSellData();
        }
    }

    @FXML
    private void removeSellButton() {
        Sales sales = sellTable.getSelectionModel().getSelectedItem();
        if (sales != null) {
            saleDao.deleteSales(sales.getInvoice().getInvoiceNumber());
            loadSellData();
        }
    }

    @FXML
    private void confirmBuyButton() {
        if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
            List<Purchasing> purchasingList = new ArrayList<>(buyTable.getItems());
            confirmPurchasing(purchasingList);
            buyTable.getItems().clear();
            currentInvoiceNumber = generateInvoiceNumber();
            buyInvoiceNumber.setText(String.valueOf(currentInvoiceNumber));
        } else {
            List<Sales> salesList = new ArrayList<>(sellTable.getItems());
            // confirmSales(salesList);
            sellTable.getItems().clear();
            currentInvoiceNumber = generateInvoiceNumber();
            sellInvoiceNumber.setText(String.valueOf(currentInvoiceNumber));
        }
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
            // Calculate total price for purchasing data
            for (Purchasing purchasing : buyTable.getItems()) {
                total = total.add(purchasing.getSubTotal());
            }
        } else {
            // Calculate total price for sales data
            for (Sales sales : sellTable.getItems()) {
                total = total.add(sales.getSubTotal());
            }
        }
        buyTotalPrice.setText(total.toString());
    }

    private void confirmPurchasing(List<Purchasing> purchasingList) {
        for (Purchasing purchasing : purchasingList) {
            buyDao.addPurchasing(purchasing);
        }
    }

    private void confirmSales(List<Sales> salesList) {
        for (Sales sale : salesList) {
            saleDao.addSales(sale);
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

    @FXML
    private void confirmSellButton() {
        List<Sales> salesList = new ArrayList<>(sellData);
        saleDao.confirmSales(salesList);
        sellData.clear();
        loadSellData();
    }

    @FXML
    private void calculateBuyTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;

        for (Purchasing purchasing : buyData) {
            total = total.add(purchasing.getSubTotal());
        }

        buyTotalPrice.setText(total.toString());
    }

    // calculateSellTotalPrice

    @FXML
    private void calculateSellTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;

        for (Purchasing purchasing : buyData) {
            total = total.add(purchasing.getSubTotal());
        }

        buyTotalPrice.setText(total.toString());
    }

    @FXML
    private void searchDataBuyAction() {
        // Purchasing purchasing = buyDao.searchDataBuy(buyStockIDDropdown.getValue());
        // buyBrandField.setText(purchasing.getBrand());
        // buyTypeField.setText(purchasing.getProductType());
        // buyPriceField.setText(purchasing.getPrice().toString());
    }

    @FXML
    private void searchDataSellAction() {
        // Purchasing purchasing = buyDao.searchDataBuy(buyStockIDDropdown.getValue());
        // buyBrandField.setText(purchasing.getBrand());
        // buyTypeField.setText(purchasing.getProductType());
        // buyPriceField.setText(purchasing.getPrice().toString());
    }

    @FXML
    private void resetBuyButton() {

    }

    @FXML
    private void resetSellButton() {

    }
}
