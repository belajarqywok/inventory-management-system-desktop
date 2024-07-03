package com.lestarieragemilang.app.desktop.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import com.jfoenix.controls.JFXButton;
import com.lestarieragemilang.app.desktop.Dao.CustomerDao;
import com.lestarieragemilang.app.desktop.Entities.Customer;
import com.lestarieragemilang.app.desktop.Utilities.CustomerTablePopulator;
import com.lestarieragemilang.app.desktop.Utilities.GenerateRandomID;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CustomerForm {
    GenerateRandomID gen = new GenerateRandomID();

    @FXML
    JFXButton editCustomerButtonText;

    // Customer Table
    @FXML
    private TableColumn<Customer, String> customerIDCol, customerNameCol, customerAddressCol, customerContactCol,
            customerEmailCol;

    @FXML
    TableView<Customer> customerTable;

    @FXML
    TextField customerIDIncrement, customerNameField, customerAddressField, customerContactField, customerEmailField,
            customerSearchField;

    private final CustomerTablePopulator customerTablePopulator = new CustomerTablePopulator();

    // Customer Table

    // add

    @FXML
    void addCustomerButton(ActionEvent event) {
        Customer customer = new Customer(gen.getId(), customerNameField.getText(),
                customerAddressField.getText(),
                customerContactField.getText(), customerEmailField.getText());

        CustomerDao customerDao = new CustomerDao();
        customerDao.addCustomer(customer);

        customerTable.setItems(FXCollections.observableArrayList());
        customerTable.getItems().add(customer);

        tablePopulator();

    }

    @FXML
    void editCustomerButton(ActionEvent event) {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            if (editCustomerButtonText.getText().equals("KONFIRMASI")) {
                // Confirmation alert
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Do you want to update the customer?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    selectedCustomer.setCustomerName(customerNameField.getText());
                    selectedCustomer.setCustomerAddress(customerAddressField.getText());
                    selectedCustomer.setCustomerContact(customerContactField.getText());
                    selectedCustomer.setCustomerEmail(customerEmailField.getText());

                    CustomerDao customerDao = new CustomerDao();
                    customerDao.updateCustomer(selectedCustomer);

                    customerTable.refresh();

                    editCustomerButtonText.setText("EDIT");

                    // Success alert
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Customer has been successfully updated.");

                    successAlert.showAndWait();
                }
            } else {
                // Populate the fields with the selected customer's details
                customerNameField.setText(selectedCustomer.getCustomerName());
                customerAddressField.setText(selectedCustomer.getCustomerAddress());
                customerContactField.setText(selectedCustomer.getCustomerContact());
                customerEmailField.setText(selectedCustomer.getCustomerEmail());

                editCustomerButtonText.setText("KONFIRMASI");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer to edit.");

            alert.showAndWait();
        }
    }

    @FXML
    void removeCustomerButton(ActionEvent event) {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        if (customer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete the following customer: \n" + customer.toString() + "?",
                    ButtonType.YES,
                    ButtonType.NO);
            alert.setTitle("Delete Customer");
            alert.setHeaderText(null);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                CustomerDao customerDao = new CustomerDao();
                customerDao.removeCustomer(customer);

                List<Customer> customers = new ArrayList<>(customerTable.getItems());
                customers.remove(customer);
                customerTable.setItems(FXCollections.observableArrayList(customers));

                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION,
                        "Customer with ID: " + customer.getCustomerId() + " deleted.");
                infoAlert.setTitle("Customer Deleted");
                infoAlert.setHeaderText(null);
                infoAlert.showAndWait();
            } else {
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Deletion cancelled.");
                infoAlert.setTitle("Deletion Cancelled");
                infoAlert.setHeaderText(null);
                infoAlert.showAndWait();
            }
        }
    }

    void tablePopulator() {
        customerTablePopulator.populateCustomerTable(customerIDCol, customerNameCol, customerAddressCol,
                customerContactCol, customerEmailCol, customerTable);
    }

    void resetField() {
        customerIDIncrement.clear();
        customerNameField.clear();
        customerAddressField.clear();
        customerContactField.clear();
        customerEmailField.clear();

    }

    @FXML
    void resetCustomerButton(ActionEvent event) {
        resetField();
    }

    void customerSearch() {
        FilteredList<Customer> filteredData = new FilteredList<>(customerTable.getItems());
        customerSearchField.textProperty()
                .addListener((observable, oldValue, newValue) -> filteredData.setPredicate(customer -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (customer.getCustomerName().toLowerCase().contains(lowerCaseFilter)
                            || customer.getCustomerAddress().toLowerCase().contains(lowerCaseFilter)
                            || customer.getCustomerContact().toLowerCase().contains(lowerCaseFilter)
                            || customer.getCustomerEmail().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                }));
        customerTable.setItems(filteredData);
    }

    @FXML
    public void initialize() {
        // Customer Table
        customerTablePopulator.populateCustomerTable(customerIDCol, customerNameCol, customerAddressCol,
                customerContactCol, customerEmailCol, customerTable);

        customerSearch();
    }
}
