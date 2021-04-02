package modele.deplacements;

public class ColonneBleue2Direction extends Colonne {
	
    // Design pattern singleton
    private static ColonneBleue2Direction cb2d;

    public static ColonneBleue2Direction getInstance() {
        if (cb2d == null) {
        	cb2d = new ColonneBleue2Direction();
        }
        return cb2d;
    }

    
}
