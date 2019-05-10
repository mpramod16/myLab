/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahstores.app.util;

import com.pcbsys.nirvana.client.nReconnectHandler;
import com.pcbsys.nirvana.client.nSession;

/**
 *
 * @author pmadishe
 */
public class UMReconnectHandler implements nReconnectHandler{

    int noOfRetries=3,currentRetries=0;
        
    @Override
    public void disconnected(nSession nsn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reconnected(nSession nsn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean tryAgain(nSession nsn) {
      currentRetries++;
      return (currentRetries<noOfRetries)?true:false;
      
    }
    
}
