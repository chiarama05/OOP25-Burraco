package view.start;

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
    // Dimensioni più generose e orizzontali
    frame.setSize(600, 650); 
    frame.setResizable(true); 
    frame.setLocationRelativeTo(null);

    JPanel panel = new JPanel(new GridBagLayout()); 
    GridBagConstraints gbc = new GridBagConstraints();
    
    gbc.fill = GridBagConstraints.HORIZONTAL; // Permette ai componenti di occupare la larghezza necessaria
    gbc.insets = new Insets(12, 15, 12, 15);
    panel.setBackground(new Color(0, 102, 51));

    // --- TASTO BACK ---
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2; // Occupa due colonne per bilanciare il layout
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.NONE;
    JButton backBtn = new JButton("← BACK");
    backBtn.setFont(new Font("Arial", Font.BOLD, 19));
    backBtn.setBackground(new Color(255, 255, 204)); 
    backBtn.setPreferredSize(new Dimension(120, 35));
    backBtn.addActionListener(e -> {
        close();
        listener.onBackClicked();
    });
    panel.add(backBtn, gbc);

    gbc.gridwidth = 1; 
    gbc.anchor = GridBagConstraints.CENTER;

    // --- NOME PLAYER 1 ---
gbc.gridy = 1;
panel.add(createLabel("Name Player 1:"), gbc);
gbc.gridy = 2;
name1 = new JTextField("Player 1", 20);
name1.setFont(new Font("Arial", Font.PLAIN, 20)); // Font leggermente più grande
name1.setPreferredSize(new Dimension(300, 45));  // Più alto (45px come i tasti punteggio)
name1.setHorizontalAlignment(JTextField.LEFT);  // Testo allineato a sinistra
panel.add(name1, gbc);

// --- NOME PLAYER 2 ---
gbc.gridy = 3;
panel.add(createLabel("Name Player 2:"), gbc);
gbc.gridy = 4;
name2 = new JTextField("Player 2", 20);
name2.setFont(new Font("Arial", Font.PLAIN, 20));
name2.setPreferredSize(new Dimension(300, 45));  // Stessa altezza generosa
name2.setHorizontalAlignment(JTextField.LEFT);  // Testo allineato a sinistra
panel.add(name2, gbc);

    // --- PUNTI ---
    gbc.gridy = 5;
    panel.add(createLabel("Points to win:"), gbc);
    gbc.gridy = 6;
    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Più spazio tra i tasti
    btnPanel.setOpaque(false);
    btnPanel.add(createScoreBtn(1005));
    btnPanel.add(createScoreBtn(1505));
    btnPanel.add(createScoreBtn(2005));
    panel.add(btnPanel, gbc);

    // --- TASTO PLAY ---
    gbc.gridy = 7;
    gbc.insets = new Insets(40, 10, 10, 10);
    JButton playBtn = new JButton("PLAY");
    playBtn.setFont(new Font("Arial", Font.BOLD, 40)); // Font più grande
    playBtn.setBackground(Color.YELLOW);
    playBtn.setForeground(new Color(0, 102, 51));
    playBtn.setPreferredSize(new Dimension(250, 70)); // Notevolmente più grande
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
    b.setFont(new Font("Arial", Font.BOLD, 20)); // Font più leggibile
    b.setPreferredSize(new Dimension(100, 45)); // Tasti punteggio più grandi
    
    b.addActionListener(e -> {
        this.selectedScore = score;
        for (JButton btn : scoreButtons) {
            btn.setBackground(Color.WHITE);
        }
        b.setBackground(new Color(255, 204, 0));
    });
    
    scoreButtons.add(b);
    return b;
}

    private JLabel createLabel(String text) {
    JLabel l = new JLabel(text);
    l.setForeground(Color.YELLOW);
    l.setFont(new Font("Arial", Font.BOLD, 23));
    l.setHorizontalAlignment(JLabel.LEFT); // Forza l'allineamento a sinistra della label
    return l;
}

    @Override public void display() { 
        frame.setVisible(true); 
    }
    @Override public void close() { 
        frame.dispose(); 
    }

}
