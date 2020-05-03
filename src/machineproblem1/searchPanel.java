package machineproblem1;

import com.alee.extended.label.WebStyledLabel;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class searchPanel extends JPanel implements ActionListener {

    //declare + initialize panel layout
    GridBagConstraints format = new GridBagConstraints();
    JTable searchTable = new JTable();

    //declare + initialize jComponents
    WebStyledLabel titleL = new WebStyledLabel("SEARCH TRANSACTIONS");
    JLabel searchL = new JLabel("Enter receiver's last name:");
    JTextField searchTF = new JTextField(15);
    JButton searchB = new JButton("Search");
    DefaultTableModel model;
    
    boolean validInfo = false;

    public searchPanel() {
        setLayout(new GridBagLayout());

        //set custom style and size for label
        titleL.setFontSizeAndStyle(15, true, false);

        //add a scrollpane for the table
        JScrollPane scrollPane = new JScrollPane(searchTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(searchTable);

        //action listener for the search button
        searchB.addActionListener(this);

        //format of the gridbaglayout
        //adding components to the panel
        format.ipadx = 10;
        format.ipady = 10;
        format.gridx = 0;
        format.gridy = 0;

        format.gridwidth = 3;
        format.insets = new Insets(20, 0, 0, 0);
        add(titleL, format);

        format.gridy = 1;
        format.gridwidth = 1;
        format.insets = new Insets(20,100,0,0);
        add(searchL, format);

        format.ipadx = 200;
        format.ipady = 10;
        format.gridx = 1;
        format.insets = new Insets(20,0, 0, -100);
        add(searchTF, format);

        format.ipadx = 50;
        format.ipady = 20;
        format.gridx = 2;
        format.insets = new Insets(20,0,0,0);
        add(searchB, format);

        format.ipadx = 700;
        format.ipady = 100;
        format.gridy = 5;
        format.gridx = 0;
        format.gridwidth = 3;
        add(scrollPane, format);
        
        //set entire table to not be editable
        searchTable.setModel(new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
               //all cells false
               return false;
            }
        });

        model = (DefaultTableModel) searchTable.getModel();

        model.addColumn("Trans. #");
        model.addColumn("First Name");
        model.addColumn("Last Name");
        model.addColumn("Address");
        model.addColumn("Contact No.");
        model.addColumn("Weight");
        model.addColumn("Value");
        model.addColumn("Sender Email");
        model.addColumn("Date");
        model.addColumn("Delivery Boy");

        //align center column headers
        TableCellRenderer rendererFromHeader = searchTable.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        

        setVisible(true);
    }

    private void populateData() {
        //DATABASE CODES
        //these are sample codes for testing only
        //edit/delete nalang

        
        String dbURL = "jdbc:oracle:thin:@10.22.14.215:1521:orcl";
        String username = "BH2_TANJ_N21";
        String password = "oracle";
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(dbURL, username, password);
            statement = conn.createStatement();
            String sql = "select * from package order by transacid";
            rs = statement.executeQuery(sql);
            int row = 0;
            
            while (rs.next()) {
                if(stringSearch(searchTF.getText(), rs.getString("reclname"))){
                    model.addRow(new Object[0]);
                    model.setValueAt(rs.getInt("transacid"), row, 0);
                    model.setValueAt(rs.getString("recfname"), row, 1);
                    model.setValueAt(rs.getString("reclname"), row, 2);
                    model.setValueAt(rs.getString("recaddress"), row, 3);
                    model.setValueAt(rs.getString("reconnum"), row, 4);
                    model.setValueAt(rs.getInt("packweight"), row, 5);
                    model.setValueAt(rs.getDouble("packvalue"), row, 6);
                    model.setValueAt(rs.getString("emailaddress"), row, 7);
                    model.setValueAt(rs.getDate("packdate"), row, 8);
                    model.setValueAt(rs.getInt("boyid"), row, 9);
                    row++;
                }
            }

            conn.close();
            statement.close();
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        //add database connection code here
        //source: http://www.thaicreate.com/java/java-gui-workshop-search-data.html
        //alternate solution: https://stackoverflow.com/questions/10620448/most-simple-code-to-populate-jtable-from-resultset
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchB) {
            //checks if input matches the given regular expression
            validInfo = searchTF.getText().matches("^[\\p{L} .'-]+$");

            //shows error message when search input is invalid
            if (!validInfo) {
                JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //if input is valid
            model.setRowCount(0);
            populateData();
        }
    }
    
    public boolean stringSearch(String pattern, String names){
	int nameLength = names.length();
	int patternLength = pattern.length();
	int patternFound = 0;
	
	for (int i = 0; i < nameLength; i++) {
            
            if (pattern.charAt(0) == names.charAt(i)) {
                
                patternFound = 0;
                        
                for (int j = i; patternFound < patternLength; j++) {
                    
                    if (pattern.charAt(patternFound) == names.charAt(j)) {
                        patternFound++;
                        if(patternFound == patternLength){
                            return true;
                        }
                    }
                    else{
                        break;
                    }
                }
                
            }
            
        }
        return false;
    }
    
}
