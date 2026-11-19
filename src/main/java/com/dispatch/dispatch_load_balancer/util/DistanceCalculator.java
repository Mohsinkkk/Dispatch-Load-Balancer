
package com.dispatch.dispatch_load_balancer.util;

public class DistanceCalculator {

    // Earth's radius in kilometers
    private static final double EARTH_RADIUS = 6371.0;

    public static double calculateDistance(
            double lat1,
            double lon1,
            double lat2,
            double lon2) {

        // Convert latitude and longitude differences into radians
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        // Haversine formula
        double a = Math.sin(latDistance / 2)
                * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );

        // Return distance in kilometers
        return EARTH_RADIUS * c;
    }
}
