package sample;

public class Letter {
    private String letter;
    private int x;
    private int y;

    public Letter() {
    }

    public Letter(String letter, int x, int y) {
        this.letter = letter;
        this.x = x;
        this.y = y;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
