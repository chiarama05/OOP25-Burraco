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
        
        setupUI(p1, p2, name1, name2);
    }

    private void setupUI(Player p1, Player p2, String name1, String name2) {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 450);
        frame.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0, 102, 51)); // Verde tavolo
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- TITOLO ---
        JLabel titleLabel = new JLabel("TABELLONE PUNTEGGI", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 26));
        titleLabel.setForeground(Color.BLACK); // Scritta nera e grande
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // --- TABELLA PUNTEGGI ---
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 10, 20));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(40, 30, 40, 30));

        // Calcolo punteggi reali
        int roundS1 = scoreManager.calculateFinalScore(p1);
        int roundS2 = scoreManager.calculateFinalScore(p2);

        // 2. Aggiorna il totale storico dei giocatori
        ((PlayerImpl)p1).addPointsToMatch(roundS1);
        ((PlayerImpl)p2).addPointsToMatch(roundS2);

        int totalS1 = ((PlayerImpl)p1).getMatchTotalScore();
        int totalS2 = ((PlayerImpl)p2).getMatchTotalScore();

        System.out.println("Punti Round P1: " + roundS1 + " | Totale: " + totalS1);

        // 3. Mostra i totali nella griglia
        gridPanel.add(createStyledLabel(name1));
        gridPanel.add(createScoreValueLabel(totalS1));
        gridPanel.add(createStyledLabel(name2));
        gridPanel.add(createScoreValueLabel(totalS2));

        // 4. Logica del bottone (CONTINUA o FINE)
        JButton actionBtn;
        if (totalS1 >= targetScore || totalS2 >= targetScore) {
            String winner = totalS1 > totalS2 ? name1 : name2;
            actionBtn = new JButton("CAMPIONE: " + winner + " (ESCI)");
            actionBtn.addActionListener(e -> System.exit(0));
        } else {
            actionBtn = new JButton("PROSSIMO ROUND (Target: " + targetScore + ")");
            actionBtn.addActionListener(e -> {
                frame.dispose();
                tableView.startNewRound(); // Metodo da implementare per pulire il tavolo
            });
        }
        mainPanel.add(actionBtn, BorderLayout.SOUTH);
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
