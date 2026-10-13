package com.napier.gp19;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles generating and retrieving the Country Report for a specific continent.
 */
public class CountriesContinentReport extends CountriesReportBase {

    /**Create a Logger instance for this class.
     * This logger is used to log warnings, errors, and informational messages related to
     * generating continent-based country reports.
     **/
    private static final Logger LOGGER = Logger.getLogger(CountriesContinentReport.class.getName());

    /**
     * Retrieves a list of all countries within a specified continent,
     * ordered by population (largest to smallest).
     * Columns: Name, Continent, Region, Population
     *
     * @param conn      Active database connection
     * @param continent Continent name (e.g., "Asia", "Europe")
     * @return List of Country objects, or an empty list if an error occurs or no data is found.
     */
    public ArrayList<Country> getCountriesContinentReport(Connection conn, String continent) {

        ArrayList<Country> countries = new ArrayList<>();

        // 1. Validate connection and parameter
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate continent report.");
            return countries;
        }

        // 2. Prepare SQL query using PreparedStatement
        String query = """
                SELECT country.Code, country.Name AS CountryName, country.Continent, country.Region, 
                       country.Population, city.Name AS CapitalName
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                WHERE country.Continent = ?
                ORDER BY country.Population DESC;
                """;

        // 3. Execute query safely
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, continent.trim());
            try (ResultSet rs = preparedStatement.executeQuery()) {
                countries = buildCountriesFromResultSet(rs); // use base class method
            }
        } catch (SQLException e) {
            // 4. Handle SQL exceptions with detailed messages
            LOGGER.log(Level.SEVERE, "Error retrieving continent country report for '" + continent + "'.", e);
            return countries;
        }

        return countries;
    }

    /**
     * Retrieves a list of the top N countries within a specified continent,
     * ordered by population (largest to smallest).
     *
     * @param connection             Active database connection
     * @param continent          Continent name (e.g., "Asia", "Europe")
     * @param numberOfCountries  Number of top countries to limit
     * @return List of Country objects, or an empty list if an error occurs or no data is found.
     */
    public ArrayList<Country> getTopNCountriesContinentReport(Connection connection, String continent, int numberOfCountries) {

        ArrayList<Country> countries = new ArrayList<>();

        // 1. Validate connection
        if (connection == null) {
            LOGGER.warning("Database not connected. Cannot generate top continent report.");
            return countries;
        }

        // 2. Prepare SQL query with LIMIT clause
        String query = """
                SELECT country.Code, country.Name AS CountryName, country.Continent, country.Region,
                       country.Population, city.Name AS CapitalName
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                WHERE country.Continent = ?
                ORDER BY country.Population DESC
                LIMIT ?;
                """;

        // 3. Execute query safely
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, continent.trim());
            preparedStatement.setInt(2, numberOfCountries);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                countries = buildCountriesFromResultSet(rs); // use base class method
            }
        } catch (SQLException e) {
            // 4. Handle SQL exceptions with detailed messages
            LOGGER.log(Level.SEVERE, "Error retrieving top " + numberOfCountries + " countries in continent '" + continent + "'.", e);
            return countries;
        }


        return countries;
    }

    /**
     * Prints the Country Report for a specific continent to the console.
     *
     * @param continent Continent name
     * @param countries List of Country objects
     */
    public void printCountriesContinentReport(String continent, ArrayList<Country> countries) {
        printCountries(countries, "ReportID 2. All Countries in Continent '" + continent + "' Report");
    }

    /**
     * Prints the Top N Country Report for a specific continent to the console.
     *
     * @param continent         Continent name
     * @param countries         List of Country objects
     * @param numberOfCountries Number of top countries displayed
     */
    public void printTopNCountriesContinentReport(String continent, ArrayList<Country> countries, int numberOfCountries) {
        printCountries(countries, "ReportID 5. Top " + numberOfCountries + " Countries in Continent '" + continent + "' Report");
    }
}
