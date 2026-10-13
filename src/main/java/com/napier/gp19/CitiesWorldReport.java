package com.napier.gp19;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles world city reports (all cities, top N cities) using CitiesReportBase.
 * This class extends {@link CitiesReportBase} to reuse methods for building and printing city data.
 */
public class CitiesWorldReport extends CitiesReportBase {

    /**
     * Logger instance for CitiesWorldReport class.
     */
    private static final Logger LOGGER = Logger.getLogger(CitiesWorldReport.class.getName());
    /**
     * Retrieves all cities in the world, ordered by population (from highest to lowest).
     *
     * @param conn A valid database connection.
     * @return A list of {@link City} objects sorted by population descending.
     *         Returns an empty list if an error occurs or no data is found.
     */
    public ArrayList<City> getCitiesWorldReport(Connection conn) {
        // Initialize an empty list to store city data
        ArrayList<City> cities = new ArrayList<>();

        // Check for null connection or invalid input
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate world city report.");
            return cities;
        }

        // SQL query to retrieve all cities joined with their corresponding countries
        // Ordered by population in descending order
        String query = """
                SELECT city.Name AS CityName, country.Name AS CountryName,
                       city.District, city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                ORDER BY city.Population DESC;
                """;

        // Use a try-with-resources block to ensure PreparedStatement and ResultSet are closed automatically
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Convert the ResultSet into a list of City objects using a method from the base class
            cities = buildCitiesFromResultSet(rs);

        } catch (SQLException e) {
            // Handle any SQL-related errors
            LOGGER.log(Level.SEVERE, "Error retrieving all cities in the world.", e);
        }

        // Return the list of cities (possibly empty)
        return cities;
    }

    /**
     * Retrieves the top N most populated cities in the world.
     *
     * @param conn A valid database connection.
     * @param topN The number of top cities to retrieve (e.g., 10 for the top 10).
     * @return A list of {@link City} objects containing the top N cities.
     *         Returns an empty list if an error occurs or no data is found.
     */
    public ArrayList<City> getTopNCitiesWorldReport(Connection conn, int topN) {
        // Initialize an empty list to store city data
        ArrayList<City> cities = new ArrayList<>();

        // Check for null connection or invalid input
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate top " + topN + " world city report.");
            return cities;
        }

        // SQL query to retrieve only the top N cities by population
        String query = """
                SELECT city.Name AS CityName, country.Name AS CountryName,
                       city.District, city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                ORDER BY city.Population DESC
                LIMIT ?;
                """;

        // Use a try-with-resources block to ensure all database resources are properly closed
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the parameter for the LIMIT clause dynamically
            pstmt.setInt(1, topN);

            // Execute the query and process the result
            try (ResultSet rs = pstmt.executeQuery()) {
                // Convert the ResultSet into a list of City objects
                cities = buildCitiesFromResultSet(rs);
            }

        } catch (SQLException e) {
            // Handle SQL errors and display which report failed
            LOGGER.log(Level.SEVERE, "Error retrieving top " + topN + " cities in the world.", e);
        }

        // Return the resulting city list (possibly empty)
        return cities;
    }

    /**
     * Prints a formatted report showing all cities in the world.
     * Utilizes the printCities() method inherited from {@link CitiesReportBase}.
     *
     * @param cities The list of cities to display.
     */
    public void printCitiesWorldReport(ArrayList<City> cities) {
        printCities(cities, "ReportID 7. All Cities in the World by Population Report");
    }

    /**
     * Prints a formatted report showing the top N cities in the world.
     * Utilizes the printCities() method inherited from {@link CitiesReportBase}.
     *
     * @param cities The list of top N cities to display.
     * @param topN The number of top cities included in the report.
     */
    public void printTopNCitiesWorldReport(ArrayList<City> cities, int topN) {
        printCities(cities, "ReportID 12. Top " + topN + " Cities in the World by Population Report");
    }
}
