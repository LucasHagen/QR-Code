package br.inf.ufrgs.qrcode;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Main instance;

    private Stage stage;
    private Scene scene;


    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;

        primaryStage.setTitle("Image-based QR Code");
        primaryStage.setResizable(false);

        this.stage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("controllers/home.fxml"));
        this.scene = new Scene(root, 940, 433);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getStage() {
        return stage;
    }

    public Scene getScene() {
        return scene;
    }

    public static Main getInstance() {
        return instance;
    }
}
