package com.lestarieragemilang.app.desktop.Controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.lestarieragemilang.app.desktop.Configurations.ReportConfiguration.JasperLoader;
import com.lestarieragemilang.app.desktop.Entities.Transactions.Sales;
import com.lestarieragemilang.app.desktop.Utilities.SellTablePopulator;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ReportSales {

  @FXML
  private DatePicker SellListDateFirstField;

  @FXML
  private DatePicker SellListDateSecondField;

  @FXML
  private TextField SellListSearchField;

  @FXML
  private TableColumn<?, ?> sellBrandCol;

  @FXML
  private TableColumn<?, ?> sellDateCol;

  @FXML
  private TableColumn<?, ?> sellInvoiceCol;

  @FXML
  private TableColumn<?, ?> sellOnCustomerNameCol;

  @FXML
  private TableColumn<?, ?> sellPriceCol;

  @FXML
  private TableColumn<?, ?> sellSubTotalCol;

  @FXML
  private TableView<Sales> sellTable;

  @FXML
  private TableColumn<?, ?> sellTotalCol;

  @FXML
  private TableColumn<?, ?> sellTypeCol;

  @FXML
  void printJasperSellList(MouseEvent event) throws IOException, URISyntaxException {
    String path = "/com/lestarieragemilang/app/desktop/jasper/sales-list.jasper";
    URL url = ReportSales.class.getResource(path).toURI().toURL();
    try {
      JasperLoader loader = new JasperLoader();
      LocalDate firstLocalDate = SellListDateFirstField.getValue();
      LocalDate secondLocalDate = SellListDateSecondField.getValue();

      Date firstDate = firstLocalDate != null
          ? Date.from(firstLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
          : null;
      Date secondDate = secondLocalDate != null
          ? Date.from(secondLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
          : null;

      loader.showJasperReportSellList(
          url,
          SellListSearchField.getText(),
          firstDate,
          secondDate,
          event);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  void sellSearch() {
    FilteredList<Sales> filteredData = new FilteredList<>(sellTable.getItems());
    SellListSearchField.textProperty()
        .addListener((observable, oldValue, newValue) -> filteredData.setPredicate(sell -> {
          if (newValue == null || newValue.isEmpty()) {
            return true;
          }
          String lowerCaseFilter = newValue.toLowerCase();
          if (String.valueOf(sell.getInvoiceNumber()).toLowerCase().contains(lowerCaseFilter)) {
            return true;
          }
          return false;
        }));
    sellTable.setItems(filteredData);
  }

  @FXML
  void initialize() throws SQLException {
    SellTablePopulator sellTablePopulator = new SellTablePopulator();
    sellTablePopulator.populateSellTable(sellDateCol, sellInvoiceCol, sellOnCustomerNameCol, sellBrandCol, sellTypeCol,
        sellTotalCol, sellPriceCol, sellSubTotalCol, sellTable);

    SellListSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.isEmpty()) {
        SellListDateFirstField.setValue(null);
        SellListDateSecondField.setValue(null);
      }
    });

    SellListDateFirstField.valueProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        SellListSearchField.clear();
      }
    });

    SellListDateSecondField.valueProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        SellListSearchField.clear();
      }
    });
    sellSearch();
  }

}
