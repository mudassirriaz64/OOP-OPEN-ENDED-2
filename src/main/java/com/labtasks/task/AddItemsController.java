package com.labtasks.task;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddItemsController {
    @FXML
    private TextField product;
    @FXML
    private TextField price;
    @FXML
    private TextArea description;
    @FXML
    private ImageView imageView;
    private File imageFile;

    @FXML
    private Label statusMessage;

    @FXML
    private TilePane tilePane;

    private DatabaseConnection databaseConnection;
    private Product selectedProduct;

    public void initialize()
    {
        databaseConnection = MAIN.getDatabaseConnection();
        displayProductsInTilePane();
    }

    @FXML
    protected void onSubmitButtonClick()
    {
        try {
            insertProduct(new Product(0, product.getText(), Double.parseDouble(price.getText()), imageFile, description.getText()));
            statusMessage.setText("Product submitted successfully!");
            product.clear();
            price.clear();
            description.clear();
            imageView.setImage(null);
            imageFile = null;

            displayProductsInTilePane();
        } catch (Exception e) {
            e.printStackTrace();
            statusMessage.setText("Error submitting product!");
        }
    }

    @FXML
    protected void onSelectImageClick() {
        imageFile = selectImageFile();
        if (imageFile != null) {
            imageView.setImage(new Image(imageFile.toURI().toString()));
        }
    }

    private File selectImageFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        return fileChooser.showOpenDialog(new Stage());
    }

    private void insertProduct(Product product) {
        String sql = "INSERT INTO Products (product_name, price, image_path, description) VALUES (?, ?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setString(3, product.getImagePath());
            pstmt.setString(4, product.getDescription());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayProductsInTilePane() {
        tilePane.getChildren().clear();
        for (Product product : getProductsFromDatabase()) {
            VBox productBox = createProductBox(product);
            tilePane.getChildren().add(productBox);
        }
    }

    private VBox createProductBox(Product product) {
        VBox productBox = new VBox();
        productBox.setAlignment(Pos.CENTER);
        productBox.setSpacing(5);

        Label nameLabel = new Label(product.getName());
        Label priceLabel = new Label(String.format("$%.2f", product.getPrice()));
        Label descriptionLabel = new Label(product.getDescription());

        ImageView imageView = new ImageView();
        if (product.getImagePath() != null)
        {
            imageView.setImage(new Image(new File(product.getImagePath()).toURI().toString()));
        }
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        productBox.getChildren().addAll(imageView, nameLabel, priceLabel, descriptionLabel);

        productBox.setOnMouseClicked(event -> {
            selectedProduct = product;
            statusMessage.setText("Selected: " + product.getName());
        });

        return productBox;
    }

    private List<Product> getProductsFromDatabase()
    {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, product_name, price, image_path, description FROM Products";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("product_name");
                double price = rs.getDouble("price");
                String imagePath = rs.getString("image_path");
                String description = rs.getString("description");

                products.add(new Product(id, name, price, new File(imagePath), description));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @FXML
    protected void onDeleteButtonClick() {
        if (selectedProduct != null) {
            deleteProduct(selectedProduct);
            statusMessage.setText("Deleted: " + selectedProduct.getName());
            selectedProduct = null;
            displayProductsInTilePane();
        } else {
            statusMessage.setText("No product selected to delete!");
        }
    }

    private void deleteProduct(Product product) {
        String sql = "DELETE FROM Products WHERE id = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, product.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onCloseButtonClick() {
        Stage stage = MAIN.getPrimaryStage();
        stage.close();
    }
}
