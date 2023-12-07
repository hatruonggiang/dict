package group7;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import Dictionary.Question;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

public class QuizController extends DictionaryController {
    private static final boolean[] result = new boolean[10];
    protected static int numberOfQuestions = 10;
    protected static ArrayList<Question> questionList = new ArrayList<>();
    protected static ArrayList<Question> questions = new ArrayList<>();
    protected static String quizDataPath = "dict/src/main/resources/group7/Utils/quiz-data.txt";
    protected static int currentQuestion = 0;
    private static String[] choices = new String[10];
    private static String choice;
    private int score = 0;
    @FXML
    private Label title, question;
    @FXML
    private Label optionA, optionB, optionC, optionD;
    @FXML
    private WebView answer;
    @FXML
    private Button next, prev, exit;
    @FXML
    private Label lab1, lab2, lab3, lab4, lab5, lab6, lab7, lab8, lab9, lab10;
    private boolean review = false;
    private boolean viewResult = false;
    @FXML
    private AnchorPane screen;
    Label[] labs = { lab1, lab2, lab3, lab4, lab5, lab6, lab7, lab8, lab9, lab10 };
    private String dataPath;

    @FXML
    private AnchorPane container;

    public void initialize() throws InterruptedException {
        generateQuiz();
        final boolean[] canHandleKeyPress = { true };
        dataPath = quizDataPath;
        getQuizData();
        chooseRandomQuestions();
        displayQuestion(0);
    }

