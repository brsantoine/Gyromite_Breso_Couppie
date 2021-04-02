package modele.plateau;

public class Colonne extends EntiteDynamique {
	private String couleur, type;
	
    public Colonne(Jeu _jeu, String _couleur, String _type) {
    	super(_jeu); 
    	couleur = _couleur;
    	type = _type;
    }

    public boolean peutEtreEcrase() { return false; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; };
    public boolean peutEtreRecuperer(){return false;};
    public boolean peutMonterDescendre() { return true; };
    
    public String getCouleur() { return couleur; }
    public String getType() { return type; }
}
