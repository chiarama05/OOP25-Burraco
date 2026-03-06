package view.start;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.*;
import javax.swing.*;

public class StartMenuViewImpl implements StartMenuView{

    private final JFrame frame;
    private final OnGameStartListener listener;

    public StartMenuViewImpl(OnGameStartListener listener) {
        this.listener = listener;
        this.frame = new JFrame("Burraco Game - Home");
        setupUI();
    }

    private void setupUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0, 102, 51));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel title = new JLabel("BURRACO GAME");
        title.setFont(new Font("Serif", Font.BOLD, 45));
        title.setForeground(Color.YELLOW);
        gbc.gridy = 0;
        panel.add(title, gbc);

        JButton startBtn = new JButton("START");
        startBtn.setPreferredSize(new Dimension(150, 50));
        startBtn.addActionListener(e -> {
            close();
            listener.onStartClicked(); // Notifica il Main di aprire il setup
        });
        gbc.gridy = 1;
        panel.add(startBtn, gbc);

        JButton rulesBtn = new JButton("REGOLE");
        rulesBtn.setPreferredSize(new Dimension(150, 50));
        rulesBtn.addActionListener(e -> showRules());
        gbc.gridy = 2;
        panel.add(rulesBtn, gbc);

        frame.add(panel);
    }

    private void showRules() {
        JOptionPane.showMessageDialog(frame, "Regole: ...", "Regole del Gioco", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override public void display() { 
        frame.setVisible(true); 
    }
    @Override public void close() { 
        frame.dispose(); 
    }
}

