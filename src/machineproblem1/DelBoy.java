/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machineproblem1;

import com.alee.extended.label.WebStyledLabel;
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
import static machineproblem1.signUpFrame.setEmail;

/**
 *
 * @author joseph
 */
public class DelBoy extends JFrame implements ActionListener {
    
    
    public DelBoy(){
        super("Delivery Boy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        add(new delReg());
        setBounds(500, 100, 800, 600);
    }
    
    class delReg extends JPanel implements ActionListener{
        GridBagConstraints format = new GridBagConstraints();

        //jcomponent declaration + initialization
        WebStyledLabel signUpTitle = new WebStyledLabel("REGISTER");

        

        JLabel signUpFNameL = new JLabel("First Name:");
        JTextField signUpFNameTF = new JTextField(15);

        JLabel signUpLNameL = new JLabel("Last Name:");
        JTextField signUpLNameTF = new JTextField(15);

        JLabel signUpAddrL = new JLabel("Address:");
        JTextField signUpAddrTF = new JTextField(15);
        JButton signUpBtn = new JButton("Customer: Sign Up");
        
        public delReg(){
             //set layout of signUpPanel
            setLayout(new GridBagLayout());

            //set custom font styles and sizes
            signUpTitle.setFontSizeAndStyle(15, true, false);

            

            //signUpPanel label components initialization
            format.ipadx = 10;
            format.ipady = 10;
            format.gridx = 0;
            format.gridy = 0;

           
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

            //signUpPanel text field components initialization
            format.ipadx = 5;
            format.ipady = 5;
            format.gridx = 1;
            format.gridy = 1;

            format.gridx = 3;

            format.gridy = 1;
            format.insets = new Insets(10, 10, 0, 0);
            add(signUpFNameTF, format);

            format.gridy = 2;
            add(signUpLNameTF, format);

            format.gridy = 3;
            add(signUpAddrTF, format);

           

            //buttons initialization
            format.gridx = 0;
            format.gridy = 4;
            format.ipadx = 40;
            format.ipady = 20;
            format.gridwidth = 2;
            format.anchor = GridBagConstraints.CENTER;
            format.insets = new Insets(20, 10, 0, 0);
            ;

            format.gridx = 2;
            format.gridy = 8;
            format.gridx = 2;
            format.gridy = 9;
            signUpBtn.addActionListener(this);
            add(signUpBtn, format);
            
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
               
                //email validation
                

                //DATABASE CODES
                //open db connection here
                String dbURL = "jdbc:oracle:thin:@10.22.14.215:1521:orcl";
                String username = "BH2_TANJ_N21";
                String password = "oracle";
                Connection conn = null;
                PreparedStatement ps = null;
                Statement statement = null;
                ResultSet rs = null;
                String lname = signUpLNameTF.getText(); 
                String fname = signUpFNameTF.getText();
                try {
                    conn = DriverManager.getConnection(dbURL, username, password);
                    statement = conn.createStatement();
                    String sql = "select lname from sender where lname = '" +lname+" ' and "+"fname= '" +fname + "'" ;
                    rs = statement.executeQuery(sql);
                    
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Last name already exists!", "Error!", JOptionPane.ERROR_MESSAGE);
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
                
                try {
                     conn = DriverManager.getConnection(dbURL, username, password);
                     String query = "insert into delboy(boyid,fname, lname, address) values(TRANSAC_SEQ.NEXTVAL, ?, ?, ?)";
                     ps = conn.prepareStatement(query); 
                     ps.setString(1, sfname);
                     ps.setString(2, slname); 
                     ps.setString(3, saddress);
                     ps.executeUpdate(); 
                } catch (SQLException ex) {
                     ex.printStackTrace();
                }
                
                //if email/password is correct
                JOptionPane.showConfirmDialog(this, "Sign up successful!", "Success!", JOptionPane.PLAIN_MESSAGE);
                
                //setting current user of program
                
                
                //close signUp frame
                //open tabbedPanel frame
                DelBoy.this.dispose();
                DelBoy.this.setVisible(false);    
            }
        }
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
