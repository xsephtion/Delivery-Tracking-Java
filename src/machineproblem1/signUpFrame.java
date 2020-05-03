package machineproblem1;

import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.WebLookAndFeel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class signUpFrame extends JFrame implements ActionListener {
    
    private static String email;
    
    public static String getEmail(){
        return email;
    }
    
    public static void setEmail(String email_){
        email = email_;
    }
    
    public signUpFrame() {
        super("Log-in/Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        add(new signUpPanel());
        setBounds(500, 100, 800, 600);
    }

    class signUpPanel extends JPanel implements ActionListener {

        GridBagConstraints format = new GridBagConstraints();

        //jcomponent declaration + initialization
        WebStyledLabel logInTitle = new WebStyledLabel("LOG IN");
        WebStyledLabel signUpTitle = new WebStyledLabel("REGISTER");

        JLabel logInEmailL = new JLabel("E-mail Address:");
        JTextField logInEmailTF = new JTextField(15);

        JLabel logInPassL = new JLabel("Password:");
        JPasswordField logInPassTF = new JPasswordField(15);

        JLabel signUpFNameL = new JLabel("First Name:");
        JTextField signUpFNameTF = new JTextField(15);

        JLabel signUpLNameL = new JLabel("Last Name:");
        JTextField signUpLNameTF = new JTextField(15);

        JLabel signUpAddrL = new JLabel("Address:");
        JTextField signUpAddrTF = new JTextField(15);

        JLabel signUpContactNoL = new JLabel("Contact Number:");
        JTextField signUpContactNoTF = new JTextField(15);

        JLabel signUpEmailL = new JLabel("E-mail Address:");
        JTextField signUpEmailTF = new JTextField(15);

        JLabel signUpPassL = new JLabel("Password:");
        JPasswordField signUpPassTF = new JPasswordField(15);

        JCheckBox logInShowPass = new JCheckBox("Show Password");
        JCheckBox signUpShowPass = new JCheckBox("Show Password");

        JButton logInBtn = new JButton("Log In");
        JButton signUpBtn = new JButton("Customer: Sign Up");
        JButton signUpBtnD = new JButton("Click here to register as delivery boy");

        boolean validInfo;

        public signUpPanel() {
            //set layout of signUpPanel
            setLayout(new GridBagLayout());

            //set custom font styles and sizes
            logInTitle.setFontSizeAndStyle(15, true, false);
            signUpTitle.setFontSizeAndStyle(15, true, false);

            validInfo = false;

            //signUpPanel label components initialization
            format.ipadx = 10;
            format.ipady = 10;
            format.gridx = 0;
            format.gridy = 0;

            format.gridwidth = 2;
            format.insets = new Insets(20, 10, 0, 0);
            add(logInTitle, format);

            format.gridwidth = 1;
            format.gridy = 1;
            format.anchor = GridBagConstraints.WEST;
            format.insets = new Insets(10, 10, 0, 0);
            add(logInEmailL, format);

            format.gridy = 2;
            add(logInPassL, format);

            format.gridx = 2;
            format.gridy = 0;
            format.gridwidth = 2;
            format.anchor = GridBagConstraints.CENTER;
            format.insets = new Insets(20, 30, 0, 0);
            add(signUpTitle, format);

            format.gridy = 1;
            format.gridwidth = 1;
            format.anchor = GridBagConstraints.WEST;
            format.insets = new Insets(10, 20, 0, 0);
            add(signUpFNameL, format);

            format.gridy = 2;
            add(signUpLNameL, format);

            format.gridy = 3;
            add(signUpAddrL, format);

            format.gridy = 4;
            add(signUpContactNoL, format);

            format.gridy = 5;
            add(signUpEmailL, format);

            format.gridy = 6;
            add(signUpPassL, format);

            //signUpPanel text field components initialization
            format.ipadx = 5;
            format.ipady = 5;
            format.gridx = 1;
            format.gridy = 1;

            format.insets = new Insets(10, 10, 0, 20);
            add(logInEmailTF, format);

            format.gridy = 2;
            add(logInPassTF, format);

            format.gridy = 3;
            logInShowPass.addActionListener(this);
            add(logInShowPass, format);

            format.gridx = 3;

            format.gridy = 1;
            format.insets = new Insets(10, 10, 0, 0);
            add(signUpFNameTF, format);

            format.gridy = 2;
            add(signUpLNameTF, format);

            format.gridy = 3;
            add(signUpAddrTF, format);

            format.gridy = 4;
            add(signUpContactNoTF, format);

            format.gridy = 5;
            add(signUpEmailTF, format);

            format.gridy = 6;
            add(signUpPassTF, format);

            format.gridy = 7;
            signUpShowPass.addActionListener(this);
            add(signUpShowPass, format);

            //buttons initialization
            format.gridx = 0;
            format.gridy = 4;
            format.ipadx = 40;
            format.ipady = 20;
            format.gridwidth = 2;
            format.anchor = GridBagConstraints.CENTER;
            format.insets = new Insets(20, 10, 0, 0);
            logInBtn.addActionListener(this);
            add(logInBtn, format);

            format.gridx = 2;
            format.gridy = 8;
            signUpBtn.addActionListener(this);
            add(signUpBtn, format);
            format.gridx = 2;
            format.gridy = 9;
            signUpBtnD.addActionListener(this);
            add(signUpBtnD, format);
        }

        private boolean textVerifier(JTextField textField, String regex) {
            if (textField.getText().equals("")) {
                return false;
            }else if(textField.getText().matches(regex)){
                return true;
            }
            else {
                return false;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //shows password as plaintext
            if (e.getSource() == logInShowPass) {
                if (logInShowPass.isSelected()) {
                    logInPassTF.setEchoChar((char) 0);
                } else {
                    logInPassTF.setEchoChar('*');
                }
            }

            if (e.getSource() == signUpShowPass) {
                if (signUpShowPass.isSelected()) {
                    signUpPassTF.setEchoChar((char) 0);
                } else {
                    signUpPassTF.setEchoChar('*');
                }
            }

            //validation for input
            validInfo = false;

            //regular expression in string variables
            String emailRegEx = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"
                    + "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            //source: https://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
            if(e.getSource() == signUpBtnD){
                WebLookAndFeel.install();
                DelBoy dBoy = new DelBoy();
                dBoy.setVisible(true);
            }
            if (e.getSource() == logInBtn) {
                //email validation
                validInfo = textVerifier(logInEmailTF, emailRegEx);

                //shows error message when e-mail address is invalid
                if (!validInfo) {
                    JOptionPane.showMessageDialog(this, "Invalid e-mail format!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                //stores email and password into string
                String email = logInEmailTF.getText().trim();
                String pass = new String(logInPassTF.getPassword());
                
                //DATABASE CODES
                //open db connection to verify if password/email is valid
                String dbURL = "jdbc:oracle:thin:@10.22.14.215:1521:orcl";
                String username = "BH2_TANJ_N21";
                String password = "oracle";
                Connection conn = null;
                Statement statement = null;
                ResultSet rs = null;
                boolean emailFound = false;

                try {
                    conn = DriverManager.getConnection(dbURL, username, password);
                    statement = conn.createStatement();
                    String sql = "select emailaddress,pass from sender where emailaddress = '" + email + "' and "
                            + "pass = '" + pass + "'";
                    rs = statement.executeQuery(sql);
                    
                    if(rs.next()){
                        JOptionPane.showConfirmDialog(this, "Log in successful!", "Success!", JOptionPane.PLAIN_MESSAGE);
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "Invalid email/password!", "Error!", JOptionPane.ERROR_MESSAGE);
                        conn.close();
                        statement.close();
                        rs.close();
                        return;
                    }
                    conn.close();
                    statement.close();
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
                //setting current user of program
                setEmail(email);
                
                //close signUp frame
                //open tabbedPanel frame
                signUpFrame.this.dispose();
                signUpFrame.this.setVisible(false);

                tabbedFrame tabbed = new tabbedFrame();
                tabbed.setVisible(true);
            }

            
            
            if (e.getSource() == signUpBtn) {

                //regular expression in string variables
                String nameRegEx = "^[\\p{L} .'-]+$";
                String addRegEx = "^\\d+(\\s[A-z]+)+.*";
                String numRegEx = "(\\d{7,11})";

                //shows error message when any of the input is invalid
                //name validation
                if (!textVerifier(signUpFNameTF, nameRegEx)) {
                    JOptionPane.showMessageDialog(this, "1Invalid information!\n"
                            + "Ensure all information entered is correct.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!textVerifier(signUpLNameTF, nameRegEx)) {
                    JOptionPane.showMessageDialog(this, "2Invalid information!\n"
                            + "Ensure all information entered is correct.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //address validation
                if (!textVerifier(signUpAddrTF, addRegEx)) {
                    JOptionPane.showMessageDialog(this, "3Invalid information!\n"
                            + "Ensure all information entered is correct.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //number validation
                if (!textVerifier(signUpContactNoTF, numRegEx)) {
                    JOptionPane.showMessageDialog(this, "4Invalid information!\n"
                            + "Ensure all information entered is correct.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //email validation
                if (!textVerifier(signUpEmailTF, emailRegEx)) {
                    JOptionPane.showMessageDialog(this, "5Invalid information!\n"
                            + "Ensure all information entered is correct.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //store email into string
                String email = signUpEmailTF.getText();

                //DATABASE CODES
                //open db connection here
                String dbURL = "jdbc:oracle:thin:@10.22.14.215:1521:orcl";
                String username = "BH2_TANJ_N21";
                String password = "oracle";
                Connection conn = null;
                PreparedStatement ps = null;
                Statement statement = null;
                ResultSet rs = null;

                //email should not already be existing in dbase
                try {
                    conn = DriverManager.getConnection(dbURL, username, password);
                    statement = conn.createStatement();
                    String sql = "select emailaddress from sender where emailaddress = '" + email + "'";
                    rs = statement.executeQuery(sql);
                    
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "E-mail already exists!", "Error!", JOptionPane.ERROR_MESSAGE);
                        conn.close();
                        statement.close();
                        rs.close();
                        return;
                    }

                    conn.close();
                    statement.close();
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                
                //if all info is valid
                //info entered in text fields will be inserted
                //as rows in the database tables
                String sfname = signUpFNameTF.getText();
                String slname  = signUpLNameTF.getText(); 
                String saddress = signUpAddrTF.getText();
                String spassword = new String(signUpPassTF.getPassword());
                long sconnum = Long.parseLong(signUpContactNoTF.getText());  
                try {
                     conn = DriverManager.getConnection(dbURL, username, password);
                     String query = "insert into sender(fname, lname, address,connum, emailaddress, pass) values(?, ?, ?, ?, ?, ?)";
                     ps = conn.prepareStatement(query); 
                     ps.setString(1, sfname);
                     ps.setString(2, slname); 
                     ps.setString(3, saddress);
                     ps.setLong(4, sconnum);
                     ps.setString(5, email);
                     ps.setString(6, spassword);
                     ps.executeUpdate(); 
                } catch (SQLException ex) {
                     ex.printStackTrace();
                }
                
                //if email/password is correct
                JOptionPane.showConfirmDialog(this, "Sign up successful!", "Success!", JOptionPane.PLAIN_MESSAGE);
                
                //setting current user of program
                setEmail(email);
                
                //close signUp frame
                //open tabbedPanel frame
                signUpFrame.this.dispose();
                signUpFrame.this.setVisible(false);    
                
                tabbedFrame tabbed = new tabbedFrame();
                tabbed.setVisible(true);
            }
            
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
