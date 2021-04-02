package VueControleur;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import modele.deplacements.ColonneBleue2Direction;
import modele.deplacements.ColonneRouge2Direction;
import modele.deplacements.Controle4Directions;
import modele.deplacements.Direction;
import modele.plateau.Colonne;
import modele.plateau.Corde;
import modele.plateau.Dynamite;
import modele.plateau.Heros;
import modele.plateau.Jeu;
import modele.plateau.Mur;
import utils.Parameters;

public class VueControleurGyromite implements Observer {

	private Jeu jeu;
	
	// frameJeu
	private int jeuSizeX, jeuSizeY;
	private ImageIcon icoHero, icoVide, icoMur, icoCorde, icoDynamite, icoColonneRougeHaut, icoColonneRougeCentre, icoColonneRougeBas, icoColonneBleuHaut, icoColonneBleuCentre, icoColonneBleuBas;
	private JLabel[][] tabJLabel;
	
	//
	private JFrame frameMenu, frameJeu;
	
	private JPanel menuPanelBoutons;
	private JButton menuBoutonPlay, menuBoutonRules, menuBoutonHS, menuBoutonQuit;
	
	public VueControleurGyromite(Jeu _jeu) {
		jeu = _jeu;
		jeuSizeX = jeu.SIZE_X;
		jeuSizeY = jeu.SIZE_Y;
		
		////////////////////////////////////
		///////// CREATION DU MENU /////////
		////////////////////////////////////
		frameMenu = new JFrame();
		frameMenu.setTitle("Gyromite");
		frameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameMenu.setResizable(false);
		frameMenu.setLocation(Parameters.HORIZONTAL_BUFFER*((Parameters.screenSize.width-Parameters.MENU_WIDTH)/40), Parameters.VERTICAL_BUFFER*2 - Parameters.TASKBAR_BUFFER);
		frameMenu.setLayout(new BorderLayout(100, 100));
		frameMenu.setSize(Parameters.MENU_WIDTH, Parameters.MENU_HEIGHT);
		
		JPanel panelTitre = new JPanel();
		frameMenu.add(panelTitre, BorderLayout.NORTH);
        JLabel labelTitre = new JLabel("Menu Gyromite");
        panelTitre.add(labelTitre);
        labelTitre.setFont(labelTitre.getFont().deriveFont(64.0f));
        
        menuPanelBoutons = new JPanel();
        menuPanelBoutons.setLayout(new GridLayout(7, 1));
        frameMenu.add(menuPanelBoutons, BorderLayout.CENTER);
        
        JPanel emptyPanelWest = new JPanel();
        JPanel emptyPanelEast = new JPanel();
        JPanel emptyPanelSouth = new JPanel();
        frameMenu.add(emptyPanelWest, BorderLayout.WEST);
        frameMenu.add(emptyPanelEast, BorderLayout.EAST);
        frameMenu.add(emptyPanelSouth, BorderLayout.SOUTH);
        
        menuBoutonPlay = new JButton("Jouer");
        menuBoutonRules = new JButton("R�gles");
        menuBoutonHS = new JButton("Meilleurs scores");
        menuBoutonQuit = new JButton("Quitter");
        
        menuPanelBoutons.add(menuBoutonPlay);
        menuPanelBoutons.add(new JPanel());
        menuPanelBoutons.add(menuBoutonRules);
        menuPanelBoutons.add(new JPanel());
        menuPanelBoutons.add(menuBoutonHS);
        menuPanelBoutons.add(new JPanel());
        menuPanelBoutons.add(menuBoutonQuit);
        
		////////////////////////////////////
		////////// ACTIONS BOUTONS /////////
		////////////////////////////////////
        menuBoutonPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameMenu.setVisible(false);
                creerJeu();
                
                if (jeu.debutPartie()) {
                	frameJeu.setVisible(true);
                	jeu.start(300);
                }
           } 
        });
        
        menuBoutonQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	frameMenu.dispose();
           } 
        });
	}
	
	public void afficherMenu() {
		frameMenu.setVisible(true);
	}
	
	private void creerJeu() {
		frameJeu = new JFrame();
        frameJeu.setTitle("Gyromite");
        frameJeu.setSize(jeuSizeX*Parameters.IMAGE_SIZE, jeuSizeY*Parameters.IMAGE_SIZE);
        frameJeu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(jeuSizeY, jeuSizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[jeuSizeX][jeuSizeY];

        for (int y = 0; y < jeuSizeY; y++) {
            for (int x = 0; x < jeuSizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
            }
        }
        frameJeu.add(grilleJLabels);
        
        chargerLesIcones();
        ajouterEcouteurClavier();
	}
	
	private void ajouterEcouteurClavier() {
        frameJeu.addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
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
        icoHero = chargerIcone("Images/Hector.png");
        icoVide = chargerIcone("Images/Vide.png");
        icoColonneRougeHaut = chargerIcone("Images/ColonneRougeHaut.png");
        icoColonneRougeCentre = chargerIcone("Images/ColonneRougeCentre.png");
        icoColonneRougeBas = chargerIcone("Images/ColonneRougeBas.png");
        icoColonneBleuHaut = chargerIcone("Images/ColonneBleuHaut.png");
        icoColonneBleuCentre = chargerIcone("Images/ColonneBleuCentre.png");
        icoColonneBleuBas = chargerIcone("Images/ColonneBleuBas.png");
        icoMur = chargerIcone("Images/Mur.png");
        icoCorde = chargerIcone("Images/Corde.png");
        icoDynamite = chargerIcone("Images/Dynamite.png");
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
	
	private void mettreAJourAffichage() {

        for (int x = 0; x < jeuSizeX; x++) {
            for (int y = 0; y < jeuSizeY; y++) {
                if (jeu.getGrille()[x][y] instanceof Mur) {
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
                    tabJLabel[x][y].setIcon(icoDynamite);
                } else {
                    tabJLabel[x][y].setIcon(icoVide);
                }
                if (jeu.getGrilleDynamique()[x][y] instanceof Heros) {
                    tabJLabel[x][y].setIcon(icoHero);
                }
            }
        }
    }
	
	@Override
	public void update(Observable arg0, Object arg1) {
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	if (arg1 instanceof Integer) {
            		if ((int) arg1 == Parameters.RETURN_TO_MENU) {
            			frameJeu.setVisible(false);
            			frameJeu.dispose();
            			frameMenu.setVisible(true);
            		}
            	} else {
            		mettreAJourAffichage();
            	}
            }
        }); 
		
	}

}
