package modele.deplacements;

import modele.plateau.Jeu;
import utils.Parameters;

import java.util.ArrayList;
import java.util.Observable;

import static java.lang.Thread.*;

public class Ordonnanceur extends Observable implements Runnable {
    private Jeu jeu;
    private ArrayList<RealisateurDeDeplacement> lstDeplacements = new ArrayList<RealisateurDeDeplacement>();
    private long pause;
    public void add(RealisateurDeDeplacement deplacement) {
        lstDeplacements.add(deplacement);
    }

    public Ordonnanceur(Jeu _jeu) {
        jeu = _jeu;
    }

    public void start(long _pause) {
        pause = _pause;
        new Thread(this).start();
    }
    
    public void stop() {
    	currentThread().interrupt();
    }
    
    public void reprise() {
    	currentThread().run();
    }

    @Override
    public void run() {
        boolean update = false;

        while(true) {
        	if (jeu.niveauFinit()) {
        		if (jeu.niveauSuivant()) {
        			setChanged();
        			notifyObservers(new Integer(Parameters.NEXT_LEVEL));
        		} else {
        			setChanged();
                	notifyObservers(new Integer(Parameters.RETURN_TO_MENU));
        		}
        		
            }
        	
            jeu.resetCmptDepl();
            for (RealisateurDeDeplacement d : lstDeplacements) {
                if (d.realiserDeplacement())
                    update = true;
            }

            Controle4Directions.getInstance().resetDirection();
            ColonneRouge2Direction.getInstance().resetDirection();
            ColonneBleue2Direction.getInstance().resetDirection();

            if (update) {
                setChanged();
                notifyObservers();
            }

            try {
                sleep(pause);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
