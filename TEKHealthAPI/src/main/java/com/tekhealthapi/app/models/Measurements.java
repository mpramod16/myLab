/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tekhealthapi.app.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author pmadishe
 */
@Document
@JsonIgnoreProperties(ignoreUnknown =  true)
public class Measurements {
    @JsonProperty("c8y_HealthMonitoring")
    C8Y_HealthMonitoring c8y_HealthMonitoring;
    @JsonProperty("c8y_HealthMonitoring")
    public C8Y_HealthMonitoring getC8yHealthMonitoring() {
        return c8y_HealthMonitoring;
    }
    @JsonProperty("c8y_HealthMonitoring")
    public void setC8yHealthMonitoring(C8Y_HealthMonitoring c8y_HealthMonitoring) {
        this.c8y_HealthMonitoring = c8y_HealthMonitoring;
    }
    
    @Override
    public String toString(){
        return "measurements:["+
                "{"+
                "c8y_HealthMonitoring="+c8y_HealthMonitoring+
                "}"+
                        "]";
    }
}
