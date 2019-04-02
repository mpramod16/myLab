package com.ahstores.app.util;

import java.util.List;

import org.hibernate.service.spi.InjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ahstores.app.model.MessageShipment_S;
import com.ahstores.app.repository.MessageShipmentSourceRepository;
import com.ahstores.app.service.MessageShipmentSourceService;
import com.ahstores.app.service.PublishToQueueService;
import com.ahstores.app.service.PublishToTopicService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Task implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(Task.class);
	
	String task;
	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	@Value(value="${ahstore.app.umurl}")
    private String umURL;
    @Value(value="${ahstore.app.topicname}")
    private String topicname;
    @Value(value="${ahstore.app.queuename}")
    private String queuename;
	@Autowired
	private PublishToQueueService queueService;
	@Autowired
	private PublishToTopicService topicService;
	@Autowired
	private MessageShipmentSourceService messageShipmentSourceService;
	
	public Task(String task){
		this.task=task;
	}
	
	public Task() {}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(task);
		LOG.info(task);
		
		List<MessageShipment_S> messageShipmentList=messageShipmentSourceService.getAllMessageShipmentSource();
		LOG.info("messageShipmentList is Empty>>>>>>"+messageShipmentList.isEmpty());
		for (MessageShipment_S messageShipment_S : messageShipmentList) {
			String message=Utility.getJsonFromObject(messageShipment_S);
			LOG.info("Message to be published>>\n"+message);
			try {
				//queueService.sendMessage(umURL, queuename, message);
				topicService.sendMessage(umURL, topicname, message);
				messageShipmentSourceService.deleteByMsgSeqNumber(messageShipment_S.getMsgSeqNumber());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOG.error("Error In the Task:"+e.getMessage());
			}
		}
		
	}
	
}

