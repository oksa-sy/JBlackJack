package view.screen;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import model.players.Player;


//classe singleton che rappresenta un panel dedicato alla scelta dell'importo della scommessa del giocatore
//implementa Observer e osserva il giocatore
@SuppressWarnings("deprecation")
public class PlaceBetPanel extends Panel implements Observer {
    
    private JLabel mainBackgroundLabel; //label di sfondo
    private JButton[] betButtons; //pulsanti per inserire l'importo della scommessa
    private int[] betAmounts; //valori dei pulsanti 
    private JButton homeButton; //pulsante per tornare alla schermata home
    private JLabel moneyLabel; //soldi del giocatore
    private JLabel currentBet; //scommessa del giocatore
    private JButton startPlaying; //pulsante per iniziare a giocare 
    private JPanel betPanel; //panel sul quale vengono inseriti i pulanti per inserire l'importo della scommessa
    
    private static PlaceBetPanel instance;
    
    private PlaceBetPanel() 
    {
        initialize();        
    }
    
    public static PlaceBetPanel getInstance() 
    {
        if (instance == null) {instance = new PlaceBetPanel();}
        return instance;
    }

    //inizializzazion grafica 
    @Override
    public void initialize() {
    	//sfondo
        mainBackgroundLabel = createImageLabel("sprite\\general-background.jpg", 0, 0, 1045, 783);
        add(mainBackgroundLabel);

        //label
        mainBackgroundLabel.add(createLabel("PLACE YOUR BET", Color.white, Font.PLAIN, 26, 420, 20));
        moneyLabel = createLabel("Money: $1000", Color.RED, Font.BOLD, 36, 420, 80);
        mainBackgroundLabel.add(moneyLabel);
        mainBackgroundLabel.add(createLabel("Current bet amount:", Color.white, Font.PLAIN, 26, 415, 160));
        currentBet = createLabel("$0", Color.RED, Font.BOLD, 36, 415, 220);
        mainBackgroundLabel.add(currentBet);

        //pannello scommessa
        betPanel = new JPanel();
        betPanel.setLayout(null);
        betPanel.setOpaque(false);
        betPanel.setBounds(175, 300, 800, 783);
        mainBackgroundLabel.add(betPanel);
        
        //inizializzazione dei pulsanti 
        initializeBetButtons();

        //pulsante per accedere alla schermata home
        homeButton = createButton("sprite\\home-button.png", null, 10, 10, 80, 80);
        mainBackgroundLabel.add(homeButton);
       
        //pulsante per iniziare a giocare
        startPlaying = createButton(null, "Start Playing!", 440, 660, 220, 50);
        mainBackgroundLabel.add(startPlaying);
    }

    //inizializzazione dei pulsanti ai quali viene dato un aspetto grafico in base all'importo, che vengono successivamente
    //inseriti sul betPanel
    public void initializeBetButtons() 
    {
        betAmounts = new int[]{1, 10, 25, 50, 100, 250, 500, 1000};
        betButtons = new JButton[betAmounts.length]; // Inizializza l'array con lo stesso numero di elementi

        //posizioni iniziali
        int xPosition = 0;
        int yPosition = 0;
        //distanza tra i pulsanti
        int buttonSpacing = 40;
        
        //dimensioni pulsanti
        int buttonWidth = 150;
        int buttonHeight = 150;
        
        for (int i = 0; i < betAmounts.length; i++) 
        {
            int betAmount = betAmounts[i];
            	String imagePath = "sprite\\chips\\" + betAmount + ".png";
                JButton betButton = createButton(imagePath, null, xPosition, yPosition, buttonWidth, buttonHeight);
                betPanel.add(betButton);
                betButtons[i] = betButton; 
                
                xPosition += buttonWidth + buttonSpacing; //aumento spazio tra i pulsanti 
                if (xPosition + buttonWidth > betPanel.getWidth())  //se si arriva alla fine si passa alla riga successiva
                {
                    xPosition = 0;
                    yPosition += buttonHeight + buttonSpacing;
                }  
        }
        //si aggiorna l'aspetto del panel
        betPanel.revalidate();
        betPanel.repaint();
    }
    
    //metodo update che aggiorna la view quando ci sono cambiamenti nel giocatore
    @Override
    public void update(Observable o, Object arg) 
    {
            Player player = (Player) o;
            int money = player.getMoney();
            int bet = player.getBet();
            moneyLabel.setText("Money: $" + money);
            currentBet.setText("$" + bet);
            //controllo per verificare che sul panel non sono presenti pulsanti che hanno un'importo superiore ai soldi del giocatore
        	for (int i = 0; i < betButtons.length; i++)
        	{
        		if (betAmounts[i] > money) {betButtons[i].setVisible(false);}
        		else betButtons[i].setVisible(true);
        	}
    }

    //messaggio di errore se si cerca di iniziare il gioco senza aver fatto la scommessa
    public void errorPlacingBet()
    {
        JOptionPane.showMessageDialog(this, "Please Enter Bet", 
                "ERROR", JOptionPane.ERROR_MESSAGE);
    }
    
    //getter
    public JButton getStartPlayingButton() 
    {
        return startPlaying;
    }

    public JButton[] getBetButtons() 
    {
        return betButtons;
    }

    public int[] getBetAmounts()
    {
    	return betAmounts;
    }
    
    public JButton getHomeButton()
    {
    	return homeButton;
    }
    
}
