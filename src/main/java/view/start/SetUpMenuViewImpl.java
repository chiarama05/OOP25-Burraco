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

    frame.setSize(600, 650); 
    frame.setResizable(true); 
    frame.setLocationRelativeTo(null);

    JPanel panel = new JPanel(new GridBagLayout()); 
    GridBagConstraints gbc = new GridBagConstraints();
    
    gbc.fill = GridBagConstraints.HORIZONTAL; 
    gbc.insets = new Insets(12, 15, 12, 15);
    panel.setBackground(new Color(0, 102, 51));

    // --- Back button---
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2; 
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

    // Player 1's name
    gbc.gridy = 1;
    panel.add(createLabel("Name Player 1:"), gbc);
    gbc.gridy = 2;
    name1 = new JTextField("Player 1", 20);
    name1.setFont(new Font("Arial", Font.PLAIN, 20)); 
    name1.setPreferredSize(new Dimension(300, 45));  
    name1.setHorizontalAlignment(JTextField.LEFT);
    panel.add(name1, gbc);

    //Player 2' s name
    gbc.gridy = 3;
    panel.add(createLabel("Name Player 2:"), gbc);
    gbc.gridy = 4;
    name2 = new JTextField("Player 2", 20);
    name2.setFont(new Font("Arial", Font.PLAIN, 20));
    name2.setPreferredSize(new Dimension(300, 45)); 
    name2.setHorizontalAlignment(JTextField.LEFT);  
    panel.add(name2, gbc);

    // Points
    gbc.gridy = 5;
    panel.add(createLabel("Points to win:"), gbc);
    gbc.gridy = 6;
    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); 
    btnPanel.setOpaque(false);
    btnPanel.add(createScoreBtn(1005));
    btnPanel.add(createScoreBtn(1505));
    btnPanel.add(createScoreBtn(2005));
    panel.add(btnPanel, gbc);

    // Play button
    gbc.gridy = 7;
    gbc.insets = new Insets(40, 10, 10, 10);
    JButton playBtn = new JButton("PLAY");
    playBtn.setFont(new Font("Arial", Font.BOLD, 40)); 
    playBtn.setBackground(Color.YELLOW);
    playBtn.setForeground(new Color(0, 102, 51));
    playBtn.setPreferredSize(new Dimension(250, 70)); 
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
        b.setFont(new Font("Arial", Font.BOLD, 20)); 
        b.setPreferredSize(new Dimension(100, 45));
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
        l.setHorizontalAlignment(JLabel.LEFT); 
        return l;   
    }

    @Override public void display() { 
        frame.setVisible(true); 
    }

    @Override public void close() { 
        frame.dispose(); 
    }

}
