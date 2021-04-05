package modele.deplacements;

import modele.plateau.EntiteDynamique;
import java.util.ArrayList;

/**
Tous les d�placement sont d�clench�s par cette classe (gravit�, controle clavier, IA, etc.)
 */
public abstract class RealisateurDeDeplacement {
    protected ArrayList<EntiteDynamique> lstEntitesDynamiques = new ArrayList<EntiteDynamique>();
    protected ArrayList<ArrayList<EntiteDynamique>> lstColonneE = new ArrayList<ArrayList<EntiteDynamique>>();
    protected abstract boolean realiserDeplacement();

    public void addEntiteDynamique(EntiteDynamique ed) {lstEntitesDynamiques.add(ed);};

    public void AddColonneEntiere(ArrayList<EntiteDynamique> lstC) {
        System.out.println("nombre de colonne :" + lstColonneE.size());
    }

}