package Modele;

public class FormeL extends Forme {

    private static Colours colour = Colours.ORANGE;
    public FormeL(GrilleSimple g) {
        super(g, colour);
        creerForme();
    }
    @Override
    void creerForme() {
        this.list[0].change(9,0, this.colour);
        this.list[1].change(9,1, this.colour);
        this.list[2].change(10,0, this.colour);
        this.list[3].change(11,0, this.colour);
    }
}
