package sample;

import javafx.scene.control.Button;

import java.awt.*;
import java.io.*;
import java.util.*;

public class LogicController {
    private ArrayList<String> vocabulary;
    private ArrayList<Letter[]> codeWords;
    private Letter[][] matrixLetter;
    private String[] path;
    private String selectWord;
    private int numberWords;
    private int realNumberWords;
    private boolean flFinish = false;

    public ArrayList<Letter[]> fillMatrixWords(int sizeMatrix,int maxLayer) throws IOException, ClassNotFoundException {
        // hard logic
        if (maxLayer<1)
            maxLayer = 1;

        Random rand = new Random();
        codeWords = new ArrayList<>();
        selectionWords(sizeMatrix);
        createMatrixLetter(sizeMatrix);
        flFinish = false;
        final int size = 8;
        final double[] vectorMoveArr = createProbabilityArr(size);
        final boolean[] flVectorMoveArr = new boolean[size];
        int [][]matrixLayer = new int[sizeMatrix][sizeMatrix];
        for (int i = 0; i < vocabulary.size(); i++)
        {
            int x, y;
            String bufferWord = vocabulary.get(i);
            Letter[] word = new Letter[bufferWord.length()];
            ArrayList<Move> blockStep = new ArrayList<>();
            int []stepArr = new int[bufferWord.length()];
            stepArr[0] = -1;//first letter don't move
            int indexChar = 0;
            int indexMove = 0;
            double random;
            boolean flMoveControl = false;
            int counterFlMoveControl = 0;

            x = rand.nextInt(sizeMatrix);
            y = rand.nextInt(sizeMatrix);
            while (!checkPlace(x, y, indexMove, blockStep, String.valueOf(
                    bufferWord.charAt(indexChar)), indexChar, matrixLayer, maxLayer))
            {
                x = rand.nextInt(sizeMatrix);
                y = rand.nextInt(sizeMatrix);
            }
            matrixLetter[y][x].setLetter(String.valueOf(bufferWord.
                    charAt(indexChar)));
            matrixLayer[y][x]++;
            word[indexChar] = new Letter(
                    String.valueOf(bufferWord.charAt(indexChar)), x, y);
            indexChar++;

            while (indexChar < bufferWord.length())
            {
                if (counterFlMoveControl>10000)
                    return null;
                if (flMoveControl && checkMoveArr(flVectorMoveArr))
                {
                    // step back
                    indexChar--;
                    if (indexChar == 0) {
                        //System.out.println("Error");
                        if (matrixLayer[y][x] == 1) {
                            matrixLetter[y][x].setLetter("-");
                        }
                        matrixLayer[y][x]--;
                        do {
                            x = rand.nextInt(sizeMatrix);
                            y = rand.nextInt(sizeMatrix);
                        } while (matrixLayer[y][x] != 0);
                        blockStep = new ArrayList<>();
                        Arrays.fill(flVectorMoveArr, false);
                        matrixLetter[y][x].setLetter(String.valueOf(bufferWord.
                                charAt(indexChar)));
                        word[indexChar] = new Letter(
                                String.valueOf(bufferWord.charAt(indexChar)), x, y);
                        matrixLayer[y][x]++;
                        indexChar++;

                    } else {
                        //System.out.println("Step back");
                        Arrays.fill(flVectorMoveArr, false);
                        blockStep.add(new Move(String.valueOf(
                                bufferWord.charAt(indexChar)), indexChar,
                                backRewriteX(x, stepArr[indexChar]),
                                backRewriteY(y, stepArr[indexChar]), x, y));
                        if (matrixLayer[y][x] == 1)
                            matrixLetter[y][x].setLetter("-");
                        matrixLayer[y][x]--;
                        y = backRewriteY(y, stepArr[indexChar]);
                        x = backRewriteX(x, stepArr[indexChar]);

                    }
                }
                random = Math.random();
                indexMove = getIndexMove(vectorMoveArr, random);
                flVectorMoveArr[indexMove] = true;
                flMoveControl = true;
                if (checkPlace(x, y, indexMove, blockStep, String.valueOf(
                        bufferWord.charAt(indexChar)), indexChar, matrixLayer, maxLayer))
                {
                    x = rewriteX(x, indexMove);
                    y = rewriteY(y, indexMove);
                    matrixLayer[y][x]++;
                    matrixLetter[y][x].setLetter(String.valueOf(bufferWord.
                            charAt(indexChar)));
                    word[indexChar] = new Letter(
                            String.valueOf(bufferWord.charAt(indexChar)), x, y);
                    Arrays.fill(flVectorMoveArr, false);
                    stepArr[indexChar] = indexMove;
                    indexChar++;
                }
                counterFlMoveControl++;
            }
            codeWords.add(word);
        }
        flFinish = true;
        return codeWords;
    }

