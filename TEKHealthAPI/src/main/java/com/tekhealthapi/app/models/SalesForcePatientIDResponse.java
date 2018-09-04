/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tekhealthapi.app.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author skadhira
 */
@Document
@JsonIgnoreProperties(ignoreUnknown =  true)
public class SalesForcePatientIDResponse {

  
    
     @JsonProperty("sfPatientIDresponse")
     private List<SalesForceMRNumberResponse> sfPatientIDresponse;

    public List<SalesForceMRNumberResponse> getSfPatientIDresponse() {
        return sfPatientIDresponse;
    }

    public void setSfPatientIDresponse(List<SalesForceMRNumberResponse> sfPatientIDresponse) {
        this.sfPatientIDresponse = sfPatientIDresponse;
    }
     
    
}
