package com.restaurant.luch;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Главный класс приложения ресторана "Луч"
 */
public class RestaurantApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(RestaurantApplication.class.getResource("auth-view.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 1000, 700);

        stage.setTitle("Ресторан 'Луч' - Авторизация");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}