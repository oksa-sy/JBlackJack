package view.animations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextAnimation extends JPanel {

    private JLabel label; //label da animare
    private Timer timer; //timer dell'animazione
    private int size = 40; //dimensione iniziale
    private boolean zoomingIn = true; //se ingrandisce o diminuisce

    public TextAnimation(String message) 
    {
        label = new JLabel(message);
        label.setFont(new Font("Serif", Font.BOLD, size));
        label.setForeground(Color.RED);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        setOpaque(false);
        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);

        timer = new Timer(80, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	//aumenta dimensione font fino a dimensione massima
                if (zoomingIn) 
                {
                    size += 2;
                    if (size >= 60) {zoomingIn = false;}
                } else //arrivato a dimensione massima diminusce la dimensione del font
                {
                    size -= 2;
                    if (size <= 40) {zoomingIn = true;}
                }
                //modifica la dimensione e aggiorna il panel
                label.setFont(new Font("Serif", Font.BOLD, size));
                revalidate();
                repaint();
            }
        });
    }
    
    //getter timer per farlo partire e fermarlo
    public Timer getTimer()
    {
    	return timer;
    }
}
