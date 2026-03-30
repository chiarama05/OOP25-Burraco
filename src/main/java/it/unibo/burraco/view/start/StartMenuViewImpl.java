package it.unibo.burraco.view.start;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import it.unibo.burraco.view.colorbutton.RoundedGradientButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

/**
 * Swing-based implementation of the StartMenuView.
 * Provides the main entry point UI with access to game rules and match initialization.
 */
public class StartMenuViewImpl implements StartMenuView{

    private static final Color BG_COLOR = new Color(0, 102, 51);
    private static final Color TITLE_COLOR = new Color(255, 182, 193);
    private static final Color BTN_BG_COLOR = new Color(255, 240, 245);
    private static final Color RULES_BG_COLOR = new Color(180, 220, 180);

    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 450;
    private static final int INSET_GAP = 25;
    private static final int TITLE_SIZE = 55;
    private static final int BTN_FONT_SIZE = 18;
    private static final int BTN_WIDTH = 200;
    private static final int BTN_HEIGHT = 60;

    private static final int RULES_WIDTH = 600;
    private static final int RULES_HEIGHT = 650;
    private static final int RULES_FONT_SIZE = 14;
    private static final int RULES_MARGIN = 20;

    private final JFrame frame;
    private final OnGameStartListener listener;

    /**
     * @param listener callback handler for game start events.
     */
    public StartMenuViewImpl(final OnGameStartListener listener) {
        this.listener = listener;
        this.frame = new JFrame("Burraco Game - Home");
        setupUI();
    }

    /**
     * Initializes components, layout, and styling.
     */
    private void setupUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT); 
        frame.setLocationRelativeTo(null);

        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_COLOR);
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);

        final JLabel title = new JLabel("BURRACO GAME");
        title.setFont(new Font("Serif", Font.BOLD, TITLE_SIZE)); 
        title.setForeground(TITLE_COLOR);
        gbc.gridy = 0;
        panel.add(title, gbc);

        final RoundedGradientButton newBtn = new RoundedGradientButton("NEW MATCH");
        final RoundedGradientButton rulesBtn = new RoundedGradientButton("RULES");
        
        styleButton(newBtn);
        styleButton(rulesBtn);

        // New Match Logic
        newBtn.addActionListener(e -> {
            close();
            listener.onStartClicked();
        });
        gbc.gridy = 1;
        panel.add(newBtn, gbc);

        // Rules Logic
        rulesBtn.addActionListener(e -> showRules());
        gbc.gridy = 2;
        panel.add(rulesBtn, gbc);

        frame.add(panel);
    }

    /**
     * Standardizes button appearance.
     */
    private void styleButton(final JButton btn) {
        btn.setFont(new Font("Arial", Font.BOLD, BTN_FONT_SIZE));
        btn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
        btn.setBackground(BTN_BG_COLOR);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
    }

    /**
     * Displays a scrollable dialog containing the game rules.
     */
    private void showRules() {
        final StringBuilder rules = new StringBuilder();
        rules.append("BURRACO GAME RULES\n\n")
             .append("1. TURN STEPS\n")
             .append("• Drawing: Take a card from the Deck or all cards from the Discard Pile.\n")
             .append("• Melding: Open new Straight/Set or attach cards to existing ones.\n")
             .append("• Discarding: Discard one card to end your turn.\n\n")
             .append("2. OPENING RUNS\n")
             .append("• Straight: 3+ cards of the same suit in order.\n")
             .append("• Set: 3+ cards of the same rank.\n")
             .append("• You can attach cards only to your own Runs.\n\n")
             .append("3. WILD CARDS\n")
             .append("• You can use one Joker or one 'Pinella' (any 2) per Run.\n")
             .append("• Replacing a wild card moves it to a new position in the same sequence.\n\n")
             .append("4. BURRACO\n")
             .append("A sequence of at least 7 cards. Clean (no wild cards) or Dirty.\n\n")
             .append("5. THE POT\n")
             .append("Taking the 11 extra cards is mandatory to close the round.\n\n")
             .append("6. CLOSING\n")
             .append("Requires a Burraco, taking the Pot, and discarding a final natural card.");

        // Dialog configuration
        final JDialog rulesDialog = new JDialog(frame, "Rules of the Game", true);
        rulesDialog.setLayout(new BorderLayout());
        rulesDialog.setSize(RULES_WIDTH, RULES_HEIGHT);
        rulesDialog.setLocationRelativeTo(frame);

        // Text configuration
        final JTextArea textArea = new JTextArea(rules.toString());
        textArea.setFont(new Font("Arial", Font.BOLD, RULES_FONT_SIZE));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setForeground(Color.BLACK);

        // Container panel
        final JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(RULES_BG_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(RULES_MARGIN, RULES_MARGIN, RULES_MARGIN, RULES_MARGIN));

        // Scrollable area
        final JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Close Button
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        final RoundedGradientButton closeBtn = new RoundedGradientButton("CLOSE");
        closeBtn.setFont(new Font("Arial", Font.BOLD, BTN_FONT_SIZE - 2));
        closeBtn.setPreferredSize(new Dimension(120, 40));
        closeBtn.addActionListener(e -> rulesDialog.dispose());
        buttonPanel.add(closeBtn);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        rulesDialog.add(contentPanel, BorderLayout.CENTER);
        rulesDialog.setVisible(true);
    }

    @Override public void display() { 
        frame.setVisible(true); 
    }

    @Override public void close() { 
        frame.dispose(); 
    }
}

