package com.example.iso8583poc.service.fleet_data_decoder;

import com.example.iso8583poc.domain.preauth.CommanderPreAuthRequest;

public class FleetDataDecoderService {

    public static void extractFleetDataTwoFromPreAuthRequest(CommanderPreAuthRequest preAuthRequest, String dataElement63) {
        int startIndex = 0;
        //var driverData =  dataElement63.substring(startIndex, startIndex+8);
        var driverData = dataElement63.substring(startIndex, startIndex + 15);
        //String driverId = driverData.substring(1, 7);
        String driverId = driverData;
        startIndex += 15;

        //var vehicleData =  dataElement63.substring(startIndex, startIndex+8);
        var vehicleData = dataElement63.substring(startIndex, startIndex + 10);
        //String vehicleId = vehicleData.substring(1, 7);
        String vehicleId = vehicleData;
        startIndex += 10;

        var odoMeterData = dataElement63.substring(startIndex, startIndex + 8);
        String odoMeter = odoMeterData.substring(1, 7);
        startIndex += 8;

        System.out.println("DriverID : " + driverId + ", VehicleID : " + vehicleId + ", Odometer : " + odoMeterData);
        preAuthRequest.setDriverCode(driverId).setVehicleCode(vehicleId).setOdoMeter(Integer.valueOf(odoMeter).toString());
    }
}