    private void selectionWords(int sizeMatrix) throws IOException, ClassNotFoundException {
        int counterChar = 0;
        for (int i = 0; i < vocabulary.size(); i++)
            counterChar+=vocabulary.get(i).length();
        if (counterChar>sizeMatrix*sizeMatrix)
        {
            ArrayList<String> allWords = getAllWords();
            Comparator<String> comparator = Comparator.comparing(String::length);
            allWords.sort(comparator);
            vocabulary.sort(comparator);
            int index = 1;
            for (int i = 0; i < vocabulary.size(); i++)
            {
                if (!vocabulary.contains(allWords.get(i)))
                {
                    counterChar-=vocabulary.get(vocabulary.size()-i-1).length()-
                            allWords.get(i).length();
                    vocabulary.set(vocabulary.size()-index,allWords.get(i));
                    index++;
                }
                if (counterChar<=sizeMatrix*sizeMatrix)
                    i=vocabulary.size();
            }
            vocabulary.sort(comparator);
            for (int i = 0; i < vocabulary.size(); i++)
            {
                if (counterChar>sizeMatrix*sizeMatrix)
                {
                    counterChar-=vocabulary.get(vocabulary.size()-1).length();
                    vocabulary.remove(vocabulary.size()-1);
                    i=0;
                }
            }
        }
        realNumberWords = vocabulary.size();
    }

    private ArrayList<String> getAllWords() throws IOException, ClassNotFoundException {
        ArrayList<String> allWords = new ArrayList<>();
        ArrayList<String> buffer;

        for (int i = 0; i < path.length; i++)
        {
            FileInputStream fis = new FileInputStream(path[i]);
            ObjectInputStream read = new ObjectInputStream (fis);
            buffer = (ArrayList) read.readObject();
            read.close();
            fis.close();
            allWords.addAll(buffer);
        }

        return allWords;
    }


    private boolean checkMoveArr(boolean arr[])
    {
        for (int i = 0; i < arr.length; i++)
        {
            if (!arr[i])
                return false;
        }
        return true;
    }

    private int rewriteX(int x,int indexMoveArr)
    {
        if (indexMoveArr==1||indexMoveArr==2||indexMoveArr==3)
            return x+1;
        if (indexMoveArr==5||indexMoveArr==6||indexMoveArr==7)
            return x-1;
        return x;
    }

    private int rewriteY(int y,int indexMoveArr)
    {
        if (indexMoveArr==3||indexMoveArr==4||indexMoveArr==5)
            return y+1;
        if (indexMoveArr==0||indexMoveArr==1||indexMoveArr==7)
            return y-1;
        return y;
    }

    private int backRewriteX(int x,int indexMoveArr)
    {
        if (indexMoveArr==1||indexMoveArr==2||indexMoveArr==3)
            return x-1;
        if (indexMoveArr==5||indexMoveArr==6||indexMoveArr==7)
            return x+1;
        return x;
    }

    private int backRewriteY(int y,int indexMoveArr)
    {
        if (indexMoveArr==3||indexMoveArr==4||indexMoveArr==5)
            return y-1;
        if (indexMoveArr==0||indexMoveArr==1||indexMoveArr==7)
            return y+1;
        return y;
    }

    private boolean checkPlace(int x, int y, int indexMoveArr, ArrayList<Move>blockSteps, String letter, int indexLetter,int[][] matrixLayer,int maxLayer)
    {
        int size = matrixLetter.length;
        if (indexLetter == 0)
            return matrixLayer[y][x] < maxLayer && (matrixLetter[y][x].getLetter().equals("-") || matrixLetter[y][x].getLetter().equals(letter));
        for (int i = 0; i < blockSteps.size(); i++)
        {
            if (blockSteps.get(i).getIndexLetter()==indexLetter)
            {
                if (blockSteps.get(i).getLetter().equals(letter)
                        &&blockSteps.get(i).getX1()==x
                        &&blockSteps.get(i).getY1()==y
                        &&blockSteps.get(i).getX2()==rewriteX(x,indexMoveArr)
                        &&blockSteps.get(i).getY2()==rewriteY(y,indexMoveArr))
                    return false;
            }
        }
        if (x >= size || x < 0 || y >= size || y < 0)
            return false;
        if (indexMoveArr == 0)
            return y - 1 > -1 && matrixLayer[y - 1][x] < maxLayer && (matrixLetter[y - 1][x].getLetter().equals("-") || matrixLetter[y - 1][x].getLetter().equals(letter));
        if (indexMoveArr == 1)
            return y - 1 > -1 && x + 1 < size && matrixLayer[y - 1][x + 1] < maxLayer && (matrixLetter[y - 1][x + 1].getLetter().equals("-") || matrixLetter[y - 1][x + 1].getLetter().equals(letter));
        if (indexMoveArr == 2)
            return x + 1 < size && matrixLayer[y][x + 1] < maxLayer && (matrixLetter[y][x + 1].getLetter().equals("-") || matrixLetter[y][x + 1].getLetter().equals(letter));
        if (indexMoveArr == 3)
            return y + 1 < size && x + 1 < size && matrixLayer[y + 1][x + 1] < maxLayer && (matrixLetter[y + 1][x + 1].getLetter().equals("-") || matrixLetter[y + 1][x + 1].getLetter().equals(letter));
        if (indexMoveArr == 4)
            return y + 1 < size && matrixLayer[y + 1][x] < maxLayer && (matrixLetter[y + 1][x].getLetter().equals("-") || matrixLetter[y + 1][x].getLetter().equals(letter));
        if (indexMoveArr == 5)
            return y + 1 < size && x - 1 > -1 && matrixLayer[y + 1][x - 1] < maxLayer && (matrixLetter[y + 1][x - 1].getLetter().equals("-") || matrixLetter[y + 1][x - 1].getLetter().equals(letter));
        if (indexMoveArr == 6)
            return x - 1 > -1 && matrixLayer[y][x - 1] < maxLayer && (matrixLetter[y][x - 1].getLetter().equals("-") || matrixLetter[y][x - 1].getLetter().equals(letter));
        if (indexMoveArr == 7)
            return (y - 1) > -1 && (x - 1) > -1 && matrixLayer[y - 1][x - 1] < maxLayer && (matrixLetter[y - 1][x - 1].getLetter().equals("-") || matrixLetter[y - 1][x - 1].getLetter().equals(letter));
        return false;
    }

