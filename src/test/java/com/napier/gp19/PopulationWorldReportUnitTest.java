package com.napier.gp19;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PopulationWorldReport.
 * Consistent in style with PopulationRegionReportUnitTest and PopulationCountryReportUnitTest.
 */
public class PopulationWorldReportUnitTest
{
    static PopulationWorldReport report;

    @BeforeAll
    static void init()
    {
        report = new PopulationWorldReport();
    }

    // ---------------------------------------------------------------------
    // Test: getPopulation_World_Report with null connection
    // ---------------------------------------------------------------------
    @Test
    void getPopulationWorldReportTestNullConnection()
    {
        PopulationWorldReport.PopulationData data = report.getPopulationWorldReport(null);
        assertNotNull(data);
        assertEquals("World", data.name);
        assertEquals(0, data.totalPopulation);
        assertEquals(0, data.cityPopulation);
        assertEquals(0, data.nonCityPopulation);
        assertEquals(0.0, data.cityPercentage);
        assertEquals(0.0, data.nonCityPercentage);
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_World_Report with null data
    // ---------------------------------------------------------------------
    @Test
    void printPopulationWorldReportTestNull()
    {
        report.printPopulationWorldReport(new PopulationWorldReport.PopulationData("World"));
    }

    // ---------------------------------------------------------------------
    // Test: printPopulation_World_Report with populated data
    // ---------------------------------------------------------------------
    @Test
    void printPopulationWorldReportTestData()
    {
        PopulationWorldReport.PopulationData data = new PopulationWorldReport.PopulationData("World");
        data.totalPopulation = 8_000_000_000L;
        data.cityPopulation = 4_400_000_000L;
        data.nonCityPopulation = 3_600_000_000L;
        data.cityPercentage = 55.0;
        data.nonCityPercentage = 45.0;

        report.printPopulationWorldReport(data);
    }

    // ---------------------------------------------------------------------
    // Test: getPopulation_World_Report with mock-like zero values (no real DB)
    // ---------------------------------------------------------------------
    @Test
    void getPopulationWorldReportTestEmptyDatabase()
    {
        // This test just ensures no exceptions with a fake (null-like) connection
        Connection conn = null;
        PopulationWorldReport.PopulationData data = report.getPopulationWorldReport(conn);
        assertNotNull(data);
    }

    // ---------------------------------------------------------------------
    // Test: PopulationData constructor validation
    // ---------------------------------------------------------------------
    @Test
    void populationDataConstructorTest()
    {
        PopulationWorldReport.PopulationData data = new PopulationWorldReport.PopulationData("Earth");
        assertEquals("Earth", data.name);
        assertEquals(0, data.totalPopulation);
    }

    @Test
    void printPopulationWorldReportTest_NullDataBranch() {
        // This explicitly passes null to trigger the "if (data == null)" condition
        report.printPopulationWorldReport(null);
    }

}
