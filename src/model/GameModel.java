package model;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import model.objects.Deck;
import model.players.ArtificialPlayer;
import model.players.Dealer;
import model.players.Entity;
import model.players.Player;

//model di gioco
public class GameModel {

	private Player player; 
	private Dealer dealer;
	private List<ArtificialPlayer> totPlayers;
	private boolean isPlayerWinner; //controlla se il player vince 
	private int frequency; //controlla quante volte ha vinto in caso di split
	
	public GameModel(Player p, Dealer d, List<ArtificialPlayer> tPlayers)
	{
		this.player = p;
		this.dealer = d;
		this.totPlayers = tPlayers;
	}
	
	//scommessa da parte dei giocatori artificiali
	//si ritorna la somma delle scommesse totali
    public int betting(int numberOfPlayers) 
    {
        int totBetAmount = 0;
        Random random = new Random();
        if (numberOfPlayers == 1) {totBetAmount += player.getBet() * 2;} 
        else 
        {
            totBetAmount += totPlayers.stream()
                                      .mapToInt(aP -> {
                                          int bet = random.nextInt(201);
                                          aP.placeBet(bet);
                                          return bet;
                                      })
                                      .sum();
            totBetAmount += player.getBet();
        }
        return totBetAmount; 
    }
    
    //distribuzione iniziale di due carte
    public void deal(Deck deck) 
    {
    	dealer.hit(deck);
    	dealer.hit(deck);
    	
        player.hit(deck);
        player.hit(deck);

        totPlayers.forEach(aP -> {
            aP.hit(deck);
            aP.hit(deck);
        });
    }
    
    //una volta terminato il gioco si girano le carte e se il player aveva fatto l'assicurazione e le condizioni
    //sono verificate il giocatore riottiene i soldi scommessi
    public void finish(int numberOfPlayers)
    {
    	for (int i = 1; i < dealer.getHand().size(); i++)
    	{
    		dealer.getHand().get(i).flip();
    	}

    	if (player.getInsurance()) {player.insurance(dealer);}
    	if (numberOfPlayers > 1)
    	{
    		for (ArtificialPlayer aP : totPlayers)
    		{
	    		for (int j = 1; j < aP.getHand().size(); j++) {aP.getHand().get(j).flip();}
    		}
    	}
    }
    
    //si controlla chi è il vincitore
    public void endGame(int totBetAmount) {
        //si fa una lista di tutti i giocatori inclusi player e dealer
        List<Entity> allPlayers = Stream.concat(totPlayers.stream(), Stream.of(player, dealer))
                                        .distinct()
                                        .collect(Collectors.toList());

        //trova il punteggio massimo <= di 21
        int maxPoint = allPlayers.stream()
                                 .mapToInt(e -> e.handValue()) 
                                 .filter(score -> score <= 21) 
                                 .max()
                                 .orElse(-1); //se nessun punteggio è valido, restituisce -1

        //lista dei giocatori con punteggio valido
        List<Entity> winners = allPlayers.stream()
                                         .filter(e -> e.handValue() == maxPoint)
                                         .collect(Collectors.toList());

        //se il giocatore ha fatto lo split e il primo valore ottenuto è un punteggio valido viene inserito nella lista dei vincitori
        if (player.hasSplitHand() && player.getFirstHandValue() >= maxPoint && player.getFirstHandValue() <= 21) {winners.add(player);}
        isPlayerWinner = winners.contains(player); //controlla se la lista contiene il giocatore
        frequency = Collections.frequency(winners, player); //controlla quante volte il giocatore è nella lista
        setPlayerGame(isPlayerWinner, frequency, totBetAmount); //si inseriscono i dati in base ai risultati
        
    }
    
	public void setPlayerGame(boolean isPlayerWinner, int frequency, int totBetAmount)
	{
		//se il giocatore è vincitore si aumenta i giochi vinti in base alla frequenza(massimo 2 volte se split)
		if (isPlayerWinner) 
		{
			player.setWonGames(frequency);
			//in caso di split se se vince con entrambi le mani o si vince senza split da la somma completa di scommessa
			//se con lo splitsi vince con una sola mano si da la meta dell'importo 
			player.addMoney((frequency > 1 && player.hasSplitHand()) || (frequency == 1 && !player.isFirstWin())? totBetAmount : totBetAmount / 2);
		}
		//altrimenti si inserisce nei giochi persi in base a se si ha split o meno
		else {player.setLostGames(player.hasSplitHand() ? 2 : 1);}
		player.setGamesPlayed(); //partite giocate totali
	}
	
	//si resetta il model per il nuovo turno
	public void reset() 
	{
		if (player.getMoney() <= 0) {player.addMoney(1000);}
	    dealer.resetForNewRound();
	    player.resetForNewRound(); 
	    totPlayers.forEach(aP -> aP.resetForNewRound());
	}
	
	//getter
	public int getFrequency()
	{
		return frequency;
	}
	
	public boolean getIsPlayerWinner()
	{
		return isPlayerWinner;
	}
    
}
