package VueControleur;

import Modele.GrilleSimple;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VC extends JFrame implements Observer {

    JTextField jt = new JTextField("");
    JPanel jp = new JPanel(new BorderLayout());
    JButton restart = new JButton("Restart");

    JButton restartEnd = new JButton("Restart");

    JButton resume = new JButton("Resume");

    JPanel p2 = new JPanel();

    JPanel pEnd = new JPanel();
    GrilleSimple modele;

    boolean p;

    Popup Pause;

    Popup End;

    Observer vueGrille;
    private Executor ex =  Executors.newSingleThreadExecutor();

    public VC(GrilleSimple _modele) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        modele = _modele; //la grille simple
        p = false;
        setSize(350, 450);
        p2.add(resume);
        pEnd.add(restartEnd);
        p2.add(restart);



        jp.add(jt, BorderLayout.NORTH);

        restartEnd.addActionListener(new ActionListener() { //évènement bouton : object contrôleur qui réceptionne
            @Override
            public void actionPerformed(ActionEvent e) {
                ex.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            modele.resetGame();
                            Pause.hide();
                            End.hide();
                            p = false;
                            lastTime = System.currentTimeMillis();
                        } catch (IOException exc) {
                            throw new RuntimeException(exc);
                        }
                    }
                });
            }
        });

        restart.addActionListener(new ActionListener() { //évènement bouton : object contrôleur qui réceptionne
            @Override
            public void actionPerformed(ActionEvent e) {
                ex.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            modele.resetGame();
                            Pause.hide();
                            p = false;
                            lastTime = System.currentTimeMillis();
                        } catch (IOException exc) {
                            throw new RuntimeException(exc);
                        }
                    }
                });
            }
        });

        resume.addActionListener(new ActionListener() { //évènement bouton : object contrôleur qui réceptionne
            @Override
            public void actionPerformed(ActionEvent e) {
                ex.execute(new Runnable() {
                    @Override
                    public void run() {
                        modele.pause();
                        Pause.hide();
                        p = false;
                    }
                });
            }
        });


        //vueGrille = new VueGrilleV1(modele); // composants swing, saccades
        vueGrille = new VueGrilleV2(modele); // composant AWT dédié

        jp.add((JPanel)vueGrille, BorderLayout.CENTER);
        setContentPane(jp);


        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    switch (e.getKeyCode()){
                        case KeyEvent.VK_LEFT :
                            modele.left();
                            return true;
                        case KeyEvent.VK_RIGHT :
                            modele.right();
                            return true;
                        case KeyEvent.VK_SPACE:
                            modele.drop();
                             return true;
                        case KeyEvent.VK_UP :
                            modele.flip();
                            return true;
                        case KeyEvent.VK_ESCAPE :
                                modele.pause();
                                if(!p) {
                                    PopupFactory pf = new PopupFactory();
                                    Pause = pf.getPopup(jp, p2, 150, 220);
                                    Pause.show();
                                    p = true;
                                }
                                else
                                {
                                    Pause.hide();
                                    p = false;
                                }

                    }
                }
                return false;
            }
        });

    }

    static long lastTime = System.currentTimeMillis();

    @Override
    public void update(Observable o, Object arg) { // rafraichissement de la vue

        SwingUtilities.invokeLater(new Runnable() {
            //@Override
            public void run() {
                vueGrille.update(o, arg);
                if(modele.getOver())
                {
                    PopupFactory pf = new PopupFactory();
                    End = pf.getPopup(jp, pEnd, 175, 220);
                    Pause = pf.getPopup(jp, p2, 150, 220);
                    End.show();
                }

                jt.setText("Elapsed time : " + ((System.currentTimeMillis() - lastTime))/1000 + " sc"); //+ "ms - x = " + modele.getPieceCourante().getx() + " y = " + modele.getPieceCourante().gety());

            }
        });

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    GrilleSimple m = null;
                    try {
                        m = new GrilleSimple();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    VC vc = new VC(m);
                    vc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    m.addObserver(vc);
                    vc.setVisible(true);
                    vc.pack();

                }
            }
        );
    }







}
