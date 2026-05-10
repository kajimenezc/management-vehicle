package com.unir.management.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.unir.management.dto.Rental;
import com.unir.management.dto.RentalRequest;
import com.unir.management.dto.RentalResponse;
import com.unir.management.persistence.model.Vehicle;
import com.unir.management.shared.exception.ResourceNotFoundException;

@Service
public class RentalService {

    private final VehicleService vehicleService;
    private final Map<Long, Rental> rentals = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public RentalService(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public RentalResponse registerRental(RentalRequest request) {
        // Validate vehicle availability
        Vehicle vehicle = vehicleService.getVehicleById(request.getVehicleId());
        if (!"disponible".equals(vehicle.getState())) {
            throw new IllegalStateException("Vehicle is not available for rental");
        }

        // Create rental
        Rental rental = new Rental(
            idGenerator.getAndIncrement(),
            request.getVehicleId(),
            request.getUserId(),
            request.getStartDate(),
            request.getEndDate(),
            "confirmed"
        );
        rentals.put(rental.getId(), rental);

        // Update vehicle status
        vehicle.setState("no_disponible");
        vehicle.setDateReturn(request.getEndDate());
        vehicleService.updateVehicle(vehicle.getId(), vehicle);

        return mapToResponse(rental);
    }

    public void cancelRental(Long id) {
        Rental rental = rentals.get(id);
        if (rental == null) {
            throw new ResourceNotFoundException("Rental not found with id " + id);
        }

        // Update vehicle status back to available
        Vehicle vehicle = vehicleService.getVehicleById(rental.getVehicleId());
        vehicle.setState("disponible");
        vehicle.setDateReturn(null);
        vehicleService.updateVehicle(vehicle.getId(), vehicle);

        // Remove rental
        rentals.remove(id);
    }

    public List<RentalResponse> getAllRentals() {
        return rentals.values().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public RentalResponse getRentalById(Long id) {
        Rental rental = rentals.get(id);
        if (rental == null) {
            throw new ResourceNotFoundException("Rental not found with id " + id);
        }
        return mapToResponse(rental);
    }

    private RentalResponse mapToResponse(Rental rental) {
        return new RentalResponse(
            rental.getId(),
            rental.getVehicleId(),
            rental.getUserId(),
            rental.getStartDate(),
            rental.getEndDate(),
            rental.getStatus()
        );
    }
}