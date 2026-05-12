package it.unibo.burraco.view.scenes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import it.unibo.burraco.view.components.RoundedGradientButton;

/**
 * Swing-based implementation of the StartMenuView.
 * Provides the main entry point UI with access to game rules and match initialization.
 */
public final class StartMenuViewImpl implements StartMenuView {

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

    private static final int RULES_WIDTH = 700;
    private static final int RULES_HEIGHT = 750;
    private static final int RULES_FONT_SIZE = 14;
    private static final int RULES_MARGIN = 20;

    private static final int CLOSE_BTN_WIDTH = 120;
    private static final int CLOSE_BTN_HEIGHT = 40;
    private static final int CLOSE_BTN_FONT_SIZE = BTN_FONT_SIZE - 2;

    private static final String FONT_ARIAL = "Arial";
    private static final String FONT_SERIF = "Serif";

    private static final String RULES_TEXT =
        "BURRACO GAME RULES\n\n"
        + "1. SETUP\n"
        + "• Each player enters an optional name and chooses a target score to play up to.\n"
        + "• Player 1 is the one who takes the first turn.\n"
        + "• The player who reaches the target score first wins the game.\n\n"
        + "2. YOUR TURN\n"
        + "Each turn has three phases, in order:\n\n"
        + "  A) DRAW\n"
        + "     • Tap 'Draw' to take the top card from the Deck, OR\n"
        + "     • Tap 'Take Discard' to take all cards from the Discard Pile.\n\n"
        + "  B) MELD (optional)\n"
        + "     • To lay down a new Run: select 3 or more cards from your hand,\n"
        + "       then tap 'Put Combination'.\n"
        + "     • To extend an existing Run: select the cards you want to add,\n"
        + "       then tap on the Run you want to attach them to.\n"
        + "     • You may meld multiple times per turn.\n\n"
        + "  C) DISCARD (mandatory)\n"
        + "     • Select one card from your hand and tap 'Discard' to end your turn.\n\n"
        + "3. RUNS & WILD CARDS\n"
        + "STRAIGHT:\n"
        + "  • three or more cards of the same seed in consecutive order.\n"
        + "  • Can include one wild card (a Jolly or a 2).\n"
        + "  • A wild card is placed in the exact position of the card it replaces.\n"
        + "  • It can only be replaced by that specific natural card.\n"
        + "  • Once replaced, the wild card moves to the first free position at the top of the Straight.\n"
        + "  • A 2 can also be used as a natural card if placed next to an Ace or a 3 of the same seed.\n\n"
        + "SET:\n"
        + "  • three or more cards of the same rank.\n"
        + "  • Can include one wild card.\n\n"
        + "GENERAL RULE:\n"
        + "  • You can only attach cards to your own Runs, not your opponent's.\n\n"
        + "5. THE POT\n"
        + "• Each player has a Pot of 11 extra cards, taken once their initial hand runs out.\n"
        + "• You must take the Pot before you are allowed to close the round.\n"
        + "• However, you do not need to have a Burraco yet in order to take the Pot.\n"
        + "• If your hand runs out before discarding, you take the Pot on fly\n"
        + "  and continue your turn right away.\n"
        + "• If your hand runs out after discarding, you will receive the Pot\n"
        + "  at your next turn.\n\n"
        + "4. BURRACO & CLOSING\n"
        + "• The goal is to score as many points as possible and close the round.\n"
        + "• To close, you must have at least one Burraco, have taken the Pot,\n"
        + "  and discard your final card.\n\n"
        + "  BURRACO:\n"
        + "  • A Run of at least 7 cards.\n"
        + "  • Clean Burraco (no wild cards): +200 points.\n"
        + "  • Dirty Burraco: +100 points.\n\n"
        + "  SCORING:\n"
        + "  • +100 points for closing the round.\n"
        + "  • -100 points if you never took the Pot.\n"
        + "  • Cards left in hand are subtracted; cards melded on the table are added:\n"
        + "      2:        +20 pts\n"
        + "      Ace:      +15 pts\n"
        + "      8 to K:   +10 pts\n"
        + "      1 to 7:    +5 pts\n"
        + "      Joker:    +30 pts\n";

    private final JFrame frame;
    private final OnGameStartListener listener;

    /**
     * Constructs a StartMenuViewImpl.
     *
     * @param listener callback handler for game start events.
     */
    public StartMenuViewImpl(final OnGameStartListener listener) {
        this.listener = listener;
        this.frame = new JFrame("Burraco Game - Home");
        this.setupUI();
    }

    /**
     * Initializes components, layout, and styling.
     */
    private void setupUI() {
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.frame.setLocationRelativeTo(null);
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_COLOR);
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        final JLabel title = new JLabel("BURRACO GAME");
        title.setFont(new Font(FONT_SERIF, Font.BOLD, TITLE_SIZE));
        title.setForeground(TITLE_COLOR);
        gbc.gridy = 0;
        panel.add(title, gbc);
        final RoundedGradientButton newBtn = new RoundedGradientButton("NEW MATCH");
        final RoundedGradientButton rulesBtn = new RoundedGradientButton("RULES");
        this.styleButton(newBtn);
        this.styleButton(rulesBtn);
        newBtn.addActionListener(e -> {
            this.close();
            this.listener.onStartClicked();
        });
        gbc.gridy = 1;
        panel.add(newBtn, gbc);
        rulesBtn.addActionListener(e -> this.showRules());
        gbc.gridy = 2;
        panel.add(rulesBtn, gbc);
        this.frame.add(panel);
    }

    /**
     * Standardizes button appearance.
     *
     * @param btn the button to style
     */
    private void styleButton(final JButton btn) {
        btn.setFont(new Font(FONT_ARIAL, Font.BOLD, BTN_FONT_SIZE));
        btn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
        btn.setBackground(BTN_BG_COLOR);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
    }

    /**
     * Displays a scrollable dialog containing the game rules.
     */
    private void showRules() {
        final JDialog rulesDialog = new JDialog(this.frame, "Rules of the Game", true);
        rulesDialog.setLayout(new BorderLayout());
        rulesDialog.setSize(RULES_WIDTH, RULES_HEIGHT);
        rulesDialog.setLocationRelativeTo(this.frame);
        final JTextArea textArea = new JTextArea(RULES_TEXT);
        textArea.setFont(new Font(FONT_ARIAL, Font.BOLD, RULES_FONT_SIZE));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setForeground(Color.BLACK);
        final JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(RULES_BG_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(RULES_MARGIN, RULES_MARGIN, RULES_MARGIN, RULES_MARGIN));
        final JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        final RoundedGradientButton closeBtn = new RoundedGradientButton("CLOSE");
        closeBtn.setFont(new Font(FONT_ARIAL, Font.BOLD, CLOSE_BTN_FONT_SIZE));
        closeBtn.setPreferredSize(new Dimension(CLOSE_BTN_WIDTH, CLOSE_BTN_HEIGHT));
        closeBtn.addActionListener(e -> rulesDialog.dispose());
        buttonPanel.add(closeBtn);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        rulesDialog.add(contentPanel, BorderLayout.CENTER);
        rulesDialog.setVisible(true);
    }

    @Override
    public void display() {
        this.frame.setVisible(true);
    }

    @Override
    public void close() {
        this.frame.dispose();
    }
}
