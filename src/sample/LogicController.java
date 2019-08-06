package sample;

import java.util.ArrayList;

public class LogicController {
    //1. Fill in the matrix
    //2. Check step
    //3. Get result
    private ArrayList<String>Vocabulary;
    private ArrayList<Letter[]>codeWords;
    private int[][] matrixButtonsColor;
    private String complexity;
    private String[] path;
    private int numberWords;
    private int realNumberWords;
    private float percentRealWords = 0.7f;

    public ArrayList<Letter[]> fillMatrixWords()
    {
        Letter[] word = new Letter[1];
        word[0] = new Letter();
        codeWords = new ArrayList<>();
        codeWords.add(word);
        return codeWords;
    }

    public boolean checkStep()
    {
        return false;
    }

    public void loadRandomWords()
    {

    }

    public int getRealNumberWords() {
        return realNumberWords;
    }

    public LogicController() {
    }

    public LogicController(int[][] matrixButtonsColor, String complexity, String[] path, int numberWords) {
        this.matrixButtonsColor = matrixButtonsColor;
        this.complexity = complexity;
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
        return Vocabulary;
    }

    public void setVocabulary(ArrayList<String> vocabulary) {
        Vocabulary = vocabulary;
    }

    public ArrayList<Letter[]> getCodeWords() {
        return codeWords;
    }

    public void setCodeWords(ArrayList<Letter[]> codeWords) {
        this.codeWords = codeWords;
    }

    public int[][] getMatrixButtonsColor() {
        return matrixButtonsColor;
    }

    public void setMatrixButtonsColor(int[][] matrixButtonsColor) {
        this.matrixButtonsColor = matrixButtonsColor;
    }

    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }
}
