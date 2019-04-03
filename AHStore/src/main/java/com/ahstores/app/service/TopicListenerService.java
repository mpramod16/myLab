package com.ahstores.app.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahstores.app.model.MessageShipment_S;
import com.ahstores.app.model.MessageShipment_T;
import com.ahstores.app.util.Utility;
import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nChannelIterator;
import com.pcbsys.nirvana.client.nChannelNotFoundException;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nConsumeEventFragmentReader;
import com.pcbsys.nirvana.client.nEventListener;
import com.pcbsys.nirvana.client.nNamedObject;
import com.pcbsys.nirvana.client.nRealmNotFoundException;
import com.pcbsys.nirvana.client.nSecurityException;
import com.pcbsys.nirvana.client.nSelectorParserException;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;
import com.pcbsys.nirvana.client.nSessionNotConnectedException;
import com.pcbsys.nirvana.client.nSessionPausedException;
import com.pcbsys.nirvana.client.nUnexpectedResponseException;

@Service
public class TopicListenerService implements nEventListener {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageShipmentTargetService messageShipmentTargetService;
	nChannelIterator iterator = null;
    nSession session = null;
    boolean isRunning=false;
    
	public void mySyncTopicReader(String umURL, String topicName, String durableSubscriberName) throws Exception {
        this.session = this.connect(umURL);
        this.LOG.info("Created session");
        nChannelAttributes cattrib = new nChannelAttributes();
        cattrib.setName(topicName);
        nChannel myChannel = this.session.findChannel(cattrib);
        this.LOG.info("Found Channel " + topicName);
        //nNamedObject durableSubscriber = myChannel.createNamedObject("durableSubscriber", 0, true);
        nNamedObject durableSubscriber = myChannel.createSharedNamedObject(durableSubscriberName, true, false, 0);
        this.iterator = myChannel.createIterator(0L);
        this.LOG.info("Created durableSubscriber");
    }

    public void start() throws nSecurityException, nChannelNotFoundException, nSessionNotConnectedException, nSessionPausedException, nRealmNotFoundException, nUnexpectedResponseException, nSelectorParserException {
        while (!isRunning) {
        	isRunning=true;
            nConsumeEvent event = this.iterator.getNext();
            this.go(event);
            isRunning=false;
        }
    }
    
    @Override
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
