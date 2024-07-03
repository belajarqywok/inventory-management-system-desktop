package com.lestarieragemilang.app.desktop.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.lestarieragemilang.app.desktop.Dao.CategoryDao;
import com.lestarieragemilang.app.desktop.Entities.Category;
import com.lestarieragemilang.app.desktop.Utilities.CategoryTablePopulator;
import com.lestarieragemilang.app.desktop.Utilities.GenerateRandomID;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CategoryForm {
    GenerateRandomID gen = new GenerateRandomID();

    @FXML
    JFXButton editCategoryButtonText;

    // Category Table
    @FXML
    private TableColumn<Category, String> categoryIDCol, brandCategoryCol, typeCategoryCol, sizeCategoryCol,
            weightCategoryCol, unitCategoryCol;

    @FXML
    private TableView<Category> categoryTable;

    private CategoryTablePopulator categoryTablePopulator = new CategoryTablePopulator();

    @FXML
    TextField categoryIDIncrement, categorySearchField;

    @FXML
    JFXComboBox<String> categoryBrandDropDown, categoryTypeDropDown, categorySizeDropDown, categoryWeightDropDown,
            categoryUnitDropDown;

    // Category Table

    @FXML
    void addCategoryButton(ActionEvent event) {
        Category category = new Category(gen.getId(), categoryBrandDropDown.getValue(),
                categoryTypeDropDown.getValue(), categorySizeDropDown.getValue(), categoryWeightDropDown.getValue(),
                categoryUnitDropDown.getValue());
        CategoryDao categoryDao = new CategoryDao();
        categoryDao.addCategory(category);

        categoryTable.setItems(FXCollections.observableArrayList());
        categoryTable.getItems().add(category);

        tablePopulator();

    }

    void tablePopulator() {
        categoryTablePopulator.populateCategoryTable(categoryIDCol, brandCategoryCol, typeCategoryCol, sizeCategoryCol,
                weightCategoryCol, unitCategoryCol, categoryTable);
    }

    @FXML
    void editCategoryButton(ActionEvent event) {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            if (editCategoryButtonText.getText().equals("KONFIRMASI")) {
                // Confirmation alert
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Konfirmasi");
                alert.setHeaderText(null);
                alert.setContentText("Apakah Anda ingin memperbarui kategori?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    selectedCategory.setCategoryBrand(categoryBrandDropDown.getValue());
                    selectedCategory.setCategoryType(categoryTypeDropDown.getValue());
                    selectedCategory.setCategorySize(categorySizeDropDown.getValue());
                    selectedCategory.setCategoryWeight(categoryWeightDropDown.getValue());
                    selectedCategory.setCategoryUnit(categoryUnitDropDown.getValue());

                    CategoryDao categoryDao = new CategoryDao();
                    categoryDao.updateCategory(selectedCategory);

                    categoryTable.refresh();

                    editCategoryButtonText.setText("UBAH");

                    // Success alert
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Sukses");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Kategori telah berhasil diperbarui.");

                    successAlert.showAndWait();
                }
            } else {
                // Populate the fields with the selected category's details
                categoryBrandDropDown.setValue(selectedCategory.getCategoryBrand());
                categoryTypeDropDown.setValue(selectedCategory.getCategoryType());
                categorySizeDropDown.setValue(selectedCategory.getCategorySize());
                categoryWeightDropDown.setValue(selectedCategory.getCategoryWeight());
                categoryUnitDropDown.setValue(selectedCategory.getCategoryUnit());

                editCategoryButtonText.setText("KONFIRMASI");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informasi");
            alert.setHeaderText(null);
            alert.setContentText("Silakan pilih kategori yang akan diedit.");

            alert.showAndWait();
        }
    }

    @FXML
    void removeCategoryButton(ActionEvent event) {
        Category category = categoryTable.getSelectionModel().getSelectedItem();
        if (category != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Apakah Anda yakin ingin menghapus kategori berikut: \n" + category.toString() + "?",
                    ButtonType.YES,
                    ButtonType.NO);
            alert.setTitle("Kategori Dihapus");
            alert.setHeaderText(null);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                CategoryDao categoryDao = new CategoryDao();
                categoryDao.removeCategory(category);

                List<Category> categories = new ArrayList<>(categoryTable.getItems());
                categories.remove(category);
                categoryTable.setItems(FXCollections.observableArrayList(categories));

                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION,
                        "Kategori dengan ID: " + category.getCategoryId() + " dihapus.");
                infoAlert.setTitle("Kategori Dihapus");
                infoAlert.setHeaderText(null);
                infoAlert.showAndWait();
            } else {
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Penghapusan dibatalkan.");
                infoAlert.setTitle("Penghapusan dibatalkan.");
                infoAlert.setHeaderText(null);
                infoAlert.showAndWait();
            }
        }
    }

    @FXML
    void resetCategoryButton(ActionEvent event) {
        categoryIDIncrement.clear();
        categoryBrandDropDown.setValue(null);
        categoryTypeDropDown.setValue(null);
        categorySizeDropDown.setValue(null);
        categoryWeightDropDown.setValue(null);
        categoryUnitDropDown.setValue(null);
    }

    @FXML
    private void setCategoryDropDownItems(List<Category> categories, Function<Category, String> categoryMapper,
            ComboBox<String> dropDown) {
        ObservableList<String> categoryItems = categories.stream()
                .map(categoryMapper)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        dropDown.setItems(categoryItems);
    }

    void categorySearch() {
        FilteredList<Category> filteredData = new FilteredList<>(categoryTable.getItems());
        categorySearchField.textProperty()
                .addListener((observable, oldValue, newValue) -> filteredData.setPredicate(category -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (category.getCategoryBrand().toLowerCase().contains(lowerCaseFilter)
                            || category.getCategoryType().toLowerCase().contains(lowerCaseFilter)
                            || category.getCategorySize().toLowerCase().contains(lowerCaseFilter)
                            || category.getCategoryWeight().toLowerCase().contains(lowerCaseFilter)
                            || category.getCategoryUnit().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                }));
        categoryTable.setItems(filteredData);
    }

    @FXML
    public void initialize() {
        categoryTablePopulator.populateCategoryTable(categoryIDCol, brandCategoryCol, typeCategoryCol, sizeCategoryCol,
                weightCategoryCol, unitCategoryCol, categoryTable);

        CategoryDao categoryDao = new CategoryDao();

        setCategoryDropDownItems(categoryDao.getAllCategoryBrands(), Category::getCategoryBrand, categoryBrandDropDown);
        setCategoryDropDownItems(categoryDao.getAllCategoryTypes(), Category::getCategoryType, categoryTypeDropDown);
        setCategoryDropDownItems(categoryDao.getAllCategorySizes(), Category::getCategorySize, categorySizeDropDown);
        setCategoryDropDownItems(categoryDao.getAllCategoryWeights(), Category::getCategoryWeight,
                categoryWeightDropDown);
        setCategoryDropDownItems(categoryDao.getAllCategoryUnits(), Category::getCategoryUnit, categoryUnitDropDown);
        categorySearch();

    }

}
