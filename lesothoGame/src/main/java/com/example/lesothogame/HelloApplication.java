package com.example.lesothogame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class HelloApplication extends Application {
    private final List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;

    private Label questionLabel = new Label();
    private ToggleGroup toggleGroup = new ToggleGroup();
    private Button nextButton = new Button("Next");

    @Override
    public void start(Stage primaryStage) {
        // Create questions
        questions.add(new Question("1) Who is Lesotho's current Prime Minister?", "Sam Matekane","Moeketsi Majoro", "Pakalitha Mosisili", "Mathibeli Mokhothu"));
        questions.add(new Question("2) What is the name of this mountain?", "Qiloane", "Thaba-Telle", "Thabana-Ntlenyane", "Qoqolosing"));
        questions.add(new Question("3) Do you have any idea what the name of this waterfall is?", "'Maletsunyane", "Letloepe", "Maokeng", "Victoria"));
        questions.add(new Question("4) What is the name of this dam?", "Katse", "'Muela", "Mohale", "Poli-hali"));
        questions.add(new Question("5) How many districts are there in Lesotho?", "10", "8", "5", "15"));

        // Set up UI
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(490, 380);

        questionLabel.setWrapText(true);

        root.getChildren().addAll(questionLabel, nextButton);

        // Display first question
        displayQuestion();

        // Event handler for next button
        nextButton.setOnAction(e -> {
            checkAnswer();
            // Clear selection for next question (optional)
            toggleGroup.getToggles().clear();
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                displayQuestion();
            } else {
                showFinalScore(primaryStage);
            }
        });


        // Set up scene
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lesotho Quiz");
        primaryStage.show();
    }

    // Inside the displayQuestion() method
    private void displayQuestion() {
        Question question = questions.get(currentQuestionIndex);
        questionLabel.setText(question.getQuestion());
    //    questionLabel.setStyle("-fx-font-size: 14pt;");
        toggleGroup.getToggles().clear();

        VBox optionsBox = new VBox(5);

        // Define image paths array (assuming paths are relative to resources folder)
        final String[] IMAGE_PATHS = {"p1.jpg", "p2.jpg", "p3.jpg", "p4.jpg", "p5.jpg"};

        // Check if image path exists within the array bounds
        if (currentQuestionIndex >= 0 && currentQuestionIndex < IMAGE_PATHS.length) {
            String imagePath = IMAGE_PATHS[currentQuestionIndex];
            try {
                Image image = new Image(getClass().getResource(imagePath).toExternalForm());
                if (image != null) { // Check if image is found
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(580); // Adjust width as needed
                    imageView.setFitHeight(430); // Adjust height as needed
                    optionsBox.getChildren().add(imageView);
                } else {
                    System.out.println("Image not found: " + imagePath);
                }
            } catch (Exception e) {
                System.out.println("Error loading image: " + imagePath);
            }
        } else {
            System.out.println("Image path index out of bounds.");
        }

        for (String option : question.getOptions()) {
            RadioButton radioButton = new RadioButton(option);
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setOnAction(e -> nextButton.setDisable(false));
            optionsBox.getChildren().add(radioButton);
        }

        VBox root = (VBox) questionLabel.getParent();
        root.getChildren().clear();
        root.getChildren().addAll(questionLabel, optionsBox, nextButton);
        nextButton.setDisable(true);
    }

    private void updateScoreLabel() {
        String scoreText = "Current Score: " + score + " out of " + questions.size();
        questionLabel.setText(scoreText + "\n\n" + questionLabel.getText()); // Add newline before question
    }



    private void checkAnswer() {
        Question question = questions.get(currentQuestionIndex);
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            String selectedAnswer = selectedRadioButton.getText();
            if (question.isCorrect(selectedAnswer)) {
                score++;
            }
            updateScoreLabel(); // Update score display after checking answer
        }
    }


    private void showFinalScore(Stage primaryStage) {
        VBox root = new VBox(70);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(600, 500);
        Label scoreLabel = new Label("Your final score: " + score + " out of " + questions.size());
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> primaryStage.close());
        root.getChildren().addAll(scoreLabel, closeButton);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class Question {
    private String question;
    private String correctAnswer;
    private String[] options;

    public Question(String question, String correctAnswer, String... options) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        List<String> optionsList = new ArrayList<>(Arrays.asList(options));
        optionsList.add(correctAnswer); // Include the correct answer in the options
        Collections.shuffle(optionsList); // Shuffle the options
        this.options = optionsList.toArray(new String[0]);
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public boolean isCorrect(String answer) {
        return answer.equals(correctAnswer);
    }
}
