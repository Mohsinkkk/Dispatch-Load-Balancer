
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

    public List<DeliveryOrder> saveOrders(List<DeliveryOrder> orders) {
        return orderRepository.saveAll(orders);
    }

    public List<Vehicle> saveVehicles(List<Vehicle> vehicles) {
        return vehicleRepository.saveAll(vehicles);
    }

    public List<DeliveryOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public DispatchPlanResponse generateDispatchPlan() {

        List<DeliveryOrder> orders =
                new ArrayList<>(orderRepository.findAll());

        List<Vehicle> vehicles =
                vehicleRepository.findAll();

        orders.sort(
                Comparator.comparingInt(
                        order -> getPriorityRank(order.getPriority())
                )
        );

        List<VehicleRouteState> routeStates = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            routeStates.add(new VehicleRouteState(vehicle));
        }

        List<DeliveryOrder> unassignedOrders = new ArrayList<>();

        for (DeliveryOrder order : orders) {

            VehicleRouteState bestVehicle = null;
            double shortestDistance = Double.MAX_VALUE;

            for (VehicleRouteState state : routeStates) {

                double remainingCapacity =
                        state.vehicle.getCapacity() - state.totalLoad;

                if (remainingCapacity < order.getPackageWeight()) {
                    continue;
                }

                double distance = DistanceCalculator.calculateDistance(
                        state.currentLatitude,
                        state.currentLongitude,
                        order.getLatitude(),
                        order.getLongitude()
                );

                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    bestVehicle = state;
                }
            }

            if (bestVehicle == null) {
                unassignedOrders.add(order);
                continue;
            }

            bestVehicle.assignedOrders.add(order);

            bestVehicle.totalLoad += order.getPackageWeight();
            bestVehicle.totalDistance += shortestDistance;

            bestVehicle.currentLatitude = order.getLatitude();
            bestVehicle.currentLongitude = order.getLongitude();
        }

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

    private int getPriorityRank(Priority priority) {

        if (priority == Priority.HIGH) {
            return 1;
        }

        if (priority == Priority.MEDIUM) {
            return 2;
        }

        return 3;
    }

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
