package com.napier.gp19;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for all Capital City report classes:
 * - CapitalCitiesReportBase
 * - CapitalCitiesWorldReport
 * - CapitalCitiesContinentReport
 * - CapitalCitiesRegionReport
 * These tests validate null handling, empty data handling,
 * and sample data printing logic across all report layers.
 */

class CapitalCitiesReportsUnitTest {

    // Report classes under test
    CapitalCitiesWorldReport worldReport;
    CapitalCitiesContinentReport continentReport;
    CapitalCitiesRegionReport regionReport;
    CapitalCitiesReportBase baseReport;

    // Test datasets
    ArrayList<City> emptyList;
    ArrayList<City> sampleList;
    ArrayList<City> continentSample;
    ArrayList<City> regionSample;

    @BeforeEach
    void setUp() {
        // Initialize all report objects
        worldReport = new CapitalCitiesWorldReport();
        continentReport = new CapitalCitiesContinentReport();
        regionReport = new CapitalCitiesRegionReport();
        baseReport = new CapitalCitiesReportBase();

        // Initialize array lists
        emptyList = new ArrayList<>();
        sampleList = new ArrayList<>();
        continentSample = new ArrayList<>();
        regionSample = new ArrayList<>();

        // ---------- Country 1: Japan ----------
        Country japan = new Country();
        japan.setCountryName("Japan");
        japan.setCode("JPN");
        japan.setContinent("Asia");
        japan.setRegion("Eastern Asia");

        City tokyo = new City();
        tokyo.setId(1);
        tokyo.setCityName("Tokyo");
        tokyo.setCountryCode("JPN");
        tokyo.setDistrict("Tokyo");
        tokyo.setPopulation(37400068);
        tokyo.setCountry(japan);

        sampleList.add(tokyo);
        continentSample.add(tokyo);
        regionSample.add(tokyo);

        // ---------- Country 2: India ----------
        Country india = new Country();
        india.setCountryName("India");
        india.setCode("IND");
        india.setContinent("Asia");
        india.setRegion("Southern Asia");

        City delhi = new City();
        delhi.setId(2);
        delhi.setCityName("Delhi");
        delhi.setCountryCode("IND");
        delhi.setDistrict("Delhi");
        delhi.setPopulation(31870000);
        delhi.setCountry(india);

        sampleList.add(delhi);
        continentSample.add(delhi);
        regionSample.add(delhi);

    }

    // ---------------- CapitalCitiesReportBase ----------------

