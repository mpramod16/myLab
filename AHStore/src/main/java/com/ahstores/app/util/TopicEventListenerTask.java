package com.ahstores.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ahstores.app.service.QueueListenerService;
import com.ahstores.app.service.TopicListenerService;
@Component
public class TopicEventListenerTask implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(TopicEventListenerTask.class);
	@Value(value="${ahstore.app.umurl}")
    private  String umURL;
    @Value(value="${ahstore.app.topicname}")
    private  String topicname;
    @Value(value="${ahstore.app.queuename}")
    private  String queuename;
    @Value(value="${ahstore.app.durable.name}")
    private String durableSubscriberName;
    @Autowired
    private  TopicListenerService topicListenerService;
    
	@Override
	public void run() {
		LOG.info("Scheduled Listener has just started up>>>");
	    try {
	    	topicListenerService.mySyncTopicReader(umURL, topicname, durableSubscriberName);
	    	topicListenerService.start();
			LOG.info("Scheduled Listener has kicked up TopicListeners>>>");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Error in the TopicEventListenerTask:"+e.getMessage());
		}
	}

}
