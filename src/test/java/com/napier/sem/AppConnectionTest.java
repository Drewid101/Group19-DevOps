package com.napier.sem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class AppConnectionTest {

    @ParameterizedTest(name = "Blank location case {index}")
    @NullAndEmptySource
    @ValueSource(strings = {" \t"})
    void connectRejectsBlankLocation(String location) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new App().connect(location, 0));

        assertEquals("Database location must not be blank.", error.getMessage());
    }

    @ParameterizedTest(name = "Negative delay case {index}")
    @ValueSource(ints = {-1, Integer.MIN_VALUE})
    void connectRejectsNegativeDelay(int delay) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new App().connect("localhost:33060", delay));

        assertEquals("Database retry delay must not be negative.", error.getMessage());
    }

    @ParameterizedTest(name = "Invalid numeric delay case {index}")
    @ValueSource(strings = {"invalid", "2147483648"})
    void mainRejectsInvalidNumericDelay(String delay) {
        assertThrows(NumberFormatException.class,
                () -> App.main(new String[]{"localhost:33060", delay}));
    }

    @Test
    void interruptionAbortsConnectionAndPreservesInterruptFlag() {
        boolean previouslyInterrupted = Thread.interrupted();
        try {
            Thread.currentThread().interrupt();
            App app = new App();

            IllegalStateException error = assertThrows(IllegalStateException.class,
                    () -> app.connect("localhost:33060", 1));

            assertInstanceOf(InterruptedException.class, error.getCause());
            assertEquals("Database connection interrupted.", error.getMessage());
            assertTrue(Thread.currentThread().isInterrupted());
            assertNull(app.getConnection());
        } finally {
            Thread.interrupted();
            if (previouslyInterrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
