/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tekhealthapi.app.controller;

import com.tekhealthapi.app.models.UserDeviceDataRepository;
import com.tekhealthapi.app.models.UserDeviceData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Pramod M
 */
@RestController
@RequestMapping(value = "/devicedata")
public class UserDeviceDataController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private final UserDeviceDataRepository userDeviceDataRepository;
    private ExampleMatcher exampleMatcher;
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
    @Query("{'patientId':?2,'createdTimestamp': { $lte: new Date(?0), $gte : ISODate(?1)}}")
    public List<UserDeviceData> findByCreationTimestampBetweenAndPatientId(@RequestParam("fromDate") Date fromDate,@RequestParam("toDate") Date toDate,@RequestParam("patientId") String patientId) {
        LOG.info("Getting userDeviceData with patientId: {}.", patientId);
        LOG.info("Getting userDeviceData with fromDate: {}.", fromDate);
        LOG.info("Getting userDeviceData with toDate: {}.", toDate);

       /* this.exampleMatcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withMatcher("patientId",match -> match.startsWith().stringMatcher( ExampleMatcher.StringMatcher.valueOf(patientId)))
                .withMatcher("createdTimestamp",match -> match.startsWith().stringMatcher(ExampleMatcher.StringMatcher.valueOf()))
                .withMatcher("createdTimestamp",match -> match.startsWith().stringMatcher(ExampleMatcher.StringMatcher.valueOf(toDate)));
       */
        return userDeviceDataRepository.findAll();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public UserDeviceData addUserDeviceData(@RequestBody UserDeviceData userDeviceData) {
        LOG.info("Saving userDeviceData.");
        return userDeviceDataRepository.save(userDeviceData);
    }

}
