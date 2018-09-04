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
 * @author skadhira
 */
@Document
@JsonIgnoreProperties(ignoreUnknown =  true)
public class SalesForceTokenResponse {
    
    @JsonProperty("access_token")
    private String access_token;
    @JsonProperty("instance_url")
    private String instance_url;
    @JsonProperty("id")
    private String id;
    @JsonProperty("token_type")
    private String token_type;
    @JsonProperty("issued_at")
    private String issued_at;
    @JsonProperty("signature")
    private String signature;

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setInstance_url(String instance_url) {
        this.instance_url = instance_url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public void setIssued_at(String issued_at) {
        this.issued_at = issued_at;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getInstance_url() {
        return instance_url;
    }

    public String getId() {
        return id;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getIssued_at() {
        return issued_at;
    }
   
    public String getSignature() {
        return signature;
    }
    
    
    
    
}
