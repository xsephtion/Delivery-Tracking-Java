package machineproblem1;

import com.alee.extended.label.WebStyledLabel;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class transactPanel extends JPanel implements ActionListener {

    //Variable declaration
    GridBagConstraints format = new GridBagConstraints();

    WebStyledLabel packageL = new WebStyledLabel("PACKAGE INFO");

    JLabel valueL = new JLabel("Package Value");
    JTextField valueTF = new JTextField(15);

    JLabel weightL = new JLabel("Package Weight");
    JTextField weightTF = new JTextField(15);
    
    JLabel itemName = new JLabel("Item Name");
    JTextField iName = new JTextField(15);
    
    JLabel qty = new JLabel("Quantity");
    JTextField Qty = new JTextField(15);
    
    JLabel delboyIDlbl = new JLabel("Delivery Boy ID");
    JTextField delboyID = new JTextField(15);

    WebStyledLabel receiverL = new WebStyledLabel("RECEIVER");

    JLabel receiverFNameL = new JLabel("First name");
    JTextField receiverFNameTF = new JTextField(15);

    JLabel receiverLNameL = new JLabel("Last name");
    JTextField receiverLNameTF = new JTextField(15);

    JLabel receiverAddrL = new JLabel("Address");
    JTextField receiverAddrTF = new JTextField(15);

    JLabel receiverContactNoL = new JLabel("Contact Number");
    JTextField receiverContactNoTF = new JTextField(15);

    JButton acceptB = new JButton("Accept");

    boolean validInfo;

    public transactPanel() {
        //set layout of transactPanel
        setLayout(new GridBagLayout());

        //set custom font styles and sizes
        packageL.setFontSizeAndStyle(15, true, false);
        receiverL.setFontSizeAndStyle(15, true, false);

        validInfo = false;

        //transactPanel label components initialization
        format.ipadx = 10;
        format.ipady = 10;
        format.gridx = 0;
        format.gridy = 0;

        format.gridwidth = 2;
        format.insets = new Insets(10, 10, 0, 0);
        add(packageL, format);

        format.gridwidth = 1;
        format.gridy = 1;
        format.anchor = GridBagConstraints.WEST;
        format.insets = new Insets(10, 10, 0, 0);
        add(valueL, format);

        format.gridy = 2;
        add(weightL, format);
        format.gridy = 3;
        add(itemName, format);
        format.gridy = 4;
        add(qty, format);
        format.gridy = 5;
        add(delboyIDlbl, format);

        format.gridx = 2;
        format.gridy = 0;
        format.gridwidth = 2;
        format.anchor = GridBagConstraints.CENTER;
        format.insets = new Insets(10, 30, 0, 0);
        add(receiverL, format);

        format.gridy = 1;
        format.gridwidth = 1;
        format.anchor = GridBagConstraints.WEST;
        format.insets = new Insets(10, 20, 0, 0);
        add(receiverFNameL, format);

        format.gridy = 2;
        add(receiverLNameL, format);

        format.gridy = 3;
        add(receiverAddrL, format);

        format.gridy = 4;
        add(receiverContactNoL, format);

        //transactPanel text field components initialization
        format.ipadx = 5;
        format.ipady = 5;
        format.gridx = 1;
        format.gridy = 1;

        format.insets = new Insets(10, 10, 0, 20);
        add(valueTF, format);

        format.gridy = 2;
        add(weightTF, format);
        format.gridy = 3;
        add(iName, format);
        format.gridy = 4;
        add(Qty, format);
        format.gridy = 5;
        add(delboyID, format);
        
        format.gridx = 3;

        format.gridy = 1;
        format.insets = new Insets(10, 10, 0, 0);
        add(receiverFNameTF, format);

        format.gridy = 2;
        add(receiverLNameTF, format);

        format.gridy = 3;
        add(receiverAddrTF, format);

        format.gridy = 4;
        add(receiverContactNoTF, format);

        //Accept button initialization
        acceptB.addActionListener(this);
        format.ipadx = 100;
        format.gridx = 0;
        format.gridy = 7;
        format.gridwidth = 4;
        format.anchor = GridBagConstraints.CENTER;
        format.insets = new Insets(20, 0, 0, 0);
        add(acceptB, format);
        
    }

    //method to validate whether the given regular expression
    //matches the text from the given text field
    private boolean textVerifier(JTextField textField, String regex) {
        if (textField.getText().equals("")) {
            return false;
        } else if (textField.getText().matches(regex)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == acceptB) {
            //validation for input
            validInfo = false;

            //regular expression in string variables
            String nameRegEx = "^[\\p{L} .'-]+$";
            String addRegEx = "^\\d+(\\s[A-z]+)+.*";
            String contactNumRegEx = "(\\d{7,11})";
            String numRegEx = "^(\\d*\\.)?\\d+$";

            //name validation
            if (!textVerifier(receiverFNameTF, nameRegEx)) {
                JOptionPane.showMessageDialog(this, "Invalid info!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!textVerifier(receiverLNameTF, nameRegEx)) {
                JOptionPane.showMessageDialog(this, "Invalid info!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //address validation
            if (!textVerifier(receiverAddrTF, addRegEx)) {
                JOptionPane.showMessageDialog(this, "Invalid info!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //number validation
            if (!textVerifier(receiverContactNoTF, contactNumRegEx)) {
                JOptionPane.showMessageDialog(this, "Invalid info!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!textVerifier(valueTF, numRegEx)) {
                JOptionPane.showMessageDialog(this, "Invalid info!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //shows error message when any of the input is invalid
            if (!textVerifier(weightTF, numRegEx)) {
                JOptionPane.showMessageDialog(this, "Invalid info!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Checking if address is a valid location. (Must be within Manila)
            try {
                GeoApiContext context = new GeoApiContext.Builder().apiKey("").build();
                String[] originAddress = new String[1];
                originAddress[0] = receiverAddrTF.getText();
                
                GeocodingResult[] results = GeocodingApi.geocode(context, originAddress[0]).await();
                
                System.out.println(results[0].formattedAddress);
                
                if (results.length == 0) {
                    JOptionPane.showMessageDialog(this, "Invalid address. Address does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (!results[0].formattedAddress.contains("Manila")) {
                    JOptionPane.showMessageDialog(this, "Invalid address. Address must be within Manila.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (ApiException | InterruptedException | IOException exe) {
                exe.printStackTrace();
            }

            //if all info is valid
            //info entered in text fields will be inserted
            //as rows in the database tables
            //open db connection here
            String recfname = receiverFNameTF.getText();
            String reclname = receiverLNameTF.getText();
            String recaddress = receiverAddrTF.getText();
            long recconnum = Long.parseLong(receiverContactNoTF.getText());
            double recpackval = Double.parseDouble(valueTF.getText());
            double recpackweight = Double.parseDouble(weightTF.getText());
            String emailaddress = signUpFrame.getEmail();
            String delboyid = delboyID.getText();
            int delboyidnum = Integer.parseInt(delboyid);

            String dbURL = "jdbc:oracle:thin:@10.22.14.215:1521:orcl";
            String username = "BH2_TANJ_N21";
            String password = "oracle";
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs;
            try {
                conn = DriverManager.getConnection(dbURL, username, password);

                String alterDateFormat = "ALTER SESSION SET NLS_DATE_FORMAT = 'DD-Mon-YYYY HH24:MI:SS'";
                ps = conn.prepareStatement(alterDateFormat);
                ps.executeUpdate();

                String query = "insert into package(transacid, recfname, reclname, recaddress, reconnum, packvalue, packweight, emailaddress, packdate, boyid)"
                        + "values(TRANSAC_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, (select to_char(sysdate, 'DD-Mon-YYYY HH24:MI:SS') as \"Current Time\" from dual), ?)";

                ps = conn.prepareStatement(query);
                ps.setString(1, recfname);
                ps.setString(2, reclname);
                ps.setString(3, recaddress);
                ps.setLong(4, recconnum);
                ps.setDouble(5, recpackval);
                ps.setDouble(6, recpackweight);
                ps.setString(7, emailaddress);
                ps.setInt(8, delboyidnum);
                ps.executeUpdate();
                
                int transacid;
                
                ps = conn.prepareStatement("SELECT transacid FROM package WHERE boyid = ?");
                ps.setInt(1, delboyidnum);
                
                rs = ps.executeQuery();
                
                //try catch should be indicated here
               
                while (rs.next()){
                    transacid = rs.getInt(1);
                    String transid = Integer.toString(transacid);
                    
                    if (transid != null){
                       JOptionPane.showMessageDialog(null, "Delivery Boy is unavailable", null, JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                

                if (conn != null) {
                    JOptionPane.showMessageDialog(this, "Package info has been inserted", null, JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            
            try {
                conn = DriverManager.getConnection(dbURL, username, password);
                String name = iName.getText();
                String qt = Qty.getText();
                String query = "insert into item(itemid,item_name,item_qty ) values(ITEM_VAL.NEXTVAL, ?, ?)";

                ps = conn.prepareStatement(query);
                ps.setString(1, name);
                ps.setString(2, qt);
                ps.executeUpdate();
                if (conn != null) {
                    JOptionPane.showMessageDialog(this, "Package info has been inserted", null, JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
        }
    }
}
