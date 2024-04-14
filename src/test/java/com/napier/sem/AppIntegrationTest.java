package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33060", 30000);

    }

    @Test
    void testGetName()
    {
        World test1 = app.getCity(113);
        assertEquals("San Fernando del Valle de Cata", test1.Name);
    }

    @Test
    void testGetCountryCode()
    {
        World testGetCountryCode = app.getCity(1);
        assertEquals("AFG", testGetCountryCode.CountryCode);
    }

    @Test
    void testGetPopulation()
    {
        World testGetPopulation = app.getCity(71);
        assertEquals(1157507, testGetPopulation.Population);
    }

    @Test
    void testGetDistrict()
    {
        World testGetDistrict = app.getCity(871);
        assertEquals("Southern Mindanao", testGetDistrict.District);
    }

    @Test
    void testGetAllInfo()
    {
        World testGetAllInfo = app.getCity(1052);
        assertEquals("Vishakhapatnam", testGetAllInfo.Name);
        assertEquals("IND", testGetAllInfo.CountryCode);
        assertEquals("Andhra Pradesh", testGetAllInfo.District);
        assertEquals(752037, testGetAllInfo.Population);
    }
}