package com.napier.gp19;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates a report showing the total number of speakers
 * and world population percentage for major languages.
 */
public class LanguagePopulationReport {

    // Logger instance
    private static final Logger LOGGER = Logger.getLogger(LanguagePopulationReport.class.getName());

    /**
     * Retrieves the population and world percentage of speakers for major languages.
     * Languages: Chinese, English, Hindi, Spanish, Arabic.
     *
     * @param conn Active database connection (must not be null)
     * @return A list of CountryLanguage objects representing the report data
     */
    public ArrayList<CountryLanguage> getLanguagePopulationReport(Connection conn) {
        ArrayList<CountryLanguage> reportList = new ArrayList<>();

        if (conn == null) {
            LOGGER.warning("Database connection is null. Cannot generate language population report.");
            return reportList;
        }

        String strSelect =
                "SELECT cl.Language, " +
                        "SUM((c.Population * cl.Percentage) / 100) AS Speakers, " +
                        "ROUND(SUM((c.Population * cl.Percentage) / 100) / " +
                        "(SELECT SUM(Population) FROM country) * 100, 2) AS WorldPercentage " +
                        "FROM countrylanguage cl " +
                        "JOIN country c ON cl.CountryCode = c.Code " +
                        "WHERE cl.Language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic') " +
                        "GROUP BY cl.Language " +
                        "ORDER BY Speakers DESC";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(strSelect)) {

            while (rs.next()) {
                CountryLanguage cl = new CountryLanguage();

                // Reusing CountryLanguage fields for report data
                cl.setLanguage(rs.getString("Language"));
                cl.setCountryCode(String.format("%,d", rs.getLong("Speakers"))); // store speaker count as string
                cl.setPercentage(rs.getDouble("WorldPercentage")); // reuse 'percentage' for world percentage
                cl.setIsOfficial("N/A"); // Not applicable for this aggregated report

                reportList.add(cl);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error retrieving language population report", e);
            reportList.clear();
        }

        if (reportList.isEmpty()) {
            LOGGER.warning("No language population data found.");
        }

        return reportList;
    }

    /**
     * Prints the language population report in a formatted table.
     *
     * @param reportList List of CountryLanguage objects
     */
    public void printLanguagePopulationReport(ArrayList<CountryLanguage> reportList) {
        if (reportList == null || reportList.isEmpty()) {
            LOGGER.info("No data available for language population report.");
            return;
        }

        System.out.println("\n============= ReportID 32. Language Population Report =============");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-15s %-20s %-20s%n", "Language", "Speakers", "% of World Population");
        System.out.println("------------------------------------------------------------");

        for (CountryLanguage cl : reportList) {
            System.out.printf("%-15s %-20s %-20.2f%n",
                    cl.getLanguage(),
                    cl.getCountryCode(),     // used here to display formatted speakers
                    cl.getPercentage());     // reused for world percentage
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("============================================================");
    }
}
