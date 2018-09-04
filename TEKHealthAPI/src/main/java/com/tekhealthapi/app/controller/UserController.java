/**
 * 
 */
package com.tekhealthapi.app.controller;

import com.tekhealthapi.app.models.SalesForceMRNumberResponse;
import com.tekhealthapi.app.models.SalesForcePatientIDResponse;
import com.tekhealthapi.app.models.SalesForceTokenResponse;
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
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
        
        @RequestMapping(value = "/getPatientID/{mrNumber}", method = RequestMethod.GET)
	public SalesForceMRNumberResponse getPatientID(@PathVariable String mrNumber) {
		LOG.info("Fetching PatientId from SalesForce.");
                final String SALESFORCETOKEN_URL ="https://teksystemshealthcare.my.salesforce.com/services/oauth2/token?grant_type=password&client_id=3MVG9YDQS5WtC11qc1AI9.6dtMUtSKMiDu7IC7E4zcjtL.OziW599N056Cbd6uyBHX0MylXzNQLdYo3AKZc3H&client_secret=6796271916356064713&username=teksystemshealthcare@gmail.com&password=Abcd1234";
        
            System.out.println("Getting Token--------");
            RestTemplate restTemplateForTken = new RestTemplate();
            SalesForceTokenResponse tokenRes = restTemplateForTken.postForObject(SALESFORCETOKEN_URL, null, SalesForceTokenResponse.class);//.getForObject(SALESFORCETOKEN_URL, SalesForceTokenResponse.class);
            System.out.println("Token--------"+tokenRes.getAccess_token());
            restTemplateForTken = null;

            final String SALESFORCEPATIENT_URL = "https://teksystemshealthcare.my.salesforce.com/services/apexrest/TekSystems_PatientIdByMRN";
            RestTemplate restTemplateForPatientInfo = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", "Bearer "+tokenRes.getAccess_token());
            headers.setContentType(MediaType.APPLICATION_JSON);
           // headers.set("Content-Type", "application/json");
            Map<String, String> params = new HashMap<String, String>();
            params.put("medicalRecordNumber", mrNumber);
            HttpEntity request = new HttpEntity(params, headers);
            SalesForceMRNumberResponse[] response;
            response = restTemplateForPatientInfo.
                    postForObject(SALESFORCEPATIENT_URL, request, SalesForceMRNumberResponse[].class);
            System.out.println("response----"+response[0].getId());
		return response[0];
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

