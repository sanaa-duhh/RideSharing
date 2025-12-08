package com.sanaa.RideShare.controller;

import com.sanaa.RideShare.model.Ride;
import com.sanaa.RideShare.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/rides")
public class UserController {
    private final RideService rideService;
    public UserController(RideService rideService) { this.rideService = rideService; }

    @GetMapping
    public ResponseEntity<List<Ride>> getMyRides(Authentication auth) {
        String username = (String) auth.getPrincipal();
        return ResponseEntity.ok(rideService.getUserRides(username));
    }
}
