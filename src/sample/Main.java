package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
//-----------------Main-------------------------//

    private Scene scene;
    private Pane root;

//-----------------Menu------------------------//

    private Button butStart;
    private Label labNameGame;
    private Label labSizeMatrixWord;
    private Label labMaxNumberWordsInMatrix;
    private Label labComplexity;
    private Label labPlayer;
    private Spinner<Integer> spinMaxNumberWordsInMatrix;
    private Spinner<Integer> spinSizeMatrixWord;
    private RadioButton rdButComputer;
    private RadioButton rdButHuman;
    private RadioButton rdButComplexityEasy;
    private RadioButton rdButComplexityHard;

//----------------------------------------------//

//----------------GameMenu----------------------//

    private Button[][] matrixWords;
    private Button butFinishGame;
    private Button butFinishStep;
    private TextArea txtArAllWords;
    private TextArea txtArFirstPlayerWords;
    private TextArea txtArSecondPlayerWords;
    private Label labTimer;
    private Label labTypeFirstPlayer;
    private Label labTypeSecondPlayer;
    private Label labScoreFirstPlayer;
    private Label labScoreSecondPlayer;
    private Label labTitle;
    private Label labInfoSizeMatrix;
    private Label labInfoNumberWords;
    private Label labInfoComplexity;
    private Line lnUp;
    private Line lnDown;
    private Line lnRight;

//----------------------------------------------//

//----------------Data--------------------------//

    private MenuInfo menuInfo;
    private int widthWindow = 1400;
    private int heightWindow = 900;
    private int minNumberWords = 1;
    private int maxNumberWords = 100;
    private int startValueWords = 5;
    private int minSize = 4;
    private int maxSize = 20;
    private int startValueSize = 10;

