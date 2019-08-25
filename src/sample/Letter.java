package sample;

import java.awt.*;

public class Letter {
    private String letter;
    private Point point;

    public Letter() {
    }

    public Letter(String letter, int x, int y) {
        this.letter = letter;
        this.point = new Point(x,y);
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getX() {
        return point.x;
    }

    public void setX(int x) {
        this.point.x = x;
    }

    public int getY() {
        return point.y;
    }

    public void setY(int y) {
        this.point.y = y;
    }
}
