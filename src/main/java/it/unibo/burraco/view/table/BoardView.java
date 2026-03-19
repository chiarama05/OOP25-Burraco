package it.unibo.burraco.view.table;

import javax.swing.*;
import java.awt.*;

public class BoardView extends JPanel {
    private final JPanel combPanel1;
    private final JPanel combPanel2;

    public BoardView(String nameP1, String nameP2, Color lightgreen) {
        this.setLayout(new GridLayout(1, 2, 0, 0)); 
        this.setBackground(lightgreen);

        this.combPanel1 = createSection(nameP1);
        this.combPanel2 = createSection(nameP2);

        this.add(createScrollWrapper(combPanel1, nameP1, lightgreen));
        this.add(createScrollWrapper(combPanel2, nameP2, lightgreen));
    }

    private JPanel createSection(String title) {
        JPanel p = new JPanel(new GridBagLayout()); 
        p.setBackground(new Color(0, 102, 51)); 
        
        return p;
    }

    private JScrollPane createScrollWrapper(JPanel panel, String playerName, Color tableBg) {
        JPanel aligner = new JPanel(new BorderLayout());
        aligner.setBackground(panel.getBackground());
        aligner.add(panel, BorderLayout.NORTH);

        JScrollPane s = new JScrollPane(aligner);
        
       
        var titledBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2), 
            playerName, 
            0, 0, 
            new Font("Arial", Font.BOLD, 20), 
            Color.BLACK
        );

        
        s.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10), 
            titledBorder                                    
        ));

        s.getViewport().setBackground(panel.getBackground());
        s.setBackground(tableBg); 
        s.setOpaque(false);
        s.getVerticalScrollBar().setUnitIncrement(16);
        s.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            titledBorder
        ));
        
        return s;
    }

    public JPanel getCombPanel1() { return combPanel1; }
    public JPanel getCombPanel2() { return combPanel2; }
}
