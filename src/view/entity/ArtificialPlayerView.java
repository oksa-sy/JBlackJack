package view.entity;

//estende EntityView e osserva un giocatore artificiale
public class ArtificialPlayerView extends EntityView{

	private int index; //il panel del giocatore è diverso al seconda del suo indice 
	
	//costruttore, chiama il costruttore superiore e piazza il panel a seconda dell'indice del giocatore 
	public ArtificialPlayerView(int index) 
	{
		super();
		this.index = index;
		placePanel();
	}
	
    public void placePanel()
    {
    	switch (index)
    	{
    	case 0: 
    		setBounds(10, 560, 250, 180); //angolo in basso a sinistra
    		break;
    	case 1:
    		setBounds(10, 10, 250, 180); //angolo in alto a sinistra
    		break;
    	case 2:
    		setBounds(650, 10, 250, 180); //angolo in alto a destra
    		break;
    	}
    }
}
