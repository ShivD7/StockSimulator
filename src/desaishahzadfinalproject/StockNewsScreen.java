/*
Aayan Shahzad, Shiv Desai
December 19, 2024
StockNewsScreen - Stock Sim Final Project
 */
package desaishahzadfinalproject;

//import the necessary libraries
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

public class StockNewsScreen extends javax.swing.JFrame {

    //declare all of the attributes of a StockNewsScreen
    private MainMenuScreen mainMenuWindow;
    private ArrayList<String> urls = new ArrayList<String>();

    /**
     * This is the primary constructor for the StockNewsScreen
     *
     * @param m - a main menu screen necessary for when the "back" button is
     * clicked
     */
    public StockNewsScreen(MainMenuScreen m) {
        initComponents(); //build the GUI
        //make it so the spinner that tracks the amount of stock cannot be edited
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinnerLinkAmount.getEditor();
        editor.getTextField().setEnabled(false);
        mainMenuWindow = m; //set the mainMenuWindow attribute to the main menu screen provided
        //create a new imageicon with file path to logo
        ImageIcon img = new ImageIcon("src/desaishahzadfinalproject/stockupdown.jpg");
        //set the image as the frame's icon
        this.setIconImage(img.getImage());
    }

    /**
     * This method provides links to articles relating to a stock the user has
     * inputted
     *
     * @param numArticles - the number of articles the user wants
     */
    private void getArticles(int numArticles) {
        try { //try this block of code
            String API_KEY = "0Y4ZEQVM4VMM5U13"; // API key
            String urlString = "https://www.alphavantage.co/query?function=NEWS_SENTIMENT&tickers=" + txtFieldTicker.getText() + "&apikey=" + API_KEY; //this is the URL that the client (the computer) will accept data from 

            URL url = new URL(urlString); //instantiate a new URL that will be used for the query
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //open up the connection from the URL
            connection.setRequestMethod("GET"); //ask the server to retrieve data

            int responseCode = connection.getResponseCode(); //this is the response code that the server sends depending on if the query was successful or not
            String message = ""; //the string that will store all of the URLs
            if (responseCode == HttpURLConnection.HTTP_OK) { //check if the query was successful
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())); //instantiate a new buffered reader that will process the data provided. buffered readers are good for large amount of data
                String inputLine; //string that holds the info. the server is providing
                String response = ""; //string that stores all of the info being provided by the server

                while ((inputLine = in.readLine()) != null) { //run this loop while there is still more data to process/being provided by the server
                    response += inputLine; //add the info to the correct string 
                }
                in.close(); //close the input stream

                // turn the JSON response from the server into a string
                String jsonResponse = response.toString();
                // Parse the JSON response
                JSONObject jsonObject = new JSONObject(jsonResponse); //instantiate a new jsonObject with the responses/data provided by the server
                JSONArray articles = jsonObject.getJSONArray("feed"); //retreive an array of article urls as "feed" will map to a bunch of articles, (i.e. feed : "https://google.com, "https://ugcloud.ca")

                DefaultListModel listModel1 = new DefaultListModel();
                if (articles.length() > 0) {// Check if there are articles in the array that was provided
                    for (int i = 0; i < numArticles; i++) { //loop through this array the number of times a user wants an article
                        // Extract the URL of each article
                        String articleTitle = articles.getJSONObject(i).getString("title");
                        //add URL to the message that will be displated

                        listModel1.addElement(articleTitle);

                        urls.add(articles.getJSONObject(i).getString("url"));
                    }
                    listArticles.setModel(listModel1);
                } else { //must be no links
                    System.out.println("No articles found."); //display the appropriate error message
                }

                listArticles.setSelectedIndex(0);

            } else { //unsuccessful query
                System.out.println("Error: Unable to get data. Response Code: " + responseCode); //display the response code
            }

        } catch (ArrayIndexOutOfBoundsException | org.json.JSONException ex) {//catch an ArrayIndexOutOfBoundsException
            //output the appropriate message
            JOptionPane.showMessageDialog(null, "Either you ran out of calls or entered an invalid ticker!");
        } catch (Exception ex) { //catch any other exception
            //display appropriate error message
            JOptionPane.showMessageDialog(null, "Error! " + ex);
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

        lblBackground = new javax.swing.JLabel();
        lblStockNews = new javax.swing.JLabel();
        lblStock = new javax.swing.JLabel();
        txtFieldTicker = new javax.swing.JTextField();
        lblAmount = new javax.swing.JLabel();
        spinnerLinkAmount = new javax.swing.JSpinner();
        lblLookAt = new javax.swing.JLabel();
        btnLoad = new javax.swing.JToggleButton();
        btnBack = new javax.swing.JToggleButton();
        btnOpen = new javax.swing.JButton();
        lblDisclaimer = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listArticles = new javax.swing.JList<>();
        lblBackground3 = new javax.swing.JLabel();

        lblBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desaishahzadfinalproject/Screenshot 2024-12-18 143023.png"))); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Stock Sim - Stock News");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblStockNews.setFont(new java.awt.Font("Segoe UI", 1, 60)); // NOI18N
        lblStockNews.setText("Stock News");
        getContentPane().add(lblStockNews, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        lblStock.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblStock.setText("What stock would you like to receive articles for?");
        getContentPane().add(lblStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 470, 20));

        txtFieldTicker.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        getContentPane().add(txtFieldTicker, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 270, 60));

        lblAmount.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblAmount.setText("And how many articles would you like to receive?");
        getContentPane().add(lblAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, -1, -1));

        spinnerLinkAmount.setModel(new javax.swing.SpinnerNumberModel(1, 1, 50, 1));
        getContentPane().add(spinnerLinkAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, 240, 40));

        lblLookAt.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblLookAt.setText("Articles to look at:");
        getContentPane().add(lblLookAt, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, -1, -1));

        btnLoad.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnLoad.setText("Load Articles");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });
        getContentPane().add(btnLoad, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 170, 230, 60));

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        getContentPane().add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 740, -1, -1));

        btnOpen.setText("Open Link");
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });
        getContentPane().add(btnOpen, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 510, 240, 60));

        lblDisclaimer.setText("Note: Only US stocks are supported at this time.");
        getContentPane().add(lblDisclaimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, -1, -1));

        listArticles.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jScrollPane2.setViewportView(listArticles);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 420, 500, 290));

        lblBackground3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/desaishahzadfinalproject/Screenshot 2024-12-18 143023.png"))); // NOI18N
        getContentPane().add(lblBackground3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-130, -60, 1060, 830));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
        if (txtFieldTicker.getText().length() <= 0) { //check if the user has not inputted a ticker
            JOptionPane.showMessageDialog(null, "Must enter a ticker!"); //dislay appropriate message
        } else { //must be gine
            getArticles((int) spinnerLinkAmount.getValue()); //call the method that gets a list of articles given the number of articles the user wants
        }
    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.setVisible(false); //make this window so it is not visible
        mainMenuWindow.setVisible(true); //make the main menu window visible
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        try { //try this block of code
            urlOpen(); //call the method that will open this url
        } catch (URISyntaxException ex) { //catch a URISyntaxException
            //display the appropriate error message
            JOptionPane.showMessageDialog(null, "Error! " + ex);
        }
    }//GEN-LAST:event_btnOpenActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnBack;
    private javax.swing.JToggleButton btnLoad;
    private javax.swing.JButton btnOpen;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAmount;
    private javax.swing.JLabel lblBackground;
    private javax.swing.JLabel lblBackground3;
    private javax.swing.JLabel lblDisclaimer;
    private javax.swing.JLabel lblLookAt;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lblStockNews;
    private javax.swing.JList<String> listArticles;
    private javax.swing.JSpinner spinnerLinkAmount;
    private javax.swing.JTextField txtFieldTicker;
    // End of variables declaration//GEN-END:variables

    /**
     * This method opens a given URL in a browse
     *
     * @throws URISyntaxException - the string provided couldn't be parse into a
     * URI (mean that URL doesn't exist)
     */
    private void urlOpen() throws URISyntaxException {
        Desktop d = Desktop.getDesktop(); //instantiate a new desktop that will allow the opening of URLs
        try { //try this block of code
            d.browse(new URI(urls.get(listArticles.getSelectedIndex()))); //browse for the provided URL
        } catch (IOException e) { //catch an IOException
            //display the necessary error message
            JOptionPane.showMessageDialog(null, "Error! This URL doesn't exist!");
        }

    }
}
