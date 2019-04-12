package com.ahstores.app.service;

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
		this.publish(message,uuid.toString(), myChannel);
		LOG.info("published message >>> EventId: "+myChannel.getLastEID());
		this.disconnect(session);
		return "Published Message Successfully";

	}

	public nChannel getChannel(nSession session, String channelName) throws Exception {
		nChannelAttributes cattrib = new nChannelAttributes();
		cattrib.setName(channelName);
		nChannel channel = session.findChannel(cattrib);
                System.out.println("Topic Retrieved: " + channel.getName());
		return channel;
	}

	public void publish(String message, String tag, nAbstractChannel channelOrTopic) throws Exception {
		nTransactionAttributes tattrib = new nTransactionAttributes(channelOrTopic);
		nTransaction myTransaction = nTransactionFactory.create(tattrib);
		nConsumeEvent evt = new nConsumeEvent(tag, message.getBytes());
		nEventAttributes evtAttrs = new nEventAttributes();
		evtAttrs.setMessageType((byte) 5);
		evt.setAttributes(evtAttrs);
                myTransaction.publish(evt);
		myTransaction.commit();
	}
	
	public nSession connect(String rnames) throws Exception {
		nSessionAttributes sessionAttributes = new nSessionAttributes(rnames);
		nSession connectionObject = nSessionFactory.create(sessionAttributes);
		// connectionObject.enableThreading(10);
		connectionObject.init();
		return connectionObject;
	}

	public void disconnect(nSession connectionObject) {
		connectionObject.close();
		connectionObject = null;
	}
}
