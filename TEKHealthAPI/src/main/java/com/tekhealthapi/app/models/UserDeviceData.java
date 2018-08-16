/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tekhealthapi.app.models;

import java.sql.Timestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author pmadishe
 */
@Document
public class UserDeviceData {
    @Id
    String deviceDataId;
    String patientId;
    String deviceName;
    String deviceId;
    String attributeType;
    double attributeValue;
    String unitOfMeasurement;
    String evaluatedTimeStamp;
    String status;//to know whether data is pushed to salesforce or not values 1-Cumulosity, 2-Salesforce
    Timestamp createdTimestamp;//current datetime
    Timestamp updatedTimestamp;//status updated datetime -- data to salesforce
}
