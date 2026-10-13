package com.napier.gp19;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Full integration test suite for App and all report modules,
 * including Country, City, Capital, Population, and Language reports.
 * Also tests Top N reports, edge cases, and SQL exception handling.
 * REQUIREMENTS:
 * - MySQL container or local DB running with 'world' schema.
 * - User: root, Password: root
 * - Port: 33060
 * Run with: mvn test
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppIntegrationTest {

    private static App app;

    /**
     * Setup method executed once before all tests.
     * Initializes App instance and establishes DB connection.
     */
    @BeforeAll
    static void setUp() {
        app = new App();
        app.connect("localhost:33060", 5000);
        // Verify app instance is created
        assertNotNull(app, "App instance should not be null");
        // Verify database connection is established
        assertNotNull(app.getConnection(), "Database connection should be established");
    }

    // Connection Establishment
    @Test
    @Order(1)
    void testDatabaseConnection() {
        try {
            Connection conn = app.getConnection();
            assertNotNull(conn, "Connection should not be null");
            assertFalse(conn.isClosed(), "Connection should be open");
        } catch (Exception e) {
            fail("Database connection test failed: " + e.getMessage());
        }
    }

    // =========================
    // Country Reports Integration Tests
    // =========================
    @Test
    @Order(2)
    void testCountriesWorldReportIntegration() {
        // Verify that the world countries report returns a non-null, non-empty list
        CountriesWorldReport report = new CountriesWorldReport();
        ArrayList<Country> countries = report.getCountriesWorldReport(app.getConnection());
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
    }

    @Test
    @Order(3)
    void testCountriesContinentReportIntegration() {
        // Verify continent-specific report returns valid data
        CountriesContinentReport report = new CountriesContinentReport();
        ArrayList<Country> countries = report.getCountriesContinentReport(app.getConnection(), "Asia");
        assertNotNull(countries); // Check that result is not null
        assertFalse(countries.isEmpty()); // Check that result contains data
    }

    @Test
    @Order(4)
    void testCountriesRegionReportIntegration() {
        // Verify region-specific report returns valid data
        CountriesRegionReport report = new CountriesRegionReport();
        ArrayList<Country> countries = report.getCountriesRegionReport(app.getConnection(), "Middle East");
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
    }

    // =========================
    // Top N Country Reports Integration Tests
    // =========================
    @Test
    @Order(5)
    void testTopNCountriesWorldIntegration() {
        // Verify Top N world countries report returns at most N results
        CountriesWorldReport report = new CountriesWorldReport();
        ArrayList<Country> topNCountries = report.getTopNCountriesWorldReport(app.getConnection(), 10);
        assertNotNull(topNCountries);
        assertFalse(topNCountries.isEmpty());
        assertTrue(topNCountries.size() <= 10);
    }

    @Test
    @Order(6)
    void testTopNCountriesContinentIntegration() {
        // Verify Top N countries in a continent
        CountriesContinentReport report = new CountriesContinentReport();
        ArrayList<Country> topNCountries = report.getTopNCountriesContinentReport(app.getConnection(), "Asia", 10);
        assertNotNull(topNCountries);
        assertFalse(topNCountries.isEmpty());
        assertTrue(topNCountries.size() <= 10);
    }

    @Test
    @Order(7)
    void testTopNCountriesRegionIntegration() {
        // Verify Top N countries in a region
        CountriesRegionReport report = new CountriesRegionReport();
        ArrayList<Country> topNCountries = report.getTopNCountriesRegionReport(app.getConnection(), "Middle East", 10);
        assertNotNull(topNCountries);
        assertFalse(topNCountries.isEmpty());
        assertTrue(topNCountries.size() <= 10);
    }

    // =========================
    // City Reports Integration Tests
    // =========================
    @Test
    @Order(8)
    void testCitiesWorldReportIntegration() {
        // Verify world cities report returns valid data
        CitiesWorldReport report = new CitiesWorldReport();
        ArrayList<City> cities = report.getCitiesWorldReport(app.getConnection());
        assertNotNull(cities);
        assertFalse(cities.isEmpty());
    }

    @Test
    @Order(9)
    void testCitiesContinentReportIntegration() {
        // Verify continent-specific city report
        CitiesContinentReport report = new CitiesContinentReport();
        ArrayList<City> cities = report.getCitiesContinentReport(app.getConnection(), "Asia");
        assertNotNull(cities);
        assertFalse(cities.isEmpty());
    }

    @Test
    @Order(10)
    void testCitiesRegionReportIntegration() {
        // Verify region-specific city report
        CitiesRegionReport report = new CitiesRegionReport();
        ArrayList<City> cities = report.getCitiesRegionReport(app.getConnection(), "Eastern Asia");
        assertNotNull(cities);
        assertFalse(cities.isEmpty());
    }

    @Test
    @Order(11)
    void testCitiesCountryReportIntegration() {
        // Verify country-specific city report
        CitiesCountryReport report = new CitiesCountryReport();
        ArrayList<City> cities = report.getCitiesCountryReport(app.getConnection(), "Myanmar");
        assertNotNull(cities);
        assertFalse(cities.isEmpty());
    }

    @Test
    @Order(12)
    void testCitiesDistrictReportIntegration() {
        // Verify district-specific city report
        CitiesDistrictReport report = new CitiesDistrictReport();
        ArrayList<City> cities = report.getCitiesDistrictReport(app.getConnection(), "California");
        assertNotNull(cities);
        assertFalse(cities.isEmpty());
    }

    // =========================
    // Top N City Reports Integration Tests
    // =========================
    @Test
    @Order(13)
    void testTopNCitiesWorldIntegration() {
        // Verify Top N cities globally
        CitiesWorldReport report = new CitiesWorldReport();
        ArrayList<City> topCities = report.getTopNCitiesWorldReport(app.getConnection(), 10);
        assertNotNull(topCities);
        assertFalse(topCities.isEmpty());
        assertTrue(topCities.size() <= 10);
    }

    @Test
    @Order(14)
    void testTopNCitiesContinentIntegration() {
        // Verify Top N cities in a continent
        CitiesContinentReport report = new CitiesContinentReport();
        ArrayList<City> topCities = report.getTopNCitiesContinentReport(app.getConnection(), "Asia", 10);
        assertNotNull(topCities);
        assertFalse(topCities.isEmpty());
        assertTrue(topCities.size() <= 10);
    }

    @Test
    @Order(15)
    void testTopNCitiesRegionIntegration() {
        // Verify Top N cities in a region
        CitiesRegionReport report = new CitiesRegionReport();
        ArrayList<City> topCities = report.getTopNCitiesRegionReport(app.getConnection(), "Eastern Asia", 10);
        assertNotNull(topCities);
        assertFalse(topCities.isEmpty());
        assertTrue(topCities.size() <= 10);
    }

    @Test
    @Order(16)
    void testTopNCitiesCountryIntegration() {
        // Verify Top N cities in a country
        CitiesCountryReport report = new CitiesCountryReport();
        ArrayList<City> topCities = report.getTopNCitiesCountryReport(app.getConnection(), "Japan", 10);
        assertNotNull(topCities, "Top N Cities Country list should not be null");
        assertFalse(topCities.isEmpty(), "Top N Cities Country list should contain data");
        assertTrue(topCities.size() <= 10, "Top N Cities Country list should have at most 10 items");
    }

    @Test
    @Order(17)
    void testTopNCitiesDistrictIntegration() {
        // Verify Top N cities in a district
        CitiesDistrictReport report = new CitiesDistrictReport();
        ArrayList<City> topCities = report.getTopNCitiesDistrictReport(app.getConnection(), "California", 10);
        assertNotNull(topCities, "Top N Cities District list should not be null");
        assertFalse(topCities.isEmpty(), "Top N Cities District list should contain data");
        assertTrue(topCities.size() <= 10, "Top N Cities District list should have at most 10 items");
    }


    // =========================
    // Capital City Reports Integration Tests
    // =========================
    @Test
    @Order(18)
    void testCapitalCitiesWorldIntegration() {
        // Verify world capital cities report
        CapitalCitiesWorldReport report = new CapitalCitiesWorldReport();
        ArrayList<City> capitals = report.getAllCapitalCitiesInWorldByPopulation(app.getConnection());
        assertNotNull(capitals);
        assertFalse(capitals.isEmpty());
    }

    @Test
    @Order(19)
    void testCapitalCitiesContinentIntegration() {
        // Verify continent-specific capital cities
        CapitalCitiesContinentReport report = new CapitalCitiesContinentReport();
        ArrayList<City> capitals = report.getAllCapitalCitiesInContinentByPopulation(app.getConnection(), "Asia");
        assertNotNull(capitals);
        assertFalse(capitals.isEmpty());
    }

    @Test
    @Order(20)
    void testCapitalCitiesRegionIntegration() {
        // Verify region-specific capital cities
        CapitalCitiesRegionReport report = new CapitalCitiesRegionReport();
        ArrayList<City> capitals = report.getAllCapitalCitiesInRegionByPopulation(app.getConnection(), "Middle East");
        assertNotNull(capitals);
        assertFalse(capitals.isEmpty());
    }

    // =========================
    // Top N Capital Cities Integration Tests
    // =========================
    @Test
    @Order(21)
    void testTopNCapitalsWorldIntegration() {
        // Verify Top N capitals globally
        CapitalCitiesWorldReport report = new CapitalCitiesWorldReport();
        ArrayList<City> topCapitals = report.getTopNCapitalCitiesInWorldByPopulation(app.getConnection(), 10);
        assertNotNull(topCapitals);
        assertFalse(topCapitals.isEmpty());
        assertTrue(topCapitals.size() <= 10);
    }

    @Test
    @Order(22)
    void testTopNCapitalsContinentIntegration() {
        // Verify Top N capitals in a continent
        CapitalCitiesContinentReport report = new CapitalCitiesContinentReport();
        ArrayList<City> topCapitals = report.getTopNCapitalCitiesInContinentByPopulation(app.getConnection(), "Asia", 10);
        assertNotNull(topCapitals);
        assertFalse(topCapitals.isEmpty());
        assertTrue(topCapitals.size() <= 10);
    }

    @Test
    @Order(23)
    void testTopNCapitalsRegionIntegration() {
        // Verify Top N capitals in a region
        CapitalCitiesRegionReport report = new CapitalCitiesRegionReport();
        ArrayList<City> topCapitals = report.getTopNCapitalCitiesInRegionByPopulation(app.getConnection(), "Middle East", 10);
        assertNotNull(topCapitals);
        assertFalse(topCapitals.isEmpty());
        assertTrue(topCapitals.size() <= 10);
    }

    // =========================
    // Population Reports Integration Tests
    // =========================
    @Test
    @Order(24)
    void testPopulationWorldReportIntegration() {
        // Verify total population report for the world
        PopulationWorldReport report = new PopulationWorldReport();
        PopulationWorldReport.PopulationData data = report.getPopulationWorldReport(app.getConnection());
        assertNotNull(data);
    }

    @Test
    @Order(25)
    void testPopulationContinentReportIntegration() {
        // Verify population report for all continents
        PopulationContinentReport report = new PopulationContinentReport();
        List<Country> list = report.getPopulationContinentReport(app.getConnection());
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    @Order(26)
    void testPopulationRegionReportIntegration() {
        // Verify population report for all regions
        PopulationRegionReport report = new PopulationRegionReport();
        List<Country> list = report.getPopulationRegionReport(app.getConnection());
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    @Order(27)
    void testPopulationCountryReportIntegration() {
        // Verify population report per country
        PopulationCountryReport report = new PopulationCountryReport();
        List<Country> list = report.getPopulationCountryReport(app.getConnection());
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    @Order(28)
    void testPopulationCityReportIntegration() {
        // Verify population report per city
        PopulationCityReport report = new PopulationCityReport();
        List<City> list = report.getPopulationCityReport(app.getConnection());
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    @Order(29)
    void testPopulationDistrictReportIntegration() {
        // Verify population report per district
        PopulationDistrictReport report = new PopulationDistrictReport();
        List<Country> list = report.getPopulationDistrictReport(app.getConnection());
        assertNotNull(list, "Population by district list should not be null");
        assertFalse(list.isEmpty(), "Population by district list should contain data");
    }


    // =========================
    // Language Reports Integration Tests
    // =========================
    @Test
    @Order(30)
    void testLanguagePopulationReportIntegration() {
        // Verify population by language report
        LanguagePopulationReport report = new LanguagePopulationReport();
        ArrayList<CountryLanguage> list = report.getLanguagePopulationReport(app.getConnection());
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }
    // =========================
    // SQL Exception / Broken Connection Tests
    // =========================
    private void breakConnection() {
        try {
            if (app.getConnection() != null && !app.getConnection().isClosed()) {
                app.getConnection().close();
            }
        } catch (SQLException ignored) {
        }
    }

    private void reconnect() {
        app.connect("localhost:33060", 3000);
    }

    // =================================================
    // Combined SQL Exception Tests
    // =================================================
    @Test
    @Order(31)
    void testAllSQLExceptionCatchBlocks() {
        // Step 1: Break connection to trigger exception paths
        breakConnection();

        // Verify all report methods handle SQLException gracefully
        assertTrue(new CountriesWorldReport().getCountriesWorldReport(app.getConnection()).isEmpty());
        assertTrue(new CountriesContinentReport().getCountriesContinentReport(app.getConnection(), "Asia").isEmpty());
        assertTrue(new CountriesRegionReport().getCountriesRegionReport(app.getConnection(), "Middle East").isEmpty());

        assertTrue(new CitiesWorldReport().getCitiesWorldReport(app.getConnection()).isEmpty());
        assertTrue(new CitiesContinentReport().getCitiesContinentReport(app.getConnection(), "Asia").isEmpty());
        assertTrue(new CitiesRegionReport().getCitiesRegionReport(app.getConnection(), "Eastern Asia").isEmpty());
        assertTrue(new CitiesCountryReport().getCitiesCountryReport(app.getConnection(), "Japan").isEmpty());
        assertTrue(new CitiesDistrictReport().getCitiesDistrictReport(app.getConnection(), "California").isEmpty());

        assertTrue(new CapitalCitiesWorldReport().getAllCapitalCitiesInWorldByPopulation(app.getConnection()).isEmpty());
        assertTrue(new CapitalCitiesContinentReport().getAllCapitalCitiesInContinentByPopulation(app.getConnection(), "Asia").isEmpty());
        assertTrue(new CapitalCitiesRegionReport().getAllCapitalCitiesInRegionByPopulation(app.getConnection(), "Middle East").isEmpty());

        assertTrue(new CountriesWorldReport().getTopNCountriesWorldReport(app.getConnection(), 10).isEmpty());
        assertTrue(new CountriesContinentReport().getTopNCountriesContinentReport(app.getConnection(), "Asia", 10).isEmpty());
        assertTrue(new CountriesRegionReport().getTopNCountriesRegionReport(app.getConnection(), "Middle East", 10).isEmpty());

        assertTrue(new CitiesWorldReport().getTopNCitiesWorldReport(app.getConnection(), 10).isEmpty());
        assertTrue(new CitiesContinentReport().getTopNCitiesContinentReport(app.getConnection(), "Asia", 10).isEmpty());
        assertTrue(new CitiesRegionReport().getTopNCitiesRegionReport(app.getConnection(), "Eastern Asia", 10).isEmpty());
        assertTrue(new CitiesCountryReport().getTopNCitiesCountryReport(app.getConnection(), "Japan", 10).isEmpty());
        assertTrue(new CitiesDistrictReport().getTopNCitiesDistrictReport(app.getConnection(), "California", 10).isEmpty());

        assertTrue(new CapitalCitiesWorldReport().getTopNCapitalCitiesInWorldByPopulation(app.getConnection(), 10).isEmpty());
        assertTrue(new CapitalCitiesContinentReport().getTopNCapitalCitiesInContinentByPopulation(app.getConnection(), "Asia", 10).isEmpty());
        assertTrue(new CapitalCitiesRegionReport().getTopNCapitalCitiesInRegionByPopulation(app.getConnection(), "Middle East", 10).isEmpty());

        assertTrue(new PopulationContinentReport().getPopulationContinentReport(app.getConnection()).isEmpty());
        assertTrue(new PopulationRegionReport().getPopulationRegionReport(app.getConnection()).isEmpty());
        assertTrue(new PopulationCountryReport().getPopulationCountryReport(app.getConnection()).isEmpty());
        assertTrue(new PopulationCityReport().getPopulationCityReport(app.getConnection()).isEmpty());
        assertTrue(new PopulationDistrictReport().getPopulationDistrictReport(app.getConnection()).isEmpty());
        assertTrue(new LanguagePopulationReport().getLanguagePopulationReport(app.getConnection()).isEmpty());
        PopulationWorldReport report = new PopulationWorldReport();
        PopulationWorldReport.PopulationData data = report.getPopulationWorldReport(app.getConnection());

        // Verify zero population returned as empty result
        assertEquals(0, data.totalPopulation, "Total population should be 0 when SQLException occurs");
        assertEquals(0, data.cityPopulation, "City population should be 0 when SQLException occurs");
        assertEquals(0, data.nonCityPopulation, "Non-city population should be 0 when SQLException occurs");

        // Step 2: Reconnect to verify successful execution
        reconnect();

        assertNotNull(new CountriesWorldReport().getCountriesWorldReport(app.getConnection()));
        assertNotNull(new CountriesContinentReport().getCountriesContinentReport(app.getConnection(), "Asia"));
        assertNotNull(new CountriesRegionReport().getCountriesRegionReport(app.getConnection(), "Middle East"));

        assertNotNull(new CitiesWorldReport().getCitiesWorldReport(app.getConnection()));
        assertNotNull(new CitiesContinentReport().getCitiesContinentReport(app.getConnection(), "Asia"));
        assertNotNull(new CitiesRegionReport().getCitiesRegionReport(app.getConnection(), "Eastern Asia"));
        assertNotNull(new CitiesCountryReport().getCitiesCountryReport(app.getConnection(), "Japan"));
        assertNotNull(new CitiesDistrictReport().getCitiesDistrictReport(app.getConnection(), "California"));

        assertNotNull(new CapitalCitiesWorldReport().getAllCapitalCitiesInWorldByPopulation(app.getConnection()));
        assertNotNull(new CapitalCitiesContinentReport().getAllCapitalCitiesInContinentByPopulation(app.getConnection(), "Asia"));
        assertNotNull(new CapitalCitiesRegionReport().getAllCapitalCitiesInRegionByPopulation(app.getConnection(), "Middle East"));

        assertNotNull(new CountriesWorldReport().getTopNCountriesWorldReport(app.getConnection(), 10));
        assertNotNull(new CountriesContinentReport().getTopNCountriesContinentReport(app.getConnection(), "Asia", 10));
        assertNotNull(new CountriesRegionReport().getTopNCountriesRegionReport(app.getConnection(), "Middle East", 10));

        assertNotNull(new CitiesWorldReport().getTopNCitiesWorldReport(app.getConnection(), 10));
        assertNotNull(new CitiesContinentReport().getTopNCitiesContinentReport(app.getConnection(), "Asia", 10));
        assertNotNull(new CitiesRegionReport().getTopNCitiesRegionReport(app.getConnection(), "Eastern Asia", 10));
        assertNotNull(new CitiesCountryReport().getTopNCitiesCountryReport(app.getConnection(), "Japan", 10));
        assertNotNull(new CitiesDistrictReport().getTopNCitiesDistrictReport(app.getConnection(), "California", 10));

        assertNotNull(new CapitalCitiesWorldReport().getTopNCapitalCitiesInWorldByPopulation(app.getConnection(), 10));
        assertNotNull(new CapitalCitiesContinentReport().getTopNCapitalCitiesInContinentByPopulation(app.getConnection(), "Asia", 10));
        assertNotNull(new CapitalCitiesRegionReport().getTopNCapitalCitiesInRegionByPopulation(app.getConnection(), "Middle East", 10));

        assertNotNull(new PopulationContinentReport().getPopulationContinentReport(app.getConnection()));
        assertNotNull(new PopulationRegionReport().getPopulationRegionReport(app.getConnection()));
        assertNotNull(new PopulationCountryReport().getPopulationCountryReport(app.getConnection()));
        assertNotNull(new PopulationCityReport().getPopulationCityReport(app.getConnection()));
        assertNotNull(new PopulationDistrictReport().getPopulationDistrictReport(app.getConnection()));
        assertNotNull(new PopulationWorldReport().getPopulationWorldReport(app.getConnection()));
        assertNotNull(new LanguagePopulationReport().getLanguagePopulationReport(app.getConnection()));
    }




    // =================================================
    // Test App Main Execution
    // =================================================
    @Test
    @Order(32)
    void testAppMainExecution() {
        // Verify App.main executes without exceptions
        reconnect();

        String dbHost = "localhost:33060";
        String dbDelay = "100";

        App.main(new String[]{dbHost, dbDelay});

    }



    // =========================
    // Empty Result Set Edge Case Tests
    // =========================
    @Test
    @Order(33)
    void testBuildCapitalCities_EmptyResultSet() {
        // Verify capital cities report handles non-existent continent
        CapitalCitiesContinentReport report = new CapitalCitiesContinentReport();

        String nonexistentContinent = "Atlantis";

        ArrayList<City> capitals = report.getAllCapitalCitiesInContinentByPopulation(
                AppIntegrationTest.app.getConnection(),
                nonexistentContinent
        );

        assertNotNull(capitals, "List should not be null");
        assertTrue(capitals.isEmpty(), "List must be empty when querying a non-existent continent");

    }


    @Test
    @Order(34) // Use an appropriate order number
    void testBuildCountries_EmptyResultSet() {
        // Verify country report handles non-existent region
        CountriesRegionReport report = new CountriesRegionReport();

        String nonexistentRegion = "Fictional Region";

        ArrayList<Country> countries = report.getCountriesRegionReport(
                AppIntegrationTest.app.getConnection(),
                nonexistentRegion
        );

        // Assert: Verify the list is empty
        assertNotNull(countries, "List should not be null");
        assertTrue(countries.isEmpty(), "List must be empty when querying a non-existent region");
    }


    @Test
    @Order(35)
    void testBuildCities_EmptyResultSet() {
        // Verify city report handles non-existent district
        CitiesDistrictReport report = new CitiesDistrictReport();

        String nonexistentDistrict = "Fictional District X";

        ArrayList<City> cities = report.getCitiesDistrictReport(
                AppIntegrationTest.app.getConnection(),
                nonexistentDistrict
        );

        assertNotNull(cities, "List should not be null");
        assertTrue(cities.isEmpty(), "List must be empty when querying a non-existent district");

    }

    // =========================
    // Tear Down
    // =========================
    @AfterAll
    static void tearDown() {
        // Disconnect from database
        app.disconnect();
        try {
            assertTrue(app.getConnection() == null || app.getConnection().isClosed());
        } catch (Exception ignored) {
        }
    }
}

