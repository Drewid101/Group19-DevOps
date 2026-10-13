package com.napier.gp19;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for all Country report classes:
 * - CountriesReportBase
 * - CountriesWorldReport
 * - CountriesContinentReport
 * - CountriesRegionReport
 * Tests cover:
 * - Null handling for DB connections and lists
 * - Empty data handling
 * - Sample data printing across world/continent/region layers
 * - Base ResultSet null defensiveness
 */
class CountriesReportsUnitTest {

    // Report classes under test
    CountriesWorldReport worldReport;
    CountriesContinentReport continentReport;
    CountriesRegionReport regionReport;
    CountriesReportBase baseReport;

    // Test datasets
    ArrayList<Country> emptyList;
    ArrayList<Country> sampleWorld;
    ArrayList<Country> continentSample;
    ArrayList<Country> regionSample;

    @BeforeEach
    void setUp() {
        // Initialize all report objects
        worldReport = new CountriesWorldReport();
        continentReport = new CountriesContinentReport();
        regionReport = new CountriesRegionReport();
        baseReport = new CountriesReportBase();

        // Initialize lists
        emptyList = new ArrayList<>();
        sampleWorld = new ArrayList<>();
        continentSample = new ArrayList<>();
        regionSample = new ArrayList<>();

        // ---------- Country 1: Germany ----------
        Country germany = new Country();
        germany.setCountryName("Germany");
        germany.setCode("DEU");
        germany.setContinent("Europe");
        germany.setRegion("Western Europe");
        germany.setPopulation(83_783_942);

        // ---------- Country 2: France ----------
        Country france = new Country();
        france.setCountryName("France");
        france.setCode("FRA");
        france.setContinent("Europe");
        france.setRegion("Western Europe");
        france.setPopulation(65_273_511);

        // ---------- Country 3: Italy ----------
        Country italy = new Country();
        italy.setCountryName("Italy");
        italy.setCode("ITA");
        italy.setContinent("Europe");
        italy.setRegion("Southern Europe");
        italy.setPopulation(60_367_477);

        // Populate lists
        sampleWorld.add(germany);
        sampleWorld.add(france);
        sampleWorld.add(italy);

        continentSample.add(germany);
        continentSample.add(france);
        continentSample.add(italy);

        regionSample.add(germany); // Western Europe
        regionSample.add(france);  // Western Europe
        regionSample.add(italy);   // Southern Europe
    }

    // ---------------- CountriesReportBase ----------------

