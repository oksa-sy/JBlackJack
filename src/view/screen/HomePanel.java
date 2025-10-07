package view.screen;

import javax.swing.*;

//classe singleton per il pannello dell'home di gioco
public class HomePanel extends Panel {
    
    private static HomePanel instance;
    
    private JButton startGame; //pulsante per inziare il gioco
    private JButton userProfile; //pulsante per accedere al profilo utente
    private JLabel mainBackgroundLabel; //label per lo sfondo

    private HomePanel() 
    {
        initialize();
    }

    public static HomePanel getInstance() 
    {
        if (instance == null) {instance = new HomePanel();}
        return instance;
    }
    
    //inizializza la grafica del panello
    @Override
    public void initialize() {
        mainBackgroundLabel = createImageLabel("sprite\\background.png", 0, 0, 1045, 783);
        add(mainBackgroundLabel);

        startGame = createButton(null, "Start Game", 420, 590, 200, 50);
        userProfile = createButton("sprite\\icons\\profile-icon.png", null, 920, 10, 90, 90);

        mainBackgroundLabel.add(startGame);
        mainBackgroundLabel.add(userProfile);
    }

    //getter e setter
    public JButton getStartGame()
    {
    	return startGame;
    }
    
    public JButton getUserProfile()
    {
    	return userProfile;
    }
    
    //modifica l'immagine del profilo una volta che viene selezionata
    public void setUserProfile(ImageIcon path) 
    {
        userProfile.setIcon(path);
    }


}



