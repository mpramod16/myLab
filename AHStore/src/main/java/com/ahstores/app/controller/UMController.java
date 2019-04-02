/**
 * 
 */
package com.ahstores.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahstores.app.model.SchedulerConfig;
import com.ahstores.app.repository.SchedulerConfigRepository;
import com.ahstores.app.service.SchedulerConfigService;
import com.ahstores.app.umservices.ChannelPub;
import com.ahstores.app.umservices.ChannelSubscriber;
import com.ahstores.app.umservices.QPub;
import com.ahstores.app.umservices.QSubscriber;




/**
 * @author Pramod M
 *
 */
@RestController
//@RequestMapping(value = "/um")
public class UMController {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	@Autowired
    private SchedulerConfigService schedulerConfigService;
	private ChannelPub channel;
    private ChannelSubscriber channelSubscriber;
    private QPub qPub;
    private QSubscriber qSubscriber;
    @Value(value="${ahstore.app.umurl}")
    private String umURL;
    @Value(value="${ahstore.app.topicname}")
    private String topicname;
    @Value(value="${ahstore.app.queuename}")
    private String queuename;

    @RequestMapping(value={"/sendMessageQ"})
    public String publishQ() {
        this.LOG.info("Sending Message to UMQ");
        this.qPub = new QPub();
        try {
            this.qPub.sendMessage(this.umURL, this.queuename);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.LOG.error(e.getMessage());
        }
        this.LOG.info("published message successfully");
        return "WELCOME HOME";
    }

    @RequestMapping(value={"/sendMessage"})
    public String publishChannel() {
        this.LOG.info("Sending Message to UM");
        this.channel = new ChannelPub();
        try {
            this.channel.sendMessage(this.umURL, this.topicname);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.LOG.error(e.getMessage());
        }
        this.LOG.info("published messages successfully");
        return "WELCOME HOME";
    }

    @RequestMapping(value={"/receiveMessage"})
    public String subscribeChannel() {
        this.LOG.info("getting messages from UM");
        try {
            this.channelSubscriber = new ChannelSubscriber();
            this.channelSubscriber.subscribe(this.umURL, this.topicname);
            this.LOG.info("Consuming Messages now>>");
        }
        catch (Exception e) {
            e.printStackTrace();
            this.LOG.error(e.getMessage());
        }
        this.LOG.info("Consumed messages successfully");
        return "Check Console for messages";
    }

    @RequestMapping(value={"/receiveMessageQ"})
    public String subscribeQ() {
        this.LOG.info("receiving Message from UMQ");
        this.qSubscriber = new QSubscriber();
        try {
            this.qSubscriber.mySyncQueueReader(this.umURL, this.queuename);
            this.qSubscriber.start();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.LOG.error(e.getMessage());
        }
        this.LOG.info("consumed message successfully");
        return "Check Console for messages";
    }
	
	@RequestMapping(value = "/getConfig", method = RequestMethod.GET)
	public String getConfigAll() {
		LOG.info("Getting Scheduler Config..");
		String message="Getting Scheduler Config..IsEmpty()?"+schedulerConfigService.getAllSchedulerConfig().isEmpty();
		LOG.info(message);
		return message;
		//return schedulerConfigService.getAllSchedulerConfig();
	}
	/*

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

	@RequestMapping(value = "/settings/{userId}", method = RequestMethod.GET)
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

