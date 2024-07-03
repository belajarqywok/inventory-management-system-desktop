package com.lestarieragemilang.app.desktop.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JOptionPane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.lestarieragemilang.app.desktop.Dao.SupplierDao;
import com.lestarieragemilang.app.desktop.Entities.Supplier;
import com.lestarieragemilang.app.desktop.Utilities.GenerateRandomID;
import com.lestarieragemilang.app.desktop.Utilities.SupplierTablePopulator;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class SupplierForm {
    GenerateRandomID gen = new GenerateRandomID();

    @FXML
    private JFXButton editSupplierButtonText;

    // Supplier Table
    @FXML
    private TextField supplierIDIncrement, supplierNameField, supplierContactField, supplierEmailField,
            supplierSearchField;

    @FXML
    private JFXTextArea supplierAddressField;

    @FXML
    private TableColumn<Supplier, String> supplierIDCol, supplierNameCol, supplierAddressCol, supplierContactCol,
            supplierEmailCol;

    @FXML
    private TableView<Supplier> supplierTable;

    private SupplierTablePopulator supplierTablePopulator = new SupplierTablePopulator();

    // Supplier Table

    @FXML
    void addSupplierButton(ActionEvent event) {
        Supplier supplier = new Supplier(gen.getId(), supplierNameField.getText(),
                supplierAddressField.getText(),
                supplierContactField.getText(), supplierEmailField.getText());

        SupplierDao supplierDao = new SupplierDao();
        supplierDao.addSupplier(supplier);

        supplierTable.setItems(FXCollections.observableArrayList());
        supplierTable.getItems().add(supplier);

        tablePopulator();
    }

    void tablePopulator() {
        supplierTablePopulator.populateSupplierTable(supplierIDCol, supplierNameCol, supplierAddressCol,
                supplierContactCol, supplierEmailCol, supplierTable);
    }

    @FXML
    void editSupplierButton(ActionEvent event) {
        Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
        SupplierDao supplierDao = new SupplierDao();

        if (selectedSupplier != null) {
            if (editSupplierButtonText.getText().equals("KONFIRMASI")) {
                // Update the supplier details
                selectedSupplier.setSupplierName(supplierNameField.getText());
                selectedSupplier.setSupplierAddress(supplierAddressField.getText());
                selectedSupplier.setSupplierContact(supplierContactField.getText());
                selectedSupplier.setSupplierEmail(supplierEmailField.getText());

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Update Supplier");
                alert.setHeaderText(null);
                alert.setContentText("Apakah Anda yakin ingin mengupdate supplier ini?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    supplierDao.updateSupplier(selectedSupplier);
                }

                supplierTable.refresh();

                editSupplierButtonText.setText("Edit");
            } else {
                supplierIDIncrement.setText(String.valueOf(selectedSupplier.getSupplierId()));
                supplierNameField.setText(selectedSupplier.getSupplierName());
                supplierAddressField.setText(selectedSupplier.getSupplierAddress());
                supplierContactField.setText(selectedSupplier.getSupplierContact());
                supplierEmailField.setText(selectedSupplier.getSupplierEmail());

                editSupplierButtonText.setText("KONFIRMASI");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informasi");
            alert.setHeaderText(null);
            alert.setContentText("Silakan pilih supplier untuk diedit.");

            alert.showAndWait();
        }
    }

    @FXML
    void resetSupplierButton(ActionEvent event) {
        editSupplierButtonText.setText("EDIT");
        supplierIDIncrement.clear();
        supplierNameField.clear();
        supplierAddressField.clear();
        supplierContactField.clear();
        supplierEmailField.clear();
    }

    @FXML
    void removeSupplierButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Hapus Supplier");
        alert.setHeaderText("Apakah Anda yakin ingin menghapus supplier ini?");
        alert.setContentText("Tekan OK untuk melanjutkan atau Cancel untuk membatalkan.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Supplier supplier = supplierTable.getSelectionModel().getSelectedItem();
            SupplierDao supplierDao = new SupplierDao();
            supplierDao.removeSupplier(supplier);

            List<Supplier> list = new ArrayList<>(supplierTable.getItems());
            list.remove(supplier);
            supplierTable.setItems(FXCollections.observableArrayList(list));

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Supplier Dihapus");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Supplier telah dihapus.");
            successAlert.showAndWait();
        } else {
            Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
            cancelAlert.setTitle("Penghapusan Dibatalkan");
            cancelAlert.setHeaderText(null);
            cancelAlert.setContentText("Penghapusan dibatalkan.");
            cancelAlert.showAndWait();
        }
    }

    void supplierSearch() {
        FilteredList<Supplier> filteredData = new FilteredList<>(supplierTable.getItems());
        supplierSearchField.textProperty()
                .addListener((observable, oldValue, newValue) -> filteredData.setPredicate(supplier -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (supplier.getSupplierName().toLowerCase().contains(lowerCaseFilter)
                            || supplier.getSupplierAddress().toLowerCase().contains(lowerCaseFilter)
                            || supplier.getSupplierContact().toLowerCase().contains(lowerCaseFilter)
                            || supplier.getSupplierEmail().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                }));
        supplierTable.setItems(filteredData);
    }

    @FXML
    void initialize() {
        supplierTablePopulator.populateSupplierTable(supplierIDCol, supplierNameCol, supplierAddressCol,
                supplierContactCol, supplierEmailCol, supplierTable);

        supplierTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                editSupplierButtonText.setText("Edit");
                supplierIDIncrement.clear();
                supplierNameField.clear();
                supplierAddressField.clear();
                supplierContactField.clear();
                supplierEmailField.clear();
            }
        });

        supplierSearch();
    }
}
