package com.napier.gp19;

/**
 * Utility class for common population calculations.
 */
public class PopulationUtils {

    // Record to store calculated values
    public record PopValues(long total, long city, long nonCity, double cityPercent, double nonCityPercent) {}

    /**
     * Calculates total, city, non-city populations and percentages for a Country object.
     *
     * @param c Country object
     * @return PopValues record containing all calculated fields
     */
    public static PopValues calculatePopulationValues(Country c) {
        long total = c.getPopulation();
        long city = c.getGnp() != null ? c.getGnp().longValue() : 0;
        long nonCity = c.getGnpOld() != null ? c.getGnpOld().longValue() : 0;
        double cityPercent = total > 0 ? (city * 100.0 / total) : 0;
        double nonCityPercent = total > 0 ? (nonCity * 100.0 / total) : 0;
        return new PopValues(total, city, nonCity, cityPercent, nonCityPercent);
    }
}
