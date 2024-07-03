package com.lestarieragemilang.app.desktop.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.lestarieragemilang.app.desktop.Dao.AuthDao;
import com.lestarieragemilang.app.desktop.Utilities.Redirect;

import animatefx.animation.FadeIn;

public class AuthController {
    @FXML
    private Label greetingText;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label localTimeApp;

    @FXML
    private StackPane authStackPane;
    @FXML
    private VBox loginView;
    @FXML
    private VBox registerView;

    @FXML
    private TextField loginUsername;
    @FXML
    private PasswordField loginPassword;

    @FXML
    private TextField registerEmail;
    @FXML
    private TextField registerUsername;
    @FXML
    private PasswordField registerPassword;
    @FXML
    private PasswordField confirmPassword;

    @FXML
    private ListView<String> profileListView;

    private static final NavigableMap<Integer, String> GREETINGS = new TreeMap<>();

    public void populateProfiles() {
        AuthDao authDao = new AuthDao();
        List<String> profiles = authDao.fetchProfiles();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        for (String profile : profiles) {
            String[] parts = profile.split("\\|\\|");
            if (parts.length < 2) {
                System.err.println("Invalid profile format: " + profile);
                continue;
            }
            String email = parts[0].trim();
            String username = parts[1].trim();
            observableList.add("[" + email + "] - [" + username + "]");
        }
        profileListView.setItems(observableList);
    }

    static {
        GREETINGS.put(0, "Selamat pagi, Admin!");
        GREETINGS.put(12, "Selamat siang, Admin!");
        GREETINGS.put(15, "Selamat sore, Admin!");
        GREETINGS.put(18, "Selamat malam, Admin!");
    }

    private final AuthDao authDao = new AuthDao();

    public void initialize() {
        populateProfiles();
    }

    @FXML
    private void loginToApp() throws IOException {
        if (authDao.loginRepo(loginUsername.getText(), loginPassword.getText())) {
            Redirect.page("dashboard", anchorPane);
            new FadeIn(anchorPane).play();
        } else {
            Alert confirmationDialog = new Alert(Alert.AlertType.ERROR);
            confirmationDialog.getDialogPane().setPrefSize(450, 250);

            confirmationDialog.setTitle("Login Gagal");
            confirmationDialog.setHeaderText("Username Atau Password Anda Salah!!!...");

            confirmationDialog.getButtonTypes().setAll(ButtonType.YES);
            confirmationDialog.showAndWait();
        }
    }

    @FXML
    private void registerAdmin() {
        if (!registerPassword.getText().equals(confirmPassword.getText())) {
            System.out.println("Passwords tidak cocok!");
            return;
        }
        System.out.println("Mencoba mendaftar: " + registerUsername.getText());
        try {
            if (authDao.registerRepo(registerUsername.getText(), registerEmail.getText(), registerUsername.getText(),
                    registerPassword.getText())) {
                populateProfiles(); // Refresh the table
                Alert confirmationDialog = new Alert(Alert.AlertType.INFORMATION);
                confirmationDialog.getDialogPane().setPrefSize(450, 250);

                confirmationDialog.setTitle("Register Berhasil");
                confirmationDialog.setHeaderText("Anda berhasil mendaftar!!!...");

                confirmationDialog.getButtonTypes().setAll(ButtonType.YES);
                confirmationDialog.showAndWait();
            } else {
                Alert confirmationDialog = new Alert(Alert.AlertType.ERROR);
                confirmationDialog.getDialogPane().setPrefSize(450, 250);

                confirmationDialog.setTitle("Register Gagal");
                confirmationDialog.setHeaderText("Gagal mendaftar!!!...");

                confirmationDialog.getButtonTypes().setAll(ButtonType.YES);
                confirmationDialog.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteAdmin() {
        String selectedProfile = profileListView.getSelectionModel().getSelectedItem();
        if (selectedProfile != null) {
            String[] parts = selectedProfile.split(" - ");
            if (parts.length < 2) {
                System.err.println("Format profil tidak valid: " + selectedProfile);
                return;
            }
            String username = parts[1].trim().replace("]", ""); // Extract username
            username = username.substring(1); // Remove the leading "["
            try {
                if (authDao.deleteRepo(username)) {
                    populateProfiles(); // Refresh the table
                    Alert confirmationDialog = new Alert(Alert.AlertType.INFORMATION);
                    confirmationDialog.getDialogPane().setPrefSize(450, 250);

                    confirmationDialog.setTitle("Delete Berhasil");
                    confirmationDialog.setHeaderText("Admin berhasil dihapus!!!...");

                    confirmationDialog.getButtonTypes().setAll(ButtonType.YES);
                    confirmationDialog.showAndWait();
                } else {
                    Alert confirmationDialog = new Alert(Alert.AlertType.ERROR);
                    confirmationDialog.getDialogPane().setPrefSize(450, 250);

                    confirmationDialog.setTitle("Delete Gagal");
                    confirmationDialog.setHeaderText("Gagal menghapus admin!!!...");

                    confirmationDialog.getButtonTypes().setAll(ButtonType.YES);
                    confirmationDialog.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert confirmationDialog = new Alert(Alert.AlertType.ERROR);
            confirmationDialog.getDialogPane().setPrefSize(450, 250);

            confirmationDialog.setTitle("Delete Gagal");
            confirmationDialog.setHeaderText("Tidak ada admin yang dipilih!!!...");

            confirmationDialog.getButtonTypes().setAll(ButtonType.YES);
            confirmationDialog.showAndWait();
        }
    }

    @FXML
    private void showRegisterView() {
        loginView.setVisible(false);
        registerView.setVisible(true);
    }

    @FXML
    private void showLoginView() {
        registerView.setVisible(false);
        loginView.setVisible(true);
    }

    @FXML
    public void exitApp() {
        System.exit(0);
    }
}
