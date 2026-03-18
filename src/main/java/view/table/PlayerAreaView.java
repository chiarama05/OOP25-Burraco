package view.table;

import javax.swing.*;
import java.awt.*;

public class PlayerAreaView extends JPanel {

    public PlayerAreaView(JComponent discardComponent, JPanel deckView, JPanel deckPanel, Color lightgreen) {
        this.setLayout(new BorderLayout());
        this.setBackground(lightgreen);

       
        JPanel centralBottomPanel = new JPanel(new BorderLayout());
        centralBottomPanel.setBackground(lightgreen);

        centralBottomPanel.add(discardComponent, BorderLayout.CENTER);
        centralBottomPanel.add(deckView, BorderLayout.WEST);

        
        this.add(centralBottomPanel, BorderLayout.NORTH);
        this.add(deckPanel, BorderLayout.CENTER); 
    }
}
