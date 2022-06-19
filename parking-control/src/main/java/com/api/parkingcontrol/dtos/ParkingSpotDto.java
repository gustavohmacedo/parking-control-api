package com.api.parkingcontrol.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ParkingSpotDto {

    @NotBlank
    private String parkingSpotNumber;

    @NotBlank(message = "licensePlateCar cannot be blank")
    @Size(max = 7)
    private String licensePlateCar;

    @NotBlank(message = "brandCar cannot be blank")
    private String brandCar;

    @NotBlank(message = "modelCar cannot be blank")
    private String modelCar;

    @NotBlank(message = "colorCar cannot be blank")
    private String colorCar;

    @NotBlank(message = "responsibleName cannot be blank")
    private String responsibleName;

    @NotBlank(message = "apartment cannot be blank")
    private String apartment;

    @NotBlank(message = "block cannot be blank")
    private String block;


}
