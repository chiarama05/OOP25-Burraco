package it.unibo.burraco.view.start;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import it.unibo.burraco.view.colorbutton.RoundedGradientButton;

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
    frame.setSize(600, 450); 
    frame.setLocationRelativeTo(null);

    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(new Color(0, 102, 51));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(25, 25, 25, 25);

    JLabel title = new JLabel("BURRACO GAME");
    title.setFont(new Font("Serif", Font.BOLD, 55)); 
    title.setForeground(new Color(255, 182, 193));
    gbc.gridy = 0;
    panel.add(title, gbc);

    
    RoundedGradientButton newBtn = new RoundedGradientButton("NEW MATCH");
    RoundedGradientButton rulesBtn = new RoundedGradientButton("RULES");
    JButton[] buttons = {newBtn, rulesBtn};
    for (JButton btn : buttons) {
    btn.setFont(new Font("Arial", Font.BOLD, 18));
    btn.setPreferredSize(new Dimension(200, 60));
    btn.setBackground(new Color(255, 240, 245)); 
    btn.setForeground(Color.BLACK);
    btn.setFocusPainted(false);
    }
    newBtn.addActionListener(e -> {
        close();
        listener.onStartClicked();
    });
    gbc.gridy = 1;
    panel.add(newBtn, gbc);

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

