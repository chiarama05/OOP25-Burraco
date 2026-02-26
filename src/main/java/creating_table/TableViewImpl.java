import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class TableViewImpl{
    
    private final JFrame frame;
    private final JLabel turnolabel;

    private final JPanel combPanel1;//
    private final JPanel combPanel2;//
    private final JPanel scartiPanel;//
    private final JPanel manoPanel;//

    private final Font baseTitleFont= new Font("Arial", Font.BOLD, 32);
    private final Font baseLabelFont = new Font("Arial", Font.BOLD, 22);

    public TabelViewImpl(){
        frame= new JFrame ("Burraco - OOOP Project");
        frame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        frame.setLayout(new BorderLayout());
    }
}