package it.unibo.burraco.view.start;

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

import it.unibo.burraco.view.colorbutton.RoundedGradientButton;

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

    private static final int RULES_WIDTH = 600;
    private static final int RULES_HEIGHT = 650;
    private static final int RULES_FONT_SIZE = 14;
    private static final int RULES_MARGIN = 20;

    private static final int CLOSE_BTN_WIDTH = 120;
    private static final int CLOSE_BTN_HEIGHT = 40;
    private static final int CLOSE_BTN_FONT_SIZE = BTN_FONT_SIZE - 2;

    private static final String FONT_ARIAL = "Arial";
    private static final String FONT_SERIF = "Serif";

    private static final String RULES_TEXT =
        "BURRACO GAME RULES\n\n"
        + "1. TURN STEPS\n"
        + "• Drawing: Take a card from the Deck or all cards from the Discard Pile.\n"
        + "• Melding: Open new Straight/Set or attach cards to existing ones.\n"
        + "• Discarding: Discard one card to end your turn.\n\n"
        + "2. OPENING RUNS\n"
        + "• Straight: 3+ cards of the same suit in order.\n"
        + "• Set: 3+ cards of the same rank.\n"
        + "• You can attach cards only to your own Runs.\n\n"
        + "3. WILD CARDS\n"
        + "• You can use one Joker or one 'Pinella' (any 2) per Run.\n"
        + "• Replacing a wild card moves it to a new position in the same sequence.\n\n"
        + "4. BURRACO\n"
        + "A sequence of at least 7 cards. Clean (no wild cards) or Dirty.\n\n"
        + "5. THE POT\n"
        + "Taking the 11 extra cards is mandatory to close the round.\n\n"
        + "6. CLOSING\n"
        + "Requires a Burraco, taking the Pot, and discarding a final natural card.";

    private final JFrame frame;
    private final OnGameStartListener listener;

    /**
     * Constructs a StartMenuViewImpl.
     *
     * @param listener callback handler for game start events.
     */
    public StartMenuViewImpl (final OnGameStartListener listener) {
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
