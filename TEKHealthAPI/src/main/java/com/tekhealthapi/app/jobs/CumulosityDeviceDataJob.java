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
import com.tekhealthapi.app.models.SalesForceTokenResponse;
import com.tekhealthapi.app.models.UserDeviceData;
import com.tekhealthapi.app.models.UserDevices;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static jdk.nashorn.internal.objects.NativeArray.map;
import static jdk.nashorn.internal.objects.NativeDebug.map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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

    @Value("${app.sf.token.url}")
    private String sfTokenUrl;
    @Value("${app.sf.devicedata.url}")
    private String sfDeviceDataUrl;
    @Value("${app.c8y.url}")
    private String c8yUrl;
    @Value("${app.c8y.username}")
    private String c8yUsername;
    @Value("${app.c8y.pwd}")
    private String c8yPwd;

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
        System.out.println(" in scheduler -----------------");
        final String CUMULOSITY_URL = c8yUrl+device.getDeviceId();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(
        new BasicAuthorizationInterceptor(c8yUsername, c8yPwd));
        C8YData c8yData =  restTemplate.getForObject(CUMULOSITY_URL, C8YData.class);
        //put c8yData into DeviceData table
        System.out.println(" got response from cumulocity -----------------");
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
            
            userDD.setStatus("CUMULOCITY");
            userDD.setUpdatedTimestamp(new Timestamp(new Date().getTime()));
            
            this.syncToSF(device.getPatientId(), userDD);
        }
        if(c8yHealthMonData.getH() != null){
            userDD.setAttributeType("HEARTRATE");
            userDD.setAttributeValue(Double.parseDouble(c8yHealthMonData.getH().getValue()));
            userDD.setDeviceId(device.getDeviceId());
            userDD.setUnitOfMeasurement(c8yHealthMonData.getH().getUnit());
            userDD.setCreatedTimestamp(new Timestamp(new Date().getTime()));
            userDD.setDeviceName(device.getDeviceName());
            userDD.setEvaluatedTimeStamp(new Timestamp(new Date().getTime()));
           
            userDD.setStatus("CUMULOCITY");
            userDD.setUpdatedTimestamp(new Timestamp(new Date().getTime()));
            if(this.syncToSF(device.getPatientId(), userDD)){
                userDD.setStatus("SALESFORCE");
            }
        }

        userDeviceDataController.addUserDeviceData(userDD);
        LOG.info("Response:" + c8yData.toString());
        System.out.println("Response:"+c8yData.toString());
    }
    
    
    private boolean syncToSF(String patientID, UserDeviceData data){
        
        final String SALESFORCETOKEN_URL =sfTokenUrl;
        
        System.out.println("Getting Token--------");
        RestTemplate restTemplateForTken = new RestTemplate();
        SalesForceTokenResponse tokenRes = restTemplateForTken.postForObject(SALESFORCETOKEN_URL, null, SalesForceTokenResponse.class);//.getForObject(SALESFORCETOKEN_URL, SalesForceTokenResponse.class);
        System.out.println("Token--------"+tokenRes.getAccess_token());
        restTemplateForTken = null;
        
        final String SALESFORCEDEVICEINFO_URL = sfDeviceDataUrl;
        RestTemplate restTemplateForPatientInfo = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        
        headers.set("Authorization", "Bearer "+tokenRes.getAccess_token());
        headers.setContentType(MediaType.APPLICATION_JSON);
       // headers.set("Content-Type", "application/json");
        Map<String, String> params = new HashMap<String, String>();
         System.out.println("patient id----"+patientID);
        params.put("PatientID", patientID);
        System.out.println("attribute type ----"+data.getAttributeType());
        params.put("AttributeType", "Heart Rate");
        System.out.println("attribute type ----"+data.getAttributeValue());
        params.put("AttributeValue", String.valueOf(data.getAttributeValue()));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = new Date();
        System.out.println("date ----"+dateFormat.format(date));
        params.put("DataCollectedTimestamp", dateFormat.format(date));
        params.put("DeviceID", "BPMonitor_1");
        params.put("DeviceName", "BP Monitor");
        HttpEntity request = new HttpEntity(params, headers);
        ResponseEntity<String> response = restTemplateForPatientInfo.postForEntity(SALESFORCEDEVICEINFO_URL, request, String.class);
        System.out.println("response----"+response.getBody());
        
        if(response.getStatusCode().is2xxSuccessful()){
            return true;
        }else{
            return false;
        }

    }
    
   
   
}
