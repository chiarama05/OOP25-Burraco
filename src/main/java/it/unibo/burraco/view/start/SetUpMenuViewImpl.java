package it.unibo.burraco.view.start;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.unibo.burraco.view.colorbutton.RoundedGradientButton;

import java.awt.*;
import java.util.ArrayList; 
import java.util.List;

/**
 * Swing-based implementation of the SetUpMenuView.
 * Provides a graphical interface for configuring game parameters such as 
 * player names and the target victory score.
 */
public class SetUpMenuViewImpl implements SetUpMenuView{

    private static final Color BACKGROUND_COLOR = new Color(0, 102, 51); 
    private static final Color LABEL_COLOR = new Color(255, 182, 193);     
    private static final Color BUTTON_BG_COLOR = new Color(255, 240, 245);  
    private static final Color SCORE_SELECTED_COLOR = new Color(219, 112, 147); 
    
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 650;
    private static final int FIELD_HEIGHT = 45;
    private static final int FIELD_WIDTH = 300;

    private static final int TITLE_FONT_SIZE = 23;
    private static final int BUTTON_FONT_SIZE = 19;
    private static final int SCORE_FONT_SIZE = 20;

    private final JFrame frame;
    private final OnConfigurationCompleteListener listener;
    private final List<JButton> scoreButtons = new ArrayList<>();

    private JTextField name1, name2;
    private int selectedScore = -1;

    /**
     * Constructor for the configuration menu.
     * 
     * @param listener the listener handling the configuration completion events.
     */
    public SetUpMenuViewImpl(OnConfigurationCompleteListener listener) {
        this.listener = listener;
        this.frame = new JFrame("Game Configuration");
        setupUI();
    }

    /**
     * Initializes components and sets up the GridBagLayout.
     */
    private void setupUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT); 
        frame.setResizable(true); 
        frame.setLocationRelativeTo(null);

        final JPanel panel = new JPanel(new GridBagLayout()); 
        panel.setBackground(BACKGROUND_COLOR);
        final GridBagConstraints gbc = new GridBagConstraints();
    
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.insets = new Insets(12, 15, 12, 15);
    
        // Back button
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; 
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;

        final RoundedGradientButton backBtn = new RoundedGradientButton("← BACK");
        backBtn.setFont(new Font("Arial", Font.BOLD, BUTTON_FONT_SIZE));
        backBtn.setBackground(BUTTON_BG_COLOR); 
        backBtn.setPreferredSize(new Dimension(120, 35));
        backBtn.addActionListener(e -> {
            close();
            listener.onBackClicked();
        });
        panel.add(backBtn, gbc);

        gbc.gridwidth = 1; 
        gbc.anchor = GridBagConstraints.CENTER;


        gbc.gridy = 1;
        panel.add(createLabel("Name Player 1:"), gbc);
        gbc.gridy = 2;
        name1 = new JTextField("Player 1", 20);
        name1.setFont(new Font("Arial", Font.PLAIN, 20)); 
        name1.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));  
        name1.setHorizontalAlignment(JTextField.LEFT); 
        panel.add(name1, gbc);

        gbc.gridy = 3;
        panel.add(createLabel("Name Player 2:"), gbc);
        gbc.gridy = 4;
        name2 = new JTextField("Player 2", 20);
        name2.setFont(new Font("Arial", Font.PLAIN, 20));
        name2.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));  
        name2.setHorizontalAlignment(JTextField.LEFT);  
        panel.add(name2, gbc);


        gbc.gridy = 5;
        panel.add(createLabel("Points to win:"), gbc);
        gbc.gridy = 6;
        final JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); 
        btnPanel.setOpaque(false);
        btnPanel.add(createScoreBtn(1005));
        btnPanel.add(createScoreBtn(1505));
        btnPanel.add(createScoreBtn(2005));
        panel.add(btnPanel, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(40, 10, 10, 10);
        final RoundedGradientButton playBtn = new RoundedGradientButton("PLAY");
        playBtn.setFont(new Font("Arial", Font.BOLD, 40)); 
        playBtn.setBackground(LABEL_COLOR);
        playBtn.setForeground(Color.BLACK);
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

    /**
     * Helper to create styled score selection buttons.
     */
    private JButton createScoreBtn(int score) {
        JButton b = new JButton(String.valueOf(score));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorderPainted(true);
        b.setBackground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, SCORE_FONT_SIZE)); 
        b.setPreferredSize(new Dimension(100, 45)); 
    
        b.addActionListener(e -> {
            this.selectedScore = score;
            for (JButton btn : scoreButtons) {
                btn.setBackground(Color.WHITE);
            }
            b.setBackground(SCORE_SELECTED_COLOR);
        }); 
        scoreButtons.add(b);
        return b;
    }

    /**
     * Helper to create styled labels for input prompts.
     */
    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(LABEL_COLOR);
        l.setFont(new Font("Arial", Font.BOLD, TITLE_FONT_SIZE));
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
