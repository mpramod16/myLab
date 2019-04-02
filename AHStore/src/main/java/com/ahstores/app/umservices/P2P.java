package com.ahstores.app.umservices;
import java.util.ArrayList;
import java.util.List;

import com.pcbsys.nirvana.client.nAbstractChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nEventAttributes;
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nQueueReaderContext;
import com.pcbsys.nirvana.client.nQueueSyncReader;
import com.pcbsys.nirvana.client.nQueueSyncTransactionReader;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;
import com.pcbsys.nirvana.client.nTransaction;
import com.pcbsys.nirvana.client.nTransactionAttributes;
import com.pcbsys.nirvana.client.nTransactionFactory;

public class P2P {
	public String sendMessage(String[] args) throws Exception {
		// TODO Auto-generated method stub

		/*
		 * UMChannelInspection self = new UMChannelInspection(); nSession
		 * session = self.connect("nsp://localhost:9000"); long size =
		 * self.findHiddenQueue("TestQueue",
		 * "IS_##umpubtest__trigger__TestEventTrigger", session);
		 * System.out.println(size); self.disconnect(session);
		 */

		P2P self = new P2P();
		nSession session = self.connect("nsp://hscsrv164.allegisgroup.com:9099");

		nQueue queue = self.getQueue(session, "TestQueue");

		// Load up 11 messages
		int count = 14;
		for (int i = 0; i < count; i++) {
			self.publish("MESSAGE" + Integer.toString(i), "TAG" + Integer.toString(i), queue);
		}

		Thread.sleep(3000);
		System.out.println("Published queue Size:" + queue.size());
		int batchsize = 3;
		System.out.println("Subscribing in batches of " + batchsize);
		self.subscribeToMessages(session, queue, batchsize);

		System.exit(0);
		return "Published Message Successfully";
	}

	public nQueue getQueue(nSession session, String queueName) throws Exception {
		nChannelAttributes cattrib = new nChannelAttributes();
		cattrib.setName(queueName);
		nQueue queue = session.findQueue(cattrib);
		System.out.println("Queue Retrieved: " + queue.getName());
		return queue;
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

		System.out.println("Published Message with tag: " + tag);
	}

	public void subscribeToMessages(nSession session, nQueue queue, int batch) throws Exception {
		nQueueSyncReader reader = queue.createTransactionalReader(new nQueueReaderContext());
		List<nConsumeEvent> eventArray = new ArrayList<nConsumeEvent>();

		int onBatch = 0;
		int popNoMessage = 0;
		while (true) {
			try {
				// System.out.println("About to Pop a message");
				nConsumeEvent evt = reader.pop(100);
				if (evt != null) {
					popNoMessage = 0;
					eventArray.add(evt);
					// eventArray[on]=evt;
					// on++;

					String tag = evt.getEventTag();
					System.out.println("Popped: " + tag + " into batch");
				} else {
					popNoMessage++;
				}

				// is there anything in the array to commit?
				// If we reach the batch size, commit
				if (eventArray.size() == batch) {
					onBatch++;
					System.out.println("Queue Size before doing anything with the batch:" + queue.size());

					System.out.println("Reached the batch size - commiting Events up to last ID: " + evt.getEventID()
							+ " Tag: " + evt.getEventTag());

					if (onBatch == 2) {

						System.out.println("Rolling back batch 2");

						// evtId to rollback to
						// last
						int pos = eventArray.size() - 1;
						// first
						// int pos = 0;

						long rollbackEventId = eventArray.get(pos).getEventID();
						String rollbackEventTag = eventArray.get(pos).getEventTag();

						System.out
								.println("Rollback Events up to ID: " + rollbackEventId + " Tag: " + rollbackEventTag);

						// Rollback
						((nQueueSyncTransactionReader) reader).rollback(rollbackEventId);

						eventArray.clear();
						Thread.sleep(1000);
						System.out.println("Queue Size After rollback:" + queue.size());
					} else {
						((nQueueSyncTransactionReader) reader).commit(evt.getEventID());
						eventArray.clear();
					}
					System.out.println("Queue Size After Commit:" + queue.size());
				} else {
					// if we've been around 10 times without a message and
					// there's something in the batch, commit anyway.
					if (popNoMessage > 10 && eventArray.size() > 0) {
						popNoMessage = 0;
						System.out.println(
								">> Been arround the no messages loop 10 times, check if there's anything in a batch to commit");

						// Get the last message in the list
						nConsumeEvent lastEvt = eventArray.get(eventArray.size() - 1);

						System.out.println("Commiting Events up to ID: " + lastEvt.getEventID() + " Tag: "
								+ lastEvt.getEventTag());
						((nQueueSyncTransactionReader) reader).commit(lastEvt.getEventID());
						eventArray.clear();
						new Thread().sleep(1000);
						System.out.println("Queue Size:" + queue.size());
					}

				}
			} catch (Exception e) {
				System.out.println("Exception in pop....exiting!");
				e.printStackTrace();
				break;
			}
		}

	}

	// public void connect(String host, String name, String clientId, String
	// clientGroup, String appName)
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
