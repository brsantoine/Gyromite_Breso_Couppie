package modele.plateau;

public class Dynamite extends EntiteStatique {
    public Dynamite(Jeu _jeu) { super(_jeu); }

    public boolean peutServirDeSupport() { return false; };
    public boolean peutEtreRecuperer() { return true; };
}



