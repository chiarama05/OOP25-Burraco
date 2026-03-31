package it.unibo.burraco.view.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Visual component representing the board area of the game table.
 * It displays the combination panels for both players side by side.
 */
public final class BoardView extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int GRID_HGAP = 20;
    private static final int GRID_VGAP = 10;
    private static final int FONT_SIZE = 20;
    private static final int SECTION_BG_G = 102;
    private static final int SECTION_BG_B = 51;
    private static final String FONT_NAME = "Arial";
 
    private final JPanel combPanel1;
    private final JPanel combPanel2;

     /**
     * Constructs the board view with combination panels for both players.
     *
     * @param nameP1     the display name for Player 1
     * @param nameP2     the display name for Player 2
     * @param lightgreen the background color for the board
     */
    public BoardView(final String nameP1, final  String nameP2, final Color lightgreen) {
        this.setLayout(new GridLayout(1, 2, GRID_HGAP, GRID_VGAP));
        this.setBackground(lightgreen);
 
        this.combPanel1 = this.createSection(nameP1);
        this.combPanel2 = this.createSection(nameP2);
 
        this.add(this.createScrollWrapper(this.combPanel1, lightgreen));
        this.add(this.createScrollWrapper(this.combPanel2, lightgreen));
    }

    /**
     * Helper method to create a player section with a titled border.
     * 
     * @param title the name of the player to display on the border
     * @return a styled JPanel for combinations
     */
    private JPanel createSection(final String title) {
        final JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setAlignmentY(Component.TOP_ALIGNMENT);
        p.setBackground(new Color(0, SECTION_BG_G, SECTION_BG_B));
        p.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE),
            title, 0, 0,
            new Font(FONT_NAME, Font.BOLD, FONT_SIZE), Color.WHITE));
        return p;
    }

    /**
     * Wraps a panel into a JScrollPane with transparent-like background.
     * 
     * @param panel the panel to wrap
     * @param bg the background color to apply to the viewport
     * @return a JScrollPane containing the panel
     */
    private JScrollPane createScrollWrapper(final JPanel panel, final Color bg) {
        final JScrollPane s = new JScrollPane(panel);
        s.setBorder(BorderFactory.createEmptyBorder());
        s.getViewport().setBackground(bg);
        s.setBackground(bg);
        return s;
    }
    
    /**
     * Returns the combination panel for Player 1.
     *
     * @return the combination panel for Player 1
     */
    public JPanel getCombPanel1() {
        return combPanel1;
    }
 
    /**
     * Returns the combination panel for Player 2.
     *
     * @return the combination panel for Player 2
     */
    public JPanel getCombPanel2() {
        return combPanel2;
    }
}
