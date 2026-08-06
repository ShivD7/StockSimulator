/*
Aayan Shahzad, Shiv Desai
December 19, 2024
CompetitionScreen - Stock Sim Final Project
 */
package desaishahzadfinalproject;

//import all of the necessary libraries
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;

public class CompetitionScreen extends javax.swing.JFrame {

    //declare all of the attribtues of a CompetitionScreen
    private User mainUser;
    private MainMenuScreen m;
    private DecimalFormat money = new DecimalFormat("$#,##0.00");

    /**
     * This is the primary constructor for a CompetitionScreen
     *
     * @param u - the user that is currently logged in
     */
    public CompetitionScreen(User u) {
        initComponents(); //build the GUI
        mainUser = u; //set the main user attribute to the user that is currently signed in
        update();//call the update method to update the users the mainUser is competing against
        String message = loadCompetition(); //get the information about all of the users the main user is competing against 
        txtAreaCompeting.setText(message); //display the information
        //create a new imageicon with file path to logo
        ImageIcon img = new ImageIcon("src/desaishahzadfinalproject/stockupdown.jpg");
        //set the image as the frame's icon
        this.setIconImage(img.getImage());
    }

    /**
     * This is a method that loads the current users signed up for StockSim
     *
     * @return - all of the current users
     */
    private ArrayList<User> load() {
        ArrayList<User> users = new ArrayList<User>(); //instantiate a new ArrayList that will store the ArrayList of current users
        try { //try this block of code until an exception is thrown
            FileInputStream in;
            if (System.getProperty("user.dir").substring(System.getProperty("user.dir").length() - 4, System.getProperty("user.dir").length()).equals("dist")) {//check if the file path of where the program is being run is in the dist folder (the JAR is being run)
                in = new FileInputStream(System.getProperty("user.dir") + "/saves/users.txt"); //instantiate a new FileInputStream at the correct file path
            } else {//the JAR isn't running, program just being run from netbeans
                in = new FileInputStream(System.getProperty("user.dir") + "/dist/saves/users.txt"); //instantiate a new FileInputStream at the correct file path
            }

            try {//try this block of code until an exception is thrown
                ObjectInputStream s = new ObjectInputStream(in); //instantiate a new ObjectInputStream with the FileInputStream
                users = (ArrayList<User>) s.readObject(); //read the data from the file, cast it to an ArrayList

                return users;//return the users

            } catch (ClassNotFoundException e) { //catch a ClassNotFoundException

                //output the correct error message
                JOptionPane.showMessageDialog(null, "Error!");
            }

        } catch (IOException e) { //catch an IOException

            //output the correct error message
            JOptionPane.showMessageDialog(null, "Error!");
        }
        return users; //return the users arraylist
    }

    /**
     * This method updates the users the mainUser is competing against, so it
     * gets saved
     */
    private void update() {
        ArrayList<User> users = load(); //get all of the current users signed up by calling the load method
        ArrayList<User> competing = mainUser.getCompeting(); //get the users the main user is competing against
        for (int i = 0; i < users.size(); i++) { //loop through the array list of users signed up
            User u = users.get(i);//get the user at the current index
            for (int j = 0; j < competing.size(); j++) { //loop through all of the current users being competed against 
                User u2 = competing.get(j); //get the user at the current index
                if (u.getUsername().equals(u2.getUsername())) { //check if they match
                    competing.set(j, u);//update the user at the index because their balance might've changed since last time
                    mainUser.setCompeting(competing); //update the list of users the main user is competing against
                }
            }
        }
    }

    /**
     * This method builds an array with all of the information of the users the
     * main user is competing aganinst
     *
     * @return - a string with all of the information of the users the main user
     * is competing aganinst
     */
    private String loadCompetition() {
        String message = ""; //declare and assign a string that will hold everything
        ArrayList<User> competing = mainUser.getCompeting(); //get the users the main user is competing against
        for (int i = 0; i < competing.size(); i++) { //loop through all of the users the main user is competing against
            User u = competing.get(i); //get the user at the current index

            //add all of the necessary attributes to the message variable
            message += "Username: " + u.getUsername() + "\n";
            message += "Balance: " + money.format(u.getBalance()) + "\n";
            message += "Portfolio Value: " + money.format(u.getPortfolioValue()) + "\n\n";
        }
        return message; //return the message
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblBackground1 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        lblCompetition = new javax.swing.JLabel();
        lblCompeteAgainst = new javax.swing.JLabel();
        txtFieldUsername = new javax.swing.JTextField();
        btnAdd = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaCompeting = new javax.swing.JTextArea();
        lblCompetingAgainst = new javax.swing.JLabel();
        btnBack = new javax.swing.JToggleButton();
        panelBackground = new javax.swing.JLabel();

        lblBackground1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desaishahzadfinalproject/Screenshot 2024-12-18 143023.png"))); // NOI18N

        jRadioButton1.setText("jRadioButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Stock Sim - Competition");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCompetition.setFont(new java.awt.Font("Helvetica Neue", 1, 36)); // NOI18N
        lblCompetition.setText("Competitions");
        getContentPane().add(lblCompetition, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, -1, -1));