    public void getQuizData() {
        if (!questionList.isEmpty())
            return;
        File file = new File(dataPath);
        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String question = fileScanner.hasNextLine() ? fileScanner.nextLine() : "";
                String optionA = fileScanner.hasNextLine() ? fileScanner.nextLine() : "";
                String optionB = fileScanner.hasNextLine() ? fileScanner.nextLine() : "";
                String optionC = fileScanner.hasNextLine() ? fileScanner.nextLine() : "";
                String optionD = fileScanner.hasNextLine() ? fileScanner.nextLine() : "";
                String answer = fileScanner.hasNextLine() ? fileScanner.nextLine() : "";
                String explain = (fileScanner.hasNextLine() ? fileScanner.nextLine() : "") +
                        "<br>" +
                        (fileScanner.hasNextLine() ? fileScanner.nextLine() : "");
                questionList.add(new Question(question, optionA, optionB, optionC, optionD, answer, explain));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    public void chooseRandomQuestions() {
        Random random = new Random();
        int curNumOfQues = 0;
        while (curNumOfQues != numberOfQuestions) {
            int randomNum = random.nextInt(questionList.size());
            if (questionList.get(randomNum) != null) {
                questions.add(questionList.get(randomNum));
                questionList.remove(randomNum);
                curNumOfQues++;
            }
        }
    }

    public void hangman() throws Exception {
        AnchorPane component = FXMLLoader.load(getClass().getResource("/group7/game.fxml"));
        screen.getChildren().clear();
        screen.getChildren().add(component);
    }

    public void generateQuiz() throws InterruptedException {
        resetStatus();
        displayQuiz();
        next.setVisible(true);
        prev.setVisible(true);
        dataPath = quizDataPath;
        getQuizData();
        chooseRandomQuestions();
    }

    public void jumpToQuestion(int number) {
        for (String x : choices)
            deleteChoiceEffect();
        displayQuestion(number);
        displayChoice(number);
        updateQuestionBar();
        if (viewResult) {
            displayAnswer(number);
            updateQuestionBar();
        }
        if (currentQuestion == numberOfQuestions - 1)
            next.setText("Finish");
        else
            next.setText("Next");
    }

    public void updateQuestionBar() {
        if (viewResult) {
            for (int i = 0; i < result.length; i++) {
                if (result[i]) {
                    switch (i) {
                        case 0:
                            lab1.setStyle("-fx-background-color: green");
                            break;
                        case 1:
                            lab2.setStyle("-fx-background-color: green");
                            break;
                        case 2:
                            lab3.setStyle("-fx-background-color: green");
                            break;
                        case 3:
                            lab4.setStyle("-fx-background-color: green");
                            break;
                        case 4:
                            lab5.setStyle("-fx-background-color: green");
                            break;
                        case 5:
                            lab6.setStyle("-fx-background-color: green");
                            break;
                        case 6:
                            lab7.setStyle("-fx-background-color: green");
                            break;
                        case 7:
                            lab8.setStyle("-fx-background-color: green");
                            break;
                        case 8:
                            lab9.setStyle("-fx-background-color: green");
                            break;
                        case 9:
                            lab10.setStyle("-fx-background-color: green");
                    }
                } else if (!result[i]) {
                    switch (i) {
                        case 0:
                            lab1.setStyle("-fx-background-color: red");
                            break;
                        case 1:
                            lab2.setStyle("-fx-background-color: red");
                            break;
                        case 2:
                            lab3.setStyle("-fx-background-color: red");
                            break;
                        case 3:
                            lab4.setStyle("-fx-background-color: red");
                            break;
                        case 4:
                            lab5.setStyle("-fx-background-color: red");
                            break;
                        case 5:
                            lab6.setStyle("-fx-background-color: red");
                            break;
                        case 6:
                            lab7.setStyle("-fx-background-color: red");
                            break;
                        case 7:
                            lab8.setStyle("-fx-background-color: red");
                            break;
                        case 8:
                            lab9.setStyle("-fx-background-color: red");
                            break;
                        case 9:
                            lab10.setStyle("-fx-background-color: red");
                    }
                }
            }
        } else {
            if (choices[currentQuestion] != null) {
                switch (currentQuestion) {
                    case 0:
                        lab1.setStyle("-fx-background-color:grey");
                        break;
                    case 1:
                        lab2.setStyle("-fx-background-color:grey");
                        break;
                    case 2:
                        lab3.setStyle("-fx-background-color:grey");
                        break;
                    case 3:
                        lab4.setStyle("-fx-background-color:grey");
                        break;
                    case 4:
                        lab5.setStyle("-fx-background-color:grey");
                        break;
                    case 5:
                        lab6.setStyle("-fx-background-color:grey");
                        break;
                    case 6:
                        lab7.setStyle("-fx-background-color:grey");
                        break;
                    case 7:
                        lab8.setStyle("-fx-background-color:grey");
                        break;
                    case 8:
                        lab9.setStyle("-fx-background-color:grey");
                        break;
                    case 9:
                        lab10.setStyle("-fx-background-color:grey");
                }
            }
        }
    }

    public void displayQuestion(int number) {
        if (number < questions.size()) {
            // System.out.println("Current question:" + number);
            question.setText("Question " + (number + 1) + ": " +
                    questions.get(number).getQuestion());
            optionA.setText(questions.get(number).getOptionA());
            optionB.setText(questions.get(number).getOptionB());
            optionC.setText(questions.get(number).getOptionC());
            optionD.setText(questions.get(number).getOptionD());
        }
    }

    public void displayChoice(int number) {
        if (viewResult) {
            disableChoice();
            deleteChoiceEffect();
            String[] parts = questions.get(number).getAnswer().split(" ");
            switch (parts[2]) {
                case "A": {
                    optionA.setStyle("-fx-background-color: green");
                }
                    break;
                case "B": {
                    optionB.setStyle("-fx-background-color: green");
                }
                    break;
                case "C": {
                    optionC.setStyle("-fx-background-color: green");
                }
                    break;
                case "D": {
                    optionD.setStyle("-fx-background-color: green");
                }
            }
            if (choices[number] != null) {
                switch (choices[number]) {
                    case "A": {
                        if (!parts[2].equals("A")) {
                            optionA.setStyle("-fx-background-color: red");
                        }
                    }
                        break;
                    case "B": {
                        if (!parts[2].equals("B")) {
                            optionB.setStyle("-fx-background-color: red");
                        }
                    }
                        break;
                    case "C": {
                        if (!parts[2].equals("C")) {
                            optionC.setStyle("-fx-background-color: red");
                        }
                    }
                        break;
                    case "D": {
                        if (!parts[2].equals("D")) {
                            optionD.setStyle("-fx-background-color: red");
                        }
                    }
                }
            }
        } else {
            if (number < choices.length) {
                deleteChoiceEffect();
                if (choices[number] != null) {
                    switch (choices[number]) {
                        case "A":
                            optionA.setStyle("-fx-background-color:grey");
                            break;
                        case "B":
                            optionB.setStyle("-fx-background-color:grey");
                            break;
                        case "C":
                            optionC.setStyle("-fx-background-color:grey");
                            break;
                        case "D":
                            optionD.setStyle("-fx-background-color:grey");
                    }
                }
            }
        }
    }

    public void displayAnswer(int number) {
        answer.setVisible(true);

        // Check for null before accessing the properties
        Question currentQuestion = questions.get(number);
        if (currentQuestion != null) {
            String tmp = currentQuestion.getAnswer() + "<br>" +
                    currentQuestion.getExplain() + "<br>";

            // Check for null before setting the user style sheet
            URL styleSheetUrl = getClass().getResource("/group7/Utils/style.css");
            if (styleSheetUrl != null) {
                answer.getEngine().setUserStyleSheetLocation(styleSheetUrl.toString());
                answer.getEngine().loadContent(tmp);
            } else {
                System.err.println("style.css not found");
            }
        } else {
            // System.err.println("Question at index " + number + " is null");
        }
    }

    public void addChoice(int number) {
        if (choices.length == 0) {
            if (choice != null)
                choices[number] = choice;
        } else {
            if (number == choices.length) {
                if (choice != null)
                    choices[number] = choice;
            } else if (number == 0) {
                choices[number] = choice;
            } else if (choices[number] == null) {
                if (choice != null)
                    choices[number] = choice;
            } else {
                if (choice != null)
                    choices[number] = choice;
            }
        }
    }

    public void nextQuestion() {
        // System.out.println("Review: " + review);
        // System.out.println("ViewResult: " + viewResult);
        if (next.getText().equals("Finish") && !viewResult) {
            evaluate();
        }
        if (currentQuestion == 8 && viewResult) {
            next.setVisible(false);
        }
        if (currentQuestion + 1 < numberOfQuestions)
            jumpToQuestion(++currentQuestion);
        // prev.setVisible(true);
        prev.setVisible(currentQuestion > 0);
    }

    public void prevQuestion() {
        jumpToQuestion(--currentQuestion);
        if (next.getText().equals("Finish") && !viewResult) {
            evaluate();
        }
        next.setVisible(true);
        prev.setVisible(currentQuestion > 0);
    }

    public void displayQuiz() {
        // chooseRandomQuestions();
        question.setVisible(true);
        optionA.setVisible(true);
        optionB.setVisible(true);
        optionC.setVisible(true);
        optionD.setVisible(true);
        next.setVisible(true);
        // prev.setVisible(currentQuestion > 0);
        jumpToQuestion(0);
    }

    public void setWhite() {
        optionA.setStyle("-fx-background-color: #FFFFFF");
        optionB.setStyle("-fx-background-color: #FFFFFF");
        optionC.setStyle("-fx-background-color: #FFFFFF");
        optionD.setStyle("-fx-background-color: #FFFFFF");
    }

    public void setChoice(String c, Label option) {
        choice = c;
        option.setStyle("-fx-background-color:grey");
        addChoice(currentQuestion);
        updateQuestionBar();
    }

    public void getChoice() {
        optionA.setOnMouseClicked(event -> {
            setWhite();
            setChoice("A", optionA);
        });
        optionB.setOnMouseClicked(event -> {
            setWhite();
            setChoice("B", optionB);
        });
        optionC.setOnMouseClicked(event -> {
            setWhite();
            setChoice("C", optionC);
        });
        optionD.setOnMouseClicked(event -> {
            setWhite();
            setChoice("D", optionD);
        });
    }

    public void getTargetQuestion() {
        screen.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // System.out.println(currentQuestion);
                nextQuestion();
            }
        });
        lab1.setOnMouseClicked(event -> {
            // System.out.println("hello");
            currentQuestion = 0;
            jumpToQuestion(currentQuestion);
        });
        lab2.setOnMouseClicked(event -> {
            currentQuestion = 1;
            jumpToQuestion(currentQuestion);
        });
        lab3.setOnMouseClicked(event -> {
            currentQuestion = 2;
            jumpToQuestion(currentQuestion);
        });
        lab4.setOnMouseClicked(event -> {
            currentQuestion = 3;
            jumpToQuestion(currentQuestion);
        });
        lab5.setOnMouseClicked(event -> {
            currentQuestion = 4;
            jumpToQuestion(currentQuestion);
        });
        lab6.setOnMouseClicked(event -> {
            currentQuestion = 5;
            jumpToQuestion(currentQuestion);
        });
        lab7.setOnMouseClicked(event -> {
            currentQuestion = 6;
            jumpToQuestion(currentQuestion);
        });
        lab8.setOnMouseClicked(event -> {
            currentQuestion = 7;
            jumpToQuestion(currentQuestion);
        });
        lab9.setOnMouseClicked(event -> {
            currentQuestion = 8;
            jumpToQuestion(currentQuestion);
        });
        lab10.setOnMouseClicked(event -> {
            currentQuestion = 9;
            jumpToQuestion(currentQuestion);
        });
    }

    public void deleteChoiceEffect() {
        optionA.setStyle("-fx-background-color: #FFFFFF");
        optionB.setStyle("-fx-background-color: #FFFFFF");
        optionC.setStyle("-fx-background-color: #FFFFFF");
        optionD.setStyle("-fx-background-color: #FFFFFF");
    }

    public void evaluate() {
        for (int i = 0; i < choices.length; i++) {
            String[] parts = questions.get(i).getAnswer().split(" ");
            if (choices[i] != null) {
                if (choices[i].equals(parts[2])) {
                    score++;
                    result[i] = true;
                }
            }
        }
        String comment = "";
        double accuracy = (double) score / numberOfQuestions;
        if (accuracy < 0.3) {
            comment = "Cố gắng lại nào bạn.";
        } else if (accuracy >= 0.3 && accuracy <= 0.6) {
            comment = "Bạn khá nên rồi";
        } else
            comment = "Xuất xắc quá!!";
        title.setText(comment);
        review = false;
        viewResult = true;
        currentQuestion = 0;
        next.setText("Next");
        jumpToQuestion(0);
    }

    public void enableChoice() {
        optionA.setMouseTransparent(false);
        optionB.setMouseTransparent(false);
        optionC.setMouseTransparent(false);
        optionD.setMouseTransparent(false);
    }

    public void disableChoice() {
        optionA.setMouseTransparent(true);
        optionB.setMouseTransparent(true);
        optionC.setMouseTransparent(true);
        optionD.setMouseTransparent(true);
    }

    public void resetQuestionBar() {
        Label[] labs = { lab1, lab2, lab3, lab4, lab5, lab6, lab7, lab8, lab9, lab10 };
        for (Label label : labs) {
            label.setStyle("-fx-background-color: #FFFFFF");
        }
    }

    public void resetStatus() {
        // chooseRandomQuestions();
        questionList.clear();
        questions.clear();
        currentQuestion = 0;
        choices = new String[10];
        viewResult = false;
        review = false;
        enableChoice();
        answer.setVisible(false);
        score = 0;
        next.setText("Next");
        title.setText("Quiz");
        resetQuestionBar();
    }

}