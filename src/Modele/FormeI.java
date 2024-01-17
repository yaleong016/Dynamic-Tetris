package Modele;

public class FormeI extends Forme {
    private static Colours colour = Colours.LIGHT_BLUE;

    public FormeI(GrilleSimple g) {
        super(g, colour);
        creerForme();
    }
    @Override
    void creerForme() {
        this.list[0].change(8,0, this.colour);
        this.list[1].change(9,0, this.colour);
        this.list[2].change(10,0, this.colour);
        this.list[3].change(11,0, this.colour);
    }


}
