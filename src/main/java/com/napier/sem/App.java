package com.napier.sem;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    /** creates an instance of app and calls the connect and disconnect function on it*/
    public static void main(String[] args) {
        // Create new Application and connect to database
        App a = new App();

        if (args.length < 1) {
            a.connect("localhost:33060", 10000);
        } else {
            a.connect(args[0], Integer.parseInt(args[1]));
        }

        System.out.println("City Report: \n");
        World city = a.getCity(373);
        a.displayCity(city);
        System.out.println("\n\n");

        // Get Country
        System.out.println("Country Report: \n");
        World country = a.getCountry("BLR");
        a.displayCountry(country);
        System.out.println("\n\n");

        // get Capital
        System.out.println("Capital City Report: \n");
        World capital = a.getCapitalCity(1);
        a.displayCapital(capital);
        System.out.println("\n\n");

        List<World> countriesByPopulation = a.getCountriesByPopulation();
        System.out.println("Countries by Population:");
        for (World c : countriesByPopulation) {
            System.out.println("Country: " + c.Name + ", Population: " + country.Population);
        }

        List<World> CitiesByPopulation = a.getCitiesByPopulation();
        System.out.println("\n\n\n\n\nCities by Population:");
        for (World c : CitiesByPopulation) {
            System.out.println("City: " + c.Name + ", Population: " + city.Population);
        }

        // MENU - DOESNT WORK ON GITHUB ACTIONS WHEN DEPLOYING
//
//        // Menu
//        Scanner scanner = new Scanner(System.in);
//        int choice = 0;
//        while (choice != 3) {
//            System.out.println("Menu:");
//            System.out.println("1. Get City");
//            System.out.println("2. Get Country");
//            System.out.println("3. Get Capital");
//            System.out.println("4. Exit");
//            System.out.print("Enter your choice: ");
//            choice = scanner.nextInt();
//
//            switch (choice) {
//                case 1:
//                    // Get City
//                    World city = a.getCity(373);
//                    a.displayCity(city);
//                    break;
//                case 2:
//                    // Get Country
//                    World country = a.getCountry("BLR");
//                    a.displayCountry(country);
//                    break;
//                case 3:
//                    // get capital
//                    World capital = a.getCapitalCity(1);
//                    a.displayCapital(capital);
//                    System.out.println("yet to be implemented!");
//                case 4:
//                    // Exit
//                    break;
//                default:
//                    System.out.println("Invalid choice. Please enter a valid option.");
//            }
//        }

        // Disconnect from database
        a.disconnect();
        //scanner.close();
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

    /***
     * gets city report
     * @param id
     * @return cty
     */
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

    /***
     * displays city report
     * @param cty
     */
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

    /***
     * gets country report
     * @param Code
     * @return country
     */
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

    /***
     * displays country report
     * @param country
     */
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

    /***
     * gets the capital city report
     * @param cityId
     * @return capital city
     */
    public World getCapitalCity(int cityId) {
        try {
            // Create an SQL statement for retrieving city information
            String cityQuery = "SELECT Name AS CityName, CountryCode, Population FROM city WHERE ID = " + cityId;

            // Execute city query
            ResultSet cityResult = con.createStatement().executeQuery(cityQuery);

            // Create a World object to store the capital city information
            World capitalCity = new World();

            // Retrieve city information
            if (cityResult.next()) {
                capitalCity.Name = cityResult.getString("CityName");
                capitalCity.CountryCode = cityResult.getString("CountryCode");
                capitalCity.Population = cityResult.getInt("Population");
            } else {
                // If city not found, return null
                return null;
            }

            // Close the city result set
            cityResult.close();

            // Create an SQL statement for retrieving country name
            String countryQuery = "SELECT Name AS CountryName FROM country WHERE Code = '" + capitalCity.CountryCode + "'";

            // Execute country query
            ResultSet countryResult = con.createStatement().executeQuery(countryQuery);

            // Retrieve country name
            if (countryResult.next()) {
                capitalCity.CountryName = countryResult.getString("CountryName");
            } else {
                // If country not found, return null
                return null;
            }

            // Close the country result set
            countryResult.close();

            // Return the capital city
            return capitalCity;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get capital city details");
            return null; // If an exception occurs
        }
    }

    /***
     * display capital city report
     * @param capitalCity
     */
    public void displayCapital(World capitalCity) {
        if (capitalCity != null) {
            System.out.println("Capital City Report:");
            System.out.println("City Name: " + capitalCity.Name);
            System.out.println("Country Name: " + capitalCity.Name);
            System.out.println("Population: " + capitalCity.Population);
        } else {
            System.out.println("Capital city not found.");
        }
    }

    /***
     * query 1 - get all countries by population from highest to lowest
     * @return countries
     */
    public List<World> getCountriesByPopulation() {
        List<World> countries = new ArrayList<>();

        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Population "
                            + "FROM country "
                            + "ORDER BY Population DESC";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Process the result set
            while (rset.next()) {
                World country = new World();
                country.Code = rset.getString("Code");
                country.Name = rset.getString("Name");
                country.Population = rset.getInt("Population");
                countries.add(country);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get countries by population");
        }

        return countries;
    }

    public List<World> getCitiesByPopulation() {
        List<World> cities = new ArrayList<>();

        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Name, Population "
                            + "FROM city "
                            + "ORDER BY Population DESC";

            // Execute SQL statement
            ResultSet query2 = stmt.executeQuery(strSelect);

            // Process the result set
            while (query2.next()) {
                World city = new World();
                city.Name = query2.getString("Name");
                city.Population = query2.getInt("Population");
                cities.add(city);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get countries by population");
        }

        return cities;
    }

}