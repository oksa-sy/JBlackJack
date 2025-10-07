package view;

import java.awt.Color;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import control.AudioManager;
import model.players.Player;
import view.animations.TextAnimation;
import view.screen.Panel;

//panel principale di gioco, è una classe singleton e  osserva il giocatore e si occupa del piazzamento dei pulsanti e label
//contenentiu informazioni del giocatore 
@SuppressWarnings("deprecation")
public class GameView extends Panel implements Observer{

    private JLabel mainBackgroundLabel; //label principale di sfondo
    private JButton hitButton; //pulsante per pescare
    private JButton standButton; //pulsante per stare 
    private JButton splitButton; //pulsante per lo split
    private JButton doubleBetButton; //pulsante per duplicare la scommessa
    private JButton insuranceButton; //pulsante per l'assicurazione 
    private JButton startNewGame; //pulsante per iniziare un nuovo round
    
    private JLabel bet; //label rappresentate l'importo di scommessa del giocatore
    private JLabel money; //label rappresentante i soldi del giocatore 
    private JLabel deckView; //label per rappresentare il mazzo 
    private JLabel totalBet; //label per rappresentare l'importo totale di scommessa di tutti i giocatori
	private TextAnimation animation; //testo animato 
	
	private AudioManager mainAudio;
	
    private static GameView instance;
    
    //costruttore 
    private GameView() 
    {
        initialize();
        mainAudio = AudioManager.getInstance();
        mainAudio.loop("audio\\main-audio.wav");
    }

    public static GameView getInstance() 
    {
        if (instance == null) 
        {
            instance = new GameView();
        }
        return instance;
    }
    
    //inizzializzazione grafica 
    public void initialize() 
    {
        mainBackgroundLabel = createImageLabel("sprite/general-background.jpg", 0, 0, 1045, 783);
        add(mainBackgroundLabel);
        
        hitButton = createButton(null, "HIT", 710, 480, 90, 50);
        hitButton.setVisible(false);

        standButton = createButton(null, "STAND", 830, 480, 120, 50);
        standButton.setVisible(false);

        splitButton = createButton(null, "SPLIT", 430, 530, 120, 50);
        splitButton.setVisible(false);

        doubleBetButton = createButton(null, "DOUBLEX2!", 400, 600, 180, 50);
        doubleBetButton.setVisible(false);
        
        insuranceButton = createButton(null, "INSURANCE", 400, 680, 180, 50);
        insuranceButton.setVisible(false);
        
        money = createLabel(null, Color.WHITE, Font.BOLD, 22, 775, 380);
        bet = createLabel(null, Color.WHITE, Font.BOLD, 22, 780, 430);

        deckView = createImageLabel("sprite/cards/BACK.png", 300, 321, 96, 140);

    	totalBet = createLabel(null, Color.RED, Font.BOLD, 30, 20, 350);
    	
    	startNewGame = createButton(null, "New Round", 400, 120, 180, 40);
    	startNewGame.setForeground(Color.RED);
    	startNewGame.setBackground(Color.BLACK);
        startNewGame.setVisible(false);
    	
        mainBackgroundLabel.add(totalBet);
        mainBackgroundLabel.add(hitButton);
        mainBackgroundLabel.add(standButton);
        mainBackgroundLabel.add(splitButton);
        mainBackgroundLabel.add(doubleBetButton);
        mainBackgroundLabel.add(money);
        mainBackgroundLabel.add(bet);
        mainBackgroundLabel.add(deckView);
    	mainBackgroundLabel.add(startNewGame);
    	mainBackgroundLabel.add(insuranceButton);
    }
    
    //mostra la schermata di vittoria o di perdita e fa partire un suono a seconda del caso
    public void showEndPanel(boolean playerWon, int frequency, boolean isSplit) 
    {
    	//rende visibile il pulsante per iniziare un nuovo round
	    startNewGame.setVisible(true);
        startNewGame.setEnabled(true);
        //se il giocatore ha vinto in caso di split si verifica se ha vinto entrambi i round o uno solo
        //altrimenti mostra "You Won!" con il suono associato
        if (playerWon) 
        {
            animation = frequency == 1 && isSplit ? new TextAnimation("One Round Won!") : new TextAnimation("You Won!");
            animation.getTimer().start();
            animation.setBounds(240, 60, 500, 300);
            AudioManager.getInstance().play("audio\\won.wav");
        } else  //se perde mostra "You Lost!" con il suono associato
        {
            animation = new TextAnimation("You Lost!");
            animation.getTimer().start();
            animation.setBounds(340, 60, 300, 300);
            AudioManager.getInstance().play("audio\\lost.wav"); 
        }
        mainBackgroundLabel.add(animation);
        mainBackgroundLabel.revalidate();
        mainBackgroundLabel.repaint();  
    }
    
    //prima di iniziare una nuova partita viene controllato che il giocatore abbia soldi sufficienti
    //in caso contrario viene mostrata una finestra che avvisa che verranno aggiunti dei soldi per
    //continuare a giocare
    public void checkPlayerMoney(int money) 
    {
    	if (money < 0)
    	{
	    	JOptionPane.showMessageDialog(
	    		mainBackgroundLabel,
	            "You're out of money,adding money...",
	            "Game Over",
	            JOptionPane.NO_OPTION);
    	}

    }
    
    //disattiva i pulsanti alla fine del turno del giocatore 
    public void disableButtons()
    {
		hitButton.setEnabled(false);
		hitButton.setVisible(false);
		standButton.setEnabled(false);
		standButton.setVisible(false); 
		doubleBetButton.setVisible(false);
		doubleBetButton.setEnabled(false);
		insuranceButton.setVisible(false);
		insuranceButton.setEnabled(false);
		splitButton.setVisible(false);
		splitButton.setEnabled(false);
		startNewGame.setEnabled(false);
		startNewGame.setVisible(false);
	}
    
    //metodo che aggiorna la grafica quando ci sono cambiamenti nel giocatore
    //esso si occcupa di gestire il cambiamento dei soldi e della scommessa
    //inoltre notifica il giocatore di aver fatto blackjack con un suono
    @Override
    public void update(Observable o, Object arg) 
    {
    	//se l'argomento è una stringa e uguale a BLACKJACK, parte il suono
    	if (arg instanceof String)
    	{
        	String event = (String) arg;
        	if (event.equals("BLACKJACK")){AudioManager.getInstance().play("audio\\blackJack.wav");}
    	}
    	//altrimenti modifica la label dei soldi e della scommessa in base ai combiamenti
    	else
        {     	
            Player player = (Player) o;
            int playerMoney = player.getMoney();
            int playerBet = player.getBet();
            // Aggiorna le etichette esistenti
            money.setText("bank: $" + playerMoney);
            bet.setText("bet: $: " + playerBet);
        }
    }
    
    //getter e setter
    public JButton getHitButton() {
        return hitButton;
    }

    public JButton getStandButton() {
        return standButton;
    }
    
    public JButton getDoubleBetButton()
    {
    	return doubleBetButton;
    }
    
    public JButton getSplitButton()
    {
    	return splitButton;
    }

    public JLabel getMainBackgroundLabel()
    {
    	return mainBackgroundLabel;
    }
    
    public void setTotBetAmount(int amount)
    {
    	totalBet.setText("tot: " + "$" + amount);
    }
     
    public JButton getStartNewGame()
    {
    	return startNewGame;
    }
    
    public TextAnimation getAnimation()
    {
    	return animation;
    }

    public JButton getInsuranceButton()
    {
    	return insuranceButton;
    }
    
}
