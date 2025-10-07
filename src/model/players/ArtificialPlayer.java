package model.players;

import model.objects.Card;
import model.objects.Deck;

@SuppressWarnings("deprecation")
public class ArtificialPlayer extends Player{
  
	//scommessa, se i soldi stanno per finire, se ne aggiungono 1000
	@Override 
	public void placeBet(int betAmount)
	{
		if(money < 100) {money += 1000;}
		bet += betAmount;
		money -= betAmount;
	}
	
	//verifica se sta o pesca
	public void drawsOrStands(Deck d)
	{
		if (handValue() >= 17) {stand = true;}
		else hit(d);
	}
	
	@Override
	public void getCard(Card c)
	{
		hand.add(c);
		if (hand.size() == 1) {c.setIsFaceUp(true);}
        setChanged();
        notifyObservers(c); 
	}
	
    @Override
    public void resetForNewRound()
    {
    	clearHand();
    	setStand(false);
    	bet = 0;
        setChanged();
        notifyObservers();
    }
	
	
	
}
