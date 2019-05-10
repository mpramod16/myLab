package com.ahstores.app.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahstores.app.model.MessageShipment_S;
import com.ahstores.app.model.MessageShipment_T;
import com.ahstores.app.util.UMReconnectHandler;
import com.ahstores.app.util.Utility;
import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nChannelIterator;
import com.pcbsys.nirvana.client.nChannelNotFoundException;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nConsumeEventFragmentReader;
import com.pcbsys.nirvana.client.nDurable;
import com.pcbsys.nirvana.client.nDurableAttributes;
import com.pcbsys.nirvana.client.nEventListener;
import com.pcbsys.nirvana.client.nNameDoesNotExistException;
import com.pcbsys.nirvana.client.nNamedObject;
import com.pcbsys.nirvana.client.nRealmNotFoundException;
import com.pcbsys.nirvana.client.nRequestTimedOutException;
import com.pcbsys.nirvana.client.nSecurityException;
import com.pcbsys.nirvana.client.nSelectorParserException;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;
import com.pcbsys.nirvana.client.nSessionNotConnectedException;
import com.pcbsys.nirvana.client.nSessionPausedException;
import com.pcbsys.nirvana.client.nUnexpectedResponseException;
import java.util.logging.Level;

@Service
public class TopicListenerService implements nEventListener {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageShipmentTargetService messageShipmentTargetService;
	nChannelIterator iterator = null;
        nSession session = null;
        boolean isRunning=false;
        nDurable shared = null;
    
    public void mySyncTopicReader(String umURL, String topicName, String durableSubscriberName, boolean isPersistent,boolean isClustered) {
        
        nChannel myChannel=null;
        
        try {
            this.session = this.connect(umURL);
            this.LOG.info("Created session");
            nChannelAttributes cattrib = new nChannelAttributes();
            cattrib.setName(topicName);
            myChannel = this.session.findChannel(cattrib);
                        
            nDurableAttributes.nDurableType type = nDurableAttributes.nDurableType.Shared;
            nDurableAttributes attr = nDurableAttributes.create(type, durableSubscriberName);
                     attr.setPersistent(isPersistent);
                     attr.setClustered(isClustered);
                     attr.setStartEID(0);
                     
            try {     
                shared = myChannel.getDurableManager().get(attr.getName()); 
                LOG.info("Using Existing Shared Durable Topic >>>>>");
            } catch (nNameDoesNotExistException ex) {     
                shared = myChannel.getDurableManager().add(attr); 
                LOG.info("Created Shared Durable Topic >>>>>");
            }
            this.iterator= myChannel.createIterator(shared,null,nChannelIterator.INFINITE_WINDOW,false);
            LOG.info("Got durableSubscriber::"+shared.getName());
          /*   ** removed this code with new one above.
                if(myChannel.getNamedObjects().length > 0){
                      //durableSubscriber=myChannel.getSharedNamedObject(durableSubscriberName);
                      shared=myChannel.getDurableManager().get(durableSubscriberName);
                      this.iterator = myChannel.createIterator(0L,false);
                       this.LOG.info("Fetch durableSubscriber::"+shared.getName());
                }else {
//                     durableSubscriber = myChannel.createSharedNamedObject(durableSubscriberName, true, false, 0);
//                     this.iterator = myChannel.createIterator(0L);
//          ***** Do not enable above code in for 10.3 UM.
                        //Create Shared Durable in 10.3 UM
                     nDurableAttributes.nDurableType type = nDurableAttributes.nDurableType.Shared;
                    nDurableAttributes attr = nDurableAttributes.create(type, durableSubscriberName);
                     attr.setPersistent(true);
                     attr.setClustered(false);
                     attr.setStartEID(0);
                    shared = myChannel.getDurableManager().add(attr);
                     this.iterator = myChannel.createIterator(0L,false);
                     this.LOG.info("Created durableSubscriber::"+shared.getName());
                 }            
            */ 
            } catch(Exception e){
               LOG.error(e.getMessage()); 
            }
        
        
    }

    public void start() throws nSecurityException, nChannelNotFoundException, nSessionNotConnectedException, nSessionPausedException, nRealmNotFoundException, nUnexpectedResponseException, nSelectorParserException, nRequestTimedOutException {
        nConsumeEvent event=null;
        
            while (!isRunning) {
                isRunning=true;
                event = this.iterator.getNext();
                this.go(event);
                iterator.ack(event.getEventID());
                LOG.info("Sent Acknowledgement for"+event.getEventID());
                isRunning=false;
               
        }
    }
    
    @Override
	public void go(nConsumeEvent event) {
            try {
                System.out.println("Consumed event>>>> "+event.getEventID());
	 	String message= new String(event.getEventData());
	 	//LOG.info("Message consumed>>>>"+message);
	 	MessageShipment_S messageShipment=Utility.getObjectFromJson(message, MessageShipment_S.class);
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
                
            } catch (Exception e) {
                LOG.error(e.getMessage());
                System.exit(1);
            }
                
                
	}
    
    
    public nSession connect(String rnames) throws Exception {
        nSessionAttributes sessionAttributes = new nSessionAttributes(rnames);
        UMReconnectHandler rhandler=new UMReconnectHandler(); 
        nSession connectionObject = nSessionFactory.create((nSessionAttributes)sessionAttributes);
        connectionObject.init();
        return connectionObject;
    }

    public void disconnect(nSession connectionObject) {
        connectionObject.close();
        connectionObject = null;
    }

}
