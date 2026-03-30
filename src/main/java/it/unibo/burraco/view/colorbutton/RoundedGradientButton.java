package it.unibo.burraco.view.colorbutton;

import javax.swing.JButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A custom Swing button with rounded corners and a radial gradient background.
 * It includes hover effects to enhance the user interface experience.
 */
public final class RoundedGradientButton extends JButton {

    private static final long serialVersionUID = 1L;

    private static final Color DEFAULT_OUTER = new Color(255, 170, 185);
    private static final Color DEFAULT_INNER = new Color(255, 245, 250);
    private static final Color HOVER_OUTER = new Color(255, 140, 160);
    private static final Color HOVER_INNER = new Color(255, 220, 230);
    private static final Color BORDER_COLOR = new Color(200, 130, 145);
    
    private static final int ARC_SIZE = 25;
    private static final float STROKE_WIDTH = 1.5f;
    private static final float GRADIENT_RADIUS_RATIO = 1.5f;
    private static final float[] GRADIENT_DIST = {0.0f, 1.0f};

    private Color outerColor = DEFAULT_OUTER;
    private Color innerColor = DEFAULT_INNER;

    /**
     * Constructs a new button with specified text and initializes transparency settings.
     * @param text the label displayed on the button.
     */
    public RoundedGradientButton(final String text) {
        super(text);
        // Disable default Swing rendering to use custom painting
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);

        // Add mouse listener to handle hover state transitions
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent evt) { 
                innerColor = HOVER_INNER;
                outerColor = HOVER_OUTER;
                repaint();
            }

            @Override
            public void mouseExited(final MouseEvent evt) { 
                innerColor = DEFAULT_INNER;
                outerColor = DEFAULT_OUTER;
                repaint();
            }
        });
    }

    /**
     * Custom painting logic for the button's background and border.
     */
    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create();

        // Enable anti-aliasing for smooth rounded corners
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final float radius = Math.max(getWidth(), getHeight());
        final Color[] colors = {innerColor, outerColor}; 
        
        // Create a radial gradient centered in the button
        final RadialGradientPaint rgp = new RadialGradientPaint(
            new Point2D.Double(getWidth() / 2.0, getHeight() / 2.0),
            radius / GRADIENT_RADIUS_RATIO,
            GRADIENT_DIST,
            colors
        );

        // Draw the background
        g2.setPaint(rgp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC_SIZE, ARC_SIZE);

        // Draw the border with a darker shade
        g2.setColor(BORDER_COLOR);
        g2.setStroke(new BasicStroke(STROKE_WIDTH));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_SIZE, ARC_SIZE);

        g2.dispose();

        // Render the button text and other standard components
        super.paintComponent(g);
    }
}
