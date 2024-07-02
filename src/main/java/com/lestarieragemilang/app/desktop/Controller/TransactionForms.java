package com.lestarieragemilang.app.desktop.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.util.Optional;

import com.lestarieragemilang.app.desktop.Configurations.DatabaseConfiguration;
import com.lestarieragemilang.app.desktop.Dao.Transactions.Implement.BuyDaoImpl;
import com.lestarieragemilang.app.desktop.Dao.Transactions.Implement.SaleDaoImpl;
import com.lestarieragemilang.app.desktop.Entities.Transactions.Invoice;
import com.lestarieragemilang.app.desktop.Entities.Transactions.Purchasing;
import com.lestarieragemilang.app.desktop.Entities.Transactions.Sales;
import com.lestarieragemilang.app.desktop.Utilities.GenerateRandomID;

public class TransactionForms {

    private int currentInvoiceNumber;
    @FXML
    private DatePicker buyDate, sellDate;
    @FXML
    private TextField buyInvoiceNumber, buyBrandField, buyTypeField, buyPriceField, supplierNameField, buyTotalField,
            buyTotalPrice, sellInvoiceNumber, sellBrandField, sellTypeField, sellPriceField, customerNameField,
            sellTotalField, sellTotalPrice;
    @FXML
    private ComboBox<Object> buyStockIDDropdown, supplierIDDropDown;
    @FXML
    private ComboBox<Integer> sellStockIDDropdown, customerIDDropDown;
    @FXML
    private TableView<Purchasing> buyTable;
    @FXML
    private TableView<Sales> sellTable;
    @FXML
    private TableColumn<Purchasing, LocalDate> buyDateCol;
    @FXML
    private TableColumn<Purchasing, Integer> buyInvoiceCol;
    @FXML
    private TableColumn<Purchasing, String> buyOnSupplierNameCol, buyBrandCol, buyTypeCol;
    @FXML
    private TableColumn<Purchasing, Integer> buyTotalCol;
    @FXML
    private TableColumn<Purchasing, BigDecimal> buyPriceCol, buySubTotalCol;
    @FXML
    private TableColumn<Sales, LocalDate> sellDateCol;
    @FXML
    private TableColumn<Sales, Integer> sellInvoiceCol;
    @FXML
    private TableColumn<Sales, String> sellOnCustomerNameCol, sellBrandCol, sellTypeCol;
    @FXML
    private TableColumn<Sales, Integer> sellTotalCol;
    @FXML
    private TableColumn<Sales, BigDecimal> sellPriceCol, sellSubTotalCol;

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

        sellDateCol.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
        sellInvoiceCol.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        sellOnCustomerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        sellBrandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        sellTypeCol.setCellValueFactory(new PropertyValueFactory<>("productType"));
        sellTotalCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        sellPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        sellSubTotalCol.setCellValueFactory(new PropertyValueFactory<>("subTotal"));

        buyDateCol.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
        buyInvoiceCol.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        buyOnSupplierNameCol.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        buyBrandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        buyTypeCol.setCellValueFactory(new PropertyValueFactory<>("productType"));
        buyTotalCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        buyPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        buySubTotalCol.setCellValueFactory(new PropertyValueFactory<>("subTotal"));

        buyTable.setItems(buyData);
        sellTable.setItems(sellData);

        loadBuyData();
        loadSellData();

        loadStockIDs();
        loadSupplierIDs();
        loadIDs();

        buyDate.setValue(LocalDate.now());
        sellDate.setValue(LocalDate.now());

