package com.api.parkingcontrol.repositories;

import com.api.parkingcontrol.models.ParkingSpotModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpotModel, Long> {

    Boolean existsByLicensePlateCar(String licensePlateCar);

    Boolean existsByParkingSpotNumber(String parkingSpotNumber);

    Boolean existsByApartmentAndBlock(String apartment, String block);
}
