package view;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import view.screen.HomePanel;

public class GameFrame extends JFrame {
    
    //classe singleton che definisce il frame di gioco
    private static GameFrame instance;//unica istanza
    
    private static String gameName = "BlackJack";
    private static int x = 1045;
    private static int y = 783;
    private static String gameIconPath = "sprite\\game-icon.jpg";
  
    //costruttore privato
    private GameFrame() 
    {
        initializeFrame();
        this.switchPanel(HomePanel.getInstance());
        this.setVisible(true);
    }

    //metodo per accedere all'istanza
    public static GameFrame getInstance() 
    {
        if (instance == null) {instance = new GameFrame();}
        return instance;
    }

    //inizializzazione del frame
    public void initializeFrame() 
    {
        setTitle(gameName);
        setSize(x, y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
		try {
			setIconImage(ImageIO.read(new File(gameIconPath)));
		} catch (IOException e) {
			// viene usata l'icona di default della finestra
		}
    }

    //metodo per cambiare panel
    public void switchPanel(JPanel newPanel) 
    {
        setContentPane(newPanel);
        revalidate();
        repaint();
    }

}

