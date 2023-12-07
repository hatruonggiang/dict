package group7;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    private static Scene scene;

    @Override

    public void start(Stage stage) throws Exception {
        // Load the dictionary.fxml
        FXMLLoader dictionaryLoader = new FXMLLoader(getClass().getResource("dictionary.fxml"));
        Parent dictionaryRoot = dictionaryLoader.load();

        // Access the AnchorPane in dictionary.fxml using the fx:id
        AnchorPane container = (AnchorPane) dictionaryLoader.getNamespace().get("container");

        // Load the search.fxml
        Parent searchRoot = FXMLLoader.load(getClass().getResource("search.fxml"));

        // Replace the content of container with the content of search.fxml
        container.getChildren().setAll(searchRoot);
        stage.setTitle("Dictionary app");
        stage.initStyle(StageStyle.TRANSPARENT);

        Scene scene = new Scene(dictionaryRoot);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

}