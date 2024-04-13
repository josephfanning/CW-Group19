package com.napier.sem;

import java.sql.*;

public class App
{
    /** creates an instance of app and calls the connect and disconnect function on it*/
    public static void main(String[] args) {
        // Create new Application and connect to database
        App a = new App();

        if (args.length < 1) {
            a.connect("localhost:33060", 10000);
        } else {
            a.connect(args[0], Integer.parseInt(args[1]));
        }

        // basic getCity function
        World cty = a.getCity(64); // not working error codes: //Unknown column 'ID' in 'field list'

        // Print print 1st query report
        a.executeQuery1();
         //Failed to get employee details

        // Disconnect from database
        a.disconnect();
    }

    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect(String location, int delay) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        boolean shouldWait = false;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                if (shouldWait) {
                    // Wait a bit for db to start
                    Thread.sleep(delay);
                }

                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/world?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());

                // Let's wait before attempting to reconnect
                shouldWait = true;
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    // testing function from lab3 to get a city and all its details
    public World getCity(int id)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT ID, Name, CountryCode, District, Population"
                            + "FROM city "
                            + "WHERE ID = " + id;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next())
            {
                World cty = new World();
                cty.ID = rset.getInt("ID");
                cty.Name = rset.getString("Name");
                cty.CountryCode = rset.getString("CountryCode");
                cty.District = rset.getString("District");
                cty.Population = rset.getInt("Population");
                return cty;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get World details");
            return null;
        }
    }

    public void displayCity(World cty)
    {
        if (cty != null)
        {
            System.out.println(
                    cty.ID + " "
                            + cty.ID + " "
                            + cty.Name + "\n"
                            + cty.CountryCode + "\n"
                            + "District:" + cty.District + "\n"
                            + cty.Population + "\n");
        }
    }

    public void executeQuery1() {
        // Check if the connection is established
        if (con == null) {
            System.out.println("Database connection not established. Please try again.");
            return;
        }

        try {
            // Create a statement object
            Statement stmt = con.createStatement();

            // Define your SQL query
            String query = "SELECT ID, Name, Population FROM city ORDER BY population DESC LIMIT 4";

            // Execute the query
            ResultSet rs = stmt.executeQuery(query);

            // Process the result set
            while (rs.next()) {
                // Access the columns of the current row
                //int id = rs.getInt("id");
                String name = rs.getString("name");
                // Do something with the retrieved data
                System.out.println("ID: " + rs.getInt("ID") + ", Name: " + name + ", Population: " + rs.getInt("population"));
            }

            // Close the result set and statement
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }
    }
}


