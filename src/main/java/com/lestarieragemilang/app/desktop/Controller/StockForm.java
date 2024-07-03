package com.lestarieragemilang.app.desktop.Controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.lestarieragemilang.app.desktop.Dao.CategoryDao;
import com.lestarieragemilang.app.desktop.Dao.StockDao;
import com.lestarieragemilang.app.desktop.Entities.Category;
import com.lestarieragemilang.app.desktop.Entities.Stock;
import com.lestarieragemilang.app.desktop.Utilities.GenerateRandomID;
import com.lestarieragemilang.app.desktop.Utilities.StockTablePopulator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class StockForm {
    GenerateRandomID gen = new GenerateRandomID();

    @FXML
    JFXButton editStockButtonText;

    // Stock Table
    @FXML
    TableColumn<Stock, String> stockIDCol, stockOnCategoryIDCol, stockBrandCol, stockTypeCol, stockSizeCol,
            stockWeightCol,
            stockUnitCol, stockQuantityCol, stockBuyPriceCol, stockSellPriceCol;

    @FXML
    private TableView<Stock> stockTable;

    @FXML
    TextField stockIDIncrement, stockSizeField, stockBuyPriceField, stockSellPriceField, stockQuantityField,
            stockSearchField;

    private StockTablePopulator stockTablePopulator = new StockTablePopulator();

    @FXML
    JFXComboBox<Object> categoryIDDropDown;

    private StockDao stockDao = new StockDao();
    private CategoryDao categoryDao = new CategoryDao();

    @FXML
    void addStockButton(ActionEvent event) {
        List<Category> categories = categoryDao.getAllCategories();
        Category selectedCategory = (Category) categoryIDDropDown.getValue();
        int categoryID = selectedCategory.getCategoryId();
        
        Stock stock = new Stock(
            gen.getId(),
            categoryID,
            selectedCategory.getCategoryBrand(),
            selectedCategory.getCategoryType(),
            selectedCategory.getCategorySize(),
            selectedCategory.getCategoryWeight(),
            selectedCategory.getCategoryUnit(),
            stockQuantityField.getText(),
            stockBuyPriceField.getText(),
            stockSellPriceField.getText());
        
        stockDao.addStock(stock);

        stockTable.setItems(FXCollections.observableArrayList());
        stockTable.getItems().add(stock);

        tablePopulator();
    }

    void tablePopulator() {
        try {
            stockTablePopulator.populateStockTable(stockIDCol, stockOnCategoryIDCol, stockBrandCol, stockTypeCol,
                    stockSizeCol,
                    stockWeightCol, stockUnitCol, stockQuantityCol, stockBuyPriceCol, stockSellPriceCol, stockTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void editStockButton(ActionEvent event) {
        Stock selectedStock = stockTable.getSelectionModel().getSelectedItem();
        if (selectedStock != null) {
            categoryIDDropDown.setValue(selectedStock);
        }

        if (selectedStock != null) {
            if (editStockButtonText.getText().equals("KONFIRMASI")) {
                // Confirmation alert
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Konfirmasi");
                alert.setHeaderText(null);
                alert.setContentText("Apakah anda ingin mengupdate stok?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {

                    selectedStock.setQuantity(stockQuantityField.getText());
                    selectedStock.setPurchasePrice(stockBuyPriceField.getText());
                    selectedStock.setPurchaseSell(stockSellPriceField.getText());

                    stockDao.updateStock(selectedStock);

                    stockTable.refresh();

                    editStockButtonText.setText("UBAH");

                    // Success alert
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Sukses");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Stok telah berhasil diperbarui.");

                    successAlert.showAndWait();
                }
            } else {
                categoryIDDropDown.setValue(selectedStock.getStockOnCategoryID());
                stockQuantityField.setText(selectedStock.getQuantity());
                stockBuyPriceField.setText(selectedStock.getPurchasePrice());
                stockSellPriceField.setText(selectedStock.getPurchaseSell());

                editStockButtonText.setText("KONFIRMASI");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informasi");
            alert.setHeaderText(null);
            alert.setContentText("Silakan pilih stok yang akan diedit.");

            alert.showAndWait();
        }
    }

    @FXML
    void resetStockButton(ActionEvent event) {

        editStockButtonText.setText("EDIT");
        stockIDIncrement.clear();
        categoryIDDropDown.setValue(null);
        stockQuantityField.clear();
        stockBuyPriceField.clear();
        stockSellPriceField.clear();
        stockQuantityField.clear();

        stockTable.refresh();
    }

    @FXML
    void removeStockButton(ActionEvent event) {
        Stock stock = stockTable.getSelectionModel().getSelectedItem(); // Declare and initialize the stock variable
        if (stock != null) { // Check if stock is not null
            Alert alert = new Alert(AlertType.CONFIRMATION,
                    "Apakah Anda yakin ingin menghapus stok berikut: \n" + stock.toString() + "?", ButtonType.YES,
                    ButtonType.NO);
            alert.setTitle("Hapus Stok");
            alert.setHeaderText(null);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                stockDao.removeStock(stock.getStockID());

                List<Stock> stocks = new ArrayList<>(stockTable.getItems());
                stocks.remove(stock);
                stockTable.setItems(FXCollections.observableArrayList(stocks));

                Alert infoAlert = new Alert(AlertType.INFORMATION,
                        "Stok dengan ID: " + stock.getStockID() + " hapus.");
                infoAlert.setTitle("Hapus Stok");
                infoAlert.setHeaderText(null);
                infoAlert.showAndWait();

            } else {
                Alert infoAlert = new Alert(AlertType.INFORMATION, "Penghapusan dibatalkan.");
                infoAlert.setTitle("Penghapusan Dibatalkan");
                infoAlert.setHeaderText(null);
                infoAlert.showAndWait();
            }
        }
    }

    void stockSearch() {
        FilteredList<Stock> filteredData = new FilteredList<>(stockTable.getItems());
        stockSearchField.textProperty()
                .addListener((observable, oldValue, newValue) -> filteredData.setPredicate(stock -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (stock.getCategoryBrand().toLowerCase().contains(lowerCaseFilter)
                            || stock.getCategoryType().toLowerCase().contains(lowerCaseFilter)
                            || stock.getCategorySize().toLowerCase().contains(lowerCaseFilter)
                            || stock.getCategoryWeight().toLowerCase().contains(lowerCaseFilter)
                            || stock.getCategoryUnit().toLowerCase().contains(lowerCaseFilter)
                            || stock.getQuantity().toString().toLowerCase().contains(lowerCaseFilter)
                            || stock.getPurchasePrice().toString().toLowerCase().contains(lowerCaseFilter)
                            || stock.getPurchaseSell().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                }));
        stockTable.setItems(filteredData);
    }

    @FXML
    public void initialize() {
        // populate stock table
        tablePopulator();

        ObservableList<Object> stockList = FXCollections.observableArrayList(stockDao.getStockCategories());
        categoryIDDropDown.setItems(stockList);
        if (!stockList.isEmpty()) {
            categoryIDDropDown.getSelectionModel().selectFirst();
        }

        stockSearch();
    }
}