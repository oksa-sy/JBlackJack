package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

//gestisce i turni di gioco
public class GameStateManager {
	
	private GameState currentState; //fase attutale del gioco
	private GameController controller; //controller di gioco

	//enum con le varie fasi del gioco
	public enum GameState 
	{
		BETTING, DEALING_CARDS, PLAYER_DECISION, PLAYER_TURN, AP_TURN, DEALER_TURN, HANDLE_SPLIT, ROUND_END, GAME_OVER,
	}
	
	//costruttore
	public GameStateManager(GameController gameController) 
	{
		this.controller = gameController;
	}
	
	//loop di gioco secondo l'uso di un timer per gestire in particolare i turni dei giocatori artificiali e dealer
	public void gameLoop() 
	{
		Timer timer = new Timer(800, new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//finche lo stato non passa a GAME_OVER il timer continua
				//a seconda dello stato viene chiamato un metodo del controller 
				//che gestisce la logica dei turni
				if (currentState != GameState.GAME_OVER) 
				{
					switch (currentState) 
					{
					case BETTING:
						controller.handleBetting();
						currentState = GameState.DEALING_CARDS;
						break;
					case DEALING_CARDS:
						controller.dealCards();
						currentState = GameState.PLAYER_DECISION;
						break;
					case PLAYER_DECISION:
						controller.handleDecision();
						currentState = GameState.PLAYER_TURN;
						break;
					case PLAYER_TURN:
						controller.handlePlayerTurn();
						break;
					case AP_TURN:
						controller.handleAPTurn();
						break;
					case DEALER_TURN:
						controller.handleDealerTurn();
						break;
					case HANDLE_SPLIT:
						controller.handleSplit();
						currentState = GameState.PLAYER_TURN;
						break;
					case ROUND_END:
						currentState = GameState.GAME_OVER;
						break;
					default:
						break;
					}
				} else 
				{   //quando si passa a GAME_OVER il timer viene fermato e si procede a girare le carte e decidere i vincitori
					((Timer) e.getSource()).stop();
					controller.finishGame();
					controller.win();
				}
			}
		});
		timer.start();
	}

	//metodo per modificare lo stato di gioco
	public void setState(GameState newState) 
	{
		this.currentState = newState;
	}

}
