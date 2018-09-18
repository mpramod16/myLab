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
public class C8Y_HealthMonitoring {
    @JsonProperty("D")
    public D getD() {
        return d;
    }
    @JsonProperty("D")
    public void setD(D d) {
        this.d = d;
    }
    @JsonProperty("H")
    public H getH() {
        return h;
    }
    @JsonProperty("H")
    public void setH(H h) {
        this.h = h;
    }
    @JsonProperty("P")
    public P getP() {
        return p;
    }
    @JsonProperty("P")
    public void setP(P p) {
        this.p = p;
    }
    @JsonProperty("D")
    D d;
    @JsonProperty("H")
    H h;
    @JsonProperty("P")
    P p;

     @Override
    public String toString(){
        return  "{"+
                "D='"+d+'\''+
                ",H='"+h+'\''+
                ",P='"+p+'\''+
                '}';
    }
    
}