//----------------------------------------------//

    @Override
    public void start(Stage primaryStage) {
        startInitialData();
        startInitialSettingsMenu();
        startInitialSettingsGame();
        setVisibleGame(false);
        setPositionElementInMenu();
        setPositionElementInGame();
        checkEventMenu();
        checkEventGame();

        primaryStage.setTitle("Гра слова");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void createGame()
    {
        buildMatrixButton(menuInfo.getSizeMatrix(),menuInfo.getSizeMatrix());
        setVisibleMenu(false);
        setVisibleGame(true);
        setInfoForGame();
    }

    public void setInfoForGame()
    {
        labInfoComplexity.setText("Складність : "+menuInfo.getComplexity());
        labInfoNumberWords.setText("Слів : "+menuInfo.getNumberWords());
        labInfoSizeMatrix.setText("Розмір поля : "+menuInfo.getSizeMatrix()+
                "x"+menuInfo.getSizeMatrix());
    }

    public void buildMatrixButton(int length,int height)
    {
        matrixWords = new Button[height][length];
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < length; x++)
            {
                matrixWords[x][y] = new Button("1");
                root.getChildren().add(matrixWords[x][y]);
                matrixWords[x][y].setMaxSize(40,40);
                matrixWords[x][y].setMinSize(5,5);
                matrixWords[x][y].setLayoutX(x*30+20);
                matrixWords[x][y].setLayoutY(y*30+70);

                int finalX = x;
                int finalY = y;
                EventHandler<ActionEvent> event = e -> {
                    matrixWords[finalX][finalY].setStyle("-fx-background-color: #57df31");
                };

                matrixWords[x][y].setOnAction(event);
            }
        }
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

        butStart.setOnAction(e -> {
            String player,complexity;
            if (rdButHuman.isSelected())
                player = "Людина";
            else
                player = "Комп'ютер";

            if (rdButComplexityEasy.isSelected())
                complexity = "Легко";
            else
                complexity = "Важко";

            menuInfo = new MenuInfo(spinSizeMatrixWord.getValue(),
                    spinMaxNumberWordsInMatrix.getValue(),player,complexity);
            createGame();
        });

    }

    public void checkEventGame()
    {
        butFinishGame.setOnAction(e -> {
            removeMatrixWords();
            setVisibleGame(false);
            setVisibleMenu(true);
        });
    }

    public void removeMatrixWords()
    {
        for (int i = 0; i < matrixWords.length; i++)
        {
            for (int j = 0; j < matrixWords[i].length; j++)
                root.getChildren().remove(matrixWords[i][j]);
        }
    }

    public void setVisibleGame(boolean flag)
    {
        butFinishGame.setVisible(flag);
        butFinishStep.setVisible(flag);
        lnUp.setVisible(flag);
        lnRight.setVisible(flag);
        lnDown.setVisible(flag);
        txtArAllWords.setVisible(flag);
        txtArFirstPlayerWords.setVisible(flag);
        txtArSecondPlayerWords.setVisible(flag);
        labTypeFirstPlayer.setVisible(flag);
        labTypeSecondPlayer.setVisible(flag);
        labScoreFirstPlayer.setVisible(flag);
        labScoreSecondPlayer.setVisible(flag);
        labTimer.setVisible(flag);
        labTitle.setVisible(flag);
        labInfoSizeMatrix.setVisible(flag);
        labInfoNumberWords.setVisible(flag);
        labInfoSizeMatrix.setVisible(flag);
    }

    public void setVisibleMenu(boolean flag)
    {
        butStart.setVisible(flag);
        labNameGame.setVisible(flag);
        labSizeMatrixWord.setVisible(flag);
        labMaxNumberWordsInMatrix.setVisible(flag);
        labComplexity.setVisible(flag);
        labPlayer.setVisible(flag);
        spinMaxNumberWordsInMatrix.setVisible(flag);
        spinSizeMatrixWord.setVisible(flag);
        rdButComputer.setVisible(flag);
        rdButHuman.setVisible(flag);
        rdButComplexityEasy.setVisible(flag);
        rdButComplexityHard.setVisible(flag);
    }

    public void setPositionElementInMenu()
    {
        butStart.setLayoutX(200);
        butStart.setLayoutY(200);
        labNameGame.setLayoutX(500);
        labNameGame.setLayoutY(200);
        Font font=new Font(50);
        labNameGame.setFont(font);
        spinMaxNumberWordsInMatrix.setLayoutX(500);
        spinSizeMatrixWord.setLayoutX(200);
        rdButHuman.setLayoutY(200);
        rdButComputer.setLayoutY(300);
        rdButComplexityEasy.setLayoutY(600);
        rdButComplexityHard.setLayoutY(800);
    }

    public void setPositionElementInGame()
    {
        butFinishGame.setLayoutX(200);
        butFinishGame.setLayoutY(700);
        butFinishStep.setLayoutX(900);
        txtArAllWords.setLayoutX(1000);
        txtArSecondPlayerWords.setLayoutY(300);
        txtArFirstPlayerWords.setLayoutY(500);
        txtArFirstPlayerWords.setLayoutX(800);
    }

    public void startInitialSettingsGame()
    {
        butFinishGame = new Button("Завершити гру");
        butFinishStep = new Button("Заершити хід");

        txtArAllWords = new TextArea("All");
        txtArFirstPlayerWords = new TextArea("First");
        txtArSecondPlayerWords = new TextArea("Second");

        lnDown = new Line(0,800,1400,800);
        lnRight = new Line(900,0,900,1000);
        lnUp = new Line(0,100,1400,100);

        labInfoComplexity = new Label();
        labInfoNumberWords = new Label();
        labInfoSizeMatrix = new Label();
        labTimer = new Label();
        labTitle = new Label();
        labScoreFirstPlayer = new Label();
        labScoreSecondPlayer = new Label();
        labTypeFirstPlayer = new Label();
        labTypeSecondPlayer = new Label();

        root.getChildren().add(labTypeFirstPlayer);
        root.getChildren().add(labTypeSecondPlayer);
        root.getChildren().add(labTimer);
        root.getChildren().add(labTitle);
        root.getChildren().add(labScoreFirstPlayer);
        root.getChildren().add(labScoreSecondPlayer);
        root.getChildren().add(labInfoSizeMatrix);
        root.getChildren().add(labInfoNumberWords);
        root.getChildren().add(labInfoComplexity);
        root.getChildren().add(txtArAllWords);
        root.getChildren().add(txtArFirstPlayerWords);
        root.getChildren().add(txtArSecondPlayerWords);
        root.getChildren().add(butFinishStep);
        root.getChildren().add(butFinishGame);
        root.getChildren().add(lnUp);
        root.getChildren().add(lnDown);
        root.getChildren().add(lnRight);
    }

    public void startInitialData()
    {
        root = new Pane();
        scene = new Scene(root, widthWindow, heightWindow);
        menuInfo = new MenuInfo();
    }

    public void startInitialSettingsMenu()
    {
        spinMaxNumberWordsInMatrix = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory
                        (minNumberWords, maxNumberWords, startValueWords);
        spinMaxNumberWordsInMatrix.setValueFactory(valueFactory);
        spinMaxNumberWordsInMatrix.setEditable(true);


        spinSizeMatrixWord = new Spinner<>();
        valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory
                        (minSize, maxSize, startValueSize);
        spinSizeMatrixWord.setValueFactory(valueFactory);
        spinSizeMatrixWord.setEditable(true);


        labSizeMatrixWord = new Label("Розмір поля");
        labMaxNumberWordsInMatrix = new Label("Кількість слів");


        rdButComputer = new RadioButton("Комп'ютер");
        rdButComputer.setSelected(true);
        rdButHuman = new RadioButton("Людина");
        rdButHuman.setSelected(false);


        rdButComplexityEasy = new RadioButton("Легко");
        rdButComplexityEasy.setSelected(true);
        rdButComplexityHard = new RadioButton("Важко");
        rdButComplexityHard.setSelected(false);


        labComplexity = new Label("Складність");
        labPlayer = new Label("Гравець");


        labNameGame = new Label("ГРА СЛОВА");
        butStart = new Button("Почати гру");


        root.getChildren().add(spinMaxNumberWordsInMatrix);
        root.getChildren().add(spinSizeMatrixWord);
        root.getChildren().add(labSizeMatrixWord);
        root.getChildren().add(labMaxNumberWordsInMatrix);
        root.getChildren().add(rdButComputer);
        root.getChildren().add(rdButHuman);
        root.getChildren().add(rdButComplexityEasy);
        root.getChildren().add(rdButComplexityHard);
        root.getChildren().add(labPlayer);
        root.getChildren().add(labComplexity);
        root.getChildren().add(butStart);
        root.getChildren().add(labNameGame);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
