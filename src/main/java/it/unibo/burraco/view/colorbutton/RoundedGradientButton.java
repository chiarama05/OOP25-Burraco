package it.unibo.burraco.view.colorbutton;

import javax.swing.JButton;
import java.awt.*;
import java.awt.geom.Point2D;

public class RoundedGradientButton extends JButton{

    private Color outerColor = new Color(255, 170, 185); 
    private Color innerColor = new Color(255, 245, 250); 

    private final Color originalOuter = outerColor;
    private final Color originalInner = innerColor;

    private final Color hoverOuter = new Color(255, 140, 160);
    private final Color hoverInner = new Color(255, 220, 230);

    public RoundedGradientButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                outerColor = hoverOuter;
                innerColor = hoverInner;
                repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                outerColor = originalOuter;
                innerColor = originalInner;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        float radius = Math.max(getWidth(), getHeight());
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {innerColor, outerColor}; 
        
        RadialGradientPaint rgp = new RadialGradientPaint(
            new Point2D.Double(getWidth() / 2.0, getHeight() / 2.0), 
            radius / 1.5f, 
            dist, 
            colors
        );

        g2.setPaint(rgp);
        
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        g2.setColor(new Color(200, 130, 145)); 
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);

        g2.dispose();
        super.paintComponent(g);
    }

}
