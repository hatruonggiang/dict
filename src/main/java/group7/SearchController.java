package group7;

import Dictionary.Alerts;
import Dictionary.Dictionary;
import Dictionary.DictionaryManagement;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.time.Duration;
import java.util.Optional;
import java.util.ResourceBundle;

public class SearchController extends DictionaryController implements Initializable {
    // FXML elements
    @FXML
    private TextField searchTerm;
    @FXML
    private Button saveBtn, volumeBtn;
    @FXML
    private Label englishWord, headerList;
    @FXML
    private TextArea explanation;
    @FXML
    private ListView<String> listResults;
    @FXML
    private Pane headerOfExplanation;
    // list for listView
    ObservableList<String> list = FXCollections.observableArrayList();
    // alerts
    private Alerts alerts = new Alerts();
    // index of selected word
    private int indexOfSelectedWord;
    // the first index of list found
    private int firstIndexOfListFound = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // get data for dictionary from data.txt file
        dictionaryManagement.insertFromFile(dictionary, path);

        // set data for trie
        dictionaryManagement.setTrie(dictionary);

        // set initial word list and definition
        setListDefault(0);

        // when user types in search field
        searchTerm.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (searchTerm.getText().isEmpty()) {

                    setListDefault(0);
                } else {

                    handleOnKeyTyped();
                }
            }
        });

        // initial state
        explanation.setEditable(false);
        saveBtn.setVisible(false);

    }

    // click search button
    @FXML
    private void handleOnKeyTyped() {
        // Xóa nội dung danh sách
        list.clear();
        // Lấy và làm sạch chuỗi tìm kiếm
        String searchKey = searchTerm.getText().trim();
        // Tìm kiếm từ vựng dựa trên chuỗi tìm kiếm
        list = dictionaryManagement.lookupWord(dictionary, searchKey);
        // Kiểm tra nếu danh sách kết quả tìm kiếm rỗng
        if (list.isEmpty()) {
            // Đặt lại danh sách mặc định
            setListDefault(firstIndexOfListFound);
        } else {
            // Hiển thị danh sách kết quả tìm kiếm
            listResults.setItems(list);
            // Đặt chỉ số đầu tiên của danh sách kết quả
            firstIndexOfListFound = dictionaryManagement.searchWord(dictionary, list.get(0));
        }
    }

    // click a word in word list is found
    @FXML
    private void handleMouseClickAWord(MouseEvent arg0) {
        // Lấy từ vựng được chọn từ danh sách kết quả
        String selectedWord = listResults.getSelectionModel().getSelectedItem();
        // Kiểm tra xem từ vựng được chọn có khác null hay không
        if (selectedWord != null) {
            // Tìm kiếm chỉ số của từ vựng trong từ điển
            indexOfSelectedWord = dictionaryManagement.searchWord(dictionary, selectedWord);
            // Kiểm tra xem từ vựng có tồn tại trong từ điển không
            if (indexOfSelectedWord == -1) {
                return;
            }
            // Hiển thị thông tin về từ vựng được chọn
            englishWord.setText(dictionary.get(indexOfSelectedWord).getWordTarget());
            explanation.setText(dictionary.get(indexOfSelectedWord).getWordExplain());
            // Cập nhật trạng thái giao diện
            headerOfExplanation.setVisible(true);
            explanation.setVisible(true);
            explanation.setEditable(false);
            saveBtn.setVisible(false);
        }
        handleClickSoundBtn();
    }

    @FXML
    private void handleClickEditBtn() {
        // Cập nhật trạng thái cho phép chỉnh sửa
        explanation.setEditable(true);
        // Hiển thị nút lưu và thông báo cho người dùng
        saveBtn.setVisible(true);
        alerts.showAlertInfo("Information", "Bạn đã cho phép chỉnh sửa nghĩa từ này!");
    }

    @FXML
    private void handleClickSoundBtn() {
        // Thiết lập thuộc tính voices cho FreeTTS, sử dụng giọng đọc "kevin16"
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        // Lấy đối tượng Voice từ VoiceManager, sử dụng giọng đọc "kevin16"
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");
        // Kiểm tra xem giọng đọc có tồn tại hay không
        if (voice != null) {
            // Cấp phát giọng đọc
            voice.allocate();
            // Phát âm từ vựng của từ được chọn từ từ điển
            voice.speak(dictionary.get(indexOfSelectedWord).getWordTarget());
        } else {
            throw new IllegalStateException("Cannot find voice: kevin16");
        }
    }

    @FXML
    private void handleClickSaveBtn() {
        Alert alertConfirmation = alerts.alertConfirmation("Update",
                "Bạn chắc chắn muốn cập nhật nghĩa từ này ?");
        // Lấy lựa chọn từ người dùng (OK hoặc Cancel)
        Optional<ButtonType> option = alertConfirmation.showAndWait();
        if (option.get() == ButtonType.OK) {
            // Thực hiện cập nhật từ trong từ điển
            dictionaryManagement.updateWord(dictionary, indexOfSelectedWord, explanation.getText(), path);
            // Hiển thị thông báo cập nhật thành công
            alerts.showAlertInfo("Information", "Cập nhập thành công!");
        } else {
            // Hiển thị thông báo thất bại nếu người dùng chọn Cancel
            alerts.showAlertInfo("Information", "Thay đổi không được công nhận!");
        }
        // Cập nhật trạng thái giao diện sau khi lưu
        saveBtn.setVisible(false);
        explanation.setEditable(false);
    }

    @FXML
    private void handleClickDeleteBtn() {
        Alert alertWarning = alerts.alertWarning("Delete", "Bạn chắc chắn muốn xóa từ này?");
        alertWarning.getButtonTypes().add(ButtonType.CANCEL);
        // Lấy lựa chọn từ người dùng (OK hoặc Cancel)
        Optional<ButtonType> option = alertWarning.showAndWait();
        if (option.get() == ButtonType.OK) {
            // Xóa từ đã chọn từ từ điển
            dictionaryManagement.deleteWord(dictionary, indexOfSelectedWord, path);
            // Làm mới mọi thứ sau khi xóa từ
            refreshAfterDeleting();
            // Hiển thị thông báo xóa thành công
            alerts.showAlertInfo("Information", "Xóa thành công");
        } else {
            // Hiển thị thông báo thất bại nếu người dùng chọn Cancel
            alerts.showAlertInfo("Information", "Thay đổi không được công nhận");
        }
    }

    private void refreshAfterDeleting() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(englishWord.getText())) {
                list.remove(i);
                break;
            }
        }
        listResults.setItems(list);
        // Cập nhật trạng thái giao diện sau khi xóa
        headerOfExplanation.setVisible(false);
        explanation.setVisible(false);
    }

    private void setListDefault(int index) {
        list.clear();
        for (int i = index; i < index + 30; i++) {
            list.add(dictionary.get(i).getWordTarget());
        }
        listResults.setItems(list);
        englishWord.setText(dictionary.get(index).getWordTarget());
        explanation.setText(dictionary.get(index).getWordExplain());
    }

}