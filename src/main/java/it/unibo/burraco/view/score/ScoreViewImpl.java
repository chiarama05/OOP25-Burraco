package it.unibo.burraco.view.score;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.score.Score;
import it.unibo.burraco.view.colorbutton.RoundedGradientButton;
import it.unibo.burraco.view.table.TableView;

/**
 * Swing implementation of the ScoreView interface.
 * Displays a detailed scoreboard window at the end of each round or match,
 * featuring gradient backgrounds, per-player statistics, and action buttons
 * for either advancing to the next round or concluding the game.
 */
public final class ScoreViewImpl implements ScoreView {

    private static final long serialVersionUID = 1L;
    private static final int FRAME_WIDTH = 650;
    private static final int FRAME_HEIGHT = 750;
    private static final int BORDER_PADDING = 20;
    private static final int GRID_GAP = 30;
    private static final int TITLE_FONT_SIZE = 28;
    private static final int TITLE_BOTTOM_BORDER = 40;
    private static final int NAME_FONT_SIZE = 22;
    private static final int NAME_STRUT = 20;
    private static final int BUTTON_FONT_SIZE = 18;
    private static final int BUTTON_WIDTH = 500;
    private static final int BUTTON_HEIGHT = 60;
    private static final int BUTTON_TOP_BORDER = 40;
    private static final int BUTTON_BOT_BORDER = 10;
    private static final int ROW_FONT_SIZE = 16;
    private static final int ROW_MAX_WIDTH = 250;
    private static final int ROW_MAX_HEIGHT = 30;
    private static final int SEPARATOR_STRUT = 10;
    private static final int SEPARATOR_HEIGHT = 10;

    private static final Color BACKGROUND_COLOR = new Color(0, 102, 51);
    private static final Color TITLE_COLOR = new Color(255, 182, 193);
    private static final Color GRADIENT_TOP = new Color(53, 102, 73);
    private static final Color GRADIENT_BOTTOM = new Color(94, 153, 115);
    private static final Color ACCENT_COLOR = new Color(219, 112, 147);

    private final JFrame frame;
    private final Score scoreManager;
    private final int targetScore;
    private Runnable nextAction;

    /**
     * Constructs a new ScoreViewImpl and initializes the UI.
     *
     * @param p1 The first player.
     * @param p2 The second player.
     * @param name1 Display name of the first player.
     * @param name2 Display name of the second player.
     * @param targetScore The score threshold required to win the match.
     * @param scoreManager The scoring engine providing score computations.
     * @param tableView Reference to the main table view.
     * @param matchOver {@code true} if the match has concluded; {@code false} if only a round ended.
     */
    public ScoreViewImpl(
            final Player p1, final Player p2,
            final String name1, final String name2,
            final int targetScore,
            final Score scoreManager,
            final TableView tableView,
            final boolean matchOver) {

        this.scoreManager = scoreManager;
        this.frame = new JFrame("Burraco - Final Standings");
        this.targetScore = targetScore;

        this.setupUI(p1, p2, name1, name2, matchOver);
    }

    /**
     * Registers the action to execute when the user advances to the next round.
     *
     * @param action The {@link Runnable} to invoke on button click.
     */
    @Override
    public void setOnNextAction(final Runnable action) {
        this.nextAction = action;
    }

    /**
     * Builds and arranges all UI components inside the scoreboard window.
     * Includes the title bar, the two side-by-side player stat panels,
     * and the bottom action button (either "Next Round" or "Finish Game").
     * 
     * @param p1 The first player.
     * @param p2 The second player.
     * @param name1 Display name of the first player.
     * @param name2 Display name of the second player.
     * @param matchOver {@code true} if the match is over, determining button behavior.
     */
    private void setupUI(
            final Player p1, final Player p2,
            final String name1, final String name2,
            final boolean matchOver) {

        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.frame.setLocationRelativeTo(null);

        final BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(BORDER_PADDING, BORDER_PADDING,
                                            BORDER_PADDING, BORDER_PADDING));

        final JLabel titleLabel = new JLabel("SCOREBOARD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, TITLE_BOTTOM_BORDER, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        final JPanel centerPanel = new JPanel(new GridLayout(1, 2, GRID_GAP, 0));
        centerPanel.setOpaque(false);

        final int totalS1 = p1.getMatchTotalScore();
        final int totalS2 = p2.getMatchTotalScore();

        final boolean p1Winner = matchOver && totalS1 > totalS2;
        final boolean p2Winner = matchOver && totalS2 > totalS1;

        centerPanel.add(createPlayerStatsPanel(p1, name1, p1Winner));
        centerPanel.add(createPlayerStatsPanel(p2, name2, p2Winner));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        final RoundedGradientButton actionBtn;
        if (matchOver) {
            final String winnerName = p1.getMatchTotalScore() > p2.getMatchTotalScore() ? name1 : name2;
            final String btnText = "CHAMPION: " + winnerName.toUpperCase(Locale.ROOT) + " (FINISH GAME)";
            actionBtn = new RoundedGradientButton(btnText);
            actionBtn.addActionListener(e -> this.frame.dispose());
        } else {
            actionBtn = new RoundedGradientButton("NEXT ROUND (Target: " + targetScore + " pts)");
            actionBtn.addActionListener(e -> {
                if (this.nextAction != null) {
                    this.nextAction.run();
                }
            });
        }
        actionBtn.setFont(new Font("Arial Black", Font.BOLD, BUTTON_FONT_SIZE));
        actionBtn.setForeground(Color.BLACK);
        actionBtn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        actionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        final JPanel buttonContainer = new JPanel(new BorderLayout());
        buttonContainer.setOpaque(false);
        buttonContainer.setBorder(new EmptyBorder(BUTTON_TOP_BORDER, 0, BUTTON_BOT_BORDER, 0));
        buttonContainer.add(actionBtn);
        mainPanel.add(buttonContainer, BorderLayout.SOUTH);

        this.frame.add(mainPanel);
    }

