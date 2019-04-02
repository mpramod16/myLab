package com.ahstores.app.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ahstores.app.model.MessageShipment_S;
import com.ahstores.app.model.MessageShipment_T;
import com.ahstores.app.model.SchedulerConfig;
import com.ahstores.app.repository.MessageShipmentSourceRepository;
import com.ahstores.app.repository.SchedulerConfigRepository;


@Service
@Configurable
@Transactional
public class MessageShipmentSourceService {
	@Autowired
	MessageShipmentSourceRepository messageShipmentSourceRepository;
	
	public List<MessageShipment_S> getAllMessageShipmentSource(){
		return messageShipmentSourceRepository.findAll();
	}
	
	public void deleteByMsgSeqNumber(Long msgSeqNumber){
		messageShipmentSourceRepository.removeByMsgSeqNumber(msgSeqNumber);
	}
	
	
}
