package com.ahstores.app.umservices;

import org.springframework.stereotype.Component;

import com.pcbsys.nirvana.client.*;

	
public class ChannelPubSub {
	
	public String sendMessage( String[] args){
		try {
			String RNAME = "nsp://hscsrv164.allegisgroup.com:9099";
			nSessionAttributes nsa=new nSessionAttributes(RNAME);
			nsa.setRequestPriorityConnection(false);
			nSession session=nSessionFactory.create(nsa);
			session.init();
			
			 System.out.println("Realm Connection URL: " + RNAME);
		     System.out.println("Connection URL List: " + session.getConnectionList());
		     System.out.println("Client Session connected successfully to Realm: " + session.getServerRealmName());
		     
		     
		     SendThread sendThread=new SendThread(session);
		     Thread thread= new Thread(sendThread);
		     thread.start();
		     System.out.println("Message sent");
		     
		     ReceiveThread receiveThread=new ReceiveThread();
		     Thread thread2= new Thread(receiveThread);
		     thread2.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "Published Message Successfully";
	}
	
	
	public static class SendThread implements Runnable {

		private static nSession sendSession=null;
		public SendThread(nSession session){
			sendSession=session;
		}
		public void run() {
			try {
				String channelName="TestChannel";
				nChannelAttributes cattrib = new nChannelAttributes();
				cattrib.setName(channelName);
				nChannel testChannel=sendSession.findChannel(cattrib);
				nConsumeEventFragmentWriter fragmentWriter=new nConsumeEventFragmentWriter(testChannel, 10240000);
				
				 nEventProperties props = new nEventProperties();
				  props.put("bondname", "bond1");
				  props.put("price", 100.00);
				  nConsumeEvent event = new nConsumeEvent( "atag", props, "Hello World".getBytes());
				  fragmentWriter.publish(event);

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}

}
	
	
	public static class ReceiveThread implements Runnable {

		
		public void run() {
			try {
				
				String RNAME = "nsp://hscsrv164.allegisgroup.com:9099";
				nSessionAttributes nsa=new nSessionAttributes(RNAME);
				nsa.setRequestPriorityConnection(false);
				nSession session=nSessionFactory.create(nsa);
				session.init();
				
				String channelName="TestChannel";
				nChannelAttributes cattrib = new nChannelAttributes();
				cattrib.setName(channelName);
				nChannel testChannel=session.findChannel(cattrib);
				
				
					nNamedObject durableSubsriber = testChannel.createNamedObject("channelSubscriber", 0, true);
					nConsumeEventFragmentReader reader=new nConsumeEventFragmentReader(new ClientListener());
					testChannel.addSubscriber(reader,durableSubsriber);
				
				

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}

}
	
	public static class ClientListener implements nEventListener {

		@Override
		public void go(nConsumeEvent arg0) {
			System.out.println("Received message "+arg0.toString());
			
		}
		
	}
}
