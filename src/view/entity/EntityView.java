package view.entity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import control.AudioManager;
import model.objects.Card;
import model.players.Entity;
import view.objects.CardView;
import view.screen.OverlapLayout;

//classe abstract che definische i panel dei giocatori, implementa observer e si aggiorna in base alla mano del giocatore
@SuppressWarnings("deprecation")
public abstract class EntityView extends JPanel implements Observer{

    protected CardView currentCard; //carta pescata 
    protected JLabel totPoints; //punti totali della mano
    protected JPanel cardPanel; //panel sul quale vengono inserite le carte

    //costruttore
    public EntityView() 
    {
    	setOpaque(false);
    	createCardPanel();
    	add(cardPanel);
    	createTotPointsLabel();
    }
    
    //creazione panel per le carte, applichiamo un layout per dare l'effetto di carta sovrapposta
    public void createCardPanel()
    {
    	cardPanel = new JPanel();
    	cardPanel.setPreferredSize(new Dimension(250, 140));
    	cardPanel.setOpaque(false);
    	cardPanel.setBounds(0,0, 250, 140);
    	OverlapLayout eLayout = new OverlapLayout(30);
    	cardPanel.setLayout(eLayout);
    }
    
    //creazione label rappresentante i punti della mano, inizialmente non è visibile per nascondere al Player
    //i punteggi degli altri giocatori
    public void createTotPointsLabel()
    {
        totPoints = new JLabel();
        totPoints.setFont(new Font("Arial", Font.BOLD, 30));
        totPoints.setForeground(Color.BLACK);
        totPoints.setBounds(0, 140, 40, 30);
        totPoints.setVisible(false);
        add(totPoints);
    }
    
    //modifica il panel e rimuove le carte in preparazione ad un nuovo round
	public void updatePanel() 
	{
	    cardPanel.removeAll();
	    totPoints.setVisible(false);
	    revalidate();
	    repaint();
	}

	//metodo che aggiorna la vista in risposta ai cambiamenti dei giocatori
    @Override
    public void update(Observable o, Object arg) 
    {
    	//se l'argomento passato dal giocatore è una stringa e uguale a CLEAR, si eliminano gli elementi sul panel
    	//in preparazione al nuovo round
    	if (arg instanceof String)
    	{
    		String event = (String) arg;
    		if (event.equals("CLEAR")) {updatePanel();}
    	}
    	//se l'argomento è una carta(il giocatore ha pescato/sono state distribuite le carte) parte l'animazione della carta
    	//che viene aggiunta al cardPanel, inoltre viene aggiornato il valore dei punti e fatto partire l'audio 
    	//per simulare il rumore di pescata
    	else if (arg instanceof Card) 
        {
    		Entity e = (Entity) o;
            Card newCard = (Card) arg;
            currentCard = new CardView(newCard.getImagePath());
            newCard.addObserver(currentCard);
            cardPanel.add(currentCard);
            currentCard.startHitAnimation();
            totPoints.setText("tot: " + e.handValue());
            revalidate(); 
            repaint();
            AudioManager audio = AudioManager.getInstance();
            audio.play("audio\\card.wav");
        }
    }
    
    //getter
    public JLabel getTotPoints()
    {
    	return totPoints;
    }
    
    public JPanel getCardPanel()
    {
    	return cardPanel;
    }
    

    
    
}

