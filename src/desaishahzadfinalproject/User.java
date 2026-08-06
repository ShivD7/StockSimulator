/*
Aayan Shahzad, Shiv Desai
December 19, 2024
User Class - Stock Sim Final Project
 */
package desaishahzadfinalproject;

//import all of the necessary libraries for the User class
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.ArrayList;

public class User implements Serializable { //implement the Serializable interface to read Object into a data file
    //declare all of the attributes of a user

    private String username;
    private String password;
    private HashMap<String, String[]> portfolio;
    private double balance;
    private ArrayList<User> competingAgainst;

    /**
     * This is the primary constructor of the user class
     */
    public User() {
        //give all of the attributes default values
        username = "N/A";
        password = "N/A";
        portfolio = new HashMap<String, String[]>();
        balance = 1000;
        competingAgainst = new ArrayList<User>();
    }

    /**
     * This is the secondary constructor for the user class
     *
     * @param username - the username of the user
     * @param password - the password of the user
     */
    public User(String username, String password) {
        this();//chain constructor to primary constructor

        //set new values of username and password
        this.username = username;
        this.password = password;
    }

    /**
     * This is the tertiary constructor of the User class
     *
     * @param u - the username of the user
     * @param pass - the password of the user
     * @param port - the portfolio of the user
     * @param b - the balance of the user
     */
    public User(String u, String pass, HashMap<String, String[]> port, double b) {
        this(u, pass); //chain this constructor to secondary one

        //set the new portfolio and balance of the user
        portfolio = port;
        balance = b;
    }

    /**
     * This is the fourth constructor of the user class
     *
     * @param u - the username of the user
     * @param pass - the password of the user
     * @param port - the portfolio of the user
     * @param b - the balance of the user
     * @param compete - an ArrayList of the Users this user is competing against
     */
    public User(String u, String pass, HashMap<String, String[]> port, double b, ArrayList<User> compete) {
        this(u, pass, port, b); //chain to tertiary ocnstructor
        competingAgainst = compete; //set competing against to the given array list
    }

    /**
     * This method gets the users the current user is competing against
     *
     * @return - an array list of the users the current user is competing
     * against
     */
    public ArrayList<User> getCompeting() {
        return competingAgainst;
    }

    /**
     * This method sets a new array list for the users the current user is
     * competing against
     *
     * @param compete - the new list of users the current user is competing
     * against
     */
    public void setCompeting(ArrayList<User> compete) {
        competingAgainst = compete;
    }

    /**
     * This is a getter for the username
     *
     * @return - the username of a user
     */
    public String getUsername() {
        return username;
    }

    /**
     * This is a setter for the username of a user
     *
     * @param u - the new username
     */
    public void setUsername(String u) {
        username = u;
    }

    /**
     * This is a getter for the user's password
     *
     * @return - user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * This is a setter for the user's password
     *
     * @param pass - the new password of the user
     */
    public void setPassword(String pass) {
        password = pass;
    }

    /**
     * This is a getter for the user's balance
     *
     * @return - the user's balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * This is a setter for the user's balance
     *
     * @param b - the user's new balance
     */
    public void setBalance(double b) {
        balance = b;
    }

    /**
     * This is a getter for the user's portfolio
     *
     * @return - the user's portfolio
     */
    public HashMap<String, String[]> getPortfolio() {
        return portfolio;
    }

    /**
     * This is a setter for the user's portfolio
     *
     * @param port - the user's new portfolio
     */
    public void setPortfolio(HashMap<String, String[]> port) {
        portfolio = port;
    }

    /**
     * This method allows a user to buy a certain stock
     *
     * @param price - the price of the stock
     * @param ticker - the ticker of the stock
     * @param amtStock - the amount of the stock they hold
     * @return - a boolean depending on if the purchase was a success or not
     */
    public boolean buyStock(double price, String ticker, int amtStock) {
        ticker = ticker.toUpperCase();
        if (balance >= (price * amtStock)) { //check if they have enough money to buy the given amount
            String[] priceAmt = portfolio.get(ticker); //get the previous price and amount of the stock they bought 
            if (priceAmt != null) { //check if they've ever bought the stock
                //calculate the new weighted price of the stock
                double weightPrice = (Double.parseDouble(priceAmt[0]) * Integer.parseInt(priceAmt[1]));
                weightPrice += price * amtStock;
                weightPrice = weightPrice / (amtStock + Integer.parseInt(priceAmt[1]));
                //set the new price they bought the stock at to the weighted price
                priceAmt[0] = Double.toString(weightPrice);

                //updated the quantity of the stock they own
                priceAmt[1] = Integer.toString((Integer.parseInt(priceAmt[1]) + amtStock));
                portfolio.put(ticker, priceAmt); //update the portfolio
            } else { //must've never bought it before
                String[] pA = {Double.toString(price), Integer.toString(amtStock)}; //create a new array to store the price, and amount of the stock
                portfolio.put(ticker, pA); //update portfolio
            }
            balance -= price * amtStock; //decrement balance 
            return true;
        } else { //they do not have enough money
            return false; //return false
        }
    }

