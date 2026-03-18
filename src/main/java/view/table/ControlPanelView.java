package view.table;

import javax.swing.*;
import java.awt.*;

public class ControlPanelView extends JPanel {
    private final JButton takeDiscardBtn;
    private final JButton putComboBtn;
    private final JButton discardBtn;

    public ControlPanelView(JButton takeDiscardBtn, JButton putComboBtn, JButton discardBtn, Color lightgreen) {
        // --- QUESTA È LA TUA LOGICA ORIGINALE ---
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(180, 400));
        this.setBackground(lightgreen); 

        this.takeDiscardBtn = takeDiscardBtn;
        this.putComboBtn = putComboBtn;
        this.discardBtn = discardBtn;

        Color pinkUp = new Color(255, 245, 250); 
        Color pinkDown = new Color(255, 220, 235);

        // Il tuo ciclo for identico
        for (JButton b : new JButton[]{takeDiscardBtn, putComboBtn, discardBtn}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setFont(new Font("Arial", Font.BOLD, 14));
            b.setMaximumSize(new Dimension(170, 45));
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setBorder(BorderFactory.createLineBorder(new Color(230, 200, 215), 1));
            b.setContentAreaFilled(false);

            final boolean[] isHovered = {false};
            b.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                @Override
                public void paint(Graphics g, JComponent c) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    Color c1 = isHovered[0] ? pinkDown : pinkUp; 
                    Color c2 = isHovered[0] ? new Color(255, 180, 200) : pinkDown;
                    
                    GradientPaint gp = new GradientPaint(0, 0, c1, 0, c.getHeight(), c2);
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, c.getWidth(), c.getHeight()); 
                    g2.dispose();
                    super.paint(g, c);
                }
            });

            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    isHovered[0] = true;
                    b.repaint();
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    isHovered[0] = false;
                    b.repaint();
                }
            });

            this.add(b);
            this.add(Box.createVerticalStrut(10));
        }
    }
}