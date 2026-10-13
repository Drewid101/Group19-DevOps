package com.napier.gp19;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles generating and retrieving the total population of the world,
 * including breakdowns for city and non-city populations.
 */
public class PopulationWorldReport {

    // Logger instance for this class
    private static final Logger LOGGER = Logger.getLogger(PopulationWorldReport.class.getName());

    /**
     * Retrieves the total world population and breakdown (city vs non-city) from the database.
     *
     * @param conn Active database connection
     * @return PopulationData containing total, city, and non-city data
     */

    public PopulationData getPopulationWorldReport(Connection conn) {

        PopulationData data = new PopulationData("World");

        // 1. Check for null connection
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot retrieve world population report.");
            return data;
        }

        // SQL queries
        String totalSql = "SELECT SUM(Population) AS TotalPopulation FROM country;";
        String citySql = "SELECT SUM(city.Population) AS CityPopulation FROM city;";

        try (Statement stmtTotal = conn.createStatement();
             Statement stmtCity = conn.createStatement();
             ResultSet rsTotal = stmtTotal.executeQuery(totalSql);
             ResultSet rsCity = stmtCity.executeQuery(citySql)) {

            if (rsTotal.next()) {
                data.totalPopulation = rsTotal.getLong("TotalPopulation");
            }
            if (rsCity.next()) {
                data.cityPopulation = rsCity.getLong("CityPopulation");
            }

            data.nonCityPopulation = data.totalPopulation - data.cityPopulation;

            if (data.totalPopulation > 0) {
                data.cityPercentage = (data.cityPopulation * 100.0) / data.totalPopulation;
                data.nonCityPercentage = (data.nonCityPopulation * 100.0) / data.totalPopulation;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving world population report.", e);
        }

        return data;
    }

    /**
     * Prints the world population report in formatted layout.
     *
     * @param data PopulationData object containing all population info
     */
    protected void printPopulationWorldReport(PopulationData data) {
        System.out.println("\n==================== ReportID 26. Population of the World Report ====================");
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-25s %-20s %-20s %-20s %-10s %-12s%n",
                "Name", "Total Population", "City Population", "Non-City Population", "City %", "Non-City %");
        System.out.println("---------------------------------------------------------------------------------------------------------------");

        if (data == null) {
            System.out.println("No world population data available.");
            return;
        }

        System.out.printf("%-25s %,20d %,20d %,20d %9.2f%% %12.2f%%%n",
                data.name,
                data.totalPopulation,
                data.cityPopulation,
                data.nonCityPopulation,
                data.cityPercentage,
                data.nonCityPercentage);

        System.out.println("---------------------------------------------------------------------------------------------------------------");
        System.out.println("===============================================================================================================\n");
    }

    /**
     * Inner class to hold population data.
     */
    public static class PopulationData {
        String name;
        long totalPopulation;
        long cityPopulation;
        long nonCityPopulation;
        double cityPercentage;
        double nonCityPercentage;

        public PopulationData(String name) {
            this.name = name;
        }
    }
}