    /**
     * This method allows a user to sell a stock
     *
     * @param ticker - ticker of the stock they want to sell
     * @param amtStock - amount they want to sell
     * @param currPrice - price they are selling at
     * @return - a boolean depending on it the sale was successful or not
     */
    public boolean sellStock(String ticker, int amtStock, double currPrice) {
        ticker = ticker.toUpperCase();
        String[] priceAmt = portfolio.get(ticker); //get the user's current amount of the stock, and price they bought at
        if (priceAmt == null || Integer.parseInt(priceAmt[1]) < amtStock) { //check if the user doesn't have enough stock
            return false;
        } else { //must have enough stock
            balance += amtStock * currPrice; //increase balance by how many stocks they are selling
            priceAmt[1] = Integer.toString((Integer.parseInt(priceAmt[1]) - amtStock)); //update the number of stocks they own
            if (Integer.parseInt(priceAmt[1]) <= 0) { //check if they have any stocks left
                portfolio.remove(ticker);//remove from portfolio
            } else { //must have some left over
                portfolio.put(ticker, priceAmt); //update portfolio
            }
            return true;
        }
    }

    /**
     * This method calculates the user's portfolio value
     *
     * @return - value of user's portfolio
     */
    public double getPortfolioValue() {
        double value = balance; //set base value to the user's balance
        for (String s : portfolio.keySet()) { //loop through all of the stocks the user currently owns
            String[] priceAmt = portfolio.get(s); //get the price they bought at, and the amount of stock they own
            value += Double.parseDouble(priceAmt[0]) * Integer.parseInt(priceAmt[1]); //add the total value of that stock they own to the counter
        }
        return value;
    }

    /**
     * This method creates a clone of the current user
     *
     * @return - an exact copy of the current user
     */
    public User clone() {
        return new User(username, password, portfolio, balance, competingAgainst); //instantiate a new User with the same attributes to create a clone, and return it
    }

    /**
     * This method checks the equality of two Users
     *
     * @param u - the user being compared to
     * @return - true if the same, false if not
     */
    public boolean equals(User u) {
        boolean isEqual = username.equals(u.username) && password.equals(u.password) && balance == u.balance; //compare usernames, passwords, and balances
        if (!isEqual) { //check if the two aren't equal already
            return false; //return false beause isEqual would be false
        }
        if (portfolio.keySet().size() != u.portfolio.keySet().size()) { //check if the portfolio size is not the same
            isEqual = false;
        }
        if (portfolio.keySet().equals(u.portfolio.keySet())) { //check if the stocks they both own are the same
            for (String s : portfolio.keySet()) { //loop through the stocks
                String[] first = portfolio.get(s); //get the priceAmt array of the first user
                String[] second = u.portfolio.get(s);//get the priceAmt array of the second user
                if (!first[0].equals(second[0]) || !first[1].equals(second[1])) { //check if the users own the same amount of the stock, at the same price
                    isEqual = false;
                }
            }
        } else {
            isEqual = false;
        }

        return isEqual; //return the isEqual boolean after all of the comparisons
    }

    /**
     * This method provides a String representation of the User
     *
     * @return - a String representation of the given User
     */
    public String toString() {
        String list = "";
        DecimalFormat money = new DecimalFormat("$#,##0.00"); //create a format for money
        list += "Username: " + username + "\nBalance: " + money.format(balance) + "\nPortfolio:\nOwned Stock\tAmount of Stock Owned\n"; //add all of the attributes
        for (String s : portfolio.keySet()) { //loop through all of the stocks the user owns
            list += s + "\t\t" + portfolio.get(s)[0] + " " + portfolio.get(s)[1]; //add the price, and amount of the stock that they own
        }
        list += "Users being competed against:\n";
        for (User u : competingAgainst) { //loop thruogh all of the users being competed against
            list += u.toString() + "\n"; //add user to message
        }
        return list; //return the string
    }
}