    /**
     * Builds the statistics column panel for a single player.
     * Lists all score components (cards on table, burraco bonuses, closure bonus,
     * pot penalty, hand penalty) plus the round and cumulative match totals.
     * The winner's name is highlighted with trophy emojis when the match is over.
     *
     * @param p The player whose stats are displayed.
     * @param name The player's display name.
     * @param isWinner {@code true} if this player won the match.
     * @return A fully configured {@link JPanel} ready to be added to the center layout.
     */
    private JPanel createPlayerStatsPanel(
            final Player p,
            final String name,
            final boolean isWinner) {

        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        final String displayName = isWinner ? "🏆 " + name.toUpperCase(Locale.ROOT) + " 🏆"
                                            : name.toUpperCase(Locale.ROOT);

        final JLabel nameLabel = new JLabel(displayName);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, NAME_FONT_SIZE));
        nameLabel.setForeground(isWinner ? ACCENT_COLOR : Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(NAME_STRUT));

        final int onTable = scoreManager.calculateOnlyCardsOnTable(p);
        final int pointBurracoC = scoreManager.countCleanBurraco(p) * scoreManager.getCleanBurracoBonusValue();
        final int pointBurracoD = scoreManager.countDirtyBurraco(p) * scoreManager.getDirtyBurracoBonusValue();
        final int closure = p.hasFinishedCards() ? scoreManager.getClosureBonusValue() : 0;
        final int pot = p.isInPot() ? 0 : scoreManager.getNoPotPenalty();
        final int cardInHand = scoreManager.calculateRemainingHandValue(p);
        final int totalHand = scoreManager.calculateFinalScore(p);
        final int totalMatch = p.getMatchTotalScore();

        panel.add(createRow("Cards on Table", String.valueOf(onTable), false));
        panel.add(createRow("Clean Burraco", String.valueOf(pointBurracoC), false));
        panel.add(createRow("Dirty Burraco", String.valueOf(pointBurracoD), false));
        panel.add(createRow("Closure Bonus", String.valueOf(closure), false));
        panel.add(createRow("Pot Penalty", String.valueOf(pot), false));
        panel.add(createRow("Cards in Hand", "-" + cardInHand, false));

        panel.add(Box.createVerticalStrut(SEPARATOR_STRUT));
        final JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, SEPARATOR_HEIGHT));
        panel.add(sep);

        panel.add(Box.createVerticalStrut(SEPARATOR_STRUT));
        panel.add(this.createRow("ROUND TOTAL", String.valueOf(totalHand), true));
        panel.add(this.createRow("MATCH TOTAL", String.valueOf(totalMatch), true));
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Creates a single label–value row used inside the player stats panel.
     * The label is left-aligned and the value is right-aligned within a
     * {@link BorderLayout}, capped at a fixed maximum size for consistent alignment.
     *
     * @param label The descriptive text shown on the left side.
     * @param value The numeric or string value shown on the right side.
     * @param bold {@code true} to render both texts in bold (used for totals).
     * @return A configured {@link JPanel} representing one score row.
     */
    private JPanel createRow(final String label, final String value, final boolean bold) {
        final JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);

        final JLabel lLabel = new JLabel(label);
        final JLabel lValue = new JLabel(value);

        final Font f = new Font("Arial", bold ? Font.BOLD : Font.PLAIN, ROW_FONT_SIZE);
        lLabel.setFont(f);
        lValue.setFont(f);
        lLabel.setForeground(Color.WHITE);
        lValue.setForeground(ACCENT_COLOR);

        row.add(lLabel, BorderLayout.WEST);
        row.add(lValue, BorderLayout.EAST);
        row.setMaximumSize(new Dimension(ROW_MAX_WIDTH, ROW_MAX_HEIGHT));
        return row;
    }

    /**
     * Makes the scoreboard window visible to the user.
     */
    @Override
    public void display() {
        this.frame.setVisible(true);
    }

    /**
     * Disposes of the scoreboard window and releases its resources.
     */
    @Override
    public void close() {
        this.frame.dispose();
    }

    /**
     * Inner class that paints a vertical linear gradient as its background,
     * giving the scoreboard a polished green-felt appearance.
     */
    private static final class BackgroundPanel extends JPanel {

        private static final long serialVersionUID = 1L;
        /**
         * Overrides the default painting to fill the panel with a top-to-bottom gradient.
         *
         * @param g The {@link Graphics} context provided by Swing.
         */
        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            final Graphics2D g2d = (Graphics2D) g;
            final int w = getWidth();
            final int h = getHeight();
            final GradientPaint gp = new GradientPaint(0, 0, GRADIENT_TOP, 0, h, GRADIENT_BOTTOM);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }
}
