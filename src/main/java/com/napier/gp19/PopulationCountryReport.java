package com.napier.gp19;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PopulationCountryReport generates population reports grouped by country.
 * It calculates total, city, and non-city populations for each country
 * and provides methods to print the results in a formatted table.
 */
public class PopulationCountryReport {

    private static final Logger LOGGER = Logger.getLogger(PopulationCountryReport.class.getName());

    /**
     * Get population report grouped by country.
     * @param conn Database connection
     * @return List of Country objects containing population data
     */
    public List<Country> getPopulationCountryReport(Connection conn) {
        return fetchCountryPopulation(conn);
    }

    /**
     * Get detailed population report for each country (city vs non-city population).
     * @param conn Database connection
     * @return List of Country objects with population details
     */
    public List<Country> getPopulationCityVsNonCityByCountry(Connection conn) {
        return fetchCountryPopulation(conn);
    }

    /**
     * Helper method to query the database for population data by country.
     * Calculates total, city, and non-city populations.
     * @param conn Database connection
     * @return List of Country objects with population info
     */
    private List<Country> fetchCountryPopulation(Connection conn) {
        List<Country> results = new ArrayList<>();
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate country population report.");
            return results;
        }

        String sql = """
                SELECT
                    co.Name AS Country,
                    co.Population AS TotalPopulation,
                    IFNULL(SUM(ci.Population),0) AS CityPopulation,
                    (co.Population - IFNULL(SUM(ci.Population),0)) AS NonCityPopulation
                FROM country co
                LEFT JOIN city ci ON co.Code = ci.CountryCode
                GROUP BY co.Name, co.Population
                ORDER BY TotalPopulation DESC;
                """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Country c = new Country();
                c.setCountryName(rs.getString("Country"));
                c.setPopulation(rs.getLong("TotalPopulation"));
                c.setGnp(rs.getDouble("CityPopulation"));
                c.setGnpOld(rs.getDouble("NonCityPopulation"));
                results.add(c);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving population report by country.", e);
        }

        if (results.isEmpty()) {
            LOGGER.warning("No population data found for country report.");
        }

        return results;
    }

    /**
     * Print population report for a specific country.
     * @param results List of Country objects
     * @param selectedCountry Country to display
     */
    public void printPopulationCountryReport(List<Country> results, String selectedCountry) {
        String groupByColumn = "Country";
        printPopulation(results, "ReportID 29. Population by Country Report",groupByColumn,selectedCountry);
    }

    /**
     * Print detailed population report for all countries (city vs non-city).
     * @param results List of Country objects
     */
    public void printPopulationCityVsNonCityByCountry(List<Country> results) {
        printPopulation(results, "ReportID 25. Population by Country (City vs Non-City)", "Country", null);
    }

    /**
     * Helper method to print population data in a formatted table.
     * @param results List of Country objects
     * @param title Report title
     * @param groupByColumn Column name used for grouping (Country)
     * @param selectedValue Optional filter to display a specific country
     */
    private void printPopulation(List<Country> results, String title, String groupByColumn, String selectedValue) {
        System.out.println("\n==================== " + title + " ====================");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-40s %20s %20s %20s %10s %10s%n",
                groupByColumn, "Total Pop", "City Pop", "Non-City Pop", "City %", "Non-City %");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");

        if (results == null || results.isEmpty()) {
            System.out.println("No data available to display.");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("==============================================================================================================================\n");
            return;
        }

        // Print each country's population data
        for (Country c : results) {
            String name = c.getCountryName();
            if (selectedValue != null && !name.equalsIgnoreCase(selectedValue)) continue;
            PopulationUtils.PopValues v = PopulationUtils.calculatePopulationValues(c);
            System.out.printf("%-40s %,20d %,20d %,20d %9.2f%% %9.2f%%%n",
                    name, v.total(), v.city(), v.nonCity(), v.cityPercent(), v.nonCityPercent());
        }

        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("==============================================================================================================================\n");
    }
}
