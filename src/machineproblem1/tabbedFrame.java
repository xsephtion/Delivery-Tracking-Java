package machineproblem1;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class tabbedFrame extends JFrame {
    
    JTabbedPane tabs = new JTabbedPane();
    deliveryPanel deliverP = new deliveryPanel();
    searchPanel searchP = new searchPanel();
    
    //tabs
    transactPanel transactP = new transactPanel();
    
    ImageIcon transactIcon;
    ImageIcon deliverIcon;
    ImageIcon searchIcon;
    
    public tabbedFrame() {
        super("Welcome!");
        
        //set up tab icons
        transactIcon = new ImageIcon("transact.png");
        deliverIcon = new ImageIcon("delivery.png");
        searchIcon = new ImageIcon("search.png");
        
        //add tabs
        tabs.addTab("Transactions", transactIcon, transactP, "Input info for transactions.");
        tabs.addTab("Delivery", deliverIcon, deliverP, "Initiate package delivery here.");
        tabs.addTab("Search", searchIcon, searchP, "Search for transactions.");
        
        add(tabs);
        
        setBounds(500, 100, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
