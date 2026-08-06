/*
Aayan Shahzad, Shiv Desai
December 19, 2024
PortfolioScreen - Stock Sim Final Project
 */
package desaishahzadfinalproject;

//import the necessary libraries
import java.awt.BorderLayout;
import java.awt.Color;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
//these are imports from an external library called JFreeChart used to draw graphs 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class PortfolioScreen extends javax.swing.JFrame {

    //declare the attributes of a PortfolioScreen
    private User mainUser;
    private MainMenuScreen mainMenuWindow;
    final private DecimalFormat money = new DecimalFormat("$#,##0.00");

    /**
     * swaps 2 strings in an array of a strings
     *
     * @param words - array of words
     * @param i - index of word 1 to be swapped
     * @param j - index of word 2 to be swapped
     */
    private void swap(String[] words, int i, int j) {
        //make a temporary variable containing number 1
        String temp = words[i];
        //put num 2 in the place of num 1
        words[i] = words[j];
        //put the temporary variable containing num 1 in the place of num 2
        words[j] = temp;
    }

    /**
     * Quick sort algorithm that sorts an array of strings in ascending order
     *
     * @param words - array of words to be sorted
     * @param low - the low index of the portion currently being searched
     * (should start at 0 if sorting whole array)
     * @param high - the high index of the portion currently being searched
     * (should start at array length - 1 if sorting whole array)
     */
    private void quickSort(String[] words, int low, int high) {
        //Base case - if the low index is greater then or equal to the high index, there is 1 or less then 1 element left in the array (completed sorting)
        if (low >= high) {
            //return
            return;
        }
        //set the pivot word equal to the middle point of the array
        String pivot = words[(low + high) / 2];
        //variables for the low index and high index of the array currently being searched
        int i = low;
        int j = high;
        //partition the array - move all words lexicographically less then the pivot to the left of it and move all words greater then the pivot to the right of it
        while (i < j) { //while i has not met/crossed j, as this means there is still more partitioning and swapping to be done
            //loop until a number is found where words[i] is lexicographically less then the pivot word (compare < 0)
            while (words[i].compareTo(pivot) < 0) {
                //add one to i (moving i towards the right of the array)
                i++;
            }
            //loop until a number is found where words[j] is greater lexicographically then the pivot word  (compare > 0)
            while (words[j].compareTo(pivot) > 0) {
                //subtract one from j (moving j towards the left of the array)
                j--;
            }
            if (i <= j) { //if i and j still have not met/crossed each other, that means the elements at i and j must be swapped for the partitioning process
                //swap the elements at i and j
                swap(words, i, j);
                //increase i by 1 moving it to the left
                i++;
                //decrease j by 1 moving it to the right
                j--;
            }
        }
        //i should now be at the right of the pivot and j should be at the left of the pivot by this point
        //recursive calls
        //call quick sort again for the left half of the array (words less then the pivot) to sort it
        //going from the beginning of the array to j
        quickSort(words, low, j);
        //call quick sort again for the right half of the array (words greater then the pivot) to sort it
        //going from i to the end of the array
        quickSort(words, i, high);
    }

    /**
     * This is the primary constructor for the PortfolioScreen
     *
     * @param u - the user that is currently logged in
     */
    public PortfolioScreen(User u) {
        initComponents(); //initalize the GUI
        mainUser = u; //set the mainUse attribute tothe current user that is signed in
        lblUserMoney.setText("Your Money: " + money.format(mainUser.getBalance())); //display the users money
        String[] stocks = new String[mainUser.getPortfolio().size()]; //stock array that stores all of the user's owned stock tickers
        //counter variable
        int i = 0;
        for (String s : mainUser.getPortfolio().keySet()) { //loop through all of the stocks in the user's portfolio
            stocks[i] = s; //add current ticker to array
            i++; //add 1 to counter
        }
        //sort the stock tickers using a recursive quick sort algorithm, going from 0 (first index) to size-1 (last index)
        quickSort(stocks, 0, stocks.length - 1);
        for (String s : stocks) { //loop for every ticker in the now sorted list of the user's tickers
            //display the ticker, price bought at, and amount of the stock owned
            txtAreaHoldings.setText(txtAreaHoldings.getText() + "\n" + s + " → Bought at: " + money.format(Double.parseDouble(mainUser.getPortfolio().get(s)[0])) + "\tAmount: " + mainUser.getPortfolio().get(s)[1]);
        }
        lblPortVal.setText("Portfolio Value: " + money.format(mainUser.getPortfolioValue())); //display the user's portfolio's value
        //create a new imageicon with file path to logo
        ImageIcon img = new ImageIcon("src/desaishahzadfinalproject/stockupdown.jpg");
        //set the image as the frame's icon
        this.setIconImage(img.getImage());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblPortfolio = new javax.swing.JLabel();
        lblUserMoney = new javax.swing.JLabel();
        lblHoldings = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaHoldings = new javax.swing.JTextArea();
        btnBack = new javax.swing.JButton();
        lblPortVal = new javax.swing.JLabel();
        btnPieChart = new javax.swing.JToggleButton();
        panelPieChart = new javax.swing.JPanel();
        lblBackground = new javax.swing.JLabel();
        lblBackground1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Stock Sim - Portfolio");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblPortfolio.setFont(new java.awt.Font("Helvetica Neue", 1, 60)); // NOI18N
        lblPortfolio.setText("Your Portfolio");
        getContentPane().add(lblPortfolio, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, -1, -1));

        lblUserMoney.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        lblUserMoney.setText("Your Money: ");
        getContentPane().add(lblUserMoney, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, 310, -1));

        lblHoldings.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        lblHoldings.setText("Holdings:");
        getContentPane().add(lblHoldings, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 390, -1));

        txtAreaHoldings.setEditable(false);
        txtAreaHoldings.setColumns(20);
        txtAreaHoldings.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        txtAreaHoldings.setRows(5);
        jScrollPane1.setViewportView(txtAreaHoldings);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 260, 390, 410));

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        getContentPane().add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 760, -1, -1));

        lblPortVal.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        lblPortVal.setText("Portfolio Value: ");
        getContentPane().add(lblPortVal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 700, 390, -1));

        btnPieChart.setText("Show Pie Chart");
        btnPieChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPieChartActionPerformed(evt);
            }
        });
        getContentPane().add(btnPieChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 600, -1, -1));
        getContentPane().add(panelPieChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 140, 630, 440));

        lblBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desaishahzadfinalproject/Screenshot 2024-12-18 143023.png"))); // NOI18N
        getContentPane().add(lblBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, -60, 240, 860));

        lblBackground1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desaishahzadfinalproject/Screenshot 2024-12-18 143023.png"))); // NOI18N
        getContentPane().add(lblBackground1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, 800));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        //when the back button is clicked
        mainMenuWindow = new MainMenuScreen(mainUser, new LogInScreen(new HomeScreen()), new StockScreen(mainUser), this); //instantiate a new MainMenuScreen
        mainMenuWindow.setVisible(true); //set the main menu window so it is visible
        this.setVisible(false);//set this window so it is not visible
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnPieChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPieChartActionPerformed
        showPieChart(); //call the method that will display the pie chart when the button is clicked
    }//GEN-LAST:event_btnPieChartActionPerformed

    private void showPieChart() {
        DefaultPieDataset pieDataset = new DefaultPieDataset();//instantiate a dataset for the pie graph
        for (String s : mainUser.getPortfolio().keySet()) { //loop through the stocks that the user owns
            String[] data = mainUser.getPortfolio().get(s); //get the array that stores the price bought at, and the amount of stock amount  
            pieDataset.setValue(s, Double.parseDouble(data[1]) * Double.parseDouble(data[0])); //add data to the dataset
        }

        JFreeChart piechart = ChartFactory.createPieChart3D("Portfolio", pieDataset, true, true, true);//instantiate a new pie chart object
        PiePlot piePlot = (PiePlot) piechart.getPlot(); //create plot object that will draw the pie chart

        //this code was copied, it displays the percentage that one piece of data takes up from the whole data set and the actual amount 
        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0.00"), new DecimalFormat("0%"));
        piePlot.setLabelGenerator(gen);

        //set the background to white
        piePlot.setBackgroundPaint(Color.white);

        //instantiate a chartPanel to display chart(graph)
        ChartPanel barChartPanel = new ChartPanel(piechart);
        barChartPanel.setVisible(true); //set the panel so it is visible

        //display the pie chart in the panel
        panelPieChart.removeAll();
        panelPieChart.add(barChartPanel, BorderLayout.CENTER);
        panelPieChart.validate();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JToggleButton btnPieChart;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBackground;
    private javax.swing.JLabel lblBackground1;
    private javax.swing.JLabel lblHoldings;
    private javax.swing.JLabel lblPortVal;
    private javax.swing.JLabel lblPortfolio;
    private javax.swing.JLabel lblUserMoney;
    private javax.swing.JPanel panelPieChart;
    private javax.swing.JTextArea txtAreaHoldings;
    // End of variables declaration//GEN-END:variables
}
