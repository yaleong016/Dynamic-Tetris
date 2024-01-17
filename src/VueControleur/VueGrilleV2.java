package VueControleur;

import Modele.Colours;
import Modele.GrilleSimple;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Observable;
import java.util.Observer;

class VueGrilleV2 extends JPanel implements Observer {

    private final static int TAILLE = 16;
    private GrilleSimple modele;
    Canvas c;
    private Label titleLabel, scoreLabel, highestScoreLabel, difficultyLabel;

    public VueGrilleV2(GrilleSimple _modele) {

        modele = _modele;
        setLayout(new BorderLayout());
        Dimension dim = new Dimension(TAILLE*modele.TAILLE,TAILLE*modele.TAILLE);
        Panel gamePanel = new Panel(new BorderLayout());


        c = new Canvas() {

            public void paint(Graphics g) {

                //definir la couleur du tableau
                for (int i = 0; i < modele.TAILLE; i++) {
                    for (int j = 0; j < modele.TAILLE; j++) {
                        Colours old = modele.getColourOld(i,j);
                        switch (old) {
                            case LIGHT_BLUE:
                                g.setColor(new Color(104,217,235));
                                break;
                            case YELLOW:
                                g.setColor(new Color(255,255,102));
                                break;
                            case PURPLE:
                                g.setColor(new Color(204,0,204));
                                break;
                            case ORANGE:
                                g.setColor(new Color(255,153,51));
                                break;
                            case BLUE:
                                g.setColor(new Color(102,102,255));
                                break;
                            case RED:
                                g.setColor(new Color(255,102,102));
                                break;
                            case GREEN:
                                g.setColor(new Color(102,255,102));
                                break;
                            case WHITE:
                                g.setColor(new Color(255,255,255));
                        }
                        g.fillRect(i * TAILLE, j * TAILLE, TAILLE, TAILLE);
                        g.setColor(Color.BLACK);
                        g.drawRoundRect(i * TAILLE, j * TAILLE, TAILLE, TAILLE, 1, 1);

                    }

                }

                g.setColor(Color.BLUE);
                //g.fillRect(modele.getPieceCourante().getx() * TAILLE, modele.getPieceCourante().gety() * TAILLE, TAILLE, TAILLE);

                //g.fillRect(modele.getPieceCourante1().getx() * TAILLE, modele.getPieceCourante1().gety() * TAILLE, TAILLE, TAILLE);

                //dessiner la nouvelle forme
                Colours col = modele.getBlockColour();
                switch (col) {
                    case LIGHT_BLUE:
                        g.setColor(new Color(104,217,235));
                        break;
                    case YELLOW:
                        g.setColor(new Color(255,255,102));
                        break;
                    case PURPLE:
                        g.setColor(new Color(204,0,204));
                        break;
                    case ORANGE:
                        g.setColor(new Color(255,153,51));
                        break;
                    case BLUE:
                        g.setColor(new Color(102,102,255));
                        break;
                    case RED:
                        g.setColor(new Color(255,102,102));
                        break;
                    case GREEN:
                        g.setColor(new Color(102,255,102));
                        break;
                }
                int[] coord = modele.coordinates();
                g.fillRect(coord[0] * TAILLE, coord[1] * TAILLE, TAILLE, TAILLE);
                g.fillRect(coord[2] * TAILLE, coord[3] * TAILLE, TAILLE, TAILLE);
                g.fillRect(coord[4] * TAILLE, coord[5] * TAILLE, TAILLE, TAILLE);
                g.fillRect(coord[6] * TAILLE, coord[7] * TAILLE, TAILLE, TAILLE);


            }
        };

        //creer le tableau qui montre le points et la difficulte
        gamePanel.add(c);
        add(gamePanel, BorderLayout.CENTER);

        Dimension dim1 = new Dimension(TAILLE * modele.TAILLE, TAILLE * modele.TAILLE);
        c.setPreferredSize(dim1);
        gamePanel.add(c, BorderLayout.CENTER);

        add(gamePanel, BorderLayout.CENTER);

        Panel infoPanel = new Panel(new GridLayout(4, 1));

        titleLabel = new Label("Tetris!", Label.CENTER);
        infoPanel.add(titleLabel);

        scoreLabel = new Label("Score: 0");
        highestScoreLabel = new Label("Highest Score: 0");
        difficultyLabel = new Label("Difficulty: 1");

        infoPanel.add(scoreLabel);
        infoPanel.add(highestScoreLabel);
        infoPanel.add(difficultyLabel);


        add(infoPanel, BorderLayout.NORTH);


    }


    @Override
    public void update(Observable o, Object arg) {

        scoreLabel.setText("Score: " + modele.getCurrentScore());
        highestScoreLabel.setText("Highest Score: " + modele.getHighestScore());
        difficultyLabel.setText("Difficulty: " + modele.getDifficulty());

        BufferStrategy bs = c.getBufferStrategy(); // bs + dispose + show : double buffering pour éviter les scintillements
        if(bs == null) {
            c.createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        c.paint(g); // appel de la fonction pour dessiner
        g.dispose();
        //Toolkit.getDefaultToolkit().sync(); // forcer la synchronisation
        bs.show();
    }
}
