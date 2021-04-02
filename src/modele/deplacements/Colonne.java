package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

/**
 * A la reception d'une commande, toutes les cases (EntitesDynamiques) des colonnes se déplacent dans la direction définie
 * (vérifier "collisions" avec le héros)
 */
public class Colonne extends RealisateurDeDeplacement {
	private Direction directionCourante;
	
	protected boolean realiserDeplacement() {
	   	boolean ret = false;
	        for (EntiteDynamique e : lstEntitesDynamiques) {
	            if (directionCourante != null) {
	            	Entite eHaut = e.regarderDansLaDirection(Direction.haut);
	                switch (directionCourante) {
	                    case haut:
	                        // on ne peut pas sauter sans prendre appui
	                    	// (attention, test d'appui réalisé à partir de la position courante, si la gravité à été appliquée, il ne s'agit pas de la position affichée, amélioration possible)
	                    if (eHaut == null) {
	                        if (e.avancerDirectionChoisie(Direction.haut))
	                            ret = true;
	                    }
	                    break;
	                case bas :
	                	Entite eBas = e.regarderDansLaDirection(Direction.bas);
	                    if (eBas == null) {
	                        if (e.avancerDirectionChoisie(Direction.bas))
	                        	/*if (eHaut.peutMonterDescendre()) {
	                        		((EntiteDynamique) eHaut).avancerDirectionChoisie(Direction.bas);
	                        	}*/
	                            ret = true;
	                    }
	                    break;
	            }
	        }
	    }
	
	    return ret;
	}
	
	public void setDirectionCourante(Direction _directionCourante) {
        directionCourante = _directionCourante;
    }
	
	public void resetDirection() {
        directionCourante = null;
    }

}
   