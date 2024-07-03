package com.lestarieragemilang.app.desktop.Controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;

import com.lestarieragemilang.app.desktop.Utilities.Redirect;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Dashboard {
    @FXML
    private AnchorPane setScene;

    @FXML
    private Label localTimeApp;

    // @FXML
    // private BorderPane bp;

    @FXML
    public void initialize() throws IOException {
        Redirect.page("stock", setScene);
        new FadeIn(setScene).play();
        showDateAndTime();
    }

    void showDateAndTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy '['HH:mm:ss']'",
                new Locale.Builder().setLanguageTag("id").build());

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime currentTime = LocalDateTime.now();
            localTimeApp.setText(currentTime.format(formatter));
        }),
                new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    @FXML
    void isExitApp(MouseEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    @FXML
    void isMinimizeApp(MouseEvent event) {
        Stage stage = (Stage) setScene.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void setSceneStock(MouseEvent event) throws IOException {
        Redirect.page("stock", setScene);
        new FadeIn(setScene).play();
    }

    @FXML
    void setSceneSupplier(MouseEvent event) throws IOException {
        Redirect.page("supplier", setScene);
        new FadeIn(setScene).play();
    }

    @FXML
    void setSceneCategory(MouseEvent event) throws IOException {
        Redirect.page("category", setScene);
        new FadeIn(setScene).play();
    }

    @FXML
    void setSceneCustomer(MouseEvent event) throws IOException {
        Redirect.page("customer", setScene);
        new FadeIn(setScene).play();
    }

    @FXML
    void setSceneTransactions(MouseEvent event) throws IOException {
        Redirect.page("transactions", setScene);
        new FadeIn(setScene).play();
    }

    @FXML
    void setSceneReturn(MouseEvent event) throws IOException {
        Redirect.page("return", setScene);
        new FadeIn(setScene).play();
    }

    @FXML
    void setSceneReport(MouseEvent event) throws IOException {
        Redirect.page("laporan", setScene);
        new FadeIn(setScene).play();
    }

    @FXML
    void setSceneAI(MouseEvent event) throws IOException {
        Redirect.page("ai", setScene);
        new FadeIn(setScene).play();
    }

    @FXML
    void setSceneLogin(MouseEvent event) {
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Apakah anda yakin ingin kembali ke halaman login?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/lestarieragemilang/app/desktop/login.fxml"));
                Parent loginRoot = loader.load();

                Stage stage = (Stage) setScene.getScene().getWindow();

                Scene loginScene = new Scene(loginRoot);

                new FadeOut(setScene).play();

                PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
                delay.setOnFinished(e -> {
                    stage.setScene(loginScene);

                    new FadeIn(loginRoot).play();
                });
                delay.play();

            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Gagal memuat halaman login: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
}