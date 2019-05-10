package com.ahstores.app.service;

import com.ahstores.app.model.MessageShipment_S;
import com.ahstores.app.util.UMReconnectHandler;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pcbsys.nirvana.client.nAbstractChannel;
import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nEventAttributes;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;
import com.pcbsys.nirvana.client.nTransaction;
import com.pcbsys.nirvana.client.nTransactionAttributes;
import com.pcbsys.nirvana.client.nTransactionFactory;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

@Service
public class PublishToTopicService {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
public String sendMessage(String umURL,String topicname,String message) throws Exception {
		
		LOG.info("SendMessage in PublishToTopic>>");
		nSession session = this.connect(umURL);
		//LOG.info("obtained session for UM");
		nChannel myChannel = this.getChannel(session, topicname);
		//LOG.info("Got the channel>>>");
		UUID uuid = UUID.randomUUID();
		this.publishMessageAsJMSString(message,uuid.toString(), myChannel);
		LOG.info("published message >>> EventId: "+myChannel.getLastEID());
		this.disconnect(session);
		return "Published Message Successfully";

	}


public String sendDocument(String umURL,String topicname,MessageShipment_S message) throws Exception {
		
                try {
                    // create an intermediateDoc IData object
                    IData intermediateDoc = IDataFactory.create();
                    // create a cursor to use to add key value tuples to the intermediateDoc
                    IDataCursor intermediateCursor = intermediateDoc.getCursor();
                    // add key value tuples as required to the intermediateDoc
                    IDataUtil.put(intermediateCursor, "key1", "value1");
                    IDataUtil.put(intermediateCursor, "key2", "value2");
                    // ...
                    // destroy the intermediateCursor when done adding key value tuples
                    intermediateCursor.destroy();

                    // create an outputDoc IData object
                    IData outputDoc = IDataFactory.create();
                    // create a cursor to use to add key value tuples to the outputDoc
                    IDataCursor outputCursor = outputDoc.getCursor();
                    // add the intermediateDoc to the outputDoc
                    IDataUtil.put(outputCursor, "intermediateDoc", intermediateDoc);
                    
                    // destroy the outputCursor when done adding key value tuples
                    outputCursor.destroy();
                            
                   } finally {
                   
                  }
                
		LOG.info("SendMessage in PublishToTopic>>");
		nSession session = this.connect(umURL);
		//LOG.info("obtained session for UM");
		nChannel myChannel = this.getChannel(session, topicname);
		//LOG.info("Got the channel>>>");
		UUID uuid = UUID.randomUUID();
//		this.publish(message,uuid.toString(), myChannel);
//		LOG.info("published message >>> EventId: "+myChannel.getLastEID());
//		this.disconnect(session);
//		return "Published Message Successfully";
    return "";
}


	public nChannel getChannel(nSession session, String channelName) throws Exception {
		nChannelAttributes cattrib = new nChannelAttributes();
		cattrib.setName(channelName);
		nChannel channel = session.findChannel(cattrib);
                System.out.println("Topic Retrieved: " + channel.getName());
		return channel;
	}

	public void publishMessageAsJMSString(String message, String tag, nAbstractChannel channelOrTopic) throws Exception {
		nTransactionAttributes tattrib = new nTransactionAttributes(channelOrTopic);
		nTransaction myTransaction = nTransactionFactory.create(tattrib);
		nConsumeEvent evt = new nConsumeEvent(tag, message.getBytes());
		nEventAttributes evtAttrs = new nEventAttributes();
		//evtAttrs.setMessageType((byte) 5);
                evtAttrs.setMessageType(nEventAttributes.JMS_TEXT_MESSAGE_TYPE);
                evt.setAttributes(evtAttrs);
                myTransaction.publish(evt);
		myTransaction.commit();
	}
        
        public void publishMessageAsJMSObject(String message, String tag, nAbstractChannel channelOrTopic) throws Exception {
		nTransactionAttributes tattrib = new nTransactionAttributes(channelOrTopic);
		nTransaction myTransaction = nTransactionFactory.create(tattrib);
		nConsumeEvent evt = new nConsumeEvent(tag, message.getBytes());
		nEventAttributes evtAttrs = new nEventAttributes();
		//evtAttrs.setMessageType((byte) 5);
                evtAttrs.setMessageType(nEventAttributes.JMS_OBJECT_MESSAGE_TYPE);
                evt.setAttributes(evtAttrs);
                myTransaction.publish(evt);
		myTransaction.commit();
	}
	
	public nSession connect(String rnames) throws Exception {
		nSessionAttributes sessionAttributes = new nSessionAttributes(rnames);
                UMReconnectHandler rhandler=new UMReconnectHandler();
		nSession connectionObject = nSessionFactory.create(sessionAttributes,rhandler);
                //Basic Authentication
                //nSession connectionObject = nSessionFactory.create(sessionAttributes, rhandler, "username", "password");
		// connectionObject.enableThreading(10);
		connectionObject.init();
		return connectionObject;
	}

	public void disconnect(nSession connectionObject) {
		connectionObject.close();
		connectionObject = null;
	}
}
