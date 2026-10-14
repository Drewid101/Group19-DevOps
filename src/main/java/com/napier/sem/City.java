package com.napier.sem;

/**
 * Represents a city within database
 * The class stores information about a city, its ID, name, country code,district, population,
 * and the associated Country object.
 * It includes private variables for encapsulation and getter methods.
 */
public class City {
    // Unique identifier for the city
    private int id;

    // Name of the city
    private String cityName;

    // Country code (foreign key reference)
    private String countryCode;

    // District of the city
    private String district;

    // Population of the city
    private int population;

    // Associated Country object (can be null if not loaded)
    private Country country;

    //default constructor
    public City() {
    }

    /**
     * Gets the unique ID of the city
     *
     * @return id
     */
    public int getID() {
        return id;
    }

    /**
     * Gets the name of the city
     *
     * @return cityName
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Gets the country code
     *
     * @return countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }


    /**
     * Gets the district of the city
     *
     * @return district
     */
    public String getDistrict() {
        return district;
    }


    /**
     * Gets the population of the city.
     *
     * @return population
     */
    public int getPopulation() {
        return population;
    }


    /**
     * Gets the associated country object.
     *
     * @return country object, or null if not loaded
     */
    public Country getCountry() {
        return country;
    }


    /**
     * Sets the unique ID of the city
     *
     * @param id unique identifier
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the name of the city
     *
     * @param cityName name of the city
     */

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * Sets the country code
     *
     * @param countryCode country code
     */

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * Sets the district of the city
     *
     * @param district district name
     */

    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * Sets the population of the city
     *
     * @param population population count
     */

    public void setPopulation(int population) {
        this.population = population;
    }

    /**
     * Sets the associated country object
     *
     * @param country Country object
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    /**Returns a string representation of the City object.
     *The string includes the city's ID, name, country code, district,
     *population, and associated country.
     * @return the formatted string
     */
    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", cityName='" + cityName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", district='" + district + '\'' +
                ", population=" + population +
                ", country=" + country +
                '}';
    }
}

