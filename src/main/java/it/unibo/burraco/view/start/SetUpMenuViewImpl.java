package it.unibo.burraco.view.start;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.unibo.burraco.view.colorbutton.RoundedGradientButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList; 
import java.util.List;

/**
 * Swing-based implementation of the SetUpMenuView.
 * Provides a graphical interface for configuring game parameters such as 
 * player names and the target victory score.
 */
public final class SetUpMenuViewImpl implements SetUpMenuView {

    private static final String FONT_NAME = "Arial";

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
    private static final int FIELD_FONT_SIZE = 20;

    private static final int BACK_BTN_WIDTH = 120;
    private static final int BACK_BTN_HEIGHT = 35;
    private static final int PLAY_BTN_WIDTH = 250;
    private static final int PLAY_BTN_HEIGHT = 70;
    private static final int PLAY_FONT_SIZE = 40;
    private static final int SCORE_BTN_WIDTH = 100;
    private static final int SCORE_BTN_HEIGHT = 45;

    private static final int INSET_LARGE = 40;
    private static final int INSET_SMALL = 10;
    private static final int INSET_STD = 12;
    private static final int INSET_SIDE = 15;
    private static final int FLOW_GAP_H = 20;
    private static final int FLOW_GAP_V = 10;

    private static final int SCORE_1 = 1005;
    private static final int SCORE_2 = 1505;
    private static final int SCORE_3 = 2005;

    private final JFrame frame;
    private final OnConfigurationCompleteListener listener;
    private final List<JButton> scoreButtons = new ArrayList<>();
    private JTextField name1;
    private JTextField name2;
    private int selectedScore = -1;

    /**
     * Constructor for the configuration menu.
     * 
     * @param listener the listener handling the configuration completion events.
     */
    public SetUpMenuViewImpl(final OnConfigurationCompleteListener listener) {
        this.listener = listener;
        this.frame = new JFrame("Game Configuration");
        this.setupUI();
    }

    /**
     * Initializes components and sets up the GridBagLayout.
     */
    private void setupUI() {
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.frame.setResizable(true);
        this.frame.setLocationRelativeTo(null);

        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        final GridBagConstraints gbc = new GridBagConstraints();
    
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(INSET_STD, INSET_SIDE, INSET_STD, INSET_SIDE);
    
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;

        final RoundedGradientButton backBtn = new RoundedGradientButton("← BACK");
        backBtn.setFont(new Font(FONT_NAME, Font.BOLD, BUTTON_FONT_SIZE));
        backBtn.setBackground(BUTTON_BG_COLOR);
        backBtn.setPreferredSize(new Dimension(BACK_BTN_WIDTH, BACK_BTN_HEIGHT));
        backBtn.addActionListener(e -> {
            this.close();
            this.listener.onBackClicked();
        });
        panel.add(backBtn, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 1;
        panel.add(this.createLabel("Name Player 1:"), gbc);
        gbc.gridy = 2;
        this.name1 = new JTextField("Player 1", FIELD_FONT_SIZE);
        this.name1.setFont(new Font(FONT_NAME, Font.PLAIN, FIELD_FONT_SIZE));
        this.name1.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        this.name1.setHorizontalAlignment(JTextField.LEFT);
        panel.add(this.name1, gbc);

        gbc.gridy = 3;
        panel.add(this.createLabel("Name Player 2:"), gbc);
        gbc.gridy = 4;
        this.name2 = new JTextField("Player 2", FIELD_FONT_SIZE);
        this.name2.setFont(new Font(FONT_NAME, Font.PLAIN, FIELD_FONT_SIZE));
        this.name2.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        this.name2.setHorizontalAlignment(JTextField.LEFT);
        panel.add(this.name2, gbc);

        gbc.gridy = 5;
        panel.add(this.createLabel("Points to win:"), gbc);
        gbc.gridy = 6;
        final JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, FLOW_GAP_H, FLOW_GAP_V));
        btnPanel.setOpaque(false);
        btnPanel.add(this.createScoreBtn(SCORE_1));
        btnPanel.add(this.createScoreBtn(SCORE_2));
        btnPanel.add(this.createScoreBtn(SCORE_3));
        panel.add(btnPanel, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(INSET_LARGE, INSET_SMALL, INSET_SMALL, INSET_SMALL);
        final RoundedGradientButton playBtn = new RoundedGradientButton("PLAY");
        playBtn.setFont(new Font(FONT_NAME, Font.BOLD, PLAY_FONT_SIZE));
        playBtn.setBackground(LABEL_COLOR);
        playBtn.setForeground(Color.BLACK);
        playBtn.setPreferredSize(new Dimension(PLAY_BTN_WIDTH, PLAY_BTN_HEIGHT));
        playBtn.addActionListener(e -> {
            if (this.selectedScore == -1) {
                JOptionPane.showMessageDialog(this.frame, "Please select a victory score first!");
            } else {
                close();
                this.listener.onConfigComplete(this.selectedScore, this.name1.getText(), this.name2.getText());
            }
        });
        panel.add(playBtn, gbc);

        this.frame.add(panel);
    }

    /**
     * Helper to create styled score selection buttons.
     * 
     * @param score the numeric value for the button
     * @return the styled JButton
     */
    private JButton createScoreBtn(final int score) {
        final JButton b = new JButton(String.valueOf(score));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorderPainted(true);
        b.setBackground(Color.WHITE);
        b.setFont(new Font(FONT_NAME, Font.BOLD, SCORE_FONT_SIZE));
        b.setPreferredSize(new Dimension(SCORE_BTN_WIDTH, SCORE_BTN_HEIGHT));
        b.addActionListener(e -> {
            this.selectedScore = score;
            for (final JButton btn : this.scoreButtons) {
                btn.setBackground(Color.WHITE);
            }
            b.setBackground(SCORE_SELECTED_COLOR);
        });
        this.scoreButtons.add(b);
        return b;
    }

    /**
     * Helper to create styled labels for input prompts.
     * 
     * @param text the label text
     * @return the styled JLabel
     */
    private JLabel createLabel(final String text) {
        final JLabel l = new JLabel(text);
        l.setForeground(LABEL_COLOR);
        l.setFont(new Font(FONT_NAME, Font.BOLD, TITLE_FONT_SIZE));
        l.setHorizontalAlignment(JLabel.LEFT);
        return l;
    }

    @Override 
    public void display() { 
        this.frame.setVisible(true);
    }

    @Override 
    public void close() { 
        this.frame.dispose();
    }
}
