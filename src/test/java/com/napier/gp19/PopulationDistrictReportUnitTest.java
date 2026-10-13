package com.napier.gp19;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PopulationDistrictReport.
 */
public class PopulationDistrictReportUnitTest
{
    // A single shared instance of the report class
    static PopulationDistrictReport report;

    @BeforeAll
    static void init()
    {
        // Initialize the report object before running all tests
        report = new PopulationDistrictReport();
    }

    // ---------------------------------------------------------------------
    // Test: getPopulation_District_Report with null connection
    // ---------------------------------------------------------------------
    @Test
    void getPopulationDistrictReportTestNullConnection()
    {
        List<Country> results = report.getPopulationDistrictReport(null);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_District_Report with null list
    // ---------------------------------------------------------------------
    @Test
    void printPopulationDistrictReportTestNull()
    {
        List<Country> results = new ArrayList<>();
        report.printPopulationDistrictReport(results, "TestDistrict");
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_District_Report with sample data
    // ---------------------------------------------------------------------
    @Test
    void printPopulationDistrictReportTestData()
    {
        List<Country> countries = new ArrayList<>();

        Country c = new Country();
        c.setCountryName("Yangon");
        c.setPopulation(8_000_000);
        c.setGnp(7_500_000.0);
        c.setGnpOld(500_000.0);
        countries.add(c);

        report.printPopulationDistrictReport(countries, "Yangon");
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_District_Report with no matching district
    // ---------------------------------------------------------------------
    @Test
    void printPopulationDistrictReportTestNoMatch()
    {
        List<Country> countries = new ArrayList<>();

        Country c = new Country();
        c.setCountryName("Mandalay");
        c.setPopulation(3_000_000);
        c.setGnp(2_700_000.0);
        c.setGnpOld(300_000.0);
        countries.add(c);

        // selectedDistrict does not match → nothing should print but no error
        report.printPopulationDistrictReport(countries, "Yangon");
    }

    // ---------------------------------------------------------------------
    // Test: getPopulation_District_Report with fake connection (null-like)
    // ---------------------------------------------------------------------
    @Test
    void getPopulationDistrictReportFakeConnection()
    {
        Connection conn = null;
        List<Country> results = report.getPopulationDistrictReport(conn);
        assertNotNull(results);
    }
}
