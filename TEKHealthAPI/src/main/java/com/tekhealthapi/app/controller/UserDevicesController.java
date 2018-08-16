/**
 * 
 */
package com.tekhealthapi.app.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tekhealthapi.app.models.UserDevices;
import com.tekhealthapi.app.models.UserDevicesRepository;

/**
 * @author Pramod M
 *
 */
@RestController
@RequestMapping(value = "/devices")
public class UserDevicesController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	private final UserDevicesRepository userDevicesRepository;

	public UserDevicesController(UserDevicesRepository userDevicesRepository) {
		this.userDevicesRepository = userDevicesRepository;
	}
	

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<UserDevices> getAllUserDevices() {
		LOG.info("Getting all User Devices.");
		return userDevicesRepository.findAll();
	}


	@RequestMapping(value = "/{emailId}", method = RequestMethod.GET)
	public UserDevices getUserDevice(@PathVariable String emailId) {
		LOG.info("Getting userDevice with emailId: {}.", emailId);
		return userDevicesRepository.findOne(emailId);
	}


	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public UserDevices addNewUserDevice(@RequestBody UserDevices userDevice) {
		LOG.info("Saving userDevice.");
		return userDevicesRepository.save(userDevice);
	}
	

        
	/*@RequestMapping(value = "/settings/{userId}", method = RequestMethod.GET)
	public Object getAllUserSettings(@PathVariable String userId) {
		User user = userRepository.findOne(userId);
		if (user != null) {
			return user.getUserSettings();
		} else {
			return "User not found.";
		}
	}
	

	@RequestMapping(value = "/settings/{userId}/{key}", method = RequestMethod.GET)
	public String getUserSetting(@PathVariable String userId, @PathVariable String key) {
		User user = userRepository.findOne(userId);
		if (user != null) {
			return user.getUserSettings().get(key);
		} else {
			return "User not found.";
		}
	}

	

	@RequestMapping(value = "/settings/{userId}/{key}/{value}", method = RequestMethod.GET)
	public String addUserSetting(@PathVariable String userId, @PathVariable String key, @PathVariable String value) {
		User user = userRepository.findOne(userId);
		if (user != null) {
			user.getUserSettings().put(key, value);
			userRepository.save(user);
			return "Key added";
		} else {
			return "User not found.";
		}
	}*/



}

