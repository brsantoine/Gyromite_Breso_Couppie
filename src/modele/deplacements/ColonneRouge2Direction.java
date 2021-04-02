package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

public class ColonneRouge2Direction extends RealisateurDeDeplacement {
	
	private Direction directionCourante;
    // Design pattern singleton
    private static ColonneRouge2Direction cr2d;

    public static ColonneRouge2Direction getInstance() {
        if (cr2d == null) {
        	cr2d = new ColonneRouge2Direction();
        }
        return cr2d;
    }

    public void setDirectionCourante(Direction _directionCourante) {
        directionCourante = _directionCourante;
    }

    public boolean realiserDeplacement() {
        boolean ret = false;
        for (EntiteDynamique e : lstEntitesDynamiques) {
            if (directionCourante != null)
                switch (directionCourante) {
                    case haut:
                        Entite eHaut = e.regarderDansLaDirection(Direction.haut);
                        if (eHaut == null) {
                            if (e.avancerDirectionChoisie(Direction.haut))
                                ret = true;
                        }
                        break;
                    case bas :
                    	Entite eBas = e.regarderDansLaDirection(Direction.bas);
                        if (eBas == null) {
                            if (e.avancerDirectionChoisie(Direction.bas))
                                ret = true;
                        }
                        break;
                }
        }

        return ret;

    }

    public void resetDirection() {
        directionCourante = null;
    }

}
