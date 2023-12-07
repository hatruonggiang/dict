package group7;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class HangmanController extends DictionaryController {
    List<String> wordsList;

    @FXML
    public Label outText, howPlay, lifes, debugging;
    @FXML
    public TextField letter;
    @FXML
    public Button resetButton;
    public static String mysteryWord = "ERROR";
    public static String guessedWord = "";
    public static int life = 10;
    public static int randomLetter;
    public static boolean containsLetter = false;
    public static boolean started = false;
    public static boolean isLoaded = false;
    public StringBuilder sB = new StringBuilder();

    // khởi tạo trạng thái ban đầu của trò chơi
    public void initialize() {
        sB = new StringBuilder();
        // Kiểm tra và thiết lập sự kiện cho trường nhập (letter)
        if (letter != null) {
            letter.setTextFormatter(new TextFormatter<>(this::limitToSingleLetter));
            letter.setOnKeyReleased(event -> {
                // Xử lý sự kiện khi phím Enter được nhả ra
                if (event.getCode() == KeyCode.ENTER) {
                    submitB();
                }
            });
        }
        // Đọc danh sách từ vựng từ tệp tin
        try {
            wordsList = Files.readAllLines(new File("dict/src/main/resources/group7/Utils/hangman.txt").toPath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Kiểm tra xem danh sách từ vựng có rỗng hay không
        if (!wordsList.isEmpty()) {
            // Chọn một từ ngẫu nhiên từ danh sách
            int randomIndex = new Random().nextInt(wordsList.size());
            mysteryWord = wordsList.get(randomIndex).toUpperCase();
            // Khởi tạo trạng thái của trò chơi
            guessedWord = "";
            life = 10;
            containsLetter = false;
            started = false;
        } else {
            // Hiển thị thông báo nếu có lỗi khi đọc tệp tin
            outText.setText("Something went wrong");
        }
        // Khởi tạo từ vựng đoán với các ký tự gạch ngang "-"
        for (int i = 0; i < mysteryWord.length(); i++) {
            if (guessedWord.length() <= mysteryWord.length() && !isLoaded) {
                sB.append("-");
                guessedWord = sB.toString();
            }
        }
        // Hiển thị từ vựng đoán trên giao diện
        if (outText != null)
            outText.setText(guessedWord);
        // Hiển thị số mạng còn lại
        if (lifes != null)
            lifes.setText("life: " + life);
    }

    // giới hạn phần Text nhập vào chỉ được 1 kí tự
    private TextFormatter.Change limitToSingleLetter(TextFormatter.Change change) {
        // Lấy văn bản mới từ textfield
        String text = change.getControlNewText();
        // Kiểm tra xem văn bản mới có độ dài lớn hơn 1 ký tự không
        if (text.length() > 1) {
            return null;
        }
        // Chuyển đổi tất cả các ký tự thành chữ in hoa
        change.setText(change.getText().toUpperCase());
        // Trả về đối tượng Change đã được thay đổi
        return change;
    }

    // hiển thị từ mà người chơi đã đoán đúng trong từ cần đoán
    public void updateGuessedWord(String guessedLetter) {
        StringBuilder builder = new StringBuilder(guessedWord);
        // Duyệt qua từng ký tự trong mysteryWord để kiểm tra xem có ký tự đã đoán trong
        // đó hay không
        for (int i = 0; i < mysteryWord.length(); i++) {
            // Kiểm tra xem ký tự đã đoán có xuất hiện trong mysteryWord không
            if (guessedLetter.charAt(0) == mysteryWord.charAt(i)) {
                // Nếu có, thay thế ký tự tại vị trí i trong guessedWord bằng ký tự đã đoán
                builder.setCharAt(i, guessedLetter.charAt(0));
                // Đặt biến containsLetter thành true để chỉ ra rằng guessedLetter xuất hiện
                // trong mysteryWord
                containsLetter = true;
            }
        }
        // Nếu guessedLetter không xuất hiện trong mysteryWord, giảm một lượt chơi
        if (!containsLetter)
            life--;
        // Cập nhật giá trị của guessedWord thành nội dung mới sau khi có sự thay đổi
        guessedWord = builder.toString();
    }

    // check bình thường thôi
    private void winCheck() {
        if (life <= 0 && debugging != null) {

            letter.setEditable(false);
            debugging.setWrapText(true);

            debugging.setText("Oh no, you lost!");
            outText.setText(mysteryWord);
        }

        if (Objects.equals(guessedWord, mysteryWord) && debugging != null) {

            letter.setEditable(false);

            debugging.setText("You won! ");
        }
    }

    // dùng enter để bảo rằng mình chọn từ này
    protected void submitB() {
        // Đánh dấu rằng trò chơi đã bắt đầu
        started = true;
        // Cho phép chỉnh sửa trường nhập
        letter.setEditable(true);
        // Lấy từ trường nhập và chuyển đổi thành chữ in hoa
        String guessedLetter = letter.getText().toUpperCase();
        // Kiểm tra xem người chơi đã nhập một ký tự hay không
        if (!guessedLetter.isEmpty()) {
            // Cập nhật trạng thái từ đã đoán và hiển thị kết quả
            updateGuessedWord(guessedLetter);
            outText.setText(guessedWord);
            lifes.setText("life: " + life);
            // Hiển thị thông báo tùy thuộc vào việc từ đã đoán có chứa ký tự hay không
            if (!containsLetter && debugging != null) {
                debugging.setText("There is no letter " + guessedLetter);
            } else if (containsLetter && debugging != null) {
                debugging.setText(guessedLetter + " is in this word");
            }
        }
        // Thêm ký tự đã nhập vào danh sách ký tự đã sử dụng
        addUsedLetter();
        // Đặt lại biến containsLetter và xóa trường nhập
        containsLetter = false;
        letter.setText("");
        // Kiểm tra điều kiện chiến thắng
        winCheck();
    }

    @FXML
    protected void ResetGame() {
        used.setText("Used letters: ");
        isLoaded = false;
        started = false;
        containsLetter = false;
        usedLetter.clear();
        guessedWord = "";
        debugging.setText("Type a letter");
        letter.setEditable(true);
        initialize();
    }

    @FXML
    private Label used;
    private final List<String> usedLetter = new ArrayList<>();

    public void addUsedLetter() {
        String str = letter.getText();
        str = str.toUpperCase();
        usedLetter.add(str);
        used.setText("Used letters: ");
        for (String s : usedLetter) {
            used.setText(used.getText() + s + " ");
        }
    }

    @FXML
    private AnchorPane container;

    public void quiz() throws Exception {
        try {
            AnchorPane component = FXMLLoader.load(getClass().getResource("/group7/quiz.fxml"));
            container.getChildren().clear();
            container.getChildren().add(component);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
