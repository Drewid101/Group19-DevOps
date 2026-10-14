package com.napier.sem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base class for capital cities reports.
 * It includes two methods, build capitalCities from Result set and general print method.
 * Handles ResultSet → City mapping and printing.
 */
public class CapitalCitiesReportBase {

    // Logger instance
    private static final Logger LOGGER = Logger.getLogger(CapitalCitiesReportBase.class.getName());
    /**
     * Build a list of City objects from a ResultSet.
     */
    public ArrayList<City> buildCapitalCitiesFromResultSet(ResultSet rs) {
        ArrayList<City> capitals = new ArrayList<>();
        if (rs == null) {
            LOGGER.warning("ResultSet is null. Returning empty list of capital cities.");
            return capitals;
        }

        try {
            while (rs.next()) {
                //create city and country object
                City city = new City();
                Country country = new Country();

                //set city object with data
                city.setId(rs.getInt("ID"));
                city.setCityName(rs.getString("CapitalName"));
                city.setCountryCode(rs.getString("CountryCode"));
                city.setPopulation(rs.getInt("Population"));

                //associate country object for the city
                country.setCountryName(rs.getString("CountryName"));
                city.setCountry(country);

                //Add city to the list.
                capitals.add(city);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error reading capital cities from ResultSet", e);
        }

        //Check if no data was found
        if (capitals.isEmpty()) {
            LOGGER.info("No capital city data found. Report will be empty.");
        }

        return capitals;
    }

    /**
     * Prints a list of capital cities with their country and population
     * @param capitals Array list of City Objects
     * @param reportTitle Title of the report
     */
    public void printCapitalCities(ArrayList<City> capitals, String reportTitle) {

        // Validate list if null or empty
        if (capitals == null || capitals.isEmpty()) {
            LOGGER.info("No data to display for report: " + reportTitle);
            return;
        }

        // Print header
        System.out.println("\n======================= " + reportTitle +" ============================");
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.printf("%-35s %-45s %-15s%n", "Capital", "Country", "Population");
        System.out.println("----------------------------------------------------------------------------------------------------");

        //Iterate cities and print data
        for (City city : capitals) {
            if (city != null && city.getCountry() != null) {
                System.out.printf("%-35s %-45s %,15d%n",
                        city.getCityName(),
                        city.getCountry().getCountryName(),
                        city.getPopulation());
            }
        }

        //print footer line to mark end of report.
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println("====================================================================================================\n");
    }
}
