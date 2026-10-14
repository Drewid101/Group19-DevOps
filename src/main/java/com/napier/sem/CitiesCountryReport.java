package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles country-based city reports.
 * This class provides methods to generate reports of all cities or
 * the top N populated cities within a specific country.
 * It extends {@link CitiesReportBase}, which provides shared functionality
 * for formatting and printing city report results.
 */
public class CitiesCountryReport extends CitiesReportBase {
    /**
     * Logger instance for the CitiesCountryReport class.
     * <p>
     * Declared as:
     * - private: encapsulated within the class
     * - static: shared across all instances of this class
     * - final: cannot be reassigned
     * <p>
     * The logger name is the fully qualified class name, which helps
     * identify the source of logged messages.
     */
    private static final Logger LOGGER = Logger.getLogger(CitiesCountryReport.class.getName());
    /**
     * Retrieves all cities in a given country, ordered by population in descending order.
     *
     * @param conn         The active SQL database connection.
     * @param countryName  The name of the country for which the report will be generated.
     * @return An {@link ArrayList} of {@link City} objects representing all cities in the country.
     */
    public ArrayList<City> getCitiesCountryReport(Connection conn, String countryName) {
        // Initialize an empty list to store city objects
        ArrayList<City> cities = new ArrayList<>();

        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate city report for country: " + countryName);
            return cities;
        }

        // SQL query to retrieve all cities for a specific country
        // The query joins the 'city' and 'country' tables to access the country name
        String query = """
                SELECT city.Name AS CityName, country.Name AS CountryName,
                       city.District, city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Name = ?
                ORDER BY city.Population DESC;
                """;

        // Try-with-resources ensures PreparedStatement and ResultSet are automatically closed
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set the country name parameter in the query
            pstmt.setString(1, countryName);

            // Execute query and process the result set
            try (ResultSet rs = pstmt.executeQuery()) {
                // Convert result set rows into City objects
                cities = buildCitiesFromResultSet(rs);
            }
        } catch (SQLException e) {
            // Handle database-related errors gracefully
            LOGGER.log(Level.SEVERE, "Error retrieving all cities in country: " + countryName, e);
        }

        // Return the list of all cities found
        return cities;
    }

    /**
     * Retrieves the top N most populated cities in a specific country.
     *
     * @param conn         The active SQL database connection.
     * @param countryName  The name of the country to retrieve data from.
     * @param n            The number of top populated cities to include in the report.
     * @return An {@link ArrayList} of {@link City} objects representing the top N cities.
     */
    public ArrayList<City> getTopNCitiesCountryReport(Connection conn, String countryName, int n) {
        // Initialize an empty list for storing City objects
        ArrayList<City> cities = new ArrayList<>();

        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate city report for country: " + countryName);
            return cities;
        }

        // SQL query similar to the one above but limits results to top N
        String query = """
                SELECT city.Name AS CityName, country.Name AS CountryName,
                       city.District, city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Name = ?
                ORDER BY city.Population DESC
                LIMIT ?;
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set both query parameters: country name and limit number
            pstmt.setString(1, countryName);
            pstmt.setInt(2, n);

            // Execute query and process the result set
            try (ResultSet rs = pstmt.executeQuery()) {
                // Build a list of City objects from query results
                cities = buildCitiesFromResultSet(rs);
            }
        } catch (SQLException e) {
            // Log SQL exceptions with specific context for debugging
            LOGGER.log(Level.SEVERE, "Error retrieving top " + n + " cities in country: " + countryName, e);
        }

        // Return the list of top N cities
        return cities;
    }

    /**
     * Prints a formatted report showing all cities in a specified country.
     *
     * @param cities       The list of {@link City} objects to display.
     * @param countryName  The name of the country being reported.
     */
    public void printCitiesCountryReport(ArrayList<City> cities, String countryName) {
        // Use the base class method to print cities with a descriptive title
        printCities(cities, "ReportID 10. All Cities in a Country: " + countryName + " by Population Report");
    }

    /**
     * Prints a formatted report showing the top N populated cities in a country.
     *
     * @param cities       The list of {@link City} objects to display.
     * @param countryName  The name of the country being reported.
     * @param n            The number of top cities shown in the report.
     */
    public void printTopNCitiesCountryReport(ArrayList<City> cities, String countryName, int n) {
        // Use the base class method to print cities with a descriptive title
        printCities(cities, "ReportID 15. Top " + n + " Cities in a Country: " + countryName + " by Population Report");
    }
}
