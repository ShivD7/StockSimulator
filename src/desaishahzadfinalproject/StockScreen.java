/*
Aayan Shahzad, Shiv Desai
December 19, 2024
StockScreen - Stock Sim Final Project
 */
package desaishahzadfinalproject;

//import all of the necessary libraries
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.time.LocalTime;
import javax.swing.ImageIcon;
import javax.swing.JSpinner;

//these are imports from an external library called JFreeChart which will be used for graphing
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class StockScreen extends javax.swing.JFrame implements Serializable { //implement Seriablizable to read and write objects from files

    //declare all of the attributes of a StockScreen
    private User mainUser;
    private MainMenuScreen mainMenuWindow;
    private DecimalFormat money = new DecimalFormat("$#,##0.00");
    private String[] currData = new String[6];
    private double[] highLowGrowth;
    private String[] thirtyDayData = new String[30];

    /**
     * This is the primary constructor for a StockScreen
     *
     * @param u - the mainUser that is currently logged in
     */
    public StockScreen(User u) {
        initComponents(); //set up the GUI
        mainUser = u; //set the mainUser to the one being passed in

        //set the current text label to the current money of the user
        lblUserMoney.setText("Your money: " + money.format(mainUser.getBalance()));

        //make it so the spinner that tracks the amount of stock cannot be edited
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinnerAmt.getEditor();
        editor.getTextField().setEnabled(false);
        panelLineGraph.setVisible(false);
        //create a new imageicon with file path to logo
        ImageIcon img = new ImageIcon("src/desaishahzadfinalproject/stockupdown.jpg");
        //set the image as the frame's icon
        this.setIconImage(img.getImage());
    }

    /**
     * This is a method that will draw a line chart
     *
     * @param data - the data used to create the line chart
     */
    private void showLineChart(String[] data) {
        panelLineGraph.setVisible(true);
        //create dataset for the graph
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < data.length; i++) { //loop through the data array
            dataset.setValue(Double.parseDouble(data[i]), "Price", Integer.toString(i)); //add the data to the dataset
        }

        //instantiate a new line chart with the correct properties
        JFreeChart linechart = ChartFactory.createLineChart(txtFieldTicker.getText(), "Daily", "Price",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        //create plot object that will draw the chart
        CategoryPlot lineCategoryPlot = linechart.getCategoryPlot();
        //set the background colour
        lineCategoryPlot.setBackgroundPaint(Color.white);

        //create render object to change the line properties like color
        LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) lineCategoryPlot.getRenderer();
        Color lineChartColor = new Color(204, 0, 51);
        lineRenderer.setSeriesPaint(0, lineChartColor);

        //instantiate a ChartPanel to display chart(graph)
        ChartPanel lineChartPanel = new ChartPanel(linechart);
        //draw the chart onto the correct panel
        panelLineGraph.removeAll();
        panelLineGraph.add(lineChartPanel, BorderLayout.CENTER);
        panelLineGraph.validate();
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
     * This method will get the high and low of the closest day, and the thirty
     * day data
     *
     * @param ticker - the ticker for the data we need
     * @param exchange - the exchange that stock is in
     * @return
     */
    private double[] getHighLowThirtyData(String ticker, String exchange) {
        double highLowGrowth[] = new double[3]; //instantiate an array that will store the data
        try {//try tbis block of code until an exception is through
            String API_KEY = "0Y4ZEQVM4VMM5U13"; //the api key that will be used in our query

            //create a url string and with the correct format of data we need
            String urlString = "";
            urlString = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + ticker + exchange + "&outputsize=compact&datatype=csv&apikey=" + API_KEY;
            urlString += API_KEY;

            URL url = new URL(urlString);//instantiate a new url with urlString made
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //open a connection to the provided url
            connection.setRequestMethod("GET"); //ask the server to retrieve stock data

            int responseCode = connection.getResponseCode(); //get the response code from the server (200 for success, 404 for not found, etc.)
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())); //instantiate a new buffered reader that reads data from the server, buffered readers are good for processing large chunks of data

            //declare varuables that will store a line of text from the input stream
            String inputLine;
            String response = "";
            ArrayList<String[]> data = new ArrayList<String[]>(); //instantiate a new array list that will store the data (String[])
            while ((inputLine = in.readLine()) != null) { //run this while loop until there is more data to process
                String rows[] = inputLine.split(","); //use the split to seperate the data by commas
                data.add(rows); //add the array of data to the arraylist
            }
            in.close(); //close the input stream

            String currData[] = data.get(1); //get the first array of data
            for (int i = 1; i < 31; i++) { //loop 30 times
                thirtyDayData[i - 1] = data.get(i)[1]; //get the daily open of each of the 30 days for the 30 day data array
            }

            highLowGrowth[0] = Double.parseDouble(currData[2]); //assign daily high for the day to appropriate spot in array
            highLowGrowth[1] = Double.parseDouble(currData[3]);//assign daily low for the day to appropriate spot in array
            highLowGrowth[2] = (Double.parseDouble(currData[4]) - Double.parseDouble(currData[1])) / Math.abs(Double.parseDouble(currData[1])); //assign daily growth for the day to appropriate spot in array
            connection.disconnect(); //close the active HTTPS URL connection
            return highLowGrowth; //return the array of data
        } catch (ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException ex) {//catch an ArrayIndexOutOfBoundsException
            //output the appropriate message
            JOptionPane.showMessageDialog(null, "Either you ran out of calls or entered an invalid ticker!");
            thirtyDayData = new String[30]; //reassign the thirtyDayData array so the old data doesn't get stored
            currData = new String[6]; //instantiate a new array so old data doesn't get stored
            return null;
        } catch (Exception ex) { //catch any other exception
            thirtyDayData = new String[30]; //reassign the thirtyDayData array so the old data doesn't get stored
            currData = new String[6]; //instantiate a new array so old data doesn't get stored
            JOptionPane.showMessageDialog(null, "Error! " + ex);
            return null;
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtFieldTicker = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        comboBoxExchange = new javax.swing.JComboBox<>();
        lblDataLimit = new javax.swing.JLabel();
        lblUserMoney = new javax.swing.JLabel();
        lblDayHigh = new javax.swing.JLabel();
        lblDayLow = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        lblCurrentPrice = new javax.swing.JLabel();
        btnSell = new javax.swing.JToggleButton();
        btnBuy = new javax.swing.JToggleButton();
        btnGraph = new javax.swing.JToggleButton();
        spinnerAmt = new javax.swing.JSpinner();
        lblAmount = new javax.swing.JLabel();
        panelLineGraph = new javax.swing.JPanel();
        lblBackground2 = new javax.swing.JLabel();
        lblTicker = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblGradientBackgruond = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Stock Sim - Stock Screen");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtFieldTicker.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtFieldTicker.setBorder(null);
        txtFieldTicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFieldTickerActionPerformed(evt);
            }
        });
        getContentPane().add(txtFieldTicker, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 330, 80));

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        getContentPane().add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 50, 260, 50));

        comboBoxExchange.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "US", "TSX", "London Stock Exchange", "Shanghai Stock Exchange" }));
        comboBoxExchange.setBorder(null);
        getContentPane().add(comboBoxExchange, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, 280, 80));

        lblDataLimit.setText("*Only US Exchanges (NYSE and NASDAQ) have real-time trading, other exchanges update daily");
        getContentPane().add(lblDataLimit, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 120, 530, 20));

        lblUserMoney.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        lblUserMoney.setText("Your Money: ");
        getContentPane().add(lblUserMoney, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 270, 330, -1));

        lblDayHigh.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        lblDayHigh.setText("Day High:");
        getContentPane().add(lblDayHigh, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 420, 450, -1));

        lblDayLow.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        lblDayLow.setText("Day Low:");
        getContentPane().add(lblDayLow, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 570, 340, -1));

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        getContentPane().add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 770, -1, -1));

        lblCurrentPrice.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        lblCurrentPrice.setText("Current Price: ");
        getContentPane().add(lblCurrentPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, 320, -1));

        btnSell.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        btnSell.setText("SELL");
        btnSell.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSellActionPerformed(evt);
            }
        });
        getContentPane().add(btnSell, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 560, 220, 90));

        btnBuy.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        btnBuy.setText("BUY");
        btnBuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuyActionPerformed(evt);
            }
        });
        getContentPane().add(btnBuy, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 420, 220, 90));

        btnGraph.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        btnGraph.setText("See Graph");
        btnGraph.setEnabled(false);
        btnGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGraphActionPerformed(evt);
            }
        });
        getContentPane().add(btnGraph, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 660, 350, 70));

        spinnerAmt.setModel(new javax.swing.SpinnerNumberModel(1, 1, 50, 1));
        getContentPane().add(spinnerAmt, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 520, 90, -1));

        lblAmount.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        lblAmount.setText("Amount:");
        getContentPane().add(lblAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 520, -1, -1));
        getContentPane().add(panelLineGraph, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 190, 690, 430));

        lblBackground2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desaishahzadfinalproject/Screenshot 2024-12-18 143023.png"))); // NOI18N
        getContentPane().add(lblBackground2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 0, 390, 800));

        lblTicker.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTicker.setText("Ticker:");
        getContentPane().add(lblTicker, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 5, -1, 20));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Exchange:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 0, -1, -1));

        lblGradientBackgruond.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desaishahzadfinalproject/Screenshot 2024-12-18 143023.png"))); // NOI18N
        getContentPane().add(lblGradientBackgruond, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1050, 800));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        try { //try to run this block of code
            DecimalFormat money = new DecimalFormat("$#,##0.00"); //instantiate a new decimal format for money
            String API_KEY = "0Y4ZEQVM4VMM5U13"; //api key for stock data
            String urlString = ""; //create a new urlString for the query
            String ticker = txtFieldTicker.getText(); //get the ticker the user is searching for
            String exchange = ""; //declare and assign a variable that will store the exchange of the stock
            if (comboBoxExchange.getSelectedIndex() == 0) { //check if they searched for a US stock
                //assign urlString to the appropriate URL to obtain the correct data from the server
                urlString = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + ticker + "&interval=1min&outputsize=full&datatype=csv&extended_hours=false&apikey=";
                exchange = ""; //assign exchange to appropriate value
            } else if (comboBoxExchange.getSelectedIndex() == 1) { //check if they searched for a toronto stock
                //assign urlString to the appropriate URL to obtain the correct data from the server
                urlString = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + ticker + ".TRT&outputsize=full&datatype=csv&extended_hours=false&apikey=";
                exchange = ".TRT";//assign exchange to appropriate value
            } else if (comboBoxExchange.getSelectedIndex() == 2) { //check if they searched for a london stock
                //assign urlString to the appropriate URL to obtain the correct data from the server
                urlString = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + ticker + ".LON&outputsize=full&datatype=csv&extended_hours=false&apikey=";
                exchange = ".LON";//assign exchange to appropriate value
            } else { //check if they searched for a shanghai stock
                //assign urlString to the appropriate URL to obtain the correct data from the server
                urlString = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + ticker + ".SHH&outputsize=full&datatype=csv&extended_hours=false&apikey=";
                exchange = ".SHH";//assign exchange to appropriate value
            }

            urlString += API_KEY; //add api key to urlString
            URL url = new URL(urlString);//instantiate a new url with urlString made
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //open a connection to the provided url
            connection.setRequestMethod("GET"); //ask the server to retrieve stock data

            int responseCode = connection.getResponseCode(); //get the response code from the server (200 for success, 404 for not found, etc.)

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm" + ":00");//instantiate a date formatter with the correct formatting sequence
            Date date = new Date(); //instantiate a new date
            String currDate = formatter.format(date); //format the date
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            //declare String variables that will store data given from the server
            String inputLine;
            ArrayList<String[]> data = new ArrayList<String[]>(); //instantiate a new array list that will store the data (String[])
            while ((inputLine = in.readLine()) != null) { //run this while loop until there is more data to process
                String rows[] = inputLine.split(","); //use the split to seperate the data by commas
                data.add(rows); //add the array of data to the arraylist
            }
            in.close(); //close the input stream

            boolean isCurrData = false; //check if we have found the data we need
            int counter = 0; //declare and assign a counter variable
            LocalTime refTime = LocalTime.of(16, 0); //create a reference time to check if we are passwed a certain time (4 pm)

            for (String[] columns : data) { //loop through all of the data
                if (!exchange.equals("") && !isCurrData && counter == 1) { //check if we are searching for an exchange that isn't american, we haven't found the data we want, and we are at the first data point
                    //set currData to columns because this is the data we want
                    currData = columns;
                    isCurrData = true; //we have found the data, set to true

                } else if (currDate.substring(currDate.length() - 8, currDate.length()).equals(columns[0].substring(columns[0].length() - 8, columns[0].length())) && !isCurrData) { //check if the times match if the exchange is American (because we get minutely data) and check if we haven't found data already
                    //set currData to columns because this is the data we want
                    currData = columns;
                    isCurrData = true; //we have found the data, set to true
                } else if (exchange.equals("") && LocalTime.now().isAfter(refTime) && counter == 1 && !isCurrData) { //check if the exchange is american and if we have passwed 4 and if we haven't found any other data yet
                    //set currData to columns because this is the data we want
                    currData = columns;
                    isCurrData = true; //we have found the data, set to true
                }
                counter++; //increment counter
            }
            lblCurrentPrice.setText("Current Price: " + money.format(Double.valueOf(currData[1]))); //display the current price from the data we just obtained

            highLowGrowth = getHighLowThirtyData(ticker, exchange); //get the daily high, low, and growth

            //display the daily high and low, formatting them
            lblDayHigh.setText("Day High: " + money.format(highLowGrowth[0]));
            lblDayLow.setText("Day Low: " + money.format(highLowGrowth[1]));
            connection.disconnect();//disconnect from the HTTP connection
            btnGraph.setEnabled(true);
        } catch (ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException | NullPointerException ex) { //catch an ArrayIndexOutOfBoundsException or StringIndexOutOfBoundsException
            //display the appropriate error message
            JOptionPane.showMessageDialog(null, "Either you ran out of API calls, or entered an invalid ticker!");
            //no data to show, so clear everything
            lblDayHigh.setText("Day High: ");
            lblDayLow.setText("Day Low: ");
            lblCurrentPrice.setText("Current Price: ");
            btnGraph.setEnabled(false);

            thirtyDayData = new String[30]; //reassign the thirtyDayData array so the old data doesn't get stored
            currData = new String[6]; //instantiate a new array so old data doesn't get stored
        } catch (UnknownHostException ex) { //catch an UnknownHostException
            //display the appropriate error message
            JOptionPane.showMessageDialog(null, "No Wifi!");
            btnGraph.setEnabled(false);
            thirtyDayData = new String[30]; //reassign the thirtyDayData array so the old data doesn't get stored
            currData = new String[6]; //instantiate a new array so old data doesn't get stored
        } catch (Exception ex) { //catch any othe exception
            JOptionPane.showMessageDialog(null, "Error! " + ex);//display the appropriate error message
            btnGraph.setEnabled(false);
            thirtyDayData = new String[30]; //reassign the thirtyDayData array so the old data doesn't get stored
            currData = new String[6]; //instantiate a new array so old data doesn't get stored
        }
        panelLineGraph.setVisible(false);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnBuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuyActionPerformed
        ArrayList<User> users = load(); //load the current users signed up to the applicatoin

        User currUser = new User(); //instantiate a new user that will store the current user signed in
        int currUserIndex = -1; //variable to store the index in the ArrayList of where the current user is
        for (int i = 0; i < users.size(); i++) { //loop through the array list
            if (mainUser.equals(users.get(i))) { //check if the user at the current index is the user signed in currently
                currUser = users.get(i); //store the current user at the given index because it is the user signed in
                currUserIndex = i; //store the index
            }
        }

        try { //try this block of code
            if (mainUser.buyStock(Double.parseDouble(currData[1]), txtFieldTicker.getText(), (int) spinnerAmt.getValue())) { //check if the user can buy the stock at the given price and amount
                lblUserMoney.setText("Your money: " + money.format(mainUser.getBalance())); //update the user's balance
                try { //try this block of code
                    FileOutputStream out; //declare a FileOutputStream
                    if (System.getProperty("user.dir").substring(System.getProperty("user.dir").length() - 4, System.getProperty("user.dir").length()).equals("dist")) { //check  the current user's file path and if they are in the "dist" folder (JAR is running)
                        out = new FileOutputStream(System.getProperty("user.dir") + "/saves/users.txt"); //instantiate a new FileOutputStream at the appropriate file path
                    } else {//must be running it from netbeans
                        out = new FileOutputStream(System.getProperty("user.dir") + "/dist/saves/users.txt");//instantiate a new FileOutputStream at the appropriate file path
                    }

                    ObjectOutput scan = new ObjectOutputStream(out); //instantiate a new ObjectOutput to write Objects to a file, out is FileOutputStream 

                    //update the current user's balance and portfolio
                    currUser.setBalance(mainUser.getBalance());
                    currUser.setPortfolio(mainUser.getPortfolio());
                    if (currUserIndex == -1) { //check if the user was even found in the list
                        JOptionPane.showMessageDialog(null, "Cant find person");
                    } else { //must've been found
                        users.set(currUserIndex, currUser); //set the user at the given index to the new, updated user
                    }
                    scan.writeObject(users); //write the array list into the file
                    scan.close(); //close the ObjectOutput
                } catch (IOException e) { //catch an IOException
                    //output the appropriate error message
                    JOptionPane.showMessageDialog(null, "Error!" + e);
                }
                JOptionPane.showMessageDialog(null, "Purchase successful!"); //display that the purchase was successful
            } else {//can't do the purchase
                //display that the user doesn't have enough money
                JOptionPane.showMessageDialog(null, "Purchase failed, not enough money!");
            }
        } catch (NullPointerException ex) {//catch NullPointerException
            //display error message
            JOptionPane.showMessageDialog(null, "Error! No data, you probably didn't search a stock.");
        } catch (Exception ex) { //catch any other exception
            //display error message
            JOptionPane.showMessageDialog(null, "Error!  " + ex);
        }

    }//GEN-LAST:event_btnBuyActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.setVisible(false); //set the current window so it's not visible
        mainMenuWindow = new MainMenuScreen(mainUser, new LogInScreen(new HomeScreen()), this, new PortfolioScreen(mainUser)); //instantiate a new MainMenuScree
        mainMenuWindow.setVisible(true); //set the main menu screen so that it is visible
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnSellActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSellActionPerformed
        ArrayList<User> users = load(); //load the current users signed up to the applicatoin

        User currUser = new User(); //instantiate a new user that will store the current user signed in
        int currUserIndex = -1; //variable to store the index in the ArrayList of where the current user is
        for (int i = 0; i < users.size(); i++) { //loop through the array list
            if (mainUser.equals(users.get(i))) { //check if the user at the current index is the user signed in currently
                currUser = users.get(i); //store the current user at the given index because it is the user signed in
                currUserIndex = i; //store the index
            }
        }

        try { //try this block of code
            if (mainUser.sellStock(txtFieldTicker.getText(), (int) spinnerAmt.getValue(), Double.parseDouble(currData[1]))) { //check if the user can sell the stock with the given amount
                lblUserMoney.setText("Your money: " + money.format(mainUser.getBalance())); //update the user's balance that is being shown
                try {
                    FileOutputStream out; //declare a FileOutputStream
                    if (System.getProperty("user.dir").substring(System.getProperty("user.dir").length() - 4, System.getProperty("user.dir").length()).equals("dist")) { //check  the current user's file path and if they are in the "dist" folder (JAR is running)
                        out = new FileOutputStream(System.getProperty("user.dir") + "/saves/users.txt"); //instantiate a new FileOutputStream at the appropriate file path
                    } else {//must be running it from netbeans
                        out = new FileOutputStream(System.getProperty("user.dir") + "/dist/saves/users.txt");//instantiate a new FileOutputStream at the appropriate file path
                    }

                    ObjectOutput scan = new ObjectOutputStream(out);//instantiate a ObjectOutputStream to write objects to a gile

                    //update the current users balance and portfolio
                    currUser.setBalance(mainUser.getBalance());
                    currUser.setPortfolio(mainUser.getPortfolio());
                    if (currUserIndex == -1) {//check if the user was even found in the database
                        //display error message
                        JOptionPane.showMessageDialog(null, "Can't find person");
                    } else {//must've been found
                        users.set(currUserIndex, currUser); //update the array list with the new user
                    }
                    scan.writeObject(users); //write the updated user list toe the file
                    scan.close(); //close the output stream
                } catch (IOException e) { //catch an IOException
                    //display the appropriate error message
                    JOptionPane.showMessageDialog(null, "Error!" + e);
                }
                JOptionPane.showMessageDialog(null, "Sold!"); //display that the stock was sold
            } else { //must not have enough stock
                //display a message
                JOptionPane.showMessageDialog(null, "Failed to sell, not enough stock!");
            }
        } catch (NullPointerException ex) { //catch null pointer exception
            //display error message
            JOptionPane.showMessageDialog(null, "Error! No data, you probably didn't search a stock.");
        } catch (Exception ex) {//catch any other error
            //display error message
            JOptionPane.showMessageDialog(null, "Error! " + ex);
        }
    }//GEN-LAST:event_btnSellActionPerformed

    private void btnGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraphActionPerformed
        try {
            showLineChart(thirtyDayData); //call the method to show the graph
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No data to draw the graph!"); //display error message
        }
    }//GEN-LAST:event_btnGraphActionPerformed

    private void txtFieldTickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFieldTickerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFieldTickerActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JToggleButton btnBuy;
    private javax.swing.JToggleButton btnGraph;
    private javax.swing.JButton btnSearch;
    private javax.swing.JToggleButton btnSell;
    private javax.swing.JComboBox<String> comboBoxExchange;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblAmount;
    private javax.swing.JLabel lblBackground2;
    private javax.swing.JLabel lblCurrentPrice;
    private javax.swing.JLabel lblDataLimit;
    private javax.swing.JLabel lblDayHigh;
    private javax.swing.JLabel lblDayLow;
    private javax.swing.JLabel lblGradientBackgruond;
    private javax.swing.JLabel lblTicker;
    private javax.swing.JLabel lblUserMoney;
    private javax.swing.JPanel panelLineGraph;
    private javax.swing.JSpinner spinnerAmt;
    private javax.swing.JTextField txtFieldTicker;
    // End of variables declaration//GEN-END:variables
}
