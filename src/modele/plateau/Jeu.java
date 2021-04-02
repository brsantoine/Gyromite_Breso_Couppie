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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;

/** Actuellement, cette classe gère les postions
 * (ajouter conditions de victoire, chargement du plateau, etc.)
 */

public class Jeu {

    public int SIZE_X;
    public int SIZE_Y;

    // compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    private HashMap<Entite, Integer> cmptDeplH = new HashMap<Entite, Integer>();
    private HashMap<Entite, Integer> cmptDeplV = new HashMap<Entite, Integer>();

    private Heros hector;
    private Gravite gravite;
    
    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées
    private boolean pCorde = false; // permet de mémoriser l'entité sur laquelle est passé le héros, utile que pour les Cordes

    private Ordonnanceur ordonnanceur = new Ordonnanceur(this);

    private int niveauCourant;
    
    public Jeu() {
    	initialiserOrdonnanceur();
    	niveauCourant = 0;
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
    
    public Heros getHector() {
        return hector;
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
    	    
    	    line = br.readLine();
    	    String[] sizeLine = line.split(";");
    	    SIZE_X = Integer.parseInt(sizeLine[0]);
    	    SIZE_Y = Integer.parseInt(sizeLine[1]);
    	    grilleEntites = new Entite[SIZE_X][SIZE_Y];
    	    
    	    
    	    while ((line = br.readLine()) != null && line.charAt(0) != '#') {
    	    	for (int i = 0; i < line.length(); i++) {
    	    		switch(line.charAt(i)) {
    	    		case 'W' :
    	    			addEntite(new Mur(this), i, lineNumber);
    	    			break;
    	    		case 'H' :
    	    			hector = new Heros(this);
    	    			addEntite(hector, i, lineNumber);
    	    			gravite.addEntiteDynamique(hector);
    	    	    	Controle4Directions.getInstance().addEntiteDynamique(hector);
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
    	    	
    	    	Colonne c = new Colonne(this, lineSplit[0], "Haut");
    			addEntite(c, x, y);
    			if (c.getCouleur().equals("Rouge"))
    				ColonneRouge2Direction.getInstance().addEntiteDynamique(c);
				else 
					ColonneBleue2Direction.getInstance().addEntiteDynamique(c);
    	 
    	    	
    			for (int i = y+1; i < yFin; i++) {
    				c = new Colonne(this, lineSplit[0], "Centre");
    				addEntite(c, x, i);
    				
    				if (c.getCouleur().equals("Rouge"))
        				ColonneRouge2Direction.getInstance().addEntiteDynamique(c);
    				else 
    					ColonneBleue2Direction.getInstance().addEntiteDynamique(c);
    			}
    			c = new Colonne(this, lineSplit[0], "Bas");
    			addEntite(c, x, yFin);
    			
    			if (c.getCouleur().equals("Rouge"))
    				ColonneRouge2Direction.getInstance().addEntiteDynamique(c);
				else 
					ColonneBleue2Direction.getInstance().addEntiteDynamique(c);
    	    	
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
    
    /** Permet par exemple a une entité  de percevoir sont environnement proche et de définir sa stratégie de déplacement
     *
     */
    public Entite regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = map.get(e);
        return objetALaPosition(calculerPointCible(positionEntite, d));
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
            boolean ptemp;
            if(objetALaPosition(pCible) instanceof Corde) {
                ptemp = true;
            } else {
                ptemp = false;
            }
            deplacerEntite(pCourant, pCible, e);
            pCorde = ptemp;
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
    
    private void deplacerEntite(Point pCourant, Point pCible, Entite e) {
        if(this.pCorde) {
            grilleEntites[pCourant.x][pCourant.y] = new Corde(this);
        } else {
            grilleEntites[pCourant.x][pCourant.y] = null;
        }
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
    
    public boolean niveauSuivant() {
    	niveauCourant++;
    	if (niveauCourant > Parameters.NUMBER_OF_LEVELS) {
    		return false;
    	}
    	initialisationDuMonde(niveauCourant);
    	return true;
    }
    
    public boolean niveauFinit() {
    	Point p = map.get(hector);
    	return (p.x == 2 && p.y == 8) || (p.x == 3 && p.y == 3);
    	
    }

    public Ordonnanceur getOrdonnanceur() {
        return ordonnanceur;
    }
}
