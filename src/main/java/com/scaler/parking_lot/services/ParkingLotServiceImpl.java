package com.scaler.parking_lot.services;

import com.scaler.parking_lot.exceptions.InvalidParkingLotException;
import com.scaler.parking_lot.models.*;
import com.scaler.parking_lot.respositories.ParkingLotRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ParkingLotServiceImpl  implements ParkingLotService{
    private ParkingLotRepository parkingLotRepository;

    public ParkingLotServiceImpl(ParkingLotRepository parkingLotRepository){
        this.parkingLotRepository = parkingLotRepository;
    }
    public Map<ParkingFloor, Map<String, Integer>> getParkingLotCapacity(long parkingLotId, List<Long> parkingFloors, List<VehicleType> vehicleTypes) throws InvalidParkingLotException {
        ParkingLot parkingLot = parkingLotRepository.getParkingLotById(parkingLotId).orElseThrow(()-> new InvalidParkingLotException("Invalid ParkingLot id"));

        Map<ParkingFloor, Map<String, Integer>> capacityMap = new HashMap<>();
        List<ParkingFloor> parkingFloorList = new ArrayList<>();
        if(parkingFloors==null || parkingFloors.isEmpty())
            parkingFloorList  = parkingLot.getParkingFloors();
        else{
            Map<Long,ParkingFloor> parkingFloorMap = new HashMap<>();
            for(ParkingFloor floor:parkingLot.getParkingFloors())
                parkingFloorMap.put(floor.getId(), floor);
            for(Long parkingFloorId: parkingFloors){
                if(parkingFloorMap.containsKey(parkingFloorId))
                    parkingFloorList.add(parkingFloorMap.get(parkingFloorId));
            }
        }

        if(vehicleTypes==null || vehicleTypes.isEmpty())
            vehicleTypes = Arrays.asList(VehicleType.values());

        for(ParkingFloor  floor:  parkingFloorList){
            if(floor.getStatus()==FloorStatus.OPERATIONAL){
                Map<String, Integer> map = new HashMap<>();
                for(VehicleType vehicleType:vehicleTypes){
                    map.put(vehicleType.name(),(int)floor.getSpots().stream().filter(parkingSpot -> parkingSpot.getSupportedVehicleType()==vehicleType && parkingSpot.getStatus()==ParkingSpotStatus.AVAILABLE).count());
                }
                capacityMap.put(floor,map);
            }
        }
        return capacityMap;
    }
}
