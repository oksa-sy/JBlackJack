package model.players;

import model.objects.Card;
import model.objects.Card.Value;

@SuppressWarnings("deprecation")
public class Player extends Entity {

    // Campi
    protected int bet;
    protected int money;
    
    private boolean insurance;
    private boolean splitAllowed;
    private Card splitHand; 
    private int lostGames;
    private int wonGames;
    private int gamesPlayed;
    private boolean firstRoundCompleted;
    private boolean firstWin;
    private int firstHandValue;
    
    // Costruttore
    public Player() 
    {
        super();
        money = 1000;
        bet = 0;
    }
    
    //override del metodo getCard di Entity, le carte del giocatore sono tutte face-up
    @Override
    public void getCard(Card c)
    {
    	hand.add(c);
		c.setIsFaceUp(true);
        setChanged();
        notifyObservers(c);
    }

    //scommessa
    public void placeBet(int betAmount) 
    {
        money -= betAmount;
        bet += betAmount;
        setChanged();
        notifyObservers();
    }
       
    //aggiungere soldi
    public void addMoney(int amount) 
    {
        money += amount;
        setChanged();
        notifyObservers();
    }
    
    //controllare se è possibile raddoppiare la scommessa
    public boolean doubleBetAllowed() 
    {
        for (Card card : hand) 
        {
            if ((card.getValue().getNumericValue() == 9 || card.getValue().getNumericValue() == 10 || 
                (card.getValue().getNumericValue() == 11)) && money > bet) {return true;}
        }
        return false;
    }

    //raddoppiare la scommessa
    public void doubleBet() 
    {
        money -= bet;
        bet *= 2;                
        setChanged();
        notifyObservers();
    }
    
    //controllare se è possibile chidere l'assicurazione
    public boolean insuranceAllowed(Dealer d) 
    {
        if (d.getHand().get(0).getValue().equals(Value.ACE)) {return true;}
        return false;
    }

    //assicurazione 
    public void insurance(Dealer d) 
    {
    	if (d.blackJack())
    	{
    		money += bet;
	        setChanged();
	        notifyObservers();
    	}
    }

    //controllare se è possibile lo split
    public boolean splitAllowed() 
    {
        if (hand.size() == 2 && hand.get(0).getValue().getNumericValue() == hand.get(1).getValue().getNumericValue() && money > bet * 2) {splitAllowed = true;}
        else splitAllowed = false;
        return splitAllowed;
    }
    
    //dividere la mano
    public void split() 
    {
    	splitHand = hand.remove(1);
        money -= bet;
        bet *= 2;
    }
    
    @Override
	public boolean blackJack()
	{
		if (handValue() == 21) 
		{
			stand = true;
	        setChanged();
	        notifyObservers("BLACKJACK");			
			return true;
		}
		else return false;
	}
    
    //preparare la seconda mano per lo split
    public void prepareNewHand() 
    {
        setFirstHandValue(handValue());
        clearHand();
        setStand(false);
    }

    @Override
    public void resetForNewRound()
    {
    	clearHand();
    	setStand(false);
    	insurance = false;
    	firstRoundCompleted = false;
    	firstWin = false;
    	splitHand = null;
    	bet = 0;
        setChanged();
        notifyObservers();
    }
    
    //stabilire il livello del giocatore 
    public Level getLevel() 
    {
        if (wonGames <= 10) {
            return Level.BEGINNER;
        } else if (wonGames <= 20) {
            return Level.AMATEUR;
        } else if (wonGames <= 30) {
            return Level.PROFESSIONAL;
        } else if (wonGames <= 50) {
            return Level.EXPERT;
        } else if (wonGames <= 60){
            return Level.ACE; 
        } else return Level.LEGEND;
    }

 // Getter e Setter
    public Card getSplitHand() { return splitHand; }

    public boolean hasSplitHand() { return splitHand != null; }

    public int getMoney() { return money; }

    public int getBet() { return bet; }

    public void setBet(int bet) { this.bet += bet; }

    public int getLostGames() { return lostGames; }

    public void setLostGames(int lostGames) 
    { 
    	this.lostGames += lostGames; 
    	setChanged(); 
    	notifyObservers();
    }

    public int getWonGames() { return wonGames; }

    public void setWonGames(int wonGames) 
    { 
    	this.wonGames += wonGames; 
    	setChanged(); 
    	notifyObservers(); 
    }

    public int getGamesPlayed() { return gamesPlayed; }

    public void setGamesPlayed() 
    { 
    	this.gamesPlayed = wonGames + lostGames; 
    	setChanged(); 
    	notifyObservers(); 
    }

    public boolean getInsurance() { return insurance; }

    public void setInsurance(boolean insurance) { this.insurance = insurance; }

    public boolean isFirstRoundCompleted() { return firstRoundCompleted; }

    public void setFirstRoundCompleted(boolean firstRoundCompleted) { this.firstRoundCompleted = firstRoundCompleted; }

    public boolean isFirstWin() { return firstWin; }

    public int getFirstHandValue() { return firstHandValue; }

    public void setFirstHandValue(int firstHandValue) { this.firstHandValue = firstHandValue; }

    

}
