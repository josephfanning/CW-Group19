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

        // getCity function that takes in the cities ID as a parameter and returns all the data
        World cty = a.getCity(373);
        // Display results of getCity function using the displayCity function
        a.displayCity(cty);

        World country = a.getCountry("BLR");
        a.displayCountry(country);

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
                    "SELECT ID, Name, CountryCode, District, Population "
                            + "FROM city "
                            + "WHERE ID = " + id;

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next())
            {
                // uses getString function to get all the information form the different parts of the SQL statement
                // basically sets all the values from World.swl to the variables alrerdy created in World.java
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
        catch (Exception e) // if the database cannot be located or has issues accessing it
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get World details");
            return null;
        }
    }

    public void displayCity(World cty) // method to display
    {
        if (cty != null)
        {
            System.out.println( // prints out all the variables from a particular city
                    cty.ID + " "
                            + "ID: " + cty.ID + " "
                            + "Name: " + cty.Name + "\n"
                            + "Country Code: " + cty.CountryCode + "\n"
                            + "District:" + cty.District + "\n"
                            + "Population: " + cty.Population + "\n");
        }
    }

    public World getCountry(String Code)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Region, Population, Capital "
                            + "FROM country "
                            + "WHERE Code = '" + Code + "'";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next())
            {
                // uses getString function to get all the information form the different parts of the SQL statement
                // basically sets all the values from World.sql to the variables already created in World.java
                World country = new World();
                country.Code = rset.getString("Code");
                country.Name = rset.getString("Name");
                country.Continent = rset.getString("Continent");
                country.Region = rset.getString("Region");
                country.Population = rset.getInt("Population");
                country.Capital = rset.getInt("Capital");
                return country;
            }
            else
                return null;
        }
        catch (Exception e) // if the database cannot be located or has issues accessing it
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get World details");
            return null;
        }
    }

    public void displayCountry(World country)
    {
        if (country != null)
        {
            System.out.println( // prints out all the variables from a particular city
                    country.ID + " "
                            + "Code: " + country.Code + " "
                            + "Name: " + country.Name + "\n"
                            + "Continent: " + country.Continent + "\n"
                            + "Region:" + country.Region + "\n"
                            + "Population: " + country.Population + "\n"
                            + "Capital" + country.Capital + "\n");
        }

    }


}


