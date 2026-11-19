
package com.dispatch.dispatch_load_balancer.controller;

import com.dispatch.dispatch_load_balancer.dto.ApiResponse;
import com.dispatch.dispatch_load_balancer.dto.OrderRequest;
import com.dispatch.dispatch_load_balancer.dto.VehicleRequest;
import com.dispatch.dispatch_load_balancer.dto.DispatchPlanResponse;
import com.dispatch.dispatch_load_balancer.service.DispatchService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dispatch")
public class DispatchController {

    private final DispatchService dispatchService;

    public DispatchController(DispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }

    // API 1: Save delivery orders
    @PostMapping("/orders")
    public ResponseEntity<ApiResponse> saveOrders(
            @Valid @RequestBody OrderRequest request) {

        dispatchService.saveOrders(request.getOrders());

        return ResponseEntity.ok(
                new ApiResponse(
                        "Delivery orders accepted.",
                        "success"
                )
        );
    }

    // API 2: Save vehicles
    @PostMapping("/vehicles")
    public ResponseEntity<ApiResponse> saveVehicles(
            @Valid @RequestBody VehicleRequest request) {

        dispatchService.saveVehicles(request.getVehicles());

        return ResponseEntity.ok(
                new ApiResponse(
                        "Vehicle details accepted.",
                        "success"
                )
        );
    }

    // API 3: Generate dispatch plan
    @GetMapping("/plan")
    public ResponseEntity<DispatchPlanResponse> getDispatchPlan() {

        DispatchPlanResponse response =
                dispatchService.generateDispatchPlan();

        return ResponseEntity.ok(response);
    }
}
