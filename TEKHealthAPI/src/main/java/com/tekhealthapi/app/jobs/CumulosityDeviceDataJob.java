/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tekhealthapi.app.jobs;

import com.tekhealthapi.app.controller.UserDevicesController;
import com.tekhealthapi.app.models.C8YData;
import com.tekhealthapi.app.models.UserDevices;
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
    
    private static void syncDeviceData(UserDevices device){
        
        final String CUMULOSITY_URL ="https://teksystemspoc.cumulocity.com/measurement/measurements?fragmentType=c8y_HealthMonitoring&source=68350";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(
        new BasicAuthorizationInterceptor("skadhira@teksystems.com", "Tek@123"));
        C8YData c8yData =  restTemplate.getForObject(CUMULOSITY_URL, C8YData.class);
        LOG.info("Response:"+c8yData.toString());
        System.out.println("Response:"+c8yData.toString());
    }
   
   
}
