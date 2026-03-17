package view.button;

import javax.swing.JButton;
import java.awt.*;
import java.awt.geom.Point2D;

public class RoundedGradientButton extends JButton{

    // Colore più scuro per l'esterno (Rosa Polvere/Antico)
    private Color outerColor = new Color(255, 170, 185); 
    // Colore più chiaro per l'interno (Rosa quasi bianco)
    private Color innerColor = new Color(255, 245, 250); 

    public RoundedGradientButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        // Attiva l'antialiasing per bordi lisci
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Creiamo la sfumatura radiale (dal centro verso l'esterno)
        float radius = Math.max(getWidth(), getHeight());
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {innerColor, outerColor}; // Invertiti: chiaro al centro, scuro fuori
        
        RadialGradientPaint rgp = new RadialGradientPaint(
            new Point2D.Double(getWidth() / 2.0, getHeight() / 2.0), 
            radius / 1.5f, 
            dist, 
            colors
        );

        g2.setPaint(rgp);
        
        // Disegna il corpo del bottone
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        // Aggiungi un bordo sottile leggermente più scuro per definire il 3D
        g2.setColor(new Color(200, 130, 145)); 
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);

        g2.dispose();
        super.paintComponent(g);
    }

}