        lblCompeteAgainst.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        lblCompeteAgainst.setText("Who do you want to compete against?");
        getContentPane().add(lblCompeteAgainst, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, -1, -1));

        txtFieldUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFieldUsernameActionPerformed(evt);
            }
        });
        getContentPane().add(txtFieldUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, 320, 40));

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 240, 260, 40));

        txtAreaCompeting.setEditable(false);
        txtAreaCompeting.setColumns(20);
        txtAreaCompeting.setRows(5);
        jScrollPane1.setViewportView(txtAreaCompeting);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 50, 510, 430));

        lblCompetingAgainst.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblCompetingAgainst.setText("Competing Against:");
        getContentPane().add(lblCompetingAgainst, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, -1, -1));

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        getContentPane().add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, -1, -1));

        panelBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desaishahzadfinalproject/Screenshot 2024-12-18 143023.png"))); // NOI18N
        getContentPane().add(panelBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1060, 520));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtFieldUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFieldUsernameActionPerformed

    }//GEN-LAST:event_txtFieldUsernameActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        ArrayList<User> users = load(); //get all of the current users signed up
        boolean found = false; //declare a boolean that will track if the given user is signed up
        ArrayList<User> competing = mainUser.getCompeting(); //get the users the main user is competing against
        if (txtFieldUsername.getText().equals(mainUser.getUsername())) { //check if the given username is the one of the user signed in
            //display the appropriate message
            JOptionPane.showMessageDialog(null, "You cannot compete against yourself!");
        } else {//must be a different username
            for (int i = 0; i < users.size(); i++) { //loop through all of the users signed up
                if (txtFieldUsername.getText().equals(users.get(i).getUsername())) { //check if the usernames match
                    boolean contains = false; //declare and assign a boolean that will be used to track if a user is already being competed agaist
                    for (int j = 0; j < competing.size(); j++) { //loop through the current users being comepted against
                        if (users.get(i).getUsername().equals(competing.get(j).getUsername())) { //check if the users match
                            contains = true; //user is already being competed against, set this to true
                        }
                    }
                    if (!contains) { //check if the user isn't being competed against
                        competing.add(users.get(i)); //add the user to the mainUser's competing against array list
                        mainUser.setCompeting(competing); //update the user's competing list
                        found = true;//set the found boolean to true

                    }
                }
            }

            if (!found) { //check if the user was not found
                //display the appropriate message
                JOptionPane.showMessageDialog(null, "That user does not exist or you are already competing against them!");
            } else { //user was found
                String message = loadCompetition(); //get a string of all of the data of the users the main user is competing against
                JOptionPane.showMessageDialog(null, "Added"); //display that the user was added
                txtAreaCompeting.setText(message); //display the new list of users the main user is comepting against
                try { //try this block of code
                    FileOutputStream out; //declare a FileOutputStream 
                    if (System.getProperty("user.dir").substring(System.getProperty("user.dir").length() - 4, System.getProperty("user.dir").length()).equals("dist")) { //check if the user is in the "dist" folder
                        out = new FileOutputStream(System.getProperty("user.dir") + "/saves/users.txt"); //instantiate a new FileOutputStream at the correct file path
                    } else {//must not be in the dist folder 
                        out = new FileOutputStream(System.getProperty("user.dir") + "/dist/saves/users.txt");//instantiate a new FileOutputStream at the correct file path
                    }

                    ObjectOutput scan = new ObjectOutputStream(out); //instantiate a new ObjectOutputStream with the given FileOutputStream
                    for (int i = 0; i < users.size(); i++) { //loop through all of the users that are signed up
                        User u = users.get(i);//get the user at the current index
                        if (u.getUsername().equals(mainUser.getUsername())) { //check if the user matches the current user logged in
                            users.set(i, mainUser); //update the user at the index because an attribute of the main user has changed
                        }
                    }
                    scan.writeObject(users); //read the newly updated array list to the file
                    scan.close();//close the ObjectOutputStream
                } catch (IOException e) {//catch an IOException
                    //display the appropriate error message
                    JOptionPane.showMessageDialog(null, "Error!" + e);
                }
            }
        }

    }//GEN-LAST:event_btnAddActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        //if the back button is clicked

        this.setVisible(false); //set this window so it's not visivle
        m = new MainMenuScreen(mainUser, new LogInScreen(new HomeScreen()), new StockScreen(mainUser), new PortfolioScreen(mainUser)); //instantiate a new MainMenuScreen
        m.setVisible(true); //make the main menu screen visible
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnAdd;
    private javax.swing.JToggleButton btnBack;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBackground1;
    private javax.swing.JLabel lblCompeteAgainst;
    private javax.swing.JLabel lblCompetingAgainst;
    private javax.swing.JLabel lblCompetition;
    private javax.swing.JLabel panelBackground;
    private javax.swing.JTextArea txtAreaCompeting;
    private javax.swing.JTextField txtFieldUsername;
    // End of variables declaration//GEN-END:variables
}
