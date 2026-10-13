package com.napier.gp19;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CapitalCitiesRegionReport extends CapitalCitiesReportBase and provides
 * methods to retrieve and print capital cities in a specific region by population.
 */
public class CapitalCitiesRegionReport extends CapitalCitiesReportBase {

    // Logger instance
    private static final Logger LOGGER = Logger.getLogger(CapitalCitiesRegionReport.class.getName());

    /**
     * Get all capital cities in a specific region, ordered by population descending
     *
     * @param conn Database connection object
     * @param region Region name to filter countries
     * @return ArrayList of City objects
     */
    public ArrayList<City> getAllCapitalCitiesInRegionByPopulation(Connection conn, String region) {
        // ArrayList to hold the retrieved capital cities data
        ArrayList<City> capitals = new ArrayList<>();

        //Validate Connection
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot display All Capital Cities in the Region Report");
            return capitals;
        }

        // SQL query to get capitals in the specified region, ordered by population
        String query = "SELECT c.ID, c.Name AS CapitalName, c.CountryCode, c.Population, " +
                "co.Name AS CountryName FROM country co " +
                "JOIN city c ON co.Capital = c.ID " +
                "WHERE co.Region = ? ORDER BY c.Population DESC";

        try (PreparedStatement queryStmt = conn.prepareStatement(query)) {

            // Bind the region parameter safely to prevent SQL injection
            queryStmt.setString(1, region);

            try (ResultSet rs = queryStmt.executeQuery()) {

                // Call helper method from base class to build the list of capitals
                capitals = buildCapitalCitiesFromResultSet(rs);
            }
        } catch (SQLException e) {
            //Catch SQL exceptions, print detailed error, and return the (empty) list
            LOGGER.log(Level.SEVERE, "Error retrieving all capital cities in region: " + region, e);
            return capitals; //return safely with an empty list.
        }

        return capitals;
    }

    /**
     * Get top N capital cities in a specific region by population
     *
     * @param conn Database connection object
     * @param region Region name to filter countries
     * @param numberOfCapitalCities Number of top capitals to retrieve
     * @return ArrayList of City objects
     */
    public ArrayList<City> getTopNCapitalCitiesInRegionByPopulation(Connection conn, String region, int numberOfCapitalCities) {
        ArrayList<City> capitals = new ArrayList<>();

        //Validate Connection
        if (conn == null) {
            LOGGER.warning("Database not connected. Cannot display Top N Capital Cities in the Region Report");
            return capitals;
        }

        String query = "SELECT c.ID, c.Name AS CapitalName, c.CountryCode, c.Population, " +
                "co.Name AS CountryName FROM country co " +
                "JOIN city c ON co.Capital = c.ID " +
                "WHERE co.Region = ? ORDER BY c.Population DESC LIMIT ?";

        try (PreparedStatement preparedStmt = conn.prepareStatement(query)) {
            //safe binding
            preparedStmt.setInt(2, numberOfCapitalCities);
            preparedStmt.setString(1, region);
            try (ResultSet rs = preparedStmt.executeQuery()) {
                capitals = buildCapitalCitiesFromResultSet(rs);
            }
        } catch (SQLException e) {
            //Catch SQL exceptions, print detailed error, and return the (empty) list
            LOGGER.log(Level.SEVERE, "Error retrieving top " + numberOfCapitalCities + " capital cities in region: " + region, e);
            return capitals; //return safely with an empty list.
        }

        return capitals;
    }

    /**
     * Print all capital cities in a specific region
     *
     * @param capitals List of City objects to print
     * @param region Region name for the report title
     */
    public void printAllCapitalCitiesInRegionByPopulation(ArrayList<City> capitals, String region) {

        //call general print method from base class
        printCapitalCities(capitals, "ReportID 19. All Capital Cities in Region: " + region + " Report");
    }

    /**
     * Print top N capital cities in a specific region
     *
     * @param capitals List of City objects to print
     * @param region Region name for the report title
     * @param numberOfCapitalCities Number of top capitals displayed
     */
    public void printTopNCapitalCitiesInRegionByPopulation(ArrayList<City> capitals, String region, int numberOfCapitalCities) {
        printCapitalCities(capitals, "ReportID 22. Top " + numberOfCapitalCities + " Capital Cities in Region: " + region + " Report");
    }

}
