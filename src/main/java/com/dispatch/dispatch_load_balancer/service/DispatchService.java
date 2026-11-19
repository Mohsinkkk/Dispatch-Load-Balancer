
package com.dispatch.dispatch_load_balancer.service;

import com.dispatch.dispatch_load_balancer.dto.DispatchPlanResponse;
import com.dispatch.dispatch_load_balancer.dto.VehicleDispatchDTO;
import com.dispatch.dispatch_load_balancer.entity.DeliveryOrder;
import com.dispatch.dispatch_load_balancer.entity.Vehicle;
import com.dispatch.dispatch_load_balancer.enums.Priority;
import com.dispatch.dispatch_load_balancer.repository.DeliveryOrderRepository;
import com.dispatch.dispatch_load_balancer.repository.VehicleRepository;
import com.dispatch.dispatch_load_balancer.util.DistanceCalculator;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class DispatchService {

    private final DeliveryOrderRepository orderRepository;
    private final VehicleRepository vehicleRepository;

    public DispatchService(
            DeliveryOrderRepository orderRepository,
            VehicleRepository vehicleRepository) {

        this.orderRepository = orderRepository;
        this.vehicleRepository = vehicleRepository;
    }

    // Save delivery orders
    public List<DeliveryOrder> saveOrders(List<DeliveryOrder> orders) {
        return orderRepository.saveAll(orders);
    }

    // Save vehicles
    public List<Vehicle> saveVehicles(List<Vehicle> vehicles) {
        return vehicleRepository.saveAll(vehicles);
    }

    // Get all delivery orders
    public List<DeliveryOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get all vehicles
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    // Generate dispatch plan
    public DispatchPlanResponse generateDispatchPlan() {

        List<DeliveryOrder> orders =
                new ArrayList<>(orderRepository.findAll());

        List<Vehicle> vehicles =
                vehicleRepository.findAll();

        // Sort orders: HIGH -> MEDIUM -> LOW
        orders.sort(
                Comparator.comparingInt(
                        order -> getPriorityRank(order.getPriority())
                )
        );

        // Create route states for all vehicles
        List<VehicleRouteState> routeStates = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            routeStates.add(new VehicleRouteState(vehicle));
        }

        List<DeliveryOrder> unassignedOrders = new ArrayList<>();

        // Assign each order to the nearest eligible vehicle
        for (DeliveryOrder order : orders) {

            VehicleRouteState bestVehicle = null;
            double shortestDistance = Double.MAX_VALUE;

            for (VehicleRouteState state : routeStates) {

                double remainingCapacity =
                        state.vehicle.getCapacity() - state.totalLoad;

                // Skip vehicle if capacity is insufficient
                if (remainingCapacity < order.getPackageWeight()) {
                    continue;
                }

                // Calculate distance from vehicle's current route position
                double distance = DistanceCalculator.calculateDistance(
                        state.currentLatitude,
                        state.currentLongitude,
                        order.getLatitude(),
                        order.getLongitude()
                );

                // Select the vehicle with minimum distance
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    bestVehicle = state;
                }
            }

            // No suitable vehicle found
            if (bestVehicle == null) {
                unassignedOrders.add(order);
                continue;
            }

            // Assign order to selected vehicle
            bestVehicle.assignedOrders.add(order);

            bestVehicle.totalLoad += order.getPackageWeight();
            bestVehicle.totalDistance += shortestDistance;

            // Update vehicle's last delivery location
            bestVehicle.currentLatitude = order.getLatitude();
            bestVehicle.currentLongitude = order.getLongitude();
        }

        // Convert route states into response DTOs
        List<VehicleDispatchDTO> dispatchPlan = new ArrayList<>();

        for (VehicleRouteState state : routeStates) {

            VehicleDispatchDTO dto = new VehicleDispatchDTO(
                    state.vehicle.getVehicleId(),
                    state.totalLoad,
                    String.format(
                            Locale.US,
                            "%.2f km",
                            state.totalDistance
                    ),
                    state.assignedOrders
            );

            dispatchPlan.add(dto);
        }

        return new DispatchPlanResponse(
                dispatchPlan,
                unassignedOrders
        );
    }

    // Priority ranking
    private int getPriorityRank(Priority priority) {

        if (priority == Priority.HIGH) {
            return 1;
        }

        if (priority == Priority.MEDIUM) {
            return 2;
        }

        return 3;
    }

    // Temporary route information for each vehicle
    private static class VehicleRouteState {

        private final Vehicle vehicle;

        private double currentLatitude;
        private double currentLongitude;

        private double totalLoad = 0.0;
        private double totalDistance = 0.0;

        private final List<DeliveryOrder> assignedOrders =
                new ArrayList<>();

        private VehicleRouteState(Vehicle vehicle) {

            this.vehicle = vehicle;

            this.currentLatitude = vehicle.getCurrentLatitude();
            this.currentLongitude = vehicle.getCurrentLongitude();
        }
    }
}
