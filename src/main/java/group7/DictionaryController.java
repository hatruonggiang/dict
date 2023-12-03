package group7;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DictionaryController {

    @FXML
    private void switchToSearch() throws IOException {
        showSub("/group7/search.fxml");
    }

    @FXML
    private void switchToAddition() throws IOException {
        showSub("/group7/addition.fxml");
    }

    @FXML
    private void switchToTranslation() throws IOException {
        showSub("/group7/translation.fxml");
    }

    @FXML
    private void switchToGame() throws IOException {
        showSub("/group7/game.fxml");
    }

    public void EApp(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private AnchorPane container;
    @FXML
    private Button closeBtn;

    protected void setNode(Node node) {
        container.getChildren().clear();
        container.getChildren().add(node);
    }

    protected void showSub(String path) throws IOException {
        AnchorPane component = FXMLLoader.load(getClass().getResource(path));
        setNode(component);
    }
}
