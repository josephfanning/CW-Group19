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
    void testGetEmployee()
    {
        World cty = app.getCity(113);
        assertEquals(cty.CountryCode, 134935);
        assertEquals(cty.Name, "San Fernando del Valle de Cata");
        assertEquals(cty.CountryCode, "ARG");
    }
}