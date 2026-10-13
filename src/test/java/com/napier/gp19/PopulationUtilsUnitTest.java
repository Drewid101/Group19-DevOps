package com.napier.gp19;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PopulationUtils.calculatePopulationValues().
 * These tests validate that population calculations behave correctly
 * with normal, null, and edge-case input values.
 */
class PopulationUtilsUnitTest {

    @Test
    void testCalculatePopulationValues_NormalCase() {
        // Arrange: Typical case with valid population, GNP (as city population),
        // and old GNP (as non-city population)
        Country country = new Country();
        country.setPopulation(1000);
        country.setGnp(400.0);
        country.setGnpOld(600.0);

        //Act
        PopulationUtils.PopValues values = PopulationUtils.calculatePopulationValues(country);

        // Assert: All fields should match expected values
        assertEquals(1000, values.total());
        assertEquals(400, values.city());
        assertEquals(600, values.nonCity());
        assertEquals(40.0, values.cityPercent(), 0.001);
        assertEquals(60.0, values.nonCityPercent(), 0.001);
    }

    @Test
    void testCalculatePopulationValues_WithNullGnp() {
        // Arrange: GNP is null, which PopulationUtils should treat as 0
        Country country = new Country();
        country.setPopulation(800);
        country.setGnp(null);
        country.setGnpOld(400.0);

        //Act
        PopulationUtils.PopValues values = PopulationUtils.calculatePopulationValues(country);

        // Assert: City population becomes 0, non-city from GNP old
        assertEquals(800, values.total());
        assertEquals(0, values.city());
        assertEquals(400, values.nonCity());
        assertEquals(0.0, values.cityPercent(), 0.001);
        assertEquals(50.0, values.nonCityPercent(), 0.001);
    }

    @Test
    void testCalculatePopulationValues_WithNullGnpOld() {
        // Arrange: Old GNP is null, meaning non-city population = 0
        Country country = new Country();
        country.setPopulation(500);
        country.setGnp(200.0);
        country.setGnpOld(null);

        // Act
        PopulationUtils.PopValues values = PopulationUtils.calculatePopulationValues(country);

        // Assert
        assertEquals(500, values.total());
        assertEquals(200, values.city());
        assertEquals(0, values.nonCity());
        assertEquals(40.0, values.cityPercent(), 0.001);
        assertEquals(0.0, values.nonCityPercent(), 0.001);
    }

    @Test
    void testCalculatePopulationValues_ZeroPopulation() {
        // Arrange: Total population is zero (edge case)
        Country country = new Country();
        country.setPopulation(0);
        country.setGnp(100.0);
        country.setGnpOld(50.0);

        // Act
        PopulationUtils.PopValues values = PopulationUtils.calculatePopulationValues(country);

        // Assert: Percentages should be zero to avoid division-by-zero
        assertEquals(0, values.total());
        assertEquals(100, values.city());
        assertEquals(50, values.nonCity());
        assertEquals(0.0, values.cityPercent(), 0.001);
        assertEquals(0.0, values.nonCityPercent(), 0.001);
    }

    @Test
    void testCalculatePopulationValues_AllNulls() {
        // Arrange: Everything is null or zero
        Country country = new Country();
        country.setPopulation(0);
        country.setGnp(null);
        country.setGnpOld(null);

        // Act
        PopulationUtils.PopValues values = PopulationUtils.calculatePopulationValues(country);

        // Assert: All results should default safely to zero
        assertEquals(0, values.total());
        assertEquals(0, values.city());
        assertEquals(0, values.nonCity());
        assertEquals(0.0, values.cityPercent(), 0.001);
        assertEquals(0.0, values.nonCityPercent(), 0.001);
    }
}
