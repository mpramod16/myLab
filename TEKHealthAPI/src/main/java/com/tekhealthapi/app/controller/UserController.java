/**
 * 
 */
package com.tekhealthapi.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tekhealthapi.app.models.User;
import com.tekhealthapi.app.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Pramod M
 *
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
        private final UserRepository userRepository;

	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<User> getAllUsers() {
		LOG.info("Getting all users.");
		return userRepository.findAll();
	}


	@RequestMapping(value = "/{emailId}", method = RequestMethod.GET)
	public User getUser(@PathVariable String emailId) {
		LOG.info("Getting user with ID: {}.", emailId);
		return userRepository.findOne(emailId);
	}


	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public User addNewUsers(@RequestBody User user) {
		LOG.info("Saving user.");
		return userRepository.save(user);
	}
	  
        @RequestMapping(method=RequestMethod.PUT,value="/update/{emailId}")
	public User updateUser(@RequestBody User user, @PathVariable String emailId) {
                
		if(userRepository.findOne(emailId)!= null){// emailId is primaryKey hence should nt be able to update it.
		LOG.info("updating user."+user.getFirstName());
		return userRepository.save(user);
                }
                else {
                    LOG.info("Invalid EmailId.");
                    return null;
                }
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

