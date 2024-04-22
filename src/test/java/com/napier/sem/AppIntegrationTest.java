package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest
{
    static App app; // creates an insatance

    @BeforeAll
    static void init() // initialisation method to get the file to connect to the database
    {
        app = new App();
        app.connect("localhost:33060", 10000);

    }

    @Test
    void testGetName() // basic test to get the cities name from the database
    {
        World test1 = app.getCity(113);
        assertEquals("San Fernando del Valle de Cata", test1.Name);
    }

    @Test
    void testGetCountryCode() // basic test to get the cities CountryCode from the database
    {
        World testGetCountryCode = app.getCity(1);
        assertEquals("AFG", testGetCountryCode.CountryCode);
    }

    @Test
    void testGetPopulation() // basic test to get the cities Population from the database
    {
        World testGetPopulation = app.getCity(71);
        assertEquals(1157507, testGetPopulation.Population);
    }

    @Test
    void testGetDistrict() // basid code to get the District from the database
    {
        World testGetDistrict = app.getCity(871);
        assertEquals("Southern Mindanao", testGetDistrict.District);
    }

    @Test
    void testGetAllCityInfo() // basic code to get all information from a city from the database
    {
        World testGetAllInfo = app.getCity(1052);
        assertEquals("Vishakhapatnam", testGetAllInfo.Name);
        assertEquals("IND", testGetAllInfo.CountryCode);
        assertEquals("Andhra Pradesh", testGetAllInfo.District);
        assertEquals(752037, testGetAllInfo.Population);
    }


}