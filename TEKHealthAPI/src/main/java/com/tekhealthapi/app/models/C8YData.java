/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tekhealthapi.app.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/**
 *
 * @author pmadishe
 */
@Document
@JsonIgnoreProperties(ignoreUnknown =  true)
public class C8YData {
   @JsonProperty("measurements")
   private List<Measurements> measurements;
    
   @JsonProperty("measurements")
public List<Measurements> getMeasurements() {
return measurements;
}

@JsonProperty("measurements")
public void setMeasurements(List<Measurements> measurements) {
this.measurements = measurements;
}
@Override
 public String toString() {
     return "{"+
             measurements.toString()+
             "}";
}
}
