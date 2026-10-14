package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles continent-based city reports such as:
 * - All cities in a given continent
 * - Top N most populated cities in a given continent
 * This class extends {@link CitiesReportBase} to reuse methods for building
 * city lists from a ResultSet and for printing formatted reports.
 */
public class CitiesContinentReport extends CitiesReportBase {

    /**
     * Logger instance for the CitiesContinentReport class.
     * Declared as:
     * - private: only accessible within this class (encapsulation)
     * - static: shared across all instances of this class
     * - final: cannot be reassigned
     * The logger name is the fully qualified class name to make it easier
     * to identify the source of log messages.
     */
    private static final Logger LOGGER = Logger.getLogger(CitiesContinentReport.class.getName());
    /**
     * Retrieves all cities within a specified continent, ordered by population (descending).
     * This method queries the database to return cities belonging to countries
     * located in the given continent.
     * Expected ResultSet columns: CityName, CountryName, District, Population.
     *
     * @param conn       Active database connection
     * @param continent  The name of the continent (e.g., "Asia", "Europe")
     * @return A list of {@link City} objects for the specified continent.
     *         Returns an empty list if an error occurs or no results are found.
     */
    public ArrayList<City> getCitiesContinentReport(Connection conn, String continent) {
        // Initialize a list to hold City objects
        ArrayList<City> cities = new ArrayList<>();

        // Check for null connection or invalid input
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate city report for continent: " + continent);
            return cities;
        }

        // SQL query: Select all cities that belong to countries in the specified continent,
        // joining the city and country tables to include the country name.
        // Results are sorted in descending order by population.
        String query = """
                SELECT city.Name AS CityName, country.Name AS CountryName,
                       city.District, city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Continent = ?
                ORDER BY city.Population DESC;
                """;

        // Use try-with-resources to ensure PreparedStatement and ResultSet are automatically closed
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set the parameter for the continent filter in the SQL query
            pstmt.setString(1, continent);

            // Execute the query and retrieve results
            try (ResultSet rs = pstmt.executeQuery()) {
                // Build a list of City objects from the query results using the base class helper
                cities = buildCitiesFromResultSet(rs);
            }

        } catch (SQLException e) {
            // Handle database errors and print details for debugging
            LOGGER.log(Level.SEVERE, "Error retrieving all cities in continent: " + continent, e);
        }

        // Return the populated list (or an empty one if an error occurred)
        return cities;
    }

    /**
     * Retrieves the top N most populated cities within a specified continent.
     * This method filters results by continent and limits the number of records returned.
     *
     * @param conn       Active database connection
     * @param continent  The name of the continent (e.g., "Africa", "Europe")
     * @param topN       The number of top cities to return (e.g., 10 for top 10)
     * @return A list of {@link City} objects for the specified continent.
     *         Returns an empty list if an error occurs or no results are found.
     */
    public ArrayList<City> getTopNCitiesContinentReport(Connection conn, String continent, int topN) {
        // Initialize a list to hold City objects
        ArrayList<City> cities = new ArrayList<>();

        // Check for null connection or invalid input
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate city report.");
            return cities;
        }

        // SQL query: Select cities within the specified continent, ordered by population,
        // but only return the top N results based on the LIMIT parameter.
        String query = """
                SELECT city.Name AS CityName, country.Name AS CountryName,
                       city.District, city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Continent = ?
                ORDER BY city.Population DESC
                LIMIT ?;
                """;

        // Use try-with-resources for safe database resource management
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set query parameters: continent name and top N limit
            pstmt.setString(1, continent);
            pstmt.setInt(2, topN);

            // Execute the query and process the results
            try (ResultSet rs = pstmt.executeQuery()) {
                // Build City objects from the ResultSet using a method from the base class
                cities = buildCitiesFromResultSet(rs);
            }

        } catch (SQLException e) {
            // Handle database errors and output detailed information
            LOGGER.log(Level.SEVERE, "Error retrieving all cities in continent: " + continent, e);
        }

        // Return the list of top N cities (or empty if no results)
        return cities;
    }

    /**
     * Prints a formatted report showing all cities within a given continent.
     * Utilizes the printCities() method inherited from {@link CitiesReportBase}.
     *
     * @param cities     The list of cities to display.
     * @param continent  The continent name to include in the report title.
     */
    public void printCitiesContinentReport(ArrayList<City> cities, String continent) {
        printCities(cities, "ReportID 8. All Cities in a Continent: " + continent + " by Population Report");
    }

    /**
     * Prints a formatted report showing the top N most populated cities within a given continent.
     * Utilizes the printCities() method inherited from {@link CitiesReportBase}.
     *
     * @param cities     The list of top N cities to display.
     * @param continent  The continent name to include in the report title.
     * @param topN       The number of cities displayed in the report.
     */
    public void printTopNCitiesContinentReport(ArrayList<City> cities, String continent, int topN) {
        printCities(cities, "Report ID 13. Top " + topN + " Cities in a Continent: " + continent + " by Population Report");
    }
}
