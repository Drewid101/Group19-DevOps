package com.napier.gp19;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LanguagePopulationReport.
 * Updated to match version using CountryLanguage class.
 */
public class LanguagePopulationReportUnitTest
{
    // A single shared instance of the report class
    static LanguagePopulationReport report;

    @BeforeAll
    static void init()
    {
        // Initialize the report object before running all tests
        report = new LanguagePopulationReport();
    }

    // ---------------------------------------------------------------------
    // Test: getLanguagePopulationReport with null connection
    // ---------------------------------------------------------------------
    @Test
    void getLanguagePopulationReportTestNullConnection()
    {
        ArrayList<CountryLanguage> result = report.getLanguagePopulationReport(null);
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Result list should be empty when connection is null");
    }

    // ---------------------------------------------------------------------
    // Test: printLanguagePopulationReport with null list
    // ---------------------------------------------------------------------
    @Test
    void printLanguagePopulationReportTestNullList()
    {
        assertDoesNotThrow(() -> report.printLanguagePopulationReport(null));
    }

    // ---------------------------------------------------------------------
    // Test: printLanguagePopulationReport with empty list
    // ---------------------------------------------------------------------
    @Test
    void printLanguagePopulationReportTestEmptyList()
    {
        ArrayList<CountryLanguage> reportList = new ArrayList<>();
        assertDoesNotThrow(() -> report.printLanguagePopulationReport(reportList));
    }

    // ---------------------------------------------------------------------
    // Test: printLanguagePopulationReport with sample data
    // ---------------------------------------------------------------------
    @Test
    void printLanguagePopulationReportTestData()
    {
        ArrayList<CountryLanguage> reportList = new ArrayList<>();

        CountryLanguage chinese = new CountryLanguage();
        chinese.setLanguage("Chinese");
        chinese.setCountryCode("1,200,000,000");  // formatted speakers as string
        chinese.setPercentage(15.5);              // world percentage
        reportList.add(chinese);

        CountryLanguage english = new CountryLanguage();
        english.setLanguage("English");
        english.setCountryCode("950,000,000");
        english.setPercentage(12.3);
        reportList.add(english);

        assertDoesNotThrow(() -> report.printLanguagePopulationReport(reportList));
    }

    // ---------------------------------------------------------------------
    // Test: getLanguagePopulationReport with fake connection (null)
    // ---------------------------------------------------------------------
    @Test
    void getLanguagePopulationReportFakeConnection()
    {
        Connection conn = null;
        ArrayList<CountryLanguage> result = report.getLanguagePopulationReport(conn);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ---------------------------------------------------------------------
    // Test: printLanguagePopulationReport with incomplete data
    // ---------------------------------------------------------------------
    @Test
    void printLanguagePopulationReportIncompleteData()
    {
        ArrayList<CountryLanguage> reportList = new ArrayList<>();
        CountryLanguage hindi = new CountryLanguage();
        hindi.setLanguage("Hindi");
        hindi.setCountryCode("800,000,000");
        // Missing percentage (world %) on purpose
        reportList.add(hindi);

        // Should print without throwing exceptions
        assertDoesNotThrow(() -> report.printLanguagePopulationReport(reportList));
    }
}
