package com.ahstores.app.service;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahstores.app.model.MessageShipment_S;
import com.ahstores.app.model.MessageShipment_T;
import com.ahstores.app.util.Utility;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nEventListener;
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nQueueReaderContext;
import com.pcbsys.nirvana.client.nQueueSyncReader;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;
@Service
public class QueueListenerService implements nEventListener {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private MessageShipmentTargetService messageShipmentTargetService;
	nQueueSyncReader reader = null;
	nQueue myQueue = null;
	nSession session = null;
	boolean isRunning=false;

	 public void mySyncQueueReader(String umURL, String queueName) throws Exception {

		this.session = this.connect(umURL);
		this.LOG.info("Created session");
		nChannelAttributes cattrib = new nChannelAttributes();
		cattrib.setName(queueName);
		myQueue = this.session.findQueue(cattrib);
		this.LOG.info("Found queue " + queueName);
		nQueueReaderContext ctx = new nQueueReaderContext(this,10);
		reader = myQueue.createReader(ctx);
		LOG.info("reader.hasConsumedEvents()::::"+reader.hasConsumedEvents());
		
	 }
	 
	public void start() throws Exception {
		 while (!isRunning) {//isRunning==false
		 // pop events from the queue
			 isRunning=true;
			 nConsumeEvent event = reader.pop();
			 LOG.info("event>>>"+event.toString());
			 go(event);
			 isRunning=false;
		 }
	 }
	
	public void go(nConsumeEvent event) {
		 	System.out.println("Consumed event>>>> "+event.getEventTag());
		 	String message= new String(event.getEventData());
		 	LOG.info("Message consumed>>>>"+message);
		 	MessageShipment_S messageShipment=Utility.getObjectFromJson(message, MessageShipment_S.class);
		 	LOG.info("MessageShipment_S"+messageShipment.getShipmentId());
		 	MessageShipment_T targetMessage=new MessageShipment_T();
		 	targetMessage.setGlnBuyer(messageShipment.getGlnBuyer());
		 	targetMessage.setGlnConsignee(messageShipment.getGlnConsignee());
		 	targetMessage.setGlnConsignor(messageShipment.getGlnConsignor());
		 	targetMessage.setGlnDeliveryPoint(messageShipment.getGlnDeliveryPoint());
		 	targetMessage.setGlnSupplier(messageShipment.getGlnSupplier());
		 	targetMessage.setIndBackHaul(messageShipment.getIndBackHaul());
		 	targetMessage.setInternalWareHouseNo(messageShipment.getInternalWareHouseNo());
		 	targetMessage.setMsgAction(messageShipment.getMsgAction());
		 	targetMessage.setMsgEventDateTime(messageShipment.getMsgEventDateTime());
		 	targetMessage.setMsgIndCompleted("Y");
		 	targetMessage.setMsgIndProcessed("Y");
		 	targetMessage.setMsgInsertedDateTime(new Date());
		 	targetMessage.setMsgSeqNumber(messageShipment.getMsgSeqNumber());
		 	targetMessage.setOrderId(messageShipment.getOrderId());
		 	targetMessage.setShipmentId(messageShipment.getShipmentId());
		 	messageShipmentTargetService.add(targetMessage);
		 }
		 
	 public nSession connect(String rnames) throws Exception {
	        nSessionAttributes sessionAttributes = new nSessionAttributes(rnames);
	        nSession connectionObject = nSessionFactory.create((nSessionAttributes)sessionAttributes);
	        connectionObject.init();
	        return connectionObject;
	 }
	
	public void disconnect(nSession connectionObject) {
	    connectionObject.close();
	    connectionObject = null;
	}
}
