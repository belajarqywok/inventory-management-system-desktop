package com.lestarieragemilang.app.desktop.Controller;

import java.io.IOException;

import com.lestarieragemilang.app.desktop.Utilities.Redirect;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Report {

  @FXML
  private AnchorPane setScene;

  @FXML
  public void setSceneReportSupplier(MouseEvent event) throws IOException {
    Redirect.page("laporan-supplier", setScene);
  }

  @FXML
  public void setSceneReportPurchasing(MouseEvent event) throws IOException {
    Redirect.page("laporan-pembelian", setScene);
  }
  @FXML
  public void setSceneReportSales(MouseEvent event) throws IOException {
    Redirect.page("laporan-penjualan", setScene);
  }

  @FXML
  public void setSceneReportStock(MouseEvent event) throws IOException {
    Redirect.page("laporan-stok", setScene);
  }

  @FXML
  public void setSceneReportCustomer(MouseEvent event) throws IOException {
    Redirect.page("laporan-pelanggan", setScene);
  }

  @FXML
  public void setSceneReportCategory(MouseEvent event) throws IOException {
    Redirect.page("laporan-kategori", setScene);
  }

  public void setSceneReports(MouseEvent event) throws IOException {
    Redirect.page("laporan-produk", setScene);
  }

}
