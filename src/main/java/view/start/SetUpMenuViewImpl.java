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
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));
        panel.setBackground(new Color(0, 102, 51));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        
        panel.add(createLabel("Name Player 1:"));
        name1 = new JTextField("Player 1");
        panel.add(name1);

        panel.add(createLabel("Name Player 2:"));
        name2 = new JTextField("Player 2");
        panel.add(name2);

        panel.add(createLabel("Points to win:"));
        
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setOpaque(false);
        btnPanel.add(createScoreBtn(1005));
        btnPanel.add(createScoreBtn(1505));
        btnPanel.add(createScoreBtn(2005));
        panel.add(btnPanel);
        
        panel.add(new JLabel("")); 

        JButton playBtn = new JButton("PLAY");
        playBtn.setFont(new Font("Arial", Font.BOLD, 22));
        playBtn.setBackground(Color.YELLOW);
        playBtn.setForeground(new Color(0, 102, 51));
        
        playBtn.addActionListener(e -> {
            if (selectedScore == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a victory score first!");
            } else {
                close();
                listener.onConfigComplete(selectedScore, name1.getText(), name2.getText());
            }
        });
        panel.add(playBtn);

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
