package control;

import model.GameModel;
import model.objects.Card;
import model.objects.Deck;
import model.players.ArtificialPlayer;
import model.players.Dealer;
import model.players.Player;
import view.GameFrame;
import view.GameView;
import view.entity.ArtificialPlayerView;
import view.entity.DealerView;
import view.entity.PlayerView;
import view.objects.CardView;
import view.screen.HomePanel;
import view.screen.NumberOfPlayersPanel;
import view.screen.PlaceBetPanel;
import view.screen.UserProfilePanel;
import control.GameStateManager.GameState;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class GameController {
	
	//componenti view
    private GameView view;
    private List<ArtificialPlayerView> aPViews;
    private GameFrame frame;
    private UserProfilePanel user;
    private HomePanel home;
    private NumberOfPlayersPanel numberView;
    private DealerView dealerPanel;
	private PlayerView playerPanel;
	private PlaceBetPanel betPanel;
    
	//componenti model
    private Player player;
    private Deck deck;
    private List<ArtificialPlayer> totPlayers;
    private Dealer dealer;
    private GameModel model;

    //componenti per la logica di gioco
    private int numberOfPlayers; //numero di giocatori totali
    private int index; //indice per scorrere il turno dei giocatori artificiali
	private int totBetAmount; //totale scommessa
	private GameStateManager gameManager; //manger dei turni

	//costruttore in cui vengono istanzati i componenti del model e della view
	public GameController() 
	{
		setUpView();
		player = new Player();
		player.addObserver(betPanel);
		player.addObserver(playerPanel);
		player.addObserver(user);
		player.addObserver(view);
		dealer = new Dealer();
		dealer.addObserver(dealerPanel);
		deck = new Deck();
		
    	index = 0;
    	gameManager = new GameStateManager(this);
    	gameManager.setState(GameState.BETTING); 
    	
        view.getMainBackgroundLabel().add(dealerPanel);
        view.getMainBackgroundLabel().add(playerPanel);
        
        addListeners();
		viewActionListeners();
    }
	
	//inizializzazione view
	public void setUpView()
	{
		frame = GameFrame.getInstance();
		home = HomePanel.getInstance();
		user = UserProfilePanel.getInstance();
		numberView = NumberOfPlayersPanel.getInstance();
		betPanel = PlaceBetPanel.getInstance();
		view = GameView.getInstance();
		dealerPanel = new DealerView();
        playerPanel = new PlayerView();
	}
	
	//aggiunti i listener dei pulsanti presenti nella view
	public void viewActionListeners()
	{
		home.getStartGame().addActionListener(e -> frame.switchPanel(numberView));
		home.getUserProfile().addActionListener(e -> frame.switchPanel(user));
		user.getBackButton().addActionListener(e -> frame.switchPanel(home));
		user.getChooseProfileImage().addActionListener(e -> user.chooseProfileImage());
		//quanto viene salvato il profilo utente viene nascolto il textfield e mostrata la label, se vuota si mostra un errore
		user.getSaveProfile().addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						if (user.getNicknameField().getText().isEmpty()) {user.showError();}
						else
						{
					        user.getNicknameField().setVisible(false);
					        user.getNicknameLabel().setText(user.getNicknameField().getText());
					        user.getNicknameLabel().setVisible(true);
					        user.getSaveProfile().setVisible(false);
						}
					}
				});
		
		for (int i = 0; i < betPanel.getBetButtons().length; i++) 
		{
			int n = i;
			betPanel.getBetButtons()[i].addActionListener(e -> player.placeBet(betPanel.getBetAmounts()[n]));
		}
		numberView.getHomeButton().addActionListener(e -> frame.switchPanel(home));
		for (int i = 0; i < numberView.getPlayerButtons().length; i++)
		{
			int n = i;
			numberView.getPlayerButtons()[i].addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e) {
			                numberOfPlayers = n + 1;
			                frame.switchPanel(betPanel);
			                setUpTotalPlayers();
			                model = new GameModel(player, dealer, totPlayers); //dopo aver selezionato il numero dei giocatori
			                //vengono istanziati i giocatori artificiali e creato il model
						}
					});
		}
		betPanel.getHomeButton().addActionListener(e -> frame.switchPanel(home));
		//una volta fatta la scommessa si fa partire il loop di gioco
		betPanel.getStartPlayingButton().addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
							if (player.getBet() <= 0) {betPanel.errorPlacingBet();}
							else
							{
					            frame.switchPanel(view);
					            AudioManager audio = AudioManager.getInstance();
					            audio.play("audio\\coin-spill.wav");
					            gameManager.gameLoop();
							}
					}
				});
	}
   
	//inializzazione dei giocatori artificiali in base al numero scelto dall'utente
    public void setUpTotalPlayers()
    {
    	totPlayers = new ArrayList<>();
    	aPViews = new ArrayList<>();
    	for (int i = 0; i < numberOfPlayers - 1; i++)
    	{ 	
    		ArtificialPlayer aP = new ArtificialPlayer();
    		ArtificialPlayerView aPView = new ArtificialPlayerView(i);
    		aP.addObserver(aPView);
    		aPViews.add(aPView);
    		view.getMainBackgroundLabel().add(aPView);
    		totPlayers.add(aP);
    	}		
    }

    //scommessa, svolta nel model e nella view si mostra il numero ottenuto
    public void handleBetting() 
    {
    	totBetAmount = model.betting(numberOfPlayers);
        view.setTotBetAmount(totBetAmount);
    }

    //distribuzione carte
    public void dealCards() {model.deal(deck);}
    
    //TURNI GIOCATORI:
    
    //PLAYER
    //inizialmente al player è permesso decidere cosa fare con la propria mano e l'importo della scommessa
    //se si verifica la situazione
    public void handleDecision()
    {
    	if (player.doubleBetAllowed())
    	{
    		view.getDoubleBetButton().setVisible(true);
    		view.getDoubleBetButton().setEnabled(true);
    	}
    	if (player.insuranceAllowed(dealer) && !player.blackJack()) 
    	{
    		view.getInsuranceButton().setVisible(true);
    		view.getInsuranceButton().setEnabled(true);
    	}
    	if (player.splitAllowed()) 
    	{
    		view.getSplitButton().setVisible(true);
    		view.getSplitButton().setEnabled(true);
    	}
    }
    
    //turno giocatore, vengono mostrati i bottoni hit e stand, se non si puo pescare  si disabilitano i bottoni
    //alla seconda mano in caso di split o al turno dei giocatori artificiali
    public void handlePlayerTurn()
    {  
    	view.getHitButton().setVisible(true);
    	view.getHitButton().setEnabled(true);
    	view.getStandButton().setVisible(true); 
    	view.getStandButton().setEnabled(true);

    	if (player.getStand() || player.blackJack() || player.exceededMaxValue()) 
    	{
    		view.disableButtons();
	        gameManager.setState(player.hasSplitHand() && !player.isFirstRoundCompleted() 
                    ? GameState.HANDLE_SPLIT 
                    : GameState.AP_TURN);
    	}
    }
	
	//listener dei pulsanti inerenti al turno del giocatore
    public void addListeners()
    {
        view.getHitButton().addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	player.hit(deck);
            	view.disableButtons();
            }
        });
        
        view.getStandButton().addActionListener(new ActionListener()
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	player.setStand(true);
            	view.disableButtons();
            	
    	        gameManager.setState(player.hasSplitHand() && !player.isFirstRoundCompleted() 
                        ? GameState.HANDLE_SPLIT 
                        : GameState.AP_TURN);
            }
		});
        
        view.getDoubleBetButton().addActionListener(new ActionListener()
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	player.doubleBet();
            	view.getDoubleBetButton().setVisible(false);
            	view.getDoubleBetButton().setEnabled(false);
            	totBetAmount += player.getBet()/2;
            	view.setTotBetAmount(totBetAmount);
            }
		});  
        
        view.getStartNewGame().addActionListener(new ActionListener()
        		{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						resetGame();
						view.getAnimation().setVisible(false);
					}
        	
        		});
        view.getInsuranceButton().addActionListener(new ActionListener()
        		{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						player.setInsurance(true);
						view.getInsuranceButton().setVisible(false);
						view.getInsuranceButton().setEnabled(false);
					}
        	
        		});
        view.getSplitButton().addActionListener(new ActionListener()
        		{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						playerPanel.getCardPanel().remove(playerPanel.getCardPanel().getComponent(1));
						if (player.getMoney() >= player.getBet()) 
						{
							player.split();
							player.hit(deck);
						}
						view.getSplitButton().setVisible(false);
						view.getSplitButton().setEnabled(false);
						totBetAmount += player.getBet() / 2;
						view.setTotBetAmount(totBetAmount);
					}
        	
        		});
    }   
    
    //in caso di split viene salvata la seconda carta per la seconda mano
    //viene modificato il panel e viene pescata una seconda carta, viene specificato che è gia stato completato un round
    //per non ripetere la possibilta di split
    public void handleSplit()
    {
    	player.prepareNewHand();
    	playerPanel.updatePanel();
    	Card c = player.getSplitHand();
    	player.getHand().add(c);
    	CardView cView = new CardView(c.getImagePath());
    	c.addObserver(cView);
    	playerPanel.getCardPanel().add(cView);
    	player.hit(deck);
    	player.setFirstRoundCompleted(true);
    }
    
    //ARTIFICIAL PLAYERS
    //turno giocatori artificiali, si controlla se stanno o pescano e si avanza nei turni di conseguenza
    //quando si arriva all'ultimo giocatore si passa al turno del dealer
    public void handleAPTurn() 
    {
		if (index < numberOfPlayers-1)
		{
			if (!totPlayers.get(index).getStand()) {totPlayers.get(index).drawsOrStands(deck);}
			else {index++;}
			gameManager.setState(GameState.AP_TURN);
		}
    	else gameManager.setState(GameState.DEALER_TURN);
    }

    //DEALER
	//turno dealer, si controlla se sta o pesca e se sta si conclude il loop do gioco 
    public void handleDealerTurn() 
    {
    	if (!dealer.getStand())
    	{
        	dealer.drawsOrStands(deck);
            gameManager.setState(GameState.DEALER_TURN);
    	}
    	else gameManager.setState(GameState.ROUND_END);
    }
    
    //si girano le carte 
    public void finishGame()
    {
    	model.finish(numberOfPlayers);
    	dealerPanel.getTotPoints().setVisible(true);
    	for (int i = 0; i < totPlayers.size(); i++)
    	{
    		aPViews.get(i).getTotPoints().setVisible(true);
    	}
    }

    //si controlla chi ha vinto e si mostra la schermata conseguente
    public void win() 
    {
    	model.endGame(totBetAmount);
        view.showEndPanel(model.getIsPlayerWinner(), model.getFrequency(), player.hasSplitHand());  	  
    }
	
    //si resetta il gioco per un nuovo round
    public void resetGame() 
	{
		model.reset();
		view.checkPlayerMoney(player.getMoney());
		index = 0;
	    totBetAmount = 0;
	    view.setTotBetAmount(totBetAmount);
	    view.disableButtons();
	    gameManager.setState(GameState.BETTING); 
	    frame.switchPanel(numberView);
	    view.getAnimation().getTimer().stop();
	}
}