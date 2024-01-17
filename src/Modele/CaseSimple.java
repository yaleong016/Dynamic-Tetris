package Modele;

public class CaseSimple implements Runnable {

    private int x = 5;
    private int y = 5;
    private int dY = 1;

    private Colours codeCouleur = Colours.LIGHT_BLUE;
    private GrilleSimple grille;

    public CaseSimple(GrilleSimple _grille) {
        grille = _grille;
    }

    public void action() {
        dY *= -1;
    }

    public void right() {
        if (x<20) x+=1;
    }

    public void left() {
        if (x>0) x-=1;
    }


    public void run() {
        int nextY = y;
        int nextX = x;

        nextY += dY;

        if (grille.validationPosition(nextX, nextY)) {
            y = nextY;
            x = nextX;
            //System.out.println("pos" + x + " "+ y);
        } else { //si un case ne peut plus descendre
            dY *= 0;
            if (x<20 && x>=0 && y<20 && y>=0){
                grille.setCase(x,y,false);
            }
        }


    }

    public void stockColour () {
        grille.setColourOld(x, y, codeCouleur);
    }

    public boolean isNextEmpty () {//verifier que le prochain position est dans le grille et est vide
        int nextY = y;
        int nextX = x;

        nextY += dY;
        return grille.validationPosition(nextX, nextY);
    }

    public boolean isRightEmpty () {
        int nextY = y;
        int nextX = x+1;


        return grille.validationPosition(nextX, nextY);
    }

    public boolean isLeftEmpty () {
        int nextY = y;
        int nextX = x-1;


        return grille.validationPosition(nextX, nextY);
    }

    public void setdY(int _dy) {
        dY = _dy;
    }

    public void setGrille(int x, int y, boolean b)
    {
        grille.setCase(x,y,b);
    }


    public int getx() {
        return x;
    }

    public int gety() {
        return y;
    }
    public GrilleSimple getGrille() {return this.grille;}

    public void change (int x, int y, Colours colour) {
        this.x = x;
        this.y = y;
        this.codeCouleur = colour;
    }
}
