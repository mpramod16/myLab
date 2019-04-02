package com.ahstores.app.umservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nEventListener;
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nQueueReaderContext;
import com.pcbsys.nirvana.client.nQueueSyncReader;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;

public class QSubscriber implements nEventListener {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	 nQueueSyncReader reader = null;
	 nQueue myQueue = null;
	 nSession session = null;

	 public void mySyncQueueReader(String umURL, String queueName) throws Exception {

		this.session = this.connect(umURL);
        this.LOG.info("Created session");
        nChannelAttributes cattrib = new nChannelAttributes();
        cattrib.setName(queueName);
         myQueue = this.session.findQueue(cattrib);
        this.LOG.info("Found queue " + queueName);
        

	 nQueueReaderContext ctx = new
	 nQueueReaderContext(this, 10);

	 reader = myQueue.createReader(ctx);


	 }

	 public void start() throws Exception {
	 while (!reader.equals(null)) {
	 // pop events from the queue

	 nConsumeEvent event = reader.pop();

	 go(event);
	 }
	 }

	 public void go(nConsumeEvent event) {
	 System.out.println("Consumed event "+event.getEventID());
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
