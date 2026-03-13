package view.start;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.*;

public class StartMenuViewImpl implements StartMenuView{

    private final JFrame frame;
    private final OnGameStartListener listener;

    public StartMenuViewImpl(OnGameStartListener listener) {
        this.listener = listener;
        this.frame = new JFrame("Burraco Game - Home");
        setupUI();
    }

    private void setupUI() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 450); // Larghezza aumentata a 600 come l'altro
    frame.setLocationRelativeTo(null);

    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(new Color(0, 102, 51));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(25, 25, 25, 25);

    JLabel title = new JLabel("BURRACO GAME");
    title.setFont(new Font("Serif", Font.BOLD, 55)); // Titolo più imponente
    title.setForeground(Color.YELLOW);
    gbc.gridy = 0;
    panel.add(title, gbc);

    JButton newBtn = new JButton("NEW MATCH");
    newBtn.setFont(new Font("Arial", Font.BOLD, 18));
    newBtn.setPreferredSize(new Dimension(200, 60)); // Pulsante più grande
    newBtn.addActionListener(e -> {
        close();
        listener.onStartClicked();
    });
    gbc.gridy = 1;
    panel.add(newBtn, gbc);

    JButton rulesBtn = new JButton("RULES");
    rulesBtn.setFont(new Font("Arial", Font.BOLD, 18));
    rulesBtn.setPreferredSize(new Dimension(200, 60)); // Pulsante più grande
    rulesBtn.addActionListener(e -> showRules());
    gbc.gridy = 2;
    panel.add(rulesBtn, gbc);

    frame.add(panel);
}

    private void showRules() {
    String message = "BURRACO GAME RULES\n\n" +
            "1. TURN STEPS\n" +
            "• Drawing: Take a card from the Deck or all cards from the Discard Pile.\n" +
            "• Melding: Open new Straight/Set or attach cards to existing ones.\n" +
            "• Discarding: Discard one card to end your turn.\n\n" +
            "2. OPENING RUNS\n" +
            "• Straight: 3+ cards of the same suit in order.\n" +
            "• Set: 3+ cards of the same rank.\n" +
            "• You can attach cards only to your own Runs.\n\n" +
            "3. WILD CARDS\n" +
            "• You can use one Joker or one 'Pinella' (any 2) per Run.\n" +
            "• If you replace a wild card with its natural card, it stays in the Run and takes on the role of another card.\n\n" +
            "4. BURRACO\n" +
            "A Straight/Set of at least 7 cards. It can be:\n" +
            "• CLEAN: No Joker or Pinella (except a 2 in its natural suit).\n" +
            "• DIRTY: Contains a Pinella or Joker within the sequence.\n\n" +
            "5. THE POT\n" +
            "When the player empty his hand gets 11 new cards.\n" +
            "Failure to take the Pot results in a 100-point penalty.\n\n" +
            "6. CLOSING\n" +
            "To win the round, you must:\n" +
            "• Have taken the Pot and created at least one Burraco.\n" +
            "• Empty your hand and discard a final card (not a Joker/Pinella).\n\n";

    JOptionPane.showMessageDialog(frame, message, "Rules of the Game", JOptionPane.INFORMATION_MESSAGE);
}

    @Override public void display() { 
        frame.setVisible(true); 
    }
    @Override public void close() { 
        frame.dispose(); 
    }
}

