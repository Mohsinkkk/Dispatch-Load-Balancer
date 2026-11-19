
package com.dispatch.dispatch_load_balancer.dto;

import com.dispatch.dispatch_load_balancer.entity.DeliveryOrder;
import java.util.List;

public class VehicleDispatchDTO {

    private String vehicleId;
    private Double totalLoad;
    private String totalDistance;
    private List<DeliveryOrder> assignedOrders;

    public VehicleDispatchDTO() {
    }

    public VehicleDispatchDTO(
            String vehicleId,
            Double totalLoad,
            String totalDistance,
            List<DeliveryOrder> assignedOrders) {

        this.vehicleId = vehicleId;
        this.totalLoad = totalLoad;
        this.totalDistance = totalDistance;
        this.assignedOrders = assignedOrders;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Double getTotalLoad() {
        return totalLoad;
    }

    public void setTotalLoad(Double totalLoad) {
        this.totalLoad = totalLoad;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public List<DeliveryOrder> getAssignedOrders() {
        return assignedOrders;
    }

    public void setAssignedOrders(List<DeliveryOrder> assignedOrders) {
        this.assignedOrders = assignedOrders;
    }
}
