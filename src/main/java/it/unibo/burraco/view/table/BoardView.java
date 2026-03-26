package it.unibo.burraco.view.table;

import javax.swing.*;
import java.awt.*;

public class BoardView extends JPanel {
    private final JPanel combPanel1;
    private final JPanel combPanel2;

    public BoardView(String nameP1, String nameP2, Color lightgreen) {
        this.setLayout(new GridLayout(1, 2, 20, 10));
        this.setBackground(lightgreen);

        this.combPanel1 = createSection(nameP1);
        this.combPanel2 = createSection(nameP2);

        this.add(createScrollWrapper(combPanel1, lightgreen));
        this.add(createScrollWrapper(combPanel2, lightgreen));
    }

    private JPanel createSection(String title) {
    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS)); 
    p.setAlignmentY(Component.TOP_ALIGNMENT);
    p.setBackground(new Color(0, 102, 51));
    p.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.WHITE),
        title, 0, 0, new Font("Arial", Font.BOLD, 20), Color.WHITE));
    return p;
}

    private JScrollPane createScrollWrapper(JPanel panel, Color bg) {
        JScrollPane s = new JScrollPane(panel);
        s.setBorder(BorderFactory.createEmptyBorder());
        s.getViewport().setBackground(bg);
        s.setBackground(bg);
        return s;
    }

    public JPanel getCombPanel1() { return combPanel1; }
    public JPanel getCombPanel2() { return combPanel2; }
}
