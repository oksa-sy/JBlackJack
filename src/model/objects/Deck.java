package model.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck{
	
	//campo
	private List<Card> deck;
	
	//costruttore
	public Deck()
	{
        deck = new ArrayList<>();
        createDeck();
        shuffleDeck();
	}
	
	//creazione del mazzo, si aggiunge una carta per ogni valore e figura
	public void createDeck()
	{
		for (Card.Suit suit : Card.Suit.values()) 
		{
			for (Card.Value value : Card.Value.values()) 
			{
            deck.add(new Card(suit, value));
			}
		}
	}
	
	//metodo di Collections che permette di permutare in modo randomizzato gli elementi all'interno di una collection
	public void shuffleDeck()
	{
		Collections.shuffle(deck);
	}
	
	//pescare, prende la carta e la elimina dal mazzo, se vuoto inserisce nuovamente gli elementi e li mischia
	public Card draw()
	{
		if (deck.size() <= 0) 
		{
			createDeck();
			shuffleDeck();
		}
		Card c = deck.get(deck.size()-1);
		deck.remove(deck.size()-1);
		return c;
	}

}
