package model.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import model.objects.Card;
import model.objects.Deck;

@SuppressWarnings("deprecation")
public abstract class Entity extends Observable{

	//campi
	protected List<Card> hand;
	protected boolean stand;
	
	//costruttore
	public Entity()
	{
		this.hand = new ArrayList<>();
	}
	
	//pescare dal mazzo
	public void hit(Deck d)
	{
    	Card c = d.draw();
    	getCard(c);
	}
	
	//aggiunge la carta pescata alla mano del giocatore e avvisa gli osservatori per aggiornare la vista
	public void getCard(Card c)
	{
		hand.add(c);
		if (hand.size() == 1) {c.setIsFaceUp(true);}
        setChanged();
        notifyObservers(c); 
	}
	
	//calcola il valore della mano
	public int handValue() {
	    int totalValue = 0;
	    int numberOfAces = 0;

	    // Calcola il valore iniziale della mano e quanti assi ci sono
	    for (Card card : hand) {
	        int cardValue = card.getValue().getNumericValue();

	        if (cardValue == 11) {
	            numberOfAces++;
	        }
	        totalValue += cardValue;
	    }
	    // Aggiusta il valore se il totale supera 21 e ci sono degli assi
	    while (totalValue > 21 && numberOfAces > 0) {
	        totalValue -= 10;
	        numberOfAces--;
	    }
	    return totalValue;
	}
	
	//controlla se si ha superato 21
	public boolean exceededMaxValue()
	{
		if (handValue() > 21) 
		{
			stand = true;
			return true;
		}
		else return false;

	}
	
	//controlla se si ha fatto blackjack
	public boolean blackJack()
	{
		if (handValue() == 21) 
		{
			stand = true;
			return true;
		}
		else return false;
	}
	
	//resetta lo stato attuale per il nuovo round
    public void resetForNewRound()
    {
    	clearHand();
    	stand = false;
    }
	
    //elimina gli elementi della mano attuale e avvisa gli osservatori
    public void clearHand()
	{
		hand.clear();
		String event = "CLEAR";
        setChanged();
        notifyObservers(event);
	}
    
    //getter e setter 
	public List<Card> getHand() 
	{
		return hand;
	}

	public boolean getStand()
	{
		return stand;
	}
	
	public void setStand(boolean s)
	{
		this.stand = s;
	}
	

}


