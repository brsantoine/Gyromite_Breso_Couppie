package modele.deplacements;

public class ColonneRouge2Direction extends Colonne {
	
    // Design pattern singleton
    private static ColonneRouge2Direction cr2d;

    public static ColonneRouge2Direction getInstance() {
        if (cr2d == null) {
        	cr2d = new ColonneRouge2Direction();
        }
        return cr2d;
    }


}
