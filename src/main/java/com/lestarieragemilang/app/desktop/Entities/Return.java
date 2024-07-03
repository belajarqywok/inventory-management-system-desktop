package com.lestarieragemilang.app.desktop.Entities;

import java.time.LocalDate;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Return {
    private final SimpleObjectProperty<LocalDate> returnDate;
    private final SimpleStringProperty returnID;
    private final SimpleStringProperty returnType;
    private final SimpleStringProperty invoiceNumber;
    private final SimpleStringProperty reason;

    public Return(LocalDate returnDate, String returnID, String returnType, String invoiceNumber, String reason) {
        this.returnDate = new SimpleObjectProperty<>(returnDate);
        this.returnID = new SimpleStringProperty(returnID);
        this.returnType = new SimpleStringProperty(returnType);
        this.invoiceNumber = new SimpleStringProperty(invoiceNumber);
        this.reason = new SimpleStringProperty(reason);
    }

    public SimpleObjectProperty<LocalDate> returnDateProperty() {
        return returnDate;
    }

    public StringProperty returnIDProperty() {
        return returnID;
    }

    public StringProperty returnTypeProperty() {
        return returnType;
    }

    public StringProperty invoiceNumberProperty() {
        return invoiceNumber;
    }

    public StringProperty reasonProperty() {
        return reason;
    }

    public LocalDate getReturnDate() {
        return returnDate.get();
    }

    public String getReturnID() {
        return returnID.get();
    }

    public String getReturnType() {
        return returnType.get();
    }

    public String getInvoiceNumber() {
        return invoiceNumber.get();
    }

    public String getReason() {
        return reason.get();
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate.set(returnDate);
    }

    public void setReturnID(String returnID) {
        this.returnID.set(returnID);
    }

    public void setReturnType(String returnType) {
        this.returnType.set(returnType);
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber.set(invoiceNumber);
    }

    public void setReason(String reason) {
        this.reason.set(reason);
    }
}