        System.out.println("buyDate: " + buyDate.getValue());
        System.out.println("sellDate: " + sellDate.getValue());

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
        }
    }

    @FXML
    private void addSellButton() {
        Sales sales = new Sales();

        // Initialize the Invoice object if it's null
        if (sales.getInvoice() == null) {
            sales.setInvoice(new Invoice());
        }

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
        calculateTotalPrice();
    }

    @FXML
    private void editBuyButton() {
        Purchasing selectedPurchasing = buyTable.getSelectionModel().getSelectedItem();
        if (selectedPurchasing != null) {
            // Populate the form fields with the data from the selected item
            buyDate.setValue(selectedPurchasing.getInvoice().getInvoiceDate());
            buyStockIDDropdown.setValue(selectedPurchasing.getStockId());
            buyBrandField.setText(selectedPurchasing.getBrand());
            buyTypeField.setText(selectedPurchasing.getProductType());
            buyPriceField.setText(selectedPurchasing.getPrice().toString());
            supplierIDDropDown.setValue(selectedPurchasing.getSupplierId());
            supplierNameField.setText(selectedPurchasing.getSupplierName());
            buyTotalField.setText(String.valueOf(selectedPurchasing.getQuantity()));

            // Allow the user to modify the data in the form fields

            // When the user clicks the save button, update the data in the database
            // with the new values from the form fields
            selectedPurchasing.getInvoice().setInvoiceDate(buyDate.getValue());
            selectedPurchasing.setStockId((int) buyStockIDDropdown.getValue());
            selectedPurchasing.setBrand(buyBrandField.getText());
            selectedPurchasing.setProductType(buyTypeField.getText());
            selectedPurchasing.setPrice(new BigDecimal(buyPriceField.getText()));
            selectedPurchasing.setSupplierId((int) supplierIDDropDown.getValue());
            selectedPurchasing.setSupplierName(supplierNameField.getText());
            selectedPurchasing.setQuantity(Integer.parseInt(buyTotalField.getText()));
            selectedPurchasing.setSubTotal(
                    selectedPurchasing.getPrice().multiply(BigDecimal.valueOf(selectedPurchasing.getQuantity())));
            selectedPurchasing.setPriceTotal(selectedPurchasing.getSubTotal());

            buyDao.updatePurchasing(selectedPurchasing);
            loadBuyData();
        } else {
            // Show an error message if no item is selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an item to edit.");
            alert.showAndWait();
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
    private void editSellButton() {
        Sales selectedSales = sellTable.getSelectionModel().getSelectedItem();
        if (selectedSales != null) {
            // Populate the form fields with the data from the selected item
            sellDate.setValue(selectedSales.getInvoice().getInvoiceDate());
            sellStockIDDropdown.setValue(selectedSales.getStockId());
            sellBrandField.setText(selectedSales.getBrand());
            sellTypeField.setText(selectedSales.getProductType());
            sellPriceField.setText(selectedSales.getPrice().toString());
            customerIDDropDown.setValue(selectedSales.getCustomerId());
            customerNameField.setText(selectedSales.getCustomerName());
            sellTotalField.setText(String.valueOf(selectedSales.getQuantity()));

            // Allow the user to modify the data in the form fields

            // When the user clicks the save button, update the data in the database
            // with the new values from the form fields
            selectedSales.getInvoice().setInvoiceDate(sellDate.getValue());
            selectedSales.setStockId((int) sellStockIDDropdown.getValue());
            selectedSales.setBrand(sellBrandField.getText());
            selectedSales.setProductType(sellTypeField.getText());
            selectedSales.setPrice(new BigDecimal(sellPriceField.getText()));
            selectedSales.setCustomerId((int) customerIDDropDown.getValue());
            selectedSales.setCustomerName(customerNameField.getText());
            selectedSales.setQuantity(Integer.parseInt(sellTotalField.getText()));
            selectedSales
                    .setSubTotal(selectedSales.getPrice().multiply(BigDecimal.valueOf(selectedSales.getQuantity())));
            selectedSales.setPriceTotal(selectedSales.getSubTotal());

            saleDao.updateSales(selectedSales);
            loadSellData();
        } else {
            // Show an error message if no item is selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an item to edit.");
            alert.showAndWait();
        }
    }

    @FXML
    private void removeSellButton() {
        Sales selectedSales = sellTable.getSelectionModel().getSelectedItem();
        if (selectedSales != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to remove this sales item?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                sellTable.getItems().remove(selectedSales);
                calculateSellTotalPrice();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a sales item to remove.");
            alert.showAndWait();
        }
    }

    @FXML
    private void confirmSellButton() {
        List<Sales> salesList = new ArrayList<>(sellTable.getItems());
        confirmSales(salesList);
        sellTable.getItems().clear();
        currentInvoiceNumber = generateInvoiceNumber();
        sellInvoiceNumber.setText(String.valueOf(currentInvoiceNumber));
        calculateTotalPrice();
    }

    @FXML
    private void calculateSellTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (Sales sales : sellTable.getItems()) {
            total = total.add(sales.getSubTotal());
        }
        sellTotalPrice.setText(total.toString());
    }

    @FXML
    private void searchDataSellAction() {
        FilteredList<Sales> filteredData = new FilteredList<>(sellData);
        // Implement the logic to filter the sales data based on the search query
        sellTable.setItems(filteredData);
    }

    @FXML
    private void resetSellButton() {
        sellDate.setValue(LocalDate.now());
        currentInvoiceNumber = saleDao.generateInvoiceNumber();
        sellInvoiceNumber.setText(String.valueOf(currentInvoiceNumber));
        sellStockIDDropdown.getSelectionModel().selectFirst();
        sellBrandField.setText("");
        sellTypeField.setText("");
        sellPriceField.setText("");
        customerIDDropDown.getSelectionModel().clearSelection();
        customerNameField.setText("");
        sellTotalField.setText("");
        sellTotalPrice.setText("");
        sellData.clear();
        sellTable.setItems(sellData);
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
            confirmSales(salesList);
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
    private void calculateBuyTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;

        for (Purchasing purchasing : buyData) {
            total = total.add(purchasing.getSubTotal());
        }

        buyTotalPrice.setText(total.toString());
    }

    // calculateSellTotalPrice

    @FXML
    private void searchDataBuyAction() {
        // Purchasing purchasing = buyDao.searchDataBuy(buyStockIDDropdown.getValue());
        // buyBrandField.setText(purchasing.getBrand());
        // buyTypeField.setText(purchasing.getProductType());
        // buyPriceField.setText(purchasing.getPrice().toString());
    }

    @FXML
    private void resetBuyButton() {

    }

}
