package model.players;

import model.objects.Deck;

public class Dealer extends Entity{

	//verifica se sta o pesca
	public void drawsOrStands(Deck d)
	{
		if (handValue() >= 17) {stand = true;}
		else hit(d);
	}
	
}
