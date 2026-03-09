package view.start;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.*;

public class SetUpMenuViewImpl implements SetUpMenuView{

    private final JFrame frame;
    private final OnConfigurationCompleteListener listener;
    private JTextField name1, name2;

    public SetUpMenuViewImpl(OnConfigurationCompleteListener listener) {
        this.listener = listener;
        this.frame = new JFrame("Game Configuration");
        setupUI();
    }

    private void setupUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Name Player 1:"));
        name1 = new JTextField("");
        panel.add(name1);

        panel.add(new JLabel("Name Player 2:"));
        name2 = new JTextField("");
        panel.add(name2);

        panel.add(new JLabel("Select Victory Score:"));
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(createScoreBtn(1005));
        btnPanel.add(createScoreBtn(1505));
        btnPanel.add(createScoreBtn(2005));
        panel.add(btnPanel);

        frame.add(panel);
    }

    private JButton createScoreBtn(int score) {
        JButton b = new JButton(String.valueOf(score));
        b.addActionListener(e -> {
            close();
            listener.onConfigComplete(score, name1.getText(), name2.getText());
        });
        return b;
    }

    @Override public void display() { 
        frame.setVisible(true); 
    }
    @Override public void close() { 
        frame.dispose(); 
    }

}
