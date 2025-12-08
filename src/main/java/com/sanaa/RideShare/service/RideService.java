package com.sanaa.RideShare.service;

import com.sanaa.RideShare.dto.CreateRideRequest;
import com.sanaa.RideShare.exception.BadRequestException;
import com.sanaa.RideShare.exception.NotFoundException;
import com.sanaa.RideShare.model.Ride;
import com.sanaa.RideShare.model.User;
import com.sanaa.RideShare.repository.RideRepository;
import com.sanaa.RideShare.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepo;
    private final UserRepository userRepo;

    public RideService(RideRepository rideRepo, UserRepository userRepo) {
        this.rideRepo = rideRepo;
        this.userRepo = userRepo;
    }

    public Ride createRide(String username, CreateRideRequest req) {
        User u = userRepo.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        Ride ride = new Ride(u.getId(), req.getPickupLocation(), req.getDropLocation());
        return rideRepo.save(ride);
    }

    public List<Ride> getUserRides(String username) {
        User u = userRepo.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        return rideRepo.findByUserId(u.getId());
    }

    public List<Ride> getPendingRequests() {
        return rideRepo.findByStatus("REQUESTED");
    }

    public Ride acceptRide(String driverUsername, String rideId) {
        User driver = userRepo.findByUsername(driverUsername).orElseThrow(() -> new NotFoundException("Driver not found"));
        Ride ride = rideRepo.findById(rideId).orElseThrow(() -> new NotFoundException("Ride not found"));
        if (!"REQUESTED".equals(ride.getStatus())) throw new BadRequestException("Ride not REQUESTED");
        ride.setDriverId(driver.getId());
        ride.setStatus("ACCEPTED");
        return rideRepo.save(ride);
    }

    public Ride completeRide(String username, String rideId) {
        User actor = userRepo.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        Ride ride = rideRepo.findById(rideId).orElseThrow(() -> new NotFoundException("Ride not found"));
        if (!"ACCEPTED".equals(ride.getStatus())) throw new BadRequestException("Ride not ACCEPTED");
        // allow only user who requested or driver who accepted
        if (!actor.getId().equals(ride.getUserId()) && !actor.getId().equals(ride.getDriverId())) {
            throw new BadRequestException("Not authorized to complete this ride");
        }
        ride.setStatus("COMPLETED");
        return rideRepo.save(ride);
    }
}
