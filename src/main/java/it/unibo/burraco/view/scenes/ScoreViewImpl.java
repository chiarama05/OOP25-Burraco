package it.unibo.burraco.view.scenes;
 
import it.unibo.burraco.controller.score.ScoreSnapshot;
import it.unibo.burraco.view.components.RoundedGradientButton;
import it.unibo.burraco.view.table.TableView;
 
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
 
/**
 * Swing implementation of the ScoreView interface.
 *
 * <p>Receives only {@link ScoreSnapshot} objects — plain data containers
 * pre-computed by the Controller. No {@code Player} or {@code Score}
 * model references are used here.
 */
public final class ScoreViewImpl implements ScoreView {
 
    private static final int FRAME_WIDTH        = 650;
    private static final int FRAME_HEIGHT       = 750;
    private static final int BORDER_PADDING     = 20;
    private static final int GRID_GAP           = 30;
    private static final int TITLE_FONT_SIZE    = 28;
    private static final int TITLE_BOTTOM_BORDER = 40;
    private static final int NAME_FONT_SIZE     = 22;
    private static final int NAME_STRUT         = 20;
    private static final int BUTTON_FONT_SIZE   = 18;
    private static final int BUTTON_WIDTH       = 500;
    private static final int BUTTON_HEIGHT      = 60;
    private static final int BUTTON_TOP_BORDER  = 40;
    private static final int BUTTON_BOT_BORDER  = 10;
    private static final int ROW_FONT_SIZE      = 16;
    private static final int ROW_MAX_WIDTH      = 250;
    private static final int ROW_MAX_HEIGHT     = 30;
    private static final int SEPARATOR_STRUT    = 10;
    private static final int SEPARATOR_HEIGHT   = 10;
 
    private static final Color BACKGROUND_COLOR  = new Color(0, 102, 51);
    private static final Color TITLE_COLOR       = new Color(255, 182, 193);
    private static final Color GRADIENT_TOP      = new Color(53, 102, 73);
    private static final Color GRADIENT_BOTTOM   = new Color(94, 153, 115);
    private static final Color ACCENT_COLOR      = new Color(219, 112, 147);
 
    private final JFrame frame;
    private final JFrame tableFrame;
    private final int targetScore;
    private Runnable nextAction;
 
    /**
     * Constructs a new ScoreViewImpl from pre-computed snapshots.
     *
     * @param snap1     display data for Player 1
     * @param snap2     display data for Player 2
     * @param targetScore score threshold required to win the match
     * @param tableView reference to the main table (for positioning)
     * @param matchOver {@code true} if the match has concluded
     */
    public ScoreViewImpl(
            final ScoreSnapshot snap1,
            final ScoreSnapshot snap2,
            final int targetScore,
            final TableView tableView,
            final boolean matchOver) {
        this.frame = new JFrame("Burraco - Final Standings");
        this.targetScore = targetScore;
        this.tableFrame = tableView.getFrame();
        this.setupUI(snap1, snap2, matchOver);
    }
 
    @Override
    public void setOnNextAction(final Runnable action) {
        this.nextAction = action;
    }
 
    private void setupUI(
            final ScoreSnapshot snap1,
            final ScoreSnapshot snap2,
            final boolean matchOver) {
 
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.frame.setLocationRelativeTo(null);
 
        final BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(
                BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING));
 
