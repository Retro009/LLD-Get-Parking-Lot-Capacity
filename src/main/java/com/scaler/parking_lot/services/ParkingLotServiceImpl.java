package com.scaler.parking_lot.services;

import com.scaler.parking_lot.exceptions.InvalidParkingLotException;
import com.scaler.parking_lot.models.ParkingFloor;
import com.scaler.parking_lot.models.ParkingLot;
import com.scaler.parking_lot.models.ParkingSpotStatus;
import com.scaler.parking_lot.models.VehicleType;
import com.scaler.parking_lot.respositories.ParkingLotRepository;

import java.util.*;

public class ParkingLotServiceImpl implements ParkingLotService{
    private ParkingLotRepository parkingLotRepository;

    public ParkingLotServiceImpl(ParkingLotRepository parkingLotRepository){
        this.parkingLotRepository = parkingLotRepository;
    }
    @Override
    public Map<ParkingFloor, Map<String, Integer>> getParkingLotCapacity(long parkingLotId, List<Long> parkingFloors, List<VehicleType> vehicleTypes) throws InvalidParkingLotException, InvalidParkingLotException {
        ParkingLot parkingLot = parkingLotRepository.getParkingLotById(parkingLotId).orElseThrow(()-> new InvalidParkingLotException("Invalid ParkingLot id"));

        Map<ParkingFloor, Map<String, Integer>> capacityMap = new HashMap<>();

        Set<Long> parkingFloorIds = (parkingFloors == null)? new HashSet<>(): new HashSet<>(parkingFloors);

        if(vehicleTypes.isEmpty() || vehicleTypes==null)
            vehicleTypes = Arrays.asList(VehicleType.values());

        for(ParkingFloor floor: parkingLot.getParkingFloors()){
            if(parkingFloorIds.size()>0 && !parkingFloorIds.contains(floor.getId()))
                continue;
            Map<String, Integer> vehicleMap = new HashMap<>();
            for(VehicleType vehicleType:vehicleTypes){
                vehicleMap.put(vehicleType.name(), (int) floor.getSpots().stream().filter(spot -> spot.getSupportedVehicleType().equals(vehicleType) && spot.getStatus().equals(ParkingSpotStatus.AVAILABLE)).count());
            }
            capacityMap.put(floor, vehicleMap);
        }

        return capacityMap;
    }
}
