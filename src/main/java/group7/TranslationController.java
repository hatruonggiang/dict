package group7;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Dictionary.TranslatorAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

public class TranslationController implements Initializable {

    private String sourceLanguage = "en";
    private String toLanguage = "vi";
    private boolean isToVietnameseLang = true;

    // true:eng->viet
    // false:viet->eng
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        translateBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    handleOnClickTranslateBtn();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        // when user click on english text field
        sourceLangField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (sourceLangField.getText().trim().isEmpty()) {
                    translateBtn.setDisable(true);
                } else {
                    translateBtn.setDisable(false);
                }
            }
        });
        // initial state
        translateBtn.setDisable(true);
        toLangField.setEditable(false);
    }

    @FXML
    private void handleOnClickTranslateBtn() throws IOException {
        String input = sourceLangField.getText();

        TranslatorAPI task;

        if (isToVietnameseLang == true) {
            task = new TranslatorAPI("en", "vi", input);
        } else {
            task = new TranslatorAPI("vi", "en", input);
        }

        task.setOnSucceeded(event -> {
            toLangField.setText(task.getValue());

        });

        task.setOnRunning(event -> {
            toLangField.setText("Translating...");

        });

        task.setOnFailed(event -> {
            toLangField.setText("Cannot translate this sentence");

        });
        new Thread(task).start();
    }

    @FXML
    private void handleOnClickSwitchToggle() {
        sourceLangField.clear();
        toLangField.clear();
        if (isToVietnameseLang) {
            englishLabel.setLayoutX(426);
            vietnameseLabel.setLayoutX(104);
            sourceLanguage = "vi";
            toLanguage = "en";
        } else {
            englishLabel.setLayoutX(100);
            vietnameseLabel.setLayoutX(426);
            sourceLanguage = "en";
            toLanguage = "vi";
        }
        String swap = toLangField.getText();
        toLangField.setText(sourceLangField.getText());
        sourceLangField.setText(swap);
        isToVietnameseLang = !isToVietnameseLang;
    }

    @FXML
    private TextArea sourceLangField, toLangField;

    @FXML
    private Button translateBtn;

    @FXML
    private Label englishLabel, vietnameseLabel;
}