package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * CapitalCitiesContinentReport extends CapitalCitiesReportBase and provides
 * methods to retrieve and print capital cities in a specific continent by population.
 */
public class CapitalCitiesContinentReport extends CapitalCitiesReportBase {

    // Logger instance
    private static final Logger LOGGER = Logger.getLogger(CapitalCitiesContinentReport.class.getName());
    /**
     * Get all capital cities in a specific continent, ordered by population descending
     *
     * @param conn Database connection object
     * @param continent Continent name to filter countries
     * @return ArrayList of City objects
     */
    public ArrayList<City> getAllCapitalCitiesInContinentByPopulation(Connection conn, String continent) {
        ArrayList<City> capitals = new ArrayList<>(); // ArrayList to hold the retrieved capital cities data

        //Validate Connection
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot display All Capital Cities in the Continent Report.");
            return capitals;
        }

        // SQL query to get capitals in the specified continent, ordered by population
        String query = "SELECT c.ID, c.Name AS CapitalName, c.CountryCode, c.Population, " +
                "co.Name AS CountryName FROM country co " +
                "JOIN city c ON co.Capital = c.ID " +
                "WHERE co.Continent = ? ORDER BY c.Population DESC";

        try (PreparedStatement pStmt = conn.prepareStatement(query)) {
            pStmt.setString(1, continent); // safe binding
            try (ResultSet rs = pStmt.executeQuery()) {

                //calling method form base class for building list of capitals
                capitals = buildCapitalCitiesFromResultSet(rs);
            }
        } catch (SQLException e) {
            //Catch SQL exceptions, print detailed error, and return the (empty) list
            LOGGER.log(Level.SEVERE, "Error retrieving capital cities report for continent: " + continent, e);
            return capitals; //return safely with an empty list.
        }

        return capitals;
    }

    /**
     * Get top N capital cities in a specific continent by population
     *
     * @param conn Database connection object
     * @param continent Continent name to filter countries
     * @param numberOfCapitalCities Number of top capitals to retrieve
     * @return ArrayList of City objects
     */
    public ArrayList<City> getTopNCapitalCitiesInContinentByPopulation(Connection conn, String continent, int numberOfCapitalCities) {
        ArrayList<City> capitals = new ArrayList<>();

        //Validate Connection
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot display Top N Capital Cities in the Continent Report.");
            return capitals;
        }

        String query = "SELECT c.ID, c.Name AS CapitalName, c.CountryCode, c.Population, " +
                "co.Name AS CountryName FROM country co " +
                "JOIN city c ON co.Capital = c.ID " +
                "WHERE co.Continent = ? ORDER BY c.Population DESC LIMIT ?";

        try (PreparedStatement pStmt = conn.prepareStatement(query)) {
            pStmt.setString(1, continent); // safe binding
            pStmt.setInt(2, numberOfCapitalCities); //safe binding
            try (ResultSet rs = pStmt.executeQuery()) {

                capitals = buildCapitalCitiesFromResultSet(rs);
            }
        } catch (SQLException e) {
            //Catch SQL exceptions, print detailed error, and return the (empty) list
            LOGGER.log(Level.SEVERE, "Error retrieving top " + numberOfCapitalCities + " capital cities for continent: " + continent, e);
            return capitals; //return safely with an empty list.
        }

        return capitals;
    }

    /**
     * Print all capital cities in a specific continent
     *
     * @param capitals List of City objects to print
     * @param continent Continent name for the report title
     */
    public void printAllCapitalCitiesInContinentByPopulation(ArrayList<City> capitals, String continent) {
        //call general print method from base class
        printCapitalCities(capitals, "ReportID 18. All Capital Cities in Continent: " + continent + " Report");
    }

    /**
     * Print top N capital cities in a specific continent
     *
     * @param capitals List of City objects to print
     * @param continent Continent name for the report title
     * @param numberOfCapitalCities Number of top capitals displayed
     */
    public void printTopNCapitalCitiesInContinentByPopulation(ArrayList<City> capitals, String continent, int numberOfCapitalCities) {
        printCapitalCities(capitals, "ReportID 21. Top " + numberOfCapitalCities + " Capital Cities in Continent: " + continent + " Report");
    }
}
