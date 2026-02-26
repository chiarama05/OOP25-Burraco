import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class TableViewImpl{
    
    private final JFrame frame;
    private final JLabel turnoLabel;

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

        //turno
        turnoLabel = new JLabel("Turno: Giocatore1");
        turnoLabel.setFont(baseTitleFont);
        turnoLabel.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        frame.add(turnoLabel,BorderLayout.NORTH);

        //combinazioni
        JPanel combinazioniPanel = new JPanel(new GridLayout(1,2,20,10));

        combPanel1=new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        combPanel1.setBorder(BorderFactory.createTitledBorder("Giocatore 1"));

        JScrollPane scroll1=new JScrollPane(combPanel1);
        scroll1.setBorder(BorderFactory.createEmptyBorder());
        combinazioniPanel.add(scroll1);

        combPanel2=new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        combPanel2.setBorder(BorderFactory.createTitledBorder("Giocatore 2"));
        JScrollPane scroll2 = new JScrollPane(combPanel2);
        scroll2.setBorder(BorderFactory.createEmptyBorder());
        combinazioniPanel.add(scroll2);

        frame.add(combinazioniPanel, BorderLayout.CENTER);

        //scarti
        scartiPanel=new JPanel (new FlowLayout(FlowLayout.LEFT,5,5));
        scartiPanel.setBorder(BorderFactory.createTitledBorder("Scarti"));
        scartiPanel.setBackground(new Color (250,250,240));


        //mano
        manoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
        manoPanel.setBorder(BorderFactory.createTitledBorder("Mano"));


        //contenitore inferiore
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(scartiPanel,BorderLayout.NORTH);
        bottomPanel.add(manoPanel,BorderLayout.CENTER);
        frame.add(bottomPanel,BorderLayout.SOUTH);


        //resize-responsive
        frame.addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e ){
                applyResponsiveFonts();
            }
        });

        //mostra ridimensionata
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(900,600));

        frame.pack();
        frame.setVisible(true);
        applyResponsiveFonts(); 

    }
}