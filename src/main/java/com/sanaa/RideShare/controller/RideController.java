package com.sanaa.RideShare.controller;

import com.sanaa.RideShare.dto.CreateRideRequest;
import com.sanaa.RideShare.model.Ride;
import com.sanaa.RideShare.service.RideService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rides")
public class RideController {

    private final RideService rideService;
    public RideController(RideService rideService) { this.rideService = rideService; }

    @PostMapping
    public ResponseEntity<Ride> createRide(@Valid @RequestBody CreateRideRequest req, Authentication auth) {
        String username = (String) auth.getPrincipal();
        Ride ride = rideService.createRide(username, req);
        return ResponseEntity.ok(ride);
    }

    @PostMapping("/{rideId}/complete")
    public ResponseEntity<Ride> completeRide(@PathVariable String rideId, Authentication auth) {
        String username = (String) auth.getPrincipal();
        Ride ride = rideService.completeRide(username, rideId);
        return ResponseEntity.ok(ride);
    }
}