    /**
     * Ensures buildCountriesFromResultSet() returns an empty list when given null.
     */
    @Test
    void buildCountriesFromResultSet_NullResultSet() {
        ArrayList<Country> result = baseReport.buildCountriesFromResultSet(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Validates printCountries() with:
     * - normal data
     * - a null entry in the list
     */
    @Test
    void printCountries_Base_PrintsAndSkipsNulls() {
        ArrayList<Country> list = new ArrayList<>(sampleWorld);

        // Add a null entry → should be safely skipped by printer
        list.add(null);

        baseReport.printCountries(list, "Countries Base Print Test");

        assertEquals(4, list.size());
        assertEquals("Germany", list.get(0).getCountryName());
        assertEquals("France", list.get(1).getCountryName());
        assertEquals("Italy", list.get(2).getCountryName());
        assertNull(list.get(3));
    }

    /**
     * Ensures printCountries() handles null list safely.
     */
    @Test
    void printCountries_Base_NullList() {
        baseReport.printCountries(null, "Null List Test");
        assertTrue(true);
    }

    /**
     * Ensures printCountries() handles empty list safely.
     */
    @Test
    void printCountries_Base_EmptyList() {
        baseReport.printCountries(emptyList, "Empty List Test");
        assertTrue(true);
    }

    // ---------------- World Report ----------------

    /**
     * Verifies getCountries_World_Report() returns empty when connection is null.
     */
    @Test
    void getCountries_World_Report_NullConnection() {
        ArrayList<Country> countries = worldReport.getCountriesWorldReport(null);
        assertNotNull(countries);
        assertEquals(0, countries.size());
    }

    /**
     * Verifies getTopNCountries_World_Report() returns empty when connection is null.
     */
    @Test
    void getTopNCountries_World_Report_NullConnection() {
        ArrayList<Country> countries = worldReport.getTopNCountriesWorldReport(null, 2);
        assertNotNull(countries);
        assertEquals(0, countries.size());
    }

    /**
     * Validates world print with sample data.
     */
    @Test
    void printCountries_World_Report_WithSampleData() {
        worldReport.printCountriesWorldReport(sampleWorld);
        assertEquals(3, sampleWorld.size());
        assertEquals("Germany", sampleWorld.getFirst().getCountryName());
        assertTrue(sampleWorld.getFirst().getPopulation() > 0);
    }

    /**
     * Ensures top-N world print handles empty lists.
     */
    @Test
    void printTopNCountries_World_Report_EmptyList() {
        worldReport.printTopNCountriesWorldReport(emptyList, 5);
        assertEquals(0, emptyList.size());
    }

    // ---------------- Continent Report ----------------

    /**
     * Null connection handling for continent-level retrieval.
     */
    @Test
    void getCountries_Continent_Report_NullConnection() {
        ArrayList<Country> result = continentReport.getCountriesContinentReport(null, "Europe");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Null connection handling for top-N continent retrieval.
     */
    @Test
    void getTopNCountries_Continent_Report_NullConnection() {
        ArrayList<Country> result = continentReport.getTopNCountriesContinentReport(null, "Europe", 2);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Validates continent print for Europe using sample data.
     */
    @Test
    void printCountries_Continent_Report_Europe() {
        continentReport.printCountriesContinentReport("Europe", continentSample);
        assertEquals(3, continentSample.size());
        assertEquals("Germany", continentSample.get(0).getCountryName());
        assertEquals("France", continentSample.get(1).getCountryName());
        assertEquals("Italy", continentSample.get(2).getCountryName());
    }

    /**
     * Ensures top-N continent print handles empty lists.
     */
    @Test
    void printTopNCountries_Continent_Report_EmptyList() {
        continentReport.printTopNCountriesContinentReport("Europe", emptyList, 2);
        assertEquals(0, emptyList.size());
    }

    // ---------------- Region Report ----------------

    /**
     * Null connection handling for region-level retrieval (all in region).
     */
    @Test
    void getCountries_Region_Report_NullConnection() {
        ArrayList<Country> result = regionReport.getCountriesRegionReport(null, "Western Europe");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Null connection handling for top-N region retrieval.
     */
    @Test
    void getTopNCountries_Region_Report_NullConnection() {
        ArrayList<Country> result = regionReport.getTopNCountriesRegionReport(null, "Southern Europe", 2);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Region print test for Western Europe (Germany & France).
     */
    @Test
    void printCountries_Region_Report_WesternEurope() {
        ArrayList<Country> westernEurope = new ArrayList<>();
        for (Country c : regionSample) {
            if ("Western Europe".equals(c.getRegion())) {
                westernEurope.add(c);
            }
        }

        regionReport.printCountriesRegionReport("Western Europe", westernEurope);

        assertEquals(2, westernEurope.size());
        assertEquals("Germany", westernEurope.get(0).getCountryName());
        assertEquals("France", westernEurope.get(1).getCountryName());
        assertEquals("Western Europe", westernEurope.getFirst().getRegion());
    }

    /**
     * Region print test for Southern Europe (Italy only).
     */
    @Test
    void printCountries_Region_Report_SouthernEurope() {
        ArrayList<Country> southernEurope = new ArrayList<>();
        for (Country c : regionSample) {
            if ("Southern Europe".equals(c.getRegion())) {
                southernEurope.add(c);
            }
        }

        regionReport.printCountriesRegionReport("Southern Europe", southernEurope);

        assertEquals(1, southernEurope.size());
        assertEquals("Italy", southernEurope.getFirst().getCountryName());
        assertEquals("Southern Europe", southernEurope.getFirst().getRegion());
    }

    /**
     * Ensures top-N region print handles empty input lists.
     */
    @Test
    void printTopNCountries_Region_Report_EmptyList() {
        regionReport.printTopNCountriesRegionReport("Western Europe", emptyList, 5);
        assertEquals(0, emptyList.size());
    }

    /**
     * Ensures region print handles null list safely.
     */
    @Test
    void printCountries_Region_Report_NullList() {
        regionReport.printCountriesRegionReport("Western Europe", null);
        assertTrue(true);
    }

    /**
     * Ensures top-N region print handles null list safely.
     */
    @Test
    void printTopNCountries_Region_Report_NullList() {
        regionReport.printTopNCountriesRegionReport("Western Europe", null, 3);
        assertTrue(true);
    }
}
