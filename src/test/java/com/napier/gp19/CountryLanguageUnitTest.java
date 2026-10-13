package com.napier.gp19;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CountryLanguage model class.
 * Ensures getters, setters, toString(), and default values work properly.
 */
class CountryLanguageUnitTest {

    // ---------------------------------------------------------------------
    // Test: Getters and Setters return values correctly
    // ---------------------------------------------------------------------
    @Test
    void testGettersAndSetters() {
        // Create CountryLanguage object
        CountryLanguage cl = new CountryLanguage();

        // Create a related Country object for association test
        Country country = new Country();
        country.setCountryName("Japan");

        // Set fields
        cl.setCountryCode("JPN");
        cl.setLanguage("Japanese");
        cl.setIsOfficial("T"); // "T" typically means True/Official
        cl.setPercentage(99.2);
        cl.setCountry(country); // Assign country relation

        // Verify assigned values
        assertEquals("JPN", cl.getCountryCode());
        assertEquals("Japanese", cl.getLanguage());
        assertEquals("T", cl.getIsOfficial());
        assertEquals(99.2, cl.getPercentage());
        assertEquals(country, cl.getCountry());

        // Verify nested object data
        assertEquals("Japan", cl.getCountry().getCountryName());
    }

    // ---------------------------------------------------------------------
    // Test: Ensure toString() returns formatted and complete output
    // ---------------------------------------------------------------------
    @Test
    void testToString() {
        // Create CountryLanguage object with sample data
        CountryLanguage cl = new CountryLanguage();
        cl.setCountryCode("FRA");
        cl.setLanguage("French");
        cl.setIsOfficial("T");
        cl.setPercentage(100.0);

        // Create related country object
        Country country = new Country();
        country.setCountryName("France");
        cl.setCountry(country);

        // Generate toString() output
        String result = cl.toString();

        // Validate the contents of the string
        assertTrue(result.contains("FRA"));
        assertTrue(result.contains("French"));
        assertTrue(result.contains("T"));
        assertTrue(result.contains("100.0"));
        assertTrue(result.contains("France"));

        // Validate basic formatting
        assertTrue(result.startsWith("CountryLanguage{"));
        assertTrue(result.endsWith("}"));
    }

    // ---------------------------------------------------------------------
    // Test: Ensure default constructor sets expected default/null values
    // ---------------------------------------------------------------------
    @Test
    void testDefaultValues() {
        // Newly created object with no values set
        CountryLanguage cl = new CountryLanguage();

        // All string properties should be null
        assertNull(cl.getCountryCode());
        assertNull(cl.getLanguage());
        assertNull(cl.getIsOfficial());

        // Percentage defaults to primitive double → should be 0.0
        assertEquals(0.0, cl.getPercentage());
        // Country object defaults to null
        assertNull(cl.getCountry());
    }
}
