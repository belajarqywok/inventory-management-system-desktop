package com.lestarieragemilang.app.desktop.Configurations.ReportConfiguration;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lestarieragemilang.app.desktop.Configurations.DatabaseConfiguration;

public class JasperLoader extends DatabaseConfiguration {

  private static final Logger LOGGER = Logger.getLogger(JasperLoader.class.getName());

  public void showJasperReportSupplier(URL location, String nameSupplier, String contactSupplier,
      String addressSupplier, String emailSupplier, MouseEvent event) {
    try {
      JasperReport jasperReport = (JasperReport) JRLoader.loadObject(location);
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("nameSupplier", "%" + nameSupplier + "%");
      parameters.put("contactSupplier", "%" + contactSupplier + "%");
      parameters.put("addressSupplier", "%" + addressSupplier + "%");
      parameters.put("emailSupplier", "%" + emailSupplier + "%");
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, getConnection());

      final JasperViewer viewer = new JasperViewer(jasperPrint, false);
      Platform.runLater(() -> viewer.setVisible(true));

    } catch (JRException e) {
      LOGGER.log(Level.SEVERE, "Error loading Jasper Report", e);
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText(e.getMessage());
      alert.showAndWait();
    }
  }

  public void showJasperReportCustomer(URL location, String nameCustomer, String contactCustomer,
      String addressCustomer, String emailCustomer, MouseEvent event) {
    try {
      JasperReport jasperReport = (JasperReport) JRLoader.loadObject(location);
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("nameCustomer", "%" + nameCustomer + "%");
      parameters.put("contactCustomer", "%" + contactCustomer + "%");
      parameters.put("addressCustomer", "%" + addressCustomer + "%");
      parameters.put("emailCustomer", "%" + emailCustomer + "%");
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, getConnection());

      final JasperViewer viewer = new JasperViewer(jasperPrint, false);
      Platform.runLater(() -> viewer.setVisible(true));

    } catch (JRException e) {
      LOGGER.log(Level.SEVERE, "Error loading Jasper Report", e);
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText(e.getMessage());
      alert.showAndWait();
    }
  }

  public void showJasperReportCategory(URL location, String brandCategory, String typeCategory,
      String sizeCategory, String weightCategory, String unitCategory, MouseEvent event) {
    try {
      JasperReport jasperReport = (JasperReport) JRLoader.loadObject(location);
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("brandCategory", "%" + brandCategory + "%");
      parameters.put("typeCategory", "%" + typeCategory + "%");
      parameters.put("sizeCategory", "%" + sizeCategory + "%");
      parameters.put("weightCategory", "%" + weightCategory + "%");
      parameters.put("unitCategory", "%" + unitCategory + "%");
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, getConnection());

      final JasperViewer viewer = new JasperViewer(jasperPrint, false);
      Platform.runLater(() -> viewer.setVisible(true));

    } catch (JRException e) {
      LOGGER.log(Level.SEVERE, "Error loading Jasper Report", e);
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText(e.getMessage());
      alert.showAndWait();
    }
  }

  public void showJasperReportStock(URL location, String brandStock, String typeStock,
      String sizeStock, String weightStock, String unitStock, String stock, String purchasePriceStock,
      String sellingPriceStock, MouseEvent event) {
    try {
      JasperReport jasperReport = (JasperReport) JRLoader.loadObject(location);
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("brandStock", "%" + brandStock + "%");
      parameters.put("typeStock", "%" + typeStock + "%");
      parameters.put("sizeStock", "%" + sizeStock + "%");
      parameters.put("weightStock", "%" + weightStock + "%");
      parameters.put("unitStock", "%" + unitStock + "%");
      parameters.put("stock", "%" + stock + "%");
      parameters.put("purchasePriceStock", "%" + purchasePriceStock + "%");
      parameters.put("sellingPriceStock", "%" + sellingPriceStock + "%");
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, getConnection());

      final JasperViewer viewer = new JasperViewer(jasperPrint, false);
      Platform.runLater(() -> viewer.setVisible(true));

    } catch (JRException e) {
      LOGGER.log(Level.SEVERE, "Error loading Jasper Report", e);
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText(e.getMessage());
      alert.showAndWait();
    }
  }

  public void showJasperReportBuyList(URL location, String invoicePurchasing, Date firstDate, Date secondDate,
      MouseEvent event) {
    try {
      JasperReport jasperReport = (JasperReport) JRLoader.loadObject(location);
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("invoicePurchasing", "%" + invoicePurchasing + "%");
      parameters.put("firstDate", firstDate);
      parameters.put("secondDate", secondDate);
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, getConnection());

      final JasperViewer viewer = new JasperViewer(jasperPrint, false);
      Platform.runLater(() -> viewer.setVisible(true));

    } catch (JRException e) {
      LOGGER.log(Level.SEVERE, "Error loading Jasper Report", e);
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText(e.getMessage());
      alert.showAndWait();
    }
  }

  public void showJasperReportBuy(URL location, Integer buyIdValue) {
    try {
      JasperReport jasperReport = (JasperReport) JRLoader.loadObject(location);
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("invoicePurchasing", "%" + buyIdValue.toString() + "%");
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, getConnection());

      final JasperViewer viewer = new JasperViewer(jasperPrint, false);
      Platform.runLater(() -> viewer.setVisible(true));

    } catch (JRException e) {
      LOGGER.log(Level.SEVERE, "Error loading Jasper Report", e);
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText(e.getMessage());
      alert.showAndWait();
    }
  }

  public void showJasperReportSell(URL location, Integer sellIdValue) {
    try {
      JasperReport jasperReport = (JasperReport) JRLoader.loadObject(location);
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("invoiceSales", "%" + sellIdValue.toString() + "%");
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, getConnection());

      final JasperViewer viewer = new JasperViewer(jasperPrint, false);
      Platform.runLater(() -> viewer.setVisible(true));

    } catch (JRException e) {
      LOGGER.log(Level.SEVERE, "Error loading Jasper Report", e);
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText(e.getMessage());
      alert.showAndWait();
    }
  }

  public void showJasperReportSellList(URL location, String invoiceSales, Date firstDate, Date secondDate,
      MouseEvent event) {
    try {
      JasperReport jasperReport = (JasperReport) JRLoader.loadObject(location);
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("invoiceSales", "%" + invoiceSales + "%");
      parameters.put("firstDate", firstDate);
      parameters.put("secondDate", secondDate);
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, getConnection());

      final JasperViewer viewer = new JasperViewer(jasperPrint, false);
      Platform.runLater(() -> viewer.setVisible(true));

    } catch (JRException e) {
      LOGGER.log(Level.SEVERE, "Error loading Jasper Report", e);
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText(e.getMessage());
      alert.showAndWait();
    }
  }
}
