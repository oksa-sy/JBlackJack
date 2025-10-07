package view.entity;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;

//estende EntityView e osserva il giocatore 
public class PlayerView extends EntityView{

	//costruttore, il panel del Player è piazzato in basso a destra
	public PlayerView() 
	{
		setBounds(650, 560, 250, 180);
	}
	
	//override del metodo che crea la label dedicata ai punti. I punti del giocatore
	//restano sempre visibili
	@Override
    public void createTotPointsLabel()
    {
        totPoints = new JLabel();
        totPoints.setFont(new Font("Arial", Font.BOLD, 30));
        totPoints.setForeground(Color.BLACK);
        totPoints.setBounds(0, 140, 40, 30);
        totPoints.setVisible(true);
        add(totPoints);
    }
	
	//ovverride del metodo che "resetta" il cardPanel, a differenza di EntityView non nasconde i punti
	@Override
	public void updatePanel() 
	{
	    cardPanel.removeAll();
	    revalidate();
	    repaint();
	}

}
