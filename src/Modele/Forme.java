package Modele;


//cette classe abstract est le foundation pour les formes specifique en tetris
//chaque class qui est un enfant de forme prend quatre casesimple, et les mettre dans les bonne coordinates
//pour creer le figuration


import java.io.IOException;

public abstract class Forme implements Runnable {

    protected int dX = 1;
    protected CaseSimple[] list;
    protected Colours colour;

    protected GrilleSimple grille;

    //le constructor est protected parceque les autres ne peut l'accéder que les classe qui l'inherite
    protected Forme (GrilleSimple g, Colours colour) {
        grille = g;
        this.list = new CaseSimple[4];
        this.list[0] = new CaseSimple(g);
        this.list[1] = new CaseSimple(g);
        this.list[2] = new CaseSimple(g);
        this.list[3] = new CaseSimple(g);
        this.colour = colour;
    }
    //comme chaque figuration de tetris est different, la fonction creerForme doit etre different dans chaqu'un
    abstract void creerForme ();
    public void left() {
        if (dX == 1 && this.list[0].isLeftEmpty() && this.list[1].isLeftEmpty() && this.list[2].isLeftEmpty() && this.list[3].isLeftEmpty()) {
            this.list[0].left();
            this.list[1].left();
            this.list[2].left();
            this.list[3].left();
        }
    }

    public void right() {
        if (dX == 1 && this.list[0].isRightEmpty() && this.list[1].isRightEmpty() && this.list[2].isRightEmpty() && this.list[3].isRightEmpty()) {
            this.list[0].right();
            this.list[1].right();
            this.list[2].right();
            this.list[3].right();
        }
    }
    public void rotate() {

    }
    public void drop () {

    }
    public void action() {
        this.list[0].action();
        this.list[1].action();
        this.list[2].action();
        this.list[3].action();
    }

    public void run() {
        if (this.list[0].isNextEmpty() && this.list[1].isNextEmpty() && this.list[2].isNextEmpty() && this.list[3].isNextEmpty())
        {
            this.list[0].run();
            this.list[1].run();
            this.list[2].run();
            this.list[3].run();
        }
        else {
            dX = 0;
            for (int i =0; i<4; i++)
            {
                this.list[i].setdY(0);
                this.list[i].setGrille(this.list[i].getx(), this.list[i].gety(), false);
//                System.out.println("x: " + this.list[i].getx());
//                System.out.println("y: " + this.list[i].gety());
                this.list[i].stockColour();
            }
            try {
                grille.nextForm();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void flip() {
        int Xc = this.list[2].getx();
        int Yc = this.list[2].gety();


        for (int i = 0; i < 4; i++) {
            int cx = (Xc-(Yc-this.list[i].gety()));
            int cy = (Yc+(Xc-this.list[i].getx()));
            if (!this.list[2].getGrille().validationPosition(cx, cy)) {
                return;      //si l'un de case ne peut pas rotate, on quitte cette fonction
            }
        }
        //si touts les cases peuvent rotate, on rotate le forme
        for (int i = 0; i < 4; i++) {
            int cx = (Xc-(Yc-this.list[i].gety()));
            int cy = (Yc+(Xc-this.list[i].getx()));
            this.list[i].change(
                    cx,
                    cy,
                    this.colour);
        }
    }
}
