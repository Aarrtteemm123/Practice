package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

//1.Зробити меню виграшу
//2.Зробити базу слів
public class Main extends Application {
//-----------------Main-------------------------//

    private Scene scene;
    private Pane root;

//-----------------MainMenu------------------------//

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
    private Button butClear;
    private TextArea txtArAllWords;
    private TextArea txtArFirstPlayerWords;
    private TextArea txtArSecondPlayerWords;
    private Label labInfoWord;
    private Label labSelectWord;
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

//-----------------EndMenu----------------------//

    private Button butExit;
    private Label labResult;
    private Label labResultFirstPlayer;
    private Label labResultSecondPlayer;
    private Label labNameFirstPlayer;
    private Label labNameSecondPlayer;

//----------------------------------------------//

//----------------Data--------------------------//

    private MenuInfo menuInfo;
    private LogicController logicController;
    private Computer computer;
    private ArrayList<Point>trackButtons;
    private String defaultColor;
    private String []path={"Анатомія","Їжа","Тварини","Спорт","Географія","Транспорт",
            "Наука","Економіка","Армія","Мистецтво"};
    private final String alphabet="АБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯ";
    private final String activeColor1 = "-fx-background-color: #57df31";
    private final String activeColor2 = "-fx-background-color: #567dff";
    private final String activeColor3 = "-fx-background-color: #efc430";
    private final String activeColor4 = "-fx-background-color: #fd5436";
    private int counter = 15;   // seconds
    private int pause = 0;
    private boolean flPlayer1 = true;
    private int scoreFirstPlayer = 0;
    private int scoreSecondPlayer = 0;
    private final long second = 1000000000;
    private final int timeEasyComplexity = 16;
    private final int timeHardComplexity = 11;
    private final int matrixPosX = 140;
    private final int matrixPosY = 110;
    private final int widthWindow = 1400;
    private final int heightWindow = 900;
    private final int minNumberWords = 1;
    private final int maxNumberWords = 100;
    private final int startValueWords = 5;
    private final int minSizeMatrix = 4;
    private final int maxSizeMatrix = 20;
    private final int startValueSizeMatrix = 15;

//----------------------------------------------//

    @Override
    public void start(Stage primaryStage) {
        startInitialData();
        startInitialSettingsMenu();
        startInitialSettingsGame();
        startInitialSettingsEndMenu();
        setVisibleGame(false);
        setVisibleEndMenu(false);
        setPositionElementInMenu();
        setPositionElementInGame();
        setPositionElementInEndMenu();
        checkEventMenu();
        checkEventGame();

        primaryStage.setTitle("Гра слова");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    protected AnimationTimer animationStepComputer = new AnimationTimer() {
        private long lastUpdate = 0;

        @Override
        public void handle(long now) {

            if (now - lastUpdate >= (second/1000)*pause) {    // 1 sec

                if (logicController.getVocabulary().size()!=0)
                    pause = computer.nextStep(logicController.getCodeWords(),butMatrixWords);

                if (computer.isFlFinishStep())
                {
                    computer.finishStep();
                    butFinishStep.setDisable(false);
                    butFinishStep.fire();
                }

                lastUpdate = now;
            }

        }
    };

    protected AnimationTimer timerStep = new AnimationTimer() {
        private long lastUpdate = 0;

        @Override
        public void handle(long now) {

            if (now - lastUpdate >= second) {    // 1 sec
                counter--;
                labTimer.setText("Час : "+ counter +" с");
                if (counter == 0)
                {
                    labSelectWord.setText("");
                    butFinishStep.fire();
                }
                lastUpdate = now;
            }

        }
    };

    public void clearMatrixWords()
    {
        for (int i = 0; i < butMatrixWords.length; i++) {
            for (int j = 0; j < butMatrixWords.length; j++) {
                butMatrixWords[i][j].
                        setText("-");
            }
        }
    }

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

        Random random = new Random();

        for (int i = 0; i < menuInfo.getSizeMatrix(); i++)
        {
            for (int j = 0; j < menuInfo.getSizeMatrix(); j++)
            {
                if (butMatrixWords[i][j].getText().equals("-"))
                {
                    butMatrixWords[i][j].setText(String.valueOf(alphabet.charAt
                            (random.nextInt(alphabet.length()))));
                }
            }
        }

    }

