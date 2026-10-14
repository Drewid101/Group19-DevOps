package com.napier.sem;

/**
 * Represents a language spoken in a country.
 * The class stores information about the country code, language name,
 * whether the language is official, the percentage of speakers,
 * and the associated Country object.
 * It includes private variables for encapsulation, getter and setter methods,
 * and a toString method for displaying object details.
 */
public class CountryLanguage
{
    // Country code (foreign key to Country)
    private String countryCode;

    // Language name spoken in the country
    private String language;

    // Indicates whether the language is official ("T" for true, "F" for false)
    private String isOfficial;

    // Percentage of the population that speaks the language
    private double percentage;

    // Associated Country object (optional, may be null if not loaded)
    private Country country;

    //default constructor
    public CountryLanguage() {
    }


    /**
     * Gets the country code.
     * @return country code
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Gets the language name.
     * @return language name
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Checks if the language is official.
     * @return "T" if official, "F" otherwise
     */
    public String getIsOfficial() {
        return isOfficial;
    }

    /**
     * Gets the percentage of speakers.
     * @return percentage of population that speaks the language
     */
    public double getPercentage() {
        return percentage;
    }

    /**
     * Gets the associated Country object.
     * @return Country object, or null if not loaded
     */
    public Country getCountry() {
        return country;
    }


    /**
     * sets the country code.
     * @param countryCode the country code.
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * sets the language name.
     * @param language the language name
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * sets if the language is official.
     * @param isOfficial if it is official or otherwise
     */
    public void setIsOfficial(String isOfficial) {
        this.isOfficial = isOfficial;
    }

    /**
     * sets the percentage of speakers.
     * @param percentage the percentage of people who speak this language
     */
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    /**
     * sets the associated Country object.
     * @param country the associate country
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * Returns a string representation of country language object.
     * @return a formatted string containing language and country details
     */
    @Override
    public String toString() {
        return "CountryLanguage{" +
                "countryCode='" + countryCode + '\'' +
                ", language='" + language + '\'' +
                ", isOfficial='" + isOfficial + '\'' +
                ", percentage=" + percentage +
                ", country=" + country +
                '}';
    }
}
