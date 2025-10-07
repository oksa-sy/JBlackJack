package view.screen;

import javax.swing.*;
import java.awt.*;

//classe singleton rappresentate il panel che permette di scegliere il numero di giocatori
public class NumberOfPlayersPanel extends Panel {

    private JButton[] playerButtons;
    private JLabel mainBackgroundLabel;
    private JButton homeButton;
    
    private static NumberOfPlayersPanel instance;

    private NumberOfPlayersPanel() 
    {
        initialize();
    }

    public static NumberOfPlayersPanel getInstance() 
    {
        if (instance == null) {instance = new NumberOfPlayersPanel();}
        return instance;
    }

    //inizializzazione grafica 
    @Override
    public void initialize() 
    {
        //label di sfondo 
        mainBackgroundLabel = createImageLabel("sprite\\profile-background.jpg", 0, 0, 1045, 783);
        add(mainBackgroundLabel);

        mainBackgroundLabel.add(createLabel("Choose number of players: ", Color.WHITE, Font.BOLD, 26, 340, 200));

        //creazione dei pulsanti per selezionare il numero di giocatori
        initializePlayerButtons();

        //pulsante per tornare alla home
        homeButton = createButton("sprite\\home-button.png", null, 10, 10, 80, 80);
        mainBackgroundLabel.add(homeButton);
    }


    //aggiunge i pulsanti destinati a selezionare il numero di giocatori(massimo 4)
    public void initializePlayerButtons() 
    {
        playerButtons = new JButton[4];
        int gap = 0;
        for (int i = 0; i < playerButtons.length; i++) 
        {
            int numberOfPlayers = i + 1;
            playerButtons[i] = createButton(null, String.valueOf(numberOfPlayers), 250 + gap, 300, 90, 90);
            gap += 140;
            mainBackgroundLabel.add(playerButtons[i]);
        }
    }

    //getter
    public JButton[] getPlayerButtons()
    {
    	return playerButtons;
    }
    
    public JButton getHomeButton()
    {
    	return homeButton;
    }
}

