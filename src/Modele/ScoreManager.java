package Modele;

import java.io.*;
import java.lang.Math;

public class ScoreManager {

    private int currentScore;
    private int highScore;

    public ScoreManager () throws IOException {
        this.currentScore = 0;
        String stringHs = getHighscoreFromFile().trim();
        try{
            this.highScore = Integer.parseInt(stringHs);
        }
        catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    public void setScore(int nbCol) throws IOException {
        this.currentScore += nbCol * 100 + ((int)Math.pow(2,nbCol-1)-1)*100;
        if (this.currentScore > this.highScore) {
            this.highScore = this.currentScore;
            rewrite(currentScore);
        }
    }



    public void rewrite(int curr) throws IOException {
        String filePath = "src/Resource/Highscore.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(Integer.toString(curr));
        }
    }

    public int getCurrentScore() { return currentScore; }
    public int getHighScore() { return highScore; }
    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
    public String getHighscoreFromFile() throws IOException {

        Class clazz = ScoreManager.class;
        InputStream inputStream = clazz.getResourceAsStream("..//Resource//Highscore.txt");
        String data = readFromInputStream(inputStream);

        return data;
    }
    public void changeCurr (int curr) {
        this.currentScore = curr;
    }
}
