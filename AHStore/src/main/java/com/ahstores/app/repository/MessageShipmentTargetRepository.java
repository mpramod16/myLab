package com.ahstores.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ahstores.app.model.MessageShipment_S;
import com.ahstores.app.model.MessageShipment_T;



public interface MessageShipmentTargetRepository extends JpaRepository<MessageShipment_T, String> {
	
}
