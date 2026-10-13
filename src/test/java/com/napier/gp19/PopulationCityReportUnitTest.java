package com.napier.gp19;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PopulationCityReport.
 * Consistent with the style used in other population report test classes.
 */
public class PopulationCityReportUnitTest
{
    // A single shared instance of the report class
    static PopulationCityReport report;

    @BeforeAll
    static void init()
    {
        // Initialize the report object before running all tests
        report = new PopulationCityReport();
    }

    // ---------------------------------------------------------------------
    // Test: getPopulation_City_Report with null connection
    // ---------------------------------------------------------------------
    @Test
    void getPopulationCityReportTestNullConnection()
    {
        List<City> cities = report.getPopulationCityReport(null);
        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_City_Report with null list
    // ---------------------------------------------------------------------
    @Test
    void printPopulationCityReportTestEmptyList()
    {
        List<City> cities = new ArrayList<>();
        report.printPopulationCityReport(cities);
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_City_Report with sample data
    // ---------------------------------------------------------------------
    @Test
    void printPopulationCityReportTestData()
    {
        List<City> cities = new ArrayList<>();

        City city = new City();
        city.setId(1);
        city.setCityName("Yangon");
        city.setCountryCode("MMR");
        city.setDistrict("Yangon");
        city.setPopulation(7_360_703);
        cities.add(city);

        report.printPopulationCityReport(cities);
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_City_Report with selected city (filter)
    // ---------------------------------------------------------------------
    @Test
    void printPopulationCityReportTestFilteredCity()
    {
        List<City> cities = new ArrayList<>();

        City city1 = new City();
        city1.setId(1);
        city1.setCityName("Yangon");
        city1.setCountryCode("MMR");
        city1.setDistrict("Yangon");
        city1.setPopulation(7_360_703);
        cities.add(city1);

        City city2 = new City();
        city2.setId(2);
        city2.setCityName("Mandalay");
        city2.setCountryCode("MMR");
        city2.setDistrict("Mandalay");
        city2.setPopulation(1_225_553);
        cities.add(city2);

        // Should only print Yangon
        report.printPopulationCityReport(cities, "Yangon");
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_City_Report with no matching city
    // ---------------------------------------------------------------------
    @Test
    void printPopulationCityReportTestNoMatch()
    {
        List<City> cities = new ArrayList<>();

        City city = new City();
        city.setId(3);
        city.setCityName("Naypyidaw");
        city.setCountryCode("MMR");
        city.setDistrict("Mandalay Region");
        city.setPopulation(924_608);
        cities.add(city);

        // Selected city doesn't exist → should print nothing but no crash
        report.printPopulationCityReport(cities, "Yangon");
    }

    // ---------------------------------------------------------------------
    // Test: getPopulation_City_Report with fake (null-like) connection
    // ---------------------------------------------------------------------
    @Test
    void getPopulationCityReportFakeConnection()
    {
        Connection conn = null;
        List<City> results = report.getPopulationCityReport(conn);
        assertNotNull(results);
    }
}
