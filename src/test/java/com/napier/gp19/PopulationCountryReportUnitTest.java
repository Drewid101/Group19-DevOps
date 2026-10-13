package com.napier.gp19;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PopulationCountryReport.
 */
public class PopulationCountryReportUnitTest
{
    // A single shared instance of the report class
    static PopulationCountryReport report;

    @BeforeAll
    static void init()
    {
        // Initialize the report object before running all tests
        report = new PopulationCountryReport();
    }

    // ---------------------------------------------------------------------
    // Test: getPopulation_Country_Report with null connection
    // ---------------------------------------------------------------------
    @Test
    void getPopulationCountryReportTestNullConnection()
    {
        List<Country> result = report.getPopulationCountryReport(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ---------------------------------------------------------------------
    // Test: getPopulation_City_vs_NonCity_ByCountry with null connection
    // ---------------------------------------------------------------------
    @Test
    void getPopulationCityVsNonCityByCountryTestNullConnection()
    {
        List<Country> result = report.getPopulationCityVsNonCityByCountry(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_Country_Report with null list
    // ---------------------------------------------------------------------
    @Test
    void printPopulationCountryReportTestNull()
    {
        report.printPopulationCountryReport(null, null);
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_Country_Report with empty list
    // ---------------------------------------------------------------------
    @Test
    void printPopulationCountryReportTestEmpty()
    {
        List<Country> list = new ArrayList<>();
        report.printPopulationCountryReport(list, null);
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_Country_Report with data
    // ---------------------------------------------------------------------
    @Test
    void printPopulationCountryReportTestData()
    {
        List<Country> list = new ArrayList<>();
        Country japan = new Country();
        japan.setCountryName("Japan");
        japan.setPopulation(126000000L);
        japan.setGnp(90000000.0);
        japan.setGnpOld(36000000.0);
        list.add(japan);

        report.printPopulationCountryReport(list, null);
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_Country_Report with specific country filter
    // ---------------------------------------------------------------------
    @Test
    void printPopulationCountryReportTestFilter()
    {
        List<Country> list = new ArrayList<>();

        Country usa = new Country();
        usa.setCountryName("United States");
        usa.setPopulation(331000000L);
        usa.setGnp(220000000.0);
        usa.setGnpOld(111000000.0);
        list.add(usa);

        Country canada = new Country();
        canada.setCountryName("Canada");
        canada.setPopulation(38000000L);
        canada.setGnp(21000000.0);
        canada.setGnpOld(17000000.0);
        list.add(canada);

        // Should print only United States
        report.printPopulationCountryReport(list, "United States");
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_City_vs_NonCity_ByCountry with valid data
    // ---------------------------------------------------------------------
    @Test
    void printPopulationCityVsNonCityByCountryTestData()
    {
        List<Country> list = new ArrayList<>();

        Country france = new Country();
        france.setCountryName("France");
        france.setPopulation(67000000L);
        france.setGnp(45000000.0);
        france.setGnpOld(22000000.0);
        list.add(france);

        report.printPopulationCityVsNonCityByCountry(list);
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_City_vs_NonCity_ByCountry with null list
    // ---------------------------------------------------------------------
    @Test
    void printPopulationCityVsNonCityByCountryTestNull()
    {
        report.printPopulationCityVsNonCityByCountry(null);
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_City_vs_NonCity_ByCountry with empty list
    // ---------------------------------------------------------------------
    @Test
    void printPopulationCityVsNonCityByCountryTestEmpty()
    {
        List<Country> list = new ArrayList<>();
        report.printPopulationCityVsNonCityByCountry(list);
    }
}
