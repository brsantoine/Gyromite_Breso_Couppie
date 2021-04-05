package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

public class Gravite extends RealisateurDeDeplacement {
    @Override
    public boolean realiserDeplacement() {
        boolean ret = false;

        for (EntiteDynamique e : lstEntitesDynamiques) {
            Entite eBas = e.regarderDansLaDirection(Direction.bas);
            if ( (eBas != null && !eBas.peutServirDeSupport()) || (eBas == null && !e.getCorde()) ) { /*&& (e.peutPermettreDeMonterDescendre())*/ 
            	if (e.avancerDirectionChoisie(Direction.bas)) {
                		ret = true;
            	}   
            }
        }

        return ret;
    }
}
