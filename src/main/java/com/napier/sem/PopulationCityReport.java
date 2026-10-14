package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles generating and retrieving population reports for all cities.
 */
public class PopulationCityReport {

    // Logger instance for this class
    private static final Logger LOGGER = Logger.getLogger(PopulationCityReport.class.getName());

    /**
     * Retrieves a list of cities with their populations,
     * ordered from the largest to smallest population.
     *
     * @param conn Active database connection
     * @return List of City objects containing city name, district, country code, and population
     */
    public List<City> getPopulationCityReport(Connection conn) {

        List<City> cities = new ArrayList<>();

        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate city population report.");
            return cities;
        }

        // SQL query to retrieve city populations ordered by population descending
        String sql = """
                SELECT ID, Name, CountryCode, District, Population
                FROM city
                ORDER BY Population DESC;
                """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                City city = new City();
                city.setId(rs.getInt("ID"));
                city.setCityName(rs.getString("Name"));
                city.setCountryCode(rs.getString("CountryCode"));
                city.setDistrict(rs.getString("District"));
                city.setPopulation(rs.getInt("Population"));
                cities.add(city);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving population report by city.", e);
        }

        if (cities.isEmpty()) {
            LOGGER.warning("No population data found for cities. Report will be empty.");
        }

        return cities;
    }

    /**
     * Prints the Population by City Report to the console.
     *
     * @param cities List of City objects
     */
    protected void printPopulationCityReport(List<City> cities) {
        System.out.println("\n==================== Report ID 31. Population by City Report ====================");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%-30s %-20s %-15s %-20s%n", "City Name", "District", "Country Code", "Population");
        System.out.println("----------------------------------------------------------------------------------");

        if (cities == null || cities.isEmpty()) {
            System.out.println("No city population data available.");
            return;
        }

        for (City city : cities) {
            System.out.printf("%-30s %-20s %-15s %,15d%n",
                    city.getCityName(),
                    city.getDistrict(),
                    city.getCountryCode(),
                    city.getPopulation());
        }

        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("===================================================================================\n");
    }

    /**
     * Prints the Population by City Report for a specific city.
     *
     * @param cities List of City objects
     * @param selectedCity Name of the city to print
     */
    protected void printPopulationCityReport(List<City> cities, String selectedCity) {
        List<City> filteredCities = cities.stream()
                .filter(c -> c.getCityName().equalsIgnoreCase(selectedCity))
                .toList(); // Use Collectors.toList() if Java version < 16

        printPopulationCityReport(filteredCities);
    }
}
