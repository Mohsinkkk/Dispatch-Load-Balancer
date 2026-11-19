
package com.dispatch.dispatch_load_balancer.entity;

import com.dispatch.dispatch_load_balancer.enums.Priority;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "delivery_orders")
public class DeliveryOrder {

    @Id
    @NotBlank(message = "Order ID is required")
    @Column(name = "order_id")
    private String orderId;

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double longitude;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull
    @Positive(message = "Package weight must be greater than zero")
    private Double packageWeight;

    @NotNull(message = "Priority is required")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getPackageWeight() { return packageWeight; }
    public void setPackageWeight(Double packageWeight) { this.packageWeight = packageWeight; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
}
