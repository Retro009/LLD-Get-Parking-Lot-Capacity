package com.scaler.parking_lot.respositories;

import com.scaler.parking_lot.models.ParkingLot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParkingLotRepositoryImpl implements ParkingLotRepository{
    private List<ParkingLot> parkingLots = new ArrayList<>();
    @Override
    public Optional<ParkingLot> getParkingLotByGateId(long gateId) {
        return parkingLots.stream().filter(parkingLot -> parkingLot.getGates().stream().filter(gate -> gate.getId()==gateId).findAny().isPresent()).findFirst();
    }

    @Override
    public Optional<ParkingLot> getParkingLotById(long id) {
        return parkingLots.stream().filter(parkingLot -> parkingLot.getId()==id).findFirst();
    }

    @Override
    public ParkingLot save(ParkingLot parkingLot) {
        parkingLots.add(parkingLot);
        return parkingLot;
    }
}
