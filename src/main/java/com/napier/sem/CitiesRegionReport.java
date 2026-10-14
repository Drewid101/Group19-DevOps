package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles region-based city reports such as:
 * - Listing all cities within a specific region
 * - Listing the top N most populated cities in a region
 * This class extends {@link CitiesReportBase}, allowing it to reuse methods
 * for building {@link City} objects from a ResultSet and printing reports.
 */
public class CitiesRegionReport extends CitiesReportBase {

    /**
     * Logger instance for the CitiesRegionReport class.
     * Declared as:
     * - private: accessible only within this class
     * - static: shared across all instances
     * - final: cannot be reassigned
     * The logger name is the fully qualified class name to easily identify log messages.
     */
    private static final Logger LOGGER = Logger.getLogger(CitiesRegionReport.class.getName());
    /**
     * Retrieves all cities located within a specified region, ordered by population
     * from highest to lowest.
     * Expected ResultSet columns:
     * CityName, CountryName, District, Population.
     *
     * @param conn   Active database connection
     * @param region The region name to filter cities by (e.g., "Southern Europe", "Eastern Asia")
     * @return A list of {@link City} objects representing all cities in the given region.
     *         Returns an empty list if an error occurs or no data is found.
     */
    public ArrayList<City> getCitiesRegionReport(Connection conn, String region) {
        // Initialize an empty list to hold city data
        ArrayList<City> cities = new ArrayList<>();

        // Check for null connection or invalid input
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate city report for region: " + region);
            return cities;
        }

        // SQL query: selects cities and their related country names
        // filtered by region, sorted by population in descending order.
        String query = """
                SELECT city.Name AS CityName, country.Name AS CountryName,
                       city.District, city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Region = ?
                ORDER BY city.Population DESC;
                """;

        // Use try-with-resources to automatically close PreparedStatement and ResultSet
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Bind the region parameter to the SQL query
            pstmt.setString(1, region);

            // Execute the query and retrieve results
            try (ResultSet rs = pstmt.executeQuery()) {
                // Convert ResultSet rows into City objects using the base class helper
                cities = buildCitiesFromResultSet(rs);
            }

        } catch (SQLException e) {
            // Print detailed error message if SQL execution fails
            LOGGER.log(Level.SEVERE, "Error retrieving all cities in region: " + region, e);
        }

        // Return the populated list (or empty list if query failed or had no data)
        return cities;
    }

    /**
     * Retrieves the top N most populated cities within a specific region.
     * This method filters by region and limits the number of cities returned.
     *
     * @param conn   Active database connection
     * @param region The region name to filter cities by
     * @param n      The number of top cities to return (e.g., 10 for top 10)
     * @return A list of {@link City} objects representing the top N populated cities.
     *         Returns an empty list if an error occurs or no data is found.
     */
    public ArrayList<City> getTopNCitiesRegionReport(Connection conn, String region, int n) {
        // Initialize an empty list for city results
        ArrayList<City> cities = new ArrayList<>();

        // Check for null connection or invalid input
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate top " + n + " city report for region: " + region);
            return cities;
        }

        // SQL query: selects cities from the specified region,
        // ordered by population descending, limited to top N results.
        String query = """
                SELECT city.Name AS CityName, country.Name AS CountryName,
                       city.District, city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Region = ?
                ORDER BY city.Population DESC
                LIMIT ?;
                """;

        // Try-with-resources ensures proper closing of SQL resources
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set the region name and top N limit parameters
            pstmt.setString(1, region);
            pstmt.setInt(2, n);

            // Execute the SQL query
            try (ResultSet rs = pstmt.executeQuery()) {
                // Map the query results into City objects
                cities = buildCitiesFromResultSet(rs);
            }

        } catch (SQLException e) {
            // Log an informative error message for debugging
            LOGGER.log(Level.SEVERE, "Error retrieving top " + n + " cities in region: " + region, e);
        }

        // Return the resulting list of cities (or empty list on error)
        return cities;
    }

    /**
     * Prints a formatted report listing all cities in the specified region.
     * Uses the inherited printCities() method from {@link CitiesReportBase}.
     *
     * @param cities The list of cities to display in the report.
     * @param region The name of the region displayed in the report title.
     */
    public void printCitiesRegionReport(ArrayList<City> cities, String region) {
        printCities(cities, "ReportID 9. All Cities in a Region: " + region + " by Population Report");
    }

    /**
     * Prints a formatted report listing the top N cities in the specified region.
     * Uses the inherited printCities() method from {@link CitiesReportBase}.
     *
     * @param cities The list of top N cities to display.
     * @param region The name of the region displayed in the report title.
     * @param n      The number of top cities being displayed.
     */
    public void printTopNCitiesRegionReport(ArrayList<City> cities, String region, int n) {
        printCities(cities, "ReportID 14. Top " + n + " Cities in a Region: " + region + " by Population Report");
    }
}
