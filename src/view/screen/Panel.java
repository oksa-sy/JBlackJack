package view.screen;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

//classe astratta che estende JPanel e contiente metodi inerenti alla creazione e personalizzazione
//di pulsanti e label
public abstract class Panel extends JPanel{
	
	protected JLabel mainBackgroundLabel;

	public Panel()
	{
        setLayout(null);
        setBounds(0, 0, 1045, 783);
	}
	
	//metodo astratto per inizializzare la grafica del pannello
    public abstract void initialize();

    //creazione label
    public JLabel createImageLabel(String path, int x, int y, int width, int height) 
    {
        ImageIcon icon = new ImageIcon(path);
        JLabel label = new JLabel(icon);
        label.setBounds(x, y, width, height);
        return label;
    }
    
    //creazione label
    public JLabel createLabel(String text, Color c, int font, int size, int x, int y) 
    {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", font, size));
        label.setForeground(c);
        label.setBounds(x, y, 500, 60);
        return label;
    }
    
    //creazione pulsante, casi divisi in base a se si ha un immagine o meno
    public JButton createButton(String path, String text, int x, int y, int width, int height) 
    {
    	JButton button;
    	if (path != null)
    	{
    		button = new JButton(new ImageIcon(path));
    		button.setBounds(x, y, width, height);
    		button.setBorderPainted(false);
    		button.setContentAreaFilled(false);
    	}else 
    	{
    		button =  new JButton(text);
    		customizeButton(button, Color.RED, Color.BLACK, new Font("Arial", Font.BOLD, 22));
    		button.setBounds(x, y, width, height);
    	}
        return button;
    }
    
    //personalizzazione dei pulsanti
    public void customizeButton(JButton button, Color bgColor, Color fgColor, Font font) 
    {
        button.setFont(font);
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setOpaque(true);
    }
}