        final JLabel titleLabel = new JLabel("SCOREBOARD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, TITLE_BOTTOM_BORDER, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
 
        final JPanel centerPanel = new JPanel(new GridLayout(1, 2, GRID_GAP, 0));
        centerPanel.setOpaque(false);
        centerPanel.add(createPlayerStatsPanel(snap1));
        centerPanel.add(createPlayerStatsPanel(snap2));
        mainPanel.add(centerPanel, BorderLayout.CENTER);
 
        final RoundedGradientButton actionBtn;
        if (matchOver) {
            final String winnerName = snap1.isWinner()
                    ? snap1.playerName().toUpperCase(Locale.ROOT)
                    : snap2.playerName().toUpperCase(Locale.ROOT);
            actionBtn = new RoundedGradientButton("CHAMPION: " + winnerName + " (FINISH GAME)");
            actionBtn.addActionListener(e -> {
                this.tableFrame.dispose();
                this.frame.dispose();
            });
        } else {
            actionBtn = new RoundedGradientButton(
                    "NEXT ROUND (Target: " + targetScore + " pts)");
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
        buttonContainer.setBorder(
                new EmptyBorder(BUTTON_TOP_BORDER, 0, BUTTON_BOT_BORDER, 0));
        buttonContainer.add(actionBtn);
        mainPanel.add(buttonContainer, BorderLayout.SOUTH);
 
        this.frame.add(mainPanel);
    }
 
    private JPanel createPlayerStatsPanel(final ScoreSnapshot snap) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
 
        final String displayName = snap.isWinner()
                ? "🏆 " + snap.playerName().toUpperCase(Locale.ROOT) + " 🏆"
                : snap.playerName().toUpperCase(Locale.ROOT);
 
        final JLabel nameLabel = new JLabel(displayName);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, NAME_FONT_SIZE));
        nameLabel.setForeground(snap.isWinner() ? ACCENT_COLOR : Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(NAME_STRUT));
 
        panel.add(createRow("Cards on Table",   String.valueOf(snap.cardsOnTable()),       false));
        panel.add(createRow("Clean Burraco",    String.valueOf(snap.cleanBurracoPoints()), false));
        panel.add(createRow("Dirty Burraco",    String.valueOf(snap.dirtyBurracoPoints()), false));
        panel.add(createRow("Closure Bonus",    String.valueOf(snap.closureBonus()),        false));
        panel.add(createRow("Pot Penalty",      String.valueOf(snap.potPenalty()),          false));
        panel.add(createRow("Cards in Hand",    "-" + snap.cardsInHandPenalty(),           false));
 
        panel.add(Box.createVerticalStrut(SEPARATOR_STRUT));
        final JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, SEPARATOR_HEIGHT));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(SEPARATOR_STRUT));
 
        panel.add(createRow("ROUND TOTAL", String.valueOf(snap.roundTotal()), true));
        panel.add(createRow("MATCH TOTAL", String.valueOf(snap.matchTotal()), true));
        panel.add(Box.createVerticalGlue());
 
        return panel;
    }
 
    private JPanel createRow(final String label, final String value, final boolean bold) {
        final JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
 
        final Font f = new Font("Arial", bold ? Font.BOLD : Font.PLAIN, ROW_FONT_SIZE);
        final JLabel lLabel = new JLabel(label);
        final JLabel lValue = new JLabel(value);
        lLabel.setFont(f);
        lValue.setFont(f);
        lLabel.setForeground(Color.WHITE);
        lValue.setForeground(ACCENT_COLOR);
 
        row.add(lLabel, BorderLayout.WEST);
        row.add(lValue, BorderLayout.EAST);
        row.setMaximumSize(new Dimension(ROW_MAX_WIDTH, ROW_MAX_HEIGHT));
        return row;
    }
 
    @Override
    public void display() {
        this.frame.setLocationRelativeTo(this.tableFrame);
        this.frame.setAlwaysOnTop(true);
        this.frame.setVisible(true);
        this.frame.toFront();
        this.frame.requestFocus();
    }
 
    @Override
    public void close() {
        this.frame.dispose();
    }
 
    private static final class BackgroundPanel extends JPanel {
        private static final long serialVersionUID = 1L;
 
        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            final Graphics2D g2d = (Graphics2D) g;
            final GradientPaint gp = new GradientPaint(
                    0, 0, GRADIENT_TOP, 0, getHeight(), GRADIENT_BOTTOM);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}