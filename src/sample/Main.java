package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

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

    private Button[][] butMatrixWords;
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
    LogicController logicController;
    Computer computer;
    private int[][] matrixButtonsColor;
    private String defaultColor;
    private String []path={"one","two","three","four","five"};
    private final String activeColor = "-fx-background-color: #57df31";
    private int counter = 15;   // seconds
    private boolean flPlayer1 = true;
    private int scoreFirstPlayer = 0;
    private int scoreSecondPlayer = 0;
    private final long second = 1000000000;
    private final int timeEasyComplexity = 16;
    private final int timeHardComplexity = 11;
    private final int matrixPosX = 20;
    private final int matrixPosY = 70;
    private final int widthWindow = 1400;
    private final int heightWindow = 900;
    private final int minNumberWords = 1;
    private final int maxNumberWords = 100;
    private final int startValueWords = 5;
    private final int minSizeMatrix = 4;
    private final int maxSizeMatrix = 20;
    private final int startValueSizeMatrix = 10;

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

    protected AnimationTimer timerStep = new AnimationTimer() {
        private long lastUpdate = 0;

        @Override
        public void handle(long now) {

            if (now - lastUpdate >= second) {    // 1 sec
                counter--;
                labTimer.setText("Час : "+ counter +" с");
                if (counter == 0)
                {
                    if (flPlayer1)
                    {
                        flPlayer1 = false;
                        scoreFirstPlayer--;
                        setDefaultMatrixColor();
                    }
                    else
                    {
                        flPlayer1 = true;
                        scoreSecondPlayer--;
                        setDefaultMatrixColor();
                    }

                    if (menuInfo.getComplexity().equals("Легко"))
                        counter = timeEasyComplexity;
                    else
                        counter = timeHardComplexity;
                }
                lastUpdate = now;
            }

        }
    };

    public void setWordsInMatrixWords(ArrayList<Letter[]> codeWords)
    {
        for (int i = 0; i < codeWords.size(); i++)
        {
            for (int j = 0; j < codeWords.get(i).length; j++)
            {
                butMatrixWords[codeWords.get(i)[j].getY()][codeWords.get(i)[j].getX()].
                        setText(codeWords.get(i)[j].getLetter());
            }
        }
    }

    public void createGame()
    {
        buildMatrixButton(menuInfo.getSizeMatrix(),menuInfo.getSizeMatrix());
        setVisibleMenu(false);
        setVisibleGame(true);
        setInfoForGame();
        timerStep.start();
    }

    public void setInfoForGame()
    {
        labInfoComplexity.setText("Складність : "+menuInfo.getComplexity());
        labInfoNumberWords.setText("Слів : "+menuInfo.getNumberWords());
        labInfoSizeMatrix.setText("Розмір поля : "+menuInfo.getSizeMatrix()+
                "x"+menuInfo.getSizeMatrix());
        if (menuInfo.getPlayer().equals("Комп'ютер"))
        {
            labTypeFirstPlayer.setText("Гравець");
            labTypeSecondPlayer.setText(menuInfo.getPlayer());
        }
        else
        {
            labTypeFirstPlayer.setText("Гравець 1");
            labTypeSecondPlayer.setText("Гравець 2");
        }

        if (menuInfo.getComplexity().equals("Легко"))
            counter = timeEasyComplexity;
        else
            counter = timeHardComplexity;

        matrixButtonsColor = new int[menuInfo.getSizeMatrix()][menuInfo.getSizeMatrix()];

        logicController = new LogicController(matrixButtonsColor,
                menuInfo.getComplexity(),path,menuInfo.getNumberWords());
        logicController.loadRandomWords();
        ArrayList<Letter[]> codeWords = logicController.fillMatrixWords();
        setWordsInMatrixWords(codeWords);

    }

    public void buildMatrixButton(int length,int height)
    {
        butMatrixWords = new Button[height][length];
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < length; x++)
            {
                butMatrixWords[y][x] = new Button("B");
                root.getChildren().add(butMatrixWords[y][x]);
                butMatrixWords[y][x].setMaxSize(40,40);
                butMatrixWords[y][x].setMinSize(5,5);
                butMatrixWords[y][x].setLayoutX(x*30+matrixPosX);
                butMatrixWords[y][x].setLayoutY(y*30+matrixPosY);

                int finalX = x;
                int finalY = y;
                EventHandler<ActionEvent> event = e -> {
                    if (matrixButtonsColor[finalY][finalX] == 0)
                    {
                        butMatrixWords[finalY][finalX].setStyle(activeColor);
                        matrixButtonsColor[finalY][finalX] = 1;
                    }
                    else if (matrixButtonsColor[finalY][finalX] == 1)
                    {
                        butMatrixWords[finalY][finalX].setStyle(defaultColor);
                        matrixButtonsColor[finalY][finalX] = 0;
                    }
                };

                butMatrixWords[y][x].setOnAction(event);
            }
        }
    }

    public void setDefaultMatrixColor()
    {
        matrixButtonsColor = new int[menuInfo.getSizeMatrix()][menuInfo.getSizeMatrix()];
        for(int y = 0; y < butMatrixWords.length; y++)
        {
            for(int x = 0; x < butMatrixWords[y].length; x++)
                butMatrixWords[y][x].setStyle(defaultColor);
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
            timerStep.stop();
            removeMatrixWords();
            setVisibleGame(false);
            setVisibleMenu(true);
        });
        butFinishStep.setOnAction(e -> {
            logicController.setMatrixButtonsColor(matrixButtonsColor);
            if (logicController.checkStep()&& flPlayer1)
                scoreFirstPlayer+=2;// first player find word
            else if (!logicController.checkStep() && flPlayer1)
                scoreFirstPlayer--;// first player don t find word
            else if (logicController.checkStep() && !flPlayer1)
                scoreSecondPlayer+=2;// second player find word
            else if (!logicController.checkStep() && !flPlayer1)
                scoreSecondPlayer--; // second player don t find word

            labScoreFirstPlayer.setText(String.valueOf(scoreFirstPlayer));
            labScoreSecondPlayer.setText(String.valueOf(scoreSecondPlayer));
            flPlayer1 = !flPlayer1;

            setDefaultMatrixColor();

            if (menuInfo.getComplexity().equals("Легко"))
                counter = timeEasyComplexity;
            else
                counter = timeHardComplexity;
        });
    }

    public void removeMatrixWords()
    {
        for (int i = 0; i < butMatrixWords.length; i++)
        {
            for (int j = 0; j < butMatrixWords[i].length; j++)
                root.getChildren().remove(butMatrixWords[i][j]);
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
        labInfoComplexity.setVisible(flag);
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
        labTimer.setLayoutY(500);
        labTimer.setLayoutX(800);
    }

    public void startInitialSettingsGame()
    {
        butFinishGame = new Button("Завершити гру");
        butFinishStep = new Button("Завершити хід");

        txtArAllWords = new TextArea("All");//down on model
        txtArFirstPlayerWords = new TextArea("First");//left on model
        txtArSecondPlayerWords = new TextArea("Second");//right on model
        //--------Size text area-----------------

        //---------------------------------------

        lnDown = new Line(0,800,1400,800);
        lnRight = new Line(900,0,900,1000);
        lnUp = new Line(0,100,1400,100);

        labInfoComplexity = new Label();
        labInfoNumberWords = new Label();
        labInfoSizeMatrix = new Label();
        labTimer = new Label("Час 00:15");
        labTitle = new Label("Рахунок та слова");
        labScoreFirstPlayer = new Label("0");
        labScoreSecondPlayer = new Label("0");
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
                        (minSizeMatrix, maxSizeMatrix, startValueSizeMatrix);
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
        defaultColor = butStart.getStyle();


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
