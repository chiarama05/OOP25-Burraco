package view.score;

import model.player.Player;
import model.player.PlayerImpl;
import view.sound.SoundControllerImpl;
import view.table.TableViewImpl;
import core.score.ScoreManager;
import core.score.ScoreManagerImpl;
import view.button.RoundedGradientButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ScoreViewImpl implements ScoreView{

    private final JFrame frame;
    private final ScoreManager scoreManager;
    private final int targetScore;
    private final TableViewImpl tableView;
    private final core.controller.GameController gameController;

    public ScoreViewImpl(Player p1, Player p2, String name1, String name2, int targetScore, 
                         TableViewImpl tableView, core.controller.GameController gameController) {
        this.scoreManager = new ScoreManagerImpl();
        this.frame = new JFrame("Burraco - Final Standings");
        this.targetScore = targetScore;
        this.tableView = tableView;
        this.gameController = gameController;


        int roundS1 = scoreManager.calculateFinalScore(p1);
        int roundS2 = scoreManager.calculateFinalScore(p2);

        ((PlayerImpl) p1).addPointsToMatch(roundS1);
        ((PlayerImpl) p2).addPointsToMatch(roundS2);

        int totalS1 = ((PlayerImpl) p1).getMatchTotalScore();
        int totalS2 = ((PlayerImpl) p2).getMatchTotalScore();


        boolean matchOver = totalS1 >= targetScore || totalS2 >= targetScore;


        if (matchOver) {
            new SoundControllerImpl().playVictorySound();
        } else {
            new SoundControllerImpl().playRoundEndSound();
        }

        setupUI(p1, p2, name1, name2, matchOver);
    }

    private void setupUI(Player p1, Player p2, String name1, String name2, boolean matchOver) {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(650, 750);
        frame.setLocationRelativeTo(null);
        
        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(0, 102, 51)); 
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- TITOLO ---
        JLabel titleLabel = new JLabel("SCOREBOARD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 182, 193)); 
        titleLabel.setBorder(new EmptyBorder(0, 0, 40, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        centerPanel.setOpaque(false);

        int totalS1 = ((PlayerImpl)p1).getMatchTotalScore();
        int totalS2 = ((PlayerImpl)p2).getMatchTotalScore();


        boolean p1Winner = matchOver && (totalS1 > totalS2);
        boolean p2Winner = matchOver && (totalS2 > totalS1);

        centerPanel.add(createPlayerStatsPanel(p1, name1, p1Winner));
        centerPanel.add(createPlayerStatsPanel(p2, name2, p2Winner));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // --- BOTTONE AZIONE ---
        RoundedGradientButton actionBtn;
        if (matchOver) {
            String winnerName = totalS1 > totalS2 ? name1 : name2;
            actionBtn = new RoundedGradientButton("CHAMPION: " + winnerName.toUpperCase() + " (FINISH GAME)");
            actionBtn.addActionListener(e -> System.exit(0));
        } else {
            actionBtn = new RoundedGradientButton("NEXT ROUND (Target: " + targetScore + " pts)");
            actionBtn.addActionListener(e -> {
                frame.dispose();
                gameController.getTurnModel().resetForNewRound();
                core.round.ResetManager resetManager = new core.round.ResetManagerImpl();
                core.round.RoundController rc = new core.round.RoundControllerImpl(
                    tableView, 
                    resetManager, 
                    (PlayerImpl)p1, 
                    (PlayerImpl)p2, 
                    gameController 
                );
                rc.processNewRound();
                tableView.refreshTurnLabel(true);
            });
        }
        actionBtn.setFont(new Font("Arial Black", Font.BOLD, 18));
        actionBtn.setForeground(Color.BLACK); 
        actionBtn.setPreferredSize(new Dimension(500, 60));
        actionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel buttonContainer = new JPanel(new BorderLayout());
        buttonContainer.setOpaque(false);
        buttonContainer.setBorder(new EmptyBorder(40, 0, 10, 0));
        buttonContainer.add(actionBtn);
        mainPanel.add(buttonContainer, BorderLayout.SOUTH);

        frame.add(mainPanel);
    }

    private int calculateOnlyCardsOnTable(Player p) {
    int total = 0;
    for (java.util.List<model.card.Card> combination : p.getCombinations()) {
        for (model.card.Card card : combination) {
            total += core.score.CardPointCalculator.getCardPoints(card);
        }
    }
    return total;
    }

    private JPanel createPlayerStatsPanel(Player p, String name, boolean isWinner) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setOpaque(false);

    String displayName = isWinner ? "🏆 " + name.toUpperCase() + " 🏆" : name.toUpperCase();

    JLabel nameLabel = new JLabel(displayName);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        nameLabel.setForeground(isWinner ? new Color(219, 112, 147) : Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(20));

    int aTerra = calculateOnlyCardsOnTable(p);
    int burrachiSporchi = ((ScoreManagerImpl)scoreManager).countDirtyBurraco(p);
    int burrachiPuliti = ((ScoreManagerImpl)scoreManager).countCleanBurraco(p);
    
    int chiusura = p.hasFinishedCards() ? 100 : 0;
    int mazzetto = p.isInPot() ? 0 : -100;
    int carteInMano = scoreManager.calculateRemainingHandValue(p);
    int totaleMano = scoreManager.calculateFinalScore(p);

    panel.add(createRow("Board Score", String.valueOf(aTerra), false));
    panel.add(createRow("Clean Burraco", String.valueOf(burrachiPuliti*200), false));
    panel.add(createRow("Dirty Burraco", String.valueOf(burrachiSporchi * 100), false));
    panel.add(createRow("Closure", String.valueOf(chiusura), false));
    panel.add(createRow("Pot not taken", String.valueOf(mazzetto), false));
    panel.add(createRow("Paid cards", "-" + carteInMano, false));

    panel.add(Box.createVerticalStrut(10));
    JSeparator sep = new JSeparator();
    sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10)); 
    panel.add(sep); 
    panel.add(Box.createVerticalStrut(10));
    
    panel.add(Box.createVerticalStrut(10));
    panel.add(createRow("Round Score", String.valueOf(totaleMano), true));
    
    int totalePartita = ((PlayerImpl)p).getMatchTotalScore();
    panel.add(createRow("TOTAL MATCH", String.valueOf(totalePartita), true));
    panel.add(Box.createVerticalGlue());

    return panel;
}

class BackgroundPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        Color color1 = new Color(53, 102, 73); 
        Color color2 = new Color(94, 153, 115); 
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}

    private JPanel createRow(String label, String value, boolean bold) {
    JPanel row = new JPanel(new BorderLayout());
    row.setOpaque(false);
    JLabel lLabel = new JLabel(label);
    JLabel lValue = new JLabel(value);
    
    Font f = new Font("Arial", bold ? Font.BOLD : Font.PLAIN, 16);
    lLabel.setFont(f);
    lValue.setFont(f);
    lLabel.setForeground(Color.WHITE);
    lValue.setForeground(new Color(219, 112, 147));

    row.add(lLabel, BorderLayout.WEST);
    row.add(lValue, BorderLayout.EAST);
    row.setMaximumSize(new Dimension(250, 30));
    return row;
}

    @Override
    public void display() {
        frame.setVisible(true);
    }

    @Override
    public void close() {
        frame.dispose();
    }
}
