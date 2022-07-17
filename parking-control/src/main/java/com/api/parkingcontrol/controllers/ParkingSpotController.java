package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dtos.ParkingSpotForm;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotForm parkingSpotForm) {

        if (parkingSpotService.existsByLicensePlateCar(parkingSpotForm.getLicensePlateCar())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License plate car is already in use!");
        }
        if (parkingSpotService.existsByParkingSpotNumber(parkingSpotForm.getParkingSpotNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
        }
        if (parkingSpotService.existsByApartmentAndBlock(parkingSpotForm.getApartment(), parkingSpotForm.getBlock())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Conflict: Parking Spot is already registered for Apartment/Block!");
        }
        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotForm, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpots(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable page) {
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.getAll(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") Long id) {
        Optional<ParkingSpotModel> optionalParkingSpotModel = parkingSpotService.getById(id);
        if (optionalParkingSpotModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalParkingSpotModel.get());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParkingSpot(@PathVariable(value = "id") Long id) {
        Optional<ParkingSpotModel> optionalParkingSpotModel = parkingSpotService.getById(id);
        if (optionalParkingSpotModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        parkingSpotService.delete(optionalParkingSpotModel.get());
        return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") Long id,
                                                    @RequestBody @Valid ParkingSpotForm parkingSpotForm) {
        Optional<ParkingSpotModel> optionalParkingSpotModel = parkingSpotService.getById(id);
        if (optionalParkingSpotModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        ParkingSpotModel parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotForm, parkingSpotModel);
        parkingSpotModel.setId(optionalParkingSpotModel.get().getId());
        parkingSpotModel.setRegistrationDate(optionalParkingSpotModel.get().getRegistrationDate());
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));
    }

}
