
package com.dispatch.dispatch_load_balancer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @NotBlank(message = "Vehicle ID is required")
    @Column(name = "vehicle_id")
    private String vehicleId;

    @NotNull
    @Positive(message = "Capacity must be greater than zero")
    private Double capacity;

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    @Column(name = "current_latitude")
    private Double currentLatitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    @Column(name = "current_longitude")
    private Double currentLongitude;

    @NotBlank(message = "Current address is required")
    @Column(name = "current_address")
    private String currentAddress;

    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }

    public Double getCapacity() { return capacity; }
    public void setCapacity(Double capacity) { this.capacity = capacity; }

    public Double getCurrentLatitude() { return currentLatitude; }
    public void setCurrentLatitude(Double currentLatitude) { this.currentLatitude = currentLatitude; }

    public Double getCurrentLongitude() { return currentLongitude; }
    public void setCurrentLongitude(Double currentLongitude) { this.currentLongitude = currentLongitude; }

    public String getCurrentAddress() { return currentAddress; }
    public void setCurrentAddress(String currentAddress) { this.currentAddress = currentAddress; }
}
