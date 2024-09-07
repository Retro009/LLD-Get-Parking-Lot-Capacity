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
            validateRequestDto(getParkingLotCapacityRequestDto);
            List<VehicleType> vehicleTypes = new ArrayList<>();
            if(getParkingLotCapacityRequestDto.getVehicleTypes()!=null)
                vehicleTypes = getParkingLotCapacityRequestDto.getVehicleTypes().stream().map(vehicleType -> VehicleType.valueOf(vehicleType)).collect(Collectors.toList());
            responseDto.setCapacityMap(parkingLotService.getParkingLotCapacity(getParkingLotCapacityRequestDto.getParkingLotId(),getParkingLotCapacityRequestDto.getParkingFloorIds(),vehicleTypes));
            responseDto.setResponse(new Response(ResponseStatus.SUCCESS,"Parking Lot Information Received Successfully"));
        } catch (GetParkingLotRequestValidationException | InvalidParkingLotException e) {
            responseDto.setResponse(new Response(ResponseStatus.FAILURE,e.getMessage()));
        }
        return responseDto;
    }

    public void validateRequestDto(GetParkingLotCapacityRequestDto requestDto) throws GetParkingLotRequestValidationException {
        if(requestDto.getParkingLotId()<=0)
            throw new GetParkingLotRequestValidationException("Invalid Parking Lot Id");
    }

}
