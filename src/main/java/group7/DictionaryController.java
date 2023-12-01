package group7;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

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
    private AnchorPane container;

    protected void setNode(Node node) {
        container.getChildren().clear();
        container.getChildren().add(node);
    }

    protected void showSub(String path) throws IOException {
        AnchorPane component = FXMLLoader.load(getClass().getResource(path));
        setNode(component);
    }
}
