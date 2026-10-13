package com.napier.gp19;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base class for generating city reports (world, continent, region).
 * This class handles reading city data from a database ResultSet
 * and printing formatted reports to the console.
 */
public class CitiesReportBase {

    /**
     * Logger instance for CitiesReportBase class.
     * Declared as private static final to follow best practices:
     * - private: only accessible within this class
     * - static: shared across all instances
     * - final: cannot be reassigned
     * Logger name is fully qualified class name for easy identification.
     */
    private static final Logger LOGGER = Logger.getLogger(CitiesReportBase.class.getName());

    /**
     * Builds a list of City objects from a given SQL ResultSet.
     * The ResultSet is expected to contain columns:
     * CityName, CountryName, District, and Population.
     *
     * @param rs the ResultSet returned from a database query
     * @return an ArrayList of City objects populated with data from the ResultSet
     */
    public ArrayList<City> buildCitiesFromResultSet(ResultSet rs) {
        // Create an empty list to store City objects
        ArrayList<City> cities = new ArrayList<>();

        // If the ResultSet is null, return the empty list
        if (rs == null) {
            LOGGER.warning("ResultSet is null. Returning empty city list.");
            return cities;
        }

        try {
            // Iterate through all rows in the ResultSet
            while (rs.next()) {
                // Create new City and Country objects
                City city = new City();
                Country country = new Country();

                // Retrieve city information from the ResultSet
                city.setCityName(rs.getString("CityName"));
                city.setDistrict(rs.getString("District"));
                city.setPopulation(rs.getInt("Population"));

                // Retrieve country name and assign it to the city
                country.setCountryName(rs.getString("CountryName"));
                city.setCountry(country);

                // Add the City object to the list
                cities.add(city);
            }
        } catch (SQLException e) {
            // Log SQL exceptions with SEVERE level and include the stack trace
            LOGGER.log(Level.SEVERE, "Error reading cities from ResultSet.", e);
        }

        // If no city data was found, display a message
        if (cities.isEmpty()) {
            LOGGER.info("No city data found in ResultSet. Report will be empty.");
        }

        // Return the list of cities
        return cities;
    }

    /**
     * Prints a formatted report of cities to the console.
     * Displays the report title, followed by a table of city data.
     *
     * @param cities the list of City objects to print
     * @param reportTitle the title to display above the report
     */
    public void printCities(ArrayList<City> cities, String reportTitle) {
        // Check if there are any cities to print
        if (cities == null || cities.isEmpty()) {
            LOGGER.info("No data to display for report: " + reportTitle);
            return;
        }


        // Print the report title
        System.out.println("\n========================" + reportTitle + "=======================");
        // Print a separator line for readability
        System.out.println("--------------------------------------------------------------------------------------------" +
                "------------------------------");
        // Print the table header (column titles)
        System.out.printf("%-35s %-35s %-20s %-15s%n",
                "City", "Country", "District", "Population");

        // Print a separator line for readability
        System.out.println("--------------------------------------------------------------------------------------------" +
                "------------------------------");

        // Loop through each City object and print its details
        for (City city : cities) {
            // Ensure the city and its associated country are not null before printing
            if (city != null && city.getCountry() != null) {
                System.out.printf("%-35s %-35s %-20s %,15d%n",
                        city.getCityName(),                     // City name
                        city.getCountry().getCountryName(),      // Country name
                        city.getDistrict(),                      // District
                        city.getPopulation());                   // Population (formatted with commas)
            }
        }

        // Print a closing separator line
        System.out.println("--------------------------------------------------------------------------------------------" +
                "------------------------------");
        System.out.println("============================================================================================" +
                "==============================");
    }
}
