package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CapitalCitiesWorldReport extends CapitalCitiesReportBase and provides
 * methods to retrieve and print capital cities globally by population.
 */
public class CapitalCitiesWorldReport extends CapitalCitiesReportBase {

    // Logger instance for this class
    private static final Logger LOGGER = Logger.getLogger(CapitalCitiesWorldReport.class.getName());


    /**
     * Retrieves all capital cities in the world ordered by population descending.
     * Returns an empty list if a database error occurs.
     *
     * @param conn Database connection object
     * @return List of City objects
     */
    public ArrayList<City> getAllCapitalCitiesInWorldByPopulation(Connection conn) {

        //arraylist to hold the retrieved the capital cities data
        ArrayList<City> capitals = new ArrayList<>();

        //Validate Connection
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot display All Capital Cities in the World Report");
            return capitals;
        }
        // SQL query to extract data from database, the query join 'country' and 'city' tables, ordered by population
        String query = "SELECT c.ID, c.Name AS CapitalName, c.CountryCode, c.Population, " +
                "co.Name AS CountryName FROM country co " +
                "JOIN city c ON co.Capital = c.ID ORDER BY c.Population DESC";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            //call method from parent class
            capitals = buildCapitalCitiesFromResultSet(rs);

        } catch (SQLException e) {
            //Catch SQL exceptions, print detailed error, and return the (empty) list
            LOGGER.log(Level.SEVERE, "Error retrieving all capital cities report", e);
            return capitals; //return safely with an empty list.
        }

        return capitals;
    }


    /**
     * Retrieves top N capital cities in the world ordered by population descending.
     * Returns an empty list if a database error occurs
     * @param conn Database connection object
     * @param numberOfCapitalCities Number of Capitals to limit the report
     * @return List of City objects
     */
    public ArrayList<City> getTopNCapitalCitiesInWorldByPopulation(Connection conn, int numberOfCapitalCities) {
        //arraylist to store capitals
        ArrayList<City> capitals = new ArrayList<>();

        //Validate connection
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot display Top N Capital Cities in the World Report");
            return capitals;
        }

        //query for top n capital cities
        String query = "SELECT c.ID, c.Name AS CapitalName, c.CountryCode, c.Population, " +
                "co.Name AS CountryName FROM country co " +
                "JOIN city c ON co.Capital = c.ID ORDER BY c.Population DESC LIMIT ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, numberOfCapitalCities); //set parameter to prevent SQL injection
            try (ResultSet rs = stmt.executeQuery()) {
                capitals = buildCapitalCitiesFromResultSet(rs); //call method to build capitals list
            }
        } catch (SQLException e) {
            //Catch SQL exceptions, print detailed error, and return the (empty) list
            LOGGER.log(Level.SEVERE, "Error retrieving top N capital cities report", e);
            return capitals; //return safely with an empty list.
        }

        return capitals;
    }

    /**
     * Prints a list of all capital cities in the world
     * @param capitals List of cities ot print
     */
    public void printAllCapitalCitiesInWorldByPopulation(ArrayList<City> capitals) {
        //call print method from base class
        printCapitalCities(capitals, "ReportID 17. All Capital Cities in the World Report");
    }

    /**
     * Prints a list of top N capital cities in the world.
     *
     * @param capitals List of cities to print
     * @param numberOfCapitals Number of top cities displayed
     */
    public void printTopNCapitalCitiesInWorldByPopulation(ArrayList<City> capitals, int numberOfCapitals) {
        printCapitalCities(capitals, "ReportID 20. Top " + numberOfCapitals + " Capital Cities in the World Report");
    }

}