    private int getIndexMove(double[] arr, double value)
    {
        for (int i = 0; i < arr.length-1; i++)
        {
            if (value>arr[i]&&value<arr[i+1])
                return i;
        }
        return arr.length-1;
    }

    private double[] createProbabilityArr(int size)
    {
        double []probabilityArr = new double[size];
        double delta = 1/(double)size;
        probabilityArr[0]=delta;
        for (int i = 1; i < probabilityArr.length; i++)
            probabilityArr[i]=probabilityArr[i-1]+delta;
        return probabilityArr;
    }

    private void createMatrixLetter(int sizeMatrix) {
        matrixLetter = new Letter[sizeMatrix][sizeMatrix];
        for (int i = 0; i < sizeMatrix; i++) {
            for (int j = 0; j < sizeMatrix; j++)
                matrixLetter[i][j] = new Letter("-", j, i);
        }
    }

    public boolean checkStep(ArrayList<Point>trackButtons, Button[][]matrix) {
        int counterLetters;
        StringBuilder word;
        for (int i = 0; i < codeWords.size(); i++)
        {
            counterLetters = 0;
            word = new StringBuilder();
            for (int j = 0; j < trackButtons.size(); j++)
            {
                if (trackButtons.size()==codeWords.get(i).length)
                {
                    if (matrix[trackButtons.get(j).y][trackButtons.get(j).x].getText().equals(codeWords.get(i)[j].getLetter()))
                    {
                        word.append(codeWords.get(i)[j].getLetter());
                        counterLetters++;
                    }
                }
                else j = trackButtons.size();
            }

            if (counterLetters==trackButtons.size()&&counterLetters!=0)
            {
                selectWord = word.toString();
                vocabulary.remove(selectWord);
                codeWords.remove(i);
                return true;
            }
        }
        return false;
    }

    public void loadRandomWords() throws ClassNotFoundException, IOException {
        Random rand = new Random();
        vocabulary = new ArrayList<>(numberWords);
        ArrayList<String> buffer;
        for (int i = 0; i < numberWords; i++)
        {
            int random = rand.nextInt(path.length);

            FileInputStream fis = new FileInputStream(path[random]);
            ObjectInputStream read = new ObjectInputStream (fis);
            buffer = (ArrayList) read.readObject();
            read.close();
            fis.close();

            random = rand.nextInt(buffer.size());

            if (vocabulary.contains(buffer.get(random)))
                i--;
            else
                vocabulary.add(buffer.get(random));
        }
    }

    public int getRealNumberWords() {
        return realNumberWords;
    }

    public String getSelectWord() {
        return selectWord;
    }

    public boolean isFlFinish() {
        return flFinish;
    }

    public LogicController() {
    }

    public LogicController(String[] path, int numberWords) {
        this.path = path;
        this.numberWords = numberWords;
    }

    public int getNumberWords() {
        return numberWords;
    }

    public void setNumberWords(int numberWords) {
        this.numberWords = numberWords;
    }

    public ArrayList<String> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(ArrayList<String> vocabulary) {
        this.vocabulary = vocabulary;
    }

    public ArrayList<Letter[]> getCodeWords() {
        return codeWords;
    }

    public void setCodeWords(ArrayList<Letter[]> codeWords) {
        this.codeWords = codeWords;
    }
}
