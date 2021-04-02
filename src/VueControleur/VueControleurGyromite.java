package VueControleur;

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

import modele.deplacements.ColonneBleue2Direction;
import modele.deplacements.ColonneRouge2Direction;
import modele.deplacements.Controle4Directions;
import modele.deplacements.Direction;
import modele.plateau.*;
import utils.Parameters;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 */
public class VueControleurGyromite extends JFrame implements Observer {
    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int sizeX = 20; // taille de la grille affichée
    private int sizeY = 10;

    // icones affichées dans la grille
    private ImageIcon icoHero;
    private ImageIcon icoVide;
    private ImageIcon icoMur;
    private ImageIcon icoColonne, icoColonneRougeHaut, icoColonneRougeCentre, icoColonneRougeBas, icoColonneBleuHaut, icoColonneBleuCentre, icoColonneBleuBas;


    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)


    public VueControleurGyromite(Jeu _jeu) {
        sizeX = jeu.SIZE_X;
        sizeY = _jeu.SIZE_Y;
        jeu = _jeu;

        chargerLesIcones();
    }
    
    public void afficher() {
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();
    }
    

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : Controle4Directions.getInstance().setDirectionCourante(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : Controle4Directions.getInstance().setDirectionCourante(Direction.droite); break;
                    case KeyEvent.VK_DOWN : Controle4Directions.getInstance().setDirectionCourante(Direction.bas); break;
                    case KeyEvent.VK_UP : Controle4Directions.getInstance().setDirectionCourante(Direction.haut); break;
                    case KeyEvent.VK_R : ColonneRouge2Direction.getInstance().setDirectionCourante(Direction.haut); break;
                    case KeyEvent.VK_F : ColonneRouge2Direction.getInstance().setDirectionCourante(Direction.bas); break;
                    case KeyEvent.VK_Y : ColonneBleue2Direction.getInstance().setDirectionCourante(Direction.haut); break;
                    case KeyEvent.VK_H : ColonneBleue2Direction.getInstance().setDirectionCourante(Direction.bas); break;
                }
            }
        });
    }


    private void chargerLesIcones() {
        icoHero = chargerIcone("Images/Pacman.png");
        icoVide = chargerIcone("Images/Vide.png");
        icoColonneRougeHaut = chargerIcone("Images/ColonneRougeHaut.png");
        icoColonneRougeCentre = chargerIcone("Images/ColonneRougeCentre.png");
        icoColonneRougeBas = chargerIcone("Images/ColonneRougeBas.png");
        icoColonneBleuHaut = chargerIcone("Images/ColonneBleuHaut.png");
        icoColonneBleuCentre = chargerIcone("Images/ColonneBleuCentre.png");
        icoColonneBleuBas = chargerIcone("Images/ColonneBleuBas.png");
        icoMur = chargerIcone("Images/Mur.png");
        icoCorde = chargerIcone("Images/Corde.png");
        //icoDynamite = chargerIcone("Images/Dynamite.png");
    }

    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurGyromite.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return new ImageIcon(image);
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Gyromite");
        setSize(sizeX*Parameters.IMAGE_SIZE, sizeY*Parameters.IMAGE_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
    }

    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (jeu.getGrille()[x][y] instanceof Heros) { // si la grille du modèle contient un Pacman, on associe l'icône Pacman du côté de la vue
                    // System.out.println("Héros !");
                    tabJLabel[x][y].setIcon(icoHero);
                } else if (jeu.getGrille()[x][y] instanceof Mur) {
                    tabJLabel[x][y].setIcon(icoMur);
                } else if (jeu.getGrille()[x][y] instanceof Colonne) {
                	Colonne colonne = ((Colonne) jeu.getGrille()[x][y]);
                	switch ( colonne.getType() ) {
                	case "Centre" :
                		if (colonne.getCouleur().equals("Rouge")) {
                			tabJLabel[x][y].setIcon(icoColonneRougeCentre);
                		} else {
                			tabJLabel[x][y].setIcon(icoColonneBleuCentre);
                		}
                		break;
                	case "Haut" :
                		if (colonne.getCouleur().equals("Rouge")) {
                			tabJLabel[x][y].setIcon(icoColonneRougeHaut);
                		} else {
                			tabJLabel[x][y].setIcon(icoColonneBleuHaut);
                		}
                		break;
                	case "Bas" :
                		if (colonne.getCouleur().equals("Rouge")) {
                			tabJLabel[x][y].setIcon(icoColonneRougeBas);
                		} else {
                			tabJLabel[x][y].setIcon(icoColonneBleuBas);
                		}
                		break;
                	}
                } else if (jeu.getGrille()[x][y] instanceof Corde) {
                    tabJLabel[x][y].setIcon(icoCorde);
                } else if(jeu.getGrille()[x][y] instanceof Dynamite) {
                    //tabJLabel[x][y].setIcon(icoDynamite);
                } else {
                    tabJLabel[x][y].setIcon(icoVide);
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                    	if (arg instanceof Integer) {
                    		if ((int) arg == Parameters.RETURN_TO_MENU) {
                    			setVisible(false);
                    			dispose();
                    		}
                    	} else {
                    		mettreAJourAffichage();
                    	}
                    }
                }); 

    }
}
