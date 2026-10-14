package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles generation of city reports filtered by district.
 * Extends the CitiesReportBase class which provides common reporting functionality.
 */
public class CitiesDistrictReport extends CitiesReportBase {

    /**
     * Logger instance for the CitiesDistrictReport class.
     * <p>
     * Declared as:
     * - private: only accessible within this class
     * - static: shared across all instances of this class
     * - final: cannot be reassigned
     * <p>
     * The logger name is the fully qualified class name to identify the source
     * of log messages easily.
     */
    private static final Logger LOGGER = Logger.getLogger(CitiesDistrictReport.class.getName());
    /**
     * Retrieves all cities in a specific district, ordered by population (descending).
     *
     * @param conn     Active SQL database connection.
     * @param district The district name to filter cities by.
     * @return An ArrayList of City objects representing all cities in the given district.
     */
    public ArrayList<City> getCitiesDistrictReport(Connection conn, String district) {
        ArrayList<City> cities = new ArrayList<>();

        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate city report for district: " + district);
            return cities;
        }

        // SQL query to select all city details joined with their respective country,
        // filtered by district, and ordered by population in descending order.
        String query = """
                SELECT city.Name AS CityName, country.Name AS CountryName,
                       city.District, city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE city.District = ?
                ORDER BY city.Population DESC;
                """;

        // Use try-with-resources to automatically close PreparedStatement and ResultSet
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set the district parameter in the SQL query
            pstmt.setString(1, district);

            // Execute the query and process the results
            try (ResultSet rs = pstmt.executeQuery()) {
                // Convert the ResultSet into a list of City objects
                cities = buildCitiesFromResultSet(rs);
            }
        } catch (SQLException e) {
            // Handle SQL exceptions gracefully
            LOGGER.log(Level.SEVERE, "Error retrieving all cities in district: " + district, e);
        }

        // Return the list of retrieved cities
        return cities;
    }

    /**
     * Retrieves the top N populated cities in a specific district.
     *
     * @param conn     Active SQL database connection.
     * @param district The district name to filter cities by.
     * @param n        The number of top cities to retrieve.
     * @return An ArrayList of City objects representing the top N populated cities.
     */
    public ArrayList<City> getTopNCitiesDistrictReport(Connection conn, String district, int n) {
        ArrayList<City> cities = new ArrayList<>();

        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate top " + n + " city report for district: " + district);
            return cities;
        }

        // SQL query similar to the previous one, but limits the number of rows returned
        String query = """
                SELECT city.Name AS CityName, country.Name AS CountryName,
                       city.District, city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE city.District = ?
                ORDER BY city.Population DESC
                LIMIT ?;
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set district and limit parameters
            pstmt.setString(1, district);
            pstmt.setInt(2, n);

            // Execute query and map results to City objects
            try (ResultSet rs = pstmt.executeQuery()) {
                cities = buildCitiesFromResultSet(rs);
            }
        } catch (SQLException e) {
            // Handle SQL exceptions with detailed error message
            LOGGER.log(Level.SEVERE, "Error retrieving top " + n + " cities in district: " + district, e);
        }

        // Return the top N cities
        return cities;
    }


    /**
     * Prints a report showing all cities in the given district.
     *
     * @param cities   List of City objects to display.
     * @param district The district name used in the report title.
     */
    public void printCitiesDistrictReport(ArrayList<City> cities, String district) {
        // Calls the shared printCities() method from CitiesReportBase with a descriptive title
        printCities(cities, "Report 11. All Cities in a District: " + district + " by Population Report");
    }

    /**
     * Prints a report showing the top N populated cities in the given district.
     *
     * @param cities   List of City objects to display.
     * @param district The district name used in the report title.
     * @param n        Number of top cities shown in the report.
     */
    public void printTopNCitiesDistrictReport(ArrayList<City> cities, String district, int n) {
        // Calls the shared printCities() method from CitiesReportBase with a descriptive title
        printCities(cities, "ReportID 16. Top " + n + " Cities in a District: " + district + " by Population Report");
    }
}
