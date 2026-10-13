package com.napier.gp19;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ReportWriter is a simple utility class for generating reports in Markdown format.
 * It can create reports for:
 *  - Capital cities
 *  - Cities
 *  - Countries
 *  - Language populations
 *  - Population by city, country, continent, region, and world
 * Reports are saved in the "./reports/" folder. The class handles empty or null
 * data lists by printing a message or adding placeholder rows.
 */

public class ReportWriter {

    /**
     * Writes Capital Cities Report in Markdown format
     */
    public void outputCapitalCities(ArrayList<City> capitals, String filename, String reportTitle) {
        if (capitals == null || capitals.isEmpty()) {
            System.out.println("No capital city data to display for: " + reportTitle);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(reportTitle).append("\n\n");
        sb.append("| Capital | Country | Population |\n");
        sb.append("| --- | --- | ---: |\n");

        for (City city : capitals) {
            if (city != null && city.getCountry() != null) {
                sb.append("| ").append(city.getCityName()).append(" | ")
                        .append(city.getCountry().getCountryName()).append(" | ")
                        .append(String.format("%,d", city.getPopulation())).append(" |\n");
            }
        }
        writeToFile(filename, sb);
    }

    /**
     * Writes Cities Report in Markdown format
     */
    public void outputCities(ArrayList<City> cities, String filename, String reportTitle) {
        if (cities == null || cities.isEmpty()) {
            System.out.println("No city data to display for: " + reportTitle);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(reportTitle).append("\n\n");
        sb.append("| City | Country | District | Population |\n");
        sb.append("| --- | --- | --- | ---: |\n");

        for (City city : cities) {
            if (city != null && city.getCountry() != null) {
                sb.append("| ").append(city.getCityName()).append(" | ")
                        .append(city.getCountry().getCountryName()).append(" | ")
                        .append(city.getDistrict()).append(" | ")
                        .append(String.format("%,d", city.getPopulation())).append(" |\n");
            }
        }
        writeToFile(filename, sb);
    }

    /**
     * Writes Countries Report in Markdown format
     */
    public void outputCountries(ArrayList<Country> countries, String filename, String reportTitle) {
        if (countries == null || countries.isEmpty()) {
            System.out.println("No country data to display for: " + reportTitle);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(reportTitle).append("\n\n");
        sb.append("| Code | Country | Continent | Region | Population | Capital |\n");
        sb.append("| --- | --- | --- | --- | ---: | --- |\n");

        for (Country c : countries) {
            if (c != null) {
                sb.append("| ").append(c.getCode()).append(" | ")
                        .append(c.getCountryName()).append(" | ")
                        .append(c.getContinent()).append(" | ")
                        .append(c.getRegion()).append(" | ")
                        .append(String.format("%,d", c.getPopulation())).append(" | ")
                        .append(c.getCapitalName() != null ? c.getCapitalName() : "No Capital").append(" |\n");

            }
        }
        writeToFile(filename, sb);
    }


    /**
     * Writes Language Population Report
     */
    public void outputLanguagePopulationReport(ArrayList<CountryLanguage> reportList, String filename) {
        if (reportList == null || reportList.isEmpty()) {
            System.out.println("No data available for language population report.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Report 32: Language Population Report\n\n");
        sb.append("| Language | Speakers | % of World Population |\n");
        sb.append("| --- | ---: | ---: |\n");

        for (CountryLanguage cl : reportList) {
            sb.append("| ").append(cl.getLanguage()).append(" | ")
                    .append(cl.getCountryCode()).append(" | ")
                    .append(String.format("%.2f%%", cl.getPercentage())).append(" |\n");
        }
        writeToFile(filename, sb);
    }

    /**
     * Writes Population by City Report
     */
    public void outputPopulationCityReport(List<City> cities, String filename) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Report 31: Population by City Report\n\n");
        sb.append("| City | District | Country Code | Population |\n");
        sb.append("| --- | --- | --- | ---: |\n");

        if (cities == null || cities.isEmpty()) {
            sb.append("| No data available | | | |\n");
        } else {
            for (City city : cities) {
                sb.append("| ").append(city.getCityName()).append(" | ")
                        .append(city.getDistrict()).append(" | ")
                        .append(city.getCountryCode()).append(" | ")
                        .append(String.format("%,d", city.getPopulation())).append(" |\n");
            }
        }
        writeToFile(filename, sb);
    }

    /**
     * Writes Population by Continent Report
     */
    public void outputContinentPopulation(List<Country> results, String filename, String title) {
        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(title).append("\n\n");
        sb.append("| Continent | Total Pop | City Pop | Non-City Pop | City % | Non-City % |\n");
        sb.append("| --- | ---: | ---: | ---: | ---: | ---: |\n");

        if (results == null || results.isEmpty()) {
            sb.append("| No data available | | | | | |\n");
        } else {
            for (Country c : results) {
                PopulationUtils.PopValues v = PopulationUtils.calculatePopulationValues(c);
                sb.append("| ").append(c.getContinent()).append(" | ")
                        .append(String.format("%,d", v.total())).append(" | ")
                        .append(String.format("%,d", v.city())).append(" | ")
                        .append(String.format("%,d", v.nonCity())).append(" | ")
                        .append(String.format("%.2f%%", v.cityPercent())).append(" | ")
                        .append(String.format("%.2f%%", v.nonCityPercent())).append(" |\n");
            }
        }
        writeToFile(filename, sb);
    }

    /**
     * Writes Population by Country/Region Report (shared method)
     */
    public void outputPopulationByGroup(List<Country> results, String filename, String title, String groupByColumn) {
        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(title).append("\n\n");
        sb.append("| ").append(groupByColumn)
                .append(" | Total Pop | City Pop | Non-City Pop | City % | Non-City % |\n");
        sb.append("| --- | ---: | ---: | ---: | ---: | ---: |\n");

        if (results == null || results.isEmpty()) {
            sb.append("| No data available | | | | | |\n");
        } else {
            for (Country c : results) {
                String name = groupByColumn.equals("Region") ? c.getRegion() : c.getCountryName();
                PopulationUtils.PopValues v = PopulationUtils.calculatePopulationValues(c);
                sb.append("| ").append(name).append(" | ")
                        .append(String.format("%,d", v.total())).append(" | ")
                        .append(String.format("%,d", v.city())).append(" | ")
                        .append(String.format("%,d", v.nonCity())).append(" | ")
                        .append(String.format("%.2f%%", v.cityPercent())).append(" | ")
                        .append(String.format("%.2f%%", v.nonCityPercent())).append(" |\n");
            }
        }
        writeToFile(filename, sb);
    }

    /**
     * Writes Population of the World Report
     */
    public void outputPopulationWorld(PopulationWorldReport.PopulationData data, String filename) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Report 26: Population of the World\n\n");
        sb.append("| Name | Total Population | City Population | Non-City Population | City % | Non-City % |\n");
        sb.append("| --- | ---: | ---: | ---: | ---: | ---: |\n");

        if (data == null) {
            sb.append("| No data available | | | | | |\n");
        } else {
            sb.append("| ").append(data.name).append(" | ")
                    .append(String.format("%,d", data.totalPopulation)).append(" | ")
                    .append(String.format("%,d", data.cityPopulation)).append(" | ")
                    .append(String.format("%,d", data.nonCityPopulation)).append(" | ")
                    .append(String.format("%.2f%%", data.cityPercentage)).append(" | ")
                    .append(String.format("%.2f%%", data.nonCityPercentage)).append(" |\n");
        }
        writeToFile(filename, sb);
    }

    // Common helper method to write to ./reports/
    private void writeToFile(String filename, StringBuilder sb) {
        try {
            new File("./reports/").mkdirs();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + filename)));
            writer.write(sb.toString());
            writer.close();
            System.out.println("✅ Report written: ./reports/" + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

