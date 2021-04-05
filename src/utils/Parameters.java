package utils;

import java.awt.Dimension;
import java.awt.Toolkit;

public abstract class Parameters {

	public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	// SIZE
	public static final Integer MENU_WIDTH = (int) 600;
	public static final Integer MENU_HEIGHT = (int) 800;
	
	public static final Integer SCORE_WIDTH = (int) screenSize.getWidth()/2;
	public static final Integer SCORE_HEIGHT = (int) 128;
	
	public static final Integer IMAGE_SIZE = (int) 64;
	
	// RESPONSIVE 
    public static final Integer HORIZONTAL_BUFFER = (int) screenSize.getWidth()/100; // Espace de 1% entre les bords horizontaux de l'ecran et les fenetres
    public static final Integer VERTICAL_BUFFER = (int) screenSize.getHeight()/20; // Espace de 5% entre les bords horizontaux de l'ecran et les fenetres
    public static final Integer TASKBAR_BUFFER = (int) screenSize.getHeight()/50; // Espace de 2% pour prendre en compte la barre des taches
    
    
    // LEVEL
    public static final Integer RETURN_TO_MENU = 0;
    public static final Integer NEXT_LEVEL = 1;
    public static final Integer NUMBER_OF_LEVELS = 2;
}
