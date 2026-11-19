
package com.dispatch.dispatch_load_balancer.dto;

import com.dispatch.dispatch_load_balancer.entity.DeliveryOrder;
import java.util.List;

public class DispatchPlanResponse {

    private List<VehicleDispatchDTO> dispatchPlan;
    private List<DeliveryOrder> unassignedOrders;

    public DispatchPlanResponse() {
    }

    public DispatchPlanResponse(
            List<VehicleDispatchDTO> dispatchPlan,
            List<DeliveryOrder> unassignedOrders) {
        this.dispatchPlan = dispatchPlan;
        this.unassignedOrders = unassignedOrders;
    }

    public List<VehicleDispatchDTO> getDispatchPlan() {
        return dispatchPlan;
    }

    public void setDispatchPlan(List<VehicleDispatchDTO> dispatchPlan) {
        this.dispatchPlan = dispatchPlan;
    }

    public List<DeliveryOrder> getUnassignedOrders() {
        return unassignedOrders;
    }

    public void setUnassignedOrders(List<DeliveryOrder> unassignedOrders) {
        this.unassignedOrders = unassignedOrders;
    }
}
