/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tekhealthapi.app.jobs;

import com.tekhealthapi.app.controller.UserDeviceDataController;
import com.tekhealthapi.app.controller.UserDevicesController;
import com.tekhealthapi.app.models.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
   
   @Scheduled(initialDelay = 60000, fixedRate = 300000)//fixedRate in milliseconds 60000 => 1min
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
        /*Interval of the cron job - 60 Seconds
         2 dates. enddate=currnetsysdate. start date = sysdate-60 sec;
         */
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
        dtFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        final Calendar today= Calendar.getInstance();
        today.add(today.DATE,+1);//change to 0 after demo
        final Calendar yesterday=Calendar.getInstance();
        yesterday.add(yesterday.DATE,0);
        final String CUMULOSITY_URL = c8yUrl+device.getDeviceId()+"&dateFrom="+dtFormat.format(yesterday.getTime())+"&dateTo="+dtFormat.format(today.getTime());
        //final String CUMULOSITY_URL = c8yUrl+device.getDeviceId()+"&dateFrom="+dtFormat.format(yesterday.getTime())+"&dateTo="+"2018-09-22";// for demo purpose
        System.out.println(" CUMULOSITY_URL>>>>"+CUMULOSITY_URL);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(
        new BasicAuthorizationInterceptor(c8yUsername, c8yPwd));
        C8YData c8yData =  restTemplate.getForObject(CUMULOSITY_URL, C8YData.class);
        //put c8yData into DeviceData table
        System.out.println(" got response from cumulocity -----------------");
        for (Measurements measurements:c8yData.getMeasurements() ) {
            C8Y_HealthMonitoring c8yHealthMonData = measurements.getC8yHealthMonitoring();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
            Date date = new Date();
            Date date1 = new Date();
            date1.setMinutes(date1.getMinutes() - 2);
            Timestamp currentTimeStamp = new Timestamp(today.getTimeInMillis());
            Timestamp prevTimeStamp = new Timestamp(yesterday.getTimeInMillis());
            System.out.println("CurrentTimeStamp>>>"+dateFormat.format(currentTimeStamp));
            System.out.println("prevTimeStamp>>>"+dateFormat.format(prevTimeStamp));
            Timestamp timeStamp = measurements.getTime();
            //C8Y_HealthMonitoring c8yHealthMonData= c8yData.getMeasurements().get(0).getC8yHealthMonitoring();
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        Date date = new Date();
//            Timestamp timeStamp = c8yData.getMeasurements().get(0).getTime();
            if (c8yHealthMonData.getD() != null && timeStamp.before(currentTimeStamp) && timeStamp.after(prevTimeStamp)) {
                UserDeviceData userDD = new UserDeviceData();
                userDD.setAttributeType("STEPCOUNT");
                userDD.setAttributeValue(Double.parseDouble(c8yHealthMonData.getD().getValue()));
                userDD.setDeviceId(device.getDeviceId());
                userDD.setPatientId(device.getPatientId());
                userDD.setUnitOfMeasurement(c8yHealthMonData.getD().getUnit());
                userDD.setCreatedTimestamp(timeStamp);
                userDD.setDeviceName(device.getDeviceName());
                userDD.setEvaluatedTimeStamp(timeStamp);

                userDD.setStatus("CUMULOCITY");
                userDD.setUpdatedTimestamp(timeStamp);

                if (this.syncToSF(device.getPatientId(), userDD)) {
                    userDD.setStatus("SALESFORCE");
                }
                userDeviceDataController.addUserDeviceData(userDD);
            }
            if (c8yHealthMonData.getP() != null && timeStamp.before(currentTimeStamp) && timeStamp.after(prevTimeStamp)) {
                UserDeviceData userDD = new UserDeviceData();
                userDD.setAttributeType("BLOOD PRESSURE");
                userDD.setAttributeValue(Double.parseDouble(c8yHealthMonData.getP().getValue()));
                userDD.setDeviceId(device.getDeviceId());
                userDD.setPatientId(device.getPatientId());
                userDD.setUnitOfMeasurement(c8yHealthMonData.getP().getUnit());
                userDD.setCreatedTimestamp(timeStamp);
                userDD.setDeviceName(device.getDeviceName());
                userDD.setEvaluatedTimeStamp(timeStamp);

                userDD.setStatus("CUMULOCITY");
                userDD.setUpdatedTimestamp(timeStamp);

                if (this.syncToSF(device.getPatientId(), userDD)) {
                    userDD.setStatus("SALESFORCE");
                }
                userDeviceDataController.addUserDeviceData(userDD);
            }
            if (c8yHealthMonData.getH() != null && timeStamp.before(currentTimeStamp) && timeStamp.after(prevTimeStamp)) {
                UserDeviceData userDD = new UserDeviceData();
                userDD.setAttributeType("HEARTRATE");
                userDD.setAttributeValue(Double.parseDouble(c8yHealthMonData.getH().getValue()));
                userDD.setDeviceId(device.getDeviceId());
                userDD.setPatientId(device.getPatientId());
                userDD.setUnitOfMeasurement(c8yHealthMonData.getH().getUnit());
                userDD.setCreatedTimestamp(timeStamp);
                userDD.setDeviceName(device.getDeviceName());
                userDD.setEvaluatedTimeStamp(timeStamp);

                userDD.setStatus("CUMULOCITY");
                userDD.setUpdatedTimestamp(timeStamp);
                if (this.syncToSF(device.getPatientId(), userDD)) {
                    userDD.setStatus("SALESFORCE");
                }
                userDeviceDataController.addUserDeviceData(userDD);
            }
        }
        //userDeviceDataController.addUserDeviceData(userDD);
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
        params.put("AttributeType", data.getAttributeType());
        System.out.println("attribute Value ----"+data.getAttributeValue());
        int index=Double.toString(data.getAttributeValue()).indexOf('.');
        if(index > 0){
            String[] str=Double.toString(data.getAttributeValue()).split("[.]");
            params.put("AttributeValue",str[0]);
            System.out.println("AttributeValue without ."+ str[0]);
            if(data.getAttributeType()=="BLOOD PRESSURE"){
                params.put("SecondaryAttributeValue",str[1]);
                System.out.println("SecondaryAttributeValue "+ str[1]);
            }
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        params.put("DataCollectedTimestamp", dateFormat.format(data.getCreatedTimestamp()));
        System.out.println("DataCollectedTimestamp>>>>"+dateFormat.format(data.getCreatedTimestamp()));
        params.put("DeviceID", data.getDeviceId());
        params.put("DeviceName", data.getDeviceName());
        System.out.println("data.getAttributeType()===="+data.getAttributeType());

        HttpEntity request = new HttpEntity(params, headers);
        ResponseEntity<String> response=null;
        System.out.println("Request Sent>>>>>>>>"+ request.toString()+"");
//        restTemplateForPatientInfo.postForEntity(SALESFORCEDEVICEINFO_URL,request,String.class);
        try{
            response = restTemplateForPatientInfo.postForEntity(SALESFORCEDEVICEINFO_URL, request, String.class);
            System.out.println("response----"+response.getStatusCode());
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }


        restTemplateForPatientInfo=null;

        if( response != null && response.getStatusCode().is2xxSuccessful()){
            return true;
        }else{
            return false;
        }

    }
    
   
   
}
