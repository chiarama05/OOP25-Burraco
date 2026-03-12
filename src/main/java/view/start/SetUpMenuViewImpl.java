package view.start;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.*;
import java.util.ArrayList; 
import java.util.List;

public class SetUpMenuViewImpl implements SetUpMenuView{

    private final JFrame frame;
    private final OnConfigurationCompleteListener listener;
    private JTextField name1, name2;
    private int selectedScore = -1;

    private final List<JButton> scoreButtons = new ArrayList<>();

    public SetUpMenuViewImpl(OnConfigurationCompleteListener listener) {
        this.listener = listener;
        this.frame = new JFrame("Game Configuration");
        setupUI();
    }

    private void setupUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 600);
        frame.setResizable(true); 
        frame.setLocationRelativeTo(null);

        // Usiamo GridBagLayout: è l'unico che permette di tenere i componenti "fissi" al centro
        JPanel panel = new JPanel(new GridBagLayout()); 
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Questo è il segreto: fill = NONE impedisce ai componenti di allungarsi
        gbc.fill = GridBagConstraints.NONE; 
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.setBackground(new Color(0, 102, 51));

        // --- TASTO BACK ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST; // Lo ancora in alto a sinistra del suo spazio
        JButton backBtn = new JButton("← BACK");
        backBtn.setFont(new Font("Arial", Font.BOLD, 12));
        backBtn.setBackground(new Color(255, 255, 204)); 
        backBtn.setPreferredSize(new Dimension(100, 30));
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> {
            close();
            listener.onBackClicked();
        });
        panel.add(backBtn, gbc);

        // --- RESET ANCHOR PER IL RESTO (Tutto al centro) ---
        gbc.anchor = GridBagConstraints.CENTER;

        // --- NOME PLAYER 1 ---
        gbc.gridy = 1;
        panel.add(createLabel("Name Player 1:"), gbc);
        gbc.gridy = 2;
        name1 = new JTextField("Player 1", 15); 
        panel.add(name1, gbc);

        // --- NOME PLAYER 2 ---
        gbc.gridy = 3;
        panel.add(createLabel("Name Player 2:"), gbc);
        gbc.gridy = 4;
        name2 = new JTextField("Player 2", 15);
        panel.add(name2, gbc);

        // --- PUNTI ---
        gbc.gridy = 5;
        panel.add(createLabel("Points to win:"), gbc);
        gbc.gridy = 6;
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setOpaque(false);
        btnPanel.add(createScoreBtn(1005));
        btnPanel.add(createScoreBtn(1505));
        btnPanel.add(createScoreBtn(2005));
        panel.add(btnPanel, gbc);

        // --- TASTO PLAY ---
        gbc.gridy = 7;
        gbc.insets = new Insets(30, 10, 10, 10);
        JButton playBtn = new JButton("PLAY");
        playBtn.setFont(new Font("Arial", Font.BOLD, 22));
        playBtn.setBackground(Color.YELLOW);
        playBtn.setForeground(new Color(0, 102, 51));
        playBtn.setPreferredSize(new Dimension(180, 50)); // Dimensione fissa: non cambierà mai
        playBtn.addActionListener(e -> {
            if (selectedScore == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a victory score first!");
            } else {
                close();
                listener.onConfigComplete(selectedScore, name1.getText(), name2.getText());
            }
        });
        panel.add(playBtn, gbc);

        frame.add(panel);
    }

    private JButton createScoreBtn(int score) {
        JButton b = new JButton(String.valueOf(score));
        b.setFocusPainted(false);
        b.setBackground(Color.WHITE);
        
        b.addActionListener(e -> {
            // Quando clicco, aggiorno il punteggio selezionato
            this.selectedScore = score;
            // E resetto i colori degli altri bottoni per far vedere la scelta
            for (JButton btn : scoreButtons) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(Color.BLACK);
            }
            b.setBackground(new Color(255, 204, 0)); // Colore evidenziato (Oro)
            b.setForeground(Color.BLACK);
        });
        
        scoreButtons.add(b);
        return b;
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.YELLOW);
        l.setFont(new Font("Arial", Font.BOLD, 18));
        return l;
    }

    @Override public void display() { 
        frame.setVisible(true); 
    }
    @Override public void close() { 
        frame.dispose(); 
    }

}
