package machineproblem1;

import com.alee.extended.label.WebStyledLabel;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class deliveryPanel extends JPanel implements ActionListener {

    //Variable Declaration for algo problems
    Set<Set<Package>> bestSetOfPackages = new HashSet<Set<Package>>();

    class Package {
        int transNo;
        double value;
        double weight;
        String address; //Address of receiver

        public Package(int transNo_, double value_, double weight_, String address_) {
            transNo = transNo_;
            value = value_;
            weight = weight_;
            address = address_;
        }
    }

    //JFrame components declaration
    static GridBagConstraints format = new GridBagConstraints();

    JPanel weightLimitPanel = new JPanel(new GridBagLayout());
    JLabel weightLimitL = new JLabel("Enter delivery truck weight limit.");
    JTextField weightLimitTF = new JTextField();
    JButton weightLimitB = new JButton("Accept");

    JPanel knapsackPanel = new JPanel(new GridBagLayout());
    JPanel powerSetPanel = new JPanel(new GridBagLayout());
    JScrollPane powerSetScrollPane = new JScrollPane(powerSetPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    WebStyledLabel powerSetHeader = new WebStyledLabel("Knapsack Problem by Exhaustive Search");
    JLabel[] subSetL;
    JLabel[] subSetWeightL;
    JLabel[] subSetValueL;

    JPanel solutionPanel = new JPanel(new GridBagLayout());
    JScrollPane solutionScrollPane = new JScrollPane(solutionPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JLabel solutionHeader = new JLabel("Package(s) with the most value without going over the weight limit");
    JLabel[] subSetSolutionL;
    JLabel[] subSetSolutionWeightL;
    JLabel[] subSetSolutionValueL;
    JButton[] subSetSolutionButtonL;
    JButton noSolutionB = new JButton("Return");

    static JPanel deliveryPanel = new JPanel(new GridBagLayout());
    static JLabel deliveryHeaderL = new JLabel();
    static JLabel[] deliveryLocationL;
    static JLabel[] deliveryDistanceL;
    static JButton deliveryReturnB = new JButton("Return");

    public deliveryPanel() {
        setLayout(new CardLayout());

        //weightLimitPanel Initialization
        format.gridx = 0;
        format.gridy = 0;
        format.insets = new Insets(10,10,10,10);
        weightLimitPanel.add(weightLimitL, format);

        format.gridy = 1;
        format.ipadx = 140;
        format.ipady = 10;
        weightLimitPanel.add(weightLimitTF, format);

        weightLimitB.addActionListener(this);
        format.gridy = 2;
        format.ipadx = 50;
        format.ipady = 20;
        weightLimitPanel.add(weightLimitB, format);

        //knapsackPanel Initialization
        powerSetHeader.setFontSizeAndStyle(15, true, false);
        format.gridx = 0;
        format.gridy = 0;
        format.insets = new Insets(5,5,5,5);
        knapsackPanel.add(powerSetHeader, format);

        format.gridy = 1;
        format.ipadx = 600;
        format.ipady = 300;
        powerSetScrollPane.setViewportView(powerSetPanel);
        knapsackPanel.add(powerSetScrollPane, format);

        format.gridy = 2;
        format.ipadx = 0;
        format.ipady = 0;
        knapsackPanel.add(solutionHeader, format);

        format.gridy = 3;
        format.ipadx = 600;
        format.ipady = 100;
        knapsackPanel.add(solutionScrollPane, format);

        noSolutionB.addActionListener(this);

        //deliveryPanel initialization
        deliveryReturnB.addActionListener(this);

        add(weightLimitPanel);
        add(knapsackPanel);
        add(deliveryPanel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == weightLimitB) {
            if (weightLimitTF.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Invalid info!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            } else if (weightLimitTF.getText().matches("^(\\d*\\.)?\\d+$")) {

            } else {
                JOptionPane.showMessageDialog(this, "Invalid info!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }
            
            Set<Package> packages = new HashSet<Package>();

            //*Start of extracting data from database
            String dbURL = "jdbc:oracle:thin:@10.22.14.215:1521:orcl";
            String username = "BH2_TANJ_N21";
            String password = "oracle";
            Connection conn = null;
            Statement statement = null;
            ResultSet rs = null;
            try {
                conn = DriverManager.getConnection(dbURL, username, password);
                statement = conn.createStatement();
                String sql = "select transacid, packvalue, packweight, recaddress from package";
                rs = statement.executeQuery(sql);
                while (rs.next()) {
                    packages.add(new Package(rs.getInt("transacid"), rs.getDouble("packvalue"), rs.getDouble("packweight"), rs.getString("recaddress")));
                }
                conn.close();
                statement.close();
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            //*Start of solving knapsack problem
            double weightLimit = Double.parseDouble(weightLimitTF.getText());

            //packageSubsets contains all possible subset of packages
            Set<Set<Package>> packageSubsets = new HashSet<Set<Package>>(powerSet(packages));

            //bestSetOfPackages contains all subset of packages with the highest value without going above the weight limit
            bestSetOfPackages = solveKnapsack(weightLimit, packageSubsets);
            //*End

            //Initialization for JComponents for displaying knapsack problem
            int rowCtr = 0;
            subSetL = new JLabel[packageSubsets.size()];
            subSetWeightL = new JLabel[packageSubsets.size()];
            subSetValueL = new JLabel[packageSubsets.size()];

            subSetSolutionL = new JLabel[bestSetOfPackages.size()];
            subSetSolutionWeightL = new JLabel[bestSetOfPackages.size()];
            subSetSolutionValueL = new JLabel[bestSetOfPackages.size()];
            subSetSolutionButtonL = new JButton[bestSetOfPackages.size()];

            //Setting text and format of powerSetPanel
            for (Set<Package> set : packageSubsets) {
                int totalWeight = 0;
                int totalValue = 0;

                String subSetDisplay = "{";

                for (Package package_ : set) {
                    totalWeight += package_.weight;
                    totalValue += package_.value;

                    subSetDisplay += package_.transNo + ",";
                }

                subSetDisplay += "}";
                subSetDisplay = subSetDisplay.replace(",}", "}");
                subSetL[rowCtr] = new JLabel(subSetDisplay);
                format.ipadx = subSetDisplay.length();
                format.ipady = 10;
                format.gridx = 0;
                format.gridy = rowCtr;
                powerSetPanel.add(subSetL[rowCtr], format);

                subSetWeightL[rowCtr] = new JLabel(totalWeight + "kg");
                format.gridx = 1;
                powerSetPanel.add(subSetWeightL[rowCtr], format);

                if (totalWeight > weightLimit) {
                    subSetValueL[rowCtr] = new JLabel("NOT FEASIBLE");
                } else {
                    subSetValueL[rowCtr] = new JLabel(totalValue + "$");
                }

                format.gridx = 2;
                powerSetPanel.add(subSetValueL[rowCtr], format);

                rowCtr++;
            }

            //Setting text and format of solutionPanel
            rowCtr = 0;
            for (Set<Package> set : bestSetOfPackages) {
                int totalWeight = 0;
                int totalValue = 0;

                String subSetDisplay = "{";

                for (Package package_ : set) {
                    totalWeight += package_.weight;
                    totalValue += package_.value;

                    subSetDisplay += package_.transNo + ",";
                }

                subSetDisplay += "}";
                subSetDisplay = subSetDisplay.replace(",}", "}");
                subSetSolutionL[rowCtr] = new JLabel(subSetDisplay);
                format.ipadx = subSetDisplay.length();
                format.ipady = 10;
                format.gridx = 0;
                format.gridy = rowCtr;
                solutionPanel.add(subSetSolutionL[rowCtr], format);

                subSetSolutionWeightL[rowCtr] = new JLabel(totalWeight + "kg");
                format.gridx = 1;
                solutionPanel.add(subSetSolutionWeightL[rowCtr], format);

                if (totalWeight > weightLimit) {
                    subSetSolutionValueL[rowCtr] = new JLabel("NOT FEASIBLE");
                } else {
                    subSetSolutionValueL[rowCtr] = new JLabel(totalValue + "$");
                }

                format.gridx = 2;
                solutionPanel.add(subSetSolutionValueL[rowCtr], format);

                subSetSolutionButtonL[rowCtr] = new JButton("Select");
                subSetSolutionButtonL[rowCtr].addActionListener(new selectButtonListener());
                format.gridx = 3;
                solutionPanel.add(subSetSolutionButtonL[rowCtr], format);

                rowCtr++;
            }

            //Setting text and format for solutionPanel when there is no solution
            if (subSetSolutionWeightL[0].getText().equals("0kg")) {
                subSetSolutionL[0].setText("No set of packages satisfy the weight limit");
                subSetSolutionButtonL[0].setVisible(false);
                subSetSolutionValueL[0].setVisible(false);
                subSetSolutionWeightL[0].setVisible(false);
                format.ipadx = 0;
                format.ipady = 0;
                format.gridx = 0;
                format.gridy = 1;
                solutionPanel.add(noSolutionB, format);
            }

            weightLimitPanel.setVisible(false);
            knapsackPanel.setVisible(true);
        } else if (e.getSource() == noSolutionB) {
            bestSetOfPackages.clear();
            solutionPanel.removeAll();
            powerSetPanel.removeAll();
            knapsackPanel.setVisible(false);
            weightLimitPanel.setVisible(true);
        } else if (e.getSource() == deliveryReturnB) {
            bestSetOfPackages.clear();
            solutionPanel.removeAll();
            powerSetPanel.removeAll();
            deliveryPanel.removeAll();
            deliveryPanel.setVisible(false);
            weightLimitPanel.setVisible(true);
        }
    }

    //ActioListener for selecting set of packages to delivery
    public class selectButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            for (int i = 0; i < subSetSolutionButtonL.length; i++) {
                if (e.getSource() == subSetSolutionButtonL[i]) {
                    int indexOfChosenSet = i;
                    ArrayList<Package> chosenSet = new ArrayList<Package>();
                    
                    chosenSet.addAll(returnChosenSet(indexOfChosenSet, bestSetOfPackages));

                    initializeBestPath(chosenSet);

                    break;
                }
            }

            knapsackPanel.setVisible(false);
            deliveryPanel.setVisible(true);
        }

    }

    //Methods for solving algo problems
    //Methods for knapsack problem by exhaustive search
    public static Set<Set<Package>> powerSet(Set<Package> originalSet) {
        Set<Set<Package>> sets = new HashSet<Set<Package>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<Package>());
            return sets;
        }
        List<Package> list = new ArrayList<Package>(originalSet);
        Package head = list.get(0);
        Set<Package> rest = new HashSet<Package>(list.subList(1, list.size()));
        for (Set<Package> set : powerSet(rest)) {
            Set<Package> newSet = new HashSet<Package>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }

    public static Set<Set<Package>> solveKnapsack(double weightLimit, Set<Set<Package>> packageSubsets) {
        double bestValue = 0;
        Set<Set<Package>> bestSetOfPackages = new HashSet<Set<Package>>();

        for (Set<Package> s : packageSubsets) {
            double totalWeight = 0;
            double totalValue = 0;
            for (Package p : s) {
                totalWeight += p.weight;
                totalValue += p.value;
            }
            if (totalWeight <= weightLimit) {
                if (totalValue > bestValue) {
                    bestSetOfPackages.clear();
                    bestValue = totalValue;
                    bestSetOfPackages.add(s);
                } else if (totalValue == bestValue) {
                    bestSetOfPackages.add(s);
                }
            }
        }

        return bestSetOfPackages;
    }

    //Methods for finding best path
    public static ArrayList<Package> returnChosenSet(int indexOfChosenSet, Set<Set<Package>> bestSetOfPackages) {

        ArrayList<Package> chosenSet;

        int ctr = 0;
        for (Set<Package> set : bestSetOfPackages) {
            if (ctr == indexOfChosenSet) {
                chosenSet = new ArrayList<Package>(set);
                return chosenSet;
            }
            ctr++;
        }

        return null;
    }

    public static void initializeBestPath(ArrayList<Package> chosenSet) {
        String branch = "16C Balingasa Road. Balintawak Quezon city";

        //Variables for solving all possible path
        ArrayList<Package> bestSet = new ArrayList<Package>(chosenSet);
        ArrayList<List<Package>> permutationSet = new ArrayList<List<Package>>();

        //Solve permutation set of chosen set to produce all possible paths
        permutationSet = permute(chosenSet);

        //Variables for solving distances and for displaying
        double deliveryDistances[] = new double[chosenSet.size() + 1]; // Stores distances between locations
        double bestDeliveryDistances[] = new double[chosenSet.size() + 1];// Stores distances between locations of the best path
        double distance;// Used to store distance between location to minimize of calling getDistance(from,to) method
        double totalDistance;// Stores total distance
        double bestDistance = -1;// Stores total distance of the best path

        try {
            for (List<Package> s : permutationSet) {
                distance = getDistance(branch, s.get(0).address);
                totalDistance = distance;
                deliveryDistances[0] = distance;

                for (int i = 0; i < s.size(); i++) {
                    if (i != s.size() - 1) {
                        distance = getDistance(s.get(i).address, s.get(i + 1).address);
                        totalDistance += distance;
                        deliveryDistances[i + 1] = distance;
                    } else {
                        distance = getDistance(s.get(i).address, branch);
                        totalDistance += distance;
                        deliveryDistances[i + 1] = distance;
                    }
                }

                System.out.println("Total Distance = " + totalDistance);

                if (bestDistance == -1) {
                    bestSet.clear();
                    bestSet.addAll(s);
                    bestDistance = totalDistance;
                    for (int i = 0; i < deliveryDistances.length; i++) {
                        bestDeliveryDistances[i] = deliveryDistances[i];
                    }
                } else if (totalDistance < bestDistance) {
                    bestSet.clear();
                    bestSet.addAll(s);
                    bestDistance = totalDistance;
                    for (int i = 0; i < deliveryDistances.length; i++) {
                        bestDeliveryDistances[i] = deliveryDistances[i];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Initializaion of deliveryPanel
        format.ipadx = 0;
        format.ipady = 0;
        format.gridheight = 1;
        format.insets = new Insets(0, 50, 0, 0);
        format.gridy = 0;
        format.gridx = 0;
        format.gridwidth = (chosenSet.size() + 1) * 2 + 2;
        deliveryHeaderL.setText("<html>Best path of delivery with a total distance of " + bestDistance + "m<br>"
                                + "Hover over each location for more info. <br>"
                                + "Hover over each arrow to view distances from one location to the other" + "<html>");
        deliveryPanel.add(deliveryHeaderL, format);

        format.gridheight = 1;
        format.insets = new Insets(15, 0, 0, 0);
        format.ipadx = 30;
        format.ipady = 10;
        format.gridx = 0;
        format.gridy = 2;
        format.gridwidth = (chosenSet.size() + 1) * 2 + 2;
        deliveryPanel.add(deliveryReturnB, format);

        deliveryDistanceL = new JLabel[chosenSet.size() + 2];
        deliveryLocationL = new JLabel[chosenSet.size() + 2];

        //Branch labels
        format.gridwidth = 1;
        format.ipadx = 0;
        format.ipady = 0;
        format.gridy = 1;
        format.gridx = 0;
        deliveryLocationL[0] = new JLabel("Branch");
        deliveryLocationL[0].setToolTipText(branch);
        deliveryPanel.add(deliveryLocationL[0], format);

        //last path branch
        format.gridx = (chosenSet.size() + 1) * 2;
        deliveryLocationL[chosenSet.size() + 1] = new JLabel("Branch");
        deliveryLocationL[chosenSet.size() + 1].setToolTipText(branch);
        deliveryPanel.add(deliveryLocationL[chosenSet.size() + 1], format);

        //Initializing location labels
        
        for (int i = 0; i < bestSet.size(); i++) {
            deliveryLocationL[i] = new JLabel(Integer.toString(bestSet.get(i).transNo));
        }
        
        //Opening database for additional info of packages
        String dbURL = "jdbc:oracle:thin:@10.22.14.215:1521:orcl";
        String username = "BH2_TANJ_N21";
        String password = "oracle";
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(dbURL, username, password);
            statement = conn.createStatement();
            String sql = "select * from package";
            rs = statement.executeQuery(sql);
            int colCtr;
            while(rs.next()){
                colCtr = 0;
                for (int j = 0 ; j < bestSet.size() ; j++) {
                    colCtr+=2;
                    if(deliveryLocationL[j].getText().equals(rs.getString("transacid"))){
                        format.ipadx = 0;
                        format.ipady = 0;
                        format.gridy = 1;
                        format.gridx = colCtr;
                        deliveryLocationL[j].setToolTipText("<html>Address: " + rs.getString("recaddress") + "<br>"
                                                        + "Name: " + rs.getString("recfname") + " " + rs.getString("reclname") + "<br>"
                                                        + "Contact No: " + rs.getString("reconnum") + "<br>"
                                                        + "Package Value: " + rs.getDouble("packvalue") + "<br>"
                                                        + "Package Weight: " + rs.getDouble("packweight") + "<br>"
                                                        + "Date of transaction: " + rs.getDate("packdate") + "<br>"
                                                        + "Sender Email: " + rs.getString("emailaddress") + "</html>");
                
                        deliveryPanel.add(deliveryLocationL[j], format);
                        break;
                    }
                }
            }
            conn.close();
            statement.close();
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        int colCtr = 1;
        
        ImageIcon arrow = new ImageIcon("arrow.png");

        //Initializing location distances labels
        for (int i = 0; i < bestSet.size() + 1; i++) {
            format.ipadx = 10;
            format.ipady = 0;
            format.gridy = 1;
            format.gridx = colCtr;
            deliveryDistanceL[i] = new JLabel(arrow);
            deliveryDistanceL[i].setToolTipText(Double.toString(bestDeliveryDistances[i]) + "m");
            deliveryPanel.add(deliveryDistanceL[i], format);
            colCtr += 2;
        }

    }

    public static ArrayList<List<Package>> permute(List<Package> input) {
        ArrayList<List<Package>> output = new ArrayList<List<Package>>();
        if (input.isEmpty()) {
            output.add(new ArrayList<Package>());
            return output;
        }
        List<Package> list = new ArrayList<Package>(input);
        Package head = list.get(0);
        List<Package> rest = list.subList(1, list.size());
        for (List<Package> permutations : permute(rest)) {
            List<List<Package>> subLists = new ArrayList<List<Package>>();
            for (int i = 0; i <= permutations.size(); i++) {
                List<Package> subList = new ArrayList<Package>();
                subList.addAll(permutations);
                subList.add(i, head);
                subLists.add(subList);
            }
            output.addAll(subLists);
        }
        return output;
    }

    public static double getDistance(String from, String to) throws ApiException, InterruptedException, IOException {
        GeoApiContext context = new GeoApiContext.Builder().apiKey("").build();

        String[] originAddress = new String[1];
        String[] destinationAddress = new String[1];
        originAddress[0] = from;
        destinationAddress[0] = to;

        DistanceMatrix distanceMatrix = DistanceMatrixApi.getDistanceMatrix(context, originAddress, destinationAddress).await();
        System.out.println("From " + distanceMatrix.originAddresses[0] + " To " + distanceMatrix.destinationAddresses[0] + " = " + distanceMatrix.rows[0].elements[0].distance.inMeters);
        return distanceMatrix.rows[0].elements[0].distance.inMeters;
    }

}
