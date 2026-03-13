package view.score;

import model.player.Player;
import model.player.PlayerImpl;
import view.table.TableViewImpl;
import core.score.ScoreManager;
import core.score.ScoreManagerImpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ScoreViewImpl implements ScoreView{

    private final JFrame frame;
    private final ScoreManager scoreManager;
    private final int targetScore;
    private final TableViewImpl tableView;

    public ScoreViewImpl(Player p1, Player p2, String name1, String name2, int targetScore, TableViewImpl tableView) {
        this.scoreManager = new ScoreManagerImpl();
        this.frame = new JFrame("Burraco - Final Standings");
        this.targetScore = targetScore;
        this.tableView = tableView;

        new view.sound.SoundControllerImpl().playRoundEndSound();
        setupUI(p1, p2, name1, name2);
    }

    private void setupUI(Player p1, Player p2, String name1, String name2) {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0, 102, 51)); 
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- TITOLO ---
        JLabel titleLabel = new JLabel("TABELLONE PUNTEGGI", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 24));
        titleLabel.setForeground(Color.YELLOW); 
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ---CALCOLO LOGICO---
        int roundS1 = scoreManager.calculateFinalScore(p1);
        int roundS2 = scoreManager.calculateFinalScore(p2);

        ((PlayerImpl)p1).addPointsToMatch(roundS1);
        ((PlayerImpl)p2).addPointsToMatch(roundS2);

        int totalS1 = ((PlayerImpl)p1).getMatchTotalScore();
        int totalS2 = ((PlayerImpl)p2).getMatchTotalScore();

        // --- TABELLA PUNTEGGI ---
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 10, 20));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(40, 30, 40, 30));

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        centerPanel.setOpaque(false);

        centerPanel.add(createPlayerStatsPanel(p1, name1, roundS1));
        centerPanel.add(createPlayerStatsPanel(p2, name2, roundS2));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        
        // Mostra i totali nella griglia
        gridPanel.add(createStyledLabel(name1));
        gridPanel.add(createScoreValueLabel(totalS1));
        gridPanel.add(createStyledLabel(name2));
        gridPanel.add(createScoreValueLabel(totalS2));


        // Logica del bottone (CONTINUA o FINE)
        JButton actionBtn;
        if (totalS1 >= targetScore || totalS2 >= targetScore) {
            new view.sound.SoundControllerImpl().playVictorySound();
            String winner = totalS1 > totalS2 ? name1 : name2;
            actionBtn = new JButton("CAMPIONE: " + winner + " (ESCI)");
            actionBtn.addActionListener(e -> System.exit(0));
        } else {
            actionBtn = new JButton("PROSSIMO ROUND (Target: " + targetScore + ")");

            actionBtn.setFont(new Font("Arial Black", Font.BOLD, 18));
            actionBtn.setBackground(Color.YELLOW);
            actionBtn.setForeground(new Color(0, 102, 51)); // Verde scuro
            actionBtn.setFocusPainted(false);
            actionBtn.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0)); // Più alto
            actionBtn.setOpaque(true);
            actionBtn.setBorderPainted(false);
            actionBtn.addActionListener(e -> {
            if (totalS1 >= targetScore || totalS2 >= targetScore) {
            System.exit(0);
            } else {
            frame.dispose();
            tableView.startNewRound();
            }
        });
        }
        mainPanel.add(actionBtn, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.revalidate();
        frame.repaint();
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

    private JPanel createPlayerStatsPanel(Player p, String name, int roundScore) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setOpaque(false);

    int aTerra = calculateOnlyCardsOnTable(p);
    int burrachiSporchi = ((ScoreManagerImpl)scoreManager).countDirtyBurraco(p);
    int burrachiPuliti = ((ScoreManagerImpl)scoreManager).countCleanBurraco(p);
    
    int chiusura = p.hasFinishedCards() ? 100 : 0;
    int mazzetto = p.isInPot() ? 0 : -100;
    int carteInMano = scoreManager.calculateRemainingHandValue(p);
    int totaleMano = scoreManager.calculateFinalScore(p);

    
    JLabel nameLabel = new JLabel(name.toUpperCase());
    nameLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
    nameLabel.setForeground(Color.YELLOW);
    nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(nameLabel);
    panel.add(Box.createVerticalStrut(20));

    
    panel.add(createRow("Punteggio a terra", String.valueOf(aTerra), false));
    panel.add(createRow("Burrachi", String.valueOf(burrachiPuliti + burrachiSporchi), false));
    panel.add(createRow("Burrachi Puliti", String.valueOf(burrachiPuliti), false));
    panel.add(createRow("Burrachi Sporchi", String.valueOf(burrachiSporchi * 100), false));
    panel.add(createRow("Chiusura", String.valueOf(chiusura), false));
    panel.add(createRow("Mazzetto non preso", String.valueOf(mazzetto), false));
    panel.add(createRow("Carte pagate", "-" + carteInMano, false));

    panel.add(Box.createVerticalStrut(10));
    JSeparator sep = new JSeparator();
    sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10)); 
    panel.add(sep); 
    panel.add(Box.createVerticalStrut(10));
    
    panel.add(Box.createVerticalStrut(10));
    panel.add(createRow("Totale Mano", String.valueOf(totaleMano), true));
    
    int totalePartita = ((PlayerImpl)p).getMatchTotalScore();
    panel.add(createRow("TOTALE PARTITA", String.valueOf(totalePartita), true));
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
    lValue.setForeground(Color.YELLOW); 

    row.add(lLabel, BorderLayout.WEST);
    row.add(lValue, BorderLayout.EAST);
    row.setMaximumSize(new Dimension(250, 30));
    return row;
}

    private JLabel createStyledLabel(String text) {
        JLabel l = new JLabel(text.toUpperCase() + ":");
        l.setFont(new Font("Arial Black", Font.BOLD, 22));
        l.setForeground(Color.BLACK);
        return l;
    }

    private JLabel createScoreValueLabel(int score) {
        JLabel l = new JLabel(score + " PT");
        l.setFont(new Font("Arial Black", Font.BOLD, 24));
        // Se il punteggio è negativo lo facciamo risaltare (tipico del Burraco)
        l.setForeground(score < 0 ? new Color(150, 0, 0) : Color.BLACK);
        return l;
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
