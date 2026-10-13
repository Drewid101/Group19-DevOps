package com.napier.gp19;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CountriesWorldReport extends CountriesReportBase and provides
 * methods to retrieve and print countries globally by population.
 */
public class CountriesWorldReport extends CountriesReportBase {

    // Logger instance for logging errors, warnings, and info messages
    private static final Logger LOGGER = Logger.getLogger(CountriesWorldReport.class.getName());
    /**
     * Retrieves all countries in the world ordered by population descending.
     *
     * @param conn Active database connection.
     * @return List of Country objects.
     */
    public ArrayList<Country> getCountriesWorldReport(Connection conn) {
        ArrayList<Country> countries = new ArrayList<>();

        //Check if database is connected
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate countries world report.");
            return countries;
        }

        // SQL query to retrieve all countries by population descending
        String query = "SELECT country.Code, country.Name AS CountryName, country.Continent, " +
                "country.Region, country.Population, city.Name AS CapitalName " +
                "FROM country " +
                "LEFT JOIN city ON country.Capital = city.ID " +
                "ORDER BY country.Population DESC";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            countries = buildCountriesFromResultSet(resultSet);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving countries world report from database", e);

            return countries;
        }

        return countries;
    }

    /**
     * Retrieves the top N countries in the world ordered by population descending.
     *
     * @param conn Active database connection.
     * @param topNCountries Number of top countries to show
     * @return List of Country objects.
     */
    public ArrayList<Country> getTopNCountriesWorldReport(Connection conn, int topNCountries) {
        ArrayList<Country> countries = new ArrayList<>();

        //Check if database is connected
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot generate top countries world report.");
            return countries;
        }

        String query = "SELECT country.Code, country.Name AS CountryName, country.Continent, " +
                "country.Region, country.Population, city.Name AS CapitalName " +
                "FROM country " +
                "LEFT JOIN city ON country.Capital = city.ID " +
                "ORDER BY country.Population DESC LIMIT ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, topNCountries);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                countries = buildCountriesFromResultSet(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving top countries world report from database", e);
            return countries;
        }

        return countries;
    }

    /**
     * Prints all countries in the world in population descending order.
     *
     * @param countries List of countries
     */
    public void printCountriesWorldReport(ArrayList<Country> countries) {
        printCountries(countries, "ReportID 1. All Countries in the World Report");
    }

    /**
     * Prints the top N countries in the world by population descending
     * @param countries List of countries to print.
     * @param topNCountries Number of top countries
     */
    public void printTopNCountriesWorldReport(ArrayList<Country> countries, int topNCountries) {
        printCountries(countries, "ReportID 4. Top " + topNCountries + " Countries in the World Report");
    }
}