    public void createGame() throws IOException, ClassNotFoundException, InterruptedException {
        buildMatrixButton(menuInfo.getSizeMatrix(),menuInfo.getSizeMatrix());
        setVisibleMenu(false);
        setVisibleGame(true);
        setInfoForGame();
        timerStep.start();
    }

    public void setInfoForGame() throws IOException, ClassNotFoundException {
        labInfoComplexity.setText("Складність : "+menuInfo.getComplexity());
        labInfoSizeMatrix.setText("Розмір поля : "+menuInfo.getSizeMatrix()+
                "x"+menuInfo.getSizeMatrix());
        if (menuInfo.getPlayer().equals("Комп'ютер"))
        {
            labTypeFirstPlayer.setText("Гравець");
            labTypeSecondPlayer.setText(menuInfo.getPlayer());
            computer = new Computer(menuInfo.getComplexity());
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

        labTypeFirstPlayer.setStyle("-fx-font-weight: bold");
        trackButtons = new ArrayList<>();
        labSelectWord.setText("");

        logicController = new LogicController(path,menuInfo.getNumberWords());
        ArrayList<Letter[]> codeWords = new ArrayList<>();
        while (!logicController.isFlFinish())
        {
            logicController.loadRandomWords();
            codeWords = logicController.fillMatrixWords(
                    menuInfo.getSizeMatrix(),4);
        }
        labInfoNumberWords.setText("Слів : "+logicController.getRealNumberWords());
        setWordsInTxtArAllWords(logicController.getVocabulary());
        clearMatrixWords();
        setWordsInMatrixWords(codeWords);
    }

    public void setWordsInTxtArAllWords(ArrayList<String> words)
    {
        txtArAllWords.setText("");
        for (int i = 0; i < words.size(); i++)
        {
            if (i==words.size()-1)
                txtArAllWords.setText(txtArAllWords.getText()+words.get(i));
            else if (i%7==0&&i!=0)
                txtArAllWords.setText(txtArAllWords.getText()+"\n");
            else txtArAllWords.setText(txtArAllWords.getText()+words.get(i)+",");
        }
    }

    public void buildMatrixButton(int length,int height) throws InterruptedException {
        butMatrixWords = new Button[height][length];
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < length; x++)
            {
                butMatrixWords[y][x] = new Button("-");
                root.getChildren().add(butMatrixWords[y][x]);
                butMatrixWords[y][x].setMaxSize(40,40);
                butMatrixWords[y][x].setMinSize(30,30);
                butMatrixWords[y][x].setLayoutX(x*30+matrixPosX);
                butMatrixWords[y][x].setLayoutY(y*30+matrixPosY);

                int finalX = x;
                int finalY = y;
                EventHandler<ActionEvent> event = e -> {
                    if (butMatrixWords[finalY][finalX].getStyle().equals(defaultColor))
                    {
                        butMatrixWords[finalY][finalX].setStyle(activeColor1);
                        labSelectWord.setText(labSelectWord.getText()+
                                butMatrixWords[finalY][finalX].getText());
                        trackButtons.add(new Point(finalX,finalY));
                    }
                    else if (butMatrixWords[finalY][finalX].getStyle().equals(activeColor1))
                    {
                        butMatrixWords[finalY][finalX].setStyle(activeColor2);
                        labSelectWord.setText(labSelectWord.getText()+
                                butMatrixWords[finalY][finalX].getText());
                        trackButtons.add(new Point(finalX,finalY));
                    }
                    else if (butMatrixWords[finalY][finalX].getStyle().equals(activeColor2))
                    {
                        butMatrixWords[finalY][finalX].setStyle(activeColor3);
                        labSelectWord.setText(labSelectWord.getText()+
                                butMatrixWords[finalY][finalX].getText());
                        trackButtons.add(new Point(finalX,finalY));
                    }
                    else if (butMatrixWords[finalY][finalX].getStyle().equals(activeColor3))
                    {
                        butMatrixWords[finalY][finalX].setStyle(activeColor4);
                        labSelectWord.setText(labSelectWord.getText()+
                                butMatrixWords[finalY][finalX].getText());
                        trackButtons.add(new Point(finalX,finalY));
                    }
                    else if (butMatrixWords[finalY][finalX].getStyle().equals(activeColor4))
                    {
                        butMatrixWords[finalY][finalX].setStyle(defaultColor);
                        for (int i = 0; i < trackButtons.size(); i++)
                        {
                            int bufferSize = trackButtons.size();
                            trackButtons.remove(new Point(finalX,finalY));
                            if (trackButtons.size()!=bufferSize)
                                i=0;

                        }
                        trackButtons.remove(new Point(finalX,finalY));
                        labSelectWord.setText("");
                        for (int i = 0; i < trackButtons.size(); i++)
                            labSelectWord.setText(labSelectWord.getText()+
                                    butMatrixWords[trackButtons.get(i).y]
                                            [trackButtons.get(i).x].getText());
                    }
                };
                butMatrixWords[y][x].setOnAction(event);
            }
        }
    }

    public void setDefaultMatrixColor()
    {
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
            scoreFirstPlayer = 0;
            scoreSecondPlayer = 0;
            labScoreFirstPlayer.setText(String.valueOf(scoreFirstPlayer));
            labScoreSecondPlayer.setText(String.valueOf(scoreSecondPlayer));
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

            try {
                createGame();
            } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });

    }

    public void checkEventGame()
    {
        butClear.setOnAction(e -> {
            setDefaultMatrixColor();
            labSelectWord.setText("");
        });

        butExit.setOnAction(e -> {
            setVisibleEndMenu(false);
            butFinishGame.fire();
        });

        butFinishGame.setOnAction(e -> {
            timerStep.stop();
            animationStepComputer.stop();
            labSelectWord.setText("");
            labTypeSecondPlayer.setStyle("-fx-font-weight: normal");
            txtArFirstPlayerWords.setText("");
            txtArSecondPlayerWords.setText("");
            txtArAllWords.setText("");
            flPlayer1 = true;
            butClear.setDisable(false);
            butFinishStep.setDisable(false);
            trackButtons = new ArrayList<>();
            removeMatrixWords();
            setVisibleGame(false);
            setVisibleMenu(true);
        });
        butFinishStep.setOnAction(e -> {
            if (flPlayer1&&logicController.checkStep(trackButtons,butMatrixWords))
            {
                scoreFirstPlayer+=2;// first player find word
                txtArFirstPlayerWords.setText(txtArFirstPlayerWords.getText()
                        +logicController.getSelectWord()+"\n");
                setWordsInTxtArAllWords(logicController.getVocabulary());
            }
            else if (flPlayer1&&!logicController.checkStep(trackButtons,butMatrixWords))
                scoreFirstPlayer--;// first player don t find word
            else if (!flPlayer1&&logicController.checkStep(trackButtons,butMatrixWords))
            {
                scoreSecondPlayer+=2;// second player find word
                txtArSecondPlayerWords.setText(txtArSecondPlayerWords.getText()
                        +logicController.getSelectWord()+"\n");
                setWordsInTxtArAllWords(logicController.getVocabulary());
            }
            else if (!flPlayer1&&!logicController.checkStep(trackButtons,butMatrixWords))
                scoreSecondPlayer--; // second player don t find word

            labScoreFirstPlayer.setText(String.valueOf(scoreFirstPlayer));
            labScoreSecondPlayer.setText(String.valueOf(scoreSecondPlayer));
            labSelectWord.setText("");
            trackButtons = new ArrayList<>();
            if (flPlayer1)
            {
                labTypeFirstPlayer.setStyle("-fx-font-weight: normal");
                labTypeSecondPlayer.setStyle("-fx-font-weight: bold");
            }
            else
            {
                labTypeSecondPlayer.setStyle("-fx-font-weight: normal");
                labTypeFirstPlayer.setStyle("-fx-font-weight: bold");
            }

            setDefaultMatrixColor();

            if (logicController.getVocabulary().size()==0)
            {
                setResultGame();
                setVisibleGame(false);
                for (int i = 0; i < butMatrixWords.length; i++) {
                    for (int j = 0; j < butMatrixWords[i].length; j++) {
                        butMatrixWords[i][j].setVisible(false);
                    }
                }
                setVisibleEndMenu(true);
            }

            if (menuInfo.getComplexity().equals("Легко"))
                counter = timeEasyComplexity;
            else
                counter = timeHardComplexity;

            if (menuInfo.getPlayer().equals("Комп'ютер")&& !flPlayer1)
                animationStepComputer.stop();
            else if (menuInfo.getPlayer().equals("Комп'ютер")&& flPlayer1)
                animationStepComputer.start();

            flPlayer1 = !flPlayer1;
            if (!flPlayer1&&menuInfo.getPlayer().equals("Комп'ютер"))
            {
                butClear.setDisable(true);
                butFinishStep.setDisable(true);
                setDisableMatrix(true);
            }
            else
            {
                butClear.setDisable(false);
                butFinishStep.setDisable(false);
                setDisableMatrix(false);
            }

        });
    }

    public void setDisableMatrix(boolean flag)
    {
        for (int i = 0; i < butMatrixWords.length; i++) {
            for (int j = 0; j < butMatrixWords[i].length; j++) {
                butMatrixWords[i][j].setDisable(flag);
            }
        }
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
        butClear.setVisible(flag);
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
        labInfoWord.setVisible(flag);
        labSelectWord.setVisible(flag);
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
        labNameGame.setLayoutX(490);
        labNameGame.setLayoutY(92);

        labSizeMatrixWord.setLayoutX(232);
        labSizeMatrixWord.setLayoutY(269);

        labMaxNumberWordsInMatrix.setLayoutX(231);
        labMaxNumberWordsInMatrix.setLayoutY(348);

        labComplexity.setLayoutX(233);
        labComplexity.setLayoutY(430);

        labPlayer.setLayoutX(237);
        labPlayer.setLayoutY(508);

        spinSizeMatrixWord.setLayoutX(551);
        spinSizeMatrixWord.setLayoutY(272);

        spinMaxNumberWordsInMatrix.setLayoutX(551);
        spinMaxNumberWordsInMatrix.setLayoutY(349);

        butStart.setLayoutX(606);
        butStart.setLayoutY(657);

        rdButHuman.setLayoutX(559);
        rdButHuman.setLayoutY(515);

        rdButComputer.setLayoutX(721);
        rdButComputer.setLayoutY(515);

        rdButComplexityEasy.setLayoutX(558);
        rdButComplexityEasy.setLayoutY(435);

        rdButComplexityHard.setLayoutX(721);
        rdButComplexityHard.setLayoutY(434);
    }

    public void setPositionElementInGame()
    {
        butClear.setLayoutX(490);
        butClear.setLayoutY(732);

        txtArAllWords.setLayoutX(918);
        txtArAllWords.setLayoutY(595);

        txtArFirstPlayerWords.setLayoutX(918);
        txtArFirstPlayerWords.setLayoutY(170);

        txtArSecondPlayerWords.setLayoutX(1180);
        txtArSecondPlayerWords.setLayoutY(170);

        labTypeFirstPlayer.setLayoutX(980);
        labTypeFirstPlayer.setLayoutY(86);

        labTypeSecondPlayer.setLayoutX(1245);
        labTypeSecondPlayer.setLayoutY(86);

        labScoreFirstPlayer.setLayoutX(1005);
        labScoreFirstPlayer.setLayoutY(130);

        labScoreSecondPlayer.setLayoutX(1275);
        labScoreSecondPlayer.setLayoutY(130);

        labTitle.setLayoutX(1100);
        labTitle.setLayoutY(20);

        labInfoSizeMatrix.setLayoutX(935);
        labInfoSizeMatrix.setLayoutY(810);

        labInfoNumberWords.setLayoutX(935);
        labInfoNumberWords.setLayoutY(840);

        labInfoComplexity.setLayoutX(935);
        labInfoComplexity.setLayoutY(870);

        labInfoWord.setLayoutX(418);
        labInfoWord.setLayoutY(70);

        labTimer.setLayoutX(418);
        labTimer.setLayoutY(21);

        labSelectWord.setLayoutX(478);
        labSelectWord.setLayoutY(71);

        butFinishGame.setLayoutX(400);
        butFinishGame.setLayoutY(822);

        butFinishStep.setLayoutX(304);
        butFinishStep.setLayoutY(732);
    }
    public void setResultGame()
    {
        labResult.setLayoutX(530);
        labNameFirstPlayer.setText(labTypeFirstPlayer.getText());
        labNameSecondPlayer.setText(labTypeSecondPlayer.getText());
        labResultFirstPlayer.setText(labScoreFirstPlayer.getText());
        labResultSecondPlayer.setText(labScoreSecondPlayer.getText());
        if (scoreFirstPlayer>scoreSecondPlayer)
        {
            labResult.setText("Переміг "+labTypeFirstPlayer.getText()+" !");
            labResult.setLayoutX(540);
        }
        if (scoreFirstPlayer<scoreSecondPlayer)
            labResult.setText("Переміг "+labTypeSecondPlayer.getText()+" !");
        if (scoreFirstPlayer==scoreSecondPlayer)
        {
            labResult.setText("Нічия!");
            labResult.setLayoutX(670);
        }
    }

    public void setVisibleEndMenu(boolean flag)
    {
        labNameFirstPlayer.setVisible(flag);
        labNameSecondPlayer.setVisible(flag);
        labResultFirstPlayer.setVisible(flag);
        labResultSecondPlayer.setVisible(flag);
        labResult.setVisible(flag);
        butExit.setVisible(flag);
    }

    public void setPositionElementInEndMenu()
    {
        labNameFirstPlayer.setLayoutX(519);
        labNameFirstPlayer.setLayoutY(79);

        labNameSecondPlayer.setLayoutX(760);
        labNameSecondPlayer.setLayoutY(81);

        labResultFirstPlayer.setLayoutX(568);
        labResultFirstPlayer.setLayoutY(161);

        labResultSecondPlayer.setLayoutX(825);
        labResultSecondPlayer.setLayoutY(162);

        labResult.setLayoutX(530);
        labResult.setLayoutY(385);

        butExit.setLayoutX(600);
        butExit.setLayoutY(704);
    }

    public void startInitialSettingsEndMenu()
    {
        Font font = new Font(30);
        labNameFirstPlayer = new Label();
        labNameFirstPlayer.setFont(font);
        labNameSecondPlayer = new Label();
        labNameSecondPlayer.setFont(font);
        labResultFirstPlayer = new Label();
        labResultFirstPlayer.setFont(font);
        labResultSecondPlayer = new Label();
        labResultSecondPlayer.setFont(font);
        labResult = new Label("");
        labResult.setFont(new Font(40));
        butExit = new Button("Вихід");
        butExit.setFont(new Font(25));
        butExit.setPrefSize(232,83);

        root.getChildren().add(labNameFirstPlayer);
        root.getChildren().add(labNameSecondPlayer);
        root.getChildren().add(labResultFirstPlayer);
        root.getChildren().add(labResultSecondPlayer);
        root.getChildren().add(labResult);
        root.getChildren().add(butExit);
    }

    public void startInitialSettingsGame()
    {
        butFinishGame = new Button("Завершити гру");
        butFinishStep = new Button("Завершити хід");
        butClear = new Button("Очистити");
        butFinishGame.setPrefSize(126,44);
        butFinishStep.setPrefSize(126,44);
        butClear.setPrefSize(126,44);

        txtArAllWords = new TextArea();//down on model
        txtArAllWords.setEditable(false);
        txtArFirstPlayerWords = new TextArea();//left on model
        txtArFirstPlayerWords.setEditable(false);
        txtArSecondPlayerWords = new TextArea();//right on model
        txtArSecondPlayerWords.setEditable(false);

        txtArAllWords.setPrefSize(450,186);
        txtArFirstPlayerWords.setPrefSize(187,400);
        txtArSecondPlayerWords.setPrefSize(187,400);

        lnDown = new Line(0,795,1400,795);
        lnRight = new Line(890,0,890,900);
        lnUp = new Line(0,60,1400,60);

        Font font = new Font(15);
        labInfoComplexity = new Label();
        labInfoComplexity.setFont(font);
        labInfoNumberWords = new Label();
        labInfoNumberWords.setFont(font);
        labInfoSizeMatrix = new Label();
        labInfoSizeMatrix.setFont(font);

        labTimer = new Label("Час 00:15");
        labTitle = new Label("Рахунок та слова");
        labScoreFirstPlayer = new Label("0");
        labScoreSecondPlayer = new Label("0");
        labTypeFirstPlayer = new Label();
        labTypeSecondPlayer = new Label();
        labInfoWord = new Label("Слово: ");
        labSelectWord = new Label();

        root.getChildren().add(labInfoWord);
        root.getChildren().add(butClear);
        root.getChildren().add(labSelectWord);
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
        spinMaxNumberWordsInMatrix.setPrefSize(294,40);


        spinSizeMatrixWord = new Spinner<>();
        valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory
                        (minSizeMatrix, maxSizeMatrix, startValueSizeMatrix);
        spinSizeMatrixWord.setValueFactory(valueFactory);
        spinSizeMatrixWord.setEditable(true);
        spinSizeMatrixWord.setPrefSize(294,40);

        Font font = new Font(30);
        labSizeMatrixWord = new Label("Розмір поля");
        labSizeMatrixWord.setFont(font);
        labMaxNumberWordsInMatrix = new Label("Кількість слів");
        labMaxNumberWordsInMatrix.setFont(font);
        labComplexity = new Label("Складність");
        labComplexity.setFont(font);
        labPlayer = new Label("Гравець");
        labPlayer.setFont(font);

        font = new Font(50);


        labNameGame = new Label("ГРА «ПОШУК СЛІВ»");
        labNameGame.setFont(font);

        font = new Font(20);

        rdButComputer = new RadioButton("Комп'ютер");
        rdButComputer.setSelected(true);
        rdButComputer.setFont(font);
        rdButHuman = new RadioButton("Людина");
        rdButHuman.setSelected(false);
        rdButHuman.setFont(font);


        rdButComplexityEasy = new RadioButton("Легко");
        rdButComplexityEasy.setSelected(true);
        rdButComplexityEasy.setFont(font);
        rdButComplexityHard = new RadioButton("Важко");
        rdButComplexityHard.setSelected(false);
        rdButComplexityHard.setFont(font);


        butStart = new Button("Почати гру");
        defaultColor = butStart.getStyle();
        butStart.setFont(font);
        butStart.setPrefSize(137,40);


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
