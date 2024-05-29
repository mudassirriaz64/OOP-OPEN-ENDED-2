package com.labtasks.task;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddItemsController
{
    @FXML
    private TextField product1;
    @FXML
    private TextField price1;

    @FXML
    private TextField product2;
    @FXML
    private TextField price2;

    @FXML
    private TextField product3;
    @FXML
    private TextField price3;

    @FXML
    private TextField product4;
    @FXML
    private TextField price4;

    @FXML
    private TextField product5;
    @FXML
    private TextField price5;

    private DatabaseConnection databaseConnection;

    public void initialize()
    {
        databaseConnection = MAIN.getDatabaseConnection();
    }

    @FXML
    protected void onSubmitButtonClick()
    {
        insertProduct(new Product(0, product1.getText(), Double.parseDouble(price1.getText())));
        insertProduct(new Product(0, product2.getText(), Double.parseDouble(price2.getText())));
        insertProduct(new Product(0, product3.getText(), Double.parseDouble(price3.getText())));
        insertProduct(new Product(0, product4.getText(), Double.parseDouble(price4.getText())));
        insertProduct(new Product(0, product5.getText(), Double.parseDouble(price5.getText())));
        System.out.println("Products submitted successfully.");
    }

    private void insertProduct(Product product) {
        String sql = "INSERT INTO Products (product_name, price) VALUES (?, ?)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onBackButtonClick()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        try
        {
            Parent root = loader.load();
            MainMenuController mainMenuController = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = (Stage) product1.getScene().getWindow();
            stage.setScene(scene);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
