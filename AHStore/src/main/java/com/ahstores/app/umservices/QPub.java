
package com.ahstores.app.umservices;

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
import java.io.PrintStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QPub {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private int i = 1;

    public String sendMessage(String umURL, String queuename) throws Exception {
        nSession session = this.connect(umURL);
        nChannelAttributes cattrib = new nChannelAttributes();
        cattrib.setName(queuename);
        nQueue myQueue = session.findQueue(cattrib);
        
        this.publish("this is " + this.i + " message in queue", "" + this.i++, (nAbstractChannel)myQueue);
        return "Welcome to Universal Messaging";
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

