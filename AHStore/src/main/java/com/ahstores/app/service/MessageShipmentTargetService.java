package com.ahstores.app.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ahstores.app.model.MessageShipment_S;
import com.ahstores.app.model.MessageShipment_T;
import com.ahstores.app.model.SchedulerConfig;
import com.ahstores.app.repository.MessageShipmentSourceRepository;
import com.ahstores.app.repository.MessageShipmentTargetRepository;
import com.ahstores.app.repository.SchedulerConfigRepository;


@Service
@Configurable
@Transactional
public class MessageShipmentTargetService {
	@Autowired
	MessageShipmentTargetRepository messageShipmentTargetRepository;
	
	public List<MessageShipment_T> getAllMessageShipmentTarget(){
		return messageShipmentTargetRepository.findAll();
	}
	
	public MessageShipment_T add(MessageShipment_T messageShipmentTarget) {
		return messageShipmentTargetRepository.saveAndFlush(messageShipmentTarget);	}
}
