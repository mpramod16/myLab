
package com.ahstores.app.umservices;

import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nChannelIterator;
import com.pcbsys.nirvana.client.nChannelNotFoundException;
import com.pcbsys.nirvana.client.nConsumeEvent;
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
import java.io.PrintStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ChannelSubscriber
implements nEventListener {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    nChannelIterator iterator = null;
    nSession session = null;

    public void subscribe(String umURL, String topicName) throws Exception {
        this.session = this.connect(umURL);
        this.LOG.info("Created session");
        nChannelAttributes cattrib = new nChannelAttributes();
        cattrib.setName(topicName);
        nChannel myChannel = this.session.findChannel(cattrib);
        this.LOG.info("Found Channel " + topicName);
        this.iterator = myChannel.createIterator(0L);
        this.LOG.info("Created Iterator for subscription");
        this.start();
//        nNamedObject nobj = myChannel.createNamedObject("channelSubscriber", 0, true);
//        myChannel.addSubscriber(this , nobj);
    }

    public void start() throws nSecurityException, nChannelNotFoundException, nSessionNotConnectedException, nSessionPausedException, nRealmNotFoundException, nUnexpectedResponseException, nSelectorParserException {
        while (!this.iterator.getNext().isEndOfChannel()) {
            nConsumeEvent event = this.iterator.getNext();
            this.go(event);
        }
    }
    
    
    public void go(nConsumeEvent event) {
        this.LOG.info("Consumed topic event >>>" + event.getEventID());
        System.out.println("Consumed topic event >>>" + event.getEventID());
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

