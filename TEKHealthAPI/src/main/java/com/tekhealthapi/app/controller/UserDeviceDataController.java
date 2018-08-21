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
import org.springframework.web.bind.annotation.*;

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

    public UserDeviceDataController(UserDeviceDataRepository userDeviceDataRepository) {
        this.userDeviceDataRepository = userDeviceDataRepository;
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserDeviceData> getAllUserDeviceData() {
        LOG.info("Getting all User Devices.");
        return userDeviceDataRepository.findAll();
    }


    @RequestMapping(value = "/{emailId}", method = RequestMethod.GET)
    public UserDeviceData getUserDeviceData(@PathVariable String emailId) {
        LOG.info("Getting userDeviceData with emailId: {}.", emailId);
        return userDeviceDataRepository.findOne(emailId);
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public UserDeviceData addUserDeviceData(@RequestBody UserDeviceData userDeviceData) {
        LOG.info("Saving userDeviceData.");
        return userDeviceDataRepository.save(userDeviceData);
    }

}
