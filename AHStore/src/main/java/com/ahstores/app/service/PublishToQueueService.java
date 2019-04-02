package com.ahstores.app.service;

import java.util.UUID;

import org.hibernate.id.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcbsys.nirvana.client.nAbstractChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nEventAttributes;
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;
import com.pcbsys.nirvana.client.nTransaction;
import com.pcbsys.nirvana.client.nTransactionAttributes;
import com.pcbsys.nirvana.client.nTransactionFactory;

@Service
public class PublishToQueueService {

	 private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	 
	 

	public String sendMessage(String umURL, String queuename, String message) throws Exception {
	    nSession session = this.connect(umURL);
	    nChannelAttributes cattrib = new nChannelAttributes();
	    cattrib.setName(queuename);
	    nQueue myQueue = session.findQueue(cattrib);
	    UUID uuid = UUID.randomUUID();
	    this.publish(message, uuid.toString(), (nAbstractChannel)myQueue);
	    LOG.info("Published Message to Q successfully!!");
	    this.disconnect(session);
	    return "Published Message to Q successfully!!";
	}
	    
	    
	public void publish(String message, String tag, nAbstractChannel channelOrTopic) throws Exception {
        nTransactionAttributes tattrib = new nTransactionAttributes(channelOrTopic);
        nTransaction myTransaction = nTransactionFactory.create((nTransactionAttributes)tattrib);
        nConsumeEvent evt = new nConsumeEvent(tag, message.getBytes());
        nEventAttributes evtAttrs = new nEventAttributes();
        evtAttrs.setMessageType((byte)5);
        evt.setAttributes(evtAttrs);
        myTransaction.publish(evt);
        myTransaction.commit();
        System.out.println("Published Message with tag: " + tag);
        LOG.info("Published Message with tag: " + tag);
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
