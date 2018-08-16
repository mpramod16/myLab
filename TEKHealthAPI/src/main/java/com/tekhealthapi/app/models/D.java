/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tekhealthapi.app.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author pmadishe
 */
@Document
@JsonIgnoreProperties(ignoreUnknown =  true)
public class D {
    @JsonProperty("unit")
    String unit;
    @JsonProperty("value")
    String value;
    @JsonProperty("unit")
    public String getUnit() {
        return unit;
    }
    @JsonProperty("unit")
    public void setUnit(String unit) {
        this.unit = unit;
    }
    @JsonProperty("value")
    public String getValue() {
        return value;
    }
    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }
     @Override
    public String toString(){
        return "D:{"+
                "unit="+unit+
                "value="+value+
                "}";
    }
     
}
