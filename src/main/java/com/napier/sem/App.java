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

        List<World> countriesByPopulation = a.q1();
        System.out.println("\n\n\nQUERY 1 - Countries by Population:");
        for (World c : countriesByPopulation) {
            System.out.println("Country: " + c.Name + ", Population: " + country.Population);
        }

        List<World> europeCountriesByPopulation = a.q2();
        System.out.println("\n\n\nQUERY 2 - European Countries by Population:");
        for (World c : europeCountriesByPopulation) {
            System.out.println("Country: " + c.Name + ", Population: " + c.Population);
        }

        List<World> caribbeanCountriesByPopulation = a.q3();
        System.out.println("\n\n\nQUERY 3 - Caribbean Countries by Population:");
        for (World c : caribbeanCountriesByPopulation) {
            System.out.println("Country: " + c.Name + ", Population: " + c.Population);
        }

        List<World> citiesByPopulation = a.q7();
        System.out.println("\n\n\nQUERY 7 - Cities by Population:");
        for (World c : citiesByPopulation) {
            System.out.println("Name: " + c.Name + ", Population: " + c.Population);
        }

        List<World> citiesByContinent = a.q8();
        System.out.println("\n\n\nQUERY 8 - Cities by Continent (Asia):");
        for (World c : citiesByContinent) {
            System.out.println("Name: " + c.Name + ", Population: " + c.Population);
        }

        List<World> citiesByRegion = a.q9();
        System.out.println("\n\n\nQUERY 9 - Cities by Region (Caribbean):");
        for (World c : citiesByRegion) {
            System.out.println("Name: " + c.Name + ", Population: " + c.Population);
        }

        /***
         * QUERIES COMPLETED:
         * Query 1 - All the countries in the world organised by largest population to smallest.
         * Query 2 - All the countries in a continent organised by largest population to smallest.
         * Query 3 - All the countries in a region organised by largest population to smallest.
         * Query 7 - All the cities in the world organised by largest population to smallest.
         * Query 8 - All the cities in a continent organised by largest population to smallest.
         * Query 9 - All the cities in a region organised by largest population to smallest.
         */

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
    public List<World> q1() {
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

    public List<World> q2() {
        List<World> europeCountries = new ArrayList<>();

        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Population "
                            + "FROM country "
                            + "WHERE Continent = 'Europe' "
                            + "ORDER BY Population DESC";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Process the result set
            while (rset.next()) {
                World country = new World();
                country.Code = rset.getString("Code");
                country.Name = rset.getString("Name");
                country.Population = rset.getInt("Population");
                europeCountries.add(country);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get European countries by population");
        }

        return europeCountries;
    }


    public List<World> q3() {
        List<World> caribbeanCountries = new ArrayList<>();

        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Population "
                            + "FROM country "
                            + "WHERE Region = 'Caribbean' "
                            + "ORDER BY Population DESC";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Process the result set
            while (rset.next()) {
                World country = new World();
                country.Code = rset.getString("Code");
                country.Name = rset.getString("Name");
                country.Population = rset.getInt("Population");
                caribbeanCountries.add(country);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get Caribbean countries by population");
        }

        return caribbeanCountries;
    }

    /***
     * Query 7
     * @return citiesByPopulation
     */
    public List<World> q7() {
        List<World> citiesByPopulation = new ArrayList<>();

        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Name, Population "
                            + "FROM city "
                            + "ORDER BY Population DESC";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Process the result set
            while (rset.next()) {
                World city = new World();
                city.Name = rset.getString("Name");
                city.Population = rset.getInt("Population");
                citiesByPopulation.add(city);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get countries by population");
        }

        return citiesByPopulation;
    }

    /***
     * Query 8
     * @return CitiesByContinent
     */
    public List<World> q8() {
        List<World> citiesByContinent = new ArrayList<>();

        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, city.Population " +
                            "FROM city " +
                            "JOIN country ON city.CountryCode = country.Code " +
                            "WHERE country.Continent = 'Asia' " +
                            "ORDER BY city.Population DESC";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Process the result set
            while (rset.next()) {
                World city = new World();
                city.Name = rset.getString("Name");
                city.Population = rset.getInt("Population");
                citiesByContinent.add(city);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get countries by population");
        }

        return citiesByContinent;
    }

    /***
     * Query 9
     * @return citiesByRegion
     */
    public List<World> q9() {
        List<World> citiesByRegion = new ArrayList<>();

        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, city.Population " +
                            "FROM city " +
                            "JOIN country ON city.CountryCode = country.Code " +
                            "WHERE country.Region = 'Caribbean' " +
                            "ORDER BY city.Population DESC";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Process the result set
            while (rset.next()) {
                World city = new World();
                city.Name = rset.getString("Name");
                city.Population = rset.getInt("Population");
                citiesByRegion.add(city);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get countries by population");
        }

        return citiesByRegion;
    }
}