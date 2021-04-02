package VueControleur;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import modele.plateau.Jeu;
import utils.Parameters; 


public class VueControleurMenu extends JFrame implements Observer {
	
	private Jeu jeu;
	
	private JPanel panelBoutons;
	private JButton bPlay, bRules, bHS, bQuit;
	
	public VueControleurMenu(Jeu _jeu) {
		super();
		
		jeu = _jeu;
		
		setTitle("Gyromite");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocation(Parameters.HORIZONTAL_BUFFER*((Parameters.screenSize.width-Parameters.MENU_WIDTH)/40), Parameters.VERTICAL_BUFFER*2 - Parameters.TASKBAR_BUFFER);
		setLayout(new BorderLayout(100, 100));
		setSize(Parameters.MENU_WIDTH, Parameters.MENU_HEIGHT);
		
		JPanel panelTitre = new JPanel();
        add(panelTitre, BorderLayout.NORTH);
        JLabel labelTitre = new JLabel("Gyromite");
        panelTitre.add(labelTitre);
        labelTitre.setFont(labelTitre.getFont().deriveFont(64.0f));
        
        panelBoutons = new JPanel();
        panelBoutons.setLayout(new GridLayout(7, 1));
        add(panelBoutons, BorderLayout.CENTER);
        
        JPanel emptyPanelWest = new JPanel();
        JPanel emptyPanelEast = new JPanel();
        JPanel emptyPanelSouth = new JPanel();
        add(emptyPanelWest, BorderLayout.WEST);
        add(emptyPanelEast, BorderLayout.EAST);
        add(emptyPanelSouth, BorderLayout.SOUTH);
        
        bPlay = new JButton("Jouer");
        bPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	VueControleurGyromite vc = new VueControleurGyromite(jeu);
                jeu.getOrdonnanceur().addObserver(vc);
                
                setVisible(false);
                
                if (jeu.niveauSuivant()) {
                	vc.afficher();
                	vc.setVisible(true);
                	jeu.start(300);
                }
           } 
        });
        bRules = new JButton("Règles");
        bHS = new JButton("Meilleurs scores");
        
        bQuit = new JButton("Quitter");
        bQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
           } 
        });
        panelBoutons.add(bPlay);
        panelBoutons.add(new JPanel());
        panelBoutons.add(bRules);
        panelBoutons.add(new JPanel());
        panelBoutons.add(bHS);
        panelBoutons.add(new JPanel());
        panelBoutons.add(bQuit);
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof Integer) {
    		if ((int) arg1 == Parameters.RETURN_TO_MENU) {
    			setVisible(true);
    		}
    		
    	}
		
	}

}
