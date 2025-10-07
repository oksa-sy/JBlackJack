package model.objects;

import java.util.Observable;

@SuppressWarnings("deprecation")
public class Card extends Observable{
	
	//campi
	private Suit suit;
	private Value value;
	private String frontImagePath;
	private String backImagePath;
	private boolean isFaceUp;
	
	//enum per le figure
	public enum Suit
	{
		HEARTS, DIAMONDS, SPADES, CLUBS
	}
	
	//enum per i valori
    public enum Value 
    {
        ACE(11), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10);

        private final int numericValue;

        Value(int numericValue) {
            this.numericValue = numericValue;
        }
        
        //ottenere il valore numerico
        public int getNumericValue() {
            return numericValue;
        }
    }

    //costruttore
	public Card(Suit suit, Value value)
	{
		isFaceUp = false;
		this.suit = suit;
		this.value = value;
		frontImagePath = "sprite\\cards\\" + value.getNumericValue() + "-" + suit.toString().charAt(0) + ".png";
		backImagePath = "sprite\\cards\\BACK.png";
	}
	
	//metodo per girare la carta, avvisa gli osservatori che la carta è stata girata
	public void flip() 
    {
        isFaceUp = !isFaceUp;
        setChanged();
        notifyObservers();  
    }
	
	//getter e setter
	public Suit getSuit()
	{
		return suit;
	}
	
	public Value getValue()
	{
		return value;
	}
	
	public String getFrontImagePath() {
		return frontImagePath;
	}

	public String getBackImagePath() {
		return backImagePath;
	}
	
	public boolean getIsFaceUp()
	{
		return isFaceUp;
	}
	
	public void setIsFaceUp(boolean face)
	{
		this.isFaceUp = face;
	}
	
    public String getImagePath() 
    {
        return isFaceUp ? frontImagePath : backImagePath;
    }

}
