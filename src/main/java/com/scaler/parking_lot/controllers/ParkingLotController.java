package com.scaler.parking_lot.controllers;

import com.scaler.parking_lot.dtos.GetParkingLotCapacityRequestDto;
import com.scaler.parking_lot.dtos.GetParkingLotCapacityResponseDto;
import com.scaler.parking_lot.dtos.Response;
import com.scaler.parking_lot.dtos.ResponseStatus;
import com.scaler.parking_lot.exceptions.GetParkingLotRequestValidationException;
import com.scaler.parking_lot.exceptions.InvalidParkingLotException;
import com.scaler.parking_lot.models.ParkingFloor;
import com.scaler.parking_lot.models.VehicleType;
import com.scaler.parking_lot.services.ParkingLotService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    public GetParkingLotCapacityResponseDto getParkingLotCapacity(GetParkingLotCapacityRequestDto getParkingLotCapacityRequestDto) {
        GetParkingLotCapacityResponseDto responseDto = new GetParkingLotCapacityResponseDto();
        try{
            responseDto.setCapacityMap(parkingLotService.getParkingLotCapacity(getParkingLotCapacityRequestDto.getParkingLotId(),getParkingLotCapacityRequestDto.getParkingFloorIds(),getParkingLotCapacityRequestDto.getVehicleTypes().stream().map(vehicleType -> VehicleType.valueOf(vehicleType)).collect(Collectors.toList())));
            responseDto.setResponse(new Response(ResponseStatus.SUCCESS,"Parking Lot Information Received Successfully"));
        } catch (InvalidParkingLotException e) {
            responseDto.setResponse(new Response(ResponseStatus.FAILURE,e.getMessage()));
        }
        return null;
    }

}
