package sample;

public class MenuInfo {
    private Integer sizeMatrix;
    private Integer numberWords;
    private String player;
    private String complexity;

    public MenuInfo() {
        sizeMatrix=0;
        numberWords=0;
        player="";
        complexity="";
    }

    public MenuInfo(int sizeMatrix, int numberWords, String player, String complexity) {
        this.sizeMatrix = sizeMatrix;
        this.numberWords = numberWords;
        this.player = player;
        this.complexity = complexity;
    }

    public int getSizeMatrix() {
        return sizeMatrix;
    }

    public void setSizeMatrix(int sizeMatrix) {
        this.sizeMatrix = sizeMatrix;
    }

    public int getNumberWords() {
        return numberWords;
    }

    public void setNumberWords(int numberWords) {
        this.numberWords = numberWords;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }
}
