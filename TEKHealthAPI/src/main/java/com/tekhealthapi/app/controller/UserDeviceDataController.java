/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tekhealthapi.app.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.tekhealthapi.app.models.UserDeviceDataRepository;
import com.tekhealthapi.app.models.UserDeviceData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 *
 * @author Pramod M
 */



@RestController
@RequestMapping(value = "/devicedata")
public class UserDeviceDataController {

    @Autowired
    MongoTemplate mongoTemplate;

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private final UserDeviceDataRepository userDeviceDataRepository;
    public UserDeviceDataController(UserDeviceDataRepository userDeviceDataRepository) {
        this.userDeviceDataRepository = userDeviceDataRepository;
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserDeviceData> getAllUserDeviceData() {
        LOG.info("Getting all User Devices.");
        return userDeviceDataRepository.findAll();
    }


    @RequestMapping(value = "/{patientId}", method = RequestMethod.GET)
    public UserDeviceData getUserDeviceData(@PathVariable String patientId) {
        LOG.info("Getting userDeviceData with patientId: {}.", patientId);
        return userDeviceDataRepository.findOne(patientId);
    }

    @RequestMapping(
            value = "/patientIdAndDates",
            params = { "patientId", "fromDate", "toDate" }, method = RequestMethod.GET)
    public List<UserDeviceData> findByPatientIdCreatedDates(@RequestParam("patientId") String patientId,@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
        LOG.info("Getting userDeviceData with patientId: {}.", patientId);
        LOG.info("Getting userDeviceData with fromDate: {}.", fromDate);
        LOG.info("Getting userDeviceData with toDate: {}.", toDate);
        Date frDate=new Date();
        Date tooDate=new Date();
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dtFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        try{
             frDate = dtFormat.parse(fromDate);
             tooDate = dtFormat.parse(toDate);
        }catch(ParseException e){
            e.printStackTrace();
        }

        Query query=new Query(
                Criteria.where("patientId").regex(patientId, "i")
                .andOperator(
                        Criteria.where("createdTimestamp").lt(tooDate),
                        Criteria.where("createdTimestamp").gte(frDate)
                )

        );
        System.out.println("Query>>>>>>"+query.toString());
        return mongoTemplate.find(query, UserDeviceData.class);

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public UserDeviceData addUserDeviceData(@RequestBody UserDeviceData userDeviceData) {
        LOG.info("Saving userDeviceData.");
        return userDeviceDataRepository.save(userDeviceData);
    }

}
