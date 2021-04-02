package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

public class ColonneBleue2Direction extends RealisateurDeDeplacement {
	
	private Direction directionCourante;
    // Design pattern singleton
    private static ColonneBleue2Direction cb2d;

    public static ColonneBleue2Direction getInstance() {
        if (cb2d == null) {
        	cb2d = new ColonneBleue2Direction();
        }
        return cb2d;
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
                        // on ne peut pas sauter sans prendre appui
                        // (attention, test d'appui réalisé à partir de la position courante, si la gravité à été appliquée, il ne s'agit pas de la position affichée, amélioration possible)
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
