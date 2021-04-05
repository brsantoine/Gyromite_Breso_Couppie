/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.deplacements.ColonneBleue2Direction;
import modele.deplacements.ColonneRouge2Direction;
import modele.deplacements.Controle4Directions;
import modele.deplacements.Direction;
import modele.deplacements.Gravite;
import modele.deplacements.Ordonnanceur;
import utils.Parameters;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.Timer;

/** Actuellement, cette classe garde les postions
 * (ajouter conditions de victoire, chargement du plateau, etc.)
 */

public class Jeu {

    public int SIZE_X = 20;
    public int SIZE_Y = 10;

    // compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    private HashMap<Entite, Integer> cmptDeplH = new HashMap<Entite, Integer>();
    private HashMap<Entite, Integer> cmptDeplV = new HashMap<Entite, Integer>();

    private Heros hector;
    private Gravite gravite;
    
    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées
    private Entite[][] grilleEntitesDynamique = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées

    private Ordonnanceur ordonnanceur = new Ordonnanceur(this);

    private int niveauCourant;
    private int score;
    private int temps;
    private Timer tempsThread = new Timer(1000, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			temps -= 1;
		}
	});
    
    public Jeu() {
    	initialiserOrdonnanceur();
        hector = new Heros(this);
        gravite.addEntiteDynamique(hector);
        Controle4Directions.getInstance().addEntiteDynamique(hector);
    }

    public void resetCmptDepl() {
        cmptDeplH.clear();
        cmptDeplV.clear();
    }

    public void start(long _pause) {
        ordonnanceur.start(_pause);
    }
    
    public Entite[][] getGrille() {
        return grilleEntites;
    }
    public Entite[][] getGrilleDynamique() {
        return grilleEntitesDynamique;
    }
    
    public Heros getHector() {
        return hector;
    }
    
    public int getScore() {
    	return score;
    }
    
    public int getTime() {
    	return temps;
    }
  
    public void initialiserOrdonnanceur() {
    	gravite = new Gravite();
    	
    	ordonnanceur.add(gravite);
    	ordonnanceur.add(Controle4Directions.getInstance());
    	ordonnanceur.add(ColonneRouge2Direction.getInstance());
    	ordonnanceur.add(ColonneBleue2Direction.getInstance());   
    }
    
    private void initialisationDuMonde(int numeroNiveau) {
    	String niveau = "Maps/niveau" + numeroNiveau + ".map";
    	File file = new File(niveau);
    	int lineNumber = 0;
    	
    	try (BufferedReader br = new BufferedReader(new FileReader(file))) {
    	    String line = "";

            for(int i=0; i< SIZE_X; i++) {
                for (int j=0; j< SIZE_Y; j++) {
                    this.grilleEntites[i][j] = null;
                    this.grilleEntitesDynamique[i][j] = null;
                }
            }
    	    
    	    while ((line = br.readLine()) != null && line.charAt(0) != '#') {
    	    	for (int i = 0; i < line.length(); i++) {
    	    		switch(line.charAt(i)) {
    	    		case 'W' :
    	    			addEntite(new Mur(this), i, lineNumber);
    	    			break;
    	    		case 'H' :
    	    			addEntiteDynamique(hector, i, lineNumber);
    	    			break;
    	    		case 'C' :
    	    			addEntite(new Corde(this), i, lineNumber);
    	    			break;
    	    		case 'D' :
    	    			addEntite(new Dynamite(this), i, lineNumber);
    	    			break;
    	    		/*case '' :
    	    			addEntite(new (this), i, lineNumber);
    	    			break;*/
    	    		default :
    	    			break;
    	    		}
    	        }
    	    	lineNumber++;
    	    }
    	    
    	    if (hector == null) {
    	    	throw new Exception("Hector non initialis� !");
    	    }
    	    while ((line = br.readLine()) != null && line.charAt(0) != '#') {
    	    	String[] lineSplit = line.split(";");
    	    	int x = Integer.parseInt(lineSplit[1]);
    	    	int y = Integer.parseInt(lineSplit[2]);
    	    	int yFin = Integer.parseInt(lineSplit[3]);

                ArrayList<EntiteDynamique> lstColonne = new ArrayList<EntiteDynamique>();

    	    	Colonne c = new Colonne(this, lineSplit[0], "Haut");
    			addEntite(c, x, y);
                lstColonne.add(c);
                if (c.getCouleur().equals("Rouge"))
                    ColonneRouge2Direction.getInstance().addEntiteDynamique(c);

                else
                    ColonneBleue2Direction.getInstance().addEntiteDynamique(c);

    	    	
    			for (int i = y+1; i < yFin; i++) {
    				c = new Colonne(this, lineSplit[0], "Centre");
    				addEntite(c, x, i);
                    lstColonne.add(c);
                    if (c.getCouleur().equals("Rouge"))
                        ColonneRouge2Direction.getInstance().addEntiteDynamique(c);
                    else
                        ColonneBleue2Direction.getInstance().addEntiteDynamique(c);
    			}
    			c = new Colonne(this, lineSplit[0], "Bas");
    			addEntite(c, x, yFin);

    			if (c.getCouleur().equals("Rouge")) {
                    ColonneRouge2Direction.getInstance().addEntiteDynamique(c);
                    lstColonne.add(c);
                    ColonneRouge2Direction.getInstance().AddColonneEntiere(lstColonne);
                }
				else {
                    ColonneBleue2Direction.getInstance().addEntiteDynamique(c);
                    lstColonne.add(c);
                    ColonneBleue2Direction.getInstance().AddColonneEntiere(lstColonne);
                }
    	    }
    	} catch (Exception e) {
    	    System.out.println("GENERATION MAPS : Lecture de fichier rat� (Niveau " + numeroNiveau + ")\n");
    	    System.out.println(e.getMessage());
    	}
    }
    
    private void addEntite(Entite e, int x, int y) {
        grilleEntites[x][y] = e;
        map.put(e, new Point(x, y));
    }

    private void addEntiteDynamique(Entite e, int x, int y) {
        grilleEntitesDynamique[x][y] = e;
        map.put(e, new Point(x, y));
    }
    
    /** Permet par exemple a une entité  de percevoir sont environnement proche et de définir sa stratégie de déplacement
     *
     */
    public Entite regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = map.get(e);
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }
    
    public boolean getCorde (Entite e) {
        Point p = map.get(e);
        if(objetALaPosition(p) instanceof Corde)
            return true;
        else
            return false;
    }
    
    /** Si le déplacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     * Sinon, rien n'est fait.
     */
    public boolean deplacerEntite(Entite e, Direction d) {
        boolean retour = false;
        
        Point pCourant = map.get(e);
        Point pCible = calculerPointCible(pCourant, d);
        
        if (contenuDansGrille(pCible) && objetALaPosition(pCible) == null || grilleEntites[pCible.x][pCible.y].peutPermettreDeMonterDescendre() || grilleEntites[pCible.x][pCible.y].peutEtreRecuperer()) { // a adapter (collisions murs, etc.)
            // compter le déplacement : 1 deplacement horizontal et vertical max par pas de temps par entite
            switch (d) {
                case bas:
                case haut:
                    if (cmptDeplV.get(e) == null) {
                        cmptDeplV.put(e, 1);

                        retour = true;
                    }
                    break;
                case gauche:
                case droite:
                    if (cmptDeplH.get(e) == null) {
                        cmptDeplH.put(e, 1);
                        retour = true;
                    }
                    break;
            }
        }

        if (retour) {
            if(e instanceof Heros) {
                deplacerHero(pCourant, pCible, e);
            } else {
                deplacerEntite(pCourant, pCible, e);
            }
            recupDynamite(pCible);
        }

        return retour;
    }
    
    
    private Point calculerPointCible(Point pCourant, Direction d) {
        Point pCible = null;
        
        switch(d) {
            case haut: pCible = new Point(pCourant.x, pCourant.y - 1); break;
            case bas : pCible = new Point(pCourant.x, pCourant.y + 1); break;
            case gauche : pCible = new Point(pCourant.x - 1, pCourant.y); break;
            case droite : pCible = new Point(pCourant.x + 1, pCourant.y); break;     
            
        }
        
        return pCible;
    }
    
    private void deplacerHero(Point pCourant, Point pCible, Entite e) {
        grilleEntitesDynamique[pCourant.x][pCourant.y] = null;
        grilleEntitesDynamique[pCible.x][pCible.y] = e;
        map.put(e, pCible);
    }

    private void deplacerEntite(Point pCourant, Point pCible, Entite e) {
        grilleEntites[pCourant.x][pCourant.y] = null;
        grilleEntites[pCible.x][pCible.y] = e;
        map.put(e, pCible);
    }
    
    /** Indique si p est contenu dans la grille
     */

    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }
    
    private Entite objetALaPosition(Point p) {
        Entite retour = null;
        
        if (contenuDansGrille(p)) {
                retour = grilleEntites[p.x][p.y];
        }
        
        return retour;
    }

    public boolean debutPartie() {
        niveauCourant = 1;
        score = 0;
        if (niveauCourant > Parameters.NUMBER_OF_LEVELS) {
            return false;
        }
        initialisationDuMonde(niveauCourant);
        
        temps = 999;
        tempsThread.start();
        return true;
    }

    public boolean niveauSuivant() {
    	niveauCourant++;
    	if (niveauCourant > Parameters.NUMBER_OF_LEVELS) {
    		return false;
    	}
    	initialisationDuMonde(niveauCourant);
    	
    	temps = 999;
    	tempsThread.start();
    	return true;
    }
    
    public boolean niveauFinit() {
        for(int i=0; i< SIZE_X; i++) {
            for (int j=0; j< SIZE_Y; j++) {
                if (grilleEntites[i][j] instanceof Dynamite)
                    return false;
            }
        }
        tempsThread.stop();
        score += temps*10;
        return true;
    }

    public Ordonnanceur getOrdonnanceur() {
        return ordonnanceur;
    }

    private void recupDynamite(Point pCourant) {
        if (grilleEntites[pCourant.x][pCourant.y] instanceof Dynamite) {
        	grilleEntites[pCourant.x][pCourant.y] = null;
        	score += 100;
        }
            
    }
}
