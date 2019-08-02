package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    Scene scene;
    Pane root;

    Button startButton;
    Label labelNameGame;
    Label labelSizeMatrixWord;
    Label labelMaxNumberWordsInMatrix;
    Label labelComplexity;
    Label labelPlayer;
    Spinner<Integer> spinnerMaxNumberWordsInMatrix;
    Spinner<Integer> spinnerSizeMatrixWord;
    RadioButton rdButComputer;
    RadioButton rdButHuman;
    RadioButton rdButComplexityEasy;
    RadioButton rdButComplexityHard;

    MenuInfo menuInfo;

    @Override
    public void start(Stage primaryStage) throws Exception{
        startInitialSettings();
        setPosition();

        checkEventMenu();
        if (checkDataMenu())
            createGame();


        primaryStage.setTitle("Гра слова");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public boolean checkDataMenu()
    {
        return true;
    }

    public void createGame()
    {
        System.out.println("creating game...");
    }


    public void checkEventMenu()
    {
        rdButHuman.setOnAction(e -> {
            rdButHuman.setSelected(true);
            rdButComputer.setSelected(false);
        });

        rdButComputer.setOnAction(e -> {
            rdButHuman.setSelected(false);
            rdButComputer.setSelected(true);
        });

        rdButComplexityEasy.setOnAction(e -> {
            rdButComplexityEasy.setSelected(true);
            rdButComplexityHard.setSelected(false);
        });

        rdButComplexityHard.setOnAction(e -> {
            rdButComplexityEasy.setSelected(false);
            rdButComplexityHard.setSelected(true);
        });

        startButton.setOnAction(e -> {
            String player,complexity;

            if (rdButHuman.isSelected())
                player = "Людина";
            else
                player = "Комп'ютер";

            if (rdButComplexityEasy.isSelected())
                complexity = "Легко";
            else
                complexity = "Важко";
            menuInfo = new MenuInfo(spinnerSizeMatrixWord.getValue(),
                    spinnerMaxNumberWordsInMatrix.getValue(),player,complexity);
            setVisibleMenu(false);
        });

    }

    public void setVisibleMenu(boolean flag)
    {
        startButton.setVisible(flag);
        labelNameGame.setVisible(flag);
        labelSizeMatrixWord.setVisible(flag);
        labelMaxNumberWordsInMatrix.setVisible(flag);
        labelComplexity.setVisible(flag);
        labelPlayer.setVisible(flag);
        spinnerMaxNumberWordsInMatrix.setVisible(flag);
        spinnerSizeMatrixWord.setVisible(flag);
        rdButComputer.setVisible(flag);
        rdButHuman.setVisible(flag);
        rdButComplexityEasy.setVisible(flag);
        rdButComplexityHard.setVisible(flag);
    }

    public void setPosition()
    {
        labelNameGame.setLayoutX(500);
        labelNameGame.setLayoutY(200);
        Font font=new Font(50);
        labelNameGame.setFont(font);
        rdButHuman.setLayoutY(200);
        rdButComputer.setLayoutY(300);
    }

    public void startInitialSettings()
    {
        int widthWindow = 1400;
        int heightWindow = 900;
        int minNumberWords = 1;
        int maxNumberWords = 100;
        int startValueWords = 8;
        int minSize = 3;
        int maxSize = 20;
        int startValueSize = 5;


        root = new Pane();
        scene = new Scene(root, widthWindow, heightWindow);


        spinnerMaxNumberWordsInMatrix = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory
                        (minNumberWords, maxNumberWords, startValueWords);
        spinnerMaxNumberWordsInMatrix.setValueFactory(valueFactory);
        spinnerMaxNumberWordsInMatrix.setEditable(true);


        spinnerSizeMatrixWord = new Spinner<>();
        valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory
                        (minSize, maxSize, startValueSize);
        spinnerSizeMatrixWord.setValueFactory(valueFactory);
        spinnerSizeMatrixWord.setEditable(true);


        labelSizeMatrixWord = new Label("Розмір поля");
        labelMaxNumberWordsInMatrix = new Label("Кількість слів");


        rdButComputer = new RadioButton("Комп'ютер");
        rdButComputer.setSelected(true);
        rdButHuman = new RadioButton("Людина");
        rdButHuman.setSelected(false);


        rdButComplexityEasy = new RadioButton("Легко");
        rdButComplexityEasy.setSelected(true);
        rdButComplexityHard = new RadioButton("Важко");
        rdButComplexityHard.setSelected(false);


        labelComplexity = new Label("Складність");
        labelPlayer = new Label("Гравець");


        labelNameGame = new Label("ГРА СЛОВА");
        startButton = new Button("Почати гру");


        root.getChildren().add(spinnerMaxNumberWordsInMatrix);
        root.getChildren().add(spinnerSizeMatrixWord);
        root.getChildren().add(labelSizeMatrixWord);
        root.getChildren().add(labelMaxNumberWordsInMatrix);
        root.getChildren().add(rdButComputer);
        root.getChildren().add(rdButHuman);
        root.getChildren().add(rdButComplexityEasy);
        root.getChildren().add(rdButComplexityHard);
        root.getChildren().add(labelPlayer);
        root.getChildren().add(labelComplexity);
        root.getChildren().add(startButton);
        root.getChildren().add(labelNameGame);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
