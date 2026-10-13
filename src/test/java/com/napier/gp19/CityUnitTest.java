package com.napier.gp19;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for the City model class.
 * Ensures getters, setters, toString(), and default values work correctly.
 */
class CityUnitTest {

    // ---------------------------------------------------------------------
    // Test: Verify all getters and setters function correctly
    // ---------------------------------------------------------------------
    @Test
    void testGettersAndSetters() {
        // Create a new City object
        City city = new City();

        // Create a related Country object
        Country country = new Country();
        country.setCountryName("Japan");

        // Set values for City fields
        city.setId(1);
        city.setCityName("Tokyo");
        city.setCountryCode("JPN");
        city.setDistrict("Tokyo-to");
        city.setPopulation(37833000);
        city.setCountry(country); // associate with country

        // Validate values via getters
        assertEquals(1, city.getID());
        assertEquals("Tokyo", city.getCityName());
        assertEquals("JPN", city.getCountryCode());
        assertEquals("Tokyo-to", city.getDistrict());
        assertEquals(37833000, city.getPopulation());
        assertEquals(country, city.getCountry());

        // Validate nested object data
        assertEquals("Japan", city.getCountry().getCountryName());
    }

    // ---------------------------------------------------------------------
    // Test: Ensure toString() output is correctly formatted and includes key fields
    // ---------------------------------------------------------------------
    @Test
    void testToString() {
        // Prepare sample city data
        City city = new City();
        city.setId(2);
        city.setCityName("Paris");
        city.setCountryCode("FRA");
        city.setDistrict("Île-de-France");
        city.setPopulation(2148327);

        // Related country object
        Country country = new Country();
        country.setCountryName("France");
        city.setCountry(country);

        // Convert city to string
        String result = city.toString();

        // Check that essential data is included in the output
        assertTrue(result.contains("Paris"));
        assertTrue(result.contains("FRA"));
        assertTrue(result.contains("Île-de-France"));
        assertTrue(result.contains("2148327"));
        assertTrue(result.contains("France"));

        // Ensure formatting is correct
        assertTrue(result.startsWith("City{"));
        assertTrue(result.endsWith("}"));
    }

    // ---------------------------------------------------------------------
    // Test: Ensure default constructor assigns expected default and null values
    // ---------------------------------------------------------------------
    @Test
    void testDefaultValues() {
        // New city object with no values set
        City city = new City();

        // Primitive fields default to 0; object fields to null
        assertEquals(0, city.getID());
        assertNull(city.getCityName());
        assertNull(city.getCountryCode());
        assertNull(city.getDistrict());
        assertEquals(0, city.getPopulation());
        assertNull(city.getCountry());
    }
}
