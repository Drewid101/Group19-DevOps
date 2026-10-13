package com.napier.gp19;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles generating and retrieving population reports by continent.
 */
public class PopulationContinentReport {

    private static final Logger LOGGER = Logger.getLogger(PopulationContinentReport.class.getName());



    // ---------------------------------------------------------------------
    // Private helper to fetch continent population data
    // ---------------------------------------------------------------------
    private List<Country> fetchContinentPopulationReport(Connection conn) {
        List<Country> results = new ArrayList<>();

        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate continent population report.");
            return results;
        }

        String sql = """
                    SELECT
                        c.Continent,
                        SUM(c.Population) AS TotalPopulation,
                        COALESCE(SUM(ci.CityPop), 0) AS CityPopulation,
                        SUM(c.Population) - COALESCE(SUM(ci.CityPop), 0) AS NonCityPopulation
                    FROM country c
                    LEFT JOIN (
                        SELECT CountryCode, SUM(Population) AS CityPop
                        FROM city
                        GROUP BY CountryCode
                    ) ci ON ci.CountryCode = c.Code
                    GROUP BY c.Continent
                    ORDER BY TotalPopulation DESC;
                """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Country c = new Country();
                c.setContinent(rs.getString("Continent"));
                c.setPopulation(rs.getLong("TotalPopulation"));
                c.setGnp((double) rs.getLong("CityPopulation"));     // reuse fields
                c.setGnpOld((double) rs.getLong("NonCityPopulation"));
                results.add(c);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving continent population report.", e);
        }

        if (results.isEmpty()) {
            LOGGER.warning("No population data found for continent report.");
        }

        return results;
    }

    // ---------------------------------------------------------------------
    // Public methods to fetch reports (original names)
    // ---------------------------------------------------------------------
    public List<Country> getPopulationContinentReport(Connection conn) {
        return fetchContinentPopulationReport(conn);
    }

    public List<Country> getPopulationCityVsNonCityByContinent(Connection conn) {
        return fetchContinentPopulationReport(conn);
    }

    // ---------------------------------------------------------------------
    // Private helper to print continent population
    // ---------------------------------------------------------------------
    private void printContinentPopulation(List<Country> results, String title) {
        System.out.println("\n==================== " + title + " ====================");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.printf("%-20s %20s %20s %20s %10s %10s%n",
                "Continent", "Total Pop", "City Pop", "Non-City Pop", "City %", "Non-City %");
        System.out.println("--------------------------------------------------------------------------------------------------------");

        // ✅ Prevent NullPointerException
        if (results == null || results.isEmpty()) {
            System.out.println("No data available to display.");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            System.out.println("========================================================================================================\n");
            return;
        }

        for (Country c : results) {
            PopulationUtils.PopValues v = PopulationUtils.calculatePopulationValues(c);
            System.out.printf("%-20s %,20d %,20d %,20d %9.2f%% %9.2f%%%n",
                    c.getContinent(), v.total(), v.city(), v.nonCity(), v.cityPercent(), v.nonCityPercent());
        }

        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("========================================================================================================\n");
    }


    // ---------------------------------------------------------------------
    // Public methods to print reports (original names)
    // ---------------------------------------------------------------------
    public void printPopulationContinentReport(List<Country> results) {
        printContinentPopulation(results, "ReportID 27. Population by Continent Report");
    }

    public void printPopulationCityVsNonCityByContinent(List<Country> results) {
        printContinentPopulation(results, "ReportID 23. Population in Cities vs Not in Cities by Continent");
    }
}
