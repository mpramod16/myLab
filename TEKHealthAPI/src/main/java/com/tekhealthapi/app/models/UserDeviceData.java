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
    Timestamp evaluatedTimeStamp;
    String status;//to know whether data is pushed to salesforce or not values 1-Cumulosity, 2-Salesforce
    Timestamp createdTimestamp;//current datetime
    Timestamp updatedTimestamp;//status updated datetime -- data to salesforce


    public String getDeviceDataId() {
        return deviceDataId;
    }

    public void setDeviceDataId(String deviceDataId) {
        this.deviceDataId = deviceDataId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public double getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(double attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public Timestamp getEvaluatedTimeStamp() {
        return evaluatedTimeStamp;
    }

    public void setEvaluatedTimeStamp(Timestamp evaluatedTimeStamp) {
        this.evaluatedTimeStamp = evaluatedTimeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Timestamp getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(Timestamp updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }


}
