package com.lestarieragemilang.app.desktop.Controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.lestarieragemilang.app.desktop.Configurations.ReportConfiguration.JasperLoader;
import com.lestarieragemilang.app.desktop.Entities.Transactions.Purchasing;
import com.lestarieragemilang.app.desktop.Utilities.BuyTablePopulator;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ReportPurchasing {

  @FXML
  private DatePicker BuyListDateFirstField;

  @FXML
  private DatePicker BuyListDateSecondField;

  @FXML
  private TextField BuyListSearchField;

  @FXML
  private TableColumn<?, ?> buyBrandCol;

  @FXML
  private TableColumn<?, ?> buyDateCol;

  @FXML
  private TableColumn<?, ?> buyInvoiceCol;

  @FXML
  private TableColumn<?, ?> buyOnSupplierNameCol;

  @FXML
  private TableColumn<?, ?> buyPriceCol;

  @FXML
  private TableColumn<?, ?> buySubTotalCol;

  @FXML
  private TableView<Purchasing> buyTable;

  @FXML
  private TableColumn<?, ?> buyTotalCol;

  @FXML
  private TableColumn<?, ?> buyTypeCol;

  @FXML
  void purchaseSearch() {
    FilteredList<Purchasing> filteredData = new FilteredList<>(buyTable.getItems());
    BuyListSearchField.textProperty()
        .addListener((observable, oldValue, newValue) -> filteredData.setPredicate(purchase -> {
          if (newValue == null || newValue.isEmpty()) {
            return true;
          }
          String lowerCaseFilter = newValue.toLowerCase();
          if (String.valueOf(purchase.getInvoiceNumber()).toLowerCase().contains(lowerCaseFilter)) {
            return true;
          }
          return false;
        }));
    buyTable.setItems(filteredData);
  }

  @FXML
  void printJasperBuyList(MouseEvent event) throws IOException, URISyntaxException {
    String path = "/com/lestarieragemilang/app/desktop/jasper/purchasing-list.jasper";
    URL url = ReportPurchasing.class.getResource(path).toURI().toURL();
    try {
      JasperLoader loader = new JasperLoader();

      LocalDate firstLocalDate = BuyListDateFirstField.getValue();
      LocalDate secondLocalDate = BuyListDateSecondField.getValue();

      Date firstDate = firstLocalDate != null
          ? Date.from(firstLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
          : null;
      Date secondDate = secondLocalDate != null
          ? Date.from(secondLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
          : null;

      loader.showJasperReportBuyList(
          url,
          BuyListSearchField.getText(),
          firstDate,
          secondDate,
          event);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  void initialize() throws SQLException {
    BuyTablePopulator buyTablePopulator = new BuyTablePopulator();
    buyTablePopulator.populateBuyTable(buyDateCol, buyInvoiceCol, buyOnSupplierNameCol, buyBrandCol, buyTypeCol,
        buyTotalCol, buyPriceCol, buySubTotalCol, buyTable);

    BuyListSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.isEmpty()) {
        BuyListDateFirstField.setValue(null);
        BuyListDateSecondField.setValue(null);
      }
    });

    BuyListDateFirstField.valueProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        BuyListSearchField.clear();
      }
    });

    BuyListDateSecondField.valueProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        BuyListSearchField.clear();
      }
    });
    purchaseSearch();
  }

}
