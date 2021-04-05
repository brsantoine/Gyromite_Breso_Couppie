package modele.deplacements;

import java.util.ArrayList;

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
        for (ArrayList<EntiteDynamique> e : lstColonneE) {
	         int nbcol = e.size();
	         if (directionCourante != null) {
	             Entite eHaut = e.get(0).regarderDansLaDirection(Direction.haut);
	             switch (directionCourante) {
	                 case haut:
	                     if(eHaut == null) {
	                         for (int i = 0; i<nbcol; i++) {
	                             if (e.get(i).avancerDirectionChoisie(Direction.haut))
	                                 ret = true;
	                         }
	                     }
	                     break;
	                 case bas :
	                     Entite eBas = e.get(nbcol - 1).regarderDansLaDirection(Direction.bas);
	                     if (eBas == null) {
	                         for (int i = nbcol - 1; i >= 0; i--) {
	                             if (e.get(i).avancerDirectionChoisie(Direction.bas))
	                                 ret = true;
	                         }
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
   