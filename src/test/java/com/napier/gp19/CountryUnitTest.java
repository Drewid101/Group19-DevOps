package com.napier.gp19;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Country model class.
 * Ensures getters, setters, toString, and default values work correctly.
 */
class CountryUnitTest {

    // ---------------------------------------------------------------------
    // Test: Verify all getters and setters store and return correct values
    // ---------------------------------------------------------------------
    @Test
    void testGettersAndSetters() {
        // Create new Country object
        Country country = new Country();

        // Set fields with sample data
        country.setCode("JPN");
        country.setCountryName("Japan");
        country.setContinent("Asia");
        country.setRegion("Eastern Asia");
        country.setSurfaceArea(377975.0);
        country.setIndepYear(660);
        country.setPopulation(125800000);
        country.setLifeExpectancy(84.6);
        country.setGnp(4937000.0);
        country.setGnpOld(4883000.0);
        country.setLocalName("Nippon");
        country.setGovernmentForm("Constitutional Monarchy");
        country.setHeadOfState("Emperor Naruhito");
        country.setCapital(1532);
        country.setCode2("JP");

        // Validate each field using getters
        assertEquals("JPN", country.getCode());
        assertEquals("Japan", country.getCountryName());
        assertEquals("Asia", country.getContinent());
        assertEquals("Eastern Asia", country.getRegion());
        assertEquals(377975.0, country.getSurfaceArea());
        assertEquals(660, country.getIndepYear());
        assertEquals(125800000, country.getPopulation());
        assertEquals(84.6, country.getLifeExpectancy());
        assertEquals(4937000.0, country.getGnp());
        assertEquals(4883000.0, country.getGnpOld());
        assertEquals("Nippon", country.getLocalName());
        assertEquals("Constitutional Monarchy", country.getGovernmentForm());
        assertEquals("Emperor Naruhito", country.getHeadOfState());
        assertEquals(1532, country.getCapital());
        assertEquals("JP", country.getCode2());
    }

    // ---------------------------------------------------------------------
    // Test: Ensure toString() returns meaningful and correctly formatted output
    // ---------------------------------------------------------------------
    @Test
    void testToString() {
        // Prepare sample country
        Country country = new Country();
        country.setCode("FRA");
        country.setCountryName("France");
        country.setContinent("Europe");
        country.setRegion("Western Europe");
        country.setSurfaceArea(551695.0);
        country.setIndepYear(843);
        country.setPopulation(67800000);
        country.setLifeExpectancy(82.5);
        country.setGnp(2715518.0);
        country.setGnpOld(2600000.0);
        country.setLocalName("France");
        country.setGovernmentForm("Republic");
        country.setHeadOfState("Emmanuel Macron");
        country.setCapital(2974);
        country.setCode2("FR");

        // Call toString(
        String result = country.toString();

        // Verify important fields appear in the output
        assertTrue(result.contains("France"));
        assertTrue(result.contains("Europe"));
        assertTrue(result.contains("Republic"));

        // Check general structure
        assertTrue(result.startsWith("Country{"));
        assertTrue(result.endsWith("}"));
    }

    // ---------------------------------------------------------------------
    // Test: Ensure default constructor assigns default/null values correctly
    // ---------------------------------------------------------------------
    @Test
    void testDefaultValues() {
        // Default object (no values set)
        Country country = new Country();

        // All string fields should be null
        assertNull(country.getCode());
        assertNull(country.getCountryName());
        assertNull(country.getContinent());
        assertNull(country.getRegion());

        // Numeric fields that default to primitive types
        assertEquals(0.0, country.getSurfaceArea());
        assertNull(country.getIndepYear()); // Wrapper Integer → defaults to null
        assertEquals(0L, country.getPopulation()); // long defaults to 0
        assertNull(country.getLifeExpectancy());
        assertNull(country.getGnp());
        assertNull(country.getGnpOld());

        // More string fields should also be null
        assertNull(country.getLocalName());
        assertNull(country.getGovernmentForm());
        assertNull(country.getHeadOfState());
        assertNull(country.getCapital());
        assertNull(country.getCode2());
    }
}
