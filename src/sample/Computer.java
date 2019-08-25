package sample;

import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Random;

public class Computer {

    private String complexity;
    private Random rand = new Random();
    private float percentTrueStep;
    private int maxTimeSleepBetweenLetter;
    private int maxTimeStartStep;
    private float percentPenalty;
    private int maxTimePenalty;
    private int maxTimeIgnore;
    private boolean flSelectWord = false;
    private boolean flTrueStep = true;
    private boolean flFinishStep = false;
    private int indexWord = 0;
    private int indexLetter = 0;

    // Time in ms

    public Computer(String complexity) {
        this.complexity = complexity;
        initial(complexity);
    }

    public Computer() {
        this.complexity = "Легко";
        initial(complexity);
    }

    private void initial(String complexity) {
        if (complexity.equals("Легко")) {
            this.percentTrueStep = 0.65f;
            this.maxTimeSleepBetweenLetter = 1000;
            this.maxTimeStartStep = 4000;
            this.percentPenalty = 0.18f;
            this.maxTimeIgnore = 7000;
            this.maxTimePenalty = 5000;
        } else {
            this.percentTrueStep = 0.85f;
            this.maxTimeSleepBetweenLetter = 500;
            this.maxTimeStartStep = 3000;
            this.percentPenalty = 0.13f;
            this.maxTimeIgnore = 5000;
            this.maxTimePenalty = 3000;
        }
    }

    public int nextStep(ArrayList<Letter[]> codeWords, Button[][] butMatrix) {
        if (indexLetter == codeWords.get(indexWord).length) {
            flFinishStep = true;
            return 1;
        }
        if (!flSelectWord) {
            if (Math.random() < percentTrueStep) {
                indexWord = rand.nextInt(codeWords.size());
                flSelectWord = true;
                return getRandomTimeStartStep();
            } else
                flTrueStep = false;

        }
        if (flTrueStep) {
            if (Math.random() > percentPenalty) {
                if (complexity.equals("Легко")) {
                    if (percentPenalty >= 0.1)
                        percentPenalty -= 0.005;
                    else percentPenalty = 0.1f;
                } else {
                    if (percentPenalty >= 0.05)
                        percentPenalty -= 0.005;
                    else percentPenalty = 0.05f;
                }
                butMatrix[codeWords.get(indexWord)[indexLetter].getY()]
                        [codeWords.get(indexWord)[indexLetter].getX()].setDisable(false);
                butMatrix[codeWords.get(indexWord)[indexLetter].getY()]
                        [codeWords.get(indexWord)[indexLetter].getX()].fire();
                butMatrix[codeWords.get(indexWord)[indexLetter].getY()]
                        [codeWords.get(indexWord)[indexLetter].getX()].setDisable(true);
                indexLetter++;
                return getRandomTimeSleepBetweenLetter();
            } else {
                return getRandomTimePenalty();
            }
        } else {
            flFinishStep = true;
            return getRandomTimeIgnore();
        }
    }

    public boolean isFlFinishStep() {
        return flFinishStep;
    }

    public void finishStep() {
        flSelectWord = false;
        flTrueStep = true;
        flFinishStep = false;
        indexWord = 0;
        indexLetter = 0;
    }

    private int getRandomTimeSleepBetweenLetter() {
        return rand.nextInt(maxTimeSleepBetweenLetter);
    }

    private int getRandomTimePenalty() {
        return rand.nextInt(maxTimePenalty);
    }

    private int getRandomTimeStartStep() {
        return rand.nextInt(maxTimeStartStep);
    }

    private int getRandomTimeIgnore() {
        return rand.nextInt(maxTimeIgnore);
    }
}
