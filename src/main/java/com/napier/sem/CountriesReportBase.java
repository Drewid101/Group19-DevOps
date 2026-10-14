package com.napier.sem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base class for Country reports.
 * Handles ResultSet → Country object mapping and generic print functionality.
 */
public class CountriesReportBase {

    // Logger instance for logging errors, warnings, and info messages
    private static final Logger LOGGER = Logger.getLogger(CountriesReportBase.class.getName());

    /**
     * Builds a list of Country objects from a ResultSet.
     *
     * @param rs The ResultSet containing country data.
     * @return ArrayList of Country objects.
     */
    public ArrayList<Country> buildCountriesFromResultSet(ResultSet rs) {
        ArrayList<Country> countries = new ArrayList<>();
        if (rs == null) return countries;

        try {
            while (rs.next()) {
                Country country = new Country();
                country.setCode(rs.getString("Code"));
                country.setCountryName(rs.getString("CountryName"));
                country.setContinent(rs.getString("Continent"));
                country.setRegion(rs.getString("Region"));
                country.setPopulation(rs.getInt("Population"));
                country.setCapitalName(rs.getString("CapitalName"));
                countries.add(country);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error reading countries from ResultSet", e);
        }

        if (countries.isEmpty()) {
            LOGGER.info("No country data found. Report will be empty.");
        }

        return countries;
    }

    /**
     * Prints a formatted table of countries to the console.
     *
     * @param countries   List of countries to print.
     * @param reportTitle Title of the report.
     */
    public void printCountries(ArrayList<Country> countries, String reportTitle) {
        if (countries == null || countries.isEmpty()) {
            LOGGER.info("No data to display for report: " + reportTitle);
            return;
        }

        System.out.println("\n============= " + reportTitle + " =============");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s %-35s %-20s %-25s %-15s %-25s%n",
                "Code", "Country Name", "Continent", "Region", "Population", "Capital");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------");

        for (Country country : countries) {
            if (country != null) {
                System.out.printf("%-10s %-35s %-20s %-25s %,15d %-25s%n",
                        country.getCode(),
                        country.getCountryName(),
                        country.getContinent(),
                        country.getRegion(),
                        country.getPopulation(),
                        country.getCapitalName() != null ? country.getCapitalName() : "No Capital");
            }
        }

        System.out.println("---------------------------------------------------------------------------------------------------------------------------");
        System.out.println("===========================================================================================================================\n");
    }

}
