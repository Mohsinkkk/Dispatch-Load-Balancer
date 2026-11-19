
package com.dispatch.dispatch_load_balancer.dto;

import com.dispatch.dispatch_load_balancer.entity.Vehicle;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class VehicleRequest {

    @NotEmpty(message = "Vehicles list cannot be empty")
    private List<@Valid Vehicle> vehicles;

    public List<Vehicle> getVehicles() { return vehicles; }
    public void setVehicles(List<Vehicle> vehicles) { this.vehicles = vehicles; }
}
