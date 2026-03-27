package it.unibo.burraco.view.colorbutton;

import javax.swing.JButton;
import java.awt.*;
import java.awt.geom.Point2D;

/**
 * A custom Swing button with rounded corners and a radial gradient background.
 * It includes hover effects to enhance the user interface experience.
 */
public class RoundedGradientButton extends JButton{

    // Default color palette
    private Color outerColor = new Color(255, 170, 185); 
    private Color innerColor = new Color(255, 245, 250); 

    // Constants to store the original colors for resetting after hover
    private final Color originalOuter = outerColor;
    private final Color originalInner = innerColor;

    // Hover effect colors
    private final Color hoverOuter = new Color(255, 140, 160);
    private final Color hoverInner = new Color(255, 220, 230);

    /**
     * Constructs a new button with specified text and initializes transparency settings.
     * @param text the label displayed on the button.
     */
    public RoundedGradientButton(String text) {
        super(text);
        // Disable default Swing rendering to use custom painting
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);

        // Add mouse listener to handle hover state transitions
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                outerColor = hoverOuter;
                innerColor = hoverInner;
                repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                outerColor = originalOuter;
                innerColor = originalInner;
                repaint();
            }
        });
    }

    /**
     * Custom painting logic for the button's background and border.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable anti-aliasing for smooth rounded corners
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        float radius = Math.max(getWidth(), getHeight());
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {innerColor, outerColor}; 
        
        // Create a radial gradient centered in the button
        RadialGradientPaint rgp = new RadialGradientPaint(
            new Point2D.Double(getWidth() / 2.0, getHeight() / 2.0), 
            radius / 1.5f, 
            dist, 
            colors
        );

        // Draw the background
        g2.setPaint(rgp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        // Draw the border with a darker shade
        g2.setColor(new Color(200, 130, 145)); 
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);

        g2.dispose();

        // Render the button text and other standard components
        super.paintComponent(g);
    }
}
