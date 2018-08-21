/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tekhealthapi.app.jobs;

import com.tekhealthapi.app.controller.UserDeviceDataController;
import com.tekhealthapi.app.controller.UserDevicesController;
import com.tekhealthapi.app.models.C8YData;
import com.tekhealthapi.app.models.C8Y_HealthMonitoring;
import com.tekhealthapi.app.models.UserDeviceData;
import com.tekhealthapi.app.models.UserDevices;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Pramod M
 * 
 */
@Component
public class CumulosityDeviceDataJob {
   @Autowired
    private UserDevicesController userDevicesController;
    @Autowired
    private UserDeviceDataController userDeviceDataController;

    private static final Logger LOG = LoggerFactory.getLogger(CumulosityDeviceDataJob.class);
   
   @Scheduled(initialDelay = 1000, fixedRate = 60000)//fixedRate in milliseconds 60000 => 1min
    public void run() {
       List<UserDevices> deviceList = userDevicesController.getAllUserDevices();
        System.out.println("DeviceList is Empty?"+deviceList.isEmpty());
       if (!deviceList.isEmpty()){
        for (UserDevices device : deviceList) {
           this.syncDeviceData(device);
            }
        }
    }
    
    private void syncDeviceData(UserDevices device){
        
        final String CUMULOSITY_URL ="https://teksystemspoc.cumulocity.com/measurement/measurements?fragmentType=c8y_HealthMonitoring&source=68350";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(
        new BasicAuthorizationInterceptor("skadhira@teksystems.com", "Tek@123"));
        C8YData c8yData =  restTemplate.getForObject(CUMULOSITY_URL, C8YData.class);
        //put c8yData into DeviceData table
        UserDeviceData userDD=new UserDeviceData();
        C8Y_HealthMonitoring c8yHealthMonData= c8yData.getMeasurements().get(0).getC8yHealthMonitoring();
        if(c8yHealthMonData.getD() != null ){
            userDD.setAttributeType("STEPCOUNT");
            userDD.setAttributeValue(Double.parseDouble(c8yHealthMonData.getD().getValue()));
            userDD.setDeviceId(device.getDeviceId());
            userDD.setUnitOfMeasurement(c8yHealthMonData.getD().getUnit());
            userDD.setCreatedTimestamp(new Timestamp(new Date().getTime()));
            userDD.setDeviceName(device.getDeviceName());
            userDD.setEvaluatedTimeStamp(new Timestamp(new Date().getTime()));
//            userDD.setPatientId();
            userDD.setStatus("CUMULOSITY");
            userDD.setUpdatedTimestamp(new Timestamp(new Date().getTime()));
        }
        if(c8yHealthMonData.getH() != null){
            userDD.setAttributeType("HEARTRATE");
            userDD.setAttributeValue(Double.parseDouble(c8yHealthMonData.getH().getValue()));
            userDD.setDeviceId(device.getDeviceId());
            userDD.setUnitOfMeasurement(c8yHealthMonData.getH().getUnit());
            userDD.setCreatedTimestamp(new Timestamp(new Date().getTime()));
            userDD.setDeviceName(device.getDeviceName());
            userDD.setEvaluatedTimeStamp(new Timestamp(new Date().getTime()));
//            userDD.setPatientId();
            userDD.setStatus("CUMULOSITY");
            userDD.setUpdatedTimestamp(new Timestamp(new Date().getTime()));
        }

        userDeviceDataController.addUserDeviceData(userDD);
        LOG.info("Response:" + c8yData.toString());
        System.out.println("Response:"+c8yData.toString());
    }
   
   
}
