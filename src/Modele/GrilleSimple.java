package Modele;

import java.io.IOException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Random;


public class GrilleSimple extends Observable implements Runnable {

    public final int TAILLE = 20;
    private Forme current;

    private boolean over;

    protected ScoreManager score;

    private boolean [][] Grille;

    private Colours [][] oldForms = new Colours[TAILLE][TAILLE];
    private OrdonnanceurSimple ord;
    private int diff;
    private long speed;

    private int play;

    private MusicPlayer mp;


    public GrilleSimple() throws IOException {
        this.over = false;
        this.diff = 1;
        this.play = 1;
        this.speed = 400;
        this.current = new FormeI(this);
        this.ord = new OrdonnanceurSimple(this);
        this.ord.start();    // pour changer le temps de pause, garder la référence de l'ordonnanceur
        Grille = new boolean[TAILLE][TAILLE];
        for (int i =0; i < TAILLE; i++){
            for (int j =0; j < TAILLE; j++){
                //true = vide
                //false = plein
                this.Grille[i][j]=true;
                this.oldForms[i][j]=Colours.WHITE;
            }
        }
        score = new ScoreManager();
        this.mp = new MusicPlayer();
        this.mp.playMusic("src/Resource/A-Type Music (Korobeiniki).wav");
    }

    public void right() {
        this.current.right();
    }

    public void left() {
        this.current.left();
    }

    public void drop () {
        this.ord.setSleepDuration(40);
    }
    public void flip() {
        this.current.flip();
    }


    public boolean validationPosition(int _nextX, int _nextY) {
        return (_nextX>=0 && _nextX < TAILLE)&&(_nextY>=0 && _nextY < TAILLE && getCase(_nextX,_nextY));
    }

    public void run() {
        this.current.run();
        setChanged(); // setChanged() + notifyObservers() : notification de la vue pour le rafraichissement
        notifyObservers();

    }

    public int [] coordinates () { //retourner l'ensemble qui contient les coordinate pour les quatre casesimple dans le forme actuel
        int x1 = this.current.list[0].getx();
        int y1 = this.current.list[0].gety();
        int x2 = this.current.list[1].getx();
        int y2 = this.current.list[1].gety();
        int x3 = this.current.list[2].getx();
        int y3 = this.current.list[2].gety();
        int x4 = this.current.list[3].getx();
        int y4 = this.current.list[3].gety();
        return new int [] {x1,y1,x2,y2,x3,y3,x4,y4};
    }
    public Colours getBlockColour () {
        return this.current.colour;
    }

    public boolean getCase (int x, int y) {         //Tells if said case is empty
        return Grille[x][y];
    }

    public void setCase (int x, int y, boolean b) {
        Grille[x][y]=b;
    }

    public Colours getColourOld (int x,int y){
        return oldForms[x][y];
    }

    public void setColourOld (int x, int y, Colours c){
        oldForms[x][y] = c;
    }

    public void nextForm () throws IOException {
//        for (int i = 0; i < 20; i++) {
//            for (int j = 0; j < 20; j++) {
//                System.out.print(this.Grille[i][j] + " ");
//            }
//            System.out.println("");
//        }
        this.ord.setSleepDuration(this.speed);  //reinitialiser le temps qui changera
        checkFull();  //verifier si il y a un ou plusieurs range qui sont pleins
        Random r = new Random();
        int type = r.nextInt((7 - 1) + 1) + 1;
        switch (type) {
            case 1 :
                this.current = new FormeI(this);
                break;
            case 2 :
                this.current = new FormeJ(this);
                break;
            case 3 :
                this.current = new FormeL(this);
                break;
            case 4 :
                this.current = new FormeO(this);
                break;
            case 5 :
                this.current = new FormeS(this);
                break;
            case 6 :
                this.current = new FormeT(this);
                break;
            case 7 :
                this.current = new FormeZ(this);
                break;
        }
        endGame();    //verifier si le jeu devrait terminer ou non (verifier si une nouvelle forme peut encore frayer
    }

    private void checkFull() throws IOException {
        int nbCol=0;
        for (int col = 0; col < 20; col++) { //verifier chaque range s'il est plein
            if (fullRow(col)) {
                increaseDiff();     //checks to increase diff
                shiftDown(col);
                nbCol+=1;
            }
        }
        if(nbCol>0) {      //ajouter des points et des difficulte selon les ranges qui ont ete casse
            score.setScore(nbCol);
            increaseDiff();     //checks to increase diff
        }
    }

    //retourne true si le list est vide
    private boolean fullRow (int col) {
        for (int i = 0; i < this.TAILLE; i++) {
            if (this.Grille[i][col]) {
                return false;
            }
        }
        return true;
    }

    private void shiftDown (int i) {     //changer la grille en fonction des range brise
        for (int col = i; col > 0; col--) {
            for (int j = 0; j < this.TAILLE; j++) {
                this.Grille[j][col] = this.Grille[j][col - 1];
                this.oldForms[j][col] = this.oldForms[j][col - 1];
            }
        }
        for (int col = 0; col < this.TAILLE; col++) {
            this.Grille[col][0] = true;
        }
    }

    private void endGame () throws IOException { //appele lorsque le jeu termine
        for (int i = 0; i < 4; i++) {
            CaseSimple curr = this.current.list[0];
            if (!validationPosition(curr.getx(), curr.gety())) {
                this.ord.interrupt();
                this.mp.stopMusic();
                over = true;
            }
        }
    }

    public void pause () {
        this.ord.invertPause();
    }

    public void increaseDiff () {
        if (this.score.getCurrentScore() >= diff * 200) {    //if score increase by 100
            this.diff++;
            this.speed = (long) (this.speed - (this.speed * 0.1));
            //System.out.println(this.speed);
        }
    }
    public int getCurrentScore () {
        return score.getCurrentScore();
    }
    public int getHighestScore() {
        return score.getHighScore();
    }
    public int getDifficulty() {
        return this.diff;
    }

    public boolean getOver () {return over;}

    public void resetGame () throws IOException {
        this.diff = 1;
        this.speed = 400;
        this.current = new FormeI(this);
        this.ord = new OrdonnanceurSimple(this);
        this.ord.start();    // pour changer le temps de pause, garder la référence de l'ordonnanceur
        Grille = new boolean[TAILLE][TAILLE];
        for (int i =0; i < TAILLE; i++){
            for (int j =0; j < TAILLE; j++){
                this.Grille[i][j]=true;
                this.oldForms[i][j]=Colours.WHITE;
            }
        }
        score.changeCurr(0);
        this.mp = new MusicPlayer();
        this.mp.playMusic("src/Resource/arabesque1.wav");
        this.over = false;
    }
}
