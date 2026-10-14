package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles generating and retrieving population reports by district.
 */
public class PopulationDistrictReport {

    private static final Logger LOGGER = Logger.getLogger(PopulationDistrictReport.class.getName());

    /**
     * Retrieves total population grouped by district, ordered from largest to smallest.
     *
     * @param conn Active database connection
     * @return List of Country objects containing district and population data
     */
    public List<Country> getPopulationDistrictReport(Connection conn) {
        List<Country> countries = new ArrayList<>();

        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate population report by district.");
            return countries;
        }

        // SQL query: total population per district and city/non-city population
        String sql = """
                SELECT
                    District,
                    SUM(Population) AS TotalPopulation,
                    SUM(Population) AS CityPopulation,   -- all population in cities (for simplicity)
                    0 AS NonCityPopulation               -- no separate non-city data for districts
                FROM city
                GROUP BY District
                ORDER BY TotalPopulation DESC;
                """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Country country = new Country();
                country.setCountryName(rs.getString("District"));
                country.setPopulation(rs.getLong("TotalPopulation"));

                // reuse gnp for city population, gnpOld for non-city population
                country.setGnp(rs.getDouble("CityPopulation"));
                country.setGnpOld(rs.getDouble("NonCityPopulation"));

                countries.add(country);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving population report by district.", e);
        }

        if (countries.isEmpty()) {
            LOGGER.warning("No population data found by district. Report will be empty.");
        }

        return countries;
    }

    /**
     * Prints the Population by District Report including percentages.
     *
     * @param countries List of Country objects
     */
    protected void printPopulationDistrictReport(List<Country> countries, String selectedDistrict) {
        System.out.println("\n==================== ReportID 30. Population by District Report ====================");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-30s %20s %20s %20s %10s %10s%n",
                "District", "Total Population", "City Population", "Non-City Population", "City %", "Non-City %");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");

        if (countries == null || countries.isEmpty()) {
            System.out.println("No district population data available.");
            return;
        }

        countries.stream()
                .filter(c -> c.getCountryName().equalsIgnoreCase(selectedDistrict))
                .forEach(c -> {
                    // Use PopulationUtils helper
                    PopulationUtils.PopValues v = PopulationUtils.calculatePopulationValues(c);

                    System.out.printf("%-30s %,20d %,20d %,20d %9.2f%% %9.2f%%%n",
                            c.getCountryName(),
                            v.total(),
                            v.city(),
                            v.nonCity(),
                            v.cityPercent(),
                            v.nonCityPercent());
                });

        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
        System.out.println("==========================================================================================================================\n");
    }
}
