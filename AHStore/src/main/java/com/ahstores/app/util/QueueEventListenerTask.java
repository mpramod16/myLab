package com.ahstores.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ahstores.app.service.QueueListenerService;
@Component
public class QueueEventListenerTask implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(QueueEventListenerTask.class);
	@Value(value="${ahstore.app.umurl}")
    private  String umURL;
    @Value(value="${ahstore.app.topicname}")
    private  String topicname;
    @Value(value="${ahstore.app.queuename}")
    private  String queuename;
    @Autowired
    private  QueueListenerService queueListenerService;
    
	@Override
	public void run() {
		LOG.info("Scheduled Listener has just started up>>>");
	    try {
			queueListenerService.mySyncQueueReader(umURL, queuename);
			queueListenerService.start();
			LOG.info("Scheduled Listener has kicked up QueueListeners>>>");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Error in the EventListenerTask:"+e.getMessage());
		}
	}

}
