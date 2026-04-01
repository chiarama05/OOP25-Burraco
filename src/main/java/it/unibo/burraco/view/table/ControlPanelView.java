package it.unibo.burraco.view.table;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Control panel containing action buttons for the game table.
 * It provides a vertical layout with custom-styled buttons.
 */
public final class ControlPanelView extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int PANEL_WIDTH = 180;
    private static final int PANEL_HEIGHT = 400;
    private static final int BTN_MAX_WIDTH = 170;
    private static final int BTN_MAX_HEIGHT = 45;
    private static final int STRUT_HEIGHT = 10;
    private static final int FONT_SIZE = 14;
    private static final String FONT_NAME = "Arial";

    private static final Color PINK_UP = new Color(255, 245, 250);
    private static final Color PINK_DOWN = new Color(255, 220, 235);
    private static final Color HOVER_COLOR = new Color(255, 180, 200);
    private static final Color BORDER_COLOR = new Color(230, 200, 215);

    private final JButton takeDiscardBtn;
    private final JButton putComboBtn;
    private final JButton discardBtn;

    /**
     * Constructs the control panel with the specified action buttons.
     *
     * @param takeDiscardBtn the button to take the discard pile
     * @param putComboBtn    the button to place a combination
     * @param discardBtn     the button to discard a card
     * @param backgroundColor the background color of the panel
     */
    public ControlPanelView(
            final JButton takeDiscardBtn,
            final JButton putComboBtn,
            final JButton discardBtn,
            final Color lightgreen) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(lightgreen);

        this.takeDiscardBtn = takeDiscardBtn;
        this.putComboBtn = putComboBtn;
        this.discardBtn = discardBtn;

        for (final JButton b : new JButton[]{this.takeDiscardBtn, this.putComboBtn, this.discardBtn}) {
            b.setAlignmentX(CENTER_ALIGNMENT);
            b.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE));
            b.setMaximumSize(new Dimension(BTN_MAX_WIDTH, BTN_MAX_HEIGHT));
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
            b.setContentAreaFilled(false);

            final boolean[] hoverStatus = {false};

            b.setUI(new BasicButtonUI() {
                @Override
                public void paint(final Graphics g, final JComponent c) {
                    final Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    final Color c1 = hoverStatus[0] ? PINK_DOWN : PINK_UP;
                    final Color c2 = hoverStatus[0] ? HOVER_COLOR : PINK_DOWN;
                    final GradientPaint gp = new GradientPaint(0, 0, c1, 0, c.getHeight(), c2);
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, c.getWidth(), c.getHeight());
                    g2.dispose();

                    super.paint(g, c);
                }
            });

            b.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(final MouseEvent e) {
                    hoverStatus[0] = true;
                    b.repaint();
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                    hoverStatus[0] = false;
                    b.repaint();
                }
            });

            this.add(b);
            this.add(Box.createVerticalStrut(STRUT_HEIGHT));
        }
    }
}
