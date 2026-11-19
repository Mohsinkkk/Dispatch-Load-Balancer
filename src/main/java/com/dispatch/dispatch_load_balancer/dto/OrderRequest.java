
package com.dispatch.dispatch_load_balancer.dto;

import com.dispatch.dispatch_load_balancer.entity.DeliveryOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class OrderRequest {

    @NotEmpty(message = "Orders list cannot be empty")
    private List<@Valid DeliveryOrder> orders;

    public List<DeliveryOrder> getOrders() { return orders; }
    public void setOrders(List<DeliveryOrder> orders) { this.orders = orders; }
}
