
package com.dispatch.dispatch_load_balancer.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistanceCalculatorTest {

    @Test
    void sameLocationShouldReturnZeroDistance() {

        double distance = DistanceCalculator.calculateDistance(
                28.6139, 77.2090,
                28.6139, 77.2090
        );

        assertEquals(0.0, distance, 0.001);
    }

    @Test
    void distanceShouldBePositiveForDifferentLocations() {

        double distance = DistanceCalculator.calculateDistance(
                28.6139, 77.2090,
                28.7041, 77.1025
        );

        assertTrue(distance > 0);
    }

    @Test
    void distanceShouldBeApproximatelyCorrect() {

        double distance = DistanceCalculator.calculateDistance(
                28.6139, 77.2090,
                28.7041, 77.1025
        );

        assertEquals(14.44, distance, 0.1);
    }
}
