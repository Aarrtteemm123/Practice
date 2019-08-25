package sample;

import java.awt.*;

public class Move {
    private String letter;
    private int indexLetter;
    private Point point1;
    private Point point2;

    public Move(String letter, int indexLetter, int x1, int y1, int x2, int y2) {
        this.letter = letter;
        this.indexLetter = indexLetter;
        this.point1 = new Point(x1,y1);
        this.point2 = new Point(x2,y2);
    }

    public int getIndexLetter() {
        return indexLetter;
    }

    public void setIndexLetter(int indexLetter) {
        this.indexLetter = indexLetter;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getX1() {
        return point1.x;
    }

    public void setX1(int x1) {
        this.point1.x = x1;
    }

    public int getY1() {
        return point1.y;
    }

    public void setY1(int y1) {
        this.point1.y = y1;
    }

    public int getX2() {
        return point2.x;
    }

    public void setX2(int x2) {
        this.point2.x = x2;
    }

    public int getY2() {
        return point2.y;
    }

    public void setY2(int y2) { this.point2.y = y2;
    }
}
