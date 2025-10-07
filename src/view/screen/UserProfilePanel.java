package view.screen;

import java.awt.Color;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import model.players.Level;
import model.players.Player;

@SuppressWarnings("deprecation")

//classe singleton che rappresenta il panel dedicato al profilo utente, implementa observer e osserva il giocatore 
public class UserProfilePanel extends Panel implements Observer{

	private JLabel mainBackgroundLabel;
	
    private JButton saveProfile;
    private JButton backButton;
    private JButton chooseProfileImage;
    
	private JLabel profileImageLabel;
	private JTextField nicknameField;
	private JLabel nicknameLabel;
	
	private JLabel levelLabel;
	private JLabel playedGamesLabel;
	private JLabel lostGamesLabel;
	private JLabel wonGamesLabel;
	private JLabel moneyLabel;
	
    private static UserProfilePanel instance;

    private UserProfilePanel() 
    {
    	initialize();
    }
    
    public static UserProfilePanel getInstance() 
    {
        if (instance == null) {instance = new UserProfilePanel();}
        return instance;
    }
    
    //metodo per inizalizzare la grafica del pannello
    @Override
    public void initialize()
    {
        //immagine di sfondo
        mainBackgroundLabel = createImageLabel("sprite\\profile-background.jpg", 0, 0, 1045, 783);
        add(mainBackgroundLabel);

        // bottone per tornare alla home
        backButton = createButton("sprite\\back-button.png", null, 10, 10, 80, 80);
        mainBackgroundLabel.add(backButton);
        
        //immagine profilo originale
        profileImageLabel =createImageLabel("sprite\\profile-icon(1).png", 475, 50, 110, 110);
        mainBackgroundLabel.add(profileImageLabel);
        
        mainBackgroundLabel.add(createLabel("Your Nickname:", Color.WHITE, Font.PLAIN, 22, 450, 180));
        
        //campo di testo per inserire il nickname
        nicknameField = new JTextField();
        nicknameField.setBounds(430, 230, 200, 30);
        mainBackgroundLabel.add(nicknameField);
        
        //label per visualizzare il nickname una volta inserito
        nicknameLabel = createLabel(null, Color.YELLOW, Font.BOLD, 22, 450, 210);
        nicknameLabel.setVisible(false); //visualizzato solo una volta salvato il profilo
        mainBackgroundLabel.add(nicknameLabel);
        
        initializeGamesLabel();
       
        //pulsante per scegliere l'immagine del profilo
        chooseProfileImage = createButton(null, "Choose Image", 430, 160, 200, 30);
        mainBackgroundLabel.add(chooseProfileImage);

        //pulsante per salvare il profilo
        saveProfile = createButton(null, "Save Profile", 120, 670, 200, 30);
        mainBackgroundLabel.add(saveProfile);  
    }
    
    //inizializzazione delle label inerenti alle informazioni del giocatore
    public void initializeGamesLabel()
    {
        wonGamesLabel = createLabel("Total games won: 0", Color.WHITE, Font.PLAIN, 22, 420, 280);
        mainBackgroundLabel.add(wonGamesLabel);
        
        lostGamesLabel = createLabel("Total games lost: 0", Color.WHITE, Font.PLAIN, 22, 420, 320);
        mainBackgroundLabel.add(lostGamesLabel);

        playedGamesLabel = createLabel("Total games played: 0", Color.WHITE, Font.PLAIN, 22, 420, 360);
        mainBackgroundLabel.add(playedGamesLabel);
        
        moneyLabel = createLabel("Total money owned: $1000", Color.WHITE, Font.PLAIN, 22, 420, 400);
        mainBackgroundLabel.add(moneyLabel);
        
        levelLabel = createLabel("Level: " + Level.BEGINNER.toString(), Color.WHITE, Font.PLAIN, 22, 420, 440);
        mainBackgroundLabel.add(levelLabel);
        
    }
   
    //aggiorna la view del profilo quando ci sono cambiamenti nel giocatore
    @Override
    public void update(Observable o, Object arg) 
    {
        Player player = (Player) o;
        int tMoney = player.getMoney();
        int wGames = player.getWonGames();
        int lGames = player.getLostGames();
        int tGames = player.getGamesPlayed();
        Level pLevel = player.getLevel();

        wonGamesLabel.setText("Total games won: " + wGames);
        lostGamesLabel.setText("Total games lost: " + lGames);
        playedGamesLabel.setText("Total games played: " + tGames);
        moneyLabel.setText("Money owned: $" + tMoney);
        levelLabel.setText("Level: " + pLevel);
    }
    
    //mostra un errore se si salva senza aver inserito il nome
    public void showError()
    {
        JOptionPane.showMessageDialog(this, "enter your name", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
	//permette di scegliere l'immagine profilo da una serie di immagini preselezionate 
	public void chooseProfileImage() 
	{
		//array delle immagini preselezionate
		ImageIcon[] images = new ImageIcon[7];
		for (int i = 1; i < 7; i++) {
			images[i-1] = new ImageIcon("sprite\\icons\\" + i + ".png");
		}

		//JOptionPane con le immagini preselezionate
		ImageIcon selectedImage = (ImageIcon) JOptionPane.showInputDialog(this, 
				"Choose your profile image",
				"Icons",
				JOptionPane.PLAIN_MESSAGE,
				null,
				images,
				images[0]
		);
		//quando l'utente seleziona l'immagine, la label originakle viene sostituita con quella scelta
		if (selectedImage != null) 
		{
			setSelectedFile(selectedImage);
			profileImageLabel.setBounds(485, 65, 90, 90);
		}
	}
      
    //getter e setter
    public JButton getBackButton()
    {
    	return backButton;
    }
    
    public JButton getChooseProfileImage()
    {
    	return chooseProfileImage;
    }
 
    public JLabel getProfileImageLabel() {
    	return profileImageLabel;
    }
    
    //quando viene scelta la nuova immagine aggiorna anche il pulsante nella home
    public void setSelectedFile(ImageIcon f)
    {
    	this.profileImageLabel.setIcon(f);
    	HomePanel.getInstance().setUserProfile(f);
    }
    
    public JButton getSaveProfile()
    {
    	return saveProfile;
    }
    
    public JTextField getNicknameField()
    {
    	return nicknameField;
    }
    
    public JLabel getNicknameLabel()
    {
    	return nicknameLabel;
    }

}


