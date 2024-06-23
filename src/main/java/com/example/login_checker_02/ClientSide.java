package com.example.login_checker_02;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ClientSide extends Application {
    private TextField usernameField;
    private PasswordField passwordField;
    private Label responseLabel;

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane(); // Using GridPane for layout

        // Title
        Label titleLabel = new Label("Login");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(titleLabel, 0, 0, 2, 1);

        // Username Field
        Label usernameTitle = new Label("Username:");
        usernameField = new TextField();
        gridPane.add(usernameTitle, 0, 1);
        gridPane.add(usernameField, 1, 1);

        GridPane.setMargin(usernameField, new Insets(10, 10, 10, 10));

        // Password Field
        Label passwordTitle = new Label("Password:");
        passwordField = new PasswordField();
        gridPane.add(passwordTitle, 0, 2);
        gridPane.add(passwordField, 1, 2);

        GridPane.setMargin(passwordField, new Insets(10, 10, 10, 10));

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(100);
        loginButton.setPrefHeight(30);
        gridPane.add(loginButton, 1, 3);

        // Response Label
        responseLabel = new Label();
        gridPane.add(responseLabel, 0, 4, 2, 1);

        // Set up the scene and stage
        Scene scene = new Scene(gridPane, 600, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Form");
        primaryStage.show();

        // Handle login button click
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            try {
                Socket client = new Socket("localhost", 1254);
                System.out.println("Just connected to " + client.getRemoteSocketAddress());

                // Send credentials to server
                OutputStream outToServer = client.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);
                out.writeUTF(username + "-_-" + password);

                // Read and print response from the server
                InputStream inFromServer = client.getInputStream();
                DataInputStream in = new DataInputStream(inFromServer);
                String response = in.readUTF();
                System.out.println("Server response: " + response);

                // Set the response label text and color based on the server response
                responseLabel.setText(response);
                if (response.contains("successfully")) {
                    responseLabel.setStyle("-fx-text-fill: green;");
                } else {
                    responseLabel.setStyle("-fx-text-fill: red;");
                }

                // Close the connection
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to connect to the server.");
            }
        });

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}