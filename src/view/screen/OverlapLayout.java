package view.screen;

import java.awt.*;

//layout personalizzato per collocare le carte 
public class OverlapLayout implements LayoutManager {
	
    private int overlap; //spazio tra le carte

    //costruttore
    public OverlapLayout(int overlap) 
    {
        this.overlap = overlap;
    }

    //metodi di Layout manager che devono essere integrati ma non necessari in questo caso
    @Override
    public void addLayoutComponent(String name, Component component) {}

    @Override
    public void removeLayoutComponent(Component component) {}

    //definire le dimensioni
    @Override
    public Dimension preferredLayoutSize(Container c) 
    {
    	//thread con lock per garantire che non ci siano interferenze 
        synchronized (c.getTreeLock()) 
        { 
            int width = 250;  //larghezza
            int height = 140; //altezza
            return new Dimension(width, height);
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container c) {return preferredLayoutSize(c);}

    //vengono inseriti gli elementi in base alla grandezza del container e dei component, inseriti con determinata distanza orizzontale
    @Override
    public void layoutContainer(Container c) 
    {
        synchronized (c.getTreeLock()) 
        {
            int x = 0;
            int y = 0;
            for (int i = 0; i < c.getComponentCount(); i++) 
            {
                Component comp = c.getComponent(i);
                Dimension d = comp.getPreferredSize();
                comp.setBounds(x, y, d.width, d.height);
                x += overlap;
            }
        }
    }
}