    /**
     * Tests defensive null handling in buildCapitalCitiesFromResultSet().
     * Ensures that passing a null ResultSet returns an empty list instead of throwing an exception.
     */
    @Test
    void buildCapitalCitiesFromResultSetTest_NullResultSet() {
        ArrayList<City> result = baseReport.buildCapitalCitiesFromResultSet(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    /**
     * Tests printCapitalCities() to confirm it handles:
     * - normal data
     * - city objects with null countries
     * - null entries in the list
     */
    @Test
    void printCapitalCities() {
        // Reuse original sampleList for true branch
        ArrayList<City> list = new ArrayList<>(sampleList);

        // Add city with null country → triggers false branch
        City cityWithoutCountry = new City();
        cityWithoutCountry.setCityName("NoCountryCity");
        cityWithoutCountry.setPopulation(1000);
        list.add(cityWithoutCountry);

        // Add null city → triggers false branch
        list.add(null);

        // Call the method
        baseReport.printCapitalCities(list, "Capital City Report Test");

        // Assertions
        assertEquals(4, list.size());
        assertEquals("Tokyo", list.get(0).getCityName());
        assertEquals("Delhi", list.get(1).getCityName());
        assertEquals("NoCountryCity", list.get(2).getCityName());
        assertNull(list.get(3));
    }

    // ---------------- World Report ----------------
    /**
     * Verifies that getAllCapitalCitiesInWorldByPopulation() safely
     * returns an empty list when the database connection is null.
     */
    @Test
    void getAllCapitalCitiesTest_NullConnection() {
        ArrayList<City> capitals = worldReport.getAllCapitalCitiesInWorldByPopulation(null); // will pass but empty
        assertNotNull(capitals);
        assertEquals(0, capitals.size());
    }

    /**
     * Ensures that requesting top N capital cities with a null connection
     * also returns an empty list without throwing an exception.
     */
    @Test
    void getTopNCapitalCitiesTest_NullConnection() {
        ArrayList<City> capitals = worldReport.getTopNCapitalCitiesInWorldByPopulation(null, 3);
        assertNotNull(capitals);
        assertEquals(0, capitals.size());// zero requested should also give empty list
    }


    /**
     * Validates correct behavior of printAllCapitalCitiesInWorldByPopulation()
     * with a populated list of cities.
     */
    @Test
    void printAllCapitalCitiesWithSampleData() {
        worldReport.printAllCapitalCitiesInWorldByPopulation(sampleList);
        assertEquals(2, sampleList.size());
        assertEquals("Tokyo", sampleList.getFirst().getCityName());
        assertTrue(sampleList.getFirst().getPopulation() > 0);
    }


    /**
     * Ensures printAllCapitalCitiesInWorldByPopulation() safely handles empty lists.
     */
    @Test
    void printAllCapitalCities_EmptyList() {
        worldReport.printAllCapitalCitiesInWorldByPopulation(emptyList);
        assertEquals(0, emptyList.size());
    }

    /**
     * Ensures printTopNCapitalCitiesInWorldByPopulation() safely handles empty lists.
     */
    @Test
    void printTopNCapitalCities_EmptyList() {
        worldReport.printTopNCapitalCitiesInWorldByPopulation(emptyList, 5);
        assertEquals(0, emptyList.size());
    }

    //--------------Continent-------------------------

    /**
     * Tests null connection handling for continent-level capital city retrieval.
     */
    @Test
    void getAllCapitalCitiesInContinentTest_NullConnection() {
        ArrayList<City> result = continentReport.getAllCapitalCitiesInContinentByPopulation(null, "Asia");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    /**
     * Tests top-N retrieval when connection is null for a continent query.
     */
    @Test
    void getTopNCapitalCitiesInContinentTest_NullConnection() {
        ArrayList<City> result = continentReport.getTopNCapitalCitiesInContinentByPopulation(null, "Europe", 3);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Verifies printAllCapitalCitiesInContinentByPopulation() correctly displays
     * multiple Asian capitals using sample test data.
     */
    @Test
    void printAllCapitalCitiesInContinent() {
        continentReport.printAllCapitalCitiesInContinentByPopulation(continentSample, "Asia");
        assertEquals(2, continentSample.size());
        assertEquals("Tokyo", continentSample.get(0).getCityName());
        assertEquals("Delhi", continentSample.get(1).getCityName());
    }



    /**
     * Confirms that empty continent lists are handled safely during top-N print.
     */
    @Test
    void printTopNCapitalCitiesInContinent_EmptyList() {
        continentReport.printTopNCapitalCitiesInContinentByPopulation(emptyList,"Asia",2);
        assertEquals(0, emptyList.size());
    }


    //--------------Region-------------------------

    /**
     * Ensures getAllCapitalCitiesInRegionByPopulation() returns
     * an empty list safely when connection is null.
     */
    @Test
    void getAllCapitalCitiesInRegionTest_NullConnection() {
        ArrayList<City> result = regionReport.getAllCapitalCitiesInRegionByPopulation(null, "Eastern Asia");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Ensures top-N regional report query handles null connection gracefully.
     */
    @Test
    void getTopNCapitalCitiesInRegionTest_NullConnection() {
        ArrayList<City> result = regionReport.getTopNCapitalCitiesInRegionByPopulation(null, "Southern Asia", 2);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Validates region-level report printing for Eastern Asia (Tokyo only).
     */
    @Test
    void printAllCapitalCitiesInRegion_EasternAsia() {
        // Use only Tokyo for Eastern Asia report
        ArrayList<City> easternAsia = new ArrayList<>();
        for (City c : regionSample) {
            if (c.getCountry().getRegion().equals("Eastern Asia")) {
                easternAsia.add(c);
            }
        }

        regionReport.printAllCapitalCitiesInRegionByPopulation(easternAsia, "Eastern Asia");

        assertEquals(1, easternAsia.size());
        assertEquals("Tokyo", easternAsia.getFirst().getCityName());
        assertEquals("Eastern Asia", easternAsia.getFirst().getCountry().getRegion());
    }

    /**
     * Validates region-level report printing for Southern Asia (Delhi only).
     */
    @Test
    void printAllCapitalCitiesInRegion_SouthernAsia() {
        // Use only Delhi for Southern Asia report
        ArrayList<City> southernAsia = new ArrayList<>();
        for (City c : regionSample) {
            if (c.getCountry().getRegion().equals("Southern Asia")) {
                southernAsia.add(c);
            }
        }

        regionReport.printAllCapitalCitiesInRegionByPopulation(southernAsia, "Southern Asia");

        assertEquals(1, southernAsia.size());
        assertEquals("Delhi", southernAsia.getFirst().getCityName());
        assertEquals("Southern Asia", southernAsia.getFirst().getCountry().getRegion());
    }

    /**
     * Confirms top-N region report printing handles empty input lists safely.
     */
    @Test
    void printTopNCapitalCitiesInRegion_EmptyList() {
        regionReport.printTopNCapitalCitiesInRegionByPopulation(emptyList, "Eastern Asia", 5);
        assertEquals(0, emptyList.size());
    }

    /**
     * Validates printAllCapitalCitiesInRegionByPopulation() does not fail with a null list.
     */
    @Test
    void printAllCapitalCitiesInRegion_NullList() {
        regionReport.printAllCapitalCitiesInRegionByPopulation(null, "Southern Asia");
        assertTrue(true); // method should handle null safely
    }

    /**
     * Validates printTopNCapitalCitiesInRegionByPopulation() handles null list safely.
     */
    @Test
    void printTopNCapitalCitiesInRegion_NullList() {
        regionReport.printTopNCapitalCitiesInRegionByPopulation(null, "Southern Asia", 3);
        assertTrue(true); // method should handle null safely
    }

}
