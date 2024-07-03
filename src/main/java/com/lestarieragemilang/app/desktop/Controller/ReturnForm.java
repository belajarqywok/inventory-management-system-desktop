package com.lestarieragemilang.app.desktop.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;

import javafx.fxml.FXML;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import com.lestarieragemilang.app.desktop.Entities.Return;
import com.lestarieragemilang.app.desktop.Dao.ReturnDao;

import java.util.Optional;

import java.time.LocalDate;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ReturnForm {

    @FXML
    private TableView<Return> returnTable;

    @FXML
    private TableColumn<Return, LocalDate> returnDateCol;

    @FXML
    private TableColumn<Return, String> returnIDCol;

    @FXML
    private TableColumn<Return, String> returnInvoiceCol;

    @FXML
    private TableColumn<Return, String> returnTypeCol;

    @FXML
    private TableColumn<Return, String> returnReasonCol;

    @FXML
    private DatePicker returnDate;

    @FXML
    private TextField returnIDIncrement;

    @FXML
    private JFXRadioButton returnIsBuy;

    @FXML
    private JFXRadioButton returnIsSell;

    @FXML
    private JFXComboBox<String> returnInvoicePurchasing;

    @FXML
    private TextArea returnReasonField;

    @FXML
    private JFXButton editReturnButtonText;

    @FXML
    private JFXButton searchReturnButton;

    @FXML
    private TextField searchTextField;

    private ToggleGroup returnTypeGroup;
    private ReturnDao returnDao;

    @FXML
    public void initialize() {
        try {
            returnDao = new ReturnDao();

            returnDateCol.setCellValueFactory(cellData -> cellData.getValue().returnDateProperty());
            returnIDCol.setCellValueFactory(cellData -> cellData.getValue().returnIDProperty());
            returnInvoiceCol.setCellValueFactory(cellData -> cellData.getValue().invoiceNumberProperty());
            returnTypeCol.setCellValueFactory(cellData -> cellData.getValue().returnTypeProperty());
            returnReasonCol.setCellValueFactory(cellData -> cellData.getValue().reasonProperty());

            loadDataFromDatabase();

            returnTypeGroup = new ToggleGroup();
            returnIsBuy.setToggleGroup(returnTypeGroup);
            returnIsSell.setToggleGroup(returnTypeGroup);

            returnTypeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == returnIsBuy) {
                    loadInvoiceNumbers("purchasing");
                    returnInvoicePurchasing.getSelectionModel().selectFirst();
                } else if (newValue == returnIsSell) {
                    loadInvoiceNumbers("sales");
                    returnInvoicePurchasing.getSelectionModel().selectFirst();
                }
            });

            returnInvoicePurchasing.getSelectionModel().selectedItemProperty()
                    .addListener((observable, oldValue, newValue) -> {
                        if (newValue != null) {
                            handleInvoiceSelection(newValue);
                        }
                    });

            returnTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    handleTableSelection();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDataFromDatabase() throws SQLException {
        returnTable.getItems().setAll(returnDao.getAllReturns());
    }

    @FXML
    private void searchingData() throws SQLException {
        returnTable.getItems().setAll(returnDao.searchReturns(
                searchTextField.getText()));
    }

    private void loadInvoiceNumbers(String type) {
        try {
            returnInvoicePurchasing.getItems().setAll(returnDao.getInvoiceNumbers(type));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleInvoiceSelection(String invoiceNumber) {
        try {
            if (returnIsSell.isSelected()) {
                returnDao.getSalesByInvoice(invoiceNumber);
            } else if (returnIsBuy.isSelected()) {
                returnDao.getPurchasingByInvoice(invoiceNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void resetReturnButton() {
        returnDate.setValue(null);
        returnIDIncrement.setText("");
        returnTypeGroup.selectToggle(null);
        returnInvoicePurchasing.getItems().clear();
        returnReasonField.setText("");
        editReturnButtonText.setText("EDIT");
    }

    @FXML
    private void addReturnButton() {
        try {

            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Melakukan Pengembalian");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda Yakin Ingin Melakukan Pengembalian Barang ?.");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                Return newReturn = new Return(
                        returnDate.getValue(),
                        returnIDIncrement.getText(),
                        returnIsBuy.isSelected() ? "PEMBELIAN" : "PENJUALAN",
                        returnInvoicePurchasing.getValue(),
                        returnReasonField.getText());

                returnDao.addReturn(newReturn);

                returnTable.getItems().clear();
                loadDataFromDatabase();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editReturnButton() throws SQLException {
        Return selectedReturn = returnTable.getSelectionModel().getSelectedItem();

        if (selectedReturn != null) {
            if (editReturnButtonText.getText().equals("KONFIRMASI")) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("KONFIRMASI");
                alert.setHeaderText(null);
                alert.setContentText("Apakah Anda ingin memperbarui pengembaliannya?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    selectedReturn.setReturnDate(returnDate.getValue());
                    selectedReturn.setReturnType(returnIsBuy.isSelected() ? "PEMBELIAN" : "PENJUALAN");
                    selectedReturn.setInvoiceNumber(returnInvoicePurchasing.getValue());
                    selectedReturn.setReason(returnReasonField.getText());

                    returnDao.updateReturn(selectedReturn);

                    returnTable.refresh();

                    editReturnButtonText.setText("EDIT");

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Sukses");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Pengembalian telah berhasil diperbarui.");

                    successAlert.showAndWait();
                }
            } else {
                returnDate.setValue(selectedReturn.getReturnDate());
                returnIsBuy.setSelected(selectedReturn.getReturnType().equals("PEMBELIAN"));
                returnInvoicePurchasing.setValue(selectedReturn.getInvoiceNumber());
                returnReasonField.setText(selectedReturn.getReason());

                editReturnButtonText.setText("KONFIRMASI");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informasi");
            alert.setHeaderText(null);
            alert.setContentText("Silakan pilih kembali untuk mengedit.");

            alert.showAndWait();
        }
    }

    @FXML
    private void removeReturnButton() {
        Return selectedItem = returnTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {

                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Membatalkan Pengembalian");
                alert.setHeaderText(null);
                alert.setContentText("Apakah Anda Yakin Ingin Membatalkan Pengembalian Barang ?.");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    returnDao.deleteReturn(selectedItem.getReturnID());
                    loadDataFromDatabase();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleTableSelection() {
        Return selectedItem = returnTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            returnDate.setValue(selectedItem.returnDateProperty().get());
            returnIDIncrement.setText(selectedItem.getReturnID());

            String returnType = selectedItem.returnTypeProperty().get();
            if (returnType.equals("PEMBELIAN")) {
                returnIsBuy.setSelected(true);
            } else if (returnType.equals("PENJUALAN")) {
                returnIsSell.setSelected(true);
            }

            returnInvoicePurchasing.setValue(selectedItem.invoiceNumberProperty().get());
            returnReasonField.setText(selectedItem.reasonProperty().get());
        }
    }

}
