package com.example;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExampleTest {
    @Test
    void comparesValuesAndCollections() {
        List<Integer> values = List.of(2, 4, 6);
        assertEquals(6, values.get(2));
        assertIterableEquals(List.of(2, 4, 6), values);
    }

    @Test
    void checksContainmentAndBooleans() {
        List<Integer> values = List.of(2, 4, 6);
        assertTrue(values.contains(4));
        assertFalse(values.isEmpty());
    }

    @Nested
    class ErrorCases {
        @Test
        void checksFailures() {
            assertThrows(IllegalArgumentException.class, () -> {
                throw new IllegalArgumentException("bad input");
            });
        }
    }
}
