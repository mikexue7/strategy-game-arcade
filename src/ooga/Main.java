package ooga;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ooga.view.GameScreen;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main extends Application {
    /**
     * Start of the program.
     */
    public static void main (String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
//        VBox vBox = new VBox(new Label("A JavaFX Label"));
//        Scene scene = new Scene(vBox);
//        primaryStage.setScene(scene);
//        primaryStage.setWidth(500);
//        primaryStage.setHeight(500);
//        primaryStage.show();
        GameScreen game = new GameScreen(primaryStage);

    }
}
