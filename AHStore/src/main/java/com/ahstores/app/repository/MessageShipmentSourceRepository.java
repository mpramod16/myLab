package com.ahstores.app.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ahstores.app.model.MessageShipment_S;



public interface MessageShipmentSourceRepository extends JpaRepository<MessageShipment_S, String> {
	
    Long removeByMsgSeqNumber(Long msgSeqNumber);
}
