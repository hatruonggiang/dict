package group7;

import Dictionary.Alerts;
import Dictionary.Dictionary;
import Dictionary.DictionaryManagement;
import Dictionary.Word;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdditionController extends DictionaryController implements Initializable {
	@FXML
	private Button addBtn;
	@FXML
	private TextField wordTargetInput;
	@FXML
	private TextArea explanationInput;
	@FXML
	private Label successAlert;

	// Alerts
	private Alerts alerts = new Alerts();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// get data for dictionary from data.txt file
		dictionaryManagement.insertFromFile(dictionary, path);
		if (explanationInput.getText().isEmpty() || wordTargetInput.getText().isEmpty()) {
			addBtn.setDisable(true);
		}
		wordTargetInput.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (explanationInput.getText().isEmpty() || wordTargetInput.getText().isEmpty()) {
					addBtn.setDisable(true);
				} else {
					addBtn.setDisable(false);
				}
			}
		});
		explanationInput.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (explanationInput.getText().isEmpty() || wordTargetInput.getText().isEmpty()) {
					addBtn.setDisable(true);
				} else {
					addBtn.setDisable(false);
				}
			}
		});

		// initial state
		successAlert.setVisible(false);
	}

	@FXML
	private void handleClickAddBtn() {
		Alert alertConfirmation = alerts.alertConfirmation("Add word",
				"Bạn chắc chắn muốn thêm từ này?");
		Optional<ButtonType> option = alertConfirmation.showAndWait();
		// get data from input
		String englishWord = wordTargetInput.getText().trim();
		String meaning = explanationInput.getText().trim();

		if (option.get() == ButtonType.OK) {
			Word word = new Word(englishWord, meaning);
			if (dictionary.contains(word)) {
				// find index of word in dictionary
				int indexOfWord = dictionaryManagement.searchWord(dictionary, englishWord);
				// show confirmation alert
				Alert selectionAlert = alerts.alertConfirmation("This word already exists",
						"Từ này đã tồn tại.\n" +
								"Thay thế hoặc bổ sung nghĩa vừa nhập cho nghĩa cũ.");
				// custom button
				selectionAlert.getButtonTypes().clear();
				ButtonType replaceBtn = new ButtonType("replace");
				ButtonType insertBtn = new ButtonType("insert");
				selectionAlert.getButtonTypes().addAll(replaceBtn, insertBtn, ButtonType.CANCEL);
				Optional<ButtonType> selection = selectionAlert.showAndWait();

				if (selection.get() == replaceBtn) {
					dictionary.get(indexOfWord).setWordExplain(meaning);
					dictionaryManagement.exportToFile(dictionary, path);
					showSuccessAlert();
				}
				if (selection.get() == insertBtn) {
					String oldMeaning = dictionary.get(indexOfWord).getWordExplain();
					dictionary.get(indexOfWord).setWordExplain(oldMeaning + "\n-" + meaning);
					dictionaryManagement.exportToFile(dictionary, path);
					showSuccessAlert();
				}
				if (selection.get() == ButtonType.CANCEL) {
					alerts.showAlertInfo("Information", "Thay đổi không được công nhận.");
				}
			} else {
				dictionary.add(word);
				dictionaryManagement.addWord(word, path);
				// successfully
				showSuccessAlert();
			}
			// reset input
			addBtn.setDisable(true);
			resetInput();
		} else if (option.get() == ButtonType.CANCEL) {
			alerts.showAlertInfo("Information", "Thay đổi không được công nhận.");
		}
	}

	private void resetInput() {
		wordTargetInput.setText("");
		explanationInput.setText("");
	}

	private void showSuccessAlert() {
		successAlert.setVisible(true);
		// automatic hide success alert
		// dictionaryManagement.setTimeout(() -> successAlert.setVisible(false), 100);
	}

}