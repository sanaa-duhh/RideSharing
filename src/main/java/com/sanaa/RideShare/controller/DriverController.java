package com.sanaa.RideShare.controller;

import com.sanaa.RideShare.model.Ride;
import com.sanaa.RideShare.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/driver/rides")
public class DriverController {

    private final RideService rideService;
    public DriverController(RideService rideService) { this.rideService = rideService; }

    @GetMapping("/requests")
    public ResponseEntity<List<Ride>> getPending() {
        return ResponseEntity.ok(rideService.getPendingRequests());
    }

    @PostMapping("/{rideId}/accept")
    public ResponseEntity<Ride> acceptRide(@PathVariable String rideId, Authentication auth) {
        String username = (String) auth.getPrincipal();
        Ride ride = rideService.acceptRide(username, rideId);
        return ResponseEntity.ok(ride);
    }
}
