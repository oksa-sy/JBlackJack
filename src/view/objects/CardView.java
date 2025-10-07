package view.objects;

import javax.swing.*;

import control.AudioManager;
import model.objects.Card;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

//view della carta, osserva la carta 
@SuppressWarnings("deprecation")
public class CardView extends JPanel implements Observer {

    //gestisce l'animaione del girare le carte 
    private Timer flipTimer; //timer dell'animazione flip
    private boolean flipping = false; //controlla se l'animazione è in corso 
    private int flipStep = 0; //passo per modificare dimensione immagine
    private Image currentImage; //immageine iniziale
    private Image newImage; //nuova immagine della carta una volta girata
    
    private Timer hitTimer; //timer animazione pesca
    private boolean moving = false; //controlla se l'animazione è in corso
    private boolean isStopped; //controlla se è arrivata al punto di arrivo
    private int x; //asse x sul quale si sposta la carta
    private final int WIDTH = 96; //grandezza carta 
    private final int HEIGHT = 140;
    private final int ANIMATION_SPEED = 5; //velocita di animazione (frequenza con cui l'animazione viene aggiornata)
    
    //costruttore
    public CardView(String path) 
    {
        setCardSize(WIDTH, HEIGHT); 
        setDoubleBuffered(true); //aumenta le prestazioni grafiche 
        currentImage = new ImageIcon(path).getImage(); //accede all'immagine attuale
        setOpaque(false);
    }

    //viene chiamato quando la carta viene girata, modifica l'immagine e inizia l'animazione
    //inoltre inizia un audio che simula il suono di una carta girata
    @Override
    public void update(Observable o, Object arg) 
    {
        if (o instanceof Card) 
        {
        	Card c = (Card) o;
            newImage = new ImageIcon(c.getImagePath()).getImage();
            startFlipAnimation();
            AudioManager audio = AudioManager.getInstance();
            audio.play("audio\\flipcard.wav");
        }
    }

    //animazione per girare la carta 
    public void startFlipAnimation() 
    {
        if (flipping || moving) return; //se la carta ha gia un'animazione in corso non ne parte un'altra 
        flipping = true; //inizio animazione
        flipStep = 0; 

        flipTimer = new Timer(60, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
                flipStep++; //incrememto flipStep

                //inizialmemte viene ridotta la larghezza 
                if (flipStep <= 5) 
                {
                    double scaleFactor = 1.0 - (0.2 * flipStep);
                    setCardSize((int) (96 * scaleFactor), 140);
                }
                //alla larghezza minima viene modificata l'immagine 
                else if (flipStep == 6) {currentImage = newImage;}
                //l'immagine torna alla sua dimensione iniziale
                else if (flipStep <= 10) 
                {
                    double scaleFactor = 0.2 * (flipStep - 5);
                    setCardSize((int) (96 * scaleFactor), 140);
                }
                //fine animazione
                else 
                {
                    flipTimer.stop();
                    flipping = false;
                    setCardSize(96, 140);
                }
                revalidate();
                repaint();
            }
        });
        flipTimer.start();   
    }

    public void startHitAnimation() 
    {
         isStopped = false;
         if (!moving && !flipping)
         {
            //inizalmente la carta è esterna 
            x = 260;

            hitTimer = new Timer(ANIMATION_SPEED, new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    if (!isStopped) {
                        x -= 10; //sposta la x verso sinistra
                        if (x == 0) 
                        {
                        	isStopped = true; //quando arriva a zero si ferma
                        	hitTimer.stop();
                        } 
                        repaint(); //chiama paintComponent
                    }
                }
            });
            hitTimer.start();
         }
    }   

    //modifica la dimesione della carta
    public void setCardSize(int width, int height) 
    {
        setPreferredSize(new Dimension(width, height));
        revalidate();
    }

    //disegna l'immagine della carta 
    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        g.drawImage(currentImage, x, 0, getWidth(), getHeight(), this);
    }
}

