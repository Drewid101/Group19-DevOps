package com.napier.gp19;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple JUnit 5 test class for PopulationRegionReport.
 * logic verification for null checks, empty lists, and valid data paths.
 */
public class PopulationRegionReportUnitTest
{
    // A single shared instance of the report class
    static PopulationRegionReport report;

    @BeforeAll
    static void init()
    {
        // Initialize the report object before running all tests
        report = new PopulationRegionReport();
    }

    // ---------------------------------------------------------------------
    // Test: null connection for getPopulation_Region_Report
    // ---------------------------------------------------------------------
    @Test
    void getPopulationRegionReportTestNullConnection()
    {
        List<Country> result = report.getPopulationRegionReport(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ---------------------------------------------------------------------
    // Test: null connection for getPopulation_Region_Details_Report
    // ---------------------------------------------------------------------
    @Test
    void getPopulationRegionDetailsReportTestNullConnection()
    {
        List<Country> result = report.getPopulationRegionDetailsReport(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_Region_Report with null list
    // ---------------------------------------------------------------------
    @Test
    void printPopulationRegionReportTestNull()
    {
        report.printPopulationRegionReport(null, null);
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_Region_Report with empty list
    // ---------------------------------------------------------------------
    @Test
    void printPopulationRegionReportTestEmpty()
    {
        List<Country> list = new ArrayList<>();
        report.printPopulationRegionReport(list, null);
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_Region_Report with valid data (no filter)
    // ---------------------------------------------------------------------
    @Test
    void printPopulationRegionReportTestData()
    {
        List<Country> list = new ArrayList<>();
        Country southEastAsia = new Country();
        southEastAsia.setRegion("Southeast Asia");
        southEastAsia.setPopulation(700000000L);
        southEastAsia.setGnp(300000000.0);
        southEastAsia.setGnpOld(400000000.0);
        list.add(southEastAsia);

        report.printPopulationRegionReport(list, null);
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_Region_Report with specific region filter
    // ---------------------------------------------------------------------
    @Test
    void printPopulationRegionReportTestFilter()
    {
        List<Country> list = new ArrayList<>();

        Country asia = new Country();
        asia.setRegion("Asia");
        asia.setPopulation(4000000000L);
        asia.setGnp(1500000000.0);
        asia.setGnpOld(2500000000.0);
        list.add(asia);

        Country europe = new Country();
        europe.setRegion("Europe");
        europe.setPopulation(1000000000L);
        europe.setGnp(600000000.0);
        europe.setGnpOld(400000000.0);
        list.add(europe);

        // Only print Asia
        report.printPopulationRegionReport(list, "Asia");
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_Region_Details_Report with valid data
    // ---------------------------------------------------------------------
    @Test
    void printPopulationRegionDetailsReportTestData()
    {
        List<Country> list = new ArrayList<>();

        Country africa = new Country();
        africa.setRegion("Africa");
        africa.setPopulation(1200000000L);
        africa.setGnp(400000000.0);
        africa.setGnpOld(800000000.0);
        list.add(africa);

        report.printPopulationRegionDetailsReport(list);
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_Region_Details_Report with null
    // ---------------------------------------------------------------------
    @Test
    void printPopulationRegionDetailsReportTestNull()
    {
        report.printPopulationRegionDetailsReport(null);
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_Region_Details_Report with empty list
    // ---------------------------------------------------------------------
    @Test
    void printPopulationRegionDetailsReportTestEmpty()
    {
        List<Country> list = new ArrayList<>();
        report.printPopulationRegionDetailsReport(list);
    }
}
